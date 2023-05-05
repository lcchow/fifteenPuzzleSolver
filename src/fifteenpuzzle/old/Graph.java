package fifteenpuzzle.old;

import fifteenpuzzle.Vertex;

import java.util.*;

public class Graph<T> {
    Set<Vertex<T>> vertices;
    Map<Vertex<T>, Set<Vertex<T>>> edges;

    public Graph() {
        vertices = new HashSet<Vertex<T>>();
        edges = new HashMap<Vertex<T>,Set<Vertex<T>>>();
    }

    public boolean addVertex(Vertex<T> vertex) {
        boolean added = vertices.add(vertex);
        if (added) {
            edges.put(vertex,new HashSet<Vertex<T>>());
        }
        return added;
    }

    public boolean addEdge(Vertex<T> v, Vertex<T> u) {
        if (vertices.contains(v) && vertices.contains(u)) {
            return (edges.get(v).add(u) && edges.get(u).add(v));
        } else {
            return false;
        }
    }



}
