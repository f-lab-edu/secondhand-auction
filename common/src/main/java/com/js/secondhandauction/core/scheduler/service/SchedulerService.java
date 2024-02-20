package com.js.secondhandauction.core.scheduler.service;

import com.js.secondhandauction.core.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {
    @Autowired
    private ItemService itemService;

    //배치 임시 비활성화
    //@Scheduled(cron = "0 */1 * * * *")
    //public void checkStateItemsPerMinutes() {
    //    itemService.updateStateCheckItems();
    //}
}
