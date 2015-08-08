package com.ChuanK.yeming;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.comm.CommPortIdentifier;
import javax.swing.*;
import javax.comm.SerialPort;

public class Panel extends JPanel
{   //构建所需参数
	String message;
	Timer timer;
	Portcontrol port;
	//左边面板组件
	JPanel Left; 
	JTextArea Received;
	JTextArea SendMessage;
	JButton Send;
	//右边面板组件
	JPanel Right; 
	JPanel BaudPanel,DataPanel,StopPanel,ParityPanel;
	JLabel BaudLab,DataLab,StopLab,ParityLab;
	JComboBox bote,stop,dataB,parity;
	JButton Connect,DisConnect;
	JComboBox com=null;
	CommPortIdentifier portId;
	JTextField jt=null;
	Checkbox r1;
	
	public Panel()
	{
		
		setBackground(Color.blue);
		setLayout(new FlowLayout(FlowLayout.LEFT,0,0)); //总面板大小设置和布局
		setPreferredSize(new Dimension(450,480));
		
		message=new String(""); //基本函数初始化
		
		timer=new Timer(100,new timerListener()); //每隔0.1s轮询产生一个timerListener工作事件
		
		Received=new JTextArea(20,30);//收到信息的文本框
		Received.setLineWrap(true);//Receive文本框自动换行
		Received.setAutoscrolls(true);
		 JScrollPane jsp = new JScrollPane(Received); 
		
		SendMessage=new JTextArea(5,23);
	    
		Send=new JButton("发送"); //发出信息的文本框
		Send.addActionListener(new SendListener());
		 //左面板
		Left=new JPanel();
		Left.setBackground(Color.blue);
		Left.setPreferredSize(new Dimension(350,480));
		JLabel Title=new JLabel("Receive Frame");
		Left.add(Title);
		Left.add(jsp);
		Left.add(SendMessage);
		Left.add(Send);
		
		 //右面板组件
		JPanel Blank1=new JPanel();
		Blank1.setPreferredSize(new Dimension(100,80));
		
		//右面板的参数面板
		JPanel Parameterp=new JPanel();
		Parameterp.setPreferredSize(new Dimension(100,300));
		JLabel label1=new JLabel("16进制");
		r1 = new Checkbox("", null, false);
		Parameterp.add(label1);
		Parameterp.add(r1);
		BaudLab=new JLabel("波特率：");
		bote = new JComboBox();
		bote.addItem("9600");
		bote.addItem("4800");
		bote.addItem("2400");
		bote.addItem("1200");
		bote.addItem("600");
		BaudPanel=new JPanel();
		
		
		//选择串口和定时时间
		JLabel label=new JLabel("定时发送:");
		jt = new JTextField(7);
		BaudPanel.add(label);
		BaudPanel.add(jt);
		ArrayList<String> s= new ArrayList<String>();
		Enumeration portList=CommPortIdentifier.getPortIdentifiers();//枚举可用的串口	
		while (portList.hasMoreElements()){
			portId = (CommPortIdentifier)portList.nextElement();
			s.add(portId.getName());
		}
		this.com=new JComboBox(s.toArray());
		BaudPanel.add(com);	
		
		BaudPanel.setPreferredSize(new Dimension(110,130));
		BaudPanel.add(BaudLab);
		BaudPanel.add(bote);
		
		DataLab=new JLabel("数据位：");
		DataPanel=new JPanel();
		DataPanel.setPreferredSize(new Dimension(100,30));
		dataB = new JComboBox();
		dataB.addItem("8");
		dataB.addItem("7");
		dataB.addItem("5");
		DataPanel.add(DataLab);
		DataPanel.add(dataB);

		StopLab=new JLabel("停止位：");
		StopPanel=new JPanel();
		StopPanel.setPreferredSize(new Dimension(120,30));
		StopPanel.add(StopLab);
		stop = new JComboBox();
		stop.addItem("1");
		stop.addItem("1.5");
		stop.addItem("2");
		StopPanel.add(stop);

		ParityLab=new JLabel("奇偶检验：");
		ParityPanel=new JPanel();
		ParityPanel.add(ParityLab);
		parity = new JComboBox();
		parity.addItem("NONE");
		parity.addItem("奇");
		parity.addItem("偶");
		parity.addItem("高");
		parity.addItem("低");
		
		ParityPanel.add(parity);

		ParityPanel.setPreferredSize(new Dimension(120,50));
		Parameterp.add(BaudPanel);
		Parameterp.add(DataPanel);
		Parameterp.add(StopPanel);
		Parameterp.add(ParityPanel);
		//右面板的按钮面板
		Connect=new JButton("   建立连接   ");
		Connect.addActionListener(new ConnectListener());
		JPanel Blank3=new JPanel();
		Blank3.setPreferredSize(new Dimension(100,8));
		DisConnect=new JButton("断开连接");
		DisConnect.addActionListener(new DisConnectListener());
		//右面板组件添加
		Right=new JPanel();
		Right.setBackground(Color.blue);
		Right.setPreferredSize(new Dimension(100,480));
		Right.add(Blank1);
		Right.add(Parameterp);
		Right.add(Connect);
		Right.add(Blank3);
		Right.add(DisConnect);
		
        add(Left);
        add(Right);
        
        SelecteListener itemListener = new SelecteListener();
        bote.addItemListener(itemListener);
        stop.addItemListener(itemListener);
        dataB.addItemListener(itemListener);
        parity.addItemListener(itemListener);

    }
    private class SendListener implements ActionListener               //Send按钮的监听器
    {
    	public void actionPerformed(ActionEvent event)
		{
    		
    		Runnable runnable = new Runnable() {
    			  public void run() {
    			      System.out.println("Hello !!");
    			      try {
    			    	if(jt.getText().equals("")){
    			    		Thread.sleep(0);
    			    	}else{
    			    		Thread.sleep(Integer.valueOf(jt.getText()));
    			    	}
    			    	String s=SendMessage.getText();
    			    	if(r1.getState())
     			    	{
    			    		s = Panel.encode(s);    //将字符串转为16进制
     			    	}
     			    	port.Sendto(s);
    		            message=new String(port.Getmessage());
    		            Received.append(message);
    		            System.out.println("提交！");
    			      } catch (InterruptedException e) {
    			        e.printStackTrace();
    			      }
    			    }
    			  };
    			  Thread thread = new Thread(runnable);
    			  thread.start();
		}
    }
    private class ConnectListener implements ActionListener             //Connect按钮的监听器
    {
    	public void actionPerformed(ActionEvent event)
		{
    		port=new Portcontrol(com.getSelectedIndex()+1);
    		port.open(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
            timer.start();
            Received.append("建立连接成功！\n");
            System.out.println("连接！");
		}
    }
    private class DisConnectListener implements ActionListener             //DisConnect按钮的监听器
    {
    	public void actionPerformed(ActionEvent event)
		{
            port.close();
            timer.stop();
            Received.append("断开连接！！\n");
            System.out.println("断开！");
		}
    }
    private class timerListener implements ActionListener                  //timer的轮询，每隔一定事件产生该动作，不断地响应Received的面板
	{
		public void actionPerformed(ActionEvent event)
		{
			if(port.dataComes())
			{
				message=new String(port.Getmessage());
				if(r1.getState())
				{
					message=message.trim();
					message=Panel.decode(message);
				}
				port.dataHadRead();
				Received.append("接收:"+message+"\n");
				repaint();
				System.out.println("读到数据！");
			 }
		}
	}
    
