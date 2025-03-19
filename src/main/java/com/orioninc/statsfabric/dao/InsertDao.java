package com.orioninc.statsfabric.dao;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Data
public class InsertDao {
    String cve;
    String state;
    String priority;
    String assignperson;
    LocalDateTime lastmodified;
    LocalDateTime lastview;
    LocalDateTime finalizeddate;
    LocalDateTime expirationdate;
    String carryover;
    String carryoverdetail;
    Long carryoverdesviation;
    String carryoverrecoverytime;
    String codequality;
    LocalDateTime fechaFinalizacion;
    LocalDateTime fechaupdatecomprometida;
    Long hoursindevreworks;
    Long hoursininprogress;
    Long koursinonhold;
    String kissrecoverytime;
    Long latechanges;
    Long leadtime;
    Long leadtimeforchangeshrs;
    String sprint;
    String sprintactual;
    String sprintappactual;
    String sprintappplanned;
    LocalDateTime sprintenddate;
    String sprintplanned;
    LocalDateTime sprintstartdate;
    LocalDateTime startdate;
    String timeindevrework;
    String timeininprogress;
    String timeinonhold;
    String pod;
}
