import java.util.*;
public class city {
	String name;
	List<edge> edges = new ArrayList<edge>();
	List<String> attractions = new ArrayList<String>();
	public class edge{
		String name;
		int miles;
		int time;
		edge(String n, int m, int t){
			name = n;
			miles = m;
			time = t;
		}
	}
	city(){
		name = null;
		edges = new ArrayList<edge>();
	}
	city(String n){
		name = n;
		edges = new ArrayList<edge>();
	}
	city(String n, ArrayList a){
		name = n;
		edges = new ArrayList<edge>(a.size());
		for(int i = 0; i < a.size(); i++) {
			edges.addAll(a);
		}
	}
	void setName(String n) {name = n;}
	String getName() {return name;}
	
	void newEdge(String n, int m, int t) {
		edge nedge = new edge(n , m, t);
		edges.add(nedge);
	}
	
	}