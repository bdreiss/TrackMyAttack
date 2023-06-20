package main.java.com.bdreiss.trackmyattack;

import java.util.ArrayList;
import java.util.Map;

import main.java.com.bdreiss.dataAPI.DataModel;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.Intensity;
import main.java.com.bdreiss.dataAPI.util.Datum;
import main.java.com.bdreiss.dataAPI.util.DatumWithIntensity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;

import java.awt.Color;

import java.io.Serializable;
import java.time.LocalDate;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class Main{

	public static void main(String[] args){

		DataModel data = new DataModel("/home/bernd/Schreibtisch/TrackMyAttack/Desktop");

		MainFrame frame = new MainFrame(data);	

		frame.setVisible(true);
	
		
		
	}
	
}
	

