/*
 * $Id: NumberField.java, 2011-12-9 ����1:39:47 Yao Exp $
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

import com.mars.almtool.gui.MessagePanel;
import com.mars.almtool.utils.GeneralUtils;

/**
 * <p>
 * Title: NumberField
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-9 ����1:39:47
 * @modified [who date description]
 * @check [who date description]
 */
public class NumberField extends ValidTextField {
    private static final long serialVersionUID = 4081906758573248300L;
    private int min = 0;
    private int max = 0;

    /**
     * @param title
     * @param msgPanel
     * @param comp
     */
    public NumberField(String title, MessagePanel msgPanel, JComponent comp, int min, int max) {
        super(title, msgPanel, comp);
        this.max = max;
        this.min = min;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.component.ValidTextField#valid(java.lang.String)
     */
    @Override
    public boolean valid(String text) {
        boolean flag = GeneralUtils.validNumberString(text);
        if (!flag)
            return flag;
        int value = Integer.parseInt(text);
        return value >= min && value <= max;
    }

}
