/*
 * $Id: GeneralUtils.java, 2011-12-9 ����12:54:00 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Title: GeneralUtils
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-9 ����12:54:00
 * @modified [who date description]
 * @check [who date description]
 */
public class GeneralUtils {
    public static boolean isValidAddress(String ip) {
        if (StringUtils.isEmpty(ip) || StringUtils.isBlank(ip))
            return false;

        String regx = "[0-9]+[.][0-9]+[.][0-9]+[.][0-9]+";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(ip);
        boolean matches = matcher.matches();
        return matches;
    }

    public static boolean validNumberString(String str) {
        if (StringUtils.isEmpty(str) || StringUtils.isBlank(str))
            return false;
        String regx = "[-]?[0-9]+";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(str);
        boolean matches = matcher.matches();
        return matches;
    }

    public static boolean validAlarmId(String str) {
        if (StringUtils.isEmpty(str) || StringUtils.isBlank(str))
            return false;
        String regx = "[0-9]+[/][0-9]+";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(str);
        boolean matches = matcher.matches();
        return matches;
    }

    public static boolean validAlarmCause(String str) {
        if (StringUtils.isEmpty(str) || StringUtils.isBlank(str))
            return false;
        String regx = "[0-9a-zA-Z]+[/][0-9a-zA-Z]+[/][0-9a-zA-Z]*";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(str);
        boolean matches = matcher.matches();
        return matches;
    }

    public static boolean isMdu(String code) {
        boolean result = false;
        if (StringUtils.isNotEmpty(code)) {
            result = code.contains("7353") || code.contains("7330");
        }
        return result;
    }

    /**
     * -1 means no valid vlaue
     * 
     * @param src
     * @return
     */
    public static int filterAlarmType(String src) {
        int type = 0;
        String regx = "[(][ ]*[0-9]+[ ]*[)]";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(src);
        if (matcher.find()) {
            String group = matcher.group();
            String value = group.substring(1, group.length() - 1).trim();
            type = Integer.parseInt(value);
        }
        return type;
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 24; i++) {
            System.out.println(i + "   " + getVS(i) + "/" + getVP(i));
        }
    }

    private static int getVP(int onuport) {
        return ((onuport - 1) & 7) + 1;
    }

    private static int getVS(int onuport) {
        return ((onuport - 1) >> 3) + 10;
    }
}
