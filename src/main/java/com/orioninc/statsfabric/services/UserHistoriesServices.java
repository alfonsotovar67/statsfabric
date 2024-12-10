package com.orioninc.statsfabric.services;

import com.orioninc.statsfabric.dao.InformationSchemaColumns;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserHistoriesServices
{
    private static final Log log = LogFactory.getLog(UserHistoriesServices.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    public Map<String, InformationSchemaColumns> getStructure() {

        Map<String, InformationSchemaColumns> lista = new HashMap<>();

        String query = "SELECT COLUMN_NAME, COLUMN_COMMENT, DATA_TYPE FROM information_schema.columns WHERE table_name='userhistories'";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                InformationSchemaColumns informationSchemaColumns = new InformationSchemaColumns(resultSet.getString("COLUMN_NAME"),
                        resultSet.getString("DATA_TYPE"));
                lista.put(resultSet.getString("COLUMN_COMMENT"), informationSchemaColumns);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return lista;
    }
}
