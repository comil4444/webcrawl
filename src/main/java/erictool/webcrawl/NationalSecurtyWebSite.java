package erictool.webcrawl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NationalSecurtyWebSite {
	
	public static String BASE_URL = "www.cnvd.org.cn";
	public static String LIST_URL = "www.cnvd.org.cn/flaw/list.htm";
	public static String CONTENT_URL_PREFIX = "/flaw/show/";
	
	public List<String> getPageContentUrl(Document doc,String prefix){
		List<String> list = new ArrayList<String>();
		
		List<Element> elements = doc.getElementsByTag("a");
		for(Element e:elements) {
			if(e.attr("href").startsWith(prefix)) {
				list.add(e.attr("href"));
			}
		}
		
		return list;
	}
	
	public Map<String,Map<String,String>> getListContent(String url){
		Map<String,Map<String,String>> map = new HashMap<String,Map<String,String>>();
		Map<String,String> content = new HashMap<String,String>();
		
		Document doc = HtmlParser.getHtmlContent(url);
		
		String title = doc.getElementsByTag("h1").text();
		
		Elements trs = doc.getElementsByTag("tr");
		for(int i=0;i<trs.size()-2;i++) {
			String left = "";
			String right = "";
			for(Element td:trs.get(i).children()) {
				if(td.attr("colspan").equals("2"))
					break;
				if(td.siblingIndex()%3==1)
					left = td.text();
				else
					right = td.text();
			}
			if(left !="")
				content.put(left, right);
		}
		map.put(title, content);
		return map;
	}

	public static void main(String[] args) {
		Document d = HtmlParser.getHtmlContent("www.cnvd.org.cn/flaw/list.htm");
		NationalSecurtyWebSite webSite = new NationalSecurtyWebSite();
		List<String> urls = webSite.getPageContentUrl(d, CONTENT_URL_PREFIX);
		for(String url:urls) {
			Map<String,Map<String,String>> temp = webSite.getListContent(BASE_URL+url);
			System.out.println(temp);
		}

	}

}
