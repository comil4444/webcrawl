package erictool.webcrawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {
	public static String getHtmlContent(URL url, String encode) {
		StringBuffer contentBuffer = new StringBuffer();

		int responseCode = -1;
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:55.0) Gecko/20100101 Firefox/55.0");// IE代理进行下载
			con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			con.setRequestProperty("Accept-Encoding", "gzip, deflate");
			con.setRequestProperty("Cookie", "__jsluid=e30f13702d541685d6049a7db901090a; __jsl_clearance=1509286637.093|0|VZuxbbXXcFOWX2q66I3%2BEvGcf2I%3D");
			con.setRequestProperty("Upgrade-Insecure-Requests", "1");
			con.setRequestProperty("Connection", "keep-alive");
			con.setConnectTimeout(60000);
			con.setReadTimeout(60000);
			// 获得网页返回信息码
			responseCode = con.getResponseCode();
			if (responseCode == -1) {
				System.out.println(url.toString() + " : connection is failure...");
				con.disconnect();
				return null;
			}
			if (responseCode >= 400) // 请求失败
			{
				System.out.println("请求失败:get response code: " + responseCode);
				con.disconnect();
				return null;
			}

			InputStream inStr = con.getInputStream();
			
			GZIPInputStream ungzip = new GZIPInputStream(inStr);  
			InputStreamReader istreamReader = new InputStreamReader(ungzip, encode);
			BufferedReader buffStr = new BufferedReader(istreamReader);

			String str = null;
			while ((str = buffStr.readLine()) != null)
				contentBuffer.append(str);
			inStr.close();
		} catch (IOException e) {
			e.printStackTrace();
			contentBuffer = null;
			System.out.println("error: " + url.toString());
		} finally {
			con.disconnect();
		}
		return contentBuffer.toString();
	}

	public static String getHtmlContent(String url, String encode) {
		if (!url.toLowerCase().startsWith("http://")) {
			url = "http://" + url;
		}
		try {
			URL rUrl = new URL(url);
			return getHtmlContent(rUrl, encode);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<String> getElementsById(String url){
		List<String> list = new ArrayList<String>();
		
		String contentStr = getHtmlContent(url,"utf-8");
		
		Document doc = Jsoup.parse(contentStr);
		Element e = doc.getElementById("concern");
		return list;
	}
	
	
	public static void main(String argsp[]){
		//System.out.println(getHtmlContent("www.cnvd.org.cn/flaw/list.htm?flag=true","utf-8")) ;
		getElementsById("www.cnvd.org.cn/flaw/list.htm?flag=true");
	}
}