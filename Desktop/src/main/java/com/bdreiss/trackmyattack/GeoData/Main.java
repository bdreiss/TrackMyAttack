package main.java.com.bdreiss.trackmyattack.GeoData;

import java.awt.geom.Point2D;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import main.java.com.bdreiss.trackmyattack.GeoSphereAustria.GeoSphereAustriaStation;

public class Main {

	public static void main(String[] args) throws Exception {

		List<Station> list = new ArrayList<>();
		addToList(list,9.0,1.0);
		addToList(list,6.0,1.0);
		addToList(list,6.0,4.0);
		addToList(list,3.0,2.0);
		addToList(list,3.0,1.0);
		addToList(list,8.0,2.0);
		list.add(null);
		list.add(null);
		
		printList(list);
		System.out.println();
		printList(CoordinateMergeSort.sort(list));

	}
	public static void addToList(List<Station> list, Double x, Double y) {
		list.add(new GeoSphereAustriaStation(0, new Point2D.Double(x,y)));
	}
	
	public static void printList(List<Station> list) {
		for (Station s : list)
			if (s == null)
				System.out.println("null");
			else
				System.out.println(s.getLatitude() + ":" + s.getLongitude());
		System.out.println();
	}
}
