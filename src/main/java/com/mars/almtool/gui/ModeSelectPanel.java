/*
 * $Id: WelcomPanel.java, 2012-3-13 ����3:16:05 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.gui.component.TableLayout;
import com.mars.almtool.me.enums.ImportMode;
import com.mars.almtool.utils.ClientUtils;

/**
 * <p>
 * Title: ModeSelectPanel
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2012-3-13 ����3:16:05
 * @modified [who date description]
 * @check [who date description]
 */
public class ModeSelectPanel extends ContentPanel {
    private static final long serialVersionUID = 2574061130586899507L;

    private final String title = ClientContext.getI18nString("title_welcom");
    private final String ctc_mode = ClientContext.getI18nString("ctc_mode");
    private final String transparent_mode = ClientContext.getI18nString("transparent_mode");
    private final String sync_mode = ClientContext.getI18nString("db_sync");
    private final String almassign_mode = ClientContext.getI18nString("alm_assign");

    private JRadioButton btnCtcMode;
    private JRadioButton btnTransparentMode;
    private JRadioButton btnSyncMode;
    private JRadioButton btnAssignMode;
    private ButtonGroup btnGroup;

    public ModeSelectPanel() {
        initGui();
        initAction();
    }

    /**
     * 
     */
    private void initAction() {
        btnCtcMode.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                select_changed();
            }
        });
        btnTransparentMode.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                select_changed();
            }
        });
        btnSyncMode.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                select_changed();
            }
        });
        btnAssignMode.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                select_changed();
            }
        });
    }
    
    private void select_changed(){
        ImportMode mode = ImportMode.CTC;
        if (btnCtcMode.isSelected())
            mode = ImportMode.CTC;
        else if (btnTransparentMode.isSelected())
            mode = ImportMode.TRANS;
        else if (btnSyncMode.isSelected())
            mode = ImportMode.SYNC;
        else if (btnAssignMode.isSelected())
            mode = ImportMode.ASSIGN;
        ClientContext.getConfig().setImportMode(mode);
        ClientContext.getMainFrame().updateContentPanel();
    }

    private void initGui() {
        btnCtcMode = new JRadioButton(ctc_mode);
        btnTransparentMode = new JRadioButton(transparent_mode);
        btnSyncMode = new JRadioButton(sync_mode);
        btnAssignMode = new JRadioButton(almassign_mode);
        btnGroup = new ButtonGroup();
        btnGroup.add(btnCtcMode);
        btnGroup.add(btnTransparentMode);
        btnGroup.add(btnSyncMode);
        btnGroup.add(btnAssignMode);
        btnCtcMode.setSelected(true);

        double[][] ds = { { 10, 20, TableLayout.FILL, 10 }, { 10, 30, 10, 30, 10, 30, 10, 30, 10 } };
        this.setLayout(ClientUtils.getTableLayout(ds));

        this.add(btnCtcMode, "2,1,f,f");
        this.add(btnTransparentMode, "2,3,f,f");
        this.add(btnSyncMode, "2,5,f,f");
        this.add(btnAssignMode, "2,7,f,f");
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#runTask()
     */
    @Override
    public boolean runTask() {
        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#getMessage()
     */
    @Override
    public String getMessage() {
        return title;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#getHeader()
     */
    @Override
    public String getHeader() {
        return title;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#getName()
     */
    @Override
    public String getName() {
        return "ModeSelect";
    }

}
