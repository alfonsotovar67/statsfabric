package com.orioninc.statsfabric.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class UserHistories_alf {
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

    @Column(columnDefinition = "DATETIME")
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
    private String etiquetas;

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
    private double dor;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> ambiente;
    private String assigneeqa;
    private String businessdata;
    private String businessidea;
    private String pO;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userHistories2_id", referencedColumnName = "id")
    private UserHistories2_alf userhistories2Alf;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userHistories3_id", referencedColumnName = "id")
    private UserHistories3_alf userhistories3;
}
