package com.project.system2.common.listener;

import com.project.system2.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DictDataInitListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private ISysDictDataService dictDataService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 初始化字典缓存数据
        dictDataService.loadingDictCache();
    }
} 