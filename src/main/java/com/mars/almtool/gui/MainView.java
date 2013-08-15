/*
 * $Id: MainView.java, 2011-12-8 ����12:47:53 Yao Exp $
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
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.gui.component.TableLayout;
import com.mars.almtool.me.enums.ImportMode;
import com.mars.almtool.utils.ClientUtils;

/**
 * <p>
 * Title: MainView
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-8 ����12:47:53
 * @modified [who date description]
 * @check [who date description]
 */
public class MainView extends JFrame {
    private static final long serialVersionUID = -896691377807681746L;
    private MessagePanel msgPanel;
    private JButton btnNext;
    private JButton btnPrevious;
    private ContentPanel currentPanel;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    private ModeSelectPanel welcomPanel;
    private SqlParamPanel sqlParamPanel;
    private ImportFilePanel importFilePanel;
    private DeviceParamPanel devicePanel;
    private RuleParamPanel rulePanel;

    public MainView() {
        ClientContext.setMainFrame(this);
        initGui();
        initAction();
    }

    private void initGui() {
        msgPanel = new MessagePanel();

        btnNext = new JButton(ClientContext.getI18nString("btn_next"));
        btnPrevious = new JButton(ClientContext.getI18nString("btn_previous"));
        double[][] ds = { { 10, TableLayout.FILL, 80, 10, 80, 10 }, { 5, 2, 30, 5 } };
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(ClientUtils.getTableLayout(ds));
        btnPanel.add(new JSeparator(), "0,0,5,f");
        btnPanel.add(btnPrevious, "2,2,1,f");
        btnPanel.add(btnNext, "4,2,1,f");

        initContentPanel();

        this.setLayout(new BorderLayout());
        this.add(msgPanel, BorderLayout.NORTH);
        this.add(btnPanel, BorderLayout.SOUTH);
        this.add(contentPanel, BorderLayout.CENTER);

        Dimension screenSize = ClientUtils.getScreenSize();
        this.setSize(screenSize.width * 2 / 3, screenSize.height * 2 / 3);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(ClientContext.getLogoIcon());
        this.setTitle(ClientContext.getI18nString("title_main"));

        this.setResizable(true);

        ClientUtils.centerWindow(this);
    }

    private void initContentPanel() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel();
        contentPanel.setLayout(cardLayout);

        welcomPanel = new ModeSelectPanel();
        sqlParamPanel = new SqlParamPanel();
        importFilePanel = new ImportFilePanel();
        devicePanel = new DeviceParamPanel();
        rulePanel = new RuleParamPanel();

        contentPanel.add(welcomPanel, welcomPanel.getName());
        contentPanel.add(sqlParamPanel, sqlParamPanel.getName());
        contentPanel.add(importFilePanel, importFilePanel.getName());
        contentPanel.add(devicePanel, devicePanel.getName());
        contentPanel.add(rulePanel, rulePanel.getName());

        updateContentPanel();
        changePanel(welcomPanel);
    }

    public void updateContentPanel() {
        if (ClientContext.getConfig().getImportMode() == ImportMode.ASSIGN) {
            welcomPanel.setNext(rulePanel);
            rulePanel.setPrevious(welcomPanel);
            rulePanel.setNext(devicePanel);
            devicePanel.setPrevious(rulePanel);
        } else {
            welcomPanel.setNext(sqlParamPanel);
            sqlParamPanel.setPrevious(welcomPanel);
            sqlParamPanel.setNext(importFilePanel);
            importFilePanel.setPrevious(sqlParamPanel);
        }
    }

    private void initAction() {
        getBtnNext().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final WaitingDialog dialog = ClientUtils.getWaitingDialog();
                new SwingWorker<Boolean, Void>() {

                    @Override
                    protected Boolean doInBackground() throws Exception {
                        dialog.showWaitingDialog();
                        return currentPanel.runTask();
                    }

                    protected void done() {
                        try {
                            Boolean succ = get();
                            if (succ) {
                                if (currentPanel.isLast()) {
                                    btnNext.setText(ClientContext.getI18nString("btn_complete"));
                                    btnNext.setEnabled(false);
                                    getMsgPanel().showMessage(ClientContext.getI18nString("msg_importvoer"));
                                }
                                if (currentPanel.hasNext()) {
                                    changePanel(currentPanel.getNext());
                                }
                            } else {
                                getMsgPanel().showError(currentPanel.getErrorMessage());
                            }
                            if (SwingUtilities.isEventDispatchThread()) {
                                currentPanel.afterRunTask();
                            } else {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        currentPanel.afterRunTask();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            dialog.closeWaitingDialog();
                        }
                    };
                }.execute();
            }
        });

        btnPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPanel.hasPrevious()) {
                    changePanel(currentPanel.getPrevious());
                }
            }
        });
    }

    private void changePanel(ContentPanel panel) {
        cardLayout.show(contentPanel, panel.getName());
        this.currentPanel = panel;
        getMsgPanel().showHeader(panel.getHeader());
        getMsgPanel().showMessage(panel.getMessage());

        if (currentPanel.isLast()) {
            getBtnNext().setText(ClientContext.getI18nString("btn_run"));
        } else {
            getBtnNext().setText(ClientContext.getI18nString("btn_next"));
        }

        getBtnNext().setEnabled(panel.hasNext() || panel.isLast());
        btnPrevious.setEnabled(panel.hasPrevious());
    }

    public MessagePanel getMsgPanel() {
        return msgPanel;
    }

    public JButton getBtnNext() {
        return btnNext;
    }
}
