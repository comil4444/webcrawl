package erictool.webcrawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlParser {
	
	public static String COOKIE = "";

	public static String getHtmlContent(URL url, String encode) throws InterruptedException {
		StringBuffer contentBuffer = new StringBuffer();
		
		
		int responseCode = -1;
		HttpURLConnection con = null;
		
		InputStream inStr = null;
		GZIPInputStream ungzip = null;
		try {
			con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");// IE代理进行下载
			con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
			con.setRequestProperty("Accept-Encoding", "gzip, deflate");
			con.setRequestProperty("Cookie", COOKIE);
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

			inStr = con.getInputStream();
			
			Thread.currentThread().sleep(1000l);
						
			ungzip = new GZIPInputStream(inStr,8192);  
			InputStreamReader istreamReader = new InputStreamReader(ungzip, encode);
			BufferedReader buffStr = new BufferedReader(istreamReader);

			String str = null;
			while ((str = buffStr.readLine()) != null)
				contentBuffer.append(str);
		} catch (IOException e) {
			e.printStackTrace();
			contentBuffer = null;
			System.out.println("error: " + url.toString());
		} finally {
			try {
				ungzip.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				inStr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	public static Document getHtmlContent(String url) {
		Document doc = null;
		if (!url.toLowerCase().startsWith("http://")) {
			url = "http://" + url;
		}
		try {
			URL rUrl = new URL(url);
			String content =  getHtmlContent(rUrl, "utf-8");
			return Jsoup.parse(content);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String argsp[]){
		System.out.println(getHtmlContent("www.cnvd.org.cn/flaw/list.htm?flag=true")) ;
		//getElementsById("www.cnvd.org.cn/flaw/list.htm?flag=true","concern");
	}
}