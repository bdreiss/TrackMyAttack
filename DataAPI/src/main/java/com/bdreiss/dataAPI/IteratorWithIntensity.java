package main.java.com.bdreiss.dataAPI;

import java.util.Iterator;

/**
 * Class that represents an Iterator with Intensity.
 * 
 * @author bernd
 *
 */

public class IteratorWithIntensity implements Iterator<Datum>{

	private Iterator<Datum> originalIterator;
	
	IteratorWithIntensity(Iterator<Datum> originalIterator){
		this.originalIterator = originalIterator;
	}

	@Override
	public boolean hasNext() {
		return originalIterator.hasNext();
	}

	@Override
	public Datum next() {
		return originalIterator.next();
	}
	
	

}
