import java.util.*;
//city class
public class city implements Comparable<city> {
	String name;
	List<edges> edge = new ArrayList<edges>();
	List<String> attractions = new ArrayList<String>();
	int weight = Integer.MAX_VALUE;
	boolean visited = false;
	public edges smallest;
	
	city(){
		name = null;
		edge = new ArrayList<edges>();
	}
	city(String n){
		name = n;
		edge = new ArrayList<edges>();
	}
	city(String n, ArrayList a){
		name = n;
		edge = new ArrayList<edges>(a.size());
		for(int i = 0; i < a.size(); i++) {
			edge.addAll(a);
		}
	}
	void setName(String n) {name = n;}
	String getName() {return name;}
	
	void newEdge(String n, int m, int t) {
		edges nedge = new edges(n , m, t);
		edge.add(nedge);
	}
	
	
	@Override
	public int compareTo(city o) {
		if(this.weight < o.weight) {
			return -1;
		}
		else if(this.weight > o.weight) {
			return 1;
		}
		else return 0;
	}
	/*public int compareTo(city o) {
		return Integer.compare(this.weight, o.weight);
	}*/
	}