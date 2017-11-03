package erictool.webcrawl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class UIMain {
	
	public static int JTEXT_WIDTH = 30;
	
	public static void main(String[] args) {
		JFrame jf = new JFrame("爬蟲程序");
		
		JPanel chromePanel = new JPanel(new BorderLayout());  
		final JTextField chromePath = new JTextField();
		chromePath.setEditable(false);
		chromePath.setColumns(JTEXT_WIDTH);
		JButton chromeButton = new JButton("选择ChromeDriver文件：");
		chromeButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JFileChooser jf = new JFileChooser();  
				jf.setFileSelectionMode(JFileChooser.FILES_ONLY) ;  
				jf.showDialog(null,null); 
				if(jf.getSelectedFile()!=null) {
					File f = jf.getSelectedFile();
					if(!f.isFile()) {
						JOptionPane.showMessageDialog(null, "请选择Chrome驱动文件！");
						return;
					}
					
					chromePath.setText(jf.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		chromePanel.add(chromeButton,BorderLayout.WEST);
		chromePanel.add(chromePath,BorderLayout.EAST);
		
		
		JPanel filePanel = new JPanel(new BorderLayout());  
		final JTextField filePath = new JTextField();
		filePath.setEditable(false);
		filePath.setColumns(JTEXT_WIDTH);
		JButton filePathButton = new JButton("請選擇保存文件路徑：");
		filePathButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JFileChooser jf = new JFileChooser();  
				jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  
				jf.showDialog(null,null);  
				if(jf.getSelectedFile()!=null) {
					File f = jf.getSelectedFile();
					if(!f.isDirectory()) {
						JOptionPane.showMessageDialog(null, "请输入一个文件夹！");
						return;
					}
					filePath.setText(jf.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		filePanel.add(filePathButton,BorderLayout.WEST);
		filePanel.add(filePath,BorderLayout.EAST);
		jf.add(filePanel,BorderLayout.NORTH);
		jf.add(chromePanel,BorderLayout.CENTER);
		
		JButton confirmButton = new JButton("开始爬取！");
		confirmButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(StringUtils.isBlank(chromePath.getText())||StringUtils.isBlank(filePath.getText())) {
					JOptionPane.showMessageDialog(null, "路径有误！");
					return;
				}
				webcrawl(new NationalSecurtyWebSite());
			}
		});
		jf.add(confirmButton,BorderLayout.SOUTH);
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});
		jf.setLocation(300, 300);
		jf.setSize(700, 100);
		jf.setVisible(true);
		jf.setResizable(false);
	}

	protected static void webcrawl(NationalSecurtyWebSite nationalSecurtyWebSite) {
		List<String> urls = nationalSecurtyWebSite.getListWebSites(NationalSecurtyWebSite.LIST_URL);
		for(String url:urls) {
			Map<String,Map<String,String>> data = nationalSecurtyWebSite.getWebContent(NationalSecurtyWebSite.BASE_URL+url);
			boolean isBlank = checkData(data);
			if(isBlank) {
				try {
					Thread.sleep(100000l);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
			for(Entry<String,Map<String,String>> entry:data.entrySet()) {
				String title = entry.getKey();
				Map<String,String> content = entry.getValue();
				File file = ExcelUtil.write2Excel(NationalSecurtyWebSite.TARGET_FILE_DIR, title, content);
				System.out.println(file.getName());
			}
		}
		nationalSecurtyWebSite.cd.close();
		System.out.println("success!");
	}

	private static boolean checkData(Map<String, Map<String, String>> data) {
		for(Entry<String,Map<String,String>> entry:data.entrySet()) {
			if(StringUtils.isEmpty(entry.getKey())||entry.getValue().size()==0)
				return true;
		}
		return false;
	}
}
