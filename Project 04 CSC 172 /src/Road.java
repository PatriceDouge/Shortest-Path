/*
 * Patrice Douge
 * pdouge
 * 29310296
 * Project 4
 */
/*
 * Road class contains constructor for a given road 
 * A road has a: roadID, 2 intersects and distance
 */
public class Road {
	
	String roadID;
	String intersection1;
	String intersection2;
	double distance;
	
	//constructor
	public Road(String road, String intersect1, String intersect2, double dist) {
		roadID = road;
		intersection1 = intersect1;
		intersection2 = intersect2;
		distance = dist;
	}

}