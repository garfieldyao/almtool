/*
 * $Id: AlarmSeverityAssign.java, 2013-4-16 ����10:58:54 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.task;

import com.mars.almtool.task.batch.BatchResult;

/**
 * <p>
 * Title: AlarmSeverityAssign
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2013-4-16 ����10:58:54
 * @modified [who date description]
 * @check [who date description]
 */
public interface AlarmSeverityAssign {
    public BatchResult doAssign();
}
