package erictool.webcrawl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "/Users/i323360/Downloads/chromedriver");
		ChromeDriver cd = new ChromeDriver();
		cd.get("http://www.cnvd.org.cn/flaw/list");
		cd.get("http://www.cnvd.org.cn/flaw/list");

		String content = cd.getPageSource();

		Document doc = Jsoup.parse(content);
		
		System.out.println(content);

		List<String> list = new ArrayList<String>();

		List<Element> elements = doc.getElementsByTag("a");
		for (Element e : elements) {
			if (e.attr("href").startsWith("/flaw/show/")) {
				list.add(e.attr("href"));
			}
		}
		System.out.println(list);

		cd.get("http://www.cnvd.org.cn" + list.get(0));
		doc = Jsoup.parse(cd.getPageSource());
		
		Map<String,String> target = new HashMap<String,String>();
		Map<String,Map<String,String>> map = new HashMap<String,Map<String,String>>();

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
		
		for(Entry<String, Map<String, String>> entry:map.entrySet())
			ExcelUtil.write2Excel("/Users/i323360/Desktop/webcrawl", entry.getKey(),entry.getValue() );
		cd.close();
	}
}
