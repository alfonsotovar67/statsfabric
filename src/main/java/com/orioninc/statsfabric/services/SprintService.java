package com.orioninc.statsfabric.services;

import com.orioninc.statsfabric.pojo.InsertDao;
import com.orioninc.statsfabric.utilities.Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Arrays;

@Service
public class SprintService {

    private static final Log log = LogFactory.getLog(SprintService.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    public void closeSprint(String sprint, String pod, String table) throws SQLException {
        Connection conexion = getDBConnection();
        process(conexion, sprint, pod, table);
        if (conexion != null && !conexion.isClosed()) {
            closeDBConnection(conexion);
        }
    }

    public void process(Connection conexion, String sprint, String pod, String table) throws SQLException {
        String maxSprintActualQuery = "SELECT MAX(b.sprintactual) sprintactual " +
                "FROM user_histories a, user_histories2 b " +
                "WHERE a.issuecve = b.issuecve " +
                "AND a.issuecve NOT LIKE 'CVT%' " +
                "AND sprint IS NOT NULL " +
                "AND sprint LIKE ? " +
                "AND UPPER(pod) LIKE UPPER(?) " +
                "ORDER BY a.issuecve";

        String maxSprintActualValue = null;

        try (PreparedStatement preparedStatement = conexion.prepareStatement(maxSprintActualQuery)) {
            preparedStatement.setString(1, "%" + sprint + "%");
            preparedStatement.setString(2, "%" + pod + "%");
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    maxSprintActualValue = rs.getString("sprintactual");
                }
            }
        }

