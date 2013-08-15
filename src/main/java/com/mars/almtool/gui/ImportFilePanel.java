/*
 * $Id: ImportFilePanel.java, 2011-12-8 ����11:44:14 Yao Exp $
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang.StringUtils;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.gui.component.TableLayout;
import com.mars.almtool.me.AlarmInfo;
import com.mars.almtool.me.AlarmInfoExt;
import com.mars.almtool.me.enums.ImportMode;
import com.mars.almtool.me.enums.MappingType;
import com.mars.almtool.task.DbOperator;
import com.mars.almtool.task.impl.DbOperatorImpl;
import com.mars.almtool.utils.ClientUtils;
import com.mars.almtool.utils.DbUtils;
import com.mars.almtool.utils.FileUtils;

/**
 * <p>
 * Title: ImportFilePanel
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-8 ����11:44:14
 * @modified [who date description]
 * @check [who date description]
 */
public class ImportFilePanel extends ContentPanel {
    private static final long serialVersionUID = -2763784767569011713L;
    private final String str_fielpath = ClientContext.getI18nString("str_fielpath");
    private final String str_import = ClientContext.getI18nString("str_import");
    private final String ex_nofile = ClientContext.getI18nString("ex_nofile");
    private final String ex_noinfo = ClientContext.getI18nString("ex_noinfo");

    private JLabel lb_file;
    private JTextField fd_file;
    private JButton btn_file;

    public ImportFilePanel() {
        initGui();
        initAction();
    }

    /**
     * 
     */
    private void initAction() {
        btn_file.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                loadFile();
            }
        });
    }

    /**
     * 
     */
    protected void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(str_import);
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("xls", "xls");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(fileFilter);
        int dlgresult = fileChooser.showOpenDialog(ClientContext.getMainFrame());
        if (dlgresult != JFileChooser.APPROVE_OPTION)
            return;
        String path = fileChooser.getSelectedFile().getAbsolutePath();
        if (StringUtils.isEmpty(path))
            return;
        if (!path.endsWith(".xls") && !path.endsWith(".XLS")) {
            path += ".xls";
        }
        fd_file.setText(path);
    }

    private void initGui() {
        lb_file = new JLabel(str_fielpath);
        fd_file = new JTextField();
        btn_file = new JButton(str_import);
        fd_file.setEditable(false);

        double[][] ds = { { 10, 100, 10, 100, TableLayout.FILL, 10, 100, 10 }, { 10, 30, 10 } };
        this.setLayout(ClientUtils.getTableLayout(ds));

        this.add(lb_file, "1,1,f,f");
        this.add(fd_file, "3,1,4,f");
        this.add(btn_file, "6,1,f,f");
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#runTask()
     */
    @Override
    public boolean runTask() {
        boolean isNewFile = false;
        String filePath = fd_file.getText();
        if (!FileUtils.checkFileExist(filePath, false)) {
            if (ClientContext.getConfig().getImportMode() == ImportMode.SYNC) {
                FileUtils.checkFileExist(filePath, true);
                isNewFile = true;
            } else {
                this.errorMessage = ex_nofile;
                return false;
            }
        }
        Map<String, Map<String, AlarmInfo>> sourceFile = FileUtils.readSourceFile(filePath);
        if (sourceFile == null || sourceFile.size() == 0) {
            this.errorMessage = ex_noinfo;
            return false;
        }

        if (ClientContext.getConfig().getImportMode() == ImportMode.SYNC) {
            String trans_path = ClientContext.getResourceFactory().getConfigFilePath(
                    ClientContext.getConfig().getTrans_file());
            if (StringUtils.isEmpty(trans_path)) {
                this.errorMessage = ex_noinfo;
                return false;
            }
            Map<String, Map<String, String>> trans_res = FileUtils.importTranslationFile(trans_path);
            Map<String, AlarmInfo> neAlarms = sourceFile.get(MappingType.NE.name());
            DbOperator operator = new DbOperatorImpl(ClientContext.getConfig());
            try {
                List<AlarmInfoExt> alarmList = operator.mergeAlarmList(trans_res, neAlarms);
                String alarmPath = ClientContext.getResourceFactory().getConfigFilePath(
                        ClientContext.getConfig().getAlarm_file());
                if (StringUtils.isNotEmpty(alarmPath)) {
                    FileUtils.mergeAlarmList(alarmPath, alarmList);
                }
                String filePathNew = filePath;
                if (!isNewFile) {
                    filePathNew = FileUtils.findFreeFileName(filePath);
                }
                FileUtils.export2xls(alarmList, filePathNew);
                List<String> tabs = new ArrayList<String>();
                tabs.addAll(ClientContext.getConfig().getEms_tab());
                tabs.addAll(ClientContext.getConfig().getCtc_tab());
                FileUtils.mergeXls(filePath, filePathNew, tabs.toArray(new String[0]));
            } catch (Exception e) {
                this.errorMessage = e.getMessage();
                return false;
            }
        } else {
            List<String> sqlList = DbUtils.generalSqlList(sourceFile);
            DbOperator operator = new DbOperatorImpl(ClientContext.getConfig());
            try {
                operator.importAlarm(sqlList);
            } catch (Exception e) {
                this.errorMessage = e.getMessage();
                return false;
            }
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
        return ClientContext.getI18nString("msg_importpanel");
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#getName()
     */
    @Override
    public String getName() {
        return "ImportInvFile";
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#isLast()
     */
    @Override
    public boolean isLast() {
        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.ContentPanel#getHeader()
     */
    @Override
    public String getHeader() {
        return ClientContext.getI18nString("title_importpanel");
    }
}
