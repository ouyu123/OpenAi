package cn.wang.chatgpt.data.infrastructure.dao;

import cn.wang.chatgpt.data.domain.order.model.entity.ProductEntity;
import cn.wang.chatgpt.data.infrastructure.po.OpenAIProductPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IOpenAIProductDao {

    /**
     * 按照商品id查找商品
     * @param productId 商品id
     * @return
     */
    OpenAIProductPO queryProductByProductId(Integer productId);

    /**
     * 查询所有商品
     * @return
     */
    List<OpenAIProductPO> queryProductList();
}
