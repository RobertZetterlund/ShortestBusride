
/**
* Extends an Edge with a weight and a name for the bus line.
*/
public class BusEdge extends Edge {
	
	/**
	* The name (or number) of the line
	*/
	public final String line;
	/**
	* The weight (or cost) of the edge.
	*/
	private final double weight;

	/**
	* Construct an edge. Nothing can change once created.
	*/
	BusEdge( int from, int to, double weight, String line ) {
		super(from, to);
		this.line = line;
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	} 
	
	public String getLine() {
		return line;
	}
	
} // end BusEdge

