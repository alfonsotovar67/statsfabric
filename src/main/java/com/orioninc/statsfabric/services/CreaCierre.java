package com.orioninc.statsfabric.services;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.GregorianCalendar;

public class CreaCierre {

    @Value("${cierre.fileName}")
    private String cierreFileName;

    public void Cierre() {
        Workbook workbook = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;

        // Ruta del archivo existente y la nueva ruta y nombre del archivo
        // Cambia estas rutas según tus necesidades

        File f = new File("./cierre");
        if (!f.exists()) {
            f.mkdir();
        }
        String inputFile = "./cierre/" + cierreFileName + ".xlsx"; // Ruta del archivo existente
        String outputFile = "./resultado/ruta/" + Genera_FileName(cierreFileName); // Nueva ruta y nombre del archivo

        try {
            fis = new FileInputStream(inputFile);
            workbook = new XSSFWorkbook(fis);

            fos = new FileOutputStream(outputFile);
            workbook.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String Genera_FileName(String cierreFileName) {
        GregorianCalendar gc = new GregorianCalendar();
        String fileName = "cierreFileName_" + gc.toZonedDateTime() + ".xlsx";
        return fileName;
    }
}
