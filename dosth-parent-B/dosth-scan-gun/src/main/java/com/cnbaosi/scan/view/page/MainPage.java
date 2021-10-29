package com.cnbaosi.scan.view.page;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnbaosi.scan.service.ToolService;
import com.dosth.app.dto.AppRecycleReview;


/**
 *  操作主页
 * 
 * @author Yifeng Wang  
 */
@Component("MainPage")
public class MainPage extends JFrame {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ToolService toolSvc;
	
	private JPanel mainPanel; //主面板
	
	public MainPage() {

		Font font1 = new Font("宋体", Font.PLAIN, 15);
		Font font2 = new Font("宋体", Font.PLAIN, 18);
		
		//frame相关设置
		setResizable(false);
		setTitle("智能刀具柜审核系统 v1.0");
		setBounds(100, 100, 600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainPanel = new JPanel();
		setContentPane(mainPanel);
		mainPanel.setBounds(294, 60, 280, 280);
		mainPanel.setLayout(null);
		
		//条形码
		JLabel label = new JLabel("条形码:");
		label.setFont(font2);
		label.setBounds(20, 20, 65, 30);
		mainPanel.add(label);
		
		//条形码输入框
		JTextField textBarCode = new JTextField();
		textBarCode.setFont(font2);
		textBarCode.setBounds(94, 20, 385, 30);
		mainPanel.add(textBarCode);
		textBarCode.setColumns(10);
		
		//物料图片
		JLabel metPic = new JLabel("");
		metPic.setBackground(Color.WHITE);
		metPic.setBounds(10, 60, 280, 280);
		metPic.setHorizontalAlignment(SwingConstants.CENTER);
		mainPanel.add(metPic);

		//物料归还信息面板 
		JPanel metBackInfoPanel = new JPanel();
		metBackInfoPanel.setBounds(294, 60, 280, 280);
		mainPanel.add(metBackInfoPanel);
		metBackInfoPanel.setLayout(null);
		
		//刀片编号
		JLabel metNoLabel = new JLabel("刀片编号:");
		metNoLabel.setFont(font1);
		metNoLabel.setBounds(5, 34, 77, 25);
		metBackInfoPanel.add(metNoLabel);
		
		//刀片编号值
		JTextField metNoValue = new JTextField();
		metNoValue.setEditable(false);
		metNoValue.setFont(font1);
		metNoValue.setBounds(80, 34, 190, 24);
		metBackInfoPanel.add(metNoValue);
		metNoValue.setColumns(10);
		
		//刀片名称
		JLabel metNameLabel = new JLabel("名    称:");
		metNameLabel.setFont(font1);
		metNameLabel.setBounds(5, 83, 77, 25);
		metBackInfoPanel.add(metNameLabel);
		
		//刀片名称值
		JTextField metNameValue = new JTextField();
		metNameValue.setEditable(false);
		metNameValue.setFont(font1);
		metNameValue.setColumns(10);
		metNameValue.setBounds(80, 84, 190, 24);
		metBackInfoPanel.add(metNameValue);
		
		//刀片归还数量
		JLabel metCountLabel = new JLabel("归还数量:");
		metCountLabel.setFont(font1);
		metCountLabel.setBounds(5, 135, 77, 25);
		metBackInfoPanel.add(metCountLabel);
		
		//刀片归还数量值
		JTextField metCountValue = new JTextField();
		metCountValue.setFont(font1);
		metCountValue.setEditable(false);
		metCountValue.setBounds(80, 135, 190, 25);
		metCountValue.setColumns(10);
		metBackInfoPanel.add(metCountValue);
		
		//刀片归还人员
		JLabel metBackNameLabel = new JLabel("归还人员:");
		metBackNameLabel.setFont(font1);
		metBackNameLabel.setBounds(5, 183, 77, 25);
		metBackInfoPanel.add(metBackNameLabel);
		
		//刀片归还人员值
		JTextField metBackNameValue = new JTextField();
		metBackNameValue.setFont(font1);
		metBackNameValue.setEditable(false);
		metBackNameValue.setColumns(10);
		metBackNameValue.setBounds(80, 185, 190, 25);
		metBackInfoPanel.add(metBackNameValue);
		
		//刀片归还时间
		JLabel metBackTimeLabel = new JLabel("归还时间:");
		metBackTimeLabel.setFont(font1);
		metBackTimeLabel.setBounds(5, 231, 77, 25);
		metBackInfoPanel.add(metBackTimeLabel);
		
		//刀片归还时间值
		JTextField metBackTimeValue = new JTextField();
		metBackTimeValue.setFont(font1);
		metBackTimeValue.setEditable(false);
		metBackTimeValue.setColumns(10);
		metBackTimeValue.setBounds(80, 233, 190, 25);
		metBackInfoPanel.add(metBackTimeValue);
		
		//查询按钮
		JButton btnSearch = new JButton("查询");
		btnSearch.setFont(font1);
		btnSearch.setBounds(489, 20, 85, 30);
		mainPanel.add(btnSearch);

		//审核结果
		JLabel label_5 = new JLabel("审核结果：");
		label_5.setFont(font1);
		label_5.setBounds(20, 340, 121, 23);
		mainPanel.add(label_5);
		
		//通过单选按钮
		JRadioButton radioBtnPass = new JRadioButton("通过");
		radioBtnPass.setFont(font1);
		radioBtnPass.setSelected(true);
		radioBtnPass.setBounds(20, 370, 121, 23);
		
		//刀具不符单选按钮
		JRadioButton radioBtnKnifeNotMatch = new JRadioButton("刀具不符");
		radioBtnKnifeNotMatch.setFont(font1);
		radioBtnKnifeNotMatch.setBounds(20, 400, 121, 23);
		
		//数量不符单选按钮
		JRadioButton radioBtnKnifeCountNotMatch = new JRadioButton("数量不符");
		radioBtnKnifeCountNotMatch.setFont(font1);
		radioBtnKnifeCountNotMatch.setBounds(20, 430, 93, 23);
		
		//数量不符值
		JTextField countNotMatchValue = new JTextField();
		countNotMatchValue.setFont(font1);
		countNotMatchValue.setText("请输入实际数量");
		countNotMatchValue.setToolTipText("");
		countNotMatchValue.setColumns(10);
		countNotMatchValue.setBounds(119, 430, 161, 21);
		mainPanel.add(countNotMatchValue);
		
		//其他不符单选按钮
		JRadioButton radioBtnKnifeOtherNotMatch = new JRadioButton("其他");
		radioBtnKnifeOtherNotMatch.setFont(new Font("宋体", Font.PLAIN, 17));
		radioBtnKnifeOtherNotMatch.setBounds(20, 460, 59, 23);

		//其他不符单选值
		JTextField otherNotMatchValue = new JTextField();
		otherNotMatchValue.setFont(new Font("宋体", Font.PLAIN, 17));
		otherNotMatchValue.setBounds(82, 460, 199, 21);
		mainPanel.add(otherNotMatchValue);
		otherNotMatchValue.setColumns(10);
		
		//单选按钮组
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(radioBtnPass);
		radioGroup.add(radioBtnKnifeNotMatch);
		radioGroup.add(radioBtnKnifeCountNotMatch);
		radioGroup.add(radioBtnKnifeOtherNotMatch);
		
		mainPanel.add(radioBtnPass);
		mainPanel.add(radioBtnKnifeNotMatch);
		mainPanel.add(radioBtnKnifeCountNotMatch);
		mainPanel.add(radioBtnKnifeOtherNotMatch);
		
		//提交按钮
		JButton btnSubmit = new JButton("提  交");
		btnSubmit.setFont(new Font("宋体", Font.PLAIN, 17));
		btnSubmit.setBounds(157, 500, 271, 40);
		mainPanel.add(btnSubmit);
		
		
		//点击查询按钮事件
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				String barCode = textBarCode.getText();
				String barCode = "402881e26aa01f10016aa030af930000";
				
				AppRecycleReview recycle = toolSvc.recycleInfo(barCode);
				if(recycle.getStatus()) {
					//回填回收信息  TODO
					metPic.setIcon(new ImageIcon("D:\\upload\\2a2ad9d4-846e-4998-a268-31fbe7c14483.jpg"));
					metNoValue.setText("1101060320");
					metNameValue.setText("车刀片");
					metCountValue.setText("2");
					metBackNameValue.setText("张三");
					metBackTimeValue.setText("2019年04月29日 15:23:20");
				} else {
					JOptionPane.showMessageDialog(null, recycle.getMsg(), "提示", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		//点击提交按钮事件
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//提交审核结果 TODO
				/*
				1、提交成功
				2、提交失败
					(1)物料已经审核过
					(2)服务器连接失败
					(3)其他原因
				*/
			}
		});
		
		
	}
	
	public static void main(String[] args) {
		MainPage mp = new MainPage();
		mp.setVisible(true);
	}
}

