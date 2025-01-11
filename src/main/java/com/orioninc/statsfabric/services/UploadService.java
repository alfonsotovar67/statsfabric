package com.orioninc.statsfabric.services;

import com.orioninc.statsfabric.dao.InformationSchemaColumns;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UploadService {

    final Map<String, InformationSchemaColumns> mapa;

    @Autowired
    public UploadService(Map<String, InformationSchemaColumns> mapa) {
        this.mapa = mapa;
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

    public void updateData(Sheet sheet, Map<Integer, InformationSchemaColumns> dBFileld) {
        for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
            for (int cellNum = 0; cellNum < sheet.getRow(rowNum).getLastCellNum(); cellNum++) {
                String cellValue = sheet.getRow(rowNum).getCell(cellNum).getStringCellValue();
                InformationSchemaColumns field = dBFileld.get(cellNum);
            }
        }
    }
}
