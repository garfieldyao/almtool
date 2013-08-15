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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.commons.lang.ArrayUtils;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.me.NetworkElement;
import com.mars.almtool.task.AlarmSeverityAssign;
import com.mars.almtool.task.batch.BatchManager;
import com.mars.almtool.task.batch.BatchResult;
import com.mars.almtool.task.impl.AlarmSeverityAssignImpl;
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
public class DeviceParamPanel extends ContentPanel {
    private static final long serialVersionUID = -8039496503642991831L;

    private final String str_create = ClientContext.getI18nString("str_create");
    private final String str_delete = ClientContext.getI18nString("str_delete");
    private final String dev_ip = ClientContext.getI18nString("dev_ip");
    private final String dev_community = ClientContext.getI18nString("dev_community");
    private final String dev_result = ClientContext.getI18nString("dev_result");
    private final String dev_succeed = ClientContext.getI18nString("dev_succeed");
    private final String dev_failed = ClientContext.getI18nString("dev_failed");

    private JTable deviceTable;
    private JButton btn_create;
    private JButton btn_delete;

    private Vector<String> headerData = new Vector<String>();
    private Vector<Object> srcData = new Vector<Object>();
    private List<BatchResult> results = new ArrayList<BatchResult>();

    /**
     * 
     */
    public DeviceParamPanel() {
        initGui();
        initAction();
        initData();
    }

    /**
     * 
     */
    public void initData() {
        srcData.removeAllElements();
        for (NetworkElement temp : ClientContext.getNeList()) {
            Vector<String> rowData = new Vector<String>();
            rowData.add(temp.getIpAddress());
            rowData.add(temp.getCommunity());
            rowData.add("");
            srcData.add(rowData);
        }
        deviceTable.repaint();
        deviceTable.updateUI();
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
        String[] columns = { dev_ip, dev_community, dev_result };

        for (String hearder : columns)
            headerData.add(hearder);
        deviceTable = new JTable(srcData, headerData);
        deviceTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        btn_create = new JButton();
        btn_delete = new JButton();

        btn_create.setText(str_create);
        btn_delete.setText(str_delete);

        JScrollPane tablePanel = new JScrollPane();
        tablePanel.getViewport().add(deviceTable);

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
        CreateDeviceDialog dialog = new CreateDeviceDialog(this);
        dialog.setVisible(true);
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    protected void deleteColumn() {
        int[] columns = deviceTable.getSelectedRows();
        if (ArrayUtils.isEmpty(columns))
            return;
        boolean flag = false;
        for (int column : columns) {
            Object obj = srcData.get(column);
            if (obj != null && obj instanceof Vector) {
                Vector<String> vt = (Vector<String>) obj;
                String ip = vt.get(0) + "";
                ClientContext.removeDevice(ip);
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
        results = new BatchManager(ClientContext.getNeList()) {
            @Override
            protected BatchResult runTask(NetworkElement ne) {
                AlarmSeverityAssign assign = new AlarmSeverityAssignImpl(ne);
                BatchResult result = assign.doAssign();
                return result;
            }
        }.execute();
        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#afterRunTask()
     */
    @Override
    public void afterRunTask() {
        updateResult();
    }

    @SuppressWarnings("unchecked")
    private void updateResult() {
        for (Object column : srcData) {
            if (column instanceof Vector) {
                Vector<String> vt = (Vector<String>) column;
                String ip = vt.get(0) + "";
                for (BatchResult result : results) {
                    if (result.getIpAddr().equals(ip)) {
                        vt.set(2, result.isSucceed() ? dev_succeed : dev_failed);
                        break;
                    }
                }
            }
        }

        deviceTable.repaint();
        deviceTable.updateUI();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#getMessage()
     */
    @Override
    public String getMessage() {
        return ClientContext.getI18nString("msg_devicepanel");
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#getHeader()
     */
    @Override
    public String getHeader() {
        return ClientContext.getI18nString("title_devicepanel");
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#getName()
     */
    @Override
    public String getName() {
        return "DeviceParams";
    }

}
