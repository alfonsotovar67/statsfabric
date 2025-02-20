package com.orioninc.statsfabric.controller;

import com.orioninc.statsfabric.constant.Constant;
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

@RestController
public class DataLoad {

    private static final Log log = LogFactory.getLog(DataLoad.class);

    private UploadService uploadService;

    @Autowired
    public DataLoad(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @GetMapping("/upload")
    public String upload(@RequestParam String file) {

        try (FileInputStream fis = new FileInputStream(Constant.DIRECTORY+file)) {
            Workbook wb = new XSSFWorkbook(fis);
            Sheet sh = wb.getSheetAt(0);
            return uploadService.uploadFile(sh);
        } catch (Exception e) {
            log.error("Error al cargar el archivo: " + e.getMessage());
            e.printStackTrace();
            return "Error al cargar el archivo";
        }
    }
}
