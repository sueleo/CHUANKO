package com.ChuanKo;

import java.io.*; 
import java.util.*; 
import javax.comm.*;//Java串口通信所需的包
import java.awt.event.*;
import javax.swing.*;

public class serialComm
{
	 public static void main(String[] args)
     {
     	
     	try 
        {
  	       UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
        }
        catch ( Exception e ) {}//将界面设置为当前的Windows风格
      
     	JFrame frame = new JFrame ("RS-232 串口通信");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        commPanel getpanel = new commPanel();
        frame.getContentPane().add (getpanel);
        frame.setResizable(false);//设置框架大小不可调整
        frame.setLocation(300,80);
        frame.pack();
        frame.setVisible(true);
      }
}
