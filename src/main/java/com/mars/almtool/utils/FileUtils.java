/*
 * $Id: FileUtils.java, 2011-12-8 下午12:46:47 Yao Exp $
 * 
 * Copyright (c) 2011 Alcatel-Lucent Shanghai Bell Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ASB or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.mars.almtool.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.mars.almtool.comm.ClientContext;
import com.mars.almtool.me.AlarmInfo;
import com.mars.almtool.me.AlarmInfoExt;
import com.mars.almtool.me.GlobalConfig;
import com.mars.almtool.me.enums.ImportMode;
import com.mars.almtool.me.enums.Lanaguage;
import com.mars.almtool.me.enums.MappingType;
import com.mars.almtool.me.enums.NeType;
import com.mars.almtool.task.impl.LogFactory;

/**
 * <p>
 * Title: FileUtils
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Yao Liqiang
 * @created 2011-12-8 下午12:46:47
 * @modified [who date description]
 * @check [who date description]
 */
public class FileUtils {
    public final static String EMS_ALARM = "0/0";
    public final static String ProbableCause = "ProbableCause_Mapping";
    public final static String SpecificProblem = "SpecificProblem_Mapping";
    public final static String RepairActions = "RepairActions_Mapping";
    public final static String ALARM_DEFINE = "NE Alarms";
    public final static String ALARM_TYPE = "Alarm Type";
    public final static String File_Encode = "Unicode";
    public final static char File_Separator = '\t';

