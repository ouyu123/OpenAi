package cn.wang.chatgpt.data.domain.order.repository;

import cn.wang.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import cn.wang.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import cn.wang.chatgpt.data.domain.order.model.entity.ProductEntity;
import cn.wang.chatgpt.data.domain.order.model.entity.ShopCartEntity;
import cn.wang.chatgpt.data.domain.order.model.entity.UnpaidOrderEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单仓库接口
 */
public interface IOrderRepository {

    UnpaidOrderEntity queryUnpaidOrder(ShopCartEntity shopCartEntity);


    ProductEntity queryProduct(Integer productId);

    void saveOrder(CreateOrderAggregate aggregate);

    void updateOrderPayInfo(PayOrderEntity payorderEntity);

    boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime);

    CreateOrderAggregate queryOrder(String orderId);

    void deliveGoods(String orderId);

    List<String> queryReplenishmentOrder();

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrderList();

    boolean changeOrderClose(String orderId);

    List<ProductEntity> queryProductList();
}
