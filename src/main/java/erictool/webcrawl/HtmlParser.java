package erictool.webcrawl;

import java.util.List;
import java.util.Map;

public interface HtmlParser {
	
	List<String> getListWebSites(String url);
	
	Map<String,Map<String,String>> getWebContent(String url);
	
}