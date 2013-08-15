/*
 * $Id: DbUtils.java, 2011-12-8 ����2:13:33 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.me.AlarmInfo;
import com.mars.almtool.me.GlobalConfig;
import com.mars.almtool.me.enums.ImportMode;
import com.mars.almtool.me.enums.MappingType;

/**
 * <p>
 * Title: DbUtils
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-8 ����2:13:33
 * @modified [who date description]
 * @check [who date description]
 */
public class DbUtils {
    public static String generalDbServer(GlobalConfig config) {
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://");
        sb.append(config.getServerIp());
        sb.append(":");
        sb.append(config.getServerPort());
        sb.append("/");
        sb.append(config.getDbName());
        sb.append("?");
        sb.append("user=");
        sb.append(config.getUserName());
        sb.append("&");
        sb.append("password=");
        sb.append(config.getPassWord());
        return sb.toString();
    }

    public static List<String> generalSqlList(Map<String, Map<String, AlarmInfo>> infoMap) {
        GlobalConfig config = ClientContext.getConfig();
        List<String> sqlList = new ArrayList<String>();

        // clear alarmDifinition table before inport data
        StringBuilder sbreset = new StringBuilder();
        sbreset.append("update ");
        sbreset.append(config.getTableName());
        sbreset.append(" set ");
        sbreset.append(config.getCustomCode());
        sbreset.append("='',");
        sbreset.append(config.getCustomName());
        sbreset.append("='',");
        sbreset.append(config.getCustomDescription());
        sbreset.append("=''");
        sqlList.add(sbreset.toString());

        Map<String, AlarmInfo> ctcAlarm = infoMap.get(MappingType.CTC.name());
        Map<String, AlarmInfo> ctcAlarm_mdu = infoMap.get(MappingType.CTC_MDU.name());
        Map<String, AlarmInfo> emsAlarm = infoMap.get(MappingType.EMS.name());
        Map<String, AlarmInfo> neAlarm = infoMap.get(MappingType.NE.name());

        if (config.getImportMode() == ImportMode.CTC) {
            for (AlarmInfo info : ctcAlarm.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append("update ");
                sb.append(config.getTableName());
                sb.append(" set ");
                sb.append(config.getCustomCode());
                sb.append("='");
                sb.append(info.getAlarmCode());
                sb.append("',");
                sb.append(config.getCustomName());
                sb.append("='");
                sb.append(info.getAlarmName());
                sb.append("',");
                sb.append(config.getCustomDescription());
                sb.append("='");
                sb.append(info.getAlarmDescription());
                sb.append("' where ");
                if (FileUtils.EMS_ALARM.equals(info.getAlarmId())) {
                    String cause = info.getAlarmCause();
                    String[] splits = cause.split("/");
                    sb.append("objectType like '%");
                    sb.append(splits[0]);
                    sb.append("' and ");
                    sb.append("probableCause like '");
                    sb.append(splits[1]);
                    sb.append("%'");
                    if (splits.length > 2) {
                        sb.append(" and ");
                        sb.append("specificProblem like '");
                        sb.append(splits[2]);
                        sb.append("%'");
                    }
                } else {
                    sb.append("neSpecificId='");
                    sb.append(info.getAlarmId());
                    sb.append("'");
                }
                sqlList.add(sb.toString());
            }
            for (AlarmInfo info : ctcAlarm_mdu.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append("update ");
                sb.append(config.getTableName());
                sb.append(" set ");
                sb.append(config.getCustomCode());
                sb.append("='");
                sb.append(info.getAlarmCode());
                sb.append("',");
                sb.append(config.getCustomName());
                sb.append("='");
                sb.append(info.getAlarmName());
                sb.append("',");
                sb.append(config.getCustomDescription());
                sb.append("='");
                sb.append(info.getAlarmDescription());
                sb.append("' where ");
                sb.append("neSpecificId='");
                sb.append(info.getAlarmId());
                sb.append("' and objectType like '%MDU%'");
                sb.append(" or objectType like '%iSAM.3.%'");
                sb.append(" or objectType like '%iSAM.4.0%'");
                sb.append(" or objectType like '%iSAM.4.1%'");
                sqlList.add(sb.toString());
            }
        } else if (config.getImportMode() == ImportMode.TRANS) {
            for (AlarmInfo info : emsAlarm.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append("update ");
                sb.append(config.getTableName());
                sb.append(" set ");
                sb.append(config.getCustomName());
                sb.append("='");
                sb.append(info.getAlarmName());
                sb.append("' where ");
                sb.append("probableCauseMnemonic");
                sb.append(" = '");
                sb.append(info.getAlarmId());
                sb.append("' and ");
                sb.append("neSpecificId IS NULL");
                sqlList.add(sb.toString());
            }

            for (AlarmInfo info : neAlarm.values()) {
                sqlList.add(generaSqlString(info, MappingType.NE));
            }
        }

        return sqlList;
    }

    private static String generaSqlString(AlarmInfo info, MappingType type) {
        GlobalConfig config = ClientContext.getConfig();
        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(config.getTableName());
        sb.append(" set ");
        sb.append(config.getCustomName());
        sb.append("='");
        sb.append(info.getAlarmName());
        sb.append("'");
        sb.append(" where ");
        sb.append("neSpecificId='");
        sb.append(info.getAlarmId());
        sb.append("'");
        return sb.toString();
    }
}
