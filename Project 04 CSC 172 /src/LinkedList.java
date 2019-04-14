/*
 * Patrice Douge
 * pdouge
 * 29310296
 * Project 4
 */
/*
 * Linked List class contains method that inserts intersections and roads, 
 * and methods that find the cost between the intersections at the head of the list 
 * and the intersections  connected
 */
public class LinkedList {

	public int size;
	public Node head;

	public LinkedList() {
		head = new Node();
		size = 0;
	}

	//method returns size of list
	public int size() {
		return size;
	}

	//method inserts intersection into list
	public void insert(Intersection intersect) {

		if(head.intersection == null) {
			head.intersection = intersect;
		}

		size++;
	}

	//method inserts road into list
	public void insert(Road road) {

		Edge tempEdge = new Edge();
		tempEdge.road = road;

		//isert at the front of the list (after the head)
		tempEdge.next = head.edge;
		head.edge = tempEdge;


	}

	//method checks if all intersections are connected
	public boolean connected(Intersection intersection) {

		Edge tempEdge = head.edge;

		while(tempEdge != null) {

			if(tempEdge.road.intersection1.equals(intersection.IntersectionID) || tempEdge.road.intersection2.equals(intersection.IntersectionID)) {
				return true;
			}

			tempEdge = tempEdge.next;
		}

		return false;

	}

	//method checks if two intersections are connected
	public boolean contains(Intersection intersection) {

		Node temp = head;

		while(temp != null) {

			if(temp.intersection.equals(intersection)) {
				return true;
			}

			temp = temp.next;
		}

		return false;

	}

	//method finds cost between intersections
	public double findCost(Intersection intsection) {

		Edge tempEdge = head.edge;

		//travel down the linked list
		while(tempEdge != null) {

			if(tempEdge.road.intersection1.equals(intsection.IntersectionID) || tempEdge.road.intersection2.equals(intsection.IntersectionID)) {
				return tempEdge.road.distance;
			}

			tempEdge = tempEdge.next;
		}

		return -1;

	}
}