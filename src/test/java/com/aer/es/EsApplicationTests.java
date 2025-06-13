package com.aer.es;

import com.aer.es.service.IItemService;
import com.aer.es.util.SuggestionsUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class EsApplicationTests {
    @Autowired
    private IItemService itemService;
    @Test
    void contextLoads() {
        List<String> suggestions = SuggestionsUtil.getSuggestions("RIMOWA 21寸托运箱拉杆箱 SALSA AIR系列果绿色", "", "");
        for (String suggestion : suggestions){
            System.out.println(suggestion);
        }
    }
    @Test
    void syncData(){
        itemService.syncData();
    }

}
