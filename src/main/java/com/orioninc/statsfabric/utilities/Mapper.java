package com.orioninc.statsfabric.utilities;

import com.orioninc.statsfabric.dao.InsertDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Mapper {

    public static InsertDao maptoInsertDao(ResultSet resultset, String maxSprintActualValue) throws SQLException {
        LocalDateTime ldtnull = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
        InsertDao insertDao = new InsertDao();
        insertDao.setCve(resultset.getString("issuecve") == null ? null : resultset.getString("issuecve"));
        insertDao.setState(resultset.getString("state") == null ? null : resultset.getString("state"));
        insertDao.setPriority(resultset.getString("priority") == null ? null : resultset.getString("priority"));
        insertDao.setAssignperson(resultset.getString("assignperson") == null ? null : resultset.getString("assignperson"));
        insertDao.setLastmodified(resultset.getTimestamp("lastmodified") == null ? ldtnull : resultset.getTimestamp("lastmodified").toLocalDateTime());
        insertDao.setLastview(resultset.getTimestamp("lastview") == null ? ldtnull : resultset.getTimestamp("lastview").toLocalDateTime());
        insertDao.setFinalizeddate(resultset.getTimestamp("finalizeddate") == null ? ldtnull : resultset.getTimestamp("finalizeddate").toLocalDateTime());
        insertDao.setExpirationdate(resultset.getTimestamp("expirationdate") == null ? ldtnull : resultset.getTimestamp("expirationdate").toLocalDateTime());
        insertDao.setCarryover(resultset.getString("carryover") == null ? null : resultset.getString("carryover"));
        insertDao.setCarryoverdetail(resultset.getString("carryoverdetail") == null ? null : resultset.getString("carryoverdetail"));
        insertDao.setCarryoverdesviation(resultset.getLong("carryoverdesviation"));
        insertDao.setCarryoverrecoverytime(resultset.getString("carryoverrecoverytime") == null ? null : resultset.getString("carryoverrecoverytime"));
        insertDao.setCodequality(resultset.getString("codequality") == null ? null : resultset.getString("codequality"));
        insertDao.setFechaFinalización(resultset.getTimestamp("fechafinalización") == null ? ldtnull : resultset.getTimestamp("fechafinalización").toLocalDateTime());
        insertDao.setFechaupdatecomprometida(resultset.getTimestamp("fechaupdatecomprometida") == null ? ldtnull : resultset.getTimestamp("fechaupdatecomprometida").toLocalDateTime());
        insertDao.setHoursindevreworks(resultset.getLong("hoursindevreworks"));
        insertDao.setHoursininprogress(resultset.getLong("hoursininprogress"));
        insertDao.setKoursinonhold(resultset.getLong("koursinonhold"));
        insertDao.setKissrecoverytime(resultset.getString("kissrecoverytime") == null ? null : resultset.getString("kissrecoverytime"));
        insertDao.setLatechanges(resultset.getLong("latechanges"));
        insertDao.setLeadtime(resultset.getLong("leadtime"));
        insertDao.setLeadtimeforchangeshrs(resultset.getLong("leadtimeforchangeshrs"));
        insertDao.setSprint(resultset.getString("sprint") == null ? null : resultset.getString("sprint"));
        insertDao.setSprintactual(maxSprintActualValue);
        insertDao.setSprintappactual(resultset.getString("sprintappactual") == null ? null : resultset.getString("sprintappactual"));
        insertDao.setSprintappplanned(resultset.getString("sprintappplanned") == null ? null : resultset.getString("sprintappplanned"));
        insertDao.setSprintenddate(resultset.getTimestamp("sprintenddate") == null ? ldtnull : resultset.getTimestamp("sprintenddate").toLocalDateTime());
        insertDao.setSprintplanned(resultset.getString("sprintplanned") == null ? null : resultset.getString("sprintplanned"));
        insertDao.setSprintstartdate(resultset.getTimestamp("sprintstartdate") == null ? ldtnull : resultset.getTimestamp("sprintstartdate").toLocalDateTime());
        insertDao.setStartdate(resultset.getTimestamp("startdate") == null ? ldtnull : resultset.getTimestamp("startdate").toLocalDateTime());
        insertDao.setTimeindevrework(resultset.getString("timeindevrework") == null ? null : resultset.getString("timeindevrework"));
        insertDao.setTimeininprogress(resultset.getString("timeininprogress") == null ? null : resultset.getString("timeininprogress"));
        insertDao.setTimeinonhold(resultset.getString("timeinonhold") == null ? null : resultset.getString("timeinonhold"));
        insertDao.setPod(resultset.getString("pod") == null ? null : resultset.getString("pod"));

        return insertDao;
    }
}
