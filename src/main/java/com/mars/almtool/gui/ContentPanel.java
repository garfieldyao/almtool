/*
 * $Id: ContentPanel.java, 2011-12-8 ����11:03:07 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.gui;

import javax.swing.JPanel;

/**
 * <p>
 * Title: ContentPanel
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-8 ����11:03:07
 * @modified [who date description]
 * @check [who date description]
 */
public abstract class ContentPanel extends JPanel {
    private static final long serialVersionUID = 8093472759230468722L;

    protected String errorMessage = "";

    private ContentPanel nextPanel = null;
    private ContentPanel previousPanel = null;

    public void setNext(ContentPanel nextPanel) {
        this.nextPanel = nextPanel;
    }

    public void setPrevious(ContentPanel previousPanel) {
        this.previousPanel = previousPanel;
    }

    public boolean hasNext() {
        return nextPanel != null;
    }

    public ContentPanel getNext() {
        return nextPanel;
    }

    public ContentPanel getPrevious() {
        return previousPanel;
    }

    public boolean isFirst() {
        return this.getPrevious() == null;
    }

    public boolean isLast() {
        return this.getNext() == null;
    }

    public boolean hasPrevious() {
        return previousPanel != null;
    }

    public abstract boolean runTask();

    public String getErrorMessage() {
        return errorMessage;
    }

    public void afterRunTask() {

    }

    public abstract String getMessage();

    public abstract String getHeader();

    public abstract String getName();
}
