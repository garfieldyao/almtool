/*
 * $Id: SqlParamPanel.java, 2011-12-8 ����11:36:40 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.gui;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.gui.component.IpField;
import com.mars.almtool.gui.component.NotNullField;
import com.mars.almtool.gui.component.NumberField;
import com.mars.almtool.gui.component.TableLayout;
import com.mars.almtool.me.GlobalConfig;
import com.mars.almtool.task.DbOperator;
import com.mars.almtool.task.impl.DbOperatorImpl;
import com.mars.almtool.utils.ClientUtils;

/**
 * <p>
 * Title: SqlParamPanel
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-8 ����11:36:40
 * @modified [who date description]
 * @check [who date description]
 */
public class SqlParamPanel extends ContentPanel {
    private static final long serialVersionUID = 102566443008409510L;
    private final String db_ip = ClientContext.getI18nString("db_ip");
    private final String db_port = ClientContext.getI18nString("db_port");
    private final String db_user = ClientContext.getI18nString("db_user");
    private final String db_pwd = ClientContext.getI18nString("db_pwd");

    private JLabel lb_ip;
    private JTextField fd_ip;
    private JLabel lb_user;
    private JTextField fd_user;
    private JLabel lb_pwd;
    private JPasswordField fd_pwd;
    private JLabel lb_port;
    private JTextField fd_port;

    public SqlParamPanel() {
        initGui();
        initData();
    }

    /**
     * 
     */
    private void initData() {
        fd_ip.setText(ClientContext.getConfig().getServerIp());
        fd_port.setText(ClientContext.getConfig().getServerPort() + "");
        fd_user.setText(ClientContext.getConfig().getUserName());
        fd_pwd.setText(ClientContext.getConfig().getPassWord());
    }

    /**
     * 
     */
    private void initGui() {
        lb_ip = new JLabel(db_ip);
        fd_ip = new IpField(db_ip, ClientContext.getMainFrame().getMsgPanel(), ClientContext.getMainFrame()
                .getBtnNext());

        lb_user = new JLabel(db_user);
        fd_user = new NotNullField(db_user, ClientContext.getMainFrame().getMsgPanel(), ClientContext
                .getMainFrame().getBtnNext());

        lb_pwd = new JLabel(db_pwd);
        fd_pwd = new JPasswordField();

        lb_port = new JLabel(db_port);
        fd_port = new NumberField(db_port, ClientContext.getMainFrame().getMsgPanel(), ClientContext
                .getMainFrame().getBtnNext(), 1, 65535);

        double[][] ds = { { 10, 100, 10, 100, TableLayout.FILL, 10 }, { 10, 25, 10, 25, 10, 25, 10, 25, 10 } };
        this.setLayout(ClientUtils.getTableLayout(ds));

        this.add(lb_ip, "1,1,f,f");
        this.add(fd_ip, "3,1,4,f");

        this.add(lb_user, "1,3,f,f");
        this.add(fd_user, "3,3,4,f");

        this.add(lb_pwd, "1,5,f,f");
        this.add(fd_pwd, "3,5,4,f");

        this.add(lb_port, "1,7,f,f");
        this.add(fd_port, "3,7,4,f");
    }

    private void collectData() {
        GlobalConfig config = ClientContext.getConfig();
        config.setServerIp(fd_ip.getText());
        config.setServerPort(Integer.parseInt(fd_port.getText()));
        config.setUserName(fd_user.getText());
        config.setPassWord(new String(fd_pwd.getPassword()));
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#runTask()
     */
    @Override
    public boolean runTask() {
        collectData();
        try {
            DbOperator operator = new DbOperatorImpl(ClientContext.getConfig());
            operator.checkDbConnect();
        } catch (Exception e) {
            this.errorMessage = e.getMessage();
            return false;
        }
        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#getMessage()
     */
    @Override
    public String getMessage() {
        return ClientContext.getI18nString("msg_sqlpanel");
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#getName()
     */
    @Override
    public String getName() {
        return "SqlParameters";
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#isFirst()
     */
    @Override
    public boolean isFirst() {
        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#getHeader()
     */
    @Override
    public String getHeader() {
        return ClientContext.getI18nString("title_sqlpanel");
    }

}
