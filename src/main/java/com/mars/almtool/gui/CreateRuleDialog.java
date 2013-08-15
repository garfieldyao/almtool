/*
 * $Id: CreatePowerTemplateDialog.java, 2011-10-30 ����2:30:07 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.gui.component.NumberField;
import com.mars.almtool.me.AlarmRule;
import com.mars.almtool.me.enums.AlarmSeverity;
import com.mars.almtool.utils.ClientUtils;

/**
 * <p>
 * Title: CreatePowerTemplateDialog
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-10-30 ����2:30:07
 * @modified [who date description]
 * @check [who date description]
 */
public class CreateRuleDialog extends JDialog {
    private static final long serialVersionUID = -6042009373948358398L;

    private final String title_rulepanel = ClientContext.getI18nString("title_rulepanel");
    private final String str_confrim = ClientContext.getI18nString("str_confrim");
    private final String str_cancel = ClientContext.getI18nString("str_cancel");
    private final String alarm_type = ClientContext.getI18nString("alarm_type");
    private final String alarm_num = ClientContext.getI18nString("alarm_num");
    private final String alarm_severity = ClientContext.getI18nString("alarm_severity");

    private Image logoicon = ClientContext.getLogoIcon();

    private JButton btn_confirm;
    private JButton btn_cancel;

    private JLabel lb_type;
    private JTextField fd_type;
    private JLabel lb_num;
    private JTextField fd_num;
    private JLabel lb_severity;
    @SuppressWarnings("rawtypes")
	private JComboBox cb_severity;

    private JPanel parent = null;

    public CreateRuleDialog(JPanel parent) {
        this.parent = parent;
        initGui();
        initData();
        initAction();
    }

    /**
     * 
     */
    private void initAction() {
        btn_cancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btn_confirm.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                collectData();
            }
        });
    }

    /**
     * 
     */
    protected void collectData() {
        AlarmRule temp = new AlarmRule();
        int alarmtype = Integer.parseInt(fd_type.getText());
        int alarmnum = Integer.parseInt(fd_num.getText());
        temp.setAlarmIndex(alarmtype == 0 ? 0 : (alarmtype * 65536 + alarmnum));
        temp.setSeverity(AlarmSeverity.getAlarmSeverity(cb_severity.getSelectedItem().toString()));

        ClientContext.getRuleList().add(temp);
        ((RuleParamPanel) parent).initData();
        dispose();
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
	private void initData() {
        for (AlarmSeverity severity : AlarmSeverity.values()) {
            cb_severity.addItem(severity.getLocalName());
        }
    }

    @SuppressWarnings("rawtypes")
	private void initGui() {
        btn_confirm = new JButton(str_confrim);
        btn_cancel = new JButton(str_cancel);

        fd_type = new NumberField(alarm_type, ClientContext.getMainFrame().getMsgPanel(), btn_confirm, 0,
                65535);
        lb_type = new JLabel(alarm_type);

        fd_num = new NumberField(alarm_num, ClientContext.getMainFrame().getMsgPanel(), btn_confirm, 1, 31);
        lb_num = new JLabel(alarm_num);

        cb_severity = new JComboBox();
        lb_severity = new JLabel(alarm_severity);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(ClientUtils.getTableLayout(new double[][] { { 5, 200, TableLayout.FILL, 5 },
                { 5, 20, 5, 20, 5, 20, 5, 20, 5, 20, 5, 20, 5, 20, 5, 20, 5 } }));

        contentPanel.add(lb_type, "1,1,f,f");
        contentPanel.add(fd_type, "2,1,f,f");

        contentPanel.add(lb_num, "1,3,f,f");
        contentPanel.add(fd_num, "2,3,f,f");

        contentPanel.add(lb_severity, "1,5,f,f");
        contentPanel.add(cb_severity, "2,5,f,f");

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btn_confirm);
        btnPanel.add(btn_cancel);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(contentPanel, BorderLayout.CENTER);
        this.getContentPane().add(btnPanel, BorderLayout.SOUTH);
        this.setSize(new Dimension(500, 300));
        this.setTitle(title_rulepanel);
        this.setIconImage(logoicon);
        this.setModal(true);
        this.setResizable(false);
        ClientUtils.centerRelativeWindow(this, ClientContext.getMainFrame());
    }
}
