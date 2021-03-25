package com.viettel.campaign;

import com.viettel.campaign.config.DataSourceProperties;
import com.viettel.campaign.utils.Config;
import com.viettel.campaign.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ServiceCampaignApplication {

    @Autowired
    DataSourceProperties dataSourceProperties;

    public static void main(String[] args) {
        SpringApplication.run(ServiceCampaignApplication.class, args);
        RedisUtil redis = new RedisUtil(Config.redisAddress, Config.redisTimeout);
        redis.setup();
    }

}
