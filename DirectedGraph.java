
import java.util.*;

class DirectedGraph<E extends Edge> {
	private List<E> nodes[];

	DirectedGraph(int noOfNodes) {
		nodes = (List<E>[]) new List[noOfNodes]; //creates an Array of lists<E>
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new ArrayList<>();		//Adds an empty instance of an
		}										//ArrayList in each
	}

	/**
	 * Adds edge between nodes, using value from.
	 * @param e the edge being added
	 */
	void addEdge(E e) {
		if(e==null) {
			return;
		} if (e.from < 0 || e.from >= nodes.length) {
			throw new IndexOutOfBoundsException();
		}
		nodes[e.from].add(e); //Adds all outgoing edges from a "node/bus stop"
	}

	/**
	 * Calculates the shortestPath between two Nodes
	 * @param from Starting Node
	 * @param to   End Node
	 * @return An iterator of the the list of Edges that makes the shortest path.
	 */
	Iterator<E> shortestPath(int from, int to) {

		PriorityQueue<CompDijkstraPath<E>> pQ = new PriorityQueue<>();
		List<CompDijkstraPath<E>> listOfCompDijkstraPaths = new ArrayList<>();
		CompDijkstraPath<E> compDijkstraPath;
		boolean [] visitedNodes; // keeps track of visited nodes

		//adds new compDijkstraPath for each "node"
		for (List<E> list : nodes) {
			listOfCompDijkstraPaths.add(new CompDijkstraPath<>((list.get(0).from), 1000000, new ArrayList<E>()));
		}

		visitedNodes = new boolean[listOfCompDijkstraPaths.size()];

		//Sets the cost of the first node to 0 and adds it to pQ
		compDijkstraPath = listOfCompDijkstraPaths.get(from);
		compDijkstraPath.setCost(0);
		pQ.add(compDijkstraPath);

		while (!pQ.isEmpty()) {
			compDijkstraPath = pQ.poll();
			int indexOfTo = compDijkstraPath.getTo();

			if (!visitedNodes[indexOfTo]) {
				if (indexOfTo == to) {
					return compDijkstraPath.getIterator();
				}
				else {
					visitedNodes[indexOfTo] = true;
					List<E> adjacentEdges = nodes[indexOfTo];
					CompDijkstraPath<E> pathToNeighbour;
					for (E edge : adjacentEdges) {
						pathToNeighbour = listOfCompDijkstraPaths.get(edge.to);
						if (!visitedNodes[edge.to]) { //dont do math on path we came from, since weight is positive integers
							if (compDijkstraPath.getCost() + edge.getWeight() < pathToNeighbour.getCost()) {
								pathToNeighbour.setPath(new ArrayList<>(compDijkstraPath.getPath()));
								pathToNeighbour.addEdgeToPath(edge, compDijkstraPath.getCost());
								pQ.add(pathToNeighbour);
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Calculates the minumum spanning tree using Kruskals' Algorithm
	 * @return A minimum spanning tree
	 */
	Iterator<E> minimumSpanningTree() {
		List<E> minimumSpanningTree = new ArrayList<>(); // Minimum spanning tree
		CompKruskalEdge<E> currentEdge; // Variable for storing CompKruskalEdges
		List<CompKruskalEdge<E>> connectedComponents[];
		PriorityQueue<CompKruskalEdge<E>> pQ = new PriorityQueue<>(); // An priority Queue for handling CompKruskalEdges
		Map<CompKruskalEdge<E>, E> kruskalEdgeMap = new HashMap<>(); // Hashmap for storing CompKruskalEdges
		int big = 0; //indices to indicate big and small list
		int small;

		for (List<E> edgesFromNode: nodes) {
			for(E edge : edgesFromNode) {
				currentEdge = new CompKruskalEdge<E>(edge);
				kruskalEdgeMap.put(currentEdge, edge);
				pQ.add(currentEdge);
			}
		}

		connectedComponents = (List<CompKruskalEdge<E>>[]) new List[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			connectedComponents[i] = new ArrayList<>();
		}

		while (!pQ.isEmpty() && connectedComponents[big].size() < connectedComponents.length - 1 ) { // if priorityQueue is not empty and
			currentEdge = pQ.poll();							  					  // the spanning tree doesn't contain all nodes, keep applying algorithm.
			if (connectedComponents[currentEdge.getFrom()] != connectedComponents[currentEdge.getTo()]) {// Make sure that the nodes that belong to the edge isn't
				// in the same list, meaning, avoid creating circles
				if (connectedComponents[currentEdge.getFrom()].size() >= connectedComponents[currentEdge.getTo()].size()) { // Determines which index that hold
					big = currentEdge.getFrom();															// the bigger list and assigns values accordingly
					small = currentEdge.getTo();
				} else {
					big = currentEdge.getTo();
					small = currentEdge.getFrom();
				}
				connectedComponents[big].add(currentEdge); // Add the edge connecting the small and the big list.
				connectedComponents[big].addAll(connectedComponents[small]);
				for (CompKruskalEdge<E> e : connectedComponents[small]) { // for all elements in the smaller list, add them to the bigger list
					connectedComponents[e.getFrom()] = connectedComponents[big];
					connectedComponents[e.getTo()] = connectedComponents[big];
				}
				connectedComponents[small] = connectedComponents[big]; // make sure that the list that points to small list now points to bigger list
			}
		}
		for (CompKruskalEdge<E> edge : connectedComponents[big]) { // for all elements in the big list, add them
			minimumSpanningTree.add(kruskalEdgeMap.get(edge)); // to minimumSpanningTree
		}
		if(minimumSpanningTree.size() == nodes.length-1) { // we know that in an mst the
			return minimumSpanningTree.iterator();		   // amount of vertices is one more than the amount of edges
		}
		return null;
	}
}