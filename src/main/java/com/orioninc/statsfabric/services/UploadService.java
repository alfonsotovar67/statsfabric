package com.orioninc.statsfabric.services;

import com.orioninc.statsfabric.pojo.Contenedor;
import com.orioninc.statsfabric.pojo.InformationSchemaColumns;
import com.orioninc.statsfabric.pojo.RowData;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
public class UploadService {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    private static final Log log = LogFactory.getLog(UploadService.class);

    private final Map<String, InformationSchemaColumns> schemaColumnsMap;
    private int successRows = 0;
    private int errorRows = 0;

    @Autowired
    public UploadService(Map<String, InformationSchemaColumns> schemaColumnsMap) {
        this.schemaColumnsMap = schemaColumnsMap;
    }

    public String uploadFile(Sheet sheet) {
        Map<Integer, InformationSchemaColumns> dbFields = extractDBHeaders(sheet);
        try (Connection connection = createDBConnection()) {
            processSheetRows(sheet, dbFields, connection);
        } catch (Exception e) {
            log.error("Error closing the DB connection", e);
        }
        return String.format("Records processed: %d, Records with errors: %d", successRows, errorRows);
    }

    private Map<Integer, InformationSchemaColumns> extractDBHeaders(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        Map<Integer, InformationSchemaColumns> dbFields = new HashMap<>();
        for (int cellNum = 0; cellNum < headerRow.getLastCellNum(); cellNum++) {
            String header = headerRow.getCell(cellNum).getStringCellValue();
            InformationSchemaColumns field = schemaColumnsMap.getOrDefault(header, new InformationSchemaColumns(null, header, null));
            dbFields.put(cellNum, field);
        }
        return dbFields;
    }