    private class SelecteListener implements ItemListener{
    	public void itemStateChanged(ItemEvent e){
    		port.close();
    		timer.stop();
    		String a,b,c,d;
    		a=bote.getSelectedItem().toString();
    		b=dataB.getSelectedItem().toString();
    		c=stop.getSelectedItem().toString();
    		d=parity.getSelectedItem().toString();
    			
    		int i,j=0,k=0,l=0;
    		i=Integer.valueOf(a);
    		
    		System.out.println("属性--"+a+"--"+b+"--"+c+"--"+d);
    		if(b.equals("5"))
    			j = SerialPort.DATABITS_5;
    		if(b.equals("7"))
    			j = SerialPort.DATABITS_7;
    		if(b.equals("8"))
    			j=SerialPort.DATABITS_8;
    		
    		
    		if(c.equals("1"))
    			k=SerialPort.STOPBITS_1;
    		if(c.equals("1.5"))
    			k=SerialPort.STOPBITS_1_5;
    		if(c.equals("2"))
    			k=SerialPort.STOPBITS_2;
    		
    		if(d.equals("NONE"))
    			l=SerialPort.PARITY_NONE;
    		if(d.equals("奇"))
    			l=SerialPort.PARITY_ODD;
    		if(d.equals("偶"))
    			l=SerialPort.PARITY_EVEN;
    		if(d.equals("高"))
    			l=SerialPort.PARITY_MARK;
    		if(d.equals("低"))
    			l=SerialPort.PARITY_SPACE;
    		port.open(i, j, k, l);
    		timer.start();
    		
    	}
    }
    
    private static String hexString = "0123456789ABCDEF";  
    /* 
    * 将字符串编码成16进制数字,适用于所有字符（包括中文） 
    */  
    public static String encode(String str) {  
       // 根据默认编码获取字节数组  
       byte[] bytes = str.getBytes();  
       StringBuilder sb = new StringBuilder(bytes.length * 2);  
       // 将字节数组中每个字节拆解成2位16进制整数  
       for (int i = 0; i < bytes.length; i++) {  
        sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));  
        sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));  
       }  
       return sb.toString();  
    }  
    
    /* 
    * 将16进制数字解码成字符串,适用于所有字符（包括中文） 
    */  
    public static String decode(String bytes) {  
       ByteArrayOutputStream baos = new ByteArrayOutputStream(  
         bytes.length() / 2);  
       // 将每2位16进制整数组装成一个字节  
       for (int i = 0; i < bytes.length(); i += 2)  
        baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString  
          .indexOf(bytes.charAt(i + 1))));  
       return new String(baos.toByteArray());  
    }  
}

