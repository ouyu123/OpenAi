package cn.wang.chatgpt.data.domain.order.service.weixin;

import cn.wang.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import cn.wang.chatgpt.data.domain.order.model.entity.OrderEntity;
import cn.wang.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import cn.wang.chatgpt.data.domain.order.model.entity.ProductEntity;
import cn.wang.chatgpt.data.domain.order.model.valobj.OrderStatusVO;
import cn.wang.chatgpt.data.domain.order.model.valobj.PayStatusVO;
import cn.wang.chatgpt.data.domain.order.model.valobj.PayTypeVO;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;

import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 订单服务
 */
@Slf4j
@Service
public class OrderService extends AbstractOrderService {

    @Value("${wxpay.config.appid}")
    private String appid;
    @Value("${wxpay.config.mchid}")
    private String mchid;
    @Value("${wxpay.config.notify-url}")
    private String notifyUrl;
    @Autowired(required = false)
    private NativePayService payService;

    @Override
    protected OrderEntity doSaveOrder(String openid, ProductEntity productEntity) {
        OrderEntity orderEntity = new OrderEntity();
        //数据库有幂拦截，如果有重复的订单Id会报错主键冲突。如果是公司里一般会有专门的雪花算法UUID服务
        orderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        orderEntity.setOrderTime(new Date());
        orderEntity.setOrderStatusVo(OrderStatusVO.CREATE);
        orderEntity.setTotalAmount(productEntity.getPrice());
        orderEntity.setPayTypeVo(PayTypeVO.WEIXIN_NATIVE);

        //聚类信息
        CreateOrderAggregate aggregate = CreateOrderAggregate.builder()
                .openid(openid).product(productEntity)
                .order(orderEntity).build();

        //保存订单：订单和支付，是两个操作
        // 一个是数据库操作，一个是HTTP操作。所以不能一个事务处理，只能先保存订单再操作创建支付单，如果失败则需要任务补偿

        orderRepository.saveOrder(aggregate);
        return orderEntity;
    }

    @Override
    protected PayOrderEntity doPrepayOrder(String openid, String orderId, String productName, BigDecimal amountTotal) {
        PrepayRequest request = new PrepayRequest();
        Amount amount =new Amount();
        amount.setTotal(amountTotal.multiply(new BigDecimal(100)).intValue());
        request.setAmount(amount);
        request.setAppid(appid);
        request.setMchid(mchid);
        request.setDescription(productName);
        request.setNotifyUrl(notifyUrl);
        request.setOutTradeNo(orderId);

        // 创建微信支付单，如果你有多种支付方式，则可以根据支付类型的策略模式进行创建支付单
        String codeUrl="";
        if(null != payService)
        {
            PrepayResponse prepayResponse =payService.prepay(request);
            codeUrl = prepayResponse.getCodeUrl();
        }else{
            codeUrl ="因你未配置支付渠道，所以暂时不能生成有效的支付URL。请配置支付渠道后，在application-dev.yml中配置支付渠道信息 ";
        }
        PayOrderEntity payOrderEntity = PayOrderEntity.builder()
                .orderId(orderId)
                .openid(openid)
                .payUrl(codeUrl)
                .payStatus(PayStatusVO.WALL)
                .build();
        // 更新订单支付信息
        orderRepository.updateOrderPayInfo(payOrderEntity);
        return payOrderEntity;
    }

    @Override
    public boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime) {
        return orderRepository.changeOrderPaySuccess(orderId, transactionId, totalAmount, payTime);
    }

    @Override
    public CreateOrderAggregate queryOrder(String orderId) {
        return orderRepository.queryOrder(orderId);
    }

    @Override
    public void deliverGoods(String orderId) {
        orderRepository.deliveGoods(orderId);
    }

    @Override
    public List<String> queryReplenishmentOrder() {
        return orderRepository.queryReplenishmentOrder();
    }

    @Override
    public List<String> queryNoPayNotifyOrder() {
        return orderRepository.queryNoPayNotifyOrder();
    }

    @Override
    public List<String> queryTimeoutCloseOrderList() {
        return orderRepository.queryTimeoutCloseOrderList();
    }

    @Override
    public boolean changeOrderClose(String orderId) {
        return orderRepository.changeOrderClose(orderId);
    }

    @Override
    public List<ProductEntity> queryProductList() {
        return orderRepository.queryProductList();
    }
}
