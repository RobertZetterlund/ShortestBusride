
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


class CompDijkstraPath<E extends Edge> implements Comparable<CompDijkstraPath> {
    private int to;
    private int cost;
    private List<E> path;

    /**
     *
     * @param to represents where the Path leads
     * @param cost how much is the cost to get ther
     * @param path Which Edges are used to get there
     */
    CompDijkstraPath(int to, int cost, ArrayList<E> path) {
        this.to = to;
        this.cost = cost;
        this.path = path;
    }

    int getTo() {
        return to;
    }

    int getCost() {
        return cost;
    }

    void setCost(int cost) {
        this.cost = cost;
    }

    Iterator<E> getIterator() {
        return path.iterator();
    }

    List<E> getPath() {
        return path;
    }

    void setPath(List<E> path) {
        this.path = path;
    }

    /**
     * Adds an edge to the path, updates the cost of the path and changes the destination (to)
     * @param edge the edge that should be added.
     * @param prevCost the previous cost
     */
    void addEdgeToPath(E edge, int prevCost) {
        path.add(edge);
        cost = (int) edge.getWeight() + prevCost;
    }

    /**
     *
     * @param e CompDijkstraPath to compare to.
     * @return negative value if this CompDijkstraPath.cost is smaller then e.cost,
     *          0 if the costs are equal,
     *          positive value if this CompDijkstraPath.cost is greater then e.cost
     */
    @Override
    public int compareTo(CompDijkstraPath e) {
        if (e == null) {
            throw new NullPointerException();
        }
        return (Integer.compare(this.cost,e.cost));
    }
}
