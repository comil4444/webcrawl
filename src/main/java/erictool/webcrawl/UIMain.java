package erictool.webcrawl;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

public class UIMain {

	public static int JTEXT_WIDTH = 30;
	public static Date DATE_NOW = new Date();
	public static int DEFAULT_DAY = 7;

	public static void main(String[] args) {
		JFrame jf = new JFrame("爬蟲程序");
		GridLayout gy = new GridLayout(3, 1);
		jf.setLayout(gy);

		JPanel chromePanel = new JPanel(new BorderLayout());
		final JTextField chromePath = new JTextField();
		chromePath.setEditable(false);
		chromePath.setColumns(JTEXT_WIDTH);
		JButton chromeButton = new JButton("选择ChromeDriver文件：");
		chromeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser jf = new JFileChooser();
				jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jf.showDialog(null, null);
				if (jf.getSelectedFile() != null) {
					File f = jf.getSelectedFile();
					if (!f.isFile()) {
						JOptionPane.showMessageDialog(null, "请选择Chrome驱动文件！");
						return;
					}

					chromePath.setText(jf.getSelectedFile().getAbsolutePath());
					NationalSecurtyWebSite.CHROME_DRIVER_PATH = chromePath.getText();
				}
			}
		});

		chromePanel.add(chromeButton, BorderLayout.WEST);
		chromePanel.add(chromePath, BorderLayout.EAST);

		JPanel filePanel = new JPanel(new BorderLayout());
		final JTextField filePath = new JTextField();
		filePath.setEditable(false);
		filePath.setColumns(JTEXT_WIDTH);
		JButton filePathButton = new JButton("請選擇保存文件路徑：");
		filePathButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser jf = new JFileChooser();
				jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jf.showDialog(null, null);
				if (jf.getSelectedFile() != null) {
					File f = jf.getSelectedFile();
					if (!f.isDirectory()) {
						JOptionPane.showMessageDialog(null, "请输入一个文件夹！");
						return;
					}
					filePath.setText(jf.getSelectedFile().getAbsolutePath());
					NationalSecurtyWebSite.TARGET_FILE_DIR = filePath.getText();
				}
			}
		});
		filePanel.add(filePathButton, BorderLayout.WEST);
		filePanel.add(filePath, BorderLayout.EAST);

		JPanel day = new JPanel();
		JLabel jl = new JLabel("抓取从今往前多少天数据（默认7）：");
		final JTextField jtf = new JTextField();
		jtf.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				int keyChar = e.getKeyChar();
				if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) {

				} else {
					e.consume(); // 关键，屏蔽掉非法输入
				}
			}
		});
		jtf.setColumns(JTEXT_WIDTH / 4);
		day.add(jl, BorderLayout.WEST);
		day.add(jtf, BorderLayout.CENTER);

		JButton confirmButton = new JButton("开始爬取！");
		confirmButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (chromePath.getText() ==null || chromePath.getText().equals("") || filePath.getText()==null || filePath.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "路径有误！");
					return;
				}
				int day = (jtf.getText()==null || jtf.getText().equals("")) ? DEFAULT_DAY : Integer.valueOf(jtf.getText());
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DATE, -day);
				Date deadline = c.getTime();
				webcrawl(new NationalSecurtyWebSite(), deadline, 0);
			}

		});
		day.add(confirmButton, BorderLayout.EAST);

		jf.add(filePanel);
		jf.add(chromePanel);
		jf.add(day);

		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});
		jf.setLocation(300, 300);
		jf.setSize(700, 120);
		jf.setVisible(true);
		jf.setResizable(false);
	}

	protected static void webcrawl(NationalSecurtyWebSite nationalSecurtyWebSite,Date deadline,int offset) {
		String targetUrl = NationalSecurtyWebSite.NEXT_PAGE_URL+offset;
		System.out.println("targetUrl:"+targetUrl);
		
		List<String> urls = nationalSecurtyWebSite.getListWebSites(targetUrl);
		urls.removeAll(NationalSecurtyWebSite.urlList);
		NationalSecurtyWebSite.urlList.addAll(urls);
		
		System.out.println("Total URL is:"+urls.size());
		for(String url:urls) {
			Map<String,Map<String,String>> data = nationalSecurtyWebSite.getWebContent(NationalSecurtyWebSite.BASE_URL+url);
			try {
				boolean isBlank = checkData(data);
				while(isBlank) {
					Thread.sleep(300000l);
					data = nationalSecurtyWebSite.getWebContent(NationalSecurtyWebSite.BASE_URL+url);
					isBlank = checkData(data);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			for(Entry<String,Map<String,String>> entry:data.entrySet()) {
				String title = entry.getKey();
				
				Map<String,String> content = entry.getValue();
				Date reportDate = null;
				try {
					reportDate = new SimpleDateFormat("yyyy-MM-dd").parse(content.get("发布时间"));
					System.out.println(reportDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(deadline.after(reportDate)) {
					System.out.println("success!");
					return;
				}
					
				File file = ExcelUtil.write2Excel(NationalSecurtyWebSite.TARGET_FILE_DIR, title, content);
				System.out.println(file.getName());
			}
		}
		//next page
		
		webcrawl(nationalSecurtyWebSite,deadline,(offset+=20));
		
		nationalSecurtyWebSite.cd.close();
		NationalSecurtyWebSite.urlList.clear();
		System.out.println("success!");
	}

	private static boolean checkData(Map<String, Map<String, String>> data) {
		for (Entry<String, Map<String, String>> entry : data.entrySet()) {
			if (StringUtils.isEmpty(entry.getKey()) || entry.getValue().size() == 0)
				return true;
		}
		return false;
	}
}
