package erictool.webcrawl.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import erictool.webcrawl.model.CNVDWebContent;
import erictool.webcrawl.service.HtmlParser;
import erictool.webcrawl.util.ChaoJiYing;
import erictool.webcrawl.util.PropertyUtil;

public class RedQueenWebSite extends AbstractHtmlParser {

	public static String CONFIGDAY = "configRedQueenDay";
	public static String CONFIGCHECKED = "configRedQueenchecked";
	public static String CONFIGUSERNAME = "configRedQueenUserName";
	public static String CONFIGPASSWORD = "configRedQueenPassword";
	
	private static String VALIDATIONCODE_USERNAME = "validationCodeUserName";
	private static String VALIDATIONCODE_PASSWORD = "validationCodePassword";

	public final static String SYSTEM = "REDQUEEN";

	public final static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	public static ChromeDriver cd = null;
	
	public RedQueenWebSite() {
		System.setProperty("webdriver.chrome.driver", PropertyUtil.getProperties(HtmlParser.CHROME_DRIVER_PATH));
		if(this.cd==null)
			this.cd = new ChromeDriver();
		login(PropertyUtil.getProperties(VALIDATIONCODE_USERNAME),PropertyUtil.getProperties(VALIDATIONCODE_PASSWORD));;
	}

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
	
	private void login(String userName,String userPwd) {
		cd.get("https://redqueen.sec-un.com/login.jsp");
		cd.manage().window().maximize();
		WebElement e = cd.findElementByClassName("rw-lg").findElement(By.className("jcaptcha-img"));

		Point p = e.getLocation();
		int width = e.getSize().getWidth();
		int height = e.getSize().getHeight();
		System.out.println(p.x + ":" + p.y + ":" + width + ":" + height);
		
		try {
			File img = ((TakesScreenshot) cd).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(img, new File("target.jpg"));
			BufferedImage buffer = ImageIO.read(img);
			
			BufferedImage subImage = buffer.getSubimage(p.x * 2, p.y * 2, width * 2, height * 2);
			ImageIO.write(subImage, "jpg", img);
			
			FileUtils.copyFile(img, new File("temp.png"));
			
			// login
			int length = (int)img.length();
			FileInputStream fis = new FileInputStream(img);
			byte[] data = new byte[length];
			fis.read(data, 0, length);
			cd.findElement(By.id("userName")).sendKeys("hxaqhb");
			cd.findElement(By.id("userPwd")).sendKeys("12345qwert");
			
			System.out.println("剩餘分數："+ChaoJiYing.GetScore("comil4444", "123456789"));
			String validationCodeStr = ChaoJiYing.PostPic("comil4444", "123456789", "e764b529a130c86a9d43cb71ef8d0f25", "1005", "0", data);
			//String validationCodeStr = "{\"err_no\":0,\"err_str\":\"OK\",\"pic_id\":\"6018621091283200001\",\"pic_str\":\"wagtk\",\"md5\":\"513a3bbf3da452319a64fe4ad0e64e91\"}";
			JsonParser parser = new JsonParser();
			JsonElement json = parser.parse(validationCodeStr);
			
			JsonObject root = json.getAsJsonObject();
			String validationCode = StringUtils.EMPTY;
			if(root.get("err_no") != null) {
				validationCode = root.get("pic_str").getAsString();
				System.out.println(validationCode);
			}
			
			System.out.println(validationCode);
			cd.findElement(By.id("captcha")).sendKeys(validationCode);
			
			cd.findElement(By.className("pr-log")).click();
		}catch(Exception exception) {
			this.cd.quit();
			System.out.println("error in red queen login");
		}
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("webdriver.chrome.driver", "/Users/i323360/Downloads/chromedriver");
		if (cd == null)
			cd = new ChromeDriver();
		cd.get("https://redqueen.sec-un.com/login.jsp");
		cd.manage().window().maximize();
		WebElement e = cd.findElementByClassName("rw-lg").findElement(By.className("jcaptcha-img"));

		Point p = e.getLocation();
		int width = e.getSize().getWidth();
		int height = e.getSize().getHeight();
		System.out.println(p.x + ":" + p.y + ":" + width + ":" + height);

		File img = ((TakesScreenshot) cd).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(img, new File("target.jpg"));
		BufferedImage buffer = ImageIO.read(img);

		BufferedImage subImage = buffer.getSubimage(p.x * 2, p.y * 2, width * 2, height * 2);
		ImageIO.write(subImage, "jpg", img);

		FileUtils.copyFile(img, new File("temp.png"));

		// login
		int length = (int)img.length();
		FileInputStream fis = new FileInputStream(img);
		byte[] data = new byte[length];
		fis.read(data, 0, length);
		cd.findElement(By.id("userName")).sendKeys("hxaqhb");
		cd.findElement(By.id("userPwd")).sendKeys("12345qwert");

		System.out.println("剩餘分數："+ChaoJiYing.GetScore("comil4444", "123456789"));
		String validationCodeStr = ChaoJiYing.PostPic("comil4444", "123456789", "e764b529a130c86a9d43cb71ef8d0f25", "1005", "0", data);
		//String validationCodeStr = "{\"err_no\":0,\"err_str\":\"OK\",\"pic_id\":\"6018621091283200001\",\"pic_str\":\"wagtk\",\"md5\":\"513a3bbf3da452319a64fe4ad0e64e91\"}";
		JsonParser parser = new JsonParser();
		JsonElement json = parser.parse(validationCodeStr);
		
		JsonObject root = json.getAsJsonObject();
		String validationCode = StringUtils.EMPTY;
		if(root.get("err_no") != null) {
			validationCode = root.get("pic_str").getAsString();
			System.out.println(validationCode);
		}
		
		System.out.println(validationCode);
		cd.findElement(By.id("captcha")).sendKeys(validationCode);
		
		cd.findElement(By.className("pr-log")).click();

	}

}
