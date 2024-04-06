package net.berndreiss.trackmyattack.GeoData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class that implements sort(List<Station>) and returns the same list sorted by
 * X coordinates and additionally sorted by Y coordinates, if X coordinates are
 * the same. Supports null values in the list.
 */

//TODO: create unit test for sort(List<Station>)

public class CoordinateMergeSort {

	/**
	 * Sorts a list of stations.
	 * 
	 * @param list List<Station> to be sorted
	 * @return sorted List<Station>
	 */
	public static List<Station> sort(List<Station> list) {

		// if list only has one item or is empty, just return it
		if (list.size() <= 1)
			return list;

		// sort left and right half and merge them
		int m = list.size() / 2;
		List<Station> listLeft = list.subList(0, m);
		List<Station> listRight = list.subList(m, list.size());

		List<Station> listReturn = (merge(sort(listLeft), sort(listRight)));

		// return merged list
		return listReturn;
	}

	// merge two lists
	private static List<Station> merge(List<Station> list1, List<Station> list2) {

		List<Station> listToReturn = new ArrayList<Station>();

		Iterator<Station> it1 = list1.iterator();
		Iterator<Station> it2 = list2.iterator();

		// if both lists are empty, return empty list
		if (!it1.hasNext() && !it2.hasNext())
			return listToReturn;
		// if first list is empty, return second one
		if (!it1.hasNext())
			return list2;
		// if second list is empty, return first one
		if (!it2.hasNext())
			return list1;

		// variables to keep track of current station in the lists respectively ->
		// initialize with first value
		Station current1 = it1.next();
		Station current2 = it2.next();

		// while current stations are not of type NullStation representing the end of
		// the list, continue going through both lists
		while (!(current1 instanceof NullStation) && !(current2 instanceof NullStation)) {

			// compare X values: if stations are null -> assign MAX_VALUE to put them at the
			// end
			Double diffX = (current1 == null ? Double.MAX_VALUE : current1.getLatitude())
					- (current2 == null ? Double.MAX_VALUE : current2.getLatitude());

			// keep track of how many items are to be added
			ItemToAdd itemToAdd = ItemToAdd.BOTH;

			if (diffX == 0) {
				// if X values are the same, compare Y values
				Double diffY = (current1 == null ? Double.MAX_VALUE : current1.getLongitude())
						- (current2 == null ? Double.MAX_VALUE : current2.getLongitude());

				// if Y values are the same add both stations to return list
				if (diffY == 0)
					itemToAdd = ItemToAdd.BOTH;

				// else add first or second station to return list
				if (diffY < 0)
					itemToAdd = ItemToAdd.FIRST;
				if (diffY > 0)
					itemToAdd = ItemToAdd.SECOND;

			}

			// if X values are not equal add first or second station to return list
			if (diffX < 0)
				itemToAdd = ItemToAdd.FIRST;
			if (diffX > 0)
				itemToAdd = ItemToAdd.SECOND;

			// switch cases and execute adding stations to return list
			// if first one is added only move it1 forward
			// if second one is added move it2 forward
			// if both are added move both Iterators forward

			// for each case make the current station the next from the iterator if it has
			// next
			// make it a NullStation marking the end of the list otherwise
			switch (itemToAdd) {
			case FIRST:
				listToReturn.add(current1);
				current1 = it1.hasNext() ? it1.next() : new NullStation();
				break;
			case SECOND:
				listToReturn.add(current2);
				current2 = it2.hasNext() ? it2.next() : new NullStation();
				break;
			case BOTH:
				listToReturn.add(current1);
				current1 = it1.hasNext() ? it1.next() : new NullStation();
				listToReturn.add(current2);
				current2 = it2.hasNext() ? it2.next() : new NullStation();
			}
		}

		//add "left over" values 
		if (!(current1 instanceof NullStation))
			listToReturn.add(current1);
		if (!(current2 instanceof NullStation))
			listToReturn.add(current2);

		//add remaining values from list that is not empty
		while (it1.hasNext())
			listToReturn.add(it1.next());		
		while (it2.hasNext())
			listToReturn.add(it2.next());


		return listToReturn;
	}

	//Station representing end of list
	private static class NullStation implements Station {

		@Override
		public Double getLatitude() {
			return null;
		}

		@Override
		public Double getLongitude() {
			return null;
		}

	}

	//enum for marking which items to add in each step of the merge loop
	enum ItemToAdd {
		FIRST, SECOND, BOTH;
	}
}

