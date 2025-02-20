package com.orioninc.statsfabric.dao;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class RowData {
    private String tableName;
    private String columnName;
    private String dataType;
    private Object value;
}
