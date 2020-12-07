package actualClasses;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import org.junit.jupiter.api.Test;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.node_data;

class DW_GraphAlgoTest {
	private static Random _rand;
	private static long _seed;
	public static void initSeed(long seed) {
		_seed = seed;
		_rand = new Random(_seed);
	}
	public static void initSeed() {
		initSeed(_seed);
	}
	private static int nextRnd(int min, int max) {
		double v = nextRnd(0.0+min, (double)max);
		int ans = (int)v;
		return ans;
	}
	private static double nextRnd(double min, double max) {
		double d = _rand.nextDouble();
		double dx = max-min;
		double ans = d*dx+min;
		return ans;
	}
	/*
	 * Simple method for returning an array with all the node_data of the graph,
	 * Note: this should be using an  Iterator<node_edge> to be fixed in Ex1
	 * @param g
	 * @return
	 */
	private static int[] nodes(directed_weighted_graph g) {
		int size = g.nodeSize();
		Collection<node_data> V = g.getV();
		node_data[] nodes = new node_data[size];
		V.toArray(nodes); // O(n) operation
		int[] ans = new int[size];
		for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
		Arrays.sort(ans);
		return ans;
	}

	/*
	 * Generate a random graph with v_size nodes and e_size edges
	 * @param v_size
	 * @param e_size
	 * @param seed
	 * @return
	 */
	private static directed_weighted_graph graph_creator(int v_size, int e_size, int seed) {
		directed_weighted_graph g = new DW_GraphDS();
		initSeed(seed);
		for(int i=0;i<v_size;i++) {
			g.addNode(new NodeData(i));
		}

		int[] nodes = nodes(g);
		while(g.edgeSize() < e_size) {
			double w = nextRnd(1, 10);
			int a = nextRnd(0,v_size);
			int b = nextRnd(0,v_size);
			int i = nodes[a];
			int j = nodes[b];
			g.connect(i,j,w);
		}
		return g;
	}
		private static directed_weighted_graph graph_creator1(int v_size, int e_size, int seed) {
			directed_weighted_graph g = new DW_GraphDS();
			initSeed(seed);
			for(int i=0;i<v_size;i++) {
				g.addNode(new NodeData(i));
			}
			int[] nodes = nodes(g);
			int i = 2;
			while(g.edgeSize() < e_size) {
				if(i<v_size) {
					int a = nodes [i-2];
					int b = nodes[i-1];
					int c =nodes [i];
					double w = nextRnd(1, 10);
					g.connect(a,b,w);
					w = nextRnd(1,10);
					g.connect(a, c, w);
					i++;
				}
			}
			return g;
		}
	
//	@Test
//	void savetest() {
//		directed_weighted_graph g = new DW_GraphDS();
//		int v = 10;
//		for (int i = 0; i < v ; i++) {
//			g.addNode(new NodeData(i));
//		}
//	g.connect(0, 1, 2.5);
//	g.connect(1, 2, 3.8);
//	g.connect(1,5,3.2);
//	dw_graph_algorithms g1 = new DW_GraphAlgo();
//	g1.init(g);
//	g1.save("graph.jason");
//			
//	}
//	@Test
//	void loadtest() {
//		directed_weighted_graph g = new DW_GraphDS();
//		dw_graph_algorithms g1 = new DW_GraphAlgo();
//		g1.load("graph.json");
//		
//	}
	@Test
	void copytest() {
	directed_weighted_graph g = graph_creator(10, 15, 1);
	directed_weighted_graph g1 = new  DW_GraphDS();
	dw_graph_algorithms g2 = new DW_GraphAlgo();
	g2.init(g);
	g1 = g2.copy();
	assertTrue(g1.equals(g));
	g1.addNode(new NodeData(15));//add vertex
	assertFalse(g1.equals(g));
	g2.init(g1);
	directed_weighted_graph g3 = g2.copy();
	assertTrue(g3.equals(g1));// add edge
	g3.connect(5, 9, 3.5);
	assertFalse(g3.equals(g1));
	}
	
	@Test
	void isConnectTest() {
		dw_graph_algorithms g1 = new DW_GraphAlgo();
		directed_weighted_graph g = new DW_GraphDS();
		g.addNode(new NodeData(0));
		g.addNode(new NodeData(1));
		g.connect(1, 0, 2);
		g.addNode(new NodeData(2));
		g.connect(2, 0, 1);
		g1.init(g);
		assertFalse(g1.isConnected());
	}

}
