/*
 * $Id: ClientUtils.java, 2011-10-27 ����05:02:15 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.utils;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Window;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.gui.WaitingDialog;
import com.mars.almtool.gui.component.TableLayout;

/**
 * <p>
 * Title: ClientUtils
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-10-27 ����05:02:15
 * @modified [who date description]
 * @check [who date description]
 */
public class ClientUtils {

    public synchronized static void centerWindow(Window window) {
        Dimension screen = window.getToolkit().getScreenSize();
        window.setLocation((int) (screen.getWidth() - window.getWidth()) / 2,
                (int) (screen.getHeight() - window.getHeight()) / 2);
    }

    public synchronized static void centerRelativeWindow(Window child, Window parent) {
        Dimension size = parent.getSize();
        Dimension csize = child.getSize();
        Point local = parent.getLocation();
        child.setLocation(new Point((local.x + size.width / 2 - csize.width / 2),
                (local.y + size.height / 2 - csize.height / 2)));
    }

    public synchronized static void centerRelativeWindow(Window child) {
        centerRelativeWindow(child, ClientContext.getMainFrame());
    }

    public static void showInfoDailog(String message) {
        JOptionPane.showMessageDialog(ClientContext.getMainFrame(), message, "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showInfoDailog(String title, String message) {
        JOptionPane.showMessageDialog(ClientContext.getMainFrame(), message, title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorDailog(String message) {
        JOptionPane.showMessageDialog(ClientContext.getMainFrame(), message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static WaitingDialog getWaitingDialog() {
        WaitingDialog dialog = new WaitingDialog();
        dialog.initGui();
        return dialog;
    }

    public static LayoutManager getTableLayout(double[][] ds) {
        return new TableLayout(ds);
    }

    public static JLabel getIconLabel(String iconName) {
        Icon icon = ClientContext.getIcon(iconName);
        JLabel label = new JLabel();
        if (icon != null)
            label.setIcon(icon);
        return label;
    }
    
    public static Dimension getScreenSize(){
        return java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    }

}
