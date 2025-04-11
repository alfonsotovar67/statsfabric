package com.orioninc.statsfabric.utilities;

import com.orioninc.statsfabric.dao.InsertDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Mapper {
    private static final LocalDateTime LDT_NULL = LocalDateTime.of(1900, 1, 1, 0, 0, 0);

    private Mapper() {
    }

    public static InsertDao maptoInsertDao(ResultSet resultSet, String maxSprintActualValue) throws SQLException {
        InsertDao insertDao = new InsertDao();
        insertDao.setCve(getString(resultSet, "issuecve"));
        insertDao.setState(getString(resultSet, "state"));
        insertDao.setPriority(getString(resultSet, "priority"));
        insertDao.setAssignperson(getString(resultSet, "assignperson"));
        insertDao.setLastmodified(getTimestamp(resultSet, "lastmodified"));
        insertDao.setLastview(getTimestamp(resultSet, "lastview"));
        insertDao.setFinalizeddate(getTimestamp(resultSet, "finalizeddate"));
        insertDao.setExpirationdate(getTimestamp(resultSet, "expirationdate"));
        insertDao.setCarryover(getString(resultSet, "carryover"));
        insertDao.setCarryoverdetail(getString(resultSet, "carryoverdetail"));
        insertDao.setCarryoverdesviation(resultSet.getLong("carryoverdesviation"));
        insertDao.setCarryoverrecoverytime(getString(resultSet, "carryoverrecoverytime"));
        insertDao.setCodequality(getString(resultSet, "codequality"));
        insertDao.setFechaFinalizacion(getTimestamp(resultSet, "fechafinalización"));
        insertDao.setFechaupdatecomprometida(getTimestamp(resultSet, "fechaupdatecomprometida"));
        insertDao.setHoursindevreworks(resultSet.getLong("hoursindevreworks"));
        insertDao.setHoursininprogress(resultSet.getLong("hoursininprogress"));
        insertDao.setKoursinonhold(resultSet.getLong("koursinonhold"));
        insertDao.setKissrecoverytime(getString(resultSet, "kissrecoverytime"));
        insertDao.setLatechanges(resultSet.getLong("latechanges"));
        insertDao.setLeadtime(resultSet.getLong("leadtime"));
        insertDao.setLeadtimeforchangeshrs(resultSet.getLong("leadtimeforchangeshrs"));
        insertDao.setSprint(getString(resultSet, "sprint"));
        insertDao.setSprintactual(maxSprintActualValue);
        insertDao.setSprintappactual(getString(resultSet, "sprintappactual"));
        insertDao.setSprintappplanned(getString(resultSet, "sprintappplanned"));
        insertDao.setSprintenddate(getTimestamp(resultSet, "sprintenddate"));
        insertDao.setSprintplanned(getString(resultSet, "sprintplanned"));
        insertDao.setSprintstartdate(getTimestamp(resultSet, "sprintstartdate"));
        insertDao.setStartdate(getTimestamp(resultSet, "startdate"));
        insertDao.setTimeindevrework(getString(resultSet, "timeindevrework"));
        insertDao.setTimeininprogress(getString(resultSet, "timeininprogress"));
        insertDao.setTimeinonhold(getString(resultSet, "timeinonhold"));
        insertDao.setPod(getString(resultSet, "pod"));
        insertDao.setBusinessdata(getString(resultSet, "businessdata"));
        insertDao.setMetrics(getString(resultSet, "metrics"));
        insertDao.setFunctionalrequirements(getString(resultSet, "functionalrequirements"));
        insertDao.setErrorexpectedbehavior(getString(resultSet, "errorexpectedbehavior"));
        insertDao.setUxdesignfigma(getString(resultSet, "uxdesignfigma"));
        insertDao.setCriteriaofacceptance(getString(resultSet, "criteriaofacceptance"));
        insertDao.setComponentsdor(getString(resultSet, "componentsdor"));
        insertDao.setNonfunctionalrequirements(getString(resultSet, "nonfunctionalrequirements"));
        insertDao.setErrorhandling(getString(resultSet, "errorhandling"));
        insertDao.setMitigateddependency(getString(resultSet, "mitigateddependency"));
        insertDao.setMicroservicescontract(getString(resultSet, "microservicescontract"));
        insertDao.setTestingstrategy(getString(resultSet, "testingstrategy"));
        insertDao.setSpecialpnrgeneration(getString(resultSet, "specialpnrgeneration"));
        return insertDao;
    }

    private static String getString(ResultSet resultSet, String columnLabel) throws SQLException {
        return resultSet.getString(columnLabel);
    }

    private static LocalDateTime getTimestamp(ResultSet resultSet, String columnLabel) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(columnLabel);
        return timestamp != null ? timestamp.toLocalDateTime() : LDT_NULL;
    }
}