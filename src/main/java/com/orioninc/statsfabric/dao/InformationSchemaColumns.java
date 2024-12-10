package com.orioninc.statsfabric.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class InformationSchemaColumns {
    private String columnName;
    private String dataType;
}
