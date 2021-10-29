package com.dosth.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description Excel工具类
 * @author guozhidong
 *
 */
public class ExcelUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
	
	/**
	 * @description 获取工作簿
	 * @param path 解析文件路径
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static HSSFWorkbook getWorkbook(String path) throws IOException {
		logger.info("打开excel文件,路径" + path);
		return getWorkbook(new FileInputStream(path));
	}
	
	/**
	 * @description 获取工作簿
	 * @param stream 解析文件路径
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static HSSFWorkbook getWorkbook(InputStream stream) throws IOException {
		return new HSSFWorkbook(stream);
	}
	
	/**
	 * @description 获取列表标题
	 * @param workbook 工作区
	 * @param sheetIndex sheet索引
	 * @param rowNo 行号
	 * @return
	 * @throws Exception
	 */
	public static List<String> readExcelTitle(HSSFWorkbook workbook, int sheetIndex, int rowNo) throws Exception {
		if (workbook == null) {
			throw new Exception("Workbook对象为空！");
		}
		HSSFSheet sheet = getSheet(workbook, sheetIndex);
		HSSFRow row = getRow(sheet, rowNo);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		List<String> titles = new ArrayList<>();
		for (int i = 0; i < colNum; i++) {
			titles.add(getCellValue(getCell(row, i)));
		}
		return titles;
	}
	
	/**
	 * @description 获取工作区
	 * @param workbook 工作簿
	 * @param sheetIndex sheet索引
	 * @return
	 */
	public static HSSFSheet getSheet(HSSFWorkbook workbook, int sheetIndex) {
		return workbook.getSheetAt(sheetIndex);
	}
	
	/**
	 * @description 获取行
	 * @param sheet 工作区
	 * @param rowNo 行号
	 * @return
	 */
	public static HSSFRow getRow(HSSFSheet sheet, int rowNo) {
		return sheet.getRow(rowNo);
	}
	
	/**
	 * @description 获取单元格
	 * @param row 行对象
	 * @param cellIndex 单元格索引
	 * @return
	 */
	public static HSSFCell getCell(HSSFRow row, int cellIndex) {
		return row.getCell(cellIndex);
	}
	
	/**
	 * @description 获取单元格数据
	 * @param hssfCell 指定单元格
	 * @return
	 */
	public static String getCellValue(HSSFCell hssfCell) {
		if (hssfCell == null) {
			return null;
		}
		String result = null;
		switch (hssfCell.getCellType()) {
		case BOOLEAN:
			result = String.valueOf(hssfCell.getBooleanCellValue());
			break;
		case NUMERIC:
			double cur = hssfCell.getNumericCellValue();
			long longVal = Math.round(cur);
			Object inputValue = null;
			if (Double.parseDouble(longVal + ".0") == cur) {
				inputValue = longVal;
			} else {
				inputValue = cur;
			}
			result = String.valueOf(inputValue);
			break;
		case BLANK:
		case ERROR:
			result = null;
			break;
		default:
			result = String.valueOf(hssfCell.getStringCellValue());
			break;
		}
		return result;
	}
}