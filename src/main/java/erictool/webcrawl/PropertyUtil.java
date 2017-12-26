package erictool.webcrawl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Properties;

public class PropertyUtil {
	
	private static String fileName = "config.properties";
	
	public static String getProperties(String key) {
		Properties prop = new Properties();
		try {
			InputStream in = new FileInputStream(getConfigFilePath());
			prop.load(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prop.getProperty(key);
	}
	
	public static void setProperties(String key,String value) {
		Properties prop = new Properties();
		try {
			OutputStream out = new FileOutputStream(getConfigFilePath());
			prop.setProperty(key, value);
			prop.store(out, "update"+new Date());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getConfigFilePath() {
		String path = "";
		try {
			path = URLDecoder.decode(PropertyUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
			path += fileName;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}
	
	public static void main(String[] args) {
		System.out.println(getConfigFilePath());
	}

}
