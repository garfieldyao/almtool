/**
 * 
 */
package com.mars.almtool.task.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.me.AlarmInfo;
import com.mars.almtool.me.enums.ImportMode;
import com.mars.almtool.task.DbOperator;
import com.mars.almtool.task.ImportTaskCmd;
import com.mars.almtool.utils.DbUtils;
import com.mars.almtool.utils.FileUtils;

/**
 * @author Yao
 * 
 */
public class ImportTaskCmdImpl implements ImportTaskCmd {
	private String filePath = "";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alu.almtool.task.ImportTaskCmd#importAlarmMapping(java.lang.String[])
	 */
	@Override
	public void importAlarmMapping(String[] args) {
		// TODO Auto-generated method stub
		if (!parseArgs(args)) {
			System.out.println(errorMsg());
			return;
		}

		if (!FileUtils.checkFileExist(filePath, false)) {
			System.out.println("Mapping file not exist");
			return;
		}

		Map<String, Map<String, AlarmInfo>> sourceFile = FileUtils
				.readSourceFile(filePath);
		if (sourceFile == null || sourceFile.size() == 0) {
			System.out.println("Load Mapping file failed");
			return;
		}

		List<String> sqlList = DbUtils.generalSqlList(sourceFile);
		DbOperator operator = new DbOperatorImpl(ClientContext.getConfig());
		try {
			operator.checkDbConnect();
			operator.importAlarm(sqlList);
			System.out.println("Import Alarm Mapping finished");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public boolean parseArgs(String[] args) {
		StringBuilder sb = new StringBuilder(" ");
		for (String str : args) {
			sb.append(str);
			sb.append(" ");
		}
		String cmd = sb.toString();
		String regx = null;
		Pattern pattern = null;
		Matcher matcher = null;
		String group = null;
		String mat = null;

		// -db
		regx = " -db[ ]?[^ ]+:[^ ]+@[0-9]+.[0-9]+.[0-9]+.[0-9]+:[0-9]+";
		pattern = Pattern.compile(regx);
		matcher = pattern.matcher(cmd);
		if (matcher.find()) {
			group = matcher.group();
			mat = group.substring(4).trim();
			ClientContext.getConfig().setUserName(
					mat.substring(0, mat.indexOf(":")));
			ClientContext.getConfig().setPassWord(
					mat.substring(mat.indexOf(":") + 1, mat.indexOf("@")));
			ClientContext.getConfig().setServerIp(
					mat.substring(mat.indexOf("@") + 1, mat.lastIndexOf(":")));
			ClientContext.getConfig().setServerPort(
					Integer.parseInt(mat.substring(mat.lastIndexOf(":") + 1)));
			cmd = cmd.replaceFirst(group, "");
		} else {
			return false;
		}

		// -mode
		regx = " -mode[ ]?((ctc)|(transparent))";
		pattern = Pattern.compile(regx);
		matcher = pattern.matcher(cmd);
		if (matcher.find()) {
			group = matcher.group();
			mat = group.substring(6).toLowerCase().trim();
			cmd = cmd.replaceFirst(group, "");
			if ("ctc".equals(mat)) {
				ClientContext.getConfig().setImportMode(ImportMode.CTC);
			} else if ("transparent".equals(mat)) {
				ClientContext.getConfig().setImportMode(ImportMode.TRANS);
			} else {
				return false;
			}
		} else {
			return false;
		}
		// -in
		cmd = cmd.trim();
		if (!cmd.startsWith("-in")) {
			return false;
		}

		filePath = cmd.substring(4);

		return true;
	}

	public static void main(String[] args) {
		String[] mms = new String[] { "-db", "root:mysql@135.251.31.190:3307",
				"-in", "D:/java/AlarmSrc/Alarm Mapping.xls", "-mode", "ctc" };
		new ImportTaskCmdImpl().parseArgs(mms);
	}

	private String errorMsg() {
		StringBuilder sb = new StringBuilder();
		sb.append("java -jar almtool.jar -in (Mapping file) -db (DB server) -mode (ctc/transparent)");
		sb.append("\r\n");
		sb.append("-in :");
		sb.append("Path of Alarm Mapping file");
		sb.append("\r\n");
		sb.append("-db :");
		sb.append("Database erver, user and password, E.G.: user:pwd@192.168.11.11:3006");
		sb.append("\r\n");
		sb.append("-mode :");
		sb.append("Mapping mode, ctc or transparent");
		sb.append("\r\n");
		sb.append("\r\n");
		return sb.toString();
	}

}
