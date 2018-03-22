import java.util.EmptyStackException;

/*
 * Stack uses LIFO ordering.
 */
class Stack<T> {
    private static class Node<T> {
        private T data;
        private Node<T> next;
        
        public Node(T data) {
            this.data = data;
        }
    }
    
    private Node<T> top;
    
    public Stack() {
    }
    
    public T pop() {
        if (top == null)
        {
            throw new EmptyStackException();
        }
        
        T item = top.data;
        top = top.next;
        
        return item; 
    }
    
    public T peek() {
        if (top == null)
        {
            throw new EmptyStackException();
        }
        
        return top.data;
    }
    
    public void push(T item) {
        Node<T> newNode = new Node<T>(item);
        newNode.next = top;
        top = newNode;
    }
    
    public boolean isEmpty() {
        return top == null;
    }
}

public class App {
    public static void main(String args[]) {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.pop();

        // Answer has to be 3.
        System.out.println(stack.peek());
    }
}
