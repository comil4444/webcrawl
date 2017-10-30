package erictool.webcrawl;

import java.util.Set;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.util.Cookie;

import javax.script.ScriptEngine; 
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Test {

	public static void main(String[] args) {
        HtmlUnitDriver driver = new HtmlUnitDriver();
        //必须设置为true,才能执行js代码
        driver.setJavascriptEnabled(true);
        driver.get("http://www.cnvd.org.cn/");
        Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
        for (org.openqa.selenium.Cookie cookie:cookies){
            System.out.println(cookie);
        }
        driver.setJavascriptEnabled(false);
        driver.get("http://www.cnvd.org.cn/flaw/show/CNVD-2017-30496");
        String pageSource = driver.getPageSource();
        System.out.println(pageSource);
        
        String js=pageSource.trim().replace("<script>", "").replace("</script>", "").replace("eval(y.replace(/\\b\\w+\\b/g, function(y){return x[f(y,z)-1]}))","y.replace(/\\b\\w+\\b/g, function(y){return x[f(y,z)-1]})");
		
        System.out.println(js);
        
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        String target = null;
        try {
        		target = (String) engine.eval(js);
        }catch(ScriptException e) {
        		e.printStackTrace();
        }
        System.out.println(target);
        
        
    }

}
