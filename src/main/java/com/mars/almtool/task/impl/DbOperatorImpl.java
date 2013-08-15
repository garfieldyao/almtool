/*
 * $Id: DbOprrtatorImpl.java, 2011-12-9 ����9:53:48 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.task.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.me.AlarmInfo;
import com.mars.almtool.me.AlarmInfoExt;
import com.mars.almtool.me.GlobalConfig;
import com.mars.almtool.task.DbOperator;
import com.mars.almtool.utils.DbUtils;
import com.mars.almtool.utils.FileUtils;

/**
 * <p>
 * Title: DbOperatorImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2013-4-23 下午7:35:01
 * @modified [who date description]
 * @check [who date description]
 */
public class DbOperatorImpl implements DbOperator {
    private Logger logger = ClientContext.getLogger(LogFactory.LOG_RESULT);
    private GlobalConfig config;
    //If true, alarm name will be ProbleCause
    private final static boolean useRowData = false;

    public DbOperatorImpl(GlobalConfig config) {
        this.config = config;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.task.DbOperator#checkDbConnect(com.mars.almtool.me.GlobalConfig)
     */
    @Override
    public boolean checkDbConnect() throws Exception {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(DbUtils.generalDbServer(config));
            stmt = conn.createStatement();
            boolean hasResult = stmt.execute("SHOW COLUMNS FROM " + config.getTableName());
            List<String> result = new ArrayList<String>();
            while (hasResult) {
                ResultSet resultSet = stmt.getResultSet();
                while (resultSet.next()) {
                    String coloumn = resultSet.getString(1);
                    result.add(coloumn);
                }
                hasResult = stmt.getMoreResults();
            }

            if (result.contains(config.getCustomCode()) && result.contains(config.getCustomName())
                    && result.contains(config.getCustomDescription())) {
                return true;
            } else {
                throw new Exception(ClientContext.getI18nString("ex_noexist"));
            }

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
                conn = null;
            }
        }

    }

    /**
     * (non-Javadoc)
     * 
     * @throws Exception
     * @see com.mars.almtool.task.DbOperator#importAlarm(java.util.List)
     */
    @Override
    public boolean importAlarm(List<String> sqlList) throws Exception {
        if (CollectionUtils.isEmpty(sqlList))
            return true;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(DbUtils.generalDbServer(config));
            stmt = conn.createStatement();

            for (String sql : sqlList) {
                logger.info(sql);
                stmt.execute(sql);
            }

            return true;

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
                conn = null;
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.task.DbOperator#mergeAlarmList(java.util.Map,
     *      java.util.Map)
     */
    @Override
    public List<AlarmInfoExt> mergeAlarmList(Map<String, Map<String, String>> trans_res,
            Map<String, AlarmInfo> neAlarms) throws Exception {
        Map<String, AlarmInfoExt> result = new HashMap<String, AlarmInfoExt>();

        Map<String, String> res_cause = trans_res.get(FileUtils.ProbableCause);
        Map<String, String> res_special = trans_res.get(FileUtils.SpecificProblem);
        Map<String, String> res_repair = trans_res.get(FileUtils.RepairActions);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(DbUtils.generalDbServer(config));
            stmt = conn.createStatement();

            StringBuilder sb = new StringBuilder("SELECT ");
            sb.append("neSpecificId,");
            sb.append("probableCause,");
            sb.append("specificProblem,");
            sb.append("repairActions,");
            sb.append("tl1Cause,");
            sb.append("objectType");
            sb.append(" FROM ");
            sb.append(ClientContext.getConfig().getTableName());
            sb.append(" WHERE ");
            sb.append("neSpecificId");
            sb.append(" IS NOT NULL");

            ResultSet query = stmt.executeQuery(sb.toString());

            while (query.next()) {
                String neSpecificId = query.getString("neSpecificId");
                String objectType = query.getString("objectType");
                String probableCause = trimString(query.getString("probableCause"));
                String specificProblem = trimString(query.getString("specificProblem"));
                String repairActions = trimString(query.getString("repairActions"));
                String tl1Cause = trimString(query.getString("tl1Cause"));

                if (StringUtils.isEmpty(objectType) || StringUtils.isEmpty(neSpecificId))
                    continue;

                boolean isGpon = false;
                boolean isEpon = false;
                boolean isMdu = false;
                boolean isIsam = false;

                if (objectType.contains("GPON")) {
                    isGpon = true;
                } else if (objectType.contains("EPON")) {
                    isEpon = true;
                } else if (objectType.contains("MDU")) {
                    isMdu = true;
                } else {
                    isIsam = true;
                }

                if (neAlarms.containsKey(neSpecificId)) {
                    AlarmInfoExt infoExt = null;
                    if (result.containsKey(neSpecificId)) {
                        infoExt = result.get(neSpecificId);
                    } else {
                        infoExt = new AlarmInfoExt(neAlarms.get(neSpecificId));
                        result.put(neSpecificId, infoExt);
                    }
                    // ref AMS db info
                    infoExt.setGpon(infoExt.isGpon() || isGpon);
                    infoExt.setEpon(infoExt.isEpon() || isEpon);
                    infoExt.setMdu(infoExt.isMdu() || isMdu);
                    infoExt.setIsam(infoExt.isIsam() || isIsam);
                    infoExt.setAlarmKey(tl1Cause);
                    if (StringUtils.isNotEmpty(specificProblem)) {
                        String res_sp = res_special.get(specificProblem);
                        if (StringUtils.isNotEmpty(res_sp)) {
                            infoExt.setAlarmDescription(res_sp);
                        }
                    } else if (StringUtils.isNotEmpty(probableCause)) {
                        String res_sp = res_cause.get(probableCause);
                        if (StringUtils.isNotEmpty(res_sp)) {
                            infoExt.setAlarmDescription(res_sp);
                        }
                    } else {
                        infoExt.setAlarmDescription(infoExt.getAlarmName());
                    }

                    if (StringUtils.isNotEmpty(repairActions)) {
                        String res_sp = res_repair.get(repairActions);
                        if (StringUtils.isNotEmpty(res_sp)) {
                            infoExt.setAlarmAdvice(res_sp);
                        }
                    }
                } else {
                    AlarmInfoExt infoExt = null;
                    if (result.containsKey(neSpecificId)) {
                        infoExt = result.get(neSpecificId);
                    } else {
                        infoExt = new AlarmInfoExt();
                        result.put(neSpecificId, infoExt);
                    }
                    // ref AMS db info
                    infoExt.setGpon(infoExt.isGpon() || isGpon);
                    infoExt.setEpon(infoExt.isEpon() || isEpon);
                    infoExt.setMdu(infoExt.isMdu() || isMdu);
                    infoExt.setIsam(infoExt.isIsam() || isIsam);
                    infoExt.setAlarmKey(tl1Cause);
                    infoExt.setAlarmId(neSpecificId);
                    if (StringUtils.isNotEmpty(specificProblem)) {
                        String res_sp = res_special.get(specificProblem);
                        if (StringUtils.isNotEmpty(res_sp)) {
                            infoExt.setAlarmDescription(res_sp);
                        } else {
                            infoExt.setAlarmDescription(specificProblem);
                        }
                    } else if (StringUtils.isNotEmpty(probableCause)) {
                        String res_sp = res_cause.get(probableCause);
                        if (StringUtils.isNotEmpty(res_sp)) {
                            infoExt.setAlarmDescription(res_sp);
                        } else {
                            infoExt.setAlarmDescription(probableCause);
                        }
                    }

                    if (StringUtils.isEmpty(infoExt.getAlarmName())) {
                        if (useRowData) {
                            if (StringUtils.isNotEmpty(probableCause)) {
                                String res_sp = res_cause.get(probableCause);
                                if (StringUtils.isNotEmpty(res_sp)) {
                                    infoExt.setAlarmName(res_sp);
                                } else {
                                    infoExt.setAlarmName(probableCause);
                                }
                            }
                        } else {
                            infoExt.setAlarmName(infoExt.getAlarmDescription());
                        }
                    }

                    if (StringUtils.isNotEmpty(repairActions)) {
                        String res_sp = res_repair.get(repairActions);
                        if (StringUtils.isNotEmpty(res_sp)) {
                            infoExt.setAlarmAdvice(res_sp);
                        } else {
                            infoExt.setAlarmAdvice(repairActions);
                        }
                    }
                }

            }

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
                conn = null;
            }
        }

        List<AlarmInfoExt> list = new ArrayList<AlarmInfoExt>();
        list.addAll(result.values());
        Collections.sort(list);
        return list;
    }

    private String trimString(String src) {
        if (StringUtils.isEmpty(src))
            return "";
        if (src.contains("#")) {
            src = src.substring(0, src.indexOf('#'));
        }
        if (src.contains("%")) {
            src = src.substring(src.indexOf('%') + 1);
        }
        return src;
    }

}
