/*
 * $Id: AlarmInfo.java, 2011-12-9 ����12:29:36 Yao Exp $
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

import com.mars.almtool.utils.GeneralUtils;

/**
 * <p>
 * Title: AlarmInfo
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-9 ����12:29:36
 * @modified [who date description]
 * @check [who date description]
 */
public class AlarmInfo implements Serializable, Comparable<AlarmInfo> {
    private static final long serialVersionUID = -8667056694326427282L;

    private String alarmId;
    private String alarmCause;
    private String alarmSource;
    private String alarmName;
    private String alarmCode;
    private String alarmDescription;

    /**
     * @return the alarmId
     */
    public String getAlarmId() {
        return alarmId;
    }

    /**
     * @param alarmId
     *            the alarmId to set
     */
    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public void setAlarmId(String alarmType, String alarmNumber) {
        this.alarmId = alarmType + "/" + alarmNumber;
    }

    /**
     * @return the alarmSource
     */
    public String getAlarmSource() {
        return alarmSource;
    }

    /**
     * @param alarmSource
     *            the alarmSource to set
     */
    public void setAlarmSource(String alarmSource) {
        this.alarmSource = alarmSource;
    }

    /**
     * @return the alarmName
     */
    public String getAlarmName() {
        return alarmName;
    }

    /**
     * @param alarmName
     *            the alarmName to set
     */
    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    /**
     * @return the alarmCode
     */
    public String getAlarmCode() {
        return alarmCode;
    }

    /**
     * @param alarmCode
     *            the alarmCode to set
     */
    public void setAlarmCode(String alarmCode) {
        this.alarmCode = alarmCode;
    }

    /**
     * @return the alarmDescription
     */
    public String getAlarmDescription() {
        return alarmDescription;
    }

    /**
     * @param alarmDescription
     *            the alarmDescription to set
     */
    public void setAlarmDescription(String alarmDescription) {
        this.alarmDescription = alarmDescription;
    }

    /**
     * @return the alarmDetail
     */
    public String getAlarmCause() {
        return alarmCause;
    }

    /**
     * @param alarmCause
     *            the alarmDetail to set
     */
    public void setAlarmCause(String alarmCause) {
        this.alarmCause = alarmCause;
    }

    /**
     * result[0] = AlarmType result[1] = AlarmNumber
     * 
     * @return
     */
    public int[] getAlarmType_Num() {
        int[] result = new int[2];
        if (GeneralUtils.validAlarmId(alarmId)) {
            String[] split = alarmId.split("/");
            result[0] = Integer.parseInt(split[0]);
            result[1] = Integer.parseInt(split[1]);
        }
        return result;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(AlarmInfo newAlarm) {
        String alarmId1 = this.getAlarmId();
        String alarmId2 = newAlarm.getAlarmId();

        if (alarmId1.equals(alarmId2))
            return 0;
        String[] alarmIdArr1 = alarmId1.split("/");
        String[] alarmIdArr2 = alarmId2.split("/");
        int alarmIndex1 = Integer.parseInt(alarmIdArr1[0]) * 65536 + Integer.parseInt(alarmIdArr1[1]);
        int alarmIndex2 = Integer.parseInt(alarmIdArr2[0]) * 65536 + Integer.parseInt(alarmIdArr2[1]);
        return (alarmIndex1 > alarmIndex2) ? 1 : -1;
    }

    public static String generateAlarmIndex(String alarmType, String alarmNUmber) {
        try {
            return Integer.parseInt(alarmType) + "/" + Integer.parseInt(alarmNUmber);
        } catch (Exception ex) {
            return null;
        }
    }
}
