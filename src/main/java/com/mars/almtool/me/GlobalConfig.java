/*
 * $Id: GlobalConfig.java, 2011-12-8 ����2:05:04 Yao Exp $
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
import java.util.ArrayList;
import java.util.List;

import com.mars.almtool.me.enums.ImportMode;
import com.mars.almtool.me.enums.Lanaguage;

/**
 * <p>
 * Title: GlobalConfig
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-8 ����2:05:04
 * @modified [who date description]
 * @check [who date description]
 */
public class GlobalConfig implements Serializable {
    private static final long serialVersionUID = 8888429024577992358L;
    // mysql serer info
    private String serverIp = "127.0.0.1";
    private String userName = "root";
    private String passWord = "mysql";
    private int serverPort = 3307;

    // alarm mapping file info
    private int alarmId = 0;
    private int alarmCause = 1;
    private int alarmSource = 2;
    private int alarmCode = 5;
    private int alarmName = 6;
    private int alarmDescription = 7;

    // db table info
    private String dbName = "emlplatform";
    private String tableName = "AlarmDefinition";
    private String customCode = "alarmCode";
    private String customName = "alarmName";
    private String customDescription = "alarmDescription";

    // For transparent case
    private ImportMode importMode = ImportMode.TRANS;
    private List<String> ctc_tab = new ArrayList<String>();
    private List<String> ems_tab = new ArrayList<String>();
    private List<String> ne_tab = new ArrayList<String>();

    private int transparent_name = 1;
    private int transparent_almtype = 7;
    private int transparent_almnum = 8;

    private int ems_key = 0;
    private int ems_name = 1;

    // Translation file from AMS
    private String trans_file = "Chinese.csv";
    private String alarm_file = "NE alarms.xls";
    private Lanaguage trans_lang = Lanaguage.CN;

    /**
     * @return the customCode
     */
    public String getCustomCode() {
        return customCode;
    }

    /**
     * @param customCode
     *            the customCode to set
     */
    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    /**
     * @return the customName
     */
    public String getCustomName() {
        return customName;
    }

    /**
     * @param customName
     *            the customName to set
     */
    public void setCustomName(String customName) {
        this.customName = customName;
    }

    /**
     * @return the customDiscription
     */
    public String getCustomDescription() {
        return customDescription;
    }

    /**
     * @param customDiscription
     *            the customDiscription to set
     */
    public void setCustomDescription(String customDescription) {
        this.customDescription = customDescription;
    }

    /**
     * @return the serverIp
     */
    public String getServerIp() {
        return serverIp;
    }

    /**
     * @param serverIp
     *            the serverIp to set
     */
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the passWord
     */
    public String getPassWord() {
        return passWord;
    }

    /**
     * @param passWord
     *            the passWord to set
     */
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    /**
     * @return the serverPort
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * @param serverPort
     *            the serverPort to set
     */
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * @return the alarmId
     */
    public int getAlarmId() {
        return alarmId;
    }

    /**
     * @param alarmId
     *            the alarmId to set
     */
    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    /**
     * @return the alarmCause
     */
    public int getAlarmCause() {
        return alarmCause;
    }

    /**
     * @param alarmCause
     *            the alarmCause to set
     */
    public void setAlarmCause(int alarmCause) {
        this.alarmCause = alarmCause;
    }

    /**
     * @return the alarmCode
     */
    public int getAlarmCode() {
        return alarmCode;
    }

    /**
     * @param alarmCode
     *            the alarmCode to set
     */
    public void setAlarmCode(int alarmCode) {
        this.alarmCode = alarmCode;
    }

    /**
     * @return the alarmName
     */
    public int getAlarmName() {
        return alarmName;
    }

    /**
     * @param alarmName
     *            the alarmName to set
     */
    public void setAlarmName(int alarmName) {
        this.alarmName = alarmName;
    }

    /**
     * @return the alarmDescription
     */
    public int getAlarmDescription() {
        return alarmDescription;
    }

