package erictool.webcrawl;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;

import erictool.webcrawl.model.CNVDWebContent;

public class ExcelUtil {

	private final static String EXCEL_POSTFIX = ".xls";
	private final static int COLUMN_NAME_INDEX = 0;

	public static File write2Excel(String path, String fileName, Map<String, String> data) {
		File file = new File(path, fileName + EXCEL_POSTFIX);
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("爬蟲數據");
			sheet.setColumnWidth(0, 20 * 256);
			sheet.setColumnWidth(1, 100 * 256);

			CellStyle cs = wb.createCellStyle();
			cs.setWrapText(true);

			int rowIndex = 0;

			for (Entry<String, String> entry : data.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				Row row = sheet.createRow(rowIndex++);

				row.createCell(0, CellType.STRING).setCellValue(key);
				row.createCell(1, CellType.STRING).setCellValue(value.toString());

				row.setRowStyle(cs);
			}
			wb.write(file);
			wb.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	public static File write2Excel(String path, String fileName, List<CNVDWebContent> datas,List<String> columnNames) {
		File file = new File(path, fileName + EXCEL_POSTFIX);
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet();
			
			createSheetHead(sheet,wb,columnNames);
			
			insertContent(datas,sheet);
			
			wb.write(file);
			wb.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	private static void insertContent(List<CNVDWebContent> datas, HSSFSheet sheet) {
		if(CollectionUtils.isEmpty(datas))
			return;
		int rowIndex = COLUMN_NAME_INDEX+1;
		for(CNVDWebContent content:datas) {
			int columnIndex = 0;
			Row row = sheet.createRow(rowIndex++);
			row.createCell(columnIndex++).setCellValue(columnIndex);
			row.createCell(columnIndex++).setCellValue(content.getReportDate());
			row.createCell(columnIndex++).setCellValue(content.getTitle());
			row.createCell(columnIndex++).setCellValue(content.getFromSource());
			row.createCell(columnIndex++).setCellValue(content.getCnvdID());
			row.createCell(columnIndex++).setCellValue(content.getCveID());
			row.createCell(columnIndex++).setCellValue(content.getContent());
			row.createCell(columnIndex++).setCellValue(content.getCategory().getName());
			row.createCell(columnIndex++).setCellValue(content.getAffectedProduct());
			row.createCell(columnIndex++).setCellValue(content.getAffectedProductVersion());
			row.createCell(columnIndex++).setCellValue(content.getRecommandSolution());
		}
	}

	private static void createSheetHead(HSSFSheet sheet, HSSFWorkbook wb, List<String> columnNames) {
		//set style
		CellStyle cs = wb.createCellStyle();
		HSSFFont font= wb.createFont();
		font.setBold(true);
		cs.setFont(font);
		cs.setWrapText(true);
		cs.setAlignment(HorizontalAlignment.LEFT);
		cs.setFillBackgroundColor(HSSFColor.AQUA.index);
		
		//fill in data
		Row columnRow = sheet.createRow(COLUMN_NAME_INDEX);
		int index = 0;
		for(String columnName:columnNames) {
			columnRow.createCell(index++, CellType.STRING).setCellValue(columnName);
		}
		columnRow.setRowStyle(cs);
	}
}
