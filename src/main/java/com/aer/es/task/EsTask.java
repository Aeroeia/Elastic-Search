package com.aer.es.task;

import com.aer.es.entity.Item;
import com.aer.es.service.IItemService;
import com.aer.es.util.SuggestionsUtil;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EsTask {
    private final IItemService itemService;

    private final RestHighLevelClient client;
    @Scheduled(cron = "0 */5 * * * ?") // 每5分钟执行一次
    public void syncProductToEs() {
        itemService.syncData();
    }
}
