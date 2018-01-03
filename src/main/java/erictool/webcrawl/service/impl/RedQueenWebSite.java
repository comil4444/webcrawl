package erictool.webcrawl.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import erictool.webcrawl.model.CNVDWebContent;
import erictool.webcrawl.service.HtmlParser;

public class RedQueenWebSite extends AbstractHtmlParser {
	
	public static String CONFIGDAY = "configRedQueenDay";
	public static String CONFIGCHECKED = "configRedQueenchecked";
	public static String CONFIGUSERNAME = "configRedQueenUserName";
	public static String CONFIGPASSWORD = "configRedQueenPassword";

	public List<String> getListWebSites(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Map<String, String>> getWebContent(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<CNVDWebContent> getCNVDWebContent(int offset, Date deadline) {
		// TODO Auto-generated method stub
		return null;
	}

}
