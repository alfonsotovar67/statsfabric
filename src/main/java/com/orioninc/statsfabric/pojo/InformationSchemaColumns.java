package com.orioninc.statsfabric.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class InformationSchemaColumns {
    private String tableName;
    private String columnName;
    private String dataType;
}
