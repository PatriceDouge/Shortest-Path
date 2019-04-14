/*
 * Patrice Douge
 * pdouge
 * 29310296
 * Project 4
 */
/*
 * Map class has a constructor for the map, inserts intersections and edges 
 * onto the map and implements the Dijkstra's algorithm to find the
 * shortest path between two intersectins and calculates minimum spanning tree
 */
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.PriorityQueue;

public class Map {

	public HashMap<String, LinkedList> graph;
	public static int numIntersections;
	public static ArrayList<Road> roads;
	public static PriorityQueue<Road> kruskalsRoads;
	public static Intersection [] dijkstraDirection;
	public static HashMap<String, Intersection> intersectMap;
	public static PriorityQueue<Intersection> unknownIntersectionHeap;
	public static HashMap<String, HashSet<String>> intersectionSets;
	public static ArrayList<Road> MWST; 
	public static double minLat, maxLat, minLong, maxLong;

	//constructor for the Map
	public Map(int numVertices) {

		graph = new HashMap<String, LinkedList>();

		numIntersections = numVertices;
		roads = new ArrayList<Road>();
		intersectMap = new HashMap<String, Intersection>();

		Comparator<Intersection> comparator = new Comparator<Intersection>() { //compares intersections

			@Override
			public int compare(Intersection i1, Intersection i2) {

				if(i1.distance < i2.distance) {
					return -1;
				}
				else {
					return 1;
				}
			}
		};

		//heap of Unknown Intersections
		unknownIntersectionHeap = new PriorityQueue<Intersection>(numVertices, comparator);

		Comparator<Road> comparator2 = new Comparator<Road>() { //used to compare roads

			@Override
			public int compare(Road r1, Road r2) {

				if(r1.distance < r2.distance) {
					return -1;
				}
				else {
					return 1;
				}
			}
		};

		//heap of roads
		kruskalsRoads = new PriorityQueue<Road>(numVertices*3, comparator2);

		//set the minimum and maximum latitudes and longitudes to appropriate starting integer values
		minLat = minLong = Integer.MAX_VALUE;
		maxLat = maxLong = Integer.MIN_VALUE;


	}

	public int size() { //returns intersections in graph
		return graph.size();
	}

	//method that calculates the distance between two intersections
	public static double intersectDist(Intersection intersect1, Intersection intersect2) {

		return tudeDist(intersect1.latitude, intersect1.longitude, intersect2.latitude, intersect2.longitude);

	}

	//method finds cost between two intersections
	public double findCost(Intersection intersect1, Intersection intersect2) {

		LinkedList temp = graph.get(intersect1.IntersectionID);
		return temp.findCost(intersect2);
	}

	//method checks if two intersections are connected
	public boolean connected(Intersection intersect1, Intersection intersect2) {

		//get the linked list for the first intersection
		LinkedList temp = graph.get(intersect1.IntersectionID);

		//call connected on the linked list
		return temp.connected(intersect2);

	}

	//method inserts intersections into graph by resetting the longitude and latitude of intersections
	public void insert(Intersection intersect) {

		if(intersect.longitude < minLong) {
			minLong = intersect.longitude;
		}

		if(intersect.longitude > maxLong) {
			maxLong = intersect.longitude;
		}
		if(intersect.latitude < minLat) {
			minLat = intersect.latitude;
		}

		if(intersect.latitude > maxLat) {
			maxLat = intersect.latitude;
		}

		//adding new intersection into hash map and heap
		intersectMap.put(intersect.IntersectionID, intersect);
		unknownIntersectionHeap.add(intersect);

		//inserting intersection to linkedlist
		LinkedList temp = new LinkedList();
		temp.insert(intersect);

		//finally add intersection to graph
		graph.put(intersect.IntersectionID, temp);
	}

	//method inserts roads into graph
	public void insert(Road e) {

		//getting linkedlist of 
		LinkedList intersect1 = graph.get(e.intersection1);
		LinkedList intersect2 = graph.get(e.intersection2);


		//inserts the road into each linked list
		intersect1.insert(e);
		intersect2.insert(e);

		//adds the road to the heap and array of roads
		kruskalsRoads.add(e);
		roads.add(e);
	}

	//method returns intersection using the intersection ID
	public static Intersection intersectLookup(String intersectionID) {

		return intersectMap.get(intersectionID);

	}

