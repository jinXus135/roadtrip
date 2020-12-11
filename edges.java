

public class edges implements Comparable<edges> {
	 //implements comparator so we can add edges to priority queue
		String name;
		int miles;
		int time;
		edges(String n, int m, int t){
			name = n;
			miles = m;
			time = t;
		}
		@Override
		public int compareTo(edges o) {
			if(this.miles < o.miles) {
				return -1;
			}
			else if(this.miles > o.miles) {
				return 1;
			}
			else return 0;
		}
		}
		
		
	

