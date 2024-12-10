package com.orioninc.statsfabric.configuration;

import com.orioninc.statsfabric.dao.InformationSchemaColumns;
import com.orioninc.statsfabric.services.UserHistoriesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class AppConfiguration {

    private UserHistoriesServices userHistoriesServices;

    @Autowired
    public AppConfiguration(UserHistoriesServices userHistoriesServices) {
        this.userHistoriesServices = userHistoriesServices;
    }

    @Bean
    public Map<String, InformationSchemaColumns> myUserSructure() {
        return userHistoriesServices.getStructure();
    }
}
