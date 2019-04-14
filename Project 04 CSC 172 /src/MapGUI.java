/*
 * Patrice Douge
 * pdouge
 * 29310296
 * Project 4
 */
/*
 * MapGUI class graphs graphs map
 */
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MapGUI extends JPanel{

	public static ArrayList<Road> roads;
	public static HashMap<String, Intersection> intersectionMap;
	public static double minLat, minLong, maxLat, maxLong;
	public static double xScale, yScale;

	public MapGUI(ArrayList<Road> roads, HashMap<String, Intersection> intersectMap, double minimumLat, double maximumLat, double minimumLong, double maximumLong) {
		
		setPreferredSize(new Dimension(800, 800));
		MapGUI.roads = roads;
		MapGUI.intersectionMap = intersectMap;
		minLat = minimumLat;
		maxLat = maximumLat;
		minLong = minimumLong;
		maxLong = maximumLong;
	}

	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		g2.setColor(Color.BLACK);

		xScale = this.getWidth() / (maxLong - minLong);
		yScale = this.getHeight() / (maxLat - minLat);

		Intersection intersect1, intersect2;

		double x1, y1, x2, y2;
		
		//creating graph
		for(Road r : roads) {

			reScale();

			intersect1 = intersectionMap.get(r.intersection1);
			intersect2 = intersectionMap.get(r.intersection2);

			x1 = intersect1.longitude;
			y1 = intersect1.latitude;
			x2 = intersect2.longitude;
			y2 = intersect2.latitude;

			g2.draw(new Line2D.Double((x1-minLong) * xScale, getHeight() - ((y1 - minLat) * yScale), 
					(x2-minLong) * xScale, getHeight() - ((y2 - minLat) * yScale)));

		}

		//graphing directions using dijkstra's algorithm
		if(Map.dijkstraDirection != null) {

			g2.setColor(Color.RED);

			for(int i = 0; i < Map.dijkstraDirection.length - 1; i++) {

				x1 = Map.dijkstraDirection[i].longitude;
				y1 = Map.dijkstraDirection[i].latitude;
				x2 = Map.dijkstraDirection[i+1].longitude;
				y2 = Map.dijkstraDirection[i+1].latitude;

				g2.draw(new Line2D.Double((x1-minLong) * xScale, getHeight() - ((y1 - minLat) * yScale), 
						(x2-minLong) * xScale, getHeight() - ((y2 - minLat) * yScale)));

			}


		}

		//graphing meridian map using minimum weigh spanning tree
		if(Map.MWST != null) {
			for(Road r : Map.MWST) {

				g2.setColor(Color.BLUE);

				intersect1 = intersectionMap.get(r.intersection1);
				intersect2 = intersectionMap.get(r.intersection2);

				x1 = intersect1.longitude;
				y1 = intersect1.latitude;
				x2 = intersect2.longitude;
				y2 = intersect2.latitude;

				g2.draw(new Line2D.Double((x1-minLong) * xScale, getHeight() - ((y1 - minLat) * yScale), 
						(x2-minLong) * xScale, getHeight() - ((y2 - minLat) * yScale)));

			}
		}
	}
	
	//method to scale graph to frame
	public void reScale() {

		xScale = this.getWidth() / (maxLong - minLong);
		yScale = this.getHeight() / (maxLat - minLat);

	}



}
