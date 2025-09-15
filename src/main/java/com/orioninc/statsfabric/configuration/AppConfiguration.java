package com.orioninc.statsfabric.configuration;

import com.orioninc.statsfabric.pojo.FieldForReporter;
import com.orioninc.statsfabric.pojo.InformationSchemaColumns;
import com.orioninc.statsfabric.services.FieldsReporterServices;
import com.orioninc.statsfabric.services.UserHistoriesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;

@Configuration
public class AppConfiguration {

    private UserHistoriesServices userHistoriesServices;
    private FieldsReporterServices fieldsReporterServices;

    @Autowired
    public AppConfiguration(UserHistoriesServices userHistoriesServices, FieldsReporterServices fieldsReporterServices) {
        this.userHistoriesServices = userHistoriesServices;
        this.fieldsReporterServices = fieldsReporterServices;
    }

    @Bean
    public Map<String, InformationSchemaColumns> myUserSructure() {
        return userHistoriesServices.getStructure();
    }

    @Bean
    public Map<String, FieldForReporter> getFieldsForRepors() throws IOException {
        return fieldsReporterServices.createFieldsMapper();
    }
}
