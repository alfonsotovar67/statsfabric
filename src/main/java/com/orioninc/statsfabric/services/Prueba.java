package com.orioninc.statsfabric.services;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

@Service
public class DynamicInsertService {
    private static final String URL = "jdbc:mysql://localhost:3306/tu_base_de_datos";
    private static final String USER = "tu_usuario";
    private static final String PASSWORD = "tu_contraseña";

    public void insertarDinamicamente(String tabla, Map<String, Object> datos) throws SQLException {
        StringBuilder columnas = new StringBuilder();
        StringBuilder valores = new StringBuilder();
        datos.forEach((columna, valor) -> {
            columnas.append(columna).append(",");
            valores.append("?,");
        });
        // Eliminar la última coma
        columnas.setLength(columnas.length() - 1);
        valores.setLength(valores.length() - 1);
        String sql = "INSERT INTO " + tabla + " (" + columnas + ") VALUES (" + valores + ")";
        try (Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
            int indice = 1;
            for (Object valor : datos.values()) {
                preparedStatement.setObject(indice++, valor);
            }
            preparedStatement.executeUpdate();
        }
    }
}