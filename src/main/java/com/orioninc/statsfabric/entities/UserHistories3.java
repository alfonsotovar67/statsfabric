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
public class UserHistories3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String issueId;
    private String tipoDeDesarrollo;
    private String uXDesignFigma;
    private String userStoryType;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime dateOfFirstResponse;
    private String timeInStatus;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> comentario;
    private String categoriaDeEstado;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime categoriaDeEstadoCambiada;
    private String principal;
    private String parentSummary;

}
