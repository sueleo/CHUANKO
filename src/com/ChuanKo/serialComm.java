package com.ChuanKo;

import java.io.*; 
import java.util.*; 
import javax.comm.*;//Java����ͨ������İ�
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
        catch ( Exception e ) {}//����������Ϊ��ǰ��Windows���
      
     	JFrame frame = new JFrame ("RS-232 ����ͨ��");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        commPanel getpanel = new commPanel();
        frame.getContentPane().add (getpanel);
        frame.setResizable(false);//���ÿ�ܴ�С���ɵ���
        frame.setLocation(300,80);
        frame.pack();
        frame.setVisible(true);
      }
}
