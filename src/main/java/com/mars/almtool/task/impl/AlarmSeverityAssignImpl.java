/*
 * $Id: AlarmSeverityAssignImpl.java, 2013-4-16 ����11:00:16 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.task.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.comm.SnmpException;
import com.mars.almtool.me.AlarmRule;
import com.mars.almtool.me.NetworkElement;
import com.mars.almtool.me.enums.AlarmSeverity;
import com.mars.almtool.task.AlarmSeverityAssign;
import com.mars.almtool.task.SnmpOperator;
import com.mars.almtool.task.batch.BatchResult;
import com.mars.almtool.utils.SnmpUtils;

/**
 * <p>
 * Title: AlarmSeverityAssignImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2013-4-16 ����11:00:16
 * @modified [who date description]
 * @check [who date description]
 */
public class AlarmSeverityAssignImpl implements AlarmSeverityAssign {
    private Logger logger = ClientContext.getLogFactory().getLogger(LogFactory.LOG_RESULT);

    private NetworkElement ne = null;

    public AlarmSeverityAssignImpl(NetworkElement ne) {
        this.ne = ne;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.task.AlarmSeverityAssign#doAssign(com.mars.almtool.me.NetworkElement)
     */
    @Override
    public BatchResult doAssign() {
        logger.info("Start scan NE : " + ne.getIpAddress());
        SnmpOperator operator = new SnmpOperator(ne);
        BatchResult result = new BatchResult();
        result.setIpAddr(ne.getIpAddress());
        // Check connectivity
        try {
            operator.getSingleValue(SnmpUtils.device_type_oid);
        } catch (Exception ex) {
            result.setSucceed(false);
            result.setException(new SnmpException(ne.getIpAddress(), SnmpException.SNMP_TIME_OUT));
            return result;
        }

        int freeSeverity = AlarmRule.getFreeSeverity(ClientContext.getRuleList()).getValue();

        // Scan Alarm List
        try {
            VariableBinding[] severityList = operator.getTableColumnValues(SnmpUtils.alarm_severity_oid);
            VariableBinding[] reportList = operator.getTableColumnValues(SnmpUtils.alarm_report_oid);
            if (severityList.length != reportList.length)
                throw new Exception();
            List<String> oids = new ArrayList<String>();
            List<Variable> vars = new ArrayList<Variable>();
            for (int idx = 0; idx < severityList.length; idx++) {
                String oid = severityList[idx].getOid().toString();
                String[] split = oid.split("\\.");
                int alarmIndex = Integer.parseInt(split[split.length - 1]);
                int severity = severityList[idx].getVariable().toInt();
                int alarmReport = reportList[idx].getVariable().toInt();
                for (AlarmRule rule : ClientContext.getRuleList()) {
                    if (rule.getAlarmIndex() == alarmIndex || rule.getAlarmIndex() == 0) {
                        if (rule.getSeverity() == AlarmSeverity.notreport) {
                            if (alarmReport != 2) {
                                oids.add(SnmpUtils.alarm_report_oid + "." + alarmIndex);
                                vars.add(new Integer32(2));
                            }
                        } else if (rule.getSeverity() == AlarmSeverity.other) {
                            if (AlarmRule.isSeverityConflict(severity, ClientContext.getRuleList())) {
                                oids.add(SnmpUtils.alarm_severity_oid + "." + alarmIndex);
                                vars.add(new Integer32(freeSeverity));
                            }
                            if (alarmReport == 2) {
                                oids.add(SnmpUtils.alarm_report_oid + "." + alarmIndex);
                                vars.add(new Integer32(1));
                            }
                        } else if (rule.getSeverity() == AlarmSeverity.current) {
                            if (alarmReport == 2) {
                                oids.add(SnmpUtils.alarm_report_oid + "." + alarmIndex);
                                vars.add(new Integer32(1));
                            }
                        } else {
                            if (severity != rule.getSeverity().getValue()) {
                                oids.add(SnmpUtils.alarm_severity_oid + "." + alarmIndex);
                                vars.add(new Integer32(rule.getSeverity().getValue()));
                            }
                            if (alarmReport == 2) {
                                oids.add(SnmpUtils.alarm_report_oid + "." + alarmIndex);
                                vars.add(new Integer32(1));
                            }
                        }

                        break;
                    }
                }
            }
            if (oids.size() > 0) {
                operator.batchSetValues(oids.toArray(new String[0]), vars.toArray(new Variable[0]));
            }
        } catch (Exception ex) {
            result.setSucceed(false);
            if (ex instanceof SnmpException)
                result.setException(ex);
            else
                result.setException(new SnmpException(ne.getIpAddress(), SnmpException.SNMP_INTERNAL_ERROR));
        }
        return result;
    }

}