        if (maxSprintActualValue != null) {
            String deleteQuery = "DELETE FROM " + table + " WHERE sprintactivo = ? AND UPPER(pod) LIKE UPPER(?)";
            try (PreparedStatement preparedStatement = conexion.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, sprint);
                preparedStatement.setString(2, "%" + pod + "%");
                preparedStatement.executeUpdate();
            }
        }

        String selectQuery = "SELECT a.issuecve, state, priority, assignperson, lastmodified, lastview, finalizeddate, " +
                "expirationdate, carryover, carryoverdetail, carryoverdesviation, carryoverrecoverytime, codequality, " +
                "fechafinalización, fechaupdatecomprometida, hoursindevreworks, hoursininprogress, koursinonhold, " +
                "kissrecoverytime, latechanges, leadtime, leadtimeforchangeshrs, sprint, sprintactual, sprintappactual, " +
                "sprintappplanned, sprintenddate, sprintplanned, sprintstartdate, startdate, timeindevrework, " +
                "timeininprogress, pod, timeinonhold, businessdata, metrics, functionalrequirements, errorexpectedbehavior, " +
                "uxdesignfigma, criteriaofacceptance, componentsdor, nonfunctionalrequirements, errorhandling, mitigateddependency, " +
                "microservicescontract, testingstrategy, specialpnrgeneration " +
                "FROM user_histories a, user_histories2 b, user_histories3 c " +
                "WHERE a.issuecve = b.issuecve " +
                "and a.issuecve = c.issuecve " +
                "AND a.issuecve NOT LIKE 'CVT%' " +
                "AND sprint IS NOT NULL " +
                //"AND b.comprometida is not null " +
                "AND sprint LIKE ? " +
                "AND STATE not in ('FINALIZADA','Cancelado') " +
                "AND UPPER(pod) LIKE UPPER(?) " +
                "AND a.issuetype not in ('Defect','Subtarea') " +
                //"AND cardtype is not null " +
                "ORDER BY a.issuecve";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, "%" + sprint + "%");
            preparedStatement.setString(2, "%" + pod + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    InsertDao insertDao = Mapper.maptoInsertDao(resultSet, maxSprintActualValue);
                    insertData(conexion, table, insertDao, sprint);
                }
            }
        } catch (Exception e) {
            log.error("Error al procesar el sprint con la tabla " + table, e);
        }
    }

    private void insertData(Connection conexion, String table, InsertDao insertDao, String sprintactivo) throws SQLException {
        String queryInsert = "INSERT INTO " + table + " (issuecve, state, priority, assignperson, lastmodified, " +
                "lastview, finalizeddate, expirationdate, carryover, carryoverdetail, carryoverdesviation, " +
                "carryoverrecoverytime, codequality, fechafinalización, fechaupdatecomprometida, hoursindevreworks, " +
                "hoursininprogress, koursinonhold, kissrecoverytime, latechanges, leadtime, leadtimeforchangeshrs, " +
                "sprint, sprintactual, sprintappactual, sprintappplanned, sprintenddate, sprintplanned, sprintstartdate, " +
                "startdate, timeindevrework, timeininprogress, timeinonhold, pod, sprintactivo, businessdata, " +
                "metrics, functionalrequirements, errorexpectedbehavior, uxdesignfigma, criteriaofacceptance, " +
                "componentsdor, nonfunctionalrequirements, errorhandling, mitigateddependency, microservicescontract, " +
                "testingstrategy, specialpnrgeneration) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatementInsert = conexion.prepareStatement(queryInsert)) {
            preparedStatementInsert.setString(1, insertDao.getCve());
            preparedStatementInsert.setString(2, insertDao.getState());
            preparedStatementInsert.setString(3, insertDao.getPriority());
            preparedStatementInsert.setString(4, insertDao.getAssignperson());
            preparedStatementInsert.setTimestamp(5, Timestamp.valueOf(insertDao.getLastmodified()));
            preparedStatementInsert.setTimestamp(6, Timestamp.valueOf(insertDao.getLastview()));
            preparedStatementInsert.setTimestamp(7, Timestamp.valueOf(insertDao.getFinalizeddate()));
            preparedStatementInsert.setTimestamp(8, Timestamp.valueOf(insertDao.getExpirationdate()));
            preparedStatementInsert.setString(9, insertDao.getCarryover());
            preparedStatementInsert.setString(10, insertDao.getCarryoverdetail());
            preparedStatementInsert.setLong(11, insertDao.getCarryoverdesviation());
            preparedStatementInsert.setString(12, insertDao.getCarryoverrecoverytime());
            preparedStatementInsert.setString(13, insertDao.getCodequality());
            preparedStatementInsert.setTimestamp(14, Timestamp.valueOf(insertDao.getFechaFinalizacion()));
            preparedStatementInsert.setTimestamp(15, Timestamp.valueOf(insertDao.getFechaupdatecomprometida()));
            preparedStatementInsert.setLong(16, insertDao.getHoursindevreworks());
            preparedStatementInsert.setLong(17, insertDao.getHoursininprogress());
            preparedStatementInsert.setLong(18, insertDao.getKoursinonhold());
            preparedStatementInsert.setString(19, insertDao.getKissrecoverytime());
            preparedStatementInsert.setLong(20, insertDao.getLatechanges());
            preparedStatementInsert.setLong(21, insertDao.getLeadtime());
            preparedStatementInsert.setLong(22, insertDao.getLeadtimeforchangeshrs());
            preparedStatementInsert.setString(23, insertDao.getSprint());
            preparedStatementInsert.setString(24, insertDao.getSprintactual());
            preparedStatementInsert.setString(25, insertDao.getSprintappactual());
            preparedStatementInsert.setString(26, insertDao.getSprintplanned());
            preparedStatementInsert.setTimestamp(27, Timestamp.valueOf(insertDao.getSprintenddate()));
            preparedStatementInsert.setString(28, insertDao.getSprintplanned());
            preparedStatementInsert.setTimestamp(29, Timestamp.valueOf(insertDao.getSprintstartdate()));
            preparedStatementInsert.setTimestamp(30, Timestamp.valueOf(insertDao.getStartdate()));
            preparedStatementInsert.setString(31, insertDao.getTimeindevrework());
            preparedStatementInsert.setString(32, insertDao.getTimeininprogress());
            preparedStatementInsert.setString(33, insertDao.getTimeinonhold());
            preparedStatementInsert.setString(34, insertDao.getPod());
            preparedStatementInsert.setString(35, sprintactivo);
            preparedStatementInsert.setString(36, insertDao.getBusinessdata());
            preparedStatementInsert.setString(37, insertDao.getMetrics());
            preparedStatementInsert.setString(38, insertDao.getFunctionalrequirements());
            preparedStatementInsert.setString(39, insertDao.getErrorexpectedbehavior());
            preparedStatementInsert.setString(40, insertDao.getUxdesignfigma());
            preparedStatementInsert.setString(41, insertDao.getCriteriaofacceptance());
            preparedStatementInsert.setString(42, insertDao.getComponentsdor());
            preparedStatementInsert.setString(43, insertDao.getNonfunctionalrequirements());
            preparedStatementInsert.setString(44, insertDao.getErrorhandling());
            preparedStatementInsert.setString(45, insertDao.getMitigateddependency());
            preparedStatementInsert.setString(46, insertDao.getMicroservicescontract());
            preparedStatementInsert.setString(47, insertDao.getTestingstrategy());
            preparedStatementInsert.setString(48, insertDao.getSpecialpnrgeneration());
            preparedStatementInsert.executeUpdate();
        }
    }

    private Connection getDBConnection() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            log.error("Error al conectar a la base de datos: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        return conexion;
    }

    private void closeDBConnection(Connection conexion) {
        try {
            conexion.close();
        } catch (Exception e) {
            log.error("Error al cerrar la conexión a la base de datos: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
    }

}

