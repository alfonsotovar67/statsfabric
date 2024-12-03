package com.orioninc.statsfabric.entities;

import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InformationSchemaColumns {
    @Id
    @Column(name = "COLUMN_NAME")
    private String columnName;
    @Lob
    @Column(name = "COLUMN_COMMENT")
    private String columnComment;
}
