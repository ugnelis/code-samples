import java.util.NoSuchElementException;

/*
 * Queue uses FIFO ordering.
 */
class Queue<T> {
    private static class Node<T> {
        private T data;
        private Node<T> next;
        
        public Node(T data) {
            this.data = data;
        }
    }
    
    private Node<T> first;
    private Node<T> last;
    
    public Queue() {
    }
    
    public void add(T item) {
        Node<T> newNode = new Node<T>(item);
        if (last != null) {
            last.next = newNode;
        }
        
        last = newNode;
        
        if (first == null) {
            first = last; 
        }
    }
    
    public T remove() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        
        T item = first.data;
        first = first.next;
        
        if (first == null) {
            last = null;
        }
        
        return item;
    }
    
    public T peek() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        
        return first.data;
    }
    
    public boolean isEmpty() {
        return first == null;
    }
}

public class App {
    public static void main(String args[]) {
        Queue<Integer> queue = new Queue<>();
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(4);
        queue.remove();

        // Answer has to be 2.
        System.out.println(queue.peek());
    }
}
