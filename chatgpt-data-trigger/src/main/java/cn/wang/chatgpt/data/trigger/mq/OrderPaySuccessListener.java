package cn.wang.chatgpt.data.trigger.mq;

import cn.wang.chatgpt.data.domain.order.service.weixin.IOrderService;
import com.google.common.eventbus.Subscribe;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 订单支付成功监听
 * 1.订单支付成功回调，最好是快速变更状态，避免超时重试次数上限后不能做业务，所有推送出MQ消息来做【发货】流程
 * 2. 因为ChatGPT项目，选择了轻量的技术栈，所以使用 Guava 的 EventBus 消息总线来模拟消息使用。如果你后续的场景较大，也可以替换为 RocketMQ，教程：https://bugstack.cn/md/road-map/rocketmq.html
 */

@Slf4j
@Component
public class OrderPaySuccessListener {

    @Resource
    private IOrderService orderService;

    @Subscribe
    public  void handleEvent(String orderId)
    {
        try {
            log.info("支付完成，发货并记录，开始。订单：{}", orderId);
            orderService.deliverGoods(orderId);
        }catch (Exception e)
        {
            log.error("支付完成，发货并记录，失败。订单：{}", orderId, e);
        }
    }
}
