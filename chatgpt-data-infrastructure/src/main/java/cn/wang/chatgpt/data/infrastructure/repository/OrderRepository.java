package cn.wang.chatgpt.data.infrastructure.repository;

import cn.wang.chatgpt.data.domain.openai.repository.IOpenAiRepository;
import cn.wang.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import cn.wang.chatgpt.data.domain.order.model.entity.*;
import cn.wang.chatgpt.data.domain.order.model.valobj.OrderStatusVO;
import cn.wang.chatgpt.data.domain.order.model.valobj.PayStatusVO;
import cn.wang.chatgpt.data.domain.order.repository.IOrderRepository;
import cn.wang.chatgpt.data.infrastructure.dao.IOpenAIOrderDao;
import cn.wang.chatgpt.data.infrastructure.dao.IOpenAIProductDao;
import cn.wang.chatgpt.data.infrastructure.dao.IUserAccountDao;
import cn.wang.chatgpt.data.infrastructure.po.OpenAIOrderPO;
import cn.wang.chatgpt.data.infrastructure.po.OpenAIProductPO;
import cn.wang.chatgpt.data.infrastructure.po.UserAccountPO;
import cn.wang.chatgpt.data.types.enums.OpenAIProductEnableModel;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class OrderRepository implements IOrderRepository {

    @Resource
    private IOpenAIOrderDao openAIOrderDao;

    @Resource
    private IOpenAIProductDao openAIProductDao;

    @Resource
    private IUserAccountDao userAccountDao;
    @Override
    public UnpaidOrderEntity queryUnpaidOrder(ShopCartEntity shopCartEntity) {
        OpenAIOrderPO openAIOrderPOReq = new OpenAIOrderPO();
        openAIOrderPOReq.setOpenid(shopCartEntity.getOpenid());
        openAIOrderPOReq.setProductId(shopCartEntity.getProductId());
        OpenAIOrderPO openAIOrderPORes = openAIOrderDao.queryUnpaidOrder(openAIOrderPOReq);

        if(null == openAIOrderPORes) return null;

        return UnpaidOrderEntity.builder()
                              .openid(openAIOrderPORes.getOpenid())
                              .orderId(openAIOrderPORes.getOrderId())
                              .totalAmount(openAIOrderPORes.getTotalAmount())
                              .productName(openAIOrderPORes.getProductName())
                              .payUrl(openAIOrderPORes.getPayUrl())
                              .payStatus(PayStatusVO.get(openAIOrderPORes.getPayStatus()))
                              .build();

    }

    @Override
    public ProductEntity queryProduct(Integer productId) {
        OpenAIProductPO openAIProductPO =openAIProductDao.queryProductByProductId(productId);
        ProductEntity productEntity = ProductEntity.builder()
                .productId(openAIProductPO.getProductId())
                .productName(openAIProductPO.getProductName())
                .productDesc(openAIProductPO.getProductDesc())
                .quota(openAIProductPO.getQuota())
                .price(openAIProductPO.getPrice())
                .enable(OpenAIProductEnableModel.get(openAIProductPO.getIsEnabled()))
                .build();

        return productEntity;
    }

    @Override
    public void saveOrder(CreateOrderAggregate aggregate) {
        String openid = aggregate.getOpenid();
        ProductEntity productEntity =aggregate.getProduct();
        OrderEntity orderEntity =aggregate.getOrder();

        OpenAIOrderPO openAIOrderPO = OpenAIOrderPO.builder()
                .openid(openid)
                .productId(productEntity.getProductId())
                .productName(productEntity.getProductName())
                .productQuota(productEntity.getQuota())
                .orderId(orderEntity.getOrderId())
                .orderTime(orderEntity.getOrderTime())
                .totalAmount(orderEntity.getTotalAmount())
                .orderStatus(orderEntity.getOrderStatusVo().getCode())
                .payType(orderEntity.getPayTypeVo().getCode())
                .payStatus(PayStatusVO.WALL.getCode())
                .build();
        openAIOrderDao.insert(openAIOrderPO);
    }

    @Override
    public void updateOrderPayInfo(PayOrderEntity payorderEntity) {
        OpenAIOrderPO openAIOrderPO = OpenAIOrderPO.builder()
                .openid(payorderEntity.getOpenid())
                .orderId(payorderEntity.getOrderId())
                .payUrl(payorderEntity.getPayUrl())
                .payStatus(payorderEntity.getPayStatus().getCode())
                .build();
        openAIOrderDao.updateOrderPayInfo(openAIOrderPO);
    }

    @Override
    public boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime) {
        OpenAIOrderPO openAIOrderPO = OpenAIOrderPO.builder()
                .orderId(orderId)
                .payAmount(totalAmount)
                .payTime(payTime)
                .transactionId(transactionId)
                .build();
        int count = openAIOrderDao.changeOrderPaySuccess(openAIOrderPO);
        return count == 1;
    }

    @Override
    public CreateOrderAggregate queryOrder(String orderId) {
        OpenAIOrderPO openAIOrderPO = openAIOrderDao.queryOrder(orderId);

        ProductEntity product = ProductEntity.builder()
                .productId(openAIOrderPO.getProductId())
                .productName(openAIOrderPO.getProductName())
                .build();

        OrderEntity order = OrderEntity.builder()
                .orderId(openAIOrderPO.getOrderId())
                .orderTime(openAIOrderPO.getOrderTime())
                .orderStatusVo(OrderStatusVO.get(openAIOrderPO.getOrderStatus()))
                .totalAmount(openAIOrderPO.getTotalAmount())
                .build();

        CreateOrderAggregate createOrderAggregate = CreateOrderAggregate.builder()
                .order(order)
                .product(product)
                .openid(openAIOrderPO.getOpenid())
                .build();
        return  createOrderAggregate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class ,timeout = 350,propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void deliveGoods(String orderId) {
        OpenAIOrderPO openAIOrderPO = openAIOrderDao.queryOrder(orderId);

        //1变更发货状态
        int updateOrderStatusDeliverGoods = openAIOrderDao.updateOrderStatusDeliverGoods(orderId);
        if(1 == updateOrderStatusDeliverGoods)
            throw new RuntimeException("updateOrderStatusDeliverGoodsCount update count is not equal 1");

        // 2. 账户额度变更
        UserAccountPO userAccountPO = userAccountDao.queryUserAccount(openAIOrderPO.getOpenid());
        UserAccountPO userAccountPOReq = new UserAccountPO();
        userAccountPOReq.setOpenid(openAIOrderPO.getOpenid());
        userAccountPO.setTotalQuota(openAIOrderPO.getProductQuota());
        userAccountPOReq.setSurplusQuota(openAIOrderPO.getProductQuota());

        if(null != userAccountPO)
        {
            int addAccountQuotaCount =userAccountDao.addAccountQuota(userAccountPOReq);
            if (1 != addAccountQuotaCount) throw new RuntimeException("addAccountQuotaCount update count is not equal 1");
        }else{
            userAccountDao.insert(userAccountPOReq);
        }
    }

    @Override
    public List<String> queryReplenishmentOrder() {
        return openAIOrderDao.queryReplenishmentOrder();
    }

    @Override
    public List<String> queryNoPayNotifyOrder() {
        return openAIOrderDao.queryNoPayNotifyOrder();
    }

    @Override
    public List<String> queryTimeoutCloseOrderList() {
        return openAIOrderDao.queryTimeoutCloseOrderList();
    }

    @Override
    public boolean changeOrderClose(String orderId) {
        return false;
    }

    @Override
    public List<ProductEntity> queryProductList() {
        List<OpenAIProductPO> openAIProductPOList  = openAIProductDao.queryProductList();
        List<ProductEntity> productEntityList =new ArrayList<>();
        for (OpenAIProductPO openAIProductPO : openAIProductPOList) {
            ProductEntity productEntity = new ProductEntity();
            productEntity.setProductId(openAIProductPO.getProductId());
            productEntity.setProductName(openAIProductPO.getProductName());
            productEntity.setProductDesc(openAIProductPO.getProductDesc());
            productEntity.setQuota(openAIProductPO.getQuota());
            productEntity.setPrice(openAIProductPO.getPrice());
            productEntityList.add(productEntity);
        }
        return productEntityList;
    }
}
