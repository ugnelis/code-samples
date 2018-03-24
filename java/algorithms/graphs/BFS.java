import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

class Node<T> {
    private boolean isVisited;
    private T data;
    private List<Node<T>> adjacents = new ArrayList<Node<T>>();

    public Node(T data) {
        this.data = data;    
    }
    
    public void setIsVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }
    
    public boolean getIsVisited() {
        return isVisited;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public T getData() {
        return data;
    }
    
    public void addAdjacent(Node node) {
        adjacents.add(node);
    }
    
    public List<Node<T>> getAdjacents() {
        return adjacents;
    }
}

/* 
 * Breadth-First Search
 */
class BFS<T> {
    public void search(Node<T> root) {
        Queue<Node<T>> queue = new LinkedList<>();
        root.setIsVisited(true);
        queue.add(root);
        
        while (queue.size() > 0) {
            Node<T> node = queue.poll();
            visit(node);
            
            for(Node<T> item: node.getAdjacents()) {
                if (!item.getIsVisited()) {
                    item.setIsVisited(true);
                    queue.add(item);
                }
            }
        }
    }
    
    public void visit(Node<T> node) {
        System.out.println(node.getData());
    }
}

public class App {
    public static void main(String[] args) {
        Node<String> nodeA = new Node<>("A");
        Node<String> nodeB = new Node<>("B");
        Node<String> nodeC = new Node<>("C");
        Node<String> nodeD = new Node<>("D");
        Node<String> nodeE = new Node<>("E");
        nodeA.addAdjacent(nodeB);
        nodeB.addAdjacent(nodeA);
        nodeB.addAdjacent(nodeC);
        nodeB.addAdjacent(nodeE);
        nodeC.addAdjacent(nodeB);
        nodeC.addAdjacent(nodeD);
        nodeC.addAdjacent(nodeE);
        nodeD.addAdjacent(nodeC);
        nodeE.addAdjacent(nodeB);
        
        BFS<String> bfs = new BFS<>();
        bfs.search(nodeA);
    }
}
