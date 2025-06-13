package com.aer.es.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author aer
 * @since 2025-06-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("item")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * SKU名称
     */
    private String name;

    /**
     * 价格（分）
     */
    private Integer price;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 商品品牌
     */
    private String brand;

    /**
     * 商品类目
     */
    private String category;

}
