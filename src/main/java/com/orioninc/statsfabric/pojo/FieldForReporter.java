package com.orioninc.statsfabric.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldForReporter {
    private String headerName;
    private String type;
    private int length;
    private String alignment;  // "left", "right", "center", "justify", "justify_selection", "Distributed", "Fill"
    private String fontName; // "Arial", "Calibri", "Times New Roman", "Courier New", "Verdana", "Tahoma", "Helvetica", "Georgia", "Impact", "Comic Sans MS"
    private String justification;
    private String border;  // "none", "thin", "medium", "thick", "dashed", "dotted", "double", "hairline", "mediumDashed", "dashDot", "dashDotDot", "mediumDashDot", "mediumDashDotDot", "slantDashDot"
    private String fontHeight;
    private String underline; // "none", "single", "double", "single_accounting", "double_accounting"
    private boolean bold;
    private boolean italic;

}
