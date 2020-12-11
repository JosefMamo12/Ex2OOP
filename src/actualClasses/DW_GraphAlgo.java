package actualClasses;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;


import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.edge_data;
import api.geo_location;
import api.node_data;




public class DW_GraphAlgo implements dw_graph_algorithms {
	directed_weighted_graph _graph;
	HashMap<Integer, node_data> _parent;
	static final double infinity = Double.POSITIVE_INFINITY;


	/**
	 * Init the graph on which this set of algorithms operates on.
	 * @param g
	 */
	@Override
	public void init(directed_weighted_graph g) {
		this._graph = g;

	}
	/**
	 * Return the underlying graph of which this class works.
	 * @return
	 */
	@Override
	public directed_weighted_graph getGraph() {
		return this._graph;
	}
	/**
	 * Compute a deep copy of this weighted graph.
	 * @return
	 */
	@Override
	public directed_weighted_graph copy() {
		directed_weighted_graph  g = new DW_GraphDS(); 
		Iterator<node_data> itr = _graph.getV().iterator();
		while(itr.hasNext()) {
			node_data current = itr.next();
			node_data newCopiedNode = new NodeData (current);
			g.addNode(newCopiedNode);
		}
		itr = _graph.getV().iterator();
		while(itr.hasNext()) {
			node_data current = itr.next();
			Iterator<edge_data> neiItr = _graph.getE(current.getKey()).iterator();
			while(neiItr.hasNext()) {
				edge_data e = neiItr.next();
				g.connect(e.getSrc(), e.getDest(), e.getWeight());
			}
		}
		return g;
	}
	/**
	 * Returns true if and only if (iff) there is a valid path from each node to each
	 * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
	 * @return
	 */
	@Override
	public boolean isConnected() {
		if(_graph.nodeSize() == 0 && _graph.nodeSize() == 1)
			return true;
		boolean ans1 =checkifTransposeGraphConnected(transposeG(_graph));
		boolean ans2 =checkifRegularGraphConnected(_graph);
		return ans1||ans2;
	}
	/*
	 * private function thats change all the directions of the "arrows" 
	 * means destination become source and the source become destination.
	 */
	private boolean checkifTransposeGraphConnected(directed_weighted_graph transposeG) {
		Iterator <node_data> itr = transposeG.getV().iterator();
		dijkestra(itr.next().getKey(), transposeG);
		while(itr.hasNext()) {
			node_data current = itr.next();
			if(current.getTag()==-1)
				return false;
		}
		return true;
	}

