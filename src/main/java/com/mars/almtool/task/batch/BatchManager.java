/*
 * $Id: BatchManager.java, 2011-11-10 ����10:09:08 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.task.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.commons.collections.CollectionUtils;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.me.NetworkElement;

/**
 * <p>
 * Title: BatchManager
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-11-10 ����10:09:08
 * @modified [who date description]
 * @check [who date description]
 */
public abstract class BatchManager {
    private List<NetworkElement> neList;
    private Executor exec = Executors.newFixedThreadPool(ClientContext.MAX_Threads);
    private List<FutureTask<BatchResult>> tasklist = new ArrayList<FutureTask<BatchResult>>();
    private List<BatchResult> results = new ArrayList<BatchResult>();

    public BatchManager(List<NetworkElement> neList) {
        if (neList != null) {
            this.neList = new ArrayList<NetworkElement>();
            this.neList.addAll(neList);
            buildTaskList();
        } else {
            throw new NullPointerException();
        }
    }

    public List<BatchResult> execute() {
        runTasks();
        if (CollectionUtils.isNotEmpty(results))
            results.clear();
        while (tasklist.size() > 0) {
            int size = tasklist.size();
            for (int k = 0; k < size; k++) {
                FutureTask<BatchResult> task = tasklist.get(k);
                if (task.isDone()) {
                    try {
                        BatchResult result = task.get();
                        results.add(result);
                    } catch (Exception ex) {
                    }
                    tasklist.remove(task);
                    break;
                }
            }
        }
        return results;
    }

    private void buildTaskList() {
        if (CollectionUtils.isNotEmpty(tasklist))
            tasklist.clear();
        for (final NetworkElement ne : neList) {
            FutureTask<BatchResult> task = new FutureTask<BatchResult>(new Callable<BatchResult>() {
                @Override
                public BatchResult call() throws Exception {
                    BatchResult result = new BatchResult();
                    try {
                        result = runTask(ne);
                    } catch (Exception ex) {
                        result.setException(ex);
                    }
                    result.setIpAddr(ne.getIpAddress());
                    return result;
                }
            });

            tasklist.add(task);
        }
    }

    private void runTasks() {
        for (FutureTask<BatchResult> task : tasklist) {
            exec.execute(task);
        }
    }

    protected abstract BatchResult runTask(NetworkElement ne);

}
