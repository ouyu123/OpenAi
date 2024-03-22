package cn.wang.chatgpt.data.infrastructure.dao;

import cn.wang.chatgpt.data.domain.order.model.entity.ProductEntity;
import cn.wang.chatgpt.data.infrastructure.po.OpenAIOrderPO;
import cn.wang.chatgpt.data.infrastructure.po.OpenAIProductPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IOpenAIOrderDao {

    /**
     * 创建订单
     */
    void insert(OpenAIOrderPO openAIOrderPO);

    /**
     * 查询未支付的订单
     * @param openAIOrderPO
     * @return
     */
    OpenAIOrderPO queryUnpaidOrder(OpenAIOrderPO openAIOrderPO);

    /**
     * 更新订单
     * @param openAIOrderPO
     */
    void updateOrderPayInfo(OpenAIOrderPO openAIOrderPO);

    /**
     * 支付成功修改支付状态
     * @param openAIOrderPO
     * @return
     */
    int changeOrderPaySuccess(OpenAIOrderPO openAIOrderPO);

    /**
     * 查询账单
     * @param openId
     * @return
     */
    OpenAIOrderPO queryOrder(String openId);

    /**
     * 补货
     * @param orderId
     * @return
     */
    int updateOrderStatusDeliverGoods(String orderId);
    /**
     * 查找补货的订单id
     * @return
     */
    List<String> queryReplenishmentOrder();

    /**
     * 查询时间范围类没有支付的订单
     * @return
     */
    List<String> queryNoPayNotifyOrder();

    /**
     * 查询超时的订单
     * @return
     */
    List<String>    queryTimeoutCloseOrderList();

    /**
     * 订单关闭
     * @param openAIOrderPO
     */
    void changeOrderClose(OpenAIOrderPO openAIOrderPO);
}
