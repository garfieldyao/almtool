/*
 * $Id: DbOperator.java, 2011-12-9 ����9:52:45 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.task;

import java.util.List;
import java.util.Map;

import com.mars.almtool.me.AlarmInfo;
import com.mars.almtool.me.AlarmInfoExt;

/**
 * <p>
 * Title: DbOperator
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-9 ����9:52:45
 * @modified [who date description]
 * @check [who date description]
 */
public interface DbOperator {
    public boolean checkDbConnect() throws Exception;

    public boolean importAlarm(List<String> sqlList) throws Exception;

    public List<AlarmInfoExt> mergeAlarmList(Map<String, Map<String, String>> trans_res,
            Map<String, AlarmInfo> neAlarms) throws Exception;
}
