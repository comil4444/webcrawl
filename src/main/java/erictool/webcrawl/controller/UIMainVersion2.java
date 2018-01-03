package erictool.webcrawl.controller;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import erictool.webcrawl.model.CNVDWebContent;
import erictool.webcrawl.service.HtmlParser;
import erictool.webcrawl.service.impl.NationalSecurtyWebSite;
import erictool.webcrawl.util.ExcelUtil;
import erictool.webcrawl.util.PropertyUtil;

public class UIMainVersion2 {

	public static int JTEXT_WIDTH = 30;
	public static Date DATE_NOW = new Date();
	public static int DEFAULT_DAY = 7;

	public static void main(String[] args) {
		JFrame jf = new JFrame("爬虫Version2");
		GridLayout gy = new GridLayout(3, 1);
		jf.setLayout(gy);

		JPanel chromePanel = new JPanel(new BorderLayout());
		final JTextField chromePath = new JTextField();
		chromePath.setEditable(false);
		chromePath.setColumns(JTEXT_WIDTH);
		chromePath.setText(PropertyUtil.getProperties(HtmlParser.CHROME_DRIVER_PATH));
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
					PropertyUtil.setProperties(HtmlParser.CHROME_DRIVER_PATH, jf.getSelectedFile().getAbsolutePath());
				}
			}
		});

		chromePanel.add(chromeButton, BorderLayout.WEST);
		chromePanel.add(chromePath, BorderLayout.EAST);

		JPanel filePanel = new JPanel(new BorderLayout());
		final JTextField filePath = new JTextField();
		filePath.setEditable(false);
		filePath.setColumns(JTEXT_WIDTH);
		filePath.setText(PropertyUtil.getProperties(HtmlParser.TARGET_FILE_DIR));
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
					PropertyUtil.setProperties(HtmlParser.TARGET_FILE_DIR, jf.getSelectedFile().getAbsolutePath());
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
				try {
					cnvdWebcrawl(new NationalSecurtyWebSite(), deadline);
				}catch(Exception ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
				JOptionPane.showMessageDialog(null, "爬取完成！！");
			}

		});
		day.add(confirmButton, BorderLayout.EAST);

		jf.add(filePanel);
		jf.add(chromePanel);
		jf.add(day);

		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				if(NationalSecurtyWebSite.cd!=null)
					NationalSecurtyWebSite.cd.close();
				System.exit(0);
			}
		});
		jf.setLocation(300, 300);
		jf.setSize(800, 400);
		jf.setVisible(true);
		jf.pack();
		jf.setResizable(false);
	}

	protected static void webcrawl(NationalSecurtyWebSite nationalSecurtyWebSite,Date deadline,int offset,List<CNVDWebContent> list) {
		
		webcrawl(nationalSecurtyWebSite,deadline,(offset+=20),list);
		
		nationalSecurtyWebSite.cd.close();
		System.out.println("success!");
	}
	
	
	private static void cnvdWebcrawl(NationalSecurtyWebSite nationalSecurtyWebSite, Date deadline) {
		List<CNVDWebContent> list = nationalSecurtyWebSite.getCNVDWebContent(0, deadline);
		ExcelUtil.write2Excel(PropertyUtil.getProperties(HtmlParser.TARGET_FILE_DIR), NationalSecurtyWebSite.SYSTEM,NationalSecurtyWebSite.SYSTEM+"-"+NationalSecurtyWebSite.SDF.format(new Date()), list, CNVDWebContent.COLUMN_NAME);
		System.out.println("success!");
	}
}
