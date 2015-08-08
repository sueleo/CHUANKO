package com.ChuanKo;

import java.io.*;//输入输出所需的包
import java.util.*; 
import javax.comm.*;//Java串口通信所需的包
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class commPanel extends JPanel
{
   SerialPort serialPort=null;//声明一个rs - 232串行通信端口，并赋值一个空指针
   DataOutputStream doutput=null;
   InputStream inputStream;
   CommPortIdentifier portId=null;
   
   int asValue;
   char charValue;
   
   int baudRate=9600;
   int dataBits=SerialPort.DATABITS_8;
   int stopBits=SerialPort.STOPBITS_1;
   int parity=SerialPort.PARITY_NONE;
   
   JLabel mytext=new JLabel("占用位置符");
   JLabel baudtext=new JLabel("波特率");
   JTextField forbaud;
   JRadioButton datachoose1, datachoose2, datachoose3;
   JRadioButton stopchoose1, stopchoose2, stopchoose3;
   JRadioButton parichoose1, parichoose2, parichoose3;
   
   ButtonGroup group1 = new ButtonGroup();
   ButtonGroup group2 = new ButtonGroup();
   ButtonGroup group3 = new ButtonGroup();
   
   JPanel datapanel=new JPanel();
   JPanel stoppanel=new JPanel();
   JPanel paripanel=new JPanel();
   
   
   JPanel parametersPanel=new JPanel();
   JPanel workPanel=new JPanel();
   JPanel messagePanel=new JPanel();
   JPanel inputPanel=new JPanel();
   JPanel buttonPanel=new JPanel();
   
   JTextArea wen=new JTextArea(16,37);
   JTextArea typeIn=new JTextArea(6,37);
   JButton sendOut=new JButton("发送");
   
   String messageString=null;

   public commPanel()//构造方法
   {
      //界面设置部分
      setLayout (new FlowLayout(FlowLayout.LEFT,0,0));//设置主面板为流式布局，靠左对齐
      setPreferredSize (new Dimension(500, 400));//设置面板的大小
      
      add(parametersPanel);//把参数选择面板添加到主面板
      parametersPanel.setPreferredSize(new Dimension(100, 400));//设置参数选择面板的大小
      parametersPanel.setBackground(new Color(93,148,78));//设置参数选择面板的颜色
      
      mytext.setForeground(new Color(93,148,78));//把占位label设置为背景色
      parametersPanel.add(mytext);//添加到参数选择面板进行占位
      
      //波特率输入部分
      parametersPanel.add(baudtext);
      forbaud=new JTextField(4);
      forbaud.addActionListener(new BaudListener());//给单行文本框添加事件监听器
      parametersPanel.add(forbaud);
      
      //数据位的单选框设置
      datachoose1 = new JRadioButton ("DATABITS_8", true);
      datachoose1.setBackground (new Color(93,148,78));
      datachoose2 = new JRadioButton ("DATABITS_7", true);
      datachoose2.setBackground (new Color(93,148,78));
      datachoose3 = new JRadioButton ("DATABITS_6", true);
      datachoose3.setBackground (new Color(93,148,78));

      //把三个单选项添加到同一个group中
      group1.add(datachoose1); 
group1.add(datachoose2); 
group1.add(datachoose3); 
      
      QuoteListener1 datalistener = new QuoteListener1();//给单选项添加事件监听器
      datachoose1.addActionListener (datalistener);
      datachoose2.addActionListener (datalistener);
      datachoose3.addActionListener (datalistener);

      datapanel.setLayout(new BoxLayout(datapanel,BoxLayout.Y_AXIS));
      datapanel.setBackground(new Color(93,148,78));
      datapanel.setBorder(BorderFactory.createTitledBorder("数据位"));//为单选框面板添加标题边框
      datapanel.add (datachoose1);
      datapanel.add (datachoose2);
      datapanel.add (datachoose3);

      parametersPanel.add(datapanel);
      
      //停止位的单选框设置
      stopchoose1 = new JRadioButton ("STBITS_1", true);
      stopchoose1.setBackground (new Color(93,148,78));
      stopchoose2 = new JRadioButton ("STBITS_1_5", true);
      stopchoose2.setBackground (new Color(93,148,78));
      stopchoose3 = new JRadioButton ("STBITS_2", true);
      stopchoose3.setBackground (new Color(93,148,78));

      //把三个单选项添加到同一个group中
      group2.add(stopchoose1);
group2.add(stopchoose2); 
group2.add(stopchoose3); 
      
      QuoteListener2 stoplistener = new QuoteListener2();//给单选项添加事件监听器
      stopchoose1.addActionListener (stoplistener);
      stopchoose2.addActionListener (stoplistener);
      stopchoose3.addActionListener (stoplistener);

      stoppanel.setLayout(new BoxLayout(stoppanel,BoxLayout.Y_AXIS));
      stoppanel.setBackground(new Color(93,148,78));
      stoppanel.setBorder(BorderFactory.createTitledBorder("停止位"));//为单选框面板添加标题边框
      stoppanel.add (stopchoose1);
      stoppanel.add (stopchoose2);
      stoppanel.add (stopchoose3);

      parametersPanel.add(stoppanel);
      
      //奇偶检验位的单选框设置
      parichoose1 = new JRadioButton ("PARITY_NONE", true);
      parichoose1.setBackground (new Color(93,148,78));
      parichoose2 = new JRadioButton ("PARITY_ODD", true);
      parichoose2.setBackground (new Color(93,148,78));
      parichoose3 = new JRadioButton ("PARITY_EVEN", true);
      parichoose3.setBackground (new Color(93,148,78));

      //把三个单选项添加到同一个group中
      group3.add(parichoose1);
group3.add(parichoose2); 
group3.add(parichoose3); 
      
      QuoteListener3 parilistener = new QuoteListener3();//给单选项添加事件监听器
      parichoose1.addActionListener (parilistener);
      parichoose2.addActionListener (parilistener);
      parichoose3.addActionListener (parilistener);

      paripanel.setLayout(new BoxLayout(paripanel,BoxLayout.Y_AXIS));
      paripanel.setBackground(new Color(93,148,78));
      paripanel.setBorder(BorderFactory.createTitledBorder("奇偶检验位"));//为单选框面板添加标题边框
      paripanel.add (parichoose1);
      paripanel.add (parichoose2);
      paripanel.add (parichoose3);

      parametersPanel.add(paripanel);
      
      add(workPanel);//把工作区面板添加到主面板
      workPanel.setPreferredSize(new Dimension(400, 400));
      workPanel.setBackground(Color.black);//设置工作区面板背景颜色
      workPanel.setLayout (new BoxLayout (this.workPanel, BoxLayout.Y_AXIS));//设置工作区面板为盒式布局
      
      workPanel.add(messagePanel);//把信息面板添加到工作区面板
      workPanel.add(Box.createRigidArea (new Dimension (0, 2)));//添加间隔区域到工作区面板
      workPanel.add(inputPanel);//把输入面板添加到工作区面板
      workPanel.add(buttonPanel);//把按钮面板添加到工作区面板
      
      messagePanel.setPreferredSize(new Dimension(400, 280));
      messagePanel.add(wen);//给信息面板添加输出的多行文本框
      wen.setEditable(false);//设置输出的多行文本框不可编辑
      wen.setLineWrap(true);//设置输出的多行文本框自动换行
      
      inputPanel.setPreferredSize(new Dimension(400, 94));
      inputPanel.add(typeIn);//给输入面板添加输入的多行文本框
      typeIn.setLineWrap(true);//设置输入的多行文本框自动换行
      
      //为两个多行文本框添加滚动(要在JTextArea添加后再添加)
      messagePanel.setLayout(new GridLayout(1, 1));
      JScrollPane scr1 = new JScrollPane(wen,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      messagePanel.add(scr1);
      
      inputPanel.setLayout(new GridLayout(1, 1));
      JScrollPane scr2 = new JScrollPane(typeIn,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      inputPanel.add(scr2);
      
      buttonPanel.setPreferredSize(new Dimension(400, 24));
      buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));//设置按钮面板为流式布局，靠右对齐
      buttonPanel.add(sendOut);//把"发送"按钮添加到按钮面板上
      sendOut.addActionListener(new SendListener());//给按钮添加事件监听器
      
      //串口配置部分
        try
     	{
     		portId=CommPortIdentifier.getPortIdentifier("COM");//检索识别，选定已有的串口
        }
        catch(NoSuchPortException ne)
        {
        	System.out.println("ne"); ne.printStackTrace();
        }

        try
        {
        	 serialPort=(SerialPort) portId.open("TestComm", 2);//打开串口
             OutputStream output = serialPort.getOutputStream();//通过串口获得输出流
             doutput=new DataOutputStream(output);//定义数据流
             
        }
        catch(PortInUseException ex)
        {
        	System.out.println("ex"); ex.printStackTrace();
        }
        catch(IOException ie) 
        {
        	 System.out.println("ie");
             ie.printStackTrace(); 
             serialPort.close(); 
        } 

         try
         {
         	serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parity);//设置串口参数："波特率"，"数据位"，"停止位"和"奇偶校验位"
         }
         catch (UnsupportedCommOperationException e) {}
         
         Runnable r = new ThreadTest();
         Thread readin = new Thread(r);//实例化接收线程
         readin.start();//开始接收线程
       
      }
      //构造方法结束
      
   //内部类1：波特率单行文本框监听器
   private class BaudListener implements ActionListener
   {
      public void actionPerformed (ActionEvent event)
      {
         String t = forbaud.getText();
         baudRate = Integer.parseInt( t );//将字符串转换为int型的值
         
         try
         {
         	serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parity);//重新设置串口参数
         }
         catch (UnsupportedCommOperationException e) {}
      }
   }
   
   //内部类2：数据位单选框监听器
   private class QuoteListener1 implements ActionListener
   {
      public void actionPerformed (ActionEvent event)
      {
         Object source = event.getSource();

         if (source == datachoose1)
            dataBits=SerialPort.DATABITS_8;
         else if (source == datachoose2)
            dataBits=SerialPort.DATABITS_7;
         else
            dataBits=SerialPort.DATABITS_6;
         
         try
         {
         	serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parity);//重新设置串口参数
         }
         catch (UnsupportedCommOperationException e) {}
      }
   }
   
   //内部类3：停止位单选框监听器
   private class QuoteListener2 implements ActionListener
   {
      public void actionPerformed (ActionEvent event)
      {
         Object source = event.getSource();

         if (source == stopchoose1)
            stopBits=SerialPort.STOPBITS_1;
         else if (source == stopchoose2)
            stopBits=SerialPort.STOPBITS_1_5;
         else
            stopBits=SerialPort.STOPBITS_2;
         
         try
         {
         	serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parity);//重新设置串口参数
         }
         catch (UnsupportedCommOperationException e) {}
      }
   }
   
   //内部类4：奇偶校验位单选框监听器
   private class QuoteListener3 implements ActionListener
   {
      public void actionPerformed (ActionEvent event)
      {
         Object source = event.getSource();

         if (source == parichoose1)
            parity=SerialPort.PARITY_NONE;
         else if (source == parichoose2)
            parity=SerialPort.PARITY_ODD;
         else
            parity=SerialPort.PARITY_EVEN;
         
         try
         {
         	serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parity);//重新设置串口参数
         }
         catch (UnsupportedCommOperationException e) {}
      }
   }
   
   //内部类5："发送"按钮监听器
   private class SendListener implements ActionListener
   {
      public void actionPerformed (ActionEvent event)
      {
         messageString = typeIn.getText();//从JTextArea中获取String文本
         wen.append("<<发送>>"+messageString+"\n");//往JTextArea末尾添加文本
         typeIn.setText("");//将JTextArea置为空白
         String sendMessage="*"+messageString+"\0";//字符串拼接，给发送的字符串添加开头和结束字符
         try
         {
         	doutput.write(sendMessage.getBytes());//向数据流写入ASCII码值数据
         }
         catch (IOException e) {}
         
      }
   }
  
  //内部类6：接收信息线程
  class ThreadTest implements Runnable 
  {
     public void run()//定义run方法
     {
       try
       {
          inputStream = serialPort.getInputStream();//通过串口获得输入流
         	
          try
          {
            while(true)//不断重复读入单个ASCII码
            {
         	   do
         	   {
         	   	  asValue=inputStream.read();//读取单个ASCII码值
         	      charValue=(char)asValue;//将单个ASCII码值转换为字符型
         	      if(charValue=='*')
         	      wen.append("<<收到>>");
         	      else if(charValue=='\0')
         	      wen.append("\n");
         	      else
         	      wen.append(String.valueOf(charValue));//将字符型转换为String型
         	   }
         	   while(charValue!='\0');//一次发送结束
         	   
         	   try 
               {
                  Thread.currentThread().sleep(50);//利用sleep使得进程休息50微秒，让主线程运行
               }
               catch (Exception e) {}
         	 }
          }
          catch (IOException e) {}	
        }
        catch (IOException e) {}
      }
  }
}

