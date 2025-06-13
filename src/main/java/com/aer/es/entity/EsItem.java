package com.aer.es.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class EsItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
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

    /**
     * 搜索建议，使用completion类型的特定格式
     */
    private Map<String, Object> suggest_keywords;

}
