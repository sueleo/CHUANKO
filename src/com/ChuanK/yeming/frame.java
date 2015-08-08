package com.ChuanK.yeming;
import javax.swing.*;
public class frame 
{
	public static void main(String[] args)
	{
		JFrame frame=new JFrame("串口通信1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new Panel());
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);//框架不可调整
	}
}

