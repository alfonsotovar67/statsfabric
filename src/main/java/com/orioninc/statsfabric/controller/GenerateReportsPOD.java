package com.orioninc.statsfabric.controller;

import com.orioninc.statsfabric.services.ReporterServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenerateReportsPOD {

    private final ReporterServices reportServices;

    @Autowired
    public GenerateReportsPOD(ReporterServices reportServices) {
        this.reportServices = reportServices;
    }


    @GetMapping("/kiss")
    public String kiss(@RequestParam String sprint, @RequestParam String reportName) {
        reportServices.createReport(sprint, reportName);
        return "Kiss " + sprint;
    }

}
