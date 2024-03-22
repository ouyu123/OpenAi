package cn.wang.chatgpt.data.trigger.job;

import cn.wang.chatgpt.data.domain.order.service.weixin.IOrderService;
import com.wechat.pay.java.service.payments.nativepay.model.CloseOrderRequest;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 超时关单任务
 */
@Slf4j
@Component()
public class TimeoutCloseOrderJob {

    @Resource
    private IOrderService orderService;

    @Autowired(required = false)
    private NativePayService payService;


    @Value("${wxpay.config.mchid}")
    private String machid;

    public void exec()
    {
        try {
            if(null == payService)
            {
                log.info("定时任务，订单支付状态更新。应用未配置支付渠道，任务不执行。");
                return;
            }
            List<String> orderIds =orderService.queryTimeoutCloseOrderList();

            if(orderIds.isEmpty())
            {
                log.info("定时任务，超时30分钟订单关闭，暂无超时未支付订单 orderIds is null");
                return;
            }
            orderIds.forEach(orderId->{
                boolean status = orderService.changeOrderClose(orderId);
                //微信关单；暂时不需要主动关闭
                CloseOrderRequest request =new CloseOrderRequest();

                request.setMchid(machid);
                request.setOutTradeNo(orderId);
                payService.closeOrder(request);
                log.info("定时任务，超时30分钟订单关闭 orderId: {} status：{}", orderId, status);
            });
        }catch (Exception e)
        {
            log.error("定时任务，超时15分钟订单关闭失败", e);
        }
    }
}
