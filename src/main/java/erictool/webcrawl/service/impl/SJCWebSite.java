package erictool.webcrawl.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import erictool.webcrawl.model.CNVDWebContent;
import erictool.webcrawl.service.HtmlParser;

public class SJCWebSite extends AbstractHtmlParser {

	public static String CONFIGDAY = "configSJCDay";
	public static String CONFIGCHECKED = "configSJCchecked";
	public static String CONFIGUSERNAME = "configSJCUserName";
	public static String CONFIGPASSWORD = "configSJCPassword";
	
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
