import java.util.*;

import java.io.*;
public class RoadTrip {
	// HASHMAPS and priorityQueue for storing our graph
	PriorityQueue<city> cityEdge = new PriorityQueue<city>();// contains all cities
	LinkedHashMap<String, city> cityEdges = new LinkedHashMap<String, city>();//used for getting cities with runtime O(1)
	HashMap<String, city> cityAttracts = new HashMap<String, city>(); //our list of cities containing attractions
	//files to be handled + bufferedreader declaration
	String path = "/Users/Licea/eclipse-workspace/roadtrip/roads.csv"; // roads file path
	String path2 = "/Users/Licea/eclipse-workspace/roadtrip/attractions.csv";//attractions file path
	BufferedReader br;
	//Lists containing data from file
	List<String> fileinfo = new ArrayList<String>(); // roads.csv represented as strings
	List<String> attractionsinfo = new ArrayList<String>();// attractions.csdv represented as list of strings
	//data to be used and filled by Djikstras algorithm
	int V = cityEdges.size();
	HashMap<city, Integer> dist = new HashMap<city, Integer>();
	Set<city> unchecked = new HashSet<city>();
	ArrayList<city> checked = new ArrayList<city>();
	
	
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
	public void vertexAndEdges() { // creates a graph of with vertices = cities and edges = roads
		this.openfile();
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
	}
	
	public void addattractions() { // adds attractions to appropriate cities
		this.readattractions();
		for(int i = 1; i < attractionsinfo.size(); i++) {
			String arr[] = attractionsinfo.get(i).split(",");
			city g = cityEdges.get(arr[1]);
			g.attractions.add(arr[0]);
			cityEdges.replace(g.name, g);
			cityAttracts.put(g.name, g); // if we have an attractions, add city to a smaller list of all cities containing attractions
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
		HashSet<String> nqueue = new HashSet(attractions);
		if(attractions.isEmpty()) {
			return route(start, end);
		}
		while (!nqueue.isEmpty()){
			
		}
		return MyRoute;
		
	}
	public List<city> route(String start, String end){	
		city begin = cityEdges.get(start);
		city toCheck = new city();
		begin.weight = 0;
		cityEdges.replace(begin.name, begin);
		//cityEdge.add(begin); //since this is a priority queue, value with weight = 0 is first thing we pop
		cityEdge.addAll(cityEdges.values()); // add all HashMap elements to PQ
		while(!cityEdge.isEmpty()) { //while PQ is not empty...  checked.size()< V
			city h = cityEdge.remove(); //get highest priority
			//System.out.println(h.name); checking that priority queue is being created properly
			checked.add(h); //add to list of checked nodes
			for(int i = 0; i < h.edge.size(); i++) { // for all edges connected to highest priority city update
				String next = h.edge.get(i).name;
				toCheck = cityEdges.get(next);
				if(toCheck.name == end) {
					toCheck.weight = h.weight + h.edge.get(i).miles;
					cityEdge.removeAll(cityEdge);
					checked.add(toCheck);
					//return checked;
					break;
					}
				else if(toCheck.weight > h.weight + h.edge.get(i).miles && !checked.contains(toCheck)) {
					toCheck.weight = h.weight + h.edge.get(i).miles;
					cityEdge.remove(cityEdges.get(h.edge.get(i).name));
					cityEdge.add(toCheck);
					}
				else {
					//toCheck.weight = h.edge.get(i).miles;
					//cityEdge.remove(cityEdges.get(h.edge.get(i).name));
					//cityEdge.add(toCheck);
					continue;
				}	
			}	
			if(checked.contains(cityEdges.get(end))) {
				break;
			}
		}
		return checked;
		}
	private void neighbours() {
		
	}
	
	
	public static void main(String args[]) {
		RoadTrip r = new RoadTrip();
		r.vertexAndEdges();
		r.addattractions();
		//r.printGraph();
		System.out.println(r.cityEdges.get("San Jose CA").name + " "+ r.cityEdges.get("San Jose CA").weight);
		for(int i = 0; i < r.cityEdges.get("San Jose CA").edge.size(); i++) {
			System.out.println(r.cityEdges.get("San Jose CA").edge.get(i).name + " " + r.cityEdges.get("San Jose CA").edge.get(i).miles);
		}
		List<city> cit = r.route("San Jose CA", "Las Vegas NV");
		System.out.println(r.cityEdges.get("San Jose CA").name + " " + r.cityEdges.get("San Jose CA").weight);
		System.out.println(cit.size());
		for(int i = 0; i < cit.size(); i++) {
			System.out.println(cit.get(i).name + " "+ cit.get(i).weight);
		}
		System.out.println();
		for(int i = 0; i < r.cityEdges.get("San Jose CA").edge.size(); i++) {
			String n = r.cityEdges.get("San Jose CA").edge.get(i).name;
			System.out.println(n + " " + r.cityEdges.get(n).weight);
		}
	}
}
