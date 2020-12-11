import java.util.*;

import java.io.*;
public class RoadTrip {
	// HASHMAPS and priorityQueue for storing our graph
	PriorityQueue<city> cityEdge = new PriorityQueue<city>();// contains all cities
	HashMap<String, city> cityAttracts = new HashMap<String, city>(); //list of cities w/attractions w/attraction as key and city as value
	//files to be handled + bufferedreader declaration
	String path = "C:\\Users\\antho\\eclipse-workspace\\RoadTrip\\src\\roads.csv"; // roads file path
	String path2 = "C:\\Users\\antho\\eclipse-workspace\\RoadTrip\\src\\attractions.csv";//attractions file path
	BufferedReader br;
	//Lists containing data from file
	List<String> fileinfo = new ArrayList<String>(); // roads.csv represented as strings
	List<String> attractionsinfo = new ArrayList<String>();// attractions.csv represented as list of strings
	LinkedHashMap<String, city> cityEdges = vertexAndEdges();
	int V = cityEdges.size();
	HashMap<city, Integer> dist = new HashMap<city, Integer>();
	
	
	public void openfile() { // method for opening roads.csv
		try {
		 br = new BufferedReader(new FileReader(path));
		 String line;
		 while((line= br.readLine())!= null) {
			 fileinfo.add(line);
		 }
		 br.close();
		}
		catch(Exception e){
			System.out.println("file not found");
		}
	}
	
