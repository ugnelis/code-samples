class LinkedList<T> {
    class Node<T> {
        T data;
        Node<T> next;
    
        public Node(T data) {
            this.data = data;
        }
    }

    private Node<T> headNode;
    private int size;

    public LinkedList() {
    }

    public void addAtHead(T data) {
        Node<T> tempNode = headNode;
        headNode = new Node<T>(data);
        headNode.next = tempNode;
        size++;
    }

    public void addAtTail(T data) {
        if (headNode == null) {
            addAtHead(data);
            return;
        }

        Node<T> tempNode = headNode;
        while (tempNode.next != null) {
            tempNode = tempNode.next;
        }
        tempNode.next = new Node<T>(data);
        size++;
    }

    public void addAtIndex(int index, T data) {
        if (headNode == null) {
            addAtHead(data);
            return;
        }

        Node<T> tempNode = headNode;
        Node<T> holderNode;

        for (int i = 0; i < index - 1 && tempNode.next != null; ++i) {
            tempNode = tempNode.next;
        }

        Node<T> newNode = new Node<T>(data);
        holderNode = tempNode.next;
        tempNode.next = newNode;
        newNode.next = holderNode;
        size++;
    }
 
    public void deleteAtIndex(int index) {
        if (headNode == null) {
            return;
        }
 
        Node<T> tempNode = headNode;

        for (int i = 0; i < index - 1 && tempNode.next != null; ++i) {
            tempNode = tempNode.next;
        }

        tempNode.next = tempNode.next.next;
        size--;
    }

    public void print() {
        Node<T> tempNode = headNode;
        if (tempNode != null) {
            System.out.println(tempNode.data);
        }
        while (tempNode.next != null) {
            tempNode = tempNode.next;
            System.out.println(tempNode.data);
        }
    }

    public int getSize() {
        return size;
    }
}

public class App {
    public static void main(String args[]) {
        LinkedList<Integer> list = new LinkedList<>();
        list.addAtTail(1);
        list.addAtTail(2);
        list.addAtTail(3);
        list.addAtTail(4);
        list.addAtTail(5);
        list.deleteAtIndex(2);
        list.print();
    }
}
