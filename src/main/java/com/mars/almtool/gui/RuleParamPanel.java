/*
 * $Id: DeviceParamPanel.java, 2013-4-17 ����10:41:08 Yao Exp $
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.me.AlarmRule;
import com.mars.almtool.me.enums.AlarmSeverity;
import com.mars.almtool.utils.ClientUtils;

/**
 * <p>
 * Title: DeviceParamPanel
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2013-4-17 ����10:41:08
 * @modified [who date description]
 * @check [who date description]
 */
public class RuleParamPanel extends ContentPanel {
    private static final long serialVersionUID = -8039496503642991831L;

    private final String str_create = ClientContext.getI18nString("str_create");
    private final String str_delete = ClientContext.getI18nString("str_delete");
    private final String alarm_type = ClientContext.getI18nString("alarm_type");
    private final String alarm_num = ClientContext.getI18nString("alarm_num");
    private final String alarm_severity = ClientContext.getI18nString("alarm_severity");

    private JTable ruleTable;
    private JButton btn_create;
    private JButton btn_delete;

    private Vector<String> headerData = new Vector<String>();
    private Vector<Object> srcData = new Vector<Object>();

    /**
     * 
     */
    public RuleParamPanel() {
        initGui();
        initAction();
        initData();
    }

    /**
     * 
     */
    public void initData() {
        srcData.removeAllElements();
        for (AlarmRule temp : ClientContext.getRuleList()) {
            Vector<String> rowData = new Vector<String>();
            rowData.add((temp.getAlarmId())[0] + "");
            rowData.add((temp.getAlarmId())[1] + "");
            rowData.add(temp.getSeverity().getLocalName());
            srcData.add(rowData);
        }
        ruleTable.repaint();
        ruleTable.updateUI();
    }

    /**
     * 
     */
    private void initAction() {
        btn_delete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                deleteColumn();
            }
        });

        btn_create.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createColumn();
            }
        });
    }

    /**
     * 
     */
    private void initGui() {
        String[] columns = { alarm_type, alarm_num, alarm_severity };

        for (String hearder : columns)
            headerData.add(hearder);
        ruleTable = new JTable(srcData, headerData);
        ruleTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        btn_create = new JButton();
        btn_delete = new JButton();

        btn_create.setText(str_create);
        btn_delete.setText(str_delete);

        JScrollPane tablePanel = new JScrollPane();
        tablePanel.getViewport().add(ruleTable);

        JPanel btn_panel = new JPanel();
        btn_panel.setLayout(ClientUtils
                .getTableLayout(new double[][] { { 5, 75, 5 }, { 10, 20, 10, 20, 10 } }));
        btn_panel.add(btn_create, "1,1,f,f");
        btn_panel.add(btn_delete, "1,3,f,f");

        this.setLayout(new BorderLayout());
        this.add(tablePanel, BorderLayout.CENTER);
        this.add(btn_panel, BorderLayout.EAST);
    }

    /**
     * 
     */
    protected void createColumn() {
        CreateRuleDialog dialog = new CreateRuleDialog(this);
        dialog.setVisible(true);
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    protected void deleteColumn() {
        int[] columns = ruleTable.getSelectedRows();
        if (ArrayUtils.isEmpty(columns))
            return;
        boolean flag = false;
        for (int column : columns) {
            Object obj = srcData.get(column);
            if (obj != null && obj instanceof Vector) {
                Vector<String> vt = (Vector<String>) obj;
                int alarmtype = Integer.parseInt(vt.get(0));
                int alarmnum = Integer.parseInt(vt.get(1));
                AlarmSeverity severity = AlarmSeverity.getAlarmSeverity(vt.get(2));
                AlarmRule rule = new AlarmRule();
                rule.setAlarmIndex(alarmtype * 65536 + alarmnum);
                rule.setSeverity(severity);
                ClientContext.removeAlarmRule(rule);
                flag = true;
            }
        }

        if (flag) {
            initData();
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#runTask()
     */
    @Override
    public boolean runTask() {
        return CollectionUtils.isNotEmpty(ClientContext.getRuleList());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#getMessage()
     */
    @Override
    public String getMessage() {
        return ClientContext.getI18nString("msg_rulepanel");
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#getHeader()
     */
    @Override
    public String getHeader() {
        return ClientContext.getI18nString("title_rulepanel");
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#getName()
     */
    @Override
    public String getName() {
        return "RuleParams";
    }

}
