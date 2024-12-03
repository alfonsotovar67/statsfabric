package com.orioninc.statsfabric.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfiguration {

    @Bean
    public Map<String,String> myUserSructure() {
        HashMap<String, String> map = new HashMap<>();
        map.put("user", "alfonso");
        map.put("password", "alfonso");
        return map;
    }
}
