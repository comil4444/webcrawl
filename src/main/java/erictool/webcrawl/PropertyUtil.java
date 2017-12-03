package erictool.webcrawl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {
	
	private static Properties prop = new Properties();

	public static String getProp(String key) {
		try {
			prop.load(new FileInputStream("./resource/config.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prop.getProperty(key);
	}
	
	public static void setProp(String key,String value) {
		FileOutputStream oFile = null;
		try {
			oFile = new FileOutputStream("./resource/config.properties", true);
			//true表示追加打开
			prop.setProperty(key, value);
			prop.store(oFile, "The New properties file");
			oFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