    private void processSheetRows(Sheet sheet, Map<Integer, InformationSchemaColumns> dbFields, Connection connection) {
        successRows = 0;
        errorRows = 0;
        log.info("Number of rows: " + sheet.getLastRowNum());
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            processRow(row, dbFields, connection, rowNum);
        }
        log.info("Records processed: " + successRows + ", Records with errors: " + errorRows);
    }

    private void processRow(Row row, Map<Integer, InformationSchemaColumns> dbFields, Connection connection, int rowNum) {
        Map<String, RowData> rowDataMap = new TreeMap<>();
        String issueCve = null;
        try {
            for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
                Cell cell = row.getCell(cellNum);
                InformationSchemaColumns field = dbFields.get(cellNum);
                if (field.getTableName() != null) {
                    RowData rowData = extractCellData(cell, field, rowDataMap);
                    if (rowData != null) {
                        if ("issuecve".equals(rowData.getColumnName())) {
                            issueCve = String.valueOf(rowData.getValue());
                            log.info("Processing:" + issueCve + " Renglon: " + rowNum);
                        } else {
                            rowDataMap.put(rowData.getTableName() + rowData.getColumnName(), rowData);
                        }
                    }
                }
            }

            if (!rowDataMap.isEmpty()) {
                updateRowInDB(rowDataMap, issueCve, connection);
            }
        } catch (Exception e) {
            log.error("Error processing row " + (rowNum + 1) + " issuecve :" + issueCve + " ", e);
            errorRows++;
        }
    }

    private RowData extractCellData(Cell cell, InformationSchemaColumns field, Map<String, RowData> rowDataMap) {
        if (cell == null) return null;

        RowData rowData = new RowData();
        Object cellValue = getCellValue(cell);
        if ("json".equals(field.getDataType())) {
            rowData = rowDataMap.get(field.getTableName() + field.getColumnName());
            String existingData;
            if (rowData == null) {
                existingData = "";
                rowData = new RowData();
            } else {
                existingData = rowData.getValue() == null ? "" : rowData.getValue().toString();
            }
            rowData.setValue(getJsonString(field.getColumnName(), existingData, cellValue));
        } else {
            rowData.setValue(cellValue);
        }
        rowData.setDataType(field.getDataType());
        rowData.setColumnName(field.getColumnName());
        rowData.setTableName(field.getTableName());
        return rowData;
    }

    private Object getCellValue(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().replace("\"", "´");
            case NUMERIC ->
                    DateUtil.isCellDateFormatted(cell) ? cell.getLocalDateTimeCellValue() : cell.getNumericCellValue();
            case BOOLEAN, FORMULA -> cell.getBooleanCellValue();
            case BLANK -> null;
            case ERROR -> cell.getErrorCellValue();
            default -> {
                log.error("Unknown cell type");
                yield null;
            }
        };
    }

    private void updateRowInDB(Map<String, RowData> rowDataMap, String issueCve, Connection connection) {
        Contenedor container = new Contenedor();
        rowDataMap.forEach((column, rowData) -> {
            if (!rowData.getTableName().equals(container.tabla)) {
                if (container.tabla != null) {
                    executeInsertQuery(container, issueCve, connection);
                }
                container.columnas = new StringBuilder();
                container.valores = new StringBuilder();
                container.tabla = rowData.getTableName();
            }
            appendColumnAndValue(container, rowData);
        });
        executeInsertQuery(container, issueCve, connection);
    }

    private void appendColumnAndValue(Contenedor container, RowData rowData) {
        container.columnas.append(rowData.getColumnName()).append(",");
        String value = formatValueForSQL(rowData);
        container.valores.append(value).append(",");
    }

    private String formatValueForSQL(RowData rowData) {
        return switch (rowData.getDataType()) {
            case "int" -> String.valueOf(Math.round((Double) rowData.getValue()));
            case "double", "BOOLEAN" -> String.valueOf(rowData.getValue());
            case "datetime" -> "\"" + getDateTime(rowData.getValue()) + "\"";
            case "json", "FORMULA", "BLANK", "ERROR" -> "\"" + rowData.getValue() + "\"";
            default -> "\"" + rowData.getValue().toString().replace("\"", "´") + "\"";
        };
    }

    private void executeInsertQuery(Contenedor container, String issueCve, Connection connection) {
        if (!container.columnas.isEmpty() && !container.valores.isEmpty()) {
            container.columnas.setLength(container.columnas.length() - 1);
            container.valores.setLength(container.valores.length() - 1);

            String deleteQuery = "DELETE FROM " + container.tabla + " WHERE issuecve=\"" + issueCve + "\"";
            String insertQuery = "INSERT INTO " + container.tabla + " (issuecve," + container.columnas + ") VALUES (\"" + issueCve + "\"," + container.valores + ")";
            executeSQL(deleteQuery, connection);
            executeSQL(insertQuery, connection);
            successRows++;
        }
    }

    private void executeSQL(String query, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            log.error("SQL Error: " + query + "\n" + Arrays.toString(e.getStackTrace()));
            errorRows++;
        }
    }

    private String getDateTime(Object value) {
        if (value == null) return null;

        String dateTimeStr = value.toString();
        if (isValidLocalDateTime(dateTimeStr)) {
            return dateTimeStr;
        }

        String datePart = "20" + dateTimeStr.substring(7, 9) + "-" + getMonth(dateTimeStr.substring(3, 6)) + "-" + dateTimeStr.substring(0, 2);
        int hour = Integer.parseInt(dateTimeStr.substring(10, dateTimeStr.indexOf(":")));
        String amPm = dateTimeStr.substring(dateTimeStr.indexOf(":") + 4, dateTimeStr.indexOf(":") + 6);
        if (amPm.equals("PM") && hour < 12) {
            hour += 12;
        }
        if (amPm.equals("AM") && hour == 12) {
            hour = 0;
        }
        String timePart = hour + ":" + dateTimeStr.substring(dateTimeStr.indexOf(":") + 1, dateTimeStr.indexOf(":") + 3) + ":00.000";
        return datePart + "T" + timePart;
    }

    private boolean isValidLocalDateTime(String dateTimeStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime.parse(dateTimeStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private String getMonth(String monthStr) {
        return switch (monthStr) {
            case "ene" -> "01";
            case "feb" -> "02";
            case "mar" -> "03";
            case "abr" -> "04";
            case "may" -> "05";
            case "jun" -> "06";
            case "jul" -> "07";
            case "ago" -> "08";
            case "sep" -> "09";
            case "oct" -> "10";
            case "nov" -> "11";
            case "dic" -> "12";
            default -> throw new IllegalArgumentException("Invalid month: " + monthStr);
        };
    }

    private String getJsonString(String columnName, String existingData, Object newValue) {
        if (existingData != null && !existingData.isEmpty()) {
            existingData = existingData.substring(0, existingData.length() - 2);
            return existingData + ",\\\"" + newValue.toString() + "\\\" ]}";
        } else {
            return "{ \\\"" + columnName + "\\\":[\\\"" + newValue.toString() + " \\\" ]}";
        }
    }

    private Connection createDBConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            log.error("Error creating DB connection", e);
            return null;
        }
    }
}