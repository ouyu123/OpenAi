package cn.wang.chatgpt.data.domain.order.model.aggregates;

import cn.wang.chatgpt.data.domain.order.model.entity.OrderEntity;
import cn.wang.chatgpt.data.domain.order.model.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 下单聚类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderAggregate {

    /** 用户ID；微信用户唯一标识 */
    private String openid;
    /** 商品 */
    private ProductEntity product;
    /** 订单 */
    private OrderEntity order;
}