	//method to get disjkstra's path
	public static String dijkstraPath(String intersectionID) {

		Intersection temp = intersectMap.get(intersectionID);

		//array equal to nodes in map of intersections
		String [] path = new String[intersectMap.size()];

		int counter = 0;

		//add vertexes to array until null - reach front
		while(temp.path != null) {
			path[counter] = temp.IntersectionID;
			temp = temp.path;
			counter++;
		}
		path[counter] = temp.IntersectionID;
		int totalPath = 0;

		//goes through path array to calculate total length of path
		for(int i = 0; i < path.length; i++) {
			if(path[i] == null) {
				totalPath = i;
				break;
			}
		}

		dijkstraDirection = new Intersection [totalPath]; 
		for(int i = 0; i < totalPath; i++) { //graphs directions
			dijkstraDirection[i] = intersectMap.get(path[i]);
		}

		String finalPath = "";

		//string of the path created from start to end of the vertex
		for(int i = counter ; i > -1; i--) {
			finalPath = finalPath + path[i] + "\n";
		}

		return finalPath; //final path used for dijkstra's algorithm
	}

	//method creates hashsets
	public void createHashSet() {

		intersectionSets = new HashMap<String, HashSet<String>>();

		HashSet<String> intersections;

		Iterator<Entry<String, LinkedList>> iterator = graph.entrySet().iterator();

		//iterates through graph, creating a new hashset
		while (iterator.hasNext()) {
			HashMap.Entry<String, LinkedList> pair = (HashMap.Entry<String, LinkedList>) iterator.next();
			intersections = new HashSet<String>();
			intersections.add(pair.getKey());
			intersectionSets.put(pair.getKey(), intersections);

		}

	}

	//method to determine the minimum weight spanning tree
	public void kruskals() {

		createHashSet();

		MWST = new ArrayList<Road>(); //minimum weight spanning tree

		//intersections of currentRound
		Road currentRoad;
		HashSet<String> u;
		HashSet<String> v;

		//while heap of roads not empty, create MWST
		while(kruskalsRoads.size() != 0) {

			currentRoad = kruskalsRoads.remove();
			u = intersectionSets.get(currentRoad.intersection1);
			v = intersectionSets.get(currentRoad.intersection2);

			if(!u.equals(v)) {

				MWST.add(currentRoad);
				u.addAll(v);

				for(String intersectionID: u) {
					intersectionSets.put(intersectionID, u);
				}
			}
		}
	}

	//method determines total path length between two intersections
	public static double dijkstraPathLength() {

		return dijkstraDirection[0].distance * 0.000621371; //meters -> miles
	}

	//determines smallest UNKNOWN vertex
	public static Intersection smallestUnknownVertex() {

		Intersection temp = unknownIntersectionHeap.remove();
		return intersectMap.get(temp.IntersectionID);

	}

	//dijkstra's algorithm
	public void dijkstra(String intersectionID) {

		Intersection startIntersect = intersectMap.get(intersectionID); //getting starting intersection
		unknownIntersectionHeap.remove(startIntersect); //remove starting intersection
		startIntersect.distance = 0; //set distance to 0
		unknownIntersectionHeap.add(startIntersect); //add starting intersection again

		double cost;
		int unknownVertices = intersectMap.size();
		while(unknownVertices > 0) {

			//getting unknown vertex with smallest distance from heap of intersections
			Intersection temp = smallestUnknownVertex();
			temp.known = true;
			unknownVertices--;

			//getting current unknown vertex with smallest distance from heap of intersections 
			LinkedList currentVertex = graph.get(temp.IntersectionID);
			Edge currentRoad = currentVertex.head.edge;
			Intersection currentIntersection;

			while(currentRoad != null) {

				//getting intersection
				if(currentRoad.road.intersection1.equals(temp.IntersectionID)) {
					currentIntersection = intersectMap.get(currentRoad.road.intersection2);
				}
				else {
					currentIntersection = intersectMap.get(currentRoad.road.intersection1);
				}

				//traversing through linked list, if unknownfind cost to adjacent intersection
				if(currentIntersection.known == false) {

					cost = findCost(temp, currentIntersection);
					if(temp.distance + cost < currentIntersection.distance) {

						//changing value of intersection then adding it back to heap with new value
						unknownIntersectionHeap.remove(currentIntersection);
						currentIntersection.distance = temp.distance + cost;
						currentIntersection.path = temp;
						unknownIntersectionHeap.add(currentIntersection);
					}
				}
				currentRoad = currentRoad.next;
			}
		}
	}


	//Haversine Formula - found on https://rosettacode.org/wiki/Haversine_formula to calculate 
	//the distance between two pairs of longitude and latitude
	public static double tudeDist(double lat1, double long1, double lat2, double long2) {

		int earthRadius = 6371000;
		lat1 = Math.toRadians(lat1);
		long1 = Math.toRadians(long1);
		lat2 = Math.toRadians(lat2);
		long2 = Math.toRadians(long2);

		double latDist = lat2 - lat1;
		double longDist = long2 - long1;

		double a = (Math.sin(latDist/2) * Math.sin(latDist/2)) + (Math.cos(lat1) * Math.cos(lat2) * Math.sin(longDist/2) * Math.sin(longDist/2));

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		return earthRadius * c;

	}
}
