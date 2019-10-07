class CompKruskalEdge<E extends Edge> implements Comparable<CompKruskalEdge<E>> {

    private int from,to; // Index in list of from and to node
    private double weight; // weight of edge

    CompKruskalEdge(E e){
        from = e.from;
        to = e.to;
        weight = e.getWeight();
    }

    int getFrom() {
        return from;
    }

    int getTo() {
        return to;
    }

    @Override
    public int compareTo(CompKruskalEdge<E> e) {
        if(e == null){
            throw new NullPointerException();
        }
        return Double.compare(this.weight, e.weight);
    }
}