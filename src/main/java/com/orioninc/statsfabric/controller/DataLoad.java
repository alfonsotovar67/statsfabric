package com.orioninc.statsfabric.controller;

import com.orioninc.statsfabric.constant.Constant;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class DataLoad {

    @GetMapping("/upload")
    public Boolean uploade(@RequestParam String file) {
        try (FileInputStream fis = new FileInputStream(Constant.DIRECTORY+file)) {
            Workbook wb = new XSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
