package com.ChuanKo;

import java.io.*;//�����������İ�
import java.util.*; 
import javax.comm.*;//Java����ͨ������İ�
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class commPanel extends JPanel
{
   SerialPort serialPort=null;//����һ��rs - 232����ͨ�Ŷ˿ڣ�����ֵһ����ָ��
   DataOutputStream doutput=null;
   InputStream inputStream;
   CommPortIdentifier portId=null;
   
   int asValue;
   char charValue;
   
   int baudRate=9600;
   int dataBits=SerialPort.DATABITS_8;
   int stopBits=SerialPort.STOPBITS_1;
   int parity=SerialPort.PARITY_NONE;
   
   JLabel mytext=new JLabel("ռ��λ�÷�");
   JLabel baudtext=new JLabel("������");
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
   JButton sendOut=new JButton("����");
   
   String messageString=null;

   public commPanel()//���췽��
   {
      //�������ò���
      setLayout (new FlowLayout(FlowLayout.LEFT,0,0));//���������Ϊ��ʽ���֣��������
      setPreferredSize (new Dimension(500, 400));//�������Ĵ�С
      
      add(parametersPanel);//�Ѳ���ѡ�������ӵ������
      parametersPanel.setPreferredSize(new Dimension(100, 400));//���ò���ѡ�����Ĵ�С
      parametersPanel.setBackground(new Color(93,148,78));//���ò���ѡ��������ɫ
      
      mytext.setForeground(new Color(93,148,78));//��ռλlabel����Ϊ����ɫ
      parametersPanel.add(mytext);//��ӵ�����ѡ��������ռλ
      
      //���������벿��
      parametersPanel.add(baudtext);
      forbaud=new JTextField(4);
      forbaud.addActionListener(new BaudListener());//�������ı�������¼�������
      parametersPanel.add(forbaud);
      
      //����λ�ĵ�ѡ������
      datachoose1 = new JRadioButton ("DATABITS_8", true);
      datachoose1.setBackground (new Color(93,148,78));
      datachoose2 = new JRadioButton ("DATABITS_7", true);
      datachoose2.setBackground (new Color(93,148,78));
      datachoose3 = new JRadioButton ("DATABITS_6", true);
      datachoose3.setBackground (new Color(93,148,78));

      //��������ѡ����ӵ�ͬһ��group��
      group1.add(datachoose1); 
group1.add(datachoose2); 
group1.add(datachoose3); 
      
      QuoteListener1 datalistener = new QuoteListener1();//����ѡ������¼�������
      datachoose1.addActionListener (datalistener);
      datachoose2.addActionListener (datalistener);
      datachoose3.addActionListener (datalistener);

      datapanel.setLayout(new BoxLayout(datapanel,BoxLayout.Y_AXIS));
      datapanel.setBackground(new Color(93,148,78));
      datapanel.setBorder(BorderFactory.createTitledBorder("����λ"));//Ϊ��ѡ�������ӱ���߿�
      datapanel.add (datachoose1);
      datapanel.add (datachoose2);
      datapanel.add (datachoose3);

      parametersPanel.add(datapanel);
      
      //ֹͣλ�ĵ�ѡ������
      stopchoose1 = new JRadioButton ("STBITS_1", true);
      stopchoose1.setBackground (new Color(93,148,78));
      stopchoose2 = new JRadioButton ("STBITS_1_5", true);
      stopchoose2.setBackground (new Color(93,148,78));
      stopchoose3 = new JRadioButton ("STBITS_2", true);
      stopchoose3.setBackground (new Color(93,148,78));

      //��������ѡ����ӵ�ͬһ��group��
      group2.add(stopchoose1);
group2.add(stopchoose2); 
group2.add(stopchoose3); 
      
      QuoteListener2 stoplistener = new QuoteListener2();//����ѡ������¼�������
      stopchoose1.addActionListener (stoplistener);
      stopchoose2.addActionListener (stoplistener);
      stopchoose3.addActionListener (stoplistener);

      stoppanel.setLayout(new BoxLayout(stoppanel,BoxLayout.Y_AXIS));
      stoppanel.setBackground(new Color(93,148,78));
      stoppanel.setBorder(BorderFactory.createTitledBorder("ֹͣλ"));//Ϊ��ѡ�������ӱ���߿�
      stoppanel.add (stopchoose1);
      stoppanel.add (stopchoose2);
      stoppanel.add (stopchoose3);

      parametersPanel.add(stoppanel);
      
      //��ż����λ�ĵ�ѡ������
      parichoose1 = new JRadioButton ("PARITY_NONE", true);
      parichoose1.setBackground (new Color(93,148,78));
      parichoose2 = new JRadioButton ("PARITY_ODD", true);
      parichoose2.setBackground (new Color(93,148,78));
      parichoose3 = new JRadioButton ("PARITY_EVEN", true);
      parichoose3.setBackground (new Color(93,148,78));

      //��������ѡ����ӵ�ͬһ��group��
      group3.add(parichoose1);
group3.add(parichoose2); 
group3.add(parichoose3); 
      
      QuoteListener3 parilistener = new QuoteListener3();//����ѡ������¼�������
      parichoose1.addActionListener (parilistener);
      parichoose2.addActionListener (parilistener);
      parichoose3.addActionListener (parilistener);

      paripanel.setLayout(new BoxLayout(paripanel,BoxLayout.Y_AXIS));
      paripanel.setBackground(new Color(93,148,78));
      paripanel.setBorder(BorderFactory.createTitledBorder("��ż����λ"));//Ϊ��ѡ�������ӱ���߿�
      paripanel.add (parichoose1);
      paripanel.add (parichoose2);
      paripanel.add (parichoose3);

      parametersPanel.add(paripanel);
      
      add(workPanel);//�ѹ����������ӵ������
      workPanel.setPreferredSize(new Dimension(400, 400));
      workPanel.setBackground(Color.black);//���ù�������屳����ɫ
      workPanel.setLayout (new BoxLayout (this.workPanel, BoxLayout.Y_AXIS));//���ù��������Ϊ��ʽ����
      
      workPanel.add(messagePanel);//����Ϣ�����ӵ����������
      workPanel.add(Box.createRigidArea (new Dimension (0, 2)));//��Ӽ�����򵽹��������
      workPanel.add(inputPanel);//�����������ӵ����������
      workPanel.add(buttonPanel);//�Ѱ�ť�����ӵ����������
      
      messagePanel.setPreferredSize(new Dimension(400, 280));
      messagePanel.add(wen);//����Ϣ����������Ķ����ı���
      wen.setEditable(false);//��������Ķ����ı��򲻿ɱ༭
      wen.setLineWrap(true);//��������Ķ����ı����Զ�����
      
      inputPanel.setPreferredSize(new Dimension(400, 94));
      inputPanel.add(typeIn);//����������������Ķ����ı���
      typeIn.setLineWrap(true);//��������Ķ����ı����Զ�����
      
      //Ϊ���������ı�����ӹ���(Ҫ��JTextArea��Ӻ������)
      messagePanel.setLayout(new GridLayout(1, 1));
      JScrollPane scr1 = new JScrollPane(wen,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      messagePanel.add(scr1);
      
      inputPanel.setLayout(new GridLayout(1, 1));
      JScrollPane scr2 = new JScrollPane(typeIn,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      inputPanel.add(scr2);
      
      buttonPanel.setPreferredSize(new Dimension(400, 24));
      buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));//���ð�ť���Ϊ��ʽ���֣����Ҷ���
      buttonPanel.add(sendOut);//��"����"��ť��ӵ���ť�����
      sendOut.addActionListener(new SendListener());//����ť����¼�������
      
      //�������ò���
        try
     	{
     		portId=CommPortIdentifier.getPortIdentifier("COM");//����ʶ��ѡ�����еĴ���
        }
        catch(NoSuchPortException ne)
        {
        	System.out.println("ne"); ne.printStackTrace();
        }

        try
        {
        	 serialPort=(SerialPort) portId.open("TestComm", 2);//�򿪴���
             OutputStream output = serialPort.getOutputStream();//ͨ�����ڻ�������
             doutput=new DataOutputStream(output);//����������
             
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
         	serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parity);//���ô��ڲ�����"������"��"����λ"��"ֹͣλ"��"��żУ��λ"
         }
         catch (UnsupportedCommOperationException e) {}
         
         Runnable r = new ThreadTest();
         Thread readin = new Thread(r);//ʵ���������߳�
         readin.start();//��ʼ�����߳�
       
      }
      //���췽������
      
   //�ڲ���1�������ʵ����ı��������
   private class BaudListener implements ActionListener
   {
      public void actionPerformed (ActionEvent event)
      {
         String t = forbaud.getText();
         baudRate = Integer.parseInt( t );//���ַ���ת��Ϊint�͵�ֵ
         
         try
         {
         	serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parity);//�������ô��ڲ���
         }
         catch (UnsupportedCommOperationException e) {}
      }
   }
   
   //�ڲ���2������λ��ѡ�������
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
         	serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parity);//�������ô��ڲ���
         }
         catch (UnsupportedCommOperationException e) {}
      }
   }
   
   //�ڲ���3��ֹͣλ��ѡ�������
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
         	serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parity);//�������ô��ڲ���
         }
         catch (UnsupportedCommOperationException e) {}
      }
   }
   
   //�ڲ���4����żУ��λ��ѡ�������
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
         	serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parity);//�������ô��ڲ���
         }
         catch (UnsupportedCommOperationException e) {}
      }
   }
   
   //�ڲ���5��"����"��ť������
   private class SendListener implements ActionListener
   {
      public void actionPerformed (ActionEvent event)
      {
         messageString = typeIn.getText();//��JTextArea�л�ȡString�ı�
         wen.append("<<����>>"+messageString+"\n");//��JTextAreaĩβ����ı�
         typeIn.setText("");//��JTextArea��Ϊ�հ�
         String sendMessage="*"+messageString+"\0";//�ַ���ƴ�ӣ������͵��ַ�����ӿ�ͷ�ͽ����ַ�
         try
         {
         	doutput.write(sendMessage.getBytes());//��������д��ASCII��ֵ����
         }
         catch (IOException e) {}
         
      }
   }
  
  //�ڲ���6��������Ϣ�߳�
  class ThreadTest implements Runnable 
  {
     public void run()//����run����
     {
       try
       {
          inputStream = serialPort.getInputStream();//ͨ�����ڻ��������
         	
          try
          {
            while(true)//�����ظ����뵥��ASCII��
            {
         	   do
         	   {
         	   	  asValue=inputStream.read();//��ȡ����ASCII��ֵ
         	      charValue=(char)asValue;//������ASCII��ֵת��Ϊ�ַ���
         	      if(charValue=='*')
         	      wen.append("<<�յ�>>");
         	      else if(charValue=='\0')
         	      wen.append("\n");
         	      else
         	      wen.append(String.valueOf(charValue));//���ַ���ת��ΪString��
         	   }
         	   while(charValue!='\0');//һ�η��ͽ���
         	   
         	   try 
               {
                  Thread.currentThread().sleep(50);//����sleepʹ�ý�����Ϣ50΢�룬�����߳�����
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

