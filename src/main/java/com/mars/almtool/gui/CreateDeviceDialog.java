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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.gui.component.IpField;
import com.mars.almtool.gui.component.NotNullField;
import com.mars.almtool.me.NetworkElement;
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
public class CreateDeviceDialog extends JDialog {
    private static final long serialVersionUID = -6042009373948358398L;

    private final String title_devicepanel = ClientContext.getI18nString("title_devicepanel");
    private final String str_confrim = ClientContext.getI18nString("str_confrim");
    private final String str_cancel = ClientContext.getI18nString("str_cancel");
    private final String dev_ip = ClientContext.getI18nString("dev_ip");
    private final String dev_community = ClientContext.getI18nString("dev_community");

    private Image logoicon = ClientContext.getLogoIcon();

    private JButton btn_confirm;
    private JButton btn_cancel;

    private JLabel lb_ip;
    private JTextField fd_ip;
    private JLabel lb_community;
    private JTextField fd_community;

    private JPanel parent = null;

    public CreateDeviceDialog(JPanel parent) {
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
        NetworkElement temp = new NetworkElement();
        temp.setIpAddress(fd_ip.getText());
        temp.setCommunity(fd_community.getText());

        ClientContext.addDevice(temp);
        ((DeviceParamPanel) parent).initData();
        dispose();
    }

    /**
     * 
     */
    private void initData() {
        if (ClientContext.getNeList().size() > 0) {
            fd_community.setText(ClientContext.getNeList().get(0).getCommunity());
        }
    }

    private void initGui() {
        btn_confirm = new JButton(str_confrim);
        btn_cancel = new JButton(str_cancel);

        fd_ip = new IpField(dev_ip, ClientContext.getMainFrame().getMsgPanel(), btn_confirm);
        lb_ip = new JLabel(dev_ip);

        fd_community = new NotNullField(dev_community, ClientContext.getMainFrame().getMsgPanel(),
                btn_confirm);
        lb_community = new JLabel(dev_community);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(ClientUtils.getTableLayout(new double[][] { { 5, 200, TableLayout.FILL, 5 },
                { 5, 20, 5, 20, 5, 20, 5, 20, 5, 20, 5, 20, 5, 20, 5, 20, 5 } }));

        contentPanel.add(lb_ip, "1,1,f,f");
        contentPanel.add(fd_ip, "2,1,f,f");

        contentPanel.add(lb_community, "1,3,f,f");
        contentPanel.add(fd_community, "2,3,f,f");

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btn_confirm);
        btnPanel.add(btn_cancel);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(contentPanel, BorderLayout.CENTER);
        this.getContentPane().add(btnPanel, BorderLayout.SOUTH);
        this.setSize(new Dimension(500, 300));
        this.setTitle(title_devicepanel);
        this.setIconImage(logoicon);
        this.setModal(true);
        this.setResizable(false);
        ClientUtils.centerRelativeWindow(this, ClientContext.getMainFrame());
    }
}
