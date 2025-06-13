package com.aer.es.util;


import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SuggestionsUtil {
    public static List<String> getSuggestions(String name, String brand, String category) {
        //简单按照名字中空格作为划分区
        List<String> suggestions = Arrays.stream(name.split("\\s+"))
                .filter(word -> word.length() >= 2)
                .distinct()
                .collect(Collectors.toList());

        // 将品牌和分类直接加入搜索建议（如果不为空）
        if (StrUtil.isNotBlank(brand)) {
            suggestions.add(brand.trim());
        }
        if (StrUtil.isNotBlank(category)) {
            suggestions.add(category.trim());
        }
        return suggestions;
    }
}
