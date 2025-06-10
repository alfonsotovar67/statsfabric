package com.orioninc.statsfabric.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;

@Service
public class ReporterServices {
    private static final Log log = LogFactory.getLog(ReporterServices.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Autowired
    public ReporterServices(JdbcTemplate jdbcTemplate) {
    }

    public Boolean createReport(String sprint, String reportName) {
        try {
            String query = loadQueryFromFile(reportName + ".sql");
            Connection connection = getConnection();
            if (connection != null) {
                try (ResultSet resultSet = connection.createStatement().executeQuery(query)) {
                    GeneredFileReport(resultSet, reportName);
                }
                connection.close();
                return true;
            }
        } catch (Exception e) {
            log.error("Error al crear el reporte: " + e.getMessage() + "\n" + e.getStackTrace());
        }
        return false;
    }

    private void GeneredFileReport(ResultSet resultSet, String reportName) {
        String PodAnterior = "";

        // Ruta del archivo existente y la nueva ruta y nombre del archivo
        // Cambia estas rutas según tus necesidades

        File f = new File("reportes");
        if (!f.exists()) {
            f.mkdir();
        }
        String outputFile = "reportes/" + Genera_FileName(reportName); // Nueva ruta y nombre del archivo

        ArrayList<String> headers = getHeader(resultSet);

        try (Workbook workbook = new XSSFWorkbook()) { // Crear un nuevo Workbook
            Sheet sheet = null;
            int rowNumber = 0;
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            while (resultSet.next()) {
                String Pod = resultSet.getString("pod");
                if (!Pod.equals(PodAnterior)) {
                    sheet = workbook.createSheet(Pod); // Crear una hoja
                    PodAnterior = Pod;
                    for (int i = 0; i < 3; i++) {
                        sheet.createRow(i);
                    }
                    addHeadersToSheet(sheet, headers, style, font);
                    rowNumber = 4;
                }
                insertRowToSheet(sheet, resultSet, rowNumber++);
            }

            try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
                workbook.write(fileOut);
                fileOut.flush();
                workbook.close();
                log.info("Archivo Excel creado exitosamente. " + new File(outputFile).getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertRowToSheet(Sheet sheet, ResultSet resultSet, int rownumber) {
        try {
            Row row = sheet.createRow(rownumber); // Crear una fila
            for (int i = 0; i < 3; i++) {
                Cell cell = row.createCell(i);
            }
            int fieldNumber = 0;
            insertCellsToRow(resultSet, row, fieldNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertCellsToRow(ResultSet resultSet, Row row, int fieldNumber) {
        try {
            while (fieldNumber <= resultSet.getMetaData().getColumnCount()) {
                Cell cell = row.createCell(fieldNumber + 3);
                String value = resultSet.getString(++fieldNumber);
                try {
                    double numeric = Double.valueOf(value);
                    cell.setCellValue(numeric);
                } catch (NumberFormatException e) {
                    cell.setCellValue(value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addHeadersToSheet(Sheet sheet, ArrayList<String> headers, CellStyle style, Font font) {
        Row headerRow = sheet.createRow(3);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i + 3);
            cell.setCellValue(headers.get(i));

            font.setBold(true); // Aplicar negrita
            font.setFontHeightInPoints((short) 14); // Tamaño de la fuente
            style.setFont(font);
            cell.setCellStyle(style);
        }
    }

    private ArrayList<String> getHeader(ResultSet resultSet) {
        ArrayList<String> headers = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                headers.add(metaData.getColumnName(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return headers;
    }

    private String Genera_FileName(String reportName) {
        GregorianCalendar gc = new GregorianCalendar();
        String fileName = reportName + "_" + gc.toZonedDateTime().toLocalDate() + ".xlsx";
        return fileName;
    }

    private Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return java.sql.DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            log.error("Error al conectar a la base de datos: " + e.getMessage() + "\n" + e.getStackTrace());
            return null;
        }
    }

    private String loadQueryFromFile(String fileName) throws IOException {
        // return Files.readString(Paths.get("./queries/" + fileName));
        InputStream inputStream = new ClassPathResource("queries/" + fileName).getInputStream();
        return new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A").next();
    }
}
