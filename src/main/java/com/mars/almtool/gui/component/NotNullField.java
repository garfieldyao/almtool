/*
 * $Id: NotNullField.java, 2011-12-9 ����1:40:46 Yao Exp $
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

import org.apache.commons.lang.StringUtils;

import com.mars.almtool.gui.MessagePanel;

/**
 * <p>
 * Title: NotNullField
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-9 ����1:40:46
 * @modified [who date description]
 * @check [who date description]
 */
public class NotNullField extends ValidTextField {
    private static final long serialVersionUID = 8666347975662427635L;

    /**
     * @param title
     * @param msgPanel
     * @param comp
     */
    public NotNullField(String title, MessagePanel msgPanel, JComponent comp) {
        super(title, msgPanel, comp);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.component.ValidTextField#valid(java.lang.String)
     */
    @Override
    public boolean valid(String text) {
        return StringUtils.isNotEmpty(text) && StringUtils.isNotBlank(text);
    }

}
