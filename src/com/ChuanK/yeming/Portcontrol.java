package com.ChuanK.yeming;
import java.util.*;
import java.io.*;
import javax.comm.*;
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPortEvent.*;
import javax.comm.SerialPortEventListener.*;

public class Portcontrol
{
	String portName;
	SerialPort serialPort;
	CommPortIdentifier portId;
    InputStream inputStream;
    OutputStream outputStream;
    String readResult;
    StringBuffer buffer;//���ڶ��뻺��
    boolean recieve;//�Ƿ������ݶ���ı�ʶ��
	
	public Portcontrol(int n)                //����һ������Ϊ "COM"+n �Ĵ���ͨ�Ŷ���
	{
		buffer=new StringBuffer("");
		Enumeration portList=CommPortIdentifier.getPortIdentifiers();//ö�ٿ��õĴ���
		while (portList.hasMoreElements())
		{
			portId = (CommPortIdentifier)portList.nextElement();
            if (portId.getPortType()==CommPortIdentifier.PORT_SERIAL)//�жϴ�������
            {
            	if(portId.getName().equals("COM"+n))
            	{portName="COM"+n;break;}
            }
            else
            {System.out.println("�Ҳ������ڣ�");}
		}     
	}
	public void open(int baudRate, int dataBits, int stopBits, int parity)        //�򿪴����Լ����������
	{
		recieve=false;
		try
		{serialPort=(SerialPort)portId.open("Serial Communication", 2000);}//�򿪴���
		catch(PortInUseException e){System.out.println("�˿�����ռ�ã�");}
        try 
        {serialPort.setSerialPortParams(baudRate,dataBits,stopBits,parity);} //���ڲ�������
        catch(UnsupportedCommOperationException e){System.out.println("��֧��ͨ��");}
        try 
        {
        	outputStream=serialPort.getOutputStream();
        	inputStream=serialPort.getInputStream();
        } 
        catch(IOException e){System.out.println("�޷���ʼ�������������");}
        try
        {serialPort.addEventListener(new serialPortListener());}
        catch (TooManyListenersException e) {System.out.println("������̫���ˣ�");}
        serialPort.notifyOnDataAvailable(true);
	}
	public void Sendto(String s)                         //�������ַ���sת�����ֽڣ��������ֽ���д�뵽outputStream��
	{
		buffer=new StringBuffer("����:"+s+"\n");
		try 
		{outputStream.write(s.getBytes());} 
        catch(IOException e){System.out.println("������д������ʱ��������");}
	}
	public void close()                      //�رմ���
	{
		try
		{
			outputStream.close();
		    inputStream.close();
		}
		catch(IOException e){System.out.println("����������ر�ʧ�ܣ�");}
		serialPort.close();
	}
	public class serialPortListener implements SerialPortEventListener                         //�����Ƿ��������ݶ���ļ�����
	{
		public void serialEvent(SerialPortEvent event)
		{
			if(event.getEventType()==SerialPortEvent.DATA_AVAILABLE)                      //���������ݴ���������ת�����ַ��������ӽ�����
			{
				String readResult;
		        byte[] readBuffer=new byte[20];
		        try
		        {
			        while(inputStream.available()>0)
			           inputStream.read(readBuffer);
	            }
		        catch(IOException e)
		        {
			         System.out.println(e);
			         System.out.println("�Ӵ��ڶ�ȡ����ʱ��������");
			         readResult="";
		        }
		        readResult=new String(readBuffer);
		        buffer=new StringBuffer(readResult);
		        recieve=true;//Ȼ���������ݶ����ʶΪ��
			}
		}
	}
	public StringBuffer Getmessage()//���ػ������洢���ַ���
	{System.out.println(buffer);return(buffer);}
	
	public boolean dataComes()//�жϴ����Ƿ��������ݶ���
	{return(recieve);}
	
	public void dataHadRead()//�˷����������ⲿ��Ӧ�ó����ڶ�ȡһ�����ݺ��ñ�ʶΪ��
	{recieve=false;}
	
	public String getReasult(){
		System.out.println("ddddd"+readResult);
		return readResult;
	}
}
