package com.aer.es.service;

import com.aer.es.entity.Item;
import com.aer.es.vo.ItemVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author aer
 * @since 2025-06-12
 */
public interface IItemService extends IService<Item> {

    List<String> getSuggestions(String keyword);

    void syncData();

    List<ItemVO> search(String keyword, String brand, String category);
}
