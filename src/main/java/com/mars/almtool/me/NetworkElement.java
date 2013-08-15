/*
 * $Id: NetworkElement.java, 2013-4-16 ����10:19:55 Yao Exp $
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

import org.snmp4j.mp.SnmpConstants;

/**
 * <p>
 * Title: NetworkElement
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2013-4-16 ����10:19:55
 * @modified [who date description]
 * @check [who date description]
 */
public class NetworkElement implements Serializable {
    private static final long serialVersionUID = 7274766274937998281L;
    
    private String ipAddress = "127.0.0.1";
    private String community = "public";
    private int port = 161;
    private int timeout = 3000;
    private int snmpVersion = SnmpConstants.version2c;
    private boolean online = true;
    
    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }
    /**
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    /**
     * @return the community
     */
    public String getCommunity() {
        return community;
    }
    /**
     * @param community the community to set
     */
    public void setCommunity(String community) {
        this.community = community;
    }
    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }
    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }
    /**
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }
    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    /**
     * @return the snmpVersion
     */
    public int getSnmpVersion() {
        return snmpVersion;
    }
    /**
     * @param snmpVersion the snmpVersion to set
     */
    public void setSnmpVersion(int snmpVersion) {
        this.snmpVersion = snmpVersion;
    }
    /**
     * @return the online
     */
    public boolean isOnline() {
        return online;
    }
    /**
     * @param online the online to set
     */
    public void setOnline(boolean online) {
        this.online = online;
    }
    
    
}
