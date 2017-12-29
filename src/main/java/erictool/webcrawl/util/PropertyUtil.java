package erictool.webcrawl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

public class PropertyUtil {
	
	private static String fileName = "config.properties";
	static Properties prop = new Properties();
	static {
		try {
			prop.load(new FileInputStream(getConfigFilePath()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getProperties(String key) {
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
		try {
			OutputStream out = new FileOutputStream(getConfigFilePath());
			prop.setProperty(key, value);
			System.out.println(key+":"+value);
			prop.store(out, "update by Eric");
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
			path = new File("").getAbsolutePath()+"/";
			path += fileName;
			System.out.println(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}
	
	public static void main(String[] args) {
		System.out.println(getConfigFilePath());
	}

}
