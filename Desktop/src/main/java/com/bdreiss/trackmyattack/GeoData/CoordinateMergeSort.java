package main.java.com.bdreiss.trackmyattack.GeoData;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CoordinateMergeSort {

	
	public static List<Point2D.Double> sort(List<Point2D.Double> list) throws SortActionException {
	
		if (list.size()<=1)
			return list;
		
		list = sortWithAction(list,(p1, p2)-> {
			if (p1.x==p2.x)
				return PointComparator.SAME;
			if (p1.x < p2.x)
				return PointComparator.FIRST;
			return PointComparator.SECOND;
		});
		
		list = sortWithAction(list,(p1, p2)-> {
			if (p1.y==p2.y)
				return PointComparator.SAME;
			if (p1.y < p2.y)
				return PointComparator.FIRST;
			return PointComparator.SECOND;
		});
		return list;
		
	}
	
	private static List<Point2D.Double> sortWithAction(List<Point2D.Double> list,SortAction sortAction) throws SortActionException {
				
		if (list.size()<=1)
			return list;
		
		
		int m = list.size()/2;
		List<Point2D.Double> listLeft = list.subList(0, m);
		List<Point2D.Double> listRight = list.subList(m, list.size());

//		Main.printList(list);
//		System.out.println(m);
//		Main.printList(listLeft);
//		Main.printList(listRight);
		
		List<Point2D.Double> listReturn = 
					 (merge(sortWithAction(listLeft,sortAction),
					 sortWithAction(listRight, sortAction), 
					 sortAction));
		return listReturn;
	}
	
	private static List<Point2D.Double> merge(List<Point2D.Double> list1, List<Point2D.Double> list2, SortAction sortAction) throws SortActionException{
		
		Main.printList(list1);
		Main.printList(list2);
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

		do {


			switch (sortAction.getBigger(current1, current2)) {
			case SAME:
				listReturn.add(current1);
				listReturn.add(current2);
				if (it1.hasNext())
					current1 = it1.next();
				if (it2.hasNext())
					current2 = it2.next();
				break;
			case FIRST:
				listReturn.add(current1);
				if (it1.hasNext())
					current1 = it1.next();
				break;
			case SECOND:
				listReturn.add(current2);
				if (it2.hasNext())
					current2 = it2.next();
				break;
			default:
				throw new SortActionException("SortAction was not well implemented. Must return one of PointComparators SAME, FIRST or SECOND");
			}			
				
		} while (it1.hasNext() && it2.hasNext());
		
		if (!it1.hasNext()) {
			while(it2.hasNext())
				listReturn.add(it2.next());
		}
		
		if (!it2.hasNext()) {
			while(it1.hasNext())
				listReturn.add(it1.next());
		}
		
		System.out.print("Returning: ");
		Main.printList(listReturn);
		
		return listReturn;
	}
	
}