	public void readattractions() { // methods for opening attractions.csv
		try {
			br = new BufferedReader(new FileReader(path2));
			String l;
			while((l = br.readLine())!=null) {
				attractionsinfo.add(l);
			}
			br.close();
		}
		catch(Exception e) {
			System.out.println("couldn't open file");
		}
	}
	
	
	public LinkedHashMap<String, city> vertexAndEdges() { // creates a graph of vertices = cities and edges = roads
		this.openfile();
		LinkedHashMap<String, city> cityEdges = new LinkedHashMap<String, city>();
		for (int i = 0; i < fileinfo.size(); i++) {
			String city1, city2;
			int miles, minutes;
			String [] arr = fileinfo.get(i).split(",");
			city1 = arr[0];
			city2 = arr[1];
			miles = Integer.parseInt(arr[2]);
			minutes = Integer.parseInt(arr[3]);
			if(!cityEdges.containsKey(city1)) {//if first city doesn't exist on graph yet
				city n = new city(city1); // create a city
				n.newEdge(city2, miles, minutes);//add its edge
				cityEdges.put(n.name, n);//place it in hashmap
				if(!cityEdges.containsKey(city2)) {// if we're also missing our second city...
					city m = new city(city2); // create a new city
					m.newEdge(city1, miles, minutes); // add an edge for city
					cityEdges.put(m.name, m); // place city in hashmap
				}
				else if(cityEdges.containsKey(city2)) {// if we already have our second city in the map...
					city h = cityEdges.get(city2); // get our city from the map so we can add an edge
					h.newEdge(city1, miles, minutes); // add the edge to the city class
					cityEdges.replace(h.name,h); // add the edge to the map.
				}
			}
			else if(cityEdges.containsKey(city1)) { // else if we do have out first city in the map...
				city k = cityEdges.get(city1); //find city in hashmap
				k.newEdge(city2, miles, minutes); // add edge to city
				cityEdges.replace(k.name, k); // update the city in hashmap to reflect new edge
				if(!cityEdges.containsKey(city2)) {// if we're also missing our second city...
					city m = new city(city2); // create a new city
					m.newEdge(city1, miles, minutes); // add an edge for city
					cityEdges.put(m.name, m); // place city in hashmap
				}
				else if(cityEdges.containsKey(city2)) {// if we already have our second city in the map...
					city h = cityEdges.get(city2); // get our city from the map so we can add an edge
					h.newEdge(city1, miles, minutes); // add the edge to the city class
					cityEdges.replace(h.name,h); // add the edge to the map.
				}
			}
		}
		return cityEdges;
	}
	
	
	public void addattractions() { // adds attractions to appropriate cities
		this.readattractions();
		for(int i = 1; i < attractionsinfo.size(); i++) {
			String arr[] = attractionsinfo.get(i).split(",");
			city g = cityEdges.get(arr[1]);
			g.attractions.add(arr[0]);
			cityEdges.replace(g.name, g);
			cityAttracts.put(arr[0], g); // if we have an attraction, add city to a smaller list cities
		}
	}
	
	
	public void printGraph() { //prints the information held in the HashMap
		for(String city: cityEdges.keySet()) {
			System.out.println("city: "+ city);
			for(int i = 0; i <cityEdges.get(city).edge.size(); i++) {
				System.out.println(cityEdges.get(city).edge.get(i).name+ " distance miles: " + cityEdges.get(city).edge.get(i).miles);
			}
			for(int i = 0; i < cityEdges.get(city).attractions.size(); i++) {
				System.out.println("VISIT WORLD FAMOUS: "+ cityEdges.get(city).attractions.get(i));
			}
		}
	}
	
	
	public List<city> route(String start, String end, List <String> attractions){ // meant to find shortest path between start 
		List<city> MyRoute = new ArrayList<city>();								  // to all attractions, and finally finishing at the end
		HashSet<String> nqueue = new HashSet(attractions);                        // AKA: MAIN ROUTE FUNCTION
		if(attractions.isEmpty() || attractions.equals(null)) {                  
			return route(start, end);
		}
		while (!nqueue.isEmpty()){
			attractions = new ArrayList(nqueue);
			city toVisit = closest( attractions);
			MyRoute.addAll(route(start, toVisit.name));
			nqueue.remove(toVisit.attractions.get(0));
			attractions.remove(new String(toVisit.attractions.get(0)));
			start = toVisit.name;
		}
		MyRoute.addAll(route(start,end));
		return MyRoute;
		}
	
	
	public city closest( List<String>attractions){  // gets closest attraction in a list of attractions
		List<city> toCheck = new ArrayList<city>();
		for(int i = 0; i < attractions.size(); i++) {
			toCheck.add(cityAttracts.get(attractions.get(i)));
		}
		city g = toCheck.get(0);
		for(int i = 0; i < toCheck.size();i++) {
			if (toCheck.get(i).weight>= g.weight) {
				g = toCheck.get(i);
			}
		}
		return g;	
	}
	
	
	private List<city> route(String start, String end){	
		LinkedHashMap<String, city> graph = vertexAndEdges();//graph we will use. must be re-initialized every time we look for something new
		ArrayList<city> checked = new ArrayList<city>(); // keeps track of all items already checked
		city begin = graph.get(start);
		city toCheck = new city();
		begin.weight = 0;// in order to give start value highest priority
		graph.replace(begin.name, begin);// append so value is pullable
		cityEdge.addAll(graph.values()); // add all HashMap elements to PQ
		while(!cityEdge.isEmpty()) { //while PQ is not empty...  
			city h = cityEdge.remove(); //get highest priority
			checked.add(h); //add to list of checked nodes
			for(int i = 0; i < h.edge.size(); i++) { // for all edges connected to highest priority city update
				String next = h.edge.get(i).name; // name of next city to look at
				toCheck = graph.get(next);//gets next city 
				if(toCheck.name == end) {// if this is our ending point....
					toCheck.weight = h.weight + h.edge.get(i).miles; //edit the weight
					cityEdge.removeAll(cityEdge);// delete everything to break loop
					checked.add(toCheck);// add last city to list of checked items
					break;
					}
				else if(toCheck.weight > h.weight + h.edge.get(i).miles && !checked.contains(toCheck)) {//otherwise if its not our ending city...
					toCheck.weight = h.weight + h.edge.get(i).miles;// edit our weight
					cityEdge.remove(graph.get(h.edge.get(i).name));
					cityEdge.add(toCheck);// remove and readd modified city to PQ
					}
				else {
					continue;
				}	
			}	
			if(checked.contains(graph.get(end))) {//if our ending city has already been found...
				break;
			}
		}
		return finale(checked);// return our final PATH
		}
	
	
	private List<city> finale( List<city> c) { //returns our final desired path
		city last = c.get(c.size() -1);
		city first = c.get(0);
		Stack <city> s = new Stack<city>();
		s.add(last);
		for(int j = c.size()-1; j > 0; j--) {
			ArrayList<city> track = new ArrayList<city>();
			for(int i = 0; i < last.edge.size(); i++) {
				if ( c.get(j).name.equals(last.edge.get(i).name)&&cityEdges.get(last.edge.get(i).name).weight >last.weight ) {// if the city were checking contains an edge in the list returned                                                                                                             
					track.add(c.get(j));   //add to list of things we could possibly return                                                                                   // the city on other end of edge has smaller weight than the last thing on
					c.remove(j);                                                                                            // list to be returned...
					}	
			else {
				continue;
			}	
		}
			if(!track.isEmpty()) {// id the last city we checked had edges on the list returned from route...
			city g = track.get(0);
			for(int b= 0; b < track.size();b++) {
				if(g.weight< track.get(b).weight) {// find the edge whos city has smallest weight
					continue;
				}
				else 
					g = track.get(b);
			}
			s.add(g); // add to stack 
			last = g; // set the last thing we looked at = g
			}
		}
		List<city>r = new ArrayList<city>();// actual list we want to return
		while(!s.isEmpty()) {// while stack has things to return
			r.add(s.pop());// add them to list
		}
		r.add(0,first);// puts starting city at the front of list
		return r;
		}
	
	
	public static void main(String args[]) {
		RoadTrip r = new RoadTrip();
		r.vertexAndEdges();
		r.addattractions();
		List<String> att= Arrays.asList("Whale and Dolphin Watching on the Delaware Bay","Zion National Park");// "National Elk Refuge","Whale and Dolphin Watching on the Delaware Bay"
		List<city> cit = r.route("New York NY", "Los Angeles CA",att);
		System.out.println(cit.size());
		System.out.println();
		System.out.println("your final path:");
		for(int i = 0; i < cit.size(); i++) {
			System.out.println(cit.get(i).name + " "+ cit.get(i).weight);
		}
		
		cit = r.route("Los Angeles CA", "New York NY");
			}
}
