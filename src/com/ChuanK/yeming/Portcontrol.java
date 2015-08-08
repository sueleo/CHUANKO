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
    StringBuffer buffer;//串口读入缓存
    boolean recieve;//是否有数据读入的标识符
	
	public Portcontrol(int n)                //创建一个名字为 "COM"+n 的串口通信对象
	{
		buffer=new StringBuffer("");
		Enumeration portList=CommPortIdentifier.getPortIdentifiers();//枚举可用的串口
		while (portList.hasMoreElements())
		{
			portId = (CommPortIdentifier)portList.nextElement();
            if (portId.getPortType()==CommPortIdentifier.PORT_SERIAL)//判断串口类型
            {
            	if(portId.getName().equals("COM"+n))
            	{portName="COM"+n;break;}
            }
            else
            {System.out.println("找不到串口！");}
		}     
	}
	public void open(int baudRate, int dataBits, int stopBits, int parity)        //打开串口以及输入输出流
	{
		recieve=false;
		try
		{serialPort=(SerialPort)portId.open("Serial Communication", 2000);}//打开串口
		catch(PortInUseException e){System.out.println("端口正被占用！");}
        try 
        {serialPort.setSerialPortParams(baudRate,dataBits,stopBits,parity);} //串口参数设置
        catch(UnsupportedCommOperationException e){System.out.println("不支持通信");}
        try 
        {
        	outputStream=serialPort.getOutputStream();
        	inputStream=serialPort.getInputStream();
        } 
        catch(IOException e){System.out.println("无法初始化输入输出流！");}
        try
        {serialPort.addEventListener(new serialPortListener());}
        catch (TooManyListenersException e) {System.out.println("监听器太多了！");}
        serialPort.notifyOnDataAvailable(true);
	}
	public void Sendto(String s)                         //将参数字符串s转换成字节，并将此字节流写入到outputStream中
	{
		buffer=new StringBuffer("发送:"+s+"\n");
		try 
		{outputStream.write(s.getBytes());} 
        catch(IOException e){System.out.println("往串口写入数据时发生错误！");}
	}
	public void close()                      //关闭串口
	{
		try
		{
			outputStream.close();
		    inputStream.close();
		}
		catch(IOException e){System.out.println("输入输出流关闭失败！");}
		serialPort.close();
	}
	public class serialPortListener implements SerialPortEventListener                         //监听是否有新数据读入的监听器
	{
		public void serialEvent(SerialPortEvent event)
		{
			if(event.getEventType()==SerialPortEvent.DATA_AVAILABLE)                      //若有新数据传来，则将其转换成字符串，并加进缓存
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
			         System.out.println("从串口读取数据时发生错误！");
			         readResult="";
		        }
		        readResult=new String(readBuffer);
		        buffer=new StringBuffer(readResult);
		        recieve=true;//然后置新数据读入标识为真
			}
		}
	}
	public StringBuffer Getmessage()//返回缓存所存储的字符串
	{System.out.println(buffer);return(buffer);}
	
	public boolean dataComes()//判断串口是否有新数据读入
	{return(recieve);}
	
	public void dataHadRead()//此方法可以让外部的应用程序在读取一次数据后置标识为假
	{recieve=false;}
	
	public String getReasult(){
		System.out.println("ddddd"+readResult);
		return readResult;
	}
}
