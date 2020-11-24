import java.util.*;

import java.io.*;
public class RoadTrip {
	HashMap<String, city> cityEdges = new HashMap<String, city>();
	String path = "C:\\Users\\antho\\eclipse-workspace\\RoadTrip\\roads.csv";
	String path2 = "C:\\Users\\antho\\eclipse-workspace\\RoadTrip\\attractions.csv";
	BufferedReader br;
	List<String> fileinfo = new ArrayList<String>();
	List<String> attractionsinfo = new ArrayList<String>();
	
	
	public void openfile() {
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
	
	public void readattractions() {
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
	
	public void addattractions() {
		this.readattractions();
		for(int i = 1; i < attractionsinfo.size(); i++) {
			String arr[] = attractionsinfo.get(i).split(",");
			city g = cityEdges.get(arr[1]);
			g.attractions.add(arr[0]);
			cityEdges.replace(g.name, g);
		}
	}
	public void printGraph() {
		for(String city: cityEdges.keySet()) {
			System.out.println("city: "+ city);
			for(int i = 0; i <cityEdges.get(city).edges.size(); i++) {
				System.out.println(cityEdges.get(city).edges.get(i).name+ " distance miles: " + cityEdges.get(city).edges.get(i).miles);
			}
			for(int i = 0; i < cityEdges.get(city).attractions.size(); i++) {
				System.out.println("VISIT WORLD FAMOUS: "+ cityEdges.get(city).attractions.get(i));
			}
		}
	}
	
	
	public static void main(String args[]) {
		RoadTrip r = new RoadTrip();
		r.vertexAndEdges();
		r.addattractions();
		r.printGraph();
	}
}
