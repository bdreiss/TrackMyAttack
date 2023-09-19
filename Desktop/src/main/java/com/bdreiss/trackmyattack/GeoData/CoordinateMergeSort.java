package main.java.com.bdreiss.trackmyattack.GeoData;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CoordinateMergeSort {
	
	public static List<Station> sort(List<Station> list) {
				
		if (list.size()<=1)
			return list;
		
		
		int m = list.size()/2;
		List<Station> listLeft = list.subList(0, m);
		List<Station> listRight = list.subList(m, list.size());

		List<Station> listReturn = 
					 (merge(sort(listLeft), sort(listRight)));
		
		return listReturn;
	}
	
	private static List<Station> merge(List<Station> list1, List<Station> list2){
		
		List<Station> listReturn = new ArrayList<Station>();

		
		Iterator<Station> it1 = list1.iterator();
		Iterator<Station> it2 = list2.iterator();
		
		if (!it1.hasNext() && !it2.hasNext())
			return listReturn;
		if (!it1.hasNext())
			return list2;
		if (!it2.hasNext())
			return list1;
		
		Station current1 = it1.next();
		Station current2 = it2.next();

		
		while ((current1 == null || current1.getX() != Double.POSITIVE_INFINITY) && (current2== null || current2.getX() != Double.POSITIVE_INFINITY)) {

			Double diffX = (current1==null?Double.MAX_VALUE:current1.getX()) - (current2==null?Double.MAX_VALUE:current2.getX());
			ItemToAdd itemToAdd = ItemToAdd.BOTH;
			
			if (diffX == 0) {
				Double diffY = (current1==null?Double.MAX_VALUE:current1.getY()) - (current2==null?Double.MAX_VALUE:current2.getY());
				
				if (diffY == 0) itemToAdd = ItemToAdd.BOTH;
				
				if (diffY < 0) itemToAdd = ItemToAdd.FIRST;						
				
				if (diffY > 0) itemToAdd = ItemToAdd.SECOND;

			}
			
			if (diffX < 0) itemToAdd = ItemToAdd.FIRST;						
			
			if (diffX > 0) itemToAdd = ItemToAdd.SECOND;
				
			

			switch (itemToAdd) {
				case FIRST:
					listReturn.add(current1);
					current1 = it1.hasNext()? it1.next():new NullStation();		
					break;
				case SECOND:
					listReturn.add(current2);
					current2 = it2.hasNext()? it2.next():new NullStation();
					break;
				case BOTH:
					listReturn.add(current1);
					current1 = it1.hasNext()? it1.next():new NullStation();
					listReturn.add(current2);
					current2 = it2.hasNext()? it2.next():new NullStation();
			}
		} 

		if (current1 != null) {
			if (current2 == null)
				listReturn.add(current2);
			else if (current1.getX() == Double.POSITIVE_INFINITY && current2.getX() < Double.POSITIVE_INFINITY)
				listReturn.add(current2);
		}
		
		if (current2 != null) {
			if (current1 == null)
				listReturn.add(current1);
			else if (current2.getX() == Double.POSITIVE_INFINITY && current1.getX() < Double.POSITIVE_INFINITY)
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

	private static class NullStation implements Station{

		@Override
		public Double getX() {
			return Double.POSITIVE_INFINITY;
		}

		@Override
		public Double getY() {
			return getX();
		}
		
	}

}


enum ItemToAdd {
	FIRST, SECOND, BOTH;
}