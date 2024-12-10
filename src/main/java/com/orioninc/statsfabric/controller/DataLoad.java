package com.orioninc.statsfabric.controller;

import com.orioninc.statsfabric.constant.Constant;
import com.orioninc.statsfabric.dao.InformationSchemaColumns;
import com.orioninc.statsfabric.services.UploadService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DataLoad {

    private static final Log log = LogFactory.getLog(DataLoad.class);

    private UploadService uploadService;

    @Autowired
    public DataLoad(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @GetMapping("/upload")
    public Map<Integer, InformationSchemaColumns> uploade(@RequestParam String file) {

        try (FileInputStream fis = new FileInputStream(Constant.DIRECTORY+file)) {
            Workbook wb = new XSSFWorkbook(fis);
            Sheet sh = wb.getSheetAt(0);
            Map<Integer, InformationSchemaColumns> dBFileld = uploadService.getDBHeaders(sh);
            return dBFileld;
        } catch (Exception e) {
            log.error("Error al cargar el archivo: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
