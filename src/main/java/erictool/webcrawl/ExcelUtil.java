package erictool.webcrawl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

public class ExcelUtil {

	private final static String EXCEL_POSTFIX = ".xls";

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
			
			for(Entry<String, String> entry:data.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				Row row = sheet.createRow(rowIndex++);
				
				row.createCell(0,CellType.STRING).setCellValue(key);
				row.createCell(1,CellType.STRING).setCellValue(value.toString());
				
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
	
	public static void main(String[] args) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("asdf", "adf");
		map.put("asdf2", "adf");
		map.put("asdf3", "adf");
		write2Excel("/Users/i323360/Desktop/webcrawl","test2",map);
	}
}
