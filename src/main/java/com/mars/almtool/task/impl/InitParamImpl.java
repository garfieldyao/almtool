/*
 * $Id: InitParamImpl.java, 2011-12-8 ����7:14:22 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.task.impl;

import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.me.GlobalConfig;
import com.mars.almtool.me.enums.Lanaguage;
import com.mars.almtool.task.InitParam;

/**
 * <p>
 * Title: InitParamImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-8 ����7:14:22
 * @modified [who date description]
 * @check [who date description]
 */
public class InitParamImpl implements InitParam {
    private final String config_file = "config.ini";

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.task.InitParam#initParam()
     */
    @Override
    public void initParam() {
        URL url = ClientContext.getResourceFactory().getConfigFile(config_file);
        Properties prop = new Properties();
        try {
            prop.load(url.openStream());
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        String dbserverip = prop.getProperty("dbserverip");
        String dbuser = prop.getProperty("dbuser");
        String dbpassword = prop.getProperty("dbpassword");
        String dbport = prop.getProperty("dbport");

        String alrmid = prop.getProperty("alrmid");
        String alarmcause = prop.getProperty("alarmcause");
        String alarmcode = prop.getProperty("alarmcode");
        String alarmname = prop.getProperty("alarmname");
        String alarmdescription = prop.getProperty("alarmdescription");
        String alarmsource = prop.getProperty("alarmsource");

        String namedb = prop.getProperty("namedb");
        String nametable = prop.getProperty("nametable");

        String customcode = prop.getProperty("customcode");
        String customname = prop.getProperty("customname");
        String customdiscription = prop.getProperty("customdiscription");

        String language = NotNull(prop.getProperty("language"), "cn");
        String translang = NotNull(prop.getProperty("translang"), "cn");

        String ctc_tab = prop.getProperty("ctc_tab");
        String ems_tab = prop.getProperty("ems_tab");
        String ne_tab = prop.getProperty("ne_tab");
        String transparent_name = prop.getProperty("transparent_name");
        String transparent_almtype = prop.getProperty("transparent_almtype");
        String transparent_almnum = prop.getProperty("transparent_almnum");

        String ems_key = prop.getProperty("ems_key");
        String ems_name = prop.getProperty("ems_name");

        String trans_file = prop.getProperty("trans_file");
        String alarm_file = prop.getProperty("alarm_file");

        GlobalConfig config = ClientContext.getConfig();

        config.setServerIp(dbserverip);
        config.setUserName(dbuser);
        config.setPassWord(dbpassword);
        config.setServerPort(Integer.parseInt(dbport));

        config.setAlarmId(Integer.parseInt(alrmid));
        config.setAlarmCode(Integer.parseInt(alarmcode));
        config.setAlarmCause(Integer.parseInt(alarmcause));
        config.setAlarmDescription(Integer.parseInt(alarmdescription));
        config.setAlarmName(Integer.parseInt(alarmname));
        config.setAlarmSource(Integer.parseInt(alarmsource));

        config.setCustomCode(customcode);
        config.setCustomName(customname);
        config.setCustomDescription(customdiscription);

        config.setDbName(namedb);
        config.setTableName(nametable);

        config.getCtc_tab().addAll(Arrays.asList(ctc_tab.split(",")));
        config.getEms_tab().addAll(Arrays.asList(ems_tab.split(",")));
        config.getNe_tab().addAll(Arrays.asList(ne_tab.split(",")));

        config.setTransparent_name(Integer.parseInt(transparent_name));
        config.setTransparent_almtype(Integer.parseInt(transparent_almtype));
        config.setTransparent_almnum(Integer.parseInt(transparent_almnum));
        config.setEms_key(Integer.parseInt(ems_key));
        config.setEms_name(Integer.parseInt(ems_name));

        config.setTrans_file(trans_file);
        config.setAlarm_file(alarm_file);
        config.setTrans_lang("cn".equals(translang.toLowerCase()) ? Lanaguage.CN : Lanaguage.EN);

        ClientContext.setLanguage("cn".equals(language.toLowerCase()) ? Lanaguage.CN : Lanaguage.EN);
    }

    private String NotNull(String str, String defaultstr) {
        return str == null ? defaultstr : str;
    }
}
