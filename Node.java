import java.util.ArrayList;
import java.util.List;

public class Node {

    protected int nodeNr;
    protected List<Edge> adjacentNodes;
    protected boolean visited = false;
    public Node(int nodeNr){
        this.nodeNr = nodeNr;
        adjacentNodes = new ArrayList<>();
    }
    public void addNeighbour(Edge e){
        if(!adjacentNodes.contains(e))
            adjacentNodes.add(e);
    }
}