	private boolean checkifRegularGraphConnected(directed_weighted_graph _graph2) {
		Iterator <node_data> itr = _graph.getV().iterator();
		dijkestra(itr.next().getKey(),_graph2);
		while(itr.hasNext()) {
			node_data current = itr.next();
			if(current.getTag()==-1)
				return false;
		}
		return true;
	}
	/**
	 * returns the length of the shortest path between src to dest
	 * Note: if no such path --> returns -1
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		if(src == dest)
			return -1;
		if(!contains(src) || !contains(dest))
			return -1;
		dijkestra(src, _graph);
		if(_graph.getNode(src).getWeight()== -1 || _graph.getNode(dest).getWeight() == -1)
			return -1;
		if(_graph.getNode(src).getWeight()== infinity || _graph.getNode(dest).getWeight() == infinity)
			return -1;
		return _graph.getNode(dest).getWeight();
	}
	/**
	 * returns the the shortest path between src to dest - as an ordered List of nodes:
	 * src--> n1-->n2-->...dest
	 * see: https://en.wikipedia.org/wiki/Shortest_path_problem
	 * Note if no such path --> returns null;
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return
	 */
	@Override
	public List<node_data> shortestPath(int src, int dest) {
		Stack <node_data> stack = new Stack<node_data>();
		if(shortestPathDist(src,dest) == -1) {
			return null;
		}
		if (shortestPathDist(src, dest)== infinity) {
			return null;
		}
		stack.add(_graph.getNode(dest));
		while(src!=dest) {
			node_data parentNode = _parent.get(dest);
			stack.add(parentNode);
			dest = returnIntKey(parentNode);
		}

		LinkedList<node_data> ll = new LinkedList<node_data>();
		while(!stack.isEmpty()){
			ll.add(stack.pop());
		}
		return ll;
	}
	/**
	 * Saves this weighted (directed) graph to the given
	 * file name - in JSON format
	 * @param file - the file name (may include a relative path).
	 * @return true - iff the file was successfully saved
	 */
	@Override
	public boolean save(String file) {
		String json;
		try {
			json = toJsonFile(_graph);
			try {
				PrintWriter pw = new PrintWriter(file);
				pw.write(json);
				pw.close();
				return true;
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return false;
	}
    private String toJsonFile(directed_weighted_graph _graph2) throws JSONException {
		JSONObject jo = new JSONObject();
		JSONArray jna = new JSONArray();
		JSONArray jea = new JSONArray();
		Iterator<node_data> itr = _graph2.getV().iterator();
		while(itr.hasNext()) {
			jna.put(JsonNodes(itr.next()));
		}
		itr = _graph2.getV().iterator();
		while(itr.hasNext()) {
			Iterator<edge_data> iter = _graph.getE(itr.next().getKey()).iterator();
			while(iter.hasNext()) {
				edge_data e = iter.next();
				jea.put(JsonEdges(e));
				
			}
		
		}
		jo.put("Nodes",jna);
		jo.put("Edges", jea);
		return jo.toString();
	}
	private JSONObject JsonEdges(edge_data e) throws JSONException {
		JSONObject je = new JSONObject();
		je.put("src", e.getSrc());
		je.put("w",e.getWeight());
		je.put("dest",e.getDest());
		return je;
	}
	private JSONObject JsonNodes(node_data next) throws JSONException {
		JSONObject jn = new JSONObject();
		jn.put("id", next.getKey());
		jn.put("pos", next.getLocation().toString());
		
		return jn;
	}
	/**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded
     */
	@Override
	public boolean load(String file) {
		try {
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeHierarchyAdapter(DW_GraphDS.class, new DW_GraphDSJasonDeserializer());
			Gson gson = builder.create();

			FileReader reader = new FileReader(file);
			this._graph= gson.fromJson(reader,DW_GraphDS.class);
			System.out.println(this._graph);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	private void dijkestra (int src, directed_weighted_graph graph) {
		node_data first = graph.getNode(src);
		_parent = new HashMap<Integer, node_data>();
		setDistanceWeight();
		_parent.put(first.getKey(), null);
		first.setWeight(0);
		for (int i = 0; i < graph.nodeSize(); i++) {
			node_data current = mindistance();
			if(current == null)
				break;
			current.setTag(0);
			ArrayList <edge_data> nodeNei = new ArrayList<edge_data>(graph.getE(current.getKey())); 
			for (edge_data neibo : nodeNei) {
				if(neibo!=null) {
					double neiboWei = neibo.getWeight();
					node_data neiboNode =  graph.getNode(neibo.getDest());
					if(neibo.getTag()==-1 && current.getWeight() + neiboWei < neiboNode.getWeight()) {
						neiboNode.setWeight(current.getWeight()+neiboWei);
						_parent.put(neibo.getDest(), current);
						_graph.getNode(neiboNode.getKey()).setInfo("Visited");
					}
				}
			}
		}
	}
	/**
	 * private function for the dijkestra that find the node with the smallest weight.
	 * iterate over all graph nodes and their weight.
	 * @return node_data
	 */
	private node_data mindistance() {
		int key = -1;
		double weight = infinity;
		Iterator <node_data> itr = _graph.getV().iterator();
		while(itr.hasNext()) {
			node_data curr = itr.next();
			while(curr.getTag()==0 && itr.hasNext()) {
				curr = itr.next();
			}
			if(curr.getWeight()<weight) {
				weight = curr.getWeight();
				key = curr.getKey();
			}
		}
		return _graph.getNode(key);
	}

	/**
	 * reset buffer which set the tag of every node in -1.
	 * intialize _parent HashMaps before using dijkestra.
	 * @return
	 */
	private void setDistanceWeight() {
		Collection <node_data > setHash = new ArrayList<node_data>(_graph.getV());
		for (node_data node_info : setHash) {
			node_info.setTag(-1);
			_parent.put(node_info.getKey(), null);
			node_info.setWeight(infinity);

		}
	}
	private boolean contains (int key) {
		Iterator <node_data> itr = _graph.getV().iterator();
		node_data current = null;
		while(itr.hasNext()) {
			current = itr.next();
			if(current.getKey() == key)
				return true;
		}
		return false;
	}
	/**
	 * this method link in parent hashmap between the node_info values to the integer keys.
	 * this private function espacilly for the shortestDistance method.
	 * @param current
	 * @return int
	 */
	private int returnIntKey(node_data current) {
		int d = 0;
		for(Entry<Integer, node_data> entry: _parent.entrySet()){
			if(entry.getKey()==current.getKey())
				d =entry.getKey();
		}
		return d;
		/*
		 * simple function which change the directions of the destination and source.
		 */
	}
	private directed_weighted_graph transposeG (directed_weighted_graph g){
		directed_weighted_graph transposeGraph = new DW_GraphDS();
		Iterator<node_data> itr = _graph.getV().iterator();
		while(itr.hasNext())
			transposeGraph.addNode(itr.next());
		itr = this._graph.getV().iterator();
		while(itr.hasNext()) {
			node_data current = itr.next();
			Iterator<edge_data> itrE = _graph.getE(current.getKey()).iterator();
			while(itrE.hasNext()) {
				edge_data e = itrE.next();
				transposeGraph.connect(e.getDest(), e.getSrc(), e.getWeight());
			}
		}
		return transposeGraph;
	}
	/**
	 * This class created to load a json files thats associated with graphs into my work.
	 * @author yosi
	 *
	 */
	class DW_GraphDSJasonDeserializer implements JsonDeserializer<DW_GraphDS>
	{

		@Override
		public DW_GraphDS deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2)
				throws JsonParseException {
			directed_weighted_graph g = new DW_GraphDS();
			JsonObject jo = json.getAsJsonObject();
			JsonArray jov = jo.get("Nodes").getAsJsonArray();
			JsonArray joe = jo.get("Edges").getAsJsonArray();
			addVertex(jov , g);
			addEdges(joe, g);
			return (DW_GraphDS) g;
		}

		private void addEdges(JsonArray joe, directed_weighted_graph g) {
			int src , dest  ;
			double weight ;
			for (JsonElement set : joe) {
				src = set.getAsJsonObject().get("src").getAsInt();
				dest = set.getAsJsonObject().get("dest").getAsInt();
				weight = set.getAsJsonObject().get("w").getAsDouble();
				g.connect(src, dest, weight);
				
				
			}
		}

		private void addVertex(JsonArray jov, directed_weighted_graph g) {
			int key;
			String str = " ";
			for (JsonElement set : jov) {
				 key = set.getAsJsonObject().get("id").getAsInt();
				 node_data curr = new NodeData(key);
				 str = set.getAsJsonObject().get("pos").getAsString();
				 curr.setLocation(parser(str));
				 g.addNode(curr);
				 
				
			}
		}

		private geo_location parser(String str) {
			double x ,y ,z;
			String[] arrSplit = str.split(",");
			x = Double.parseDouble(arrSplit[0]);
			y = Double.parseDouble(arrSplit[1]);
			z = Double.parseDouble(arrSplit[2]);
			geo_location p = new GeoLocation(x,y,z);
			return p;
		}
	}
}

