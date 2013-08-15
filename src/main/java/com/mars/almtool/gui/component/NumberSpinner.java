/*
 * $Id: NumberSpinner.java, 2011-10-29 ����11:50:12 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.gui.component;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>
 * Title: NumberSpinner
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-10-29 ����11:50:12
 * @modified [who date description]
 * @check [who date description]
 */
public class NumberSpinner extends JSpinner {
    private static final long serialVersionUID = -6996296013673635915L;
    int minNum = 0;
    int maxNum = 65536;
    boolean caseMax = true;
    boolean caseMin = true;

    public NumberSpinner(final int minNum, final int maxNum) {
        this.minNum = minNum;
        this.maxNum = maxNum;
        caseMax = true;
        caseMin = true;
        this.setValue(0);
        initLisener();
    }

    public NumberSpinner(final int minNum, final int maxNum, boolean caseMin, boolean caseMax) {
        this.minNum = minNum;
        this.maxNum = maxNum;
        this.caseMin = caseMin;
        this.caseMax = caseMax;
        this.setValue(0);
        initLisener();
    }

    private void initLisener() {
        this.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (Integer) getValue();
                if (caseMin && value < minNum)
                    setValue(minNum);
                if (caseMax && value > maxNum)
                    setValue(maxNum);
            }
        });
    }
}
