/*
 * $Id: AlarmInfoExt.java, 2012-11-11 ����9:00:08 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.me;

import org.apache.commons.lang.StringUtils;

import com.mars.almtool.me.enums.AlarmCategory;
import com.mars.almtool.me.enums.AlarmServAffect;
import com.mars.almtool.me.enums.AlarmSeverity;

/**
 * <p>
 * Title: AlarmInfoExt
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2012-11-11 ����9:00:08
 * @modified [who date description]
 * @check [who date description]
 */
public class AlarmInfoExt extends AlarmInfo {
    private static final long serialVersionUID = -6060166164352707937L;

    private boolean isGpon = false;
    private boolean isEpon = false;
    private boolean isIsam = false;
    private boolean isMdu = false;

    private String alarmKey;
    private String alarmAdvice;

    private AlarmSeverity alarmSeverity = AlarmSeverity.indeterminate;
    private AlarmCategory alarmCategory = AlarmCategory.equipment;
    private AlarmServAffect alarmServAffect = AlarmServAffect.notServiceAffecting;
    private String alarmSourceIndex = "";

    /**
     * @return the alarmSeverity
     */
    public AlarmSeverity getAlarmSeverity() {
        return alarmSeverity;
    }

    /**
     * @param alarmSeverity
     *            the alarmSeverity to set
     */
    public void setAlarmSeverity(AlarmSeverity alarmSeverity) {
        this.alarmSeverity = alarmSeverity;
    }

    /**
     * @return the alarmCategory
     */
    public AlarmCategory getAlarmCategory() {
        return alarmCategory;
    }

    /**
     * @param alarmCategory
     *            the alarmCategory to set
     */
    public void setAlarmCategory(AlarmCategory alarmCategory) {
        this.alarmCategory = alarmCategory;
    }

    /**
     * @return the alarmServAffect
     */
    public AlarmServAffect getAlarmServAffect() {
        return alarmServAffect;
    }

    /**
     * @param alarmServAffect
     *            the alarmServAffect to set
     */
    public void setAlarmServAffect(AlarmServAffect alarmServAffect) {
        this.alarmServAffect = alarmServAffect;
    }

    public AlarmInfoExt(AlarmInfo info) {
        super();
        this.setAlarmId(info.getAlarmId());
        this.setAlarmCause(info.getAlarmCause());
        this.setAlarmSource(info.getAlarmSource());
        this.setAlarmName(info.getAlarmName());
        this.setAlarmCode(info.getAlarmCode());
        this.setAlarmDescription(info.getAlarmDescription());
    }

    public AlarmInfoExt() {
        super();
    }

    /**
     * @return the isGpon
     */
    public boolean isGpon() {
        return isGpon;
    }

    /**
     * @param isGpon
     *            the isGpon to set
     */
    public void setGpon(boolean isGpon) {
        this.isGpon = isGpon;
    }

    /**
     * @return the isEpon
     */
    public boolean isEpon() {
        return isEpon;
    }

    /**
     * @param isEpon
     *            the isEpon to set
     */
    public void setEpon(boolean isEpon) {
        this.isEpon = isEpon;
    }

    /**
     * @return the isIsam
     */
    public boolean isIsam() {
        return isIsam;
    }

    /**
     * @param isIsam
     *            the isIsam to set
     */
    public void setIsam(boolean isIsam) {
        this.isIsam = isIsam;
    }

    /**
     * @return the isMdu
     */
    public boolean isMdu() {
        return isMdu;
    }

    /**
     * @param isMdu
     *            the isMdu to set
     */
    public void setMdu(boolean isMdu) {
        this.isMdu = isMdu;
    }

    /**
     * @return the alarmKey
     */
    public String getAlarmKey() {
        return alarmKey;
    }

    /**
     * @param alarmKey
     *            the alarmKey to set
     */
    public void setAlarmKey(String alarmKey) {
        this.alarmKey = alarmKey;
    }

    /**
     * @return the alarmAdvice
     */
    public String getAlarmAdvice() {
        return alarmAdvice;
    }

    /**
     * @param alarmAdvice
     *            the alarmAdvice to set
     */
    public void setAlarmAdvice(String alarmAdvice) {
        this.alarmAdvice = alarmAdvice;
    }

    public static AlarmSeverity getAlarmSeverity(String src) {
        if (StringUtils.isEmpty(src))
            return AlarmSeverity.indeterminate;
        src = src.toLowerCase().trim();
        if (src.startsWith("i")) {
            return AlarmSeverity.indeterminate;
        } else if (src.startsWith("w")) {
            return AlarmSeverity.warning;
        } else if (src.startsWith("mi")) {
            return AlarmSeverity.minor;
        } else if (src.startsWith("ma")) {
            return AlarmSeverity.major;
        } else if (src.startsWith("c")) {
            return AlarmSeverity.critical;
        } else {
            return AlarmSeverity.indeterminate;
        }
    }

    public static AlarmCategory getAlarmCategory(String src) {
        if (StringUtils.isEmpty(src))
            return AlarmCategory.communications;
        src = src.toLowerCase().trim();
        if (src.startsWith("c")) {
            return AlarmCategory.communications;
        } else if (src.startsWith("q")) {
            return AlarmCategory.qualityofService;
        } else if (src.startsWith("p")) {
            return AlarmCategory.processingError;
        } else if (src.startsWith("eq")) {
            return AlarmCategory.equipment;
        } else if (src.startsWith("e")) {
            return AlarmCategory.environmental;
        } else {
            return AlarmCategory.communications;
        }
    }

    public static AlarmServAffect getAlarmServAffect(String src) {
        if (StringUtils.isEmpty(src))
            return AlarmServAffect.notServiceAffecting;
        src = src.toLowerCase().trim();
        if (src.startsWith("n")) {
            return AlarmServAffect.notServiceAffecting;
        } else if (src.startsWith("s")) {
            return AlarmServAffect.serviceAffecting;
        } else {
            return AlarmServAffect.notServiceAffecting;
        }
    }

    /**
     * @return the alarmSourceIndex
     */
    public String getAlarmSourceIndex() {
        return alarmSourceIndex;
    }

    /**
     * @param alarmSourceIndex the alarmSourceIndex to set
     */
    public void setAlarmSourceIndex(String alarmSourceIndex) {
        this.alarmSourceIndex = alarmSourceIndex;
    }
}
