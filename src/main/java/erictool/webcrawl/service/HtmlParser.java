package erictool.webcrawl.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import erictool.webcrawl.model.CNVDWebContent;

public interface HtmlParser {
	int DEFAULT_DAY = 1;
	
	String TARGET_FILE_DIR="TARGET_FILE_DIR";
	String CHROME_DRIVER_PATH="CHROME_DRIVER_PATH";
	
	List<String> getListWebSites(String url);
	
	Map<String,Map<String,String>> getWebContent(String url);
	
	List<CNVDWebContent> getCNVDWebContent(int offset,Date deadline);
	
}