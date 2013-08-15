package com.mars.almtool;

import javax.swing.UIManager;

import org.apache.commons.lang.ArrayUtils;

import com.mars.almtool.gui.MainView;
import com.mars.almtool.task.ImportTaskCmd;
import com.mars.almtool.task.InitParam;
import com.mars.almtool.task.impl.ImportTaskCmdImpl;
import com.mars.almtool.task.impl.InitParamImpl;

public class StartClient {
	public static void main(String[] args) {
		// init param
		InitParam init = new InitParamImpl();
		init.initParam();

		// Add command mode
		if (ArrayUtils.isEmpty(args)) {
			// SetLookAndFeel
			try {
				UIManager.setLookAndFeel(UIManager
						.getCrossPlatformLookAndFeelClassName());
			} catch (Exception ex) {

			}

			// open gui
			MainView view = new MainView();
			view.setVisible(true);
		} else {
			ImportTaskCmd cmdTask = new ImportTaskCmdImpl();
			cmdTask.importAlarmMapping(args);
		}
	}
}
