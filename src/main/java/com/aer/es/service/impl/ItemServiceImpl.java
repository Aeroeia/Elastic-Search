package com.aer.es.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aer.es.entity.EsItem;
import com.aer.es.entity.Item;
import com.aer.es.mapper.ItemMapper;
import com.aer.es.service.IItemService;
import com.aer.es.util.SuggestionsUtil;
import com.aer.es.vo.ItemVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author aer
 * @since 2025-06-12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements IItemService {
    private final RestHighLevelClient client;

    //自定义字段
    private final String suggestionName = "sug";
    @Override
    @Cacheable(value = "suggestions",key = "#keyword")
    public List<String> getSuggestions(String keyword) {

        try {
            // 构建搜索建议请求
            SuggestBuilder sb = new SuggestBuilder();
            sb.addSuggestion(suggestionName,
                    SuggestBuilders.completionSuggestion("suggest_keywords")
                            .prefix(keyword)
                            .size(10) //最多十条建议
                            .skipDuplicates(true)
            );
            
            SearchSourceBuilder src = new SearchSourceBuilder().suggest(sb).size(0);
            SearchRequest req = new SearchRequest("item").source(src);

            
            // 执行搜索
            SearchResponse resp = client.search(req, RequestOptions.DEFAULT);
            log.info("搜索响应: {}", resp);
            
            // 解析建议结果
            CompletionSuggestion suggestion = resp.getSuggest().getSuggestion(suggestionName);
            if (suggestion == null) {
                log.warn("未找到建议结果");
                return new ArrayList<>();
            }
            
            List<String> suggestions = suggestion.getOptions().stream()
                    .map(option -> {
                        String str = option.getText().toString();
                        //关键词高亮
                        StringBuilder builder = new StringBuilder(str);
                        builder.insert(0,"<em>");
                        builder.insert(keyword.length()+4,"</em>");
                        return builder.toString();
                    })
                    .collect(Collectors.toList());
            return suggestions;
            
        } catch (IOException e) {
            log.error("搜索建议时发生错误", e);
            throw new RuntimeException("搜索建议失败", e);
        }
    }



    @Override
    public void syncData() {
        log.info("开始同步数据到ES");
        List<Item> list = this.list();
        BulkRequest request = new BulkRequest();
        
        for (Item item : list) {
            try {
                List<String> suggestions = SuggestionsUtil.getSuggestions(item.getName(), item.getBrand(), item.getCategory());
                EsItem esItem = BeanUtil.copyProperties(item, EsItem.class);
                
                // 构建completion suggester所需的特定格式
                Map<String, Object> suggest = new HashMap<>();
                suggest.put("input", suggestions);
                suggest.put("weight", 10);
                esItem.setSuggest_keywords(suggest);
                
                String jsonString = JSONUtil.toJsonStr(esItem);
                log.debug("同步数据, ID: {}, 内容: {}", item.getId(), jsonString);
                
                request.add(new IndexRequest("item")
                        .id(item.getId().toString())
                        .source(jsonString, XContentType.JSON));
                
            } catch (Exception e) {
                log.error("处理商品数据时发生错误, ID: {}", item.getId(), e);
            }
        }
        
        try {
            client.bulk(request, RequestOptions.DEFAULT);
            log.info("数据同步完成，共同步 {} 条记录", list.size());
        } catch (IOException e) {
            log.error("批量同步数据时发生错误", e);
            throw new RuntimeException("同步数据失败", e);
        }
    }

    @Override
    @Cacheable(value = "search",key = "#keyword+'-'+#brand+'-'+#category")
    public List<ItemVO> search(String keyword, String brand, String category) {
        SearchRequest request = new SearchRequest("item");
        //将keyword匹配name、brand、category
        BoolQueryBuilder boolQueryBuilder;
        if(StrUtil.isNotBlank(keyword)){
            boolQueryBuilder  = QueryBuilders.boolQuery().must(QueryBuilders.multiMatchQuery(
                    keyword, "name", "brand", "category"
            ));
        }
        else{
            boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery());
        }
        if(StrUtil.isNotBlank(brand)){
            boolQueryBuilder.filter(QueryBuilders.termQuery("brand", brand));
        }
        if(StrUtil.isNotBlank(category)){
            boolQueryBuilder.filter(QueryBuilders.termQuery("category", category));
        }
        request.source().query(boolQueryBuilder)
                .highlighter(SearchSourceBuilder.highlight().field("name"));
        request.source().size(10);
        SearchResponse response;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SearchHit[] hits = response.getHits().getHits();
        List<ItemVO> list = new ArrayList<>();
        for(var hit : hits){
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlightField = highlightFields.get("name");
            String highlight = null;
            String source = hit.getSourceAsString();
            ItemVO itemVO = JSONUtil.toBean(source, ItemVO.class);
            if(highlightField!=null){
                highlight = highlightField.getFragments()[0].toString();
            }
            else{
                highlight = itemVO.getName();
            }
            itemVO.setHighlight(highlight);
            list.add(itemVO);
        }
        return list;

    }


}
