package com.orioninc.statsfabric.services;

import com.orioninc.statsfabric.dao.Contenedor;
import com.orioninc.statsfabric.dao.InformationSchemaColumns;
import com.orioninc.statsfabric.dao.RowData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class UploadService {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    private static final Log log = LogFactory.getLog(UploadService.class);

    private int successrows = 0;
    private int errorrows = 0;


    final Map<String, InformationSchemaColumns> mapa;

    @Autowired
    public UploadService(Map<String, InformationSchemaColumns> mapa) {
        this.mapa = mapa;
    }

    public String uploadFile(Sheet sh) throws NullPointerException {
        Map<Integer, InformationSchemaColumns> dBFileld = getDBHeaders(sh);
        Connection conexion = getDBConnection();
        String resultado = setValuesToRow(sh, dBFileld, conexion);
        closeDBConnection(conexion);
        return resultado;
    }

    public Map<Integer, InformationSchemaColumns> getDBHeaders(Sheet sh) {
        Row row = sh.getRow(0);
        Map<Integer, InformationSchemaColumns> dBFileld = new HashMap<>();
        for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
            String comment = row.getCell(cellNum).getStringCellValue();
            InformationSchemaColumns field;
            if (mapa.containsKey(comment)) {
                field = mapa.get(comment);
            } else {
                field = new InformationSchemaColumns(null, comment, null);
            }
            dBFileld.put(cellNum, field);
        }
        return dBFileld;
    }

    public String setValuesToRow(Sheet sheet, Map<Integer, InformationSchemaColumns> dBFileld, Connection conexion) {
        successrows = 0;
        errorrows = 0;
        System.out.println("Número de filas: " + sheet.getLastRowNum());
        Cell temp = sheet.getRow(0).getCell(0);
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Map<String, RowData> rowDataMap = new TreeMap<>();
            String issueCve = null;
            for (int cellNum = 0; cellNum < sheet.getRow(rowNum).getLastCellNum(); cellNum++) {
                RowData rowData = new RowData();
                InformationSchemaColumns field = dBFileld.get(cellNum);
                if (field.getTableName() != null) {
                    Cell cell = sheet.getRow(rowNum).getCell(cellNum);
                    if (cell != null) {
                        Object cellValue = null;
                        switch (cell.getCellType()) {
                            case STRING -> {
                                String valor = cell.getStringCellValue() == null ? "" : cell.getStringCellValue();
                                valor = valor.replace("\"", "´");
                                cellValue = valor;
                            }
                            case NUMERIC -> {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    cellValue = cell.getLocalDateTimeCellValue();
                                } else {
                                    cellValue = cell.getNumericCellValue();
                                }
                            }
                            case BOOLEAN -> cellValue = cell.getBooleanCellValue();
                            case FORMULA -> cellValue = cell.getBooleanCellValue();
                            case BLANK -> cellValue = null;
                            case ERROR -> cellValue = cell.getErrorCellValue();
                            default -> System.out.println("Tipo de celda desconocido");
                        }

                        if (field.getDataType().equals("json")) {
                            String datos = rowDataMap.get(field.getTableName() + field.getColumnName()) == null ? "" : rowDataMap.get(field.getTableName() + field.getColumnName()).getValue().toString();
                            rowData.setValue(getJsonString(field.getColumnName(), datos, cellValue));
                        } else {
                            rowData.setValue(cellValue);
                        }
                        rowData.setDataType(field.getDataType());
                        rowData.setColumnName(field.getColumnName());
                        rowData.setTableName(field.getTableName());
                        if (rowData.getColumnName().equals("issuecve")) {
                            issueCve = String.valueOf(cellValue);
                        }
                        if (!(rowData.getColumnName().equals("issuecve"))) {
                            rowDataMap.put(rowData.getTableName() + rowData.getColumnName(), rowData);
                        }
                    }
                }
            }
            updateRow(rowDataMap, issueCve, conexion);
        }
        log.info("Registros procesados: " + successrows + " Registros con error: " + errorrows);
        return "Registros procesados: " + successrows + " Registros con error: " + errorrows;
    }

    public void updateRow(Map<String, RowData> rowDataMap, String issueCve, Connection conexion) {
        List<Map.Entry<String, RowData>> list = rowDataMap.entrySet().stream().collect(Collectors.toList());

        Contenedor contenedor = new Contenedor();

        rowDataMap.forEach((columna, rowData) -> {
            if (!rowData.getTableName().equals(contenedor.tabla)) {
                if (contenedor.tabla != null) {
                    // Eliminar la última coma
                    contenedor.columnas.setLength(contenedor.columnas.length() - 1);
                    contenedor.valores.setLength(contenedor.valores.length() - 1);
                    String sql = "INSERT INTO " + contenedor.tabla + " (issuecve," + contenedor.columnas + ") VALUES (\"" + issueCve + "\"," + contenedor.valores + ")";
                    String sqlget = "DELETE FROM " + contenedor.tabla + " WHERE issuecve=\"" + issueCve + "\"";

                    try {
                        PreparedStatement preparedStatement = conexion.prepareStatement(sqlget);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                        preparedStatement = conexion.prepareStatement(sql);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                        successrows++;
                        log.info("issueCve Procesado: " + issueCve);
                    } catch (Exception e) {
                        log.error("issueCve: " + issueCve + "Error: " + e.getMessage());
                        log.error("SQL: " + sql);
                        errorrows++;
                    }
                }
                contenedor.columnas = new StringBuilder();
                contenedor.valores = new StringBuilder();
                contenedor.tabla = rowData.getTableName();
            } else {
                contenedor.columnas.append(rowData.getColumnName()).append(",");
                switch (rowData.getDataType()) {
                    case "int" -> contenedor.valores.append(Math.round((Double) rowData.getValue()) + ",");
                    case "double" -> contenedor.valores.append((Double) rowData.getValue() + ",");
                    case "datetime" -> contenedor.valores.append("\"" + getDateTime(rowData.getValue()) + "\",");
                    case "json" -> contenedor.valores.append("\"" + rowData.getValue() + "\",");
                    case "BOOLEAN" -> contenedor.valores.append(rowData.getValue() + ",");
                    case "FORMULA" -> contenedor.valores.append("\"" + rowData.getValue() + "\",");
                    case "BLANK" -> contenedor.valores.append("\"\",");
                    case "ERROR" -> contenedor.valores.append("\"" + rowData.getValue() + "\",");
                    default ->
                            contenedor.valores.append("\"" + rowData.getValue().toString().replace("\"", "´") + "\",");
                }
            }
        });
        // Eliminar la última coma
        contenedor.columnas.setLength(contenedor.columnas.length() - 1);
        contenedor.valores.setLength(contenedor.valores.length() - 1);
        String sql = "INSERT INTO " + contenedor.tabla + " (issueCve," + contenedor.columnas + ") VALUES (\"" + issueCve + "\"," + contenedor.valores + ")";
        String sqlget = "DELETE FROM " + contenedor.tabla + " WHERE issueCve=\"" + issueCve + "\"";

        try {
            PreparedStatement preparedStatement = conexion.prepareStatement(sqlget);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = conexion.prepareStatement(sql);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            successrows++;
            log.info("issueCve Procesado: " + issueCve);
        } catch (Exception e) {
            log.error("issueCve: " + issueCve + "Error: " + e.getMessage());
            log.error("SQL: " + sql);
            errorrows++;
        }
    }

    private String getDateTime(Object value) {
        String fecha;
        if (value == null) {
            return null;
        } else {
            if (esLocalDateTimeValido(value.toString())) {
                fecha = value.toString();
            } else {
                fecha = "20" + value.toString().substring(7, 9) + "-" + getMonth(value.toString().substring(3, 6)) + "-" + value.toString().substring(0, 2) + "T";
                int hora = Integer.parseInt(value.toString().substring(10, value.toString().indexOf(":")));
                if (hora == 12) {
                    hora = 0;
                }
                if (value.toString().substring(value.toString().indexOf(":") + 4, value.toString().indexOf(":") + 6).equals("PM")) {
                    hora = hora + 12;
                    fecha = fecha + hora + ":" + value.toString().substring(value.toString().indexOf(":") + 1, value.toString().indexOf(":") + 3) + ":00.000";
                } else {
                    fecha = fecha + hora + ":" + value.toString().substring(value.toString().indexOf(":") + 1, value.toString().indexOf(":") + 3) + ":00.000";
                }
            }
        }
        return fecha;
    }

    private String getMonth(String monthStr) {
        String month = "";
        switch (monthStr) {
            case "ene" -> month = "01";
            case "feb" -> month = "02";
            case "mar" -> month = "03";
            case "abr" -> month = "04";
            case "may" -> month = "05";
            case "jun" -> month = "06";
            case "jul" -> month = "07";
            case "ago" -> month = "08";
            case "sep" -> month = "09";
            case "oct" -> month = "10";
            case "nov" -> month = "11";
            case "dic" -> month = "12";
        }
        return month;
    }

    private boolean esLocalDateTimeValido(String fechaHoraStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime.parse(fechaHoraStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private String getJsonString(String columnName, String valor, Object value) {
        if (valor != null && valor.length() > 0) {
            valor = valor.substring(0, valor.length() - 2);
            valor = valor + ",\\\"" + value.toString() + "\\\" ]}";
        } else {
            valor = "{ \\\"" + columnName + "\\\":[\\\"" + value.toString() + " \\\" ]}";
        }
        return valor;
    }

    private Connection getDBConnection() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conexion;
    }

    private void closeDBConnection(Connection conexion) {
        try {
            conexion.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

