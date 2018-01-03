package erictool.webcrawl.controller;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import erictool.webcrawl.model.CNVDWebContent;
import erictool.webcrawl.service.HtmlParser;
import erictool.webcrawl.service.impl.AbstractHtmlParser;
import erictool.webcrawl.service.impl.NationalSecurtyWebSite;
import erictool.webcrawl.service.impl.RedQueenWebSite;
import erictool.webcrawl.service.impl.SJCWebSite;
import erictool.webcrawl.util.ExcelUtil;
import erictool.webcrawl.util.PropertyUtil;
import net.miginfocom.swing.MigLayout;

public class UIMainVersion3 extends JFrame {

	// ************for CNVD tabbed Pane component************
	JPanel cnvdPanel = new JPanel();

	JLabel cnvdIsCheckedLabel = new JLabel("是否爬取该网站");
	JCheckBox cnvdIsChecked = new JCheckBox();

	JLabel cnvdUserNameLabel = new JLabel("用户名：");
	JTextField cnvdUserName = new JTextField();

	JLabel cnvdPasswordLabel = new JLabel("密码：");
	JPasswordField cnvdPassword = new JPasswordField(20);

	JLabel cnvdDayLabel = new JLabel("爬取时间：");
	JTextField cnvdDay = new JTextField();

	// ************for redQueen tabbed Pane component************
	JPanel redQueenPanel = new JPanel();

	JLabel redQueenIsCheckedLabel = new JLabel("是否爬取该网站");
	JCheckBox redQueenIsChecked = new JCheckBox();

	JLabel redQueenUserNameLabel = new JLabel("用户名：");
	JTextField redQueenUserName = new JTextField();

	JLabel redQueenPasswordLabel = new JLabel("密码：");
	JPasswordField redQueenPassword = new JPasswordField(20);

	JLabel redQueenDayLabel = new JLabel("爬取时间：");
	JTextField redQueenDay = new JTextField();

	// ************for SJC tabbed Pane component************
	JPanel sjcPanel = new JPanel();

	JLabel sjcIsCheckedLabel = new JLabel("是否爬取该网站");
	JCheckBox sjcIsChecked = new JCheckBox();

	JLabel sjcUserNameLabel = new JLabel("用户名：");
	JTextField sjcUserName = new JTextField();

	JLabel sjcPasswordLabel = new JLabel("密码：");
	JPasswordField sjcPassword = new JPasswordField(20);

	JLabel sjcDayLabel = new JLabel("爬取时间：");
	JTextField sjcDay = new JTextField();

	// tabbed component
	JTabbedPane tabbedPane = new JTabbedPane();

	// for common part: Driver and target file path and execution button
	JPanel jCommon = new JPanel();
	JButton targetFilePathButton = new JButton("文件存放位置：");
	JTextField targetFilePath = new JTextField();

	JButton driverFilePathButton = new JButton("驱动存放位置：");
	JTextField driverFilePath = new JTextField();

	JButton executionButton = new JButton("爬取");