    public static Map<String, Map<String, AlarmInfo>> readSourceFile(String filePath) {
        Logger logger = ClientContext.getLogger(LogFactory.LOG_ERROR);
        Map<String, Map<String, AlarmInfo>> alarmMap = new HashMap<String, Map<String, AlarmInfo>>();
        Map<String, AlarmInfo> ctcalarm = new HashMap<String, AlarmInfo>();
        Map<String, AlarmInfo> ctcalarm_mdu = new HashMap<String, AlarmInfo>();
        Map<String, AlarmInfo> emsAlarm = new HashMap<String, AlarmInfo>();
        Map<String, AlarmInfo> neAlarm = new HashMap<String, AlarmInfo>();

        alarmMap.put(MappingType.CTC.name(), ctcalarm);
        alarmMap.put(MappingType.EMS.name(), emsAlarm);
        alarmMap.put(MappingType.NE.name(), neAlarm);
        alarmMap.put(MappingType.CTC_MDU.name(), ctcalarm_mdu);

        GlobalConfig config = ClientContext.getConfig();

        try {
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath));
            for (int k = 0; k < wb.getNumberOfSheets(); k++) {
                HSSFSheet sheet = wb.getSheetAt(k);
                String sheetName = sheet.getSheetName();
                if (StringUtils.isEmpty(sheetName))
                    continue;

                boolean isCtc = config.getCtc_tab().contains(sheetName);
                boolean isEms = config.getEms_tab().contains(sheetName);
                boolean isNe = config.getNe_tab().contains(sheetName);

                if (config.getImportMode() == ImportMode.TRANS) {
                    if (!(isEms || isNe))
                        continue;
                } else if (config.getImportMode() == ImportMode.CTC) {
                    if (!isCtc)
                        continue;
                } else if (config.getImportMode() == ImportMode.SYNC) {
                    if (!isNe)
                        continue;
                } else {
                    continue;
                }

                int rows = sheet.getPhysicalNumberOfRows();
                for (int r = 0; r < rows; r++) {
                    HSSFRow row = sheet.getRow(r);

                    if (row == null) {
                        continue;
                    }

                    if (isNe) {
                        String alarmType = getCellValue(row.getCell(config.getTransparent_almtype()));
                        String alarmNumber = getCellValue(row.getCell(config.getTransparent_almnum()));
                        String alarmName = getCellValue(row.getCell(config.getTransparent_name()));
                        if (StringUtils.isNotEmpty(alarmType) && StringUtils.isNotEmpty(alarmNumber)) {
                            AlarmInfo info = new AlarmInfo();
                            info.setAlarmId(alarmType, alarmNumber);
                            info.setAlarmName(alarmName);
                            neAlarm.put(info.getAlarmId(), info);
                        }
                    } else if (isCtc) {
                        String alarmId = getCellValue(row.getCell(config.getAlarmId()));
                        String alarmCode = getCellValue(row.getCell(config.getAlarmCode()));
                        String alarmName = getCellValue(row.getCell(config.getAlarmName()));
                        String alarmCause = getCellValue(row.getCell(config.getAlarmCause()));
                        String alarmSource = getCellValue(row.getCell(config.getAlarmSource()));
                        String alarmDesc = getCellValue(row.getCell(config.getAlarmDescription()));
                        if (GeneralUtils.validAlarmId(alarmId)) {
                            AlarmInfo info = new AlarmInfo();
                            info.setAlarmId(alarmId);
                            info.setAlarmCause(alarmCause);
                            info.setAlarmSource(alarmSource);
                            info.setAlarmCode(alarmCode);
                            info.setAlarmName(alarmName);
                            info.setAlarmDescription(alarmDesc);
                            if (EMS_ALARM.equals(alarmId)) {
                                ctcalarm.put(alarmCause, info);
                            } else if (GeneralUtils.isMdu(alarmSource)) {
                                ctcalarm_mdu.put(alarmId, info);
                            } else {
                                ctcalarm.put(alarmId, info);
                            }
                        }
                    } else if (isEms) {
                        String alarmId = getCellValue(row.getCell(config.getEms_key()));
                        String alarmName = getCellValue(row.getCell(config.getEms_name()));
                        if (StringUtils.isNotEmpty(alarmId)) {
                            AlarmInfo info = new AlarmInfo();
                            info.setAlarmId(alarmId);
                            info.setAlarmName(alarmName);
                            emsAlarm.put(alarmId, info);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            logger.error(ex.toString());
        }
        return alarmMap;
    }

    private static String getCellValue(HSSFCell cell) {
        String value = "";
        try {
            value = cell.getStringCellValue().trim();
        } catch (Exception ex) {

        }

        if (StringUtils.isEmpty(value)) {
            try {
                value = (int) cell.getNumericCellValue() + "";
            } catch (Exception ex) {

            }
        }
        return value;
    }

    public static boolean checkFileExist(String filePath, boolean create) {
        try {
            File file = new File(filePath);
            boolean exists = file.exists();
            if (!exists && create) {
                if (filePath.endsWith(".xls") || filePath.endsWith(".XLS")) {
                    HSSFWorkbook wb = new HSSFWorkbook();
                    FileOutputStream fileOut;
                    fileOut = new FileOutputStream(filePath);
                    wb.write(fileOut);
                    fileOut.close();
                } else {
                    file.createNewFile();
                }
                exists = true;
            }
            return exists;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String findFreeFileName(String filePath) {
        String filePathNew = filePath;
        for (int pi = 1; pi < 65535; pi++) {
            StringBuilder sb = new StringBuilder();
            sb.append(filePath.substring(0, filePath.lastIndexOf('.')));
            sb.append("(");
            sb.append(pi);
            sb.append(")");
            sb.append(".xls");
            String tmp = sb.toString();
            if (!FileUtils.checkFileExist(tmp, false)) {
                filePathNew = tmp;
                break;
            }
        }
        return filePathNew;
    }

    public static Map<String, Map<String, String>> importTranslationFile(String filePath) {
        Logger logger = ClientContext.getLogger(LogFactory.LOG_ERROR);
        Map<String, String> res_cause = new HashMap<String, String>();
        Map<String, String> res_repair = new HashMap<String, String>();
        Map<String, String> res_specific = new HashMap<String, String>();
        Map<String, Map<String, String>> res = new HashMap<String, Map<String, String>>();
        res.put(ProbableCause, res_cause);
        res.put(RepairActions, res_repair);
        res.put(SpecificProblem, res_specific);
        CSVReader reader = null;
        int trans = ClientContext.getConfig().getTrans_lang() == Lanaguage.CN ? 4 : 3;
        try {
            reader = new CSVReader(new InputStreamReader(new FileInputStream(filePath), File_Encode),
                    File_Separator);
            while (true) {
                String[] result = reader.readNext();
                if (ArrayUtils.isEmpty(result)) {
                    break;
                }

                if (result.length < 5)
                    continue;
                if (StringUtils.isNotEmpty(result[1])) {
                    if (result[1].contains(ProbableCause)) {
                        res_cause.put(result[2], result[trans]);
                    } else if (result[1].contains(SpecificProblem)) {
                        res_specific.put(result[2], result[trans]);
                    } else if (result[1].contains(RepairActions)) {
                        res_repair.put(result[2], result[trans]);
                    }
                }
            }
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            logger.error(ex.toString());
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return res;
    }

    public static void export2xls(List<AlarmInfoExt> alarmList, String filePath) {
        Logger logger = ClientContext.getLogger(LogFactory.LOG_ERROR);

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Total");
        HSSFRow row_title = sheet.createRow(0);
        row_title.createCell(0).setCellValue("Alarm Type");
        row_title.createCell(1).setCellValue("Alarm Number");
        row_title.createCell(2).setCellValue("GPON");
        row_title.createCell(3).setCellValue("EPON");
        row_title.createCell(4).setCellValue("MDU7353");
        row_title.createCell(5).setCellValue("iSAM/FX");
        row_title.createCell(6).setCellValue("Alarm ID");
        row_title.createCell(7).setCellValue("Alarm Name");
        row_title.createCell(8).setCellValue("Alarm Description");
        row_title.createCell(9).setCellValue("Repair Advice");
        row_title.createCell(10).setCellValue("Alarm Severity");
        row_title.createCell(11).setCellValue("Alarm Category");
        row_title.createCell(12).setCellValue("Service Affect");
        row_title.createCell(13).setCellValue("Alarm Index");

        for (int i = 0; i < alarmList.size(); i++) {
            HSSFRow row_data = sheet.createRow(i + 1);
            AlarmInfoExt alarmInfo = alarmList.get(i);
            int[] alarmid = alarmInfo.getAlarmType_Num();
            row_data.createCell(0).setCellValue(alarmid[0]);
            row_data.createCell(1).setCellValue(alarmid[1]);
            row_data.createCell(2).setCellValue(alarmInfo.isGpon() ? "Y" : "N");
            row_data.createCell(3).setCellValue(alarmInfo.isEpon() ? "Y" : "N");
            row_data.createCell(4).setCellValue(alarmInfo.isMdu() ? "Y" : "N");
            row_data.createCell(5).setCellValue(alarmInfo.isIsam() ? "Y" : "N");
            row_data.createCell(6).setCellValue(alarmInfo.getAlarmKey());
            row_data.createCell(7).setCellValue(alarmInfo.getAlarmName());
            row_data.createCell(8).setCellValue(alarmInfo.getAlarmDescription());
            row_data.createCell(9).setCellValue(alarmInfo.getAlarmAdvice());
            row_data.createCell(10).setCellValue(alarmInfo.getAlarmSeverity().getTransName());
            row_data.createCell(11).setCellValue(alarmInfo.getAlarmCategory().getTransName());
            row_data.createCell(12).setCellValue(alarmInfo.getAlarmServAffect().getTransName());
            row_data.createCell(13).setCellValue(alarmInfo.getAlarmSourceIndex());
        }

        createSplitTab(wb, alarmList, NeType.GPON);
        createSplitTab(wb, alarmList, NeType.EPON);
        createSplitTab(wb, alarmList, NeType.MDU7353);
        createSplitTab(wb, alarmList, NeType.ISAM);

        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(filePath);
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            logger.error(ex.toString());
        }
    }

    public static void mergeXls(String srcPath, String dstPath, String... tabs) {
        if (!checkFileExist(srcPath, false) || !checkFileExist(dstPath, false) || ArrayUtils.isEmpty(tabs)) {
            return;
        }
        Logger logger = ClientContext.getLogger(LogFactory.LOG_ERROR);

        try {
            HSSFWorkbook wb_src = new HSSFWorkbook(new FileInputStream(srcPath));
            HSSFWorkbook wb_dst = new HSSFWorkbook(new FileInputStream(dstPath));
            for (String tab : tabs) {
                HSSFSheet srcSheet = wb_src.getSheet(tab);
                if (srcSheet != null) {
                    HSSFSheet dstSheet = wb_dst.createSheet(tab);
                    copySheet(srcSheet, dstSheet);
                }
            }

            FileOutputStream fileOut = new FileOutputStream(dstPath);
            wb_dst.write(fileOut);
            fileOut.close();
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }

    private static void copySheet(HSSFSheet srcSheet, HSSFSheet dstSheet) {
        dstSheet.setMargin(HSSFSheet.TopMargin, srcSheet.getMargin(HSSFSheet.TopMargin));
        dstSheet.setMargin(HSSFSheet.BottomMargin, srcSheet.getMargin(HSSFSheet.BottomMargin));
        dstSheet.setMargin(HSSFSheet.LeftMargin, srcSheet.getMargin(HSSFSheet.LeftMargin));
        dstSheet.setMargin(HSSFSheet.RightMargin, srcSheet.getMargin(HSSFSheet.RightMargin));

        for (int i = 0; i < dstSheet.getNumMergedRegions(); i++) {
            dstSheet.addMergedRegion(srcSheet.getMergedRegion(i));
        }

        HSSFRow row = srcSheet.getRow(0);
        if (row != null) {
            for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                dstSheet.setColumnWidth(i, srcSheet.getColumnWidth(i));
                if (i > 100)
                    break;
            }
        }

        for (int i = 0; i < srcSheet.getPhysicalNumberOfRows(); i++) {
            HSSFRow srcRow = srcSheet.getRow(i);
            if (srcRow != null) {
                HSSFRow dstRow = dstSheet.createRow(i);
                copyRow(srcRow, dstRow);
            }
        }

    }

    private static void copyRow(HSSFRow srcRow, HSSFRow dstRow) {
        for (int i = 0; i < srcRow.getPhysicalNumberOfCells(); i++) {
            HSSFCell srcCell = srcRow.getCell(i);
            if (srcCell == null)
                break;
            HSSFCell dstCell = dstRow.createCell(i);
            int cellType = srcCell.getCellType();
            dstCell.setCellType(cellType);
            // dstCell.setCellStyle(srcCell.getCellStyle());
            switch (cellType) {
            case HSSFCell.CELL_TYPE_STRING:
                dstCell.setCellValue(srcCell.getStringCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                dstCell.setCellValue(srcCell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                dstCell.setCellValue(srcCell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                dstCell.setCellValue(srcCell.getCellFormula());
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                dstCell.setCellValue(srcCell.getErrorCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                dstCell.setCellValue("");
                break;
            default:
                dstCell.setCellValue(srcCell.getRichStringCellValue());
                break;
            }
        }
        dstRow.setHeight(srcRow.getHeight());
    }

    private static void createSplitTab(HSSFWorkbook wb, List<AlarmInfoExt> alarmList, NeType type) {
        String sheetName = "";
        if (type == NeType.GPON) {
            sheetName = "GPON";
        } else if (type == NeType.EPON) {
            sheetName = "EPON";
        } else if (type == NeType.MDU7353) {
            sheetName = "MDU7353";
        } else if (type == NeType.ISAM) {
            sheetName = "iSAM_FX";
        } else {
            return;
        }

        HSSFSheet spSheet = wb.createSheet(sheetName);
        HSSFRow sp_row_title = spSheet.createRow(0);
        sp_row_title.createCell(0).setCellValue("Alarm Type");
        sp_row_title.createCell(1).setCellValue("Alarm Number");
        sp_row_title.createCell(2).setCellValue("Alarm ID");
        sp_row_title.createCell(3).setCellValue("Alarm Name");
        sp_row_title.createCell(4).setCellValue("Alarm Description");
        sp_row_title.createCell(5).setCellValue("Repair Advice");
        sp_row_title.createCell(6).setCellValue("Alarm Severity");
        sp_row_title.createCell(7).setCellValue("Alarm Category");
        sp_row_title.createCell(8).setCellValue("Service Affect");
        sp_row_title.createCell(9).setCellValue("Alarm Index");

        int idx = 1;
        for (AlarmInfoExt alarmInfo : alarmList) {
            if (!(((type == NeType.GPON) && alarmInfo.isGpon())
                    || ((type == NeType.EPON) && alarmInfo.isEpon())
                    || ((type == NeType.MDU7353) && alarmInfo.isMdu()) || ((type == NeType.ISAM) && alarmInfo
                    .isIsam()))) {
                continue;
            }
            HSSFRow sp_row_data = spSheet.createRow(idx);
            int[] alarmid = alarmInfo.getAlarmType_Num();
            sp_row_data.createCell(0).setCellValue(alarmid[0]);
            sp_row_data.createCell(1).setCellValue(alarmid[1]);
            sp_row_data.createCell(2).setCellValue(alarmInfo.getAlarmKey());
            sp_row_data.createCell(3).setCellValue(alarmInfo.getAlarmName());
            sp_row_data.createCell(4).setCellValue(alarmInfo.getAlarmDescription());
            sp_row_data.createCell(5).setCellValue(alarmInfo.getAlarmAdvice());
            sp_row_data.createCell(6).setCellValue(alarmInfo.getAlarmSeverity().getTransName());
            sp_row_data.createCell(7).setCellValue(alarmInfo.getAlarmCategory().getTransName());
            sp_row_data.createCell(8).setCellValue(alarmInfo.getAlarmServAffect().getTransName());
            sp_row_data.createCell(9).setCellValue(alarmInfo.getAlarmSourceIndex());
            idx++;
        }
    }

    public static void mergeAlarmList(String filePath, List<AlarmInfoExt> alarmList) {
        if (!FileUtils.checkFileExist(filePath, false))
            return;
        Logger logger = ClientContext.getLogger(LogFactory.LOG_ERROR);
        try {
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath));
            for (int k = 0; k < wb.getNumberOfSheets(); k++) {
                HSSFSheet sheet = wb.getSheetAt(k);
                String sheetName = sheet.getSheetName();
                if (!ALARM_DEFINE.equals(sheetName))
                    continue;

                int rows = sheet.getPhysicalNumberOfRows();
                if (rows < 2)
                    return;
                HSSFRow title_row = sheet.getRow(0);
                int row_start = 0;
                for (row_start = 0; row_start < 100; row_start++) {
                    String cellValue = getCellValue(title_row.getCell(row_start));
                    if (ALARM_TYPE.equals(cellValue))
                        break;
                }

                if (row_start == 0)
                    return;

                int alarmType = 0;
                String alarmIfIndex = "";
                for (int r = 1; r < rows; r++) {
                    HSSFRow row = sheet.getRow(r);
                    if (row == null) {
                        continue;
                    }

                    String cellValue = getCellValue(row.getCell(row_start));
                    int srcType = GeneralUtils.filterAlarmType(cellValue);
                    if (srcType > 0) {
                        alarmType = srcType;
                        alarmIfIndex = getCellValue(row.getCell(row_start + 1));
                    } else {
                        String cellValue1 = getCellValue(row.getCell(row_start + 1));
                        String cellValue2 = getCellValue(row.getCell(row_start + 2));
                        String cellValue3 = getCellValue(row.getCell(row_start + 3));
                        String cellValue4 = getCellValue(row.getCell(row_start + 4));
                        String alarmIndex = AlarmInfoExt.generateAlarmIndex(alarmType + "", cellValue1);
                        if (StringUtils.isEmpty(alarmIndex)) {
                            continue;
                        }
                        for (AlarmInfoExt info : alarmList) {
                            if (alarmIndex.equals(info.getAlarmId())) {
                                info.setAlarmSourceIndex(alarmIfIndex);
                                info.setAlarmCategory(AlarmInfoExt.getAlarmCategory(cellValue2));
                                info.setAlarmSeverity(AlarmInfoExt.getAlarmSeverity(cellValue3));
                                info.setAlarmServAffect(AlarmInfoExt.getAlarmServAffect(cellValue4));
                            }
                        }
                    }

                }
            }

        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }
}
