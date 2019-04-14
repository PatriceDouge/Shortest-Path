/*
 * Patrice Douge
 * pdouge
 * 29310296
 * Project 4
 */

For project 4, my program reads in data with intersections and roads of a map and makes a map and calculates the shortest path between two intersections and the minimum weight spanning tree of the graph. I use a linked list to store the intersections at the node head and reference an edge which stores the roads that are connected to the intersection. I then add all the roads to an array list to then draw the graph. I used Dijkstra's algorithm to compute the shortest path between two intersections. To compute the minimum weight spanning tree I used Kruskal's algorithm. 

The biggest obstacle I faced was finding a way to store the data. At first, I thought I could just used what I implemented in lab 16, however realized that it would be best to use what was explained above. 

To compute the runtime, I used a timer that starts when the program starts running. 
The expected runtime of plotting the map is O(roads). Moreover, the runtime is dependent on how many roads are in the data set. 
For finding the shortest path between two intersections I expect the runtime to be O(intersections) because Dijkstra's algorithm is depended on the amount of edges - intersections.
Lastly the expected runtime for generating the minimum weight spanning tree is 0(roads), because its dependent on how many roads the program has to process. 