	public UIMainVersion3(String name) {
		super(name);
		initTabPart();
		initCommonPart();
		
		setLayout(new MigLayout());
		add(tabbedPane,"wrap,pushx, growx");
		add(jCommon,"wrap, pushx, growx");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocation(300,300);
		setSize(1000,1000);
		setResizable(false);
		pack();
		setVisible(true);
		
		addListners();
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				if(AbstractHtmlParser.cd!=null)
					AbstractHtmlParser.cd.close();
				System.exit(0);
			}
		});
	}

	private void initTabPart() {
		//cnvd
		cnvdPanel.setLayout(new MigLayout());
		cnvdPanel.add(cnvdIsChecked);
		cnvdPanel.add(cnvdIsCheckedLabel, "wrap, pushx, growx");
		cnvdPanel.add(cnvdUserNameLabel);
		cnvdPanel.add(cnvdUserName, "wrap, pushx, growx");
		cnvdPanel.add(cnvdPasswordLabel);
		cnvdPanel.add(cnvdPassword, "wrap, pushx, growx");
		cnvdPanel.add(cnvdDayLabel);
		cnvdPanel.add(cnvdDay, "pushx, growx");
		//redQueen
		redQueenPanel.setLayout(new MigLayout());
		redQueenPanel.add(redQueenIsChecked);
		redQueenPanel.add(redQueenIsCheckedLabel, "wrap, pushx, growx");
		redQueenPanel.add(redQueenUserNameLabel);
		redQueenPanel.add(redQueenUserName, "wrap, pushx, growx");
		redQueenPanel.add(redQueenPasswordLabel);
		redQueenPanel.add(redQueenPassword, "wrap, pushx, growx");
		redQueenPanel.add(redQueenDayLabel);
		redQueenPanel.add(redQueenDay, "pushx, growx");
		//sjc
		sjcPanel.setLayout(new MigLayout());
		sjcPanel.add(sjcIsChecked);
		sjcPanel.add(sjcIsCheckedLabel, "wrap, pushx, growx");
		sjcPanel.add(sjcUserNameLabel);
		sjcPanel.add(sjcUserName, "wrap, pushx, growx");
		sjcPanel.add(sjcPasswordLabel);
		sjcPanel.add(sjcPassword, "wrap, pushx, growx");
		sjcPanel.add(sjcDayLabel);
		sjcPanel.add(sjcDay, "pushx, growx");
		
		tabbedPane.add("CNVD", cnvdPanel);
		tabbedPane.add("天际友盟", redQueenPanel);
		tabbedPane.add("SJC", sjcPanel);
	}

	private void initCommonPart() {
		jCommon.setLayout(new MigLayout());
		
		jCommon.add(driverFilePathButton,"w 100!");
		jCommon.add(driverFilePath, "pushx, growx");
		
		jCommon.add(executionButton,"wrap, span 2 2");
		
		jCommon.add(targetFilePathButton,"w 100!");
		jCommon.add(targetFilePath, "pushx, growx");
	}

	private void addListners() {
		cnvdComponentListener();
		redQueenComponentListener();
		sjcComponentListener();
		commonComponentListener();
	}

	private void commonComponentListener() {
		
		driverFilePath.setText(PropertyUtil.getProperties(HtmlParser.CHROME_DRIVER_PATH));
		driverFilePath.setEditable(false);
		targetFilePath.setText(PropertyUtil.getProperties(HtmlParser.TARGET_FILE_DIR));
		targetFilePath.setEditable(false);
		
		driverFilePath.setFont(new Font("Serif",0,10));
		targetFilePath.setFont(new Font("Serif",0,10));
		
		driverFilePathButton.setFont(new Font("Serif",0,10));
		targetFilePathButton.setFont(new Font("Serif",0,10));
		
		driverFilePathButton.addActionListener(new ActionListener() {

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

					driverFilePath.setText(jf.getSelectedFile().getAbsolutePath());
					PropertyUtil.setProperties(HtmlParser.CHROME_DRIVER_PATH, jf.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		targetFilePathButton.addActionListener(new ActionListener() {

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
					targetFilePath.setText(jf.getSelectedFile().getAbsolutePath());
					PropertyUtil.setProperties("TARGET_FILE_DIR", jf.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		//web crawl logic
		executionButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				//validation
				if (driverFilePath.getText() ==null || driverFilePath.getText().equals("") || targetFilePath.getText()==null || targetFilePath.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "路径有误！");
					return;
				}
				if(!(cnvdIsChecked.isSelected()||redQueenIsChecked.isSelected()||sjcIsChecked.isSelected())){
					JOptionPane.showMessageDialog(null, "请选中至少一个网址！");
					return;
				}

				try {
					if(cnvdIsChecked.isSelected()) {
						//cnvd logic
						int day = (cnvdDay.getText()==null || cnvdDay.getText().equals("")) ? HtmlParser.DEFAULT_DAY : Integer.valueOf(cnvdDay.getText());
						Calendar c = Calendar.getInstance();
						c.add(Calendar.DATE, -day);
						Date deadline = c.getTime();
						cnvdWebcrawl(new NationalSecurtyWebSite(), deadline);
					}
					if(redQueenIsChecked.isSelected()) {
						//TODO:redQueen logic
					}
					if(sjcIsChecked.isSelected()) {
						//TODO:sjc logic
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
				JOptionPane.showMessageDialog(null, "爬取完成！！");
			}

		});
		
	}

	private void sjcComponentListener() {
		sjcDay.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				int keyChar = e.getKeyChar();
				if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) {

				} else {
					e.consume(); // 关键，屏蔽掉非法输入
				}
			}
		});
		
		sjcDay.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
			}

			public void focusLost(FocusEvent e) {
				PropertyUtil.setProperties(SJCWebSite.CONFIGDAY, sjcDay.getText());
			}
			
		});
		sjcDay.setText(PropertyUtil.getProperties(SJCWebSite.CONFIGDAY));
		
		sjcIsChecked.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
			}

			public void focusLost(FocusEvent e) {
				PropertyUtil.setProperties(SJCWebSite.CONFIGCHECKED, Boolean.valueOf(sjcIsChecked.isSelected()).toString());
			}
			
		});
		sjcIsChecked.setSelected(Boolean.parseBoolean(PropertyUtil.getProperties(SJCWebSite.CONFIGCHECKED)));
		
		sjcUserName.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
			}

			public void focusLost(FocusEvent e) {
				PropertyUtil.setProperties(SJCWebSite.CONFIGUSERNAME, sjcUserName.getText());
			}
			
		});
		sjcUserName.setText(PropertyUtil.getProperties(SJCWebSite.CONFIGUSERNAME));
		
		sjcPassword.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
			}

			public void focusLost(FocusEvent e) {
				PropertyUtil.setProperties(SJCWebSite.CONFIGPASSWORD, sjcPassword.getPassword().toString());
			}
			
		});
		sjcPassword.setText(PropertyUtil.getProperties(SJCWebSite.CONFIGPASSWORD));
		
	}

	private void redQueenComponentListener() {
		redQueenDay.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				int keyChar = e.getKeyChar();
				if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) {

				} else {
					e.consume(); // 关键，屏蔽掉非法输入
				}
			}
		});
		
		redQueenDay.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
			}

			public void focusLost(FocusEvent e) {
				PropertyUtil.setProperties(RedQueenWebSite.CONFIGDAY, redQueenDay.getText());
			}
			
		});
		redQueenDay.setText(PropertyUtil.getProperties(RedQueenWebSite.CONFIGDAY));
		
		redQueenIsChecked.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
			}

			public void focusLost(FocusEvent e) {
				PropertyUtil.setProperties(RedQueenWebSite.CONFIGCHECKED, Boolean.valueOf(redQueenIsChecked.isSelected()).toString());
			}
			
		});
		redQueenIsChecked.setSelected(Boolean.parseBoolean(PropertyUtil.getProperties(RedQueenWebSite.CONFIGCHECKED)));
		
		redQueenUserName.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
			}

			public void focusLost(FocusEvent e) {
				PropertyUtil.setProperties(RedQueenWebSite.CONFIGUSERNAME, redQueenUserName.getText());
			}
			
		});
		redQueenUserName.setText(PropertyUtil.getProperties(RedQueenWebSite.CONFIGUSERNAME));
		
		redQueenPassword.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
			}

			public void focusLost(FocusEvent e) {
				PropertyUtil.setProperties(RedQueenWebSite.CONFIGPASSWORD, redQueenPassword.getPassword().toString());
			}
			
		});
		redQueenPassword.setText(PropertyUtil.getProperties(RedQueenWebSite.CONFIGPASSWORD));
	}

	private void cnvdComponentListener() {
		cnvdUserName.setEditable(false);
		cnvdUserName.setBackground(null);
		cnvdPassword.setEditable(false);
		cnvdPassword.setBackground(null);
		
		cnvdDay.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				int keyChar = e.getKeyChar();
				if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) {

				} else {
					e.consume(); // 关键，屏蔽掉非法输入
				}
			}
		});
		
		cnvdDay.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
			}

			public void focusLost(FocusEvent e) {
				PropertyUtil.setProperties(NationalSecurtyWebSite.CONFIGDAY, cnvdDay.getText());
			}
			
		});
		cnvdDay.setText(PropertyUtil.getProperties(NationalSecurtyWebSite.CONFIGDAY));
		
		cnvdIsChecked.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
			}

			public void focusLost(FocusEvent e) {
				PropertyUtil.setProperties(NationalSecurtyWebSite.CONFIGCHECKED, Boolean.valueOf(cnvdIsChecked.isSelected()).toString());
			}
			
		});
		cnvdIsChecked.setSelected(Boolean.parseBoolean(PropertyUtil.getProperties(NationalSecurtyWebSite.CONFIGCHECKED)));
	}
	
	
	
	public static void main(String[] args) {
		new UIMainVersion3("Eric 爬虫");
	}
	
	private static void cnvdWebcrawl(NationalSecurtyWebSite nationalSecurtyWebSite, Date deadline) {
		List<CNVDWebContent> list = nationalSecurtyWebSite.getCNVDWebContent(0, deadline);
		ExcelUtil.write2Excel(PropertyUtil.getProperties(HtmlParser.TARGET_FILE_DIR), NationalSecurtyWebSite.SYSTEM,NationalSecurtyWebSite.SYSTEM+"-"+NationalSecurtyWebSite.SDF.format(new Date()), list, CNVDWebContent.COLUMN_NAME);
		System.out.println("success!");
	}

}
