import java.io.*;
import java.util.*;

public class SnakesLadder extends AbstractSnakeLadders {
	
	int N, M;
	int snakes[];
	int ladders[];
	ArrayList<Node> addr;
	int[][] ladder;
	public SnakesLadder(String name)throws Exception{
		addr = new ArrayList<Node>();
		File file = new File(name);
		BufferedReader br = new BufferedReader(new FileReader(file));
		N = Integer.parseInt(br.readLine());
        
        M = Integer.parseInt(br.readLine());

	    snakes = new int[N];
		ladders = new int[N];
	    for (int i = 0; i < N; i++){
			snakes[i] = -1;
			ladders[i] = -1;
		}
		ladder = new int[M][2];
		for(int i=0;i<M;i++){
            String e = br.readLine();
            StringTokenizer st = new StringTokenizer(e);
            int source = Integer.parseInt(st.nextToken());
            int destination = Integer.parseInt(st.nextToken());

			if(source<destination){
				ladders[source] = destination;
				ladder[i][0] = source;
				ladder[i][1] = destination;
			}
			else{
				snakes[source] = destination;
			}
        }
		create_and_update(N , snakes , ladders);
		create_links(N);
		//System.out.println(addr.get(0));
		//System.out.println(addr.get(N));
		do_bfs_bottom();
		do_bfs_top(N);
		//System.out.println(addr.get(0).level);
		//System.out.println(addr.get(N).level);
		//System.out.println(addr.get(N).depth);
		//System.out.println(addr.get(0).depth);
	}






	private void create_and_update(int N , int[] snakes , int[] ladders){
		for(int i = 0 ; i<= N ; i++){
			// find new way to update in ladder
			addr.add(new Node(i));
		}
		for(int i = 0 ; i<N ; i++){
			if(ladders[i]!= -1){
				update_for_ladder(addr.get(i) , i , ladders , snakes);
			}
		}
		for(int i = 0 ; i<N ; i++){
			if(snakes[i]!= -1){
				update_for_snake(addr.get(i) , i , ladders , snakes);
			}
		}
	}
	private void update_for_ladder(Node node , int i , int[] ladders , int[] snakes){
		if(ladders[ladders[i]] != -1){
			update_for_ladder(addr.get(ladders[i]) , ladders[i] , ladders , snakes );
		}
		else if(snakes[ladders[i]] != -1){
			update_for_snake(addr.get(ladders[i]) , ladders[i] , ladders , snakes );
		}
		addr.set(i , addr.get(ladders[i]));
	}
	private void update_for_snake(Node node , int i , int[] ladders , int[] snakes){
		if(ladders[snakes[i]] != -1){
			update_for_ladder(addr.get(snakes[i]) , snakes[i] , ladders , snakes );
		}
		else if(snakes[snakes[i]] != -1){
			update_for_snake(addr.get(snakes[i]) , snakes[i] , ladders , snakes );
		}
		addr.set(i , addr.get(snakes[i]));
	}

	//done
	private void create_links(int N){
		for(int i = 0 ; i< N ; i++){
			int start = i+1;
			int end;
			if(i+6>= N){
				end = N;
			}
			else{
				end = i+6;
			}
			for(int j = start ; j<=end ; j++){
				addr.get(i).neighbours.add(addr.get(j));
				addr.get(j).antineighbours.add(addr.get(i));
			}
		}
	}






	//done
	private void do_bfs_bottom(){
		ArrayList<Node> start = new ArrayList<Node>();
		start.add(addr.get(0));
		bottom_helper(start , 0);
	}
	private void bottom_helper(ArrayList<Node> node , int counter){
		if(node.size() != 0){
			ArrayList<Node> new_node= new ArrayList<Node>();
			for(int j = 0; j<node.size() ; j++){
				node.get(j).level = counter;
			}
			for(int j = 0; j<node.size() ; j++){
				for(int i =0 ; i< node.get(j).neighbours.size() ; i++){
					if(node.get(j).neighbours.get(i).level == -1){
						new_node.add(node.get(j).neighbours.get(i));
					}
				}
			}
			bottom_helper(new_node , counter+1);
		}
	}

	//done
	private void do_bfs_top(int N){
		ArrayList<Node> start= new ArrayList<Node>();
		start.add(addr.get(N));
		top_helper(start , 0);
	}
	private void top_helper(ArrayList<Node> node , int counter){
		if(node.size() != 0){
			ArrayList<Node> new_node= new ArrayList<Node>();
			for(int j = 0 ; j<node.size() ; j++){
				node.get(j).depth = counter;
			}
			for(int j = 0 ; j<node.size() ; j++){
				for(int i = 0 ; i< node.get(j).antineighbours.size() ; i++){
					if(node.get(j).antineighbours.get(i).depth == -1){
						new_node.add(node.get(j).antineighbours.get(i));
					}
				}
			}
			top_helper(new_node , counter+1);
		}
	}


	//done
	public int OptimalMoves()
	{
		/* Complete this function and return the minimum number of moves required to win the game. */
		if(addr.get(addr.size()-1).level == -1){
			return -1;
		}
		return addr.get(addr.size() -1).level - addr.get(0).level;
	}

	//done
	public int Query(int x, int y)
	{
		/* Complete this function and 
			return +1 if adding a snake/ladder from x to y improves the optimal solution, 
			else return -1. */
		if(addr.get(addr.size() -1).level - addr.get(0).level > addr.get(x).level - addr.get(0).level + addr.get(y).depth - addr.get(addr.size() -1).depth){
			return 1;
		}
		else{
			return -1;
		}
	}

	public int[] FindBestNewSnake()
	{ 
		int result[] = {-1, -1};
		int min = this.OptimalMoves();
		Arrays.sort(ladder, Comparator.comparingDouble(o -> o[0]));
		// System.out.println(ladder.length);
		// System.out.println(min);
		for(int i = 1; i < ladder.length; i++){
			int j = i;
			//System.out.println(i);
			while(j<ladder.length && ladder[i - 1][1] > ladder[j][0]){
				// System.out.println("hi");
				// System.out.println(j);
				// System.out.println(newopt(ladder[i-1][1] , ladder[j][0]));
				// System.out.println(min);
				if(newopt(ladder[i-1][1] , ladder[j][0])<min){
					result[0] = ladder[i-1][1];
					result[1] = ladder[j][0];
					min = newopt(ladder[i-1][1] , ladder[j][0]);
				}
				j = j+1;
			}
        	//if (ladder[i - 1][1] > ladder[i][0]){
				
			//}
		}
		/* Complete this function and 
			return (x, y) i.e the position of snake if adding it increases the optimal solution by largest value,
			if no such snake exists, return (-1, -1) */
		// System.out.print(result[0]);
		// System.out.print(result[1]);
		return result;
	}
	private int newopt(int x , int y){
		return addr.get(x).level - addr.get(0).level + addr.get(y).depth - addr.get(addr.size() -1).depth;
	}
}


class Node{
	int level;
	int depth;
	int value;
	ArrayList<Node> neighbours;
	ArrayList<Node> antineighbours;
	public Node(int value){
		this.value = value;
		this.level = -1;
		this.depth = -1;
		this.neighbours = new ArrayList<Node>();
		this.antineighbours = new ArrayList<Node>();
	}
}