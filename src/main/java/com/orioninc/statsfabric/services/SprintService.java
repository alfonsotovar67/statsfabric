package com.orioninc.statsfabric.services;

import com.orioninc.statsfabric.dao.InsertDao;
import com.orioninc.statsfabric.utilities.Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;

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
        Process(conexion, sprint, pod, table);
        closeDBConnection(conexion);
    }

    public void Process(Connection conexion, String sprint, String pod, String table) throws SQLException {
        String maxSprintActual = "select max(b.sprintactual) sprintactual\n" +
                " from user_histories a, user_histories2 b\n" +
                " where a.issuecve=b.issuecve\n" +
                " and a.issuecve not like 'CVT%'\n" +
                " and sprint is not null\n" +
                " and sprint like '%" + sprint + "%'\n" +
                " and upper(pod) like upper('%" + pod + "%')" +
                " order by a.issuecve";
        PreparedStatement preparedStatement = conexion.prepareStatement(maxSprintActual);
        ResultSet rs = preparedStatement.executeQuery();
        String maxSprintActualValue = null;
        while (rs.next()) {
            maxSprintActualValue = rs.getString("sprintactual");
        }
        preparedStatement.close();

        String sqlget = "DELETE FROM " + table + " WHERE sprintactual = '" + maxSprintActualValue + "'" + " and upper(pod) like upper('%" + pod + "%')";
        preparedStatement = conexion.prepareStatement(sqlget);
        preparedStatement.executeUpdate();
        preparedStatement.close();


        String query = "select \n" +
                "  a.issuecve,\n" +
                "  state,\n" +
                "  priority,\n" +
                "  assignperson,\n" +
                "  lastmodified,\n" +
                "  lastview,\n" +
                "  finalizeddate,\n" +
                "  expirationdate,\n" +
                "  carryover,\n" +
                "  carryoverdetail,\n" +
                "  carryoverdesviation,\n" +
                "  carryoverrecoverytime,\n" +
                "  codequality,\n" +
                "  fechafinalización,\n" +
                "  fechaupdatecomprometida,\n" +
                "  hoursindevreworks,\n" +
                "  hoursininprogress,\n" +
                "  koursinonhold,\n" +
                "  kissrecoverytime,\n" +
                "  latechanges,\n" +
                "  leadtime,\n" +
                "  leadtimeforchangeshrs,\n" +
                "  sprint,\n" +
                "  sprintactual,\n" +
                "  sprintappactual,\n" +
                "  sprintappplanned,\n" +
                "  sprintenddate,\n" +
                "  sprintplanned,\n" +
                "  sprintstartdate,\n" +
                "  startdate,\n" +
                "  timeindevrework,\n" +
                "  timeininprogress,\n" +
                "  pod,\n" +
                "  timeinonhold\n" +
                "from user_histories a, user_histories2 b\n" +
                "where a.issuecve=b.issuecve\n" +
                "and a.issuecve not like 'CVT%'\n" +
                "and sprint is not null\n" +
                "and sprint like '%" + sprint + "%'\n" +
                "and upper(pod) like upper('%" + pod + "%')" +
                "order by a.issuecve;";
        String cve = null;
        String queryInsert = null;
        try {
            preparedStatement = conexion.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                cve = resultSet.getString("issuecve");
                InsertDao insertDao = Mapper.maptoInsertDao(resultSet, maxSprintActualValue);
                cve = insertDao.getCve();
                queryInsert = "INSERT INTO `aeromexico`.`" + table + "`\n" +
                        "(`issuecve`,\n" +
                        "`state`,\n" +
                        "`priority`,\n" +
                        "`assignperson`,\n" +
                        "`lastmodified`,\n" +
                        "`lastview`,\n" +
                        "`finalizeddate`,\n" +
                        "`expirationdate`,\n" +
                        "`carryover`,\n" +
                        "`carryoverdetail`,\n" +
                        "`carryoverdesviation`,\n" +
                        "`carryoverrecoverytime`,\n" +
                        "`codequality`,\n" +
                        "`fechafinalización`,\n" +
                        "`fechaupdatecomprometida`,\n" +
                        "`hoursindevreworks`,\n" +
                        "`hoursininprogress`,\n" +
                        "`koursinonhold`,\n" +
                        "`kissrecoverytime`,\n" +
                        "`latechanges`,\n" +
                        "`leadtime`,\n" +
                        "`leadtimeforchangeshrs`,\n" +
                        "`sprint`,\n" +
                        "`sprintactual`,\n" +
                        "`sprintappactual`,\n" +
                        "`sprintappplanned`,\n" +
                        "`sprintenddate`,\n" +
                        "`sprintplanned`,\n" +
                        "`sprintstartdate`,\n" +
                        "`startdate`,\n" +
                        "`timeindevrework`,\n" +
                        "`timeininprogress`,\n" +
                        "`timeinonhold`,\n" +
                        "`pod`)\n" +
                        "VALUES\n" +
                        "(?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?," +
                        "?);";
                PreparedStatement preparedStatementInsert = conexion.prepareStatement(queryInsert);
                preparedStatementInsert.setString(1, cve);
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
                preparedStatementInsert.setTimestamp(14, Timestamp.valueOf(insertDao.getFechaFinalización()));
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
                preparedStatementInsert.executeUpdate();
                preparedStatementInsert.close();
            }
            preparedStatement.close();
        } catch (Exception e) {
            log.error("Error al procesar el sprint con el tabla " + table + "en el user history:" + cve + " error " + e.getMessage());
            log.error(queryInsert);
            e.printStackTrace();
        }
    }

    private Connection getDBConnection() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conexion;
    }

    private void closeDBConnection(Connection conexion) {
        try {
            conexion.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

