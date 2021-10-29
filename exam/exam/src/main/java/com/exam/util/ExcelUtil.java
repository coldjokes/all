package com.exam.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;

/**
 * excel导出工具类
 * 
 * @author Yifeng Wang  
 */
public class ExcelUtil {

    private final int SPLIT_COUNT = 65536; //Excel每个工作簿的行数

    private ArrayList<String> fieldName = null; //excel标题数据集

    private Map<String, List<String>> fieldNames = null; //多个 excel标题数据集

    private ArrayList<ArrayList<String>> fieldData = null; //excel数据内容

    private Map<String, List<List<String>>> fieldDataMap = null; //excel数据内容

    private HSSFWorkbook workBook = null;

    /**
     * 构造器
     *
     * @param fieldName 结果集的字段名
     * @param fieldData
     */
    public ExcelUtil(ArrayList<String> fieldName, ArrayList<ArrayList<String>> fieldData) {

        this.fieldName = fieldName;
        this.fieldData = fieldData;
    }

    public ExcelUtil(Map<String, List<String>> fieldNames, Map<String, List<List<String>>> fieldDatas) {

        this.fieldNames = fieldNames;
        this.fieldDataMap = fieldDatas;
    }

    /**
     * 创建HSSFWorkbook对象
     *
     * @return HSSFWorkbook
     */
    public HSSFWorkbook createWorkbook() {

        workBook = new HSSFWorkbook(); //创建一个工作薄对象
        int rows = fieldData.size(); //总的记录数
        int sheetNum = 0;           //指定sheet的页数

        if (rows % SPLIT_COUNT == 0) {
            sheetNum = rows / SPLIT_COUNT;
        } else {
            sheetNum = rows / SPLIT_COUNT + 1;
        }

        for (int i = 1; i <= sheetNum; i++) { //循环2个sheet的值
            HSSFSheet sheet = workBook.createSheet("Page " + i); //使用workbook对象创建sheet对象
            HSSFRow headRow = sheet.createRow((short) 0); //创建行，0表示第一行（本例是excel的标题）
            for (int j = 0; j < fieldName.size(); j++) { //循环excel的标题
                HSSFCell cell = headRow.createCell(j); //使用行对象创建列对象，0表示第1列
                /**************对标题添加样式begin********************/
                //设置列的宽度/
                sheet.setColumnWidth(j, 6000);
                HSSFCellStyle cellStyle = workBook.createCellStyle();//创建列的样式对象
                HSSFFont font = workBook.createFont();//创建字体对象
                //字体加粗
                font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                //字体颜色变黑
                font.setColor(HSSFColor.BLACK.index);
                //如果font中存在设置后的字体，并放置到cellStyle对象中，此时该单元格中就具有了样式字体
                cellStyle.setFont(font);
                /**************对标题添加样式end********************/

                //添加样式
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                if (fieldName.get(j) != null) {
                    //将创建好的样式放置到对应的单元格中
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue((String) fieldName.get(j)); //为标题中的单元格设置值
                } else {
                    cell.setCellValue("-");
                }
            }
            //分页处理excel的数据，遍历所有的结果
            for (int k = 0; k < (rows < SPLIT_COUNT ? rows : SPLIT_COUNT); k++) {
                if (((i - 1) * SPLIT_COUNT + k) >= rows)//如果数据超出总的记录数的时候，就退出循环
                    break;
                HSSFRow row = sheet.createRow((short) (k + 1)); //创建1行
                //分页处理，获取每页的结果集，并将数据内容放入excel单元格
                ArrayList<String> rowList = (ArrayList<String>) fieldData.get((i - 1) * SPLIT_COUNT + k);
                for (int n = 0; n < rowList.size(); n++) { //遍历某一行的结果
                    HSSFCell cell = row.createCell(n); //使用行创建列对象
                    if (rowList.get(n) != null) {
                        cell.setCellValue((String) rowList.get(n).toString());
                    } else {
                        cell.setCellValue("");
                    }
                }
            }
        }
        return workBook;
    }

