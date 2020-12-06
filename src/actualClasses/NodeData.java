package actualClasses;
import api.geo_location;
import api.node_data;

public class NodeData implements node_data {
	private int _key ,_tag;
	private geo_location _location;
	private double _weight;
	private String _info;
	private static int counter = 0;
	/**
	 * Defultive constructur.
	 */
	public NodeData(int key) {
		_key = key;
		_tag = 0;
		_location = new GeoLocation();
		_weight = 0;
		_info = "Unvisited";
	}

	public NodeData() {
		_key = counter++;
		_tag = 0;
		_location = new GeoLocation();
		_weight = 0;
		_info = "Unvisited";
	}
	/**
	 * Returns the key (id) associated with this node.
	 * @return
	 */
	@Override
	public int getKey() {
		return this._key;
	}
	/** Returns the location of this node, if
	 * none return null.
	 * 
	 * @return
	 */
	@Override
	public geo_location getLocation() {return this._location;}
	/** Allows changing this node's location.
	 * @param p - new new location  (position) of this node.
	 */
	@Override
	public void setLocation(geo_location p) {this._location = p;}
	/**
	 * Returns the weight associated with this node.
	 * @return
	 */
	@Override
	public double getWeight() {return this._weight;}
	/**
	 * Allows changing this node's weight.
	 * @param w - the new weight
	 */
	@Override
	public void setWeight(double w) {this._weight = w;}
	/**
	 * Returns the remark (meta data) associated with this node.
	 * @return
	 */
	@Override
	public String getInfo() {return this._info;}
	/**
	 * Allows changing the remark (meta data) associated with this node.
	 * @param s
	 */
	@Override
	public void setInfo(String s) {this._info = s;}
	/**
	 * Temporal data (aka color: e,g, white, gray, black) 
	 * which can be used be algorithms 
	 * @return
	 */
	@Override
	public int getTag() {return this._tag;}
	/** 
	 * Allows setting the "tag" value for temporal marking an node - common
	 * practice for marking by algorithms.
	 * @param t - the new value of the tag
	 */
	@Override
	public void setTag(int t) {this._tag = t;}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof node_data))
			return false;
		node_data node = (node_data) o;
		if(!this._info.equals(node.getInfo()))
			return false;
		if((this._key != node.getKey()))
			return false;
		if((this._tag != node.getTag()))
			return false;
		if((this._weight != node.getWeight()))
			return false;
		return true;

	}
}
class GeoLocation implements geo_location{
	double _x;
	double _y;
	double _z;

	public GeoLocation() {
		_x = 0;
		_y = 0;
		_z = 0;
	}
	public GeoLocation (double x, double y, double z) {
		this._x = x;
		this._y = y;
		this._z = z;
	}
	
	public void setX (double x) {
		this._x = x;
	}
	public void setY (double y) {
		this._y = y;
	}
	public void setZ (double z) {
		this._z = z;	
	}
	@Override
	public double x() {
		return this._x;
	}

	@Override
	public double y() {
		return this._y;
	}

	@Override
	public double z() {
		return this._z;
	}

	@Override
	public double distance(geo_location g) {
		return Math.sqrt(Math.pow(g.x()-this._x, 2)+Math.pow(g.y()-this._y, 2));
	}


}
