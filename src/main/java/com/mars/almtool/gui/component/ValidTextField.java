/*
 * $Id: ValidTextField.java, 2011-12-9 ����1:22:42 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.gui.component;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.gui.MessagePanel;

/**
 * <p>
 * Title: ValidTextField
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-9 ����1:22:42
 * @modified [who date description]
 * @check [who date description]
 */
public abstract class ValidTextField extends JTextField {
    private static final long serialVersionUID = 1800800738269748619L;
    private final String str_inputerror = ClientContext.getI18nString("str_inputerror");

    private String title = "";
    private MessagePanel msgPanel;
    private JComponent comp;

    public abstract boolean valid(String text);

    public ValidTextField(String title) {
        this.title = title;
        this.msgPanel = null;
        this.comp = null;
        initAction();
    }

    public ValidTextField(String title, MessagePanel msgPanel) {
        this.title = title;
        this.msgPanel = msgPanel;
        this.comp = null;
        initAction();
    }

    public ValidTextField(String title, MessagePanel msgPanel, JComponent comp) {
        this.title = title;
        this.msgPanel = msgPanel;
        this.comp = comp;
        initAction();
    }

    /**
     * 
     */
    private void initAction() {
        this.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                showError(valid(getText()));
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                showError(valid(getText()));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                showError(valid(getText()));
            }
        });
    }

    private void showError(boolean noerr) {
        if (!noerr) {
            if (msgPanel != null) {
                msgPanel.showWarning(title + " " + str_inputerror);
            }
            if (comp != null) {
                comp.setEnabled(false);
            }
        } else {
            if (msgPanel != null) {
                msgPanel.showWarning(null);
            }
            if (comp != null) {
                comp.setEnabled(true);
            }
        }
    }
}
