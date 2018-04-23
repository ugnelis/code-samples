class HeapSort {
    public static void sort(int[] array) {
        int heapSize = array.length;
        buildHeap(array, heapSize);

        while (heapSize > 1) {
            swap(array, 0, heapSize - 1);
            heapSize--;
            heapify(array, heapSize, 0);
        }        
    }
    
    private static void buildHeap(int[] array, int heapSize) {
        for (int i = (int)(array.length / 2); i >= 0; i--) {
            heapify(array, heapSize, i);
        }
    }
    
    /**
      * Creates max heap.
      */
    private static void heapify(int[] array, int heapSize, int i) {
        int left = i * 2 + 1;
        int right = i * 2 + 2;
        int largest = i;
        
        if (left < heapSize && array[left] > array[largest]) {
            largest = left;
        }
        
        if (right < heapSize && array[right] > array[largest]) {
            largest = right;
        }
        
        // Swap numbers.
        if (largest != i) {
            swap(array, i, largest);
            
            // Heapify the affected sub-tree.
            heapify(array, heapSize, largest);
        }
    }
    
    private static void swap(int[] array, int a, int b) {
        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }
}

public class App {
    public static void main(String[] args) {
        int[] array = {1, 5, 6, 48, 2, 3, 7, 20, 11, 12};
        
        HeapSort.sort(array);
        for (int element: array) {
            System.out.println(element);
        }
    }
}
