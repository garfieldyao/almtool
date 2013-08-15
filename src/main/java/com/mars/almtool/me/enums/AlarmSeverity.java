/*
 * $Id: AlarmSeverity.java, 2012-11-26 ����5:25:31 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.me.enums;

import com.mars.almtool.comm.ClientContext;

/**
 * <p>
 * Title: AlarmSeverity
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2012-11-26 ����5:25:31
 * @modified [who date description]
 * @check [who date description]
 */
public enum AlarmSeverity {
    indeterminate(1, "不确定"), warning(2, "警告"), minor(3, "次要"), major(4, "主要"), critical(5, "紧急"), notreport(
            6, "不上报"), other(7, "其它"), current(8, "当前");

    private int value;
    private String localName;

    private AlarmSeverity(int value, String localName) {
        this.setValue(value);
        this.localName = localName;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

    public static AlarmSeverity getAlarmSeverity(int value) {
        for (AlarmSeverity seviriry : AlarmSeverity.values()) {
            if (seviriry.getValue() == value) {
                return seviriry;
            }
        }
        return AlarmSeverity.indeterminate;
    }

    public static AlarmSeverity getAlarmSeverity(String localName) {
        for (AlarmSeverity seviriry : AlarmSeverity.values()) {
            if (seviriry.getLocalName().equals(localName)) {
                return seviriry;
            }
        }
        return AlarmSeverity.indeterminate;
    }

    /**
     * @return the localName
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * @param localName
     *            the localName to set
     */
    public void setLocalName(String localName) {
        this.localName = localName;
    }
    
    public String getTransName() {
        return ClientContext.getConfig().getTrans_lang() == Lanaguage.CN ? localName : this.name();
    }
}