    /**
     * 创建HSSFWorkbook对象
     *
     * @return HSSFWorkbook
     */
    public HSSFWorkbook createWorkbook(List<String> sheets) {

        workBook = new HSSFWorkbook(); //创建一个工作薄对象

        for (String sheetName : sheets) { //循环sheet
            int rows = fieldDataMap.get(sheetName).size(); //每个sheet总的记录数
            HSSFSheet sheet = workBook.createSheet(sheetName); //使用workbook对象创建sheet对象
            HSSFRow headRow = sheet.createRow((short) 0); //创建行，0表示第一行（本例是excel的标题）
            for (int j = 0; j < fieldNames.get(sheetName).size(); j++) { //循环excel的标题
                HSSFCell cell = headRow.createCell(j); //使用行对象创建列对象，0表示第1列
                /**************对标题添加样式begin********************/
                //设置列的宽度/
                sheet.setColumnWidth(j, 6000);
                HSSFCellStyle cellStyle = workBook.createCellStyle();//创建列的样式对象
                HSSFFont font = workBook.createFont();//创建字体对象
                //字体加粗
                font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                //字体颜色变黑
                font.setColor(HSSFColor.BLACK.index);
                //如果font中存在设置后的字体，并放置到cellStyle对象中，此时该单元格中就具有了样式字体
                cellStyle.setFont(font);
                /**************对标题添加样式end********************/

                //添加样式
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                if (fieldNames.get(sheetName).get(j) != null) {
                    //将创建好的样式放置到对应的单元格中
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue((String) fieldNames.get(sheetName).get(j)); //为标题中的单元格设置值
                } else {
                    cell.setCellValue("-");
                }
            }
            //分页处理excel的数据，遍历所有的结果
            for (int k = 0; k < (rows < SPLIT_COUNT ? rows : SPLIT_COUNT); k++) {
//                if (((i - 1) * SPLIT_COUNT + k) >= rows)//如果数据超出总的记录数的时候，就退出循环
//                    break;
                HSSFRow row = sheet.createRow((short) (k + 1)); //创建1行
                //分页处理，获取每页的结果集，并将数据内容放入excel单元格
                ArrayList<String> rowList = (ArrayList<String>) fieldDataMap.get(sheetName).get(k);
                for (int n = 0; n < rowList.size(); n++) { //遍历某一行的结果
                    HSSFCell cell = row.createCell(n); //使用行创建列对象
                    if (rowList.get(n) != null) {
                        cell.setCellValue((String) rowList.get(n).toString());
                    } else {
                        cell.setCellValue("");
                    }
                }
            }
        }
        return workBook;
    }

    public void expordExcel(OutputStream os) throws Exception {
//        workBook = createWorkbook();
        workBook.write(os); //将excel中的数据写到输出流中，用于文件的输出
        os.close();
    }

	/**
	 * 输出名称格式化
	 * @param request
	 * @param fileName
	 * @return
	 */
	public static String processFileName(HttpServletRequest request, String fileName) {
        String codedFileName = null;
        try {
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && agent.contains("MSIE") || null != agent
                    && agent.contains("Trident")) { // ie
                codedFileName = java.net.URLEncoder.encode(fileName, "UTF8");
            } else if (null != agent && agent.contains("Mozilla")) { // 火狐,chrome等
                codedFileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codedFileName;
    }
	
	/**
	 * 解析excel
	 * @param file
	 * @return
	 */
	public static List<List<Object>> executeExcel(MultipartFile file){
		try {
			return executeExcel(file.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解析excel
	 * @param file
	 * @return
	 */
	public static List<List<Object>> executeExcel(InputStream inputStream){
		List<List<Object> > rowList = Lists.newArrayList();
		if(inputStream != null) {
			Workbook wb = null;
			try {
				wb = new HSSFWorkbook(inputStream);
				Sheet sheet = wb.getSheetAt(0);
				
				//通过第一行标题长度获取需要传的字段数
				int rowSize = sheet.getRow(0).getLastCellNum();
				
				boolean isFirstRow = true;
				for (Row row : sheet) {
					
					//跳过第一行标题
					if(isFirstRow) {
						isFirstRow = false;
						continue;
					}
					
					List<Object> cellList = Lists.newArrayList();
					
					int emptyCellSize = 0;
					
					for (int i = 0; i < rowSize; i ++) {
						Cell cell = row.getCell(i);
						Object cellValue = getExcelCellValue(cell);
						
						//记录字段为空的cell
						if(cellValue == null) {
							emptyCellSize ++;
						}
						cellList.add(cellValue);
					}
					
					//如果空的cell个数等于所有字段数，则判断此行为空行，不添加到结果中
					if(emptyCellSize == rowSize) {
						continue;
					}
					rowList.add(cellList);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(wb != null) {
					try {
						wb.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return rowList;
	}
	
	private static Object getExcelCellValue(Cell cell) {
		Object obj = null;
		if(cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				obj = cell.getNumericCellValue();
				break;
			case Cell.CELL_TYPE_STRING:
				obj = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				obj = cell.getBooleanCellValue();
				break;
			case Cell.CELL_TYPE_ERROR:
				obj = cell.getErrorCellValue();
				break;
			default:
				break;
			}
		}
		return obj;
	}
}
