/*
 * $Id: ClientContext.java, 2011-12-8 ����1:11:33 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.comm;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;

import com.mars.almtool.gui.MainView;
import com.mars.almtool.me.AlarmRule;
import com.mars.almtool.me.GlobalConfig;
import com.mars.almtool.me.NetworkElement;
import com.mars.almtool.me.enums.Lanaguage;
import com.mars.almtool.task.impl.LogFactory;
import com.mars.almtool.task.impl.ResourceFactory;

/**
 * <p>
 * Title: ClientContext
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-8 ����1:11:33
 * @modified [who date description]
 * @check [who date description]
 */
public class ClientContext {
    private static LogFactory logFactory;
    private static ResourceFactory resFactory;
    private static GlobalConfig config;
    private static Map<String, Icon> iconList;
    private static MainView mainFrame;
    private static Lanaguage language = Lanaguage.CN;
    private static int MaxRepetitions = 40;
    public static int MAX_Threads = 10;

    private static List<NetworkElement> neList = null;
    private static List<AlarmRule> ruleList = null;

    public static LogFactory getLogFactory() {
        if (logFactory == null)
            logFactory = new LogFactory();
        return logFactory;
    }

    public static Logger getLogger(String log) {
        return getLogFactory().getLogger(log);
    }

    public static ResourceFactory getResourceFactory() {
        if (resFactory == null)
            resFactory = new ResourceFactory();
        return resFactory;
    }

    public static GlobalConfig getConfig() {
        if (config == null)
            config = new GlobalConfig();
        return config;
    }

    public static String getI18nString(String key) {
        return getResourceFactory().getI18NString(key);
    }

    public static String[] getI18nString(String... keys) {
        if (ArrayUtils.isEmpty(keys))
            return new String[0];
        List<String> values = new ArrayList<String>();
        for (String key : keys) {
            values.add(getI18nString(key));
        }
        return values.toArray(new String[0]);
    }

    public static Map<String, Icon> getIconList() {
        if (iconList == null)
            iconList = new HashMap<String, Icon>();
        return iconList;
    }

    public static Icon getIcon(String iconName) {
        if (getIconList().containsKey(iconName))
            return iconList.get(iconName);
        Icon icon = new ImageIcon(getResourceFactory().getImageSource(iconName));
        if (icon != null)
            iconList.put(iconName, icon);
        return icon;
    }

    public static Image getImage(String name) {
        Icon icon = getIcon(name);
        if (icon == null)
            return null;
        return ((ImageIcon) icon).getImage();
    }

    public static Image getLogoIcon() {
        String name = "alu.png";
        Icon icon = getIcon(name);
        if (icon == null)
            return null;
        return ((ImageIcon) icon).getImage();
    }

    /**
     * @param mainFrame
     *            the mainFrame to set
     */
    public static void setMainFrame(MainView mainFrame) {
        ClientContext.mainFrame = mainFrame;
    }

    /**
     * @return the mainFrame
     */
    public static MainView getMainFrame() {
        return mainFrame;
    }

    /**
     * @return the maxRepetitions
     */
    public static int getMaxRepetitions() {
        return MaxRepetitions;
    }

    /**
     * @param maxRepetitions
     *            the maxRepetitions to set
     */
    public static void setMaxRepetitions(int maxRepetitions) {
        MaxRepetitions = maxRepetitions;
    }

    /**
     * @return the neList
     */
    public static List<NetworkElement> getNeList() {
        if (neList == null)
            neList = new ArrayList<NetworkElement>();
        return neList;
    }

    /**
     * @return the ruleList
     */
    public static List<AlarmRule> getRuleList() {
        if (ruleList == null)
            ruleList = new ArrayList<AlarmRule>();
        return ruleList;
    }

    public static void removeAlarmRule(AlarmRule rule) {
        Iterator<AlarmRule> iterator = getRuleList().iterator();
        while (iterator.hasNext()) {
            AlarmRule next = iterator.next();
            if (next.equals(rule))
                iterator.remove();
        }
    }

    public static void removeDevice(String devIp) {
        Iterator<NetworkElement> iterator = getNeList().iterator();
        while (iterator.hasNext()) {
            NetworkElement next = iterator.next();
            if (next.getIpAddress().equals(devIp))
                iterator.remove();
        }
    }

    public static void addDevice(NetworkElement newne) {
        boolean flag = true;
        for (NetworkElement ne : getNeList()) {
            if (ne.getIpAddress().equals(newne.getIpAddress())) {
                ne.setCommunity(newne.getCommunity());
                flag = false;
            }
        }

        if (flag) {
            NetworkElement tmpne = new NetworkElement();
            tmpne.setIpAddress(newne.getIpAddress());
            tmpne.setCommunity(newne.getCommunity());
            getNeList().add(tmpne);
        }
    }

    /**
     * @return the language
     */
    public static Lanaguage getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public static void setLanguage(Lanaguage language) {
        ClientContext.language = language;
    }
}
