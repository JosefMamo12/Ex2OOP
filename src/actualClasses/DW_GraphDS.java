package actualClasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;

public class DW_GraphDS implements directed_weighted_graph { 
	HashMap <Integer,node_data>  Nodes;
	HashMap <Integer, HashMap<Integer,edge_data>> Edges;
	HashMap <Integer,HashMap<Integer,node_data>> _myFather;
	int _nodeSize , _edgeSize, _mc;

	public DW_GraphDS () {
		Nodes = new HashMap<Integer, node_data>();
		Edges = new HashMap<Integer, HashMap<Integer,edge_data>>();
		_myFather = new HashMap<Integer, HashMap<Integer,node_data>>();
		_nodeSize = _edgeSize = _mc = 0 ;
	}
	/**
	 * returns the node_data by the node_id,
	 * @param key - the node_id
	 * @return the node_data by the node_id, null if none.
	 */
	@Override
	public node_data getNode(int key) {
		if(Nodes.containsKey(key))
			return Nodes.get(key);
		return null;
	}
	/**
	 * returns the data of the edge (src,dest), null if none.
	 * Note: this method should run in O(1) time.
	 * @param src
	 * @param dest
	 * @return
	 */
	@Override
	public edge_data getEdge(int src, int dest) {
		if(Nodes.containsKey(src) && Nodes.containsKey(dest)) 
			if(Edges.get(src).containsKey(dest))
				return Edges.get(src).get(dest);

		return null;
	}
	/**
	 * adds a new node to the graph with the given node_data.
	 * Note: this method should run in O(1) time.
	 * @param n
	 */
	public void addNode(node_data n) {
		if(Nodes.containsKey(n.getKey()))
			return;
		Nodes.put(n.getKey(), n);
		Edges.put(n.getKey(), new HashMap<Integer,edge_data>());
		_myFather.put(n.getKey(),new HashMap<Integer,node_data>());
		_nodeSize++;
		_mc++;
	}
	/**
	 * Connects an edge with weight w between node src to node dest.
	 * * Note: this method should run in O(1) time.
	 * @param src - the source of the edge.
	 * @param dest - the destination of the edge.
	 * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
	 */
	@Override
	public void connect(int src, int dest, double w) {
		if(src==dest)
			return;
		if(!Nodes.containsKey(src)||!Nodes.containsKey(dest))
			return;
		if(w<0)
			return;
		if(Edges.get(src).containsKey(dest))
			return;
		node_data srcNode = Nodes.get(src);
		node_data destNode = Nodes.get(dest);
		Edges.get(src).put(dest, new EdgeData(srcNode, destNode,w));
		_myFather.get(dest).put(src, srcNode);
		_edgeSize++;
		_mc++;
	}
	/**
	 * This method returns a pointer (shallow copy) for the
	 * collection representing all the nodes in the graph. 
	 * Note: this method should run in O(1) time.
	 * @return Collection<node_data>
	 */
	@Override
	public Collection<node_data> getV() {
		return Nodes.values();
	}
	/**
	 * This method returns a pointer (shallow copy) for the
	 * collection representing all the edges getting out of 
	 * the given node (all the edges starting (source) at the given node). 
	 * Note: this method should run in O(k) time, k being the collection size.
	 * @return Collection<edge_data>
	 */
	@Override
	public Collection<edge_data> getE(int node_id) {
		if(Nodes.containsKey(node_id))
			return Edges.get(node_id).values();
		return null;
	}
	/**
	 * Deletes the node (with the given ID) from the graph -
	 * and removes all edges which starts or ends at this node.
	 * This method should run in O(k), V.degree=k, as all the edges should be removed.
	 * @return the data of the removed node (null if none). 
	 * @param key
	 */
	@Override
	public node_data removeNode(int key) {
		if(Nodes.containsKey(key)) {
			node_data removedNode = Nodes.get(key);
			ArrayList<edge_data> le  =new ArrayList<edge_data>(getE(key));
			for (edge_data edge : le) {
				removeEdge(key,edge.getDest());
			}
			for (Entry<Integer, node_data> entry : _myFather.get(key).entrySet()) {
				removeEdge(entry.getKey(),key);
			}
			_nodeSize--;
			_mc++;
			return removedNode;

		}
		return null;
	}
	/**
	 * Deletes the edge from the graph,
	 * Note: this method should run in O(1) time.
	 * @param src
	 * @param dest
	 * @return the data of the removed edge (null if none).
	 */
	@Override
	public edge_data removeEdge(int src, int dest) {
		if(src == dest)
			return null;
		if(!Nodes.containsKey(src) || !Nodes.containsKey(dest))
			return null;
		if(!Edges.get(src).containsKey(dest))
			return null;
		edge_data e = Edges.get(src).remove(dest);
		_edgeSize--;
		_mc++;
		return e;
	}
	public boolean equals (Object o) {
		if(!(o instanceof directed_weighted_graph))
			return false;
		directed_weighted_graph g = (directed_weighted_graph) o;
		if(this.edgeSize()!=g.edgeSize()||this.getMC()!=g.getMC() || this.nodeSize()!=g.nodeSize())
			return false;
		Iterator<node_data> itr = this.getV().iterator();
		Iterator<node_data> itr1 = g.getV().iterator();
		while(itr.hasNext() && itr1.hasNext()) {
			node_data curr = itr.next() , currCopy = itr1.next();
			if(!curr.equals(currCopy))
				return false;
			Iterator <edge_data> itrE = this.getE(curr.getKey()).iterator();
			Iterator <edge_data> itrE1 = this.getE(currCopy.getKey()).iterator();
			while(itrE.hasNext() && itrE1.hasNext()) {
				if(!(itrE.next().equals(itrE1.next())))
					return false;
			}

		}
		return true;
	}

	@Override
	public int nodeSize() {return this._nodeSize ;}

	@Override
	public int edgeSize() {return this._edgeSize;}

	@Override
	public int getMC() {return this._mc;}


	class EdgeData implements edge_data{
		node_data src , dest ;
		double weight;
		int tag;
		String s;

		public EdgeData(node_data src, node_data dest) {
			this.src = src;
			this.dest = dest;
			this.weight = src.getLocation().distance(dest.getLocation());
			this.tag = -1;
			this.s = null;

		}
		public EdgeData(node_data src, node_data dest,double weight) {
			this.src = src;
			this.dest = dest;
			this.weight = weight;
			this.tag = -1;
			this.s = null;
		}

		@Override
		public int getSrc() {return this.src.getKey();}

		@Override
		public int getDest() {return this.dest.getKey();}

		@Override
		public double getWeight() {return this.weight;}

		@Override
		public String getInfo() {return this.s;}

		@Override
		public void setInfo(String s) {this.s = s;}

		@Override
		public int getTag() {return this.tag;}

		@Override
		public void setTag(int t) {this.tag=t;}
		@Override
		public boolean equals (Object o) {
			if(!(o instanceof edge_data))
				return false;
			edge_data p = (edge_data) o;
			if(this.getSrc()!=p.getSrc())
				return false;
			if(this.getDest()!=p.getDest())
				return false;
			if(this.getWeight()!= p.getWeight())
				return false;
			if(this.getTag()!= p.getTag())
				return false;
			if(!(this.getInfo()==null && p.getInfo()== null))
					{
				if(this.getInfo()==null && p.getInfo()!=null)
					return false;
				if(p.getInfo()==null && this.getInfo()!=null)
					return false;
				if(!this.getInfo().equals(p.getInfo()))
					return false;

					}
			return true;
		}
	}

}
