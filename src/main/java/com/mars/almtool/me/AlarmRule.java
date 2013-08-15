/*
 * $Id: AlarmRule.java, 2013-4-16 ����10:38:31 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.me;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mars.almtool.me.enums.AlarmSeverity;

/**
 * <p>
 * Title: AlarmRule
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2013-4-16 ����10:38:31
 * @modified [who date description]
 * @check [who date description]
 */
public class AlarmRule implements Serializable {
    private static final long serialVersionUID = -8613530238935842091L;

    /**
     * AlarmIndex = AlarmType*65536 + AlarmNumber 0 means "ALL"
     */
    private int alarmIndex = 0;

    private AlarmSeverity severity = AlarmSeverity.indeterminate;

    /**
     * @return the alarmIndex
     */
    public int getAlarmIndex() {
        return alarmIndex;
    }

    public int[] getAlarmId() {
        int alarmtype = alarmIndex / 65536;
        int alarnumber = alarmIndex - alarmtype * 65536;
        int[] alarmId = new int[2];
        alarmId[0] = alarmtype;
        alarmId[1] = alarnumber;
        return alarmId;
    }

    /**
     * @param alarmIndex
     *            the alarmIndex to set
     */
    public void setAlarmIndex(int alarmIndex) {
        this.alarmIndex = alarmIndex;
    }

    /**
     * @return the severity
     */
    public AlarmSeverity getSeverity() {
        return severity;
    }

    /**
     * @param severity
     *            the severity to set
     */
    public void setSeverity(AlarmSeverity severity) {
        this.severity = severity;
    }

    public static AlarmSeverity getFreeSeverity(List<AlarmRule> ruleList) {
        AlarmSeverity freeValue = AlarmSeverity.indeterminate;
        Set<Integer> severityList = new HashSet<Integer>();
        for (AlarmRule rule : ruleList) {
            if (rule.getSeverity().getValue() <= 5) {
                severityList.add(rule.getSeverity().getValue());
            }
        }
        for (int sid = 5; sid >= 1; sid--) {
            if (severityList.contains(sid))
                continue;
            freeValue = AlarmSeverity.getAlarmSeverity(sid);
            break;
        }
        return freeValue;
    }

    public static boolean isSeverityConflict(int severity, List<AlarmRule> ruleList) {
        for (AlarmRule rule : ruleList) {
            if (rule.getSeverity().getValue() == severity)
                return true;
        }
        return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AlarmRule) {
            AlarmRule tmprule = (AlarmRule) obj;
            return tmprule.getAlarmIndex() == this.alarmIndex && tmprule.getSeverity() == this.severity;
        }
        return false;
    }
}
