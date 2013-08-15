/*
 * $Id: IpField.java, 2011-12-9 ����1:38:40 Yao Exp $
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
 * Title: IpField
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-9 ����1:38:40
 * @modified [who date description]
 * @check [who date description]
 */
public class IpField extends ValidTextField {
    private static final long serialVersionUID = 3063893551601523175L;

    /**
     * @param title
     * @param msgPanel
     * @param comp
     */
    public IpField(String title, MessagePanel msgPanel, JComponent comp) {
        super(title, msgPanel, comp);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.mars.almtool.gui.component.ValidTextField#valid(java.lang.String)
     */
    @Override
    public boolean valid(String text) {
        return GeneralUtils.isValidAddress(text);
    }

}
