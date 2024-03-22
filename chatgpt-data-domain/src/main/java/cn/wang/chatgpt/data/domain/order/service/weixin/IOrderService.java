package cn.wang.chatgpt.data.domain.order.service.weixin;

import cn.wang.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import cn.wang.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import cn.wang.chatgpt.data.domain.order.model.entity.ProductEntity;
import cn.wang.chatgpt.data.domain.order.model.entity.ShopCartEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单服务
 */
public interface IOrderService {
    /**
     * 用户下单，通过购物车信息，返回下单后的支付单
     * @param shopCartEntity 简单购物车
     * @return 支付单实体对象
     */
    PayOrderEntity createOrder(ShopCartEntity shopCartEntity);

    /**
     * 变更；订单支付成功
     */
    boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime);

    /**
     * 查询订单信息
     * @param orderId 订单Id
     * @return 查询结果
     */
    CreateOrderAggregate queryOrder(String orderId);

    /**
     * 订单商品发货
     * @param orderId 订单id
     */
    void deliverGoods(String orderId);

    /**
     * 查询补货订单
     * @return
     */
    List<String> queryReplenishmentOrder();

    /**
     * 查询有效期内，未接受到支付回调的订单
     * @return
     */
    List<String> queryNoPayNotifyOrder();

    /**
     * 查询超时15分钟，未支付的订单
     * @return
     */
    List<String> queryTimeoutCloseOrderList();

    /**
     * 变更：订单支付关闭
     * @param orderId
     * @return
     */
    boolean changeOrderClose(String orderId);

    /**
     * 查询商品列表
     * @return
     */
    List<ProductEntity> queryProductList();
}
