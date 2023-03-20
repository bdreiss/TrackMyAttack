package main.java.com.bdreiss.trackmyattack;

import java.util.ArrayList;
import java.util.Map;

import main.java.com.bdreiss.dataAPI.DataModel;
import main.java.com.bdreiss.dataAPI.Intensity;

import java.util.HashMap;
import java.util.Date;

import java.awt.Color;

import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class Main{

	public static void main(String[] args){
	
	//	MainFrame frame = new MainFrame();	
		
	//	frame.setData(new DataModel(20,21));
//		frame.setVisible(true);
	
		DataModel data = new DataModel("Test");
		
		System.out.println("Test");
		data.save();
		
	}
	
}
	

