/*
 * Patrice Douge
 * pdouge
 * 29310296
 * Project 4
 */
/*
 * Test class takes in file, and creates map, finds mwst, and finds directions between two intersections
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFrame;

public class Test {

	public static void main(String [] args) throws FileNotFoundException {
		
		//used to know runtime of program
		long startTime = System.currentTimeMillis();

		File input = new File(args[0]);
		Scanner scanner = new Scanner(input);

		int numIntersects = 0;
		while(scanner.nextLine().startsWith("i")) { //counting number of intersections
			numIntersects++;
		}
		scanner.close();

		String intersectionID;
		double lat, longitude;
		Intersection intersect;

		//booleans for command line arguments
		boolean showMap = false;
		boolean dijkstras = false;
		boolean mwst = false;

		//default intersections
		String startIntersection = "i0";
		String endIntersection = "i1";

		Scanner scanner2 = new Scanner(input);
		Map map = new Map(numIntersects); //create map using the number of intersections

		String currentLine = scanner2.nextLine();
		String [] data;

		//intersections start with "i" - inserting intersections into map
		while(currentLine.startsWith("i")) {

			data = currentLine.split("\t");
			intersectionID = data[1];
			lat = Double.parseDouble(data[2]);
			longitude = Double.parseDouble(data[3]);

			intersect = new Intersection(); //creating intersections
			intersect.distance = Integer.MAX_VALUE;
			intersect.IntersectionID = intersectionID;
			intersect.latitude = lat;
			intersect.longitude = longitude;
			intersect.known = false;

			currentLine = scanner2.nextLine();
			map.insert(intersect); //inserting intersections into map
		}

		String roadID, intersect1, intersect2;
		Intersection a, b;
		double distance;

		//Roads start with "r" - inserting roads into map
		while(currentLine.startsWith("r")) {

			data = currentLine.split("\t");
			roadID = data[1];
			intersect1 = data[2];
			intersect2 = data[3];

			a = Map.intersectLookup(intersect1);
			b = Map.intersectLookup(intersect2);
			distance = Map.intersectDist(a, b);

			//inserting road into map
			map.insert(new Road(roadID, intersect1, intersect2, distance));

			if(scanner2.hasNextLine() == false) {
				break;
			}

			currentLine = scanner2.nextLine();

		}

		//checks the command line arguments
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("-show")) {
				showMap = true;
			}

			if(args[i].equals("-directions")) {
				dijkstras = true;

				startIntersection = args[i+1];
				endIntersection = args[i+2];
			}

			if(args[i].equals("-meridianmap")) {
				mwst = true;
			}

		}

		//if user wants to show map of file
		if(showMap == true) {

			JFrame frame = new JFrame("Map");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			frame.getContentPane().add(new MapGUI(Map.roads, Map.intersectMap, Map.minLat, Map.maxLat, Map.minLong, Map.maxLong));
			frame.pack();
			frame.setVisible(true);
		}

		//if user wants directions
		if(dijkstras == true) {

			map.dijkstra(startIntersection);

			System.out.println();
			System.out.println("The shortest path from " + startIntersection + " to " + endIntersection + " is: ");
			System.out.println(Map.dijkstraPath(endIntersection));

			System.out.println("Total distance from " + startIntersection + " to " + endIntersection + " is: " + Map.dijkstraPathLength() + " miles.");
		}

		//if user wants meridian map
		if(mwst == true) {

			map.kruskals();

			System.out.println();
			System.out.println("Roads taken to create minimum weight spanning tree: " + "\n");

			for(Road r : Map.MWST) {
				System.out.println(r.roadID);
			}

		}

		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime-startTime;

		System.out.println();
		System.out.println("Runtime: " + elapsedTime/1000 + " seconds.");


		scanner2.close();
	}

}