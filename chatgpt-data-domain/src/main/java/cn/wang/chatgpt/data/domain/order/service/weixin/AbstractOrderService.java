package cn.wang.chatgpt.data.domain.order.service.weixin;

import cn.wang.chatgpt.data.domain.order.model.entity.*;
import cn.wang.chatgpt.data.domain.order.model.valobj.PayStatusVO;
import cn.wang.chatgpt.data.domain.order.repository.IOrderRepository;
import cn.wang.chatgpt.data.types.common.Constants;
import cn.wang.chatgpt.data.types.exception.ChatGPTException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public abstract  class AbstractOrderService implements IOrderService {

    @Resource
    protected IOrderRepository orderRepository;
    public PayOrderEntity createOrder(ShopCartEntity shopCartEntity){
        try {
            //0 基础信息
            String openid = shopCartEntity.getOpenid();
            Integer productId = shopCartEntity.getProductId();
            //1 查询有效的未支付订单，如果存在直接返回微信微信支付
            UnpaidOrderEntity unpaidOrderEntity = orderRepository.queryUnpaidOrder(shopCartEntity);

            if(null != unpaidOrderEntity && PayStatusVO.WALL.equals(unpaidOrderEntity.getPayStatus()) && null != unpaidOrderEntity.getPayUrl())
            {
                log.info("创建订单-存在，已生成微信支付，返回 openid: {} orderId: {} payUrl: {}", openid, unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getPayUrl());
                return  PayOrderEntity.builder()
                        .orderId(unpaidOrderEntity.getOrderId())
                        .payUrl(unpaidOrderEntity.getPayUrl())
                        .payStatus(unpaidOrderEntity.getPayStatus())
                        .openid(unpaidOrderEntity.getOpenid())
                        .build();
            }
            else if(null != unpaidOrderEntity && null == unpaidOrderEntity.getPayUrl())
            {
                log.info("创建订单-存在，未生成微信支付，返回 openid: {} orderId: {}", openid, unpaidOrderEntity.getOrderId());
                PayOrderEntity payOrderEntity =this.doPrepayOrder(openid, unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getProductName(), unpaidOrderEntity.getTotalAmount());
                log.info("创建订单-完成，生成支付单。openid: {} orderId: {} payUrl: {}", openid, payOrderEntity.getOrderId(), payOrderEntity.getPayUrl());
                return payOrderEntity;
            }
            //2商品查询

            ProductEntity productEntity =orderRepository.queryProduct(productId);
            System.out.println(productEntity);
            if(!productEntity.isAvailable())
            {
                throw  new ChatGPTException(Constants.ResponseCode.ORDER_PRODUCT_ERR.getCode(),Constants.ResponseCode.ORDER_PRODUCT_ERR.getInfo());
            }
            //3保存订单

            OrderEntity orderEntity = this.doSaveOrder(openid,productEntity);
            //4创建支付
            PayOrderEntity payOrderEntity = this.doPrepayOrder(openid, orderEntity.getOrderId(), productEntity.getProductName(), orderEntity.getTotalAmount());
            log.info("创建订单-完成，生成支付单。openid: {} orderId: {} payUrl: {}", openid, orderEntity.getOrderId(), payOrderEntity.getPayUrl());

            return payOrderEntity;
        }catch (Exception e)
        {
            log.error("创建订单，已生成微信支付，返回 openid: {} productId: {}", shopCartEntity.getOpenid(), shopCartEntity.getProductId());
            throw new ChatGPTException(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
        }
    }

    protected abstract OrderEntity doSaveOrder(String openid, ProductEntity productEntity) ;

    protected abstract PayOrderEntity doPrepayOrder(String openid, String orderId, String productName, BigDecimal AmountTotal);
}
