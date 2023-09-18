package main.java.com.bdreiss.trackmyattack.GeoData;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CoordinateMergeSort {
	
	public static List<Point2D.Double> sort(List<Point2D.Double> list) {
				
		if (list.size()<=1)
			return list;
		
		
		int m = list.size()/2;
		List<Point2D.Double> listLeft = list.subList(0, m);
		List<Point2D.Double> listRight = list.subList(m, list.size());

		List<Point2D.Double> listReturn = 
					 (merge(sort(listLeft), sort(listRight)));
		
		return listReturn;
	}
	
	private static List<Point2D.Double> merge(List<Point2D.Double> list1, List<Point2D.Double> list2){
		
		List<Point2D.Double> listReturn = new ArrayList<Point2D.Double>();

		
		Iterator<Point2D.Double> it1 = list1.iterator();
		Iterator<Point2D.Double> it2 = list2.iterator();
		
		if (!it1.hasNext() && !it2.hasNext())
			return listReturn;
		if (!it1.hasNext())
			return list2;
		if (!it2.hasNext())
			return list1;
		
		Point2D.Double current1 = it1.next();
		Point2D.Double current2 = it2.next();

		
		while ((current1 == null || current1.x != Double.POSITIVE_INFINITY) && (current2== null || current2.x != Double.POSITIVE_INFINITY)) {

			Double diffX = (current1==null?Double.MAX_VALUE:current1.x) - (current2==null?Double.MAX_VALUE:current2.x);
			ItemToAdd itemToAdd = ItemToAdd.BOTH;
			
			if (diffX == 0) {
				Double diffY = (current1==null?Double.MAX_VALUE:current1.y) - (current2==null?Double.MAX_VALUE:current2.y);
				
				if (diffY == 0) itemToAdd = ItemToAdd.BOTH;
				
				if (diffY < 0) itemToAdd = ItemToAdd.FIRST;						
				
				if (diffY > 0) itemToAdd = ItemToAdd.SECOND;

			}
			
			if (diffX < 0) itemToAdd = ItemToAdd.FIRST;						
			
			if (diffX > 0) itemToAdd = ItemToAdd.SECOND;
				
			

			switch (itemToAdd) {
				case FIRST:
					listReturn.add(current1);
					current1 = it1.hasNext()? it1.next():new Point2D.Double(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);		
					break;
				case SECOND:
					listReturn.add(current2);
					current2 = it2.hasNext()? it2.next():new Point2D.Double(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
					break;
				case BOTH:
					listReturn.add(current1);
					current1 = it1.hasNext()? it1.next():new Point2D.Double(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
					listReturn.add(current2);
					current2 = it2.hasNext()? it2.next():new Point2D.Double(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
			}
		} 

		if (current1 != null) {
			if (current2 == null)
				listReturn.add(current2);
			else if (current1.x == Double.POSITIVE_INFINITY && current2.x < Double.POSITIVE_INFINITY)
				listReturn.add(current2);
		}
		
		if (current2 != null) {
			if (current1 == null)
				listReturn.add(current1);
			else if (current2.x == Double.POSITIVE_INFINITY && current1.x < Double.POSITIVE_INFINITY)
				listReturn.add(current1);
		}

		if (!it1.hasNext()) {
			while(it2.hasNext())
				listReturn.add(it2.next());
		}
		
		if (!it2.hasNext()) {
			while(it1.hasNext())
				listReturn.add(it1.next());
		}
		
		return listReturn;
	}
	
}
enum ItemToAdd {
	FIRST, SECOND, BOTH;
}