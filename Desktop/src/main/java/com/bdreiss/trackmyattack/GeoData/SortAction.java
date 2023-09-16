package main.java.com.bdreiss.trackmyattack.GeoData;

import java.awt.geom.Point2D;

public interface SortAction {
	
	PointComparator getBigger(Point2D.Double point1, Point2D.Double point2);
}
