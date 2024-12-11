package com.orioninc.statsfabric.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserHistories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String resumen;
    private String issueId;
    private String issueType;
    private String state;
    private String projectId;
    private String nombredelproyecto;
    private String priority;
    private String assignPerson;
    private String informador;
    private String createPerson;

/*    @Column(columnDefinition = "DATETIME")
    private LocalDateTime createdDate;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime lastModified;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime lastview;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime finalizeddate;
    private String fixedversion;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> component;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime expirationDate;
    private String Etiquetas;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String envioroments;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> interestedPersons;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> issuelinkinternal;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> issuelinkoutgoing;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> issuelinkinternalcloner;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> issuelinkoutgoingcloner;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> issuelinkinternalissuesplit;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> issuelinkoutgoingissuesplit;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> issuelinkinternalproblem;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> issuelinkoutgoingproblem;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> issuelinkinternalrelates;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> issuelinkoutgoingrelates;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> attachments;
    private double DOR;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> ambiente;
    private String assigneeqa;
    private String businessdata;
    private String businessidea;
    private String PO;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> canal;
    private String cardtype;
    private String carryover;
    private String carryoverdetail;
    private int carryoverdesviation;
    private String carryoverrecoverytime;
    private String causeoferrer;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime codingstartdate;
    private int complejidad;
    private String componentsdor;
    private String comprometida;
    private String criteriaofacceptance;
    private String definedenviroment;
    private String desarrollointegradoparapruebasyliberación;
    private String Domain;
    private String epicLink;
    private String epiclinkresumen;
    private String errorexpectedbehavior;
    private String ErrorHandling;
    private String Excepción;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime fechareadytoreview;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime fechacompromiso;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime fechafinalización;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime fechaupdatecomprometida;
    private String flujo;
    private String functionalrequirements;
    private int hoursindevreworks;
    private int hoursininprogress;
    private int koursinonhold;
    private String IROPstranstype;
    private String iniciativa;
    private String initiative;
    private String issuecolor;
    private String J2Ctipoincidencia;
    private String Journey;
    private String KPI;
    private int Latechanges;
    private int Leadtimeforchangeshrs;
    private String LimitedScope;
    private String Metrics;
    private String Microservicescontract;
    private String Mitigateddependency;
    private String Nodecasosdeprueba;
    private String Nonfunctionalrequirements;
    private String Onkoldreason;
    private String POD;
    private String Persistenterror;
    private String Prioridadbacklog;
    private String Projectbenefitold;
    private String Projectcostold;
    private String Pruebas;
    private String QAlead;
    private String Quarter;
    private String ranked;
    private String Refinedcard;
    private int Releasedeviation;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime Releasedateactual;
    private String Releaseversion;
    private String Reportadopor;
    private String Resolución;
    private String Softwarefactorymember;
    private String SpecialPNRGeneration;
    private String Sprint;
    private String SprintActual;
    private String SprintAppActual;
    private String SprintAppPlanned;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime SprintEndDate;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime SprintPlanned;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime SprintStartDate;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime Startdate;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime StoryPointReestimation;
    private int StoryPoints;
    private int StoryPointsQA;
    private String TechnicalLead;
    private String TechnicalSolution;
    private String TechnicalSolutionDetail;
    private String TestingStrategy;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime TimeinDevRework;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime TimeinInProgress;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime TimeinOnHold;
    private String TipodeDesarrollo;
    private String UXDesignfigma;
    private String UserStoryType;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime DateofFirstResponse;
    private String TimeinStatus;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> Comentario;
    private String Categoríadeestado;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime Categoríadeestadocambiada;
    private String Principal;
    private String Parentsummary;*/
}
