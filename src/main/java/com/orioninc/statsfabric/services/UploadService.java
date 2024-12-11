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
        for (Integer cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
            String comment = row.getCell(cellNum).getStringCellValue();
            if (mapa.containsKey(comment)) {
                InformationSchemaColumns field = mapa.get(comment);
                dBFileld.put(cellNum, field);
            } else {
                InformationSchemaColumns field = new InformationSchemaColumns(comment, null);
                dBFileld.put(cellNum, field);
            }
        }
        return dBFileld;
    }

    public void updateData(Sheet sheet) {
        for (Integer rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {

        }
    }
}
