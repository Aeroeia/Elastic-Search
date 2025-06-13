package com.aer.es.controller;

import com.aer.es.entity.Item;
import com.aer.es.service.IItemService;
import com.aer.es.vo.ItemVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("es")
@RequiredArgsConstructor
public class EsController {
    private final IItemService itemService;
    @GetMapping("/suggestions")
    public List<String> getSuggestions(@RequestParam String keyword){
        log.info("keyword:{}",keyword);
        return itemService.getSuggestions(keyword);
    }
    @GetMapping("/search")
    public List<ItemVO> search(@RequestParam(required = false) String keyword, @RequestParam(required = false) String brand, @RequestParam(required = false) String category){
        log.info("keyword:{}",keyword);
        log.info("brand:{}",brand);
        log.info("category:{}",category);
        return itemService.search(keyword,brand,category);
    }
}
