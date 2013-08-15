/*
 * $Id: ResouceFactory.java, 2011-12-8 ����1:18:19 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.task.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.me.enums.Lanaguage;

/**
 * <p>
 * Title: ResouceFactory
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-8 ����1:18:19
 * @modified [who date description]
 * @check [who date description]
 */
public class ResourceFactory {

    public final String rootPath = "META-INF/";

    public final String i18nRoot = "i18n/";
    public final String imagesRoot = "images/";
    public final String cn_file = "zh_cn.properties";
    public final String en_file = "en_us.properties";

    public final String configRoot = "config/";

    private Properties prop_i18n;

    public ResourceFactory() {

    }

    public URL getImageSource(String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(rootPath);
        sb.append(imagesRoot);
        sb.append(fileName);
        return getUrl(sb.toString());
    }

    public URL getConfigFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(configRoot);
        sb.append(fileName);
        URL url = getUrl(sb.toString());
        if (url == null) {
            url = getUrl(fileName);
        }
        return url;
    }

    public String getConfigFilePath(String fileName) {
        URL url = getConfigFile(fileName);
        String path = "";
        if (url != null) {
            try {
                path = URLDecoder.decode(url.getPath(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    public String getI18NString(String key) {
        if (prop_i18n == null) {
            prop_i18n = new Properties();
            StringBuilder sb = new StringBuilder();
            sb.append(rootPath);
            sb.append(i18nRoot);
            if (ClientContext.getLanguage() == Lanaguage.CN)
                sb.append(cn_file);
            else
                sb.append(en_file);
            URL url = getUrl(sb.toString());
            try {
                prop_i18n.load(url.openStream());
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        return prop_i18n.getProperty(key, key);
    }

    private URL getUrl(String name) {
        URL url = null;
        try {
            File file = new File(name);
            if (file.exists()) {
                url = file.toURI().toURL();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (url == null) {
            url = ClassLoader.getSystemResource(name);
        }
        if (url == null) {
            ClassLoader loader = getClass().getClassLoader();
            url = loader.getResource(name);
            while (url == null && loader.getParent() != null) {
                loader = loader.getParent();
                url = loader.getResource(name);
            }
        }
        if (url == null) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            url = loader.getResource(name);
            while (url == null && loader.getParent() != null) {
                loader = loader.getParent();
                url = loader.getResource(name);
            }
        }
        return url;
    }

}
