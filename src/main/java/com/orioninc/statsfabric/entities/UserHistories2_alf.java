package com.orioninc.statsfabric.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UserHistories2_alf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String issueid;
    private int complejidad;
    private String componentsdor;
    private String comprometida;
    private String criteriaofacceptance;
    private String definedenviroment;
    private String desarrollointegradoparapruebasyliberacion;
    private String domain;
    private String epicLink;
    private String epiclinkresumen;
    private String errorexpectedbehavior;
    private String errorHandling;
    private String excepcion;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime fechareadytoreview;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime fechacompromiso;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime fechaFinalizacion;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime fechaupdatecomprometida;
    private String flujo;
    private String functionalrequirements;
    private int hoursindevreworks;
    private int hoursininprogress;
    private int koursinonhold;
    private String iropstranstype;
    private String iniciativa;
    private String initiative;
    private String issuecolor;
    private String j2Ctipoincidencia;
    private String journey;
    private String kpi;
    private int latechanges;
    private int leadtimeforchangeshrs;
    private String limitedScope;
    private String metrics;
    private String microservicescontract;
    private String mitigateddependency;
    private String nodecasosdeprueba;
    private String nonfunctionalrequirements;
    private String onkoldreason;
    private String pod;
    private String persistenterror;
    private String prioridadbacklog;
    private String projectbenefitold;
    private String projectcostold;
    private String pruebas;
    private String qalead;
    private String quarter;
    private String ranked;
    private String refinedcard;
    private int releasedeviation;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime releaseDateActual;
    private String releaseversion;
    private String reportadopor;
    private String resolucion;
    private String softwarefactorymember;
    private String specialPNRGeneration;
    private String sprint;
    private String sprintActual;
    private String sprintAppActual;
    private String sprintAppPlanned;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime sprintEndDate;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime sprintPlanned;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime sprintStartDate;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime startdate;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime storypointreestimation;
    private int storypoints;
    private int storypointsQA;
    private String technicalLead;
    private String technicalSolution;
    private String technicalSolutionDetail;
    private String testingStrategy;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime timeinDevRework;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime timeinInProgress;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime timeinOnHold;

}
