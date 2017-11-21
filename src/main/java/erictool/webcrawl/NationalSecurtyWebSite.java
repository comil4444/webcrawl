package erictool.webcrawl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.chrome.ChromeDriver;

import erictool.webcrawl.model.CNVDWebContent;

public class NationalSecurtyWebSite implements HtmlParser {

	public final static String BASE_URL = "http://www.cnvd.org.cn";
	public final static String LIST_URL = "http://www.cnvd.org.cn/flaw/list.htm";
	public final static String CONTENT_URL_PREFIX = "/flaw/show/";
	public final static String NEXT_PAGE_URL = "http://www.cnvd.org.cn/flaw/list.htm?max=20&offset=";

	public static String TARGET_FILE_DIR="/Users/i323360/Desktop/webcrawl";
	public static String CHROME_DRIVER_PATH="/Users/i323360/Downloads/chromedriver";
	public static String PAGE_POST = "?max=20&offset=";
	public static int PAGESIZE = 20;
	
	public static Set<String> urlList = new HashSet<String>();

	public ChromeDriver cd = null;

	public NationalSecurtyWebSite() {
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
		this.cd = new ChromeDriver();
	}

	public List<String> getListWebSites(String url) {
		cd.get(url);
		cd.get(url);

		String content = cd.getPageSource();
		Document doc = Jsoup.parse(content);

		List<String> list = new ArrayList<String>();
		
		Element table = doc.getElementsByClass("tlist").get(0);
		
		List<Element> elements = table.getElementsByTag("a");
		for (Element e : elements) {
			if (e.attr("href").startsWith("/flaw/show/")) {
				list.add(e.attr("href"));
			}
		}
		System.out.println(list);
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
		System.out.println(map);
		return map;
	}

	public List<CNVDWebContent> getCNVDWebContent(String url) {
		return null;
	}

}
