import java.util.*;

class Node {
    int val;
    List<Node> neighbours;

    Node(){
        neighbours = new ArrayList<>();
    }
}

public class CloneGraph {

    public Node cloneGraph(Node node) {
        if(node == null)
            return null;

        Map<Integer, Node> map = new HashMap<>();
        return clone(node, map);
    }

    private Node clone(Node node, Map<Integer, Node> map) {

        if (node == null) {
            return null;
        }

        if (map.containsKey(node.val)) {
            return map.get(node.val);
        }

        Node new_node = new Node(node.val);
        map.add(node.val, new_node);

        for (Node neighbour : node.getNeighbours()) {
            if(map.containsKey(neighbour.val)){
                continue;
            }

            //Node new_node = new Node(neighbour.val);
            clone(neighbour, map);

        }
        return new_node;

    }

}