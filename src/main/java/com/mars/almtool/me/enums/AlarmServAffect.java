/*
 * $Id: AlarmServAffect.java, 2012-11-28 ����1:04:16 Yao Exp $
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
 * Title: AlarmServAffect
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2012-11-28 ����1:04:16
 * @modified [who date description]
 * @check [who date description]
 */
public enum AlarmServAffect {
    serviceAffecting(1, "影响"), notServiceAffecting(2, "不影响");

    private int value;
    private String localName;

    AlarmServAffect(int value, String localName) {
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
