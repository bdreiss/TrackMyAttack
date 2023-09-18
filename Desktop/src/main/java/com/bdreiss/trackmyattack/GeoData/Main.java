package main.java.com.bdreiss.trackmyattack.GeoData;

import java.awt.geom.Point2D;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws Exception {

		List<Point2D.Double> list = new ArrayList<>();
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
	public static void addToList(List<Point2D.Double> list, Double x, Double y) {
		list.add(new Point2D.Double(x,y));
	}
	
	public static void printList(List<Point2D.Double> list) {
		for (Point2D.Double p : list)
			if (p == null)
				System.out.println("null");
			else
				System.out.println(p.x + ":" + p.y);
		System.out.println();
	}
}
