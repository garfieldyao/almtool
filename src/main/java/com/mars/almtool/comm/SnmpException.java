/*
 * $Id: SnmpException.java, 2011-11-13 ����7:36:21 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.comm;

/**
 * <p>
 * Title: SnmpException
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-11-13 ����7:36:21
 * @modified [who date description]
 * @check [who date description]
 */
public class SnmpException extends RuntimeException {
    private static final long serialVersionUID = -3402426565708986888L;
    public static final String SNMP_INTERNAL_ERROR = "SNMP Internal Error";
    public static final String SNMP_SET_ERROR = "SNMP Get Error";
    public static final String SNMP_GET_ERROR = "SNMP Set Error";
    public static final String SNMP_TIME_OUT = "SNMP Timeout";
    public static final String NE_DISCONNECT = "NE Disconnect";

    private String error;
    private String source;

    public SnmpException(String source, String error) {
        super();
        this.setError(error);
        this.setSource(source);
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error
     *            the error to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Throwable#toString()
     */
    @Override
    public String toString() {
        return source + ":" + error;
    }

}
