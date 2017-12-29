package erictool.webcrawl.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.chrome.ChromeDriver;

import erictool.webcrawl.model.CNVDWebContent;
import erictool.webcrawl.model.CNVDWebContent.Category;
import erictool.webcrawl.service.HtmlParser;

public class NationalSecurtyWebSite implements HtmlParser {

	public final static String SYSTEM = "CNVD";
	public final static String BASE_URL = "http://www.cnvd.org.cn";
	public final static String LIST_URL = "http://www.cnvd.org.cn/flaw/list.htm";
	public final static String CONTENT_URL_PREFIX = "/flaw/show/";
	public final static String NEXT_PAGE_URL = "http://www.cnvd.org.cn/flaw/list.htm?max=20&offset=";

	public static String TARGET_FILE_DIR="";
	public static String CHROME_DRIVER_PATH="";
	public static String PAGE_POST = "&max=20&offset=";
	public static int PAGESIZE = 20;
	
	public final static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Set<String> urlList = new HashSet<String>();

	public static ChromeDriver cd = null;

	public NationalSecurtyWebSite() {
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
		if(this.cd==null)
			this.cd = new ChromeDriver();
	}

	public List<String> getListWebSites(String url) {
		cd.get(url);
		cd.get(url);

		String content = cd.getPageSource();
		Document doc = Jsoup.parse(content);
		
		//放爬虫措施
		while(StringUtils.isBlank(doc.getElementsByClass("tlist").toString())) {
			try {
				Thread.sleep(300000l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cd.get(url);
			content = cd.getPageSource();
			doc = Jsoup.parse(content);
		}
		
		List<String> list = new ArrayList<String>();
		
		Element table = doc.getElementsByClass("tlist").get(0);
		
		List<Element> elements = table.getElementsByTag("a");
		for (Element e : elements) {
			if (e.attr("href").startsWith("/flaw/show/")) {
				list.add(BASE_URL+e.attr("href"));
			}
		}
		return list;
	}

	public Map<String, Map<String, String>> getWebContent(String url) {

		Map<String, String> target = new HashMap<String, String>();
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		
		cd.get(url);

		String content = cd.getPageSource();
		Document doc = Jsoup.parse(content);

		String title = doc.getElementsByTag("h1").text();

		Elements trs = doc.getElementsByTag("tr");
		for (int i = 0; i < trs.size() - 2; i++) {
			String left = "";
			String right = "";
			for (Element td : trs.get(i).children()) {
				if (td.attr("colspan").equals("2"))
					break;
				if (td.siblingIndex() % 3 == 1)
					left = td.text();
				else
					right = td.text();
			}
			if (left != "")
				target.put(left, right);
		}
		map.put(title, target);
		return map;
	}

	/*
	 * 获取每个漏洞Category的具体内容
	 */
	public void getCNVDWebCategoryContent(String url,int offset,Category category, Date deadline,List<CNVDWebContent> list) {
		List<String> categoryUrls = getListWebSites(url+offset);
		
		for(String contentUrl:categoryUrls) {
			CNVDWebContent content = getCNVDWebContent(contentUrl,category);
			try {
				if(deadline.after(SDF.parse(content.getReportDate())))
					return;
				list.add(content);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//递归下一页
		getCNVDWebCategoryContent(url,offset+=20,category,deadline,list);
	}
	
	/*
	 * 获取单个网页的内容
	 */
	public CNVDWebContent getCNVDWebContent(String url,Category category){
		CNVDWebContent content = new CNVDWebContent();
		content.setCategory(category);
		content.setFromSource(SYSTEM);
		
		Map<String,Map<String,String>> contentMap = getWebContent(url);
		
		boolean isBlank = checkData(contentMap);
		
		//防爬虫措施
		while(isBlank) {
			try {
				Thread.sleep(300000l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			contentMap = getWebContent(url);
			isBlank = checkData(contentMap);
		}
		
		for(Entry<String,Map<String,String>> entry :contentMap.entrySet()) {
			String title = entry.getKey();
			//设置标题
			content.setTitle(title);
			Map<String,String> temp = entry.getValue();
			for(Entry<String,String> entry2:temp.entrySet()) {
				String key = entry2.getKey();
				String value = entry2.getValue();
				if(key.equals("发布时间"))
					content.setReportDate(value);
				else if(key.equals("CNVD-ID"))
					content.setCnvdID(value);
				else if(key.equals("CVE ID"))
					content.setCveID(value);
				else if(key.equals("影响产品"))
					content.setAffectedProduct(value);
				else if(key.equals("漏洞描述"))
					content.setContent(value);
				else if(key.equals("漏洞解决方案"))
					content.setRecommandSolution(value);
				else if(key.equals("危害级别"))
					content.setThreatLevel(value.substring(0, value.indexOf("(")));
			}
		}
		return content;
	}
	
	/*
	 * 获取漏洞所有内容
	 */
	public List<CNVDWebContent> getCNVDWebContent(int offset,Date deadline){
		List<CNVDWebContent> list = new ArrayList<CNVDWebContent>();
		
		getCNVDWebCategoryContent(Category.WEB_APPLICATION.getUrl()+PAGE_POST,offset,Category.WEB_APPLICATION,deadline,list);//web应用漏洞
		getCNVDWebCategoryContent(Category.SECURITY_PRODUCT.getUrl()+PAGE_POST,offset,Category.SECURITY_PRODUCT,deadline,list);//安全产品信息漏洞
		getCNVDWebCategoryContent(Category.APPLICATION.getUrl()+PAGE_POST,offset,Category.APPLICATION,deadline,list);//应用程序漏洞
		getCNVDWebCategoryContent(Category.OPERATION_SYSTEM.getUrl()+PAGE_POST,offset,Category.OPERATION_SYSTEM,deadline,list);//安全系统漏洞
		getCNVDWebCategoryContent(Category.DATABASE.getUrl()+PAGE_POST,offset,Category.DATABASE,deadline,list);//数据库漏洞
		getCNVDWebCategoryContent(Category.WEB_EQUIPMENT.getUrl()+PAGE_POST,offset,Category.WEB_EQUIPMENT,deadline,list);//网络设备漏洞
		
		return list;
	}
	
	private static boolean checkData(Map<String, Map<String, String>> data) {
		for (Entry<String, Map<String, String>> entry : data.entrySet()) {
			if (StringUtils.isEmpty(entry.getKey()) || entry.getValue().size() == 0)
				return true;
		}
		return false;
	}
}
