package com.orioninc.statsfabric.services;

import com.orioninc.statsfabric.constant.UnderLine;
import com.orioninc.statsfabric.pojo.FieldForReporter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

@Service
public class ReporterServices {
    private static final Log log = LogFactory.getLog(ReporterServices.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    private final Map<String, FieldForReporter> fieldsMapper;

    @Autowired
    public ReporterServices(Map<String, FieldForReporter> fieldsMapper) {
        this.fieldsMapper = fieldsMapper;
    }

    public Boolean createReport(String sprint, String reportName) {
        try {
            String query = loadQueryFromFile(reportName + ".sql");
            query = query.replaceAll("sprintparam", sprint);
            Connection connection = getConnection();
            if (connection != null) {
                try (ResultSet resultSet = connection.createStatement().executeQuery(query)) {
                    generateFileReport(resultSet, reportName + "_" + sprint);
                }
                connection.close();
                return true;
            }
        } catch (Exception e) {
            log.error("Error al crear el reporte: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        return false;
    }

    private void generateFileReport(ResultSet resultSet, String reportName) {
        String podAnterior = "";

        // Ruta del archivo existente y la nueva ruta y nombre del archivo
        // Cambia estas rutas según tus necesidades

        File f = new File("reportes");
        if (!f.exists() && !f.mkdir()) {
            log.error("No se pudo crear el directorio 'reportes'.");
            return;
        }

        String outputFile = "reportes/" + generaFilename(reportName); // Nueva ruta y nombre del archivo

        ArrayList<String> headers = getHeader(resultSet);

        try (Workbook workbook = new XSSFWorkbook()) { // Crear un nuevo Workbook
            Sheet sheet = null;
            int rowNumber = 0;
            Map<Integer, CellStyle> fieldStyle = getFieldsStyle(headers, workbook);
            Map<Integer, String> headersMap = null;
            CreationHelper helper = workbook.getCreationHelper();
            int issuecvecolumn = 0;
            while (resultSet.next()) {
                String pod = resultSet.getString("pod");
                if (!pod.equals(podAnterior)) {
                    sheet = workbook.createSheet(pod); // Crear una hoja
                    podAnterior = pod;
                    for (int i = 0; i < 3; i++) {
                        sheet.createRow(i);
                    }
                    headersMap = new HashMap<>();

                    issuecvecolumn = addHeadersToSheet(sheet, headers, getHeaderStyle(workbook));
                    rowNumber = 4;
                }
                insertRowToSheet(sheet, resultSet, rowNumber++, fieldStyle, helper, issuecvecolumn);
            }

            writeToExcelFile(workbook, outputFile);
        } catch (Exception e) {
            log.error("Error al generar el archivo Excel: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    private void writeToExcelFile(Workbook workbook, String outputFile) {
        try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
            workbook.write(fileOut);
            fileOut.flush();
            workbook.close();
            log.info("Archivo Excel creado exitosamente. " + new File(outputFile).getAbsolutePath());
        } catch (Exception e) {
            log.error("Error al escribir el archivo Excel: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    private void insertRowToSheet(Sheet sheet, ResultSet resultSet, int rowNumber, Map<Integer, CellStyle> fieldStyle, CreationHelper helper, int issuecvecolumn) {
        Row row = sheet.createRow(rowNumber); // Crear una fila
        for (int i = 0; i < 3; i++) {
            row.createCell(i);
        }
        row.setHeight((short) -1);
        int fieldNumber = 0;


        Hyperlink link = helper.createHyperlink(HyperlinkType.URL);


        insertCellsToRow(resultSet, row, fieldNumber, fieldStyle, link, issuecvecolumn);
    }

    private void insertCellsToRow(ResultSet resultSet, Row row, int fieldNumber, Map<Integer, CellStyle> fieldStyle, Hyperlink link, int issuecvecolumn) {
        try {
            while (fieldNumber < resultSet.getMetaData().getColumnCount()) {
                Cell cell = row.createCell(fieldNumber + 3);
                String value = resultSet.getString(++fieldNumber);

                if (fieldNumber == issuecvecolumn + 1) {
                    link.setAddress("https://aeromexico.atlassian.net/browse/" + value);
                    cell.setHyperlink(link);
                }

                if (value == null || value.isEmpty()) {
                    cell.setCellValue("");
                } else if (value.equals("true") || value.equals("false")) {
                    cell.setCellValue(Boolean.parseBoolean(value));
                } else if (value.equals("null")) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue(value);
                }

                cell.setCellStyle(fieldStyle.get(fieldNumber + 4));
            }
        } catch (Exception e) {
            log.error("Error al insertar celdas en la fila: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    private boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int addHeadersToSheet(Sheet sheet, ArrayList<String> headers, CellStyle style) {
        Row headerRow = sheet.createRow(3);
        headerRow.setHeight((short) -1);

        int issuecvecolumn = -2;

        Map<Integer, String> headerMap = new HashMap<>();

        for (int i = 0; i < headers.size(); i++) {

            if (headers.get(i).equals("issuecve")) {
                issuecvecolumn = i;
            }

            FieldForReporter ffr = fieldsMapper.get(headers.get(i) != null ? headers.get(i) : "default") != null ? fieldsMapper.get(headers.get(i) != null ? headers.get(i) : "default") : fieldsMapper.get("default");
            Cell cell = headerRow.createCell(i + 3);
            sheet.setColumnWidth(i + 3, ffr.getLength() * 256);
            cell.setCellValue(ffr.getHeaderName());
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderTop(BorderStyle.THICK);
            style.setBorderBottom(BorderStyle.THICK);
            style.setBorderLeft(BorderStyle.THICK);
            style.setBorderRight(BorderStyle.THICK);

            if (ffr.getHeaderName().equals("Sin definir")) {
                log.error("El campo 'Sin definir' no tiene un nombre de encabezado definido. Por favor, revisa tu configuración. la columna es: " + headers.get(i));
            }
            headerMap.put(i + 3, headers.get(i));
            cell.setCellStyle(style);
        }
        return issuecvecolumn;
    }

    private Map<Integer, CellStyle> getFieldsStyle(ArrayList<String> headers, Workbook workbook) {
        Map<Integer, CellStyle> headerMap = new HashMap<>();

        for (int i = 0; i < headers.size(); i++) {
            CellStyle style = workbook.createCellStyle();
            FieldForReporter field = fieldsMapper.get(headers.get(i) != null ? headers.get(i) : "default") != null ? fieldsMapper.get(headers.get(i) != null ? headers.get(i) : "default") : fieldsMapper.get("default");
            DataFormat format = workbook.createDataFormat();
            if (field.getType() == null) {
                style.setDataFormat(format.getFormat("@")); // Formato de texto por defecto
            } else if (field.getType().equalsIgnoreCase("Double")) {
                style.setDataFormat(format.getFormat("#,##0.00")); // Formato numérico
            } else if (field.getType().equalsIgnoreCase("currency")) {
                style.setDataFormat(format.getFormat("$#,##0.00")); // Formato de moneda
            } else if (field.getType().equalsIgnoreCase("Percentage")) {
                style.setDataFormat(format.getFormat("0.00%")); // Formato de texto por defecto
            }
            Font font = getFontStyle(workbook, field);
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.valueOf(field.getAlignment() != null ? field.getAlignment().toUpperCase() : "GENERAL"));
            style.setWrapText(true);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderTop(BorderStyle.valueOf(field.getBorder() != null ? field.getBorder().toUpperCase() : "NONE"));
            style.setBorderBottom(BorderStyle.valueOf(field.getBorder() != null ? field.getBorder().toUpperCase() : "NONE"));
            style.setBorderLeft(BorderStyle.valueOf(field.getBorder() != null ? field.getBorder().toUpperCase() : "NONE"));
            style.setBorderRight(BorderStyle.valueOf(field.getBorder() != null ? field.getBorder().toUpperCase() : "NONE"));

            headerMap.put(i + 5, style);
        }
        return headerMap;
    }

    private Font getFontStyle(Workbook workbook, FieldForReporter field) {
        Font font = workbook.createFont();
        font.setBold(field.isBold());
        font.setItalic(field.isItalic());
        if (field.getUnderline() != null) {
            font.setUnderline((byte) UnderLine.fromString(field.getUnderline()));
        }
        font.setCharSet(Font.DEFAULT_CHARSET);
        font.setFontName(field.getFontName() != null ? field.getFontName() : "Arial");
        font.setFontHeightInPoints(field.getFontHeight() != null ? Short.parseShort(field.getFontHeight()) : (short) 10);
        return font;
    }

    private CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true); // Aplicar negrita
        font.setFontHeightInPoints((short) 12); // Tamaño de la fuente
        style.setFont(font);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
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
            log.error("Error al obtener los encabezados del ResultSet: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        return headers;
    }

    private String generaFilename(String reportName) {
        GregorianCalendar gc = new GregorianCalendar();
        return reportName + "_" + gc.toZonedDateTime().toLocalDate() + ".xlsx";
    }

    private Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return java.sql.DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            log.error("Error al conectar a la base de datos: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    private String loadQueryFromFile(String fileName) throws IOException {
        InputStream inputStream = new ClassPathResource("queries/" + fileName).getInputStream();
        return new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A").next();
    }
}
