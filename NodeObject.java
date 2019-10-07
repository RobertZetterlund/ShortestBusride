/**
* A node object is used in the NodeTable class to map between 
* nod number and node name.
*/
public class NodeObject  {

	/** the number */
	private int nodeNo;

	/** The  name.  */
	public String name;


	NodeObject(String newName) { 
		name = newName.trim();
	}

	public int getNodeNo() {
		return nodeNo;
	}

	public void setNodeNo(int no) {
		nodeNo = no;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		//return name + "," + nodeNo;
		return name;
	}
}
