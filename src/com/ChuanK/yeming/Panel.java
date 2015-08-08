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
{   //�����������
	String message;
	Timer timer;
	Portcontrol port;
	//���������
	JPanel Left; 
	JTextArea Received;
	JTextArea SendMessage;
	JButton Send;
	//�ұ�������
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
		setLayout(new FlowLayout(FlowLayout.LEFT,0,0)); //������С���úͲ���
		setPreferredSize(new Dimension(450,480));
		
		message=new String(""); //����������ʼ��
		
		timer=new Timer(100,new timerListener()); //ÿ��0.1s��ѯ����һ��timerListener�����¼�
		
		Received=new JTextArea(20,30);//�յ���Ϣ���ı���
		Received.setLineWrap(true);//Receive�ı����Զ�����
		Received.setAutoscrolls(true);
		 JScrollPane jsp = new JScrollPane(Received); 
		
		SendMessage=new JTextArea(5,23);
	    
		Send=new JButton("����"); //������Ϣ���ı���
		Send.addActionListener(new SendListener());
		 //�����
		Left=new JPanel();
		Left.setBackground(Color.blue);
		Left.setPreferredSize(new Dimension(350,480));
		JLabel Title=new JLabel("Receive Frame");
		Left.add(Title);
		Left.add(jsp);
		Left.add(SendMessage);
		Left.add(Send);
		
		 //��������
		JPanel Blank1=new JPanel();
		Blank1.setPreferredSize(new Dimension(100,80));
		
		//�����Ĳ������
		JPanel Parameterp=new JPanel();
		Parameterp.setPreferredSize(new Dimension(100,300));
		JLabel label1=new JLabel("16����");
		r1 = new Checkbox("", null, false);
		Parameterp.add(label1);
		Parameterp.add(r1);
		BaudLab=new JLabel("�����ʣ�");
		bote = new JComboBox();
		bote.addItem("9600");
		bote.addItem("4800");
		bote.addItem("2400");
		bote.addItem("1200");
		bote.addItem("600");
		BaudPanel=new JPanel();
		
		
		//ѡ�񴮿ںͶ�ʱʱ��
		JLabel label=new JLabel("��ʱ����:");
		jt = new JTextField(7);
		BaudPanel.add(label);
		BaudPanel.add(jt);
		ArrayList<String> s= new ArrayList<String>();
		Enumeration portList=CommPortIdentifier.getPortIdentifiers();//ö�ٿ��õĴ���	
		while (portList.hasMoreElements()){
			portId = (CommPortIdentifier)portList.nextElement();
			s.add(portId.getName());
		}
		this.com=new JComboBox(s.toArray());
		BaudPanel.add(com);	
		
		BaudPanel.setPreferredSize(new Dimension(110,130));
		BaudPanel.add(BaudLab);
		BaudPanel.add(bote);
		
		DataLab=new JLabel("����λ��");
		DataPanel=new JPanel();
		DataPanel.setPreferredSize(new Dimension(100,30));
		dataB = new JComboBox();
		dataB.addItem("8");
		dataB.addItem("7");
		dataB.addItem("5");
		DataPanel.add(DataLab);
		DataPanel.add(dataB);

		StopLab=new JLabel("ֹͣλ��");
		StopPanel=new JPanel();
		StopPanel.setPreferredSize(new Dimension(120,30));
		StopPanel.add(StopLab);
		stop = new JComboBox();
		stop.addItem("1");
		stop.addItem("1.5");
		stop.addItem("2");
		StopPanel.add(stop);

		ParityLab=new JLabel("��ż���飺");
		ParityPanel=new JPanel();
		ParityPanel.add(ParityLab);
		parity = new JComboBox();
		parity.addItem("NONE");
		parity.addItem("��");
		parity.addItem("ż");
		parity.addItem("��");
		parity.addItem("��");
		
		ParityPanel.add(parity);

		ParityPanel.setPreferredSize(new Dimension(120,50));
		Parameterp.add(BaudPanel);
		Parameterp.add(DataPanel);
		Parameterp.add(StopPanel);
		Parameterp.add(ParityPanel);
		//�����İ�ť���
		Connect=new JButton("   ��������   ");
		Connect.addActionListener(new ConnectListener());
		JPanel Blank3=new JPanel();
		Blank3.setPreferredSize(new Dimension(100,8));
		DisConnect=new JButton("�Ͽ�����");
		DisConnect.addActionListener(new DisConnectListener());
		//�����������
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
    private class SendListener implements ActionListener               //Send��ť�ļ�����
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
    			    		s = Panel.encode(s);    //���ַ���תΪ16����
     			    	}
     			    	port.Sendto(s);
    		            message=new String(port.Getmessage());
    		            Received.append(message);
    		            System.out.println("�ύ��");
    			      } catch (InterruptedException e) {
    			        e.printStackTrace();
    			      }
    			    }
    			  };
    			  Thread thread = new Thread(runnable);
    			  thread.start();
		}
    }
    private class ConnectListener implements ActionListener             //Connect��ť�ļ�����
    {
    	public void actionPerformed(ActionEvent event)
		{
    		port=new Portcontrol(com.getSelectedIndex()+1);
    		port.open(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
            timer.start();
            Received.append("�������ӳɹ���\n");
            System.out.println("���ӣ�");
		}
    }
    private class DisConnectListener implements ActionListener             //DisConnect��ť�ļ�����
    {
    	public void actionPerformed(ActionEvent event)
		{
            port.close();
            timer.stop();
            Received.append("�Ͽ����ӣ���\n");
            System.out.println("�Ͽ���");
		}
    }
    private class timerListener implements ActionListener                  //timer����ѯ��ÿ��һ���¼������ö��������ϵ���ӦReceived�����
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
				Received.append("����:"+message+"\n");
				repaint();
				System.out.println("�������ݣ�");
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
    		
    		System.out.println("����--"+a+"--"+b+"--"+c+"--"+d);
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
    		if(d.equals("��"))
    			l=SerialPort.PARITY_ODD;
    		if(d.equals("ż"))
    			l=SerialPort.PARITY_EVEN;
    		if(d.equals("��"))
    			l=SerialPort.PARITY_MARK;
    		if(d.equals("��"))
    			l=SerialPort.PARITY_SPACE;
    		port.open(i, j, k, l);
    		timer.start();
    		
    	}
    }
    
    private static String hexString = "0123456789ABCDEF";  
    /* 
    * ���ַ��������16��������,�����������ַ����������ģ� 
    */  
    public static String encode(String str) {  
       // ����Ĭ�ϱ����ȡ�ֽ�����  
       byte[] bytes = str.getBytes();  
       StringBuilder sb = new StringBuilder(bytes.length * 2);  
       // ���ֽ�������ÿ���ֽڲ���2λ16��������  
       for (int i = 0; i < bytes.length; i++) {  
        sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));  
        sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));  
       }  
       return sb.toString();  
    }  
    
    /* 
    * ��16�������ֽ�����ַ���,�����������ַ����������ģ� 
    */  
    public static String decode(String bytes) {  
       ByteArrayOutputStream baos = new ByteArrayOutputStream(  
         bytes.length() / 2);  
       // ��ÿ2λ16����������װ��һ���ֽ�  
       for (int i = 0; i < bytes.length(); i += 2)  
        baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString  
          .indexOf(bytes.charAt(i + 1))));  
       return new String(baos.toByteArray());  
    }  
}

