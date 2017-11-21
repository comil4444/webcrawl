package erictool.webcrawl.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CNVDWebContent {
	
	public enum Category{
		WEB_APPLICATION("WEB应用",1),SECURITY_PRODUCT("安全产品",2),APPLICATION("应用软件",3),
		OPERATION_SYSTEM("操作系统",4),DATABASE("数据库",5),WEB_EQUIPMENT("网络设备",6);
		
		private String name;
		private int code;
		
		Category(String name,int code){
			this.name = name;
			this.code = code;
		}
		
		public String getName() {
			return this.getName();
		}
	}
	
	public final static List<String> COLUMN_NAME = new ArrayList<String>() {
		{
			this.add("序号");
			this.add("日期");
			this.add("情报内容");
			this.add("情报来源");
			this.add("CNVD ID");
			this.add("CVE ID");
			this.add("内容");
			this.add("漏洞类别");
			this.add("影响产品");
			this.add("影响版本");
			this.add("厂商尚未提供漏洞修复方案，请关注主页及时更新");
		}
	};

	private Date reportDate;
	private String title;
	private String fromSource;
	private String cnvdID;
	private String cveID;
	private String content;
	private Category category;
	private String affectedProduct;
	private String affectedProductVersion;
	private String recommandSolution;
	
	
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFromSource() {
		return fromSource;
	}
	public void setFromSource(String fromSource) {
		this.fromSource = fromSource;
	}
	public String getCnvdID() {
		return cnvdID;
	}
	public void setCnvdID(String cnvdID) {
		this.cnvdID = cnvdID;
	}
	public String getCveID() {
		return cveID;
	}
	public void setCveID(String cveID) {
		this.cveID = cveID;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getAffectedProduct() {
		return affectedProduct;
	}
	public void setAffectedProduct(String affectedProduct) {
		this.affectedProduct = affectedProduct;
	}
	public String getAffectedProductVersion() {
		return affectedProductVersion;
	}
	public void setAffectedProductVersion(String affectedProductVersion) {
		this.affectedProductVersion = affectedProductVersion;
	}
	public String getRecommandSolution() {
		return recommandSolution;
	}
	public void setRecommandSolution(String recommandSolution) {
		this.recommandSolution = recommandSolution;
	}
	
}


