package actualClasses;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;




class DW_GraphDSTest {
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
		/*
		 *Create a unique graph which help to the tests.
		 *@param v_size
		 *@param e_size
		 *@param seed
		 *@return weighted_graph
		 */
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

	/**
	 * big graph test.
	 */
	@Test
	@Timeout(value = 10,unit = TimeUnit.SECONDS)
	void test() {
		int v = 100000, e = 5*v , seed = 1;
		directed_weighted_graph g = graph_creator(v, e, seed);
		assertEquals(v, g.nodeSize());
		assertEquals(e, g.edgeSize());

	}
	@Test
	void testgetNode() {
		directed_weighted_graph g = new DW_GraphDS();
		assertNull(g.getNode(0));
		int v = 10, e = 15 , seed = 1;
		g = graph_creator(v, e, seed);
		for (int i = 0; i < g.nodeSize(); i++) {
			assertNotNull(g.getNode(i));

		}
	}
	@Test
	void testgetEdge() {
		directed_weighted_graph g = new DW_GraphDS();
		assertNull(g.getEdge(0, 1));
		int v = 2, e = 1 , seed = 1;
		g = graph_creator(v, e, seed);
		assertNotNull(g.getEdge(1, 0));

	}
	@Test
	void testaddNode() {
		directed_weighted_graph g = new DW_GraphDS();
		for (int i = 0; i < 15; i++) {
			g.addNode(new NodeData());
		}
		assertEquals(15,g.nodeSize());
	}
	@Test
	void testconnect() {
		directed_weighted_graph g = new DW_GraphDS();
		g.connect(0, 1, 2.2);
		assertEquals(0,g.edgeSize());// if they both not in the graph
		g.addNode(new NodeData(0));
		g.addNode(new NodeData(1));
		g.connect(0, 3, 2.5);
		assertEquals(0, g.edgeSize());// if one vertex is part of the graph and the second isnot.
		g.connect(0, 0, 3.2);
		assertEquals(0, g.edgeSize());// if they are equalls.
		g.connect(0, 1, 5.5);
		assertEquals(1, g.edgeSize());		// one connect in graph with 2 vertex means 1 edge.
	}
	/*
	 * Iterate over the collection 
	 */
	@Test
	void testgetV() {
		int v = 10 , e  = 15 , seed = 1;
		directed_weighted_graph g = graph_creator(v, e, seed);
		Iterator<node_data> itr = g.getV().iterator();
		while(itr.hasNext()) {
			node_data curr = itr.next();
			node_data curr1 = g.getNode(curr.getKey());
			if(!(curr.equals(curr1)))
				fail();
		}
	}
	@Test 
	void testgetE() {
		directed_weighted_graph g = new DW_GraphDS();
		g.addNode(new NodeData(0));
		ArrayList <edge_data> el = new ArrayList<edge_data>(g.getE(0));
		assertEquals(0, el.size());
		g.addNode(new NodeData(1));
		g.connect(0, 1, 2.2);
		ArrayList <edge_data> el1 = new ArrayList<edge_data>(g.getE(0));
		assertEquals(1, el1.size());	
	}
	@Test
	void testremoveNode() {
		directed_weighted_graph g = graph_creator(10, 15, 1);
		assertNull(g.removeNode(11));
		ArrayList <edge_data> le = new ArrayList<edge_data>(g.getE(1));
		g.removeNode(1);
		assertEquals(9, g.nodeSize());
		assertEquals(9, g.edgeSize());
	}
	@Test
	void testRemoveEdges() {
		directed_weighted_graph g = graph_creator(2, 0, 1);
		assertNull(g.removeEdge(0, 1));// remove edge thats not exist in the graph.
		g.connect(0,1,3.5);
		if(g.edgeSize()== 0)
			fail();
		g.removeEdge(0, 1);
		assertEquals(0, g.edgeSize());

	}
	@Test
	void size_edges_mc_Tests() {
		int v = 15 , e = 8 , seed = 1;
		directed_weighted_graph g = graph_creator(v, e, 1);
		assertEquals(v, g.nodeSize());
		assertEquals(e,g.edgeSize());
		assertEquals(v+e,g.getMC());
		for (int i = 0; i < v; i++) {
			g.removeNode(i);
		}
		assertEquals(0,g.nodeSize());
		assertEquals(0,g.edgeSize());
		assertEquals(2*(v+e),g.getMC());
	}

}