    /**
     * @param alarmDescription
     *            the alarmDescription to set
     */
    public void setAlarmDescription(int alarmDescription) {
        this.alarmDescription = alarmDescription;
    }

    /**
     * @return the dbName
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * @param dbName
     *            the dbName to set
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName
     *            the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the ctc_tab
     */
    public List<String> getCtc_tab() {
        return ctc_tab;
    }

    /**
     * @param ctc_tab
     *            the ctc_tab to set
     */
    public void setCtc_tab(List<String> ctc_tab) {
        this.ctc_tab = ctc_tab;
    }

    /**
     * @return the transparent_name
     */
    public int getTransparent_name() {
        return transparent_name;
    }

    /**
     * @param transparent_name
     *            the transparent_name to set
     */
    public void setTransparent_name(int transparent_name) {
        this.transparent_name = transparent_name;
    }

    /**
     * @return the transparent_almtype
     */
    public int getTransparent_almtype() {
        return transparent_almtype;
    }

    /**
     * @param transparent_almtype
     *            the transparent_almtype to set
     */
    public void setTransparent_almtype(int transparent_almtype) {
        this.transparent_almtype = transparent_almtype;
    }

    /**
     * @return the transparent_almnum
     */
    public int getTransparent_almnum() {
        return transparent_almnum;
    }

    /**
     * @param transparent_almnum
     *            the transparent_almnum to set
     */
    public void setTransparent_almnum(int transparent_almnum) {
        this.transparent_almnum = transparent_almnum;
    }

    /**
     * @return the ems_tab
     */
    public List<String> getEms_tab() {
        return ems_tab;
    }

    /**
     * @param ems_tab
     *            the ems_tab to set
     */
    public void setEms_tab(List<String> ems_tab) {
        this.ems_tab = ems_tab;
    }

    /**
     * @return the ne_tab
     */
    public List<String> getNe_tab() {
        return ne_tab;
    }

    /**
     * @param ne_tab
     *            the ne_tab to set
     */
    public void setNe_tab(List<String> ne_tab) {
        this.ne_tab = ne_tab;
    }

    /**
     * @return the ems_key
     */
    public int getEms_key() {
        return ems_key;
    }

    /**
     * @param ems_key
     *            the ems_key to set
     */
    public void setEms_key(int ems_key) {
        this.ems_key = ems_key;
    }

    /**
     * @return the ems_name
     */
    public int getEms_name() {
        return ems_name;
    }

    /**
     * @param ems_name
     *            the ems_name to set
     */
    public void setEms_name(int ems_name) {
        this.ems_name = ems_name;
    }

    /**
     * @return the alarmSource
     */
    public int getAlarmSource() {
        return alarmSource;
    }

    /**
     * @param alarmSource
     *            the alarmSource to set
     */
    public void setAlarmSource(int alarmSource) {
        this.alarmSource = alarmSource;
    }

    /**
     * @return the importMode
     */
    public ImportMode getImportMode() {
        return importMode;
    }

    /**
     * @param importMode
     *            the importMode to set
     */
    public void setImportMode(ImportMode importMode) {
        this.importMode = importMode;
    }

    /**
     * @return the trans_file
     */
    public String getTrans_file() {
        return trans_file;
    }

    /**
     * @param trans_file
     *            the trans_file to set
     */
    public void setTrans_file(String trans_file) {
        this.trans_file = trans_file;
    }

    /**
     * @return the alarm_file
     */
    public String getAlarm_file() {
        return alarm_file;
    }

    /**
     * @param alarm_file
     *            the alarm_file to set
     */
    public void setAlarm_file(String alarm_file) {
        this.alarm_file = alarm_file;
    }

    /**
     * @return the trans_lang
     */
    public Lanaguage getTrans_lang() {
        return trans_lang;
    }

    /**
     * @param trans_lang
     *            the trans_lang to set
     */
    public void setTrans_lang(Lanaguage trans_lang) {
        this.trans_lang = trans_lang;
    }

}
