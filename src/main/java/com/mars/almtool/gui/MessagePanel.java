/*
 * $Id: MessagePanel.java, 2011-12-8 ����10:02:59 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.gui.component.TableLayout;
import com.mars.almtool.task.impl.LogFactory;
import com.mars.almtool.utils.ClientUtils;

/**
 * <p>
 * Title: MessagePanel
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-8 ����10:02:59
 * @modified [who date description]
 * @check [who date description]
 */
public class MessagePanel extends JPanel {
    private static final long serialVersionUID = -7950270788996118095L;
    private final Logger logger = ClientContext.getLogger(LogFactory.LOG_ERROR);

    private JLabel headLbl;
    private JLabel errorLbl;
    private JTextArea msgLbl;
    private JLabel logoLbl;
    private String msg = "";

    public MessagePanel() {
        initGui();
    }

    private void initGui() {
        double[][] ds = { { 5, 18, TableLayout.FILL, 75 }, { 2, 26, 40, 2 } };
        setLayout(ClientUtils.getTableLayout(ds));
        headLbl = new JLabel();
        headLbl.setFont(new Font(headLbl.getFont().getName(), Font.BOLD, headLbl.getFont().getSize() + 5));
        headLbl.setBackground(Color.white);
        headLbl.setOpaque(false);
        errorLbl = ClientUtils.getIconLabel("nav_error.png");
        errorLbl.setVisible(false);
        msgLbl = new JTextArea();
        msgLbl.setBorder(null);
        msgLbl.setEditable(false);
        msgLbl.setBackground(Color.white);
        msgLbl.setOpaque(false);
        msgLbl.setRows(2);
        msgLbl.setFocusable(false);
        msgLbl.setLineWrap(true);
        logoLbl = ClientUtils.getIconLabel("nav_default.png");
        setOpaque(true);
        setBackground(Color.white);
        add(headLbl, "1,1,2,f");
        add(errorLbl, "1,2,t,t");
        add(msgLbl, "2,2,f,f");
        add(logoLbl, "3,1,3,2");
        add(new JSeparator(), "0,3,3,f");
        setSize(getSize().width, 100);
    }

    public void showError(String error) {
        if (!StringUtils.isEmpty(error)) {
            errorLbl.setVisible(true);
            msgLbl.setText(error);
            logger.info(error);
        } else {
            errorLbl.setVisible(false);
            msgLbl.setText(msg);
        }
    }

    public void showWarning(String error) {
        if (!StringUtils.isEmpty(error)) {
            errorLbl.setVisible(true);
            msgLbl.setText(error);
        } else {
            errorLbl.setVisible(false);
            msgLbl.setText(msg);
        }
    }

    public void showHeader(String header) {
        headLbl.setText(header);
    }

    public void showMessage(String msg) {
        this.msg = msg;
        errorLbl.setVisible(false);
        msgLbl.setText(msg);
    }

}
