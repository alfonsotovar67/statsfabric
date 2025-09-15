package com.orioninc.statsfabric.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orioninc.statsfabric.pojo.FieldForReporter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class FieldsReporterServices {
    private static final Log log = LogFactory.getLog(FieldsReporterServices.class);

    public LinkedHashMap<String, FieldForReporter> createFieldsMapper() throws IOException {

        String content;
        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("fields.json")) {
            assert inputStream != null;
            content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FileNotFoundException("Archivo no encontrado en el classpath");
        }

        LinkedHashMap<String, LinkedHashMap> fieldsMap = objectMapper.readValue(content, LinkedHashMap.class);
        LinkedHashMap<String, FieldForReporter> fields = new LinkedHashMap();
        for (Map.Entry<String, LinkedHashMap> entry : fieldsMap.entrySet()) {
            ObjectMapper mapper = new ObjectMapper();
            FieldForReporter field = mapper.convertValue(entry.getValue(), FieldForReporter.class);
            fields.put(entry.getKey(), field);
        }
        return fields;
    }
}
