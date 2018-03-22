class QuickSort {
    public static void sort(int[] array) {
        quicksort(array, 0, array.length - 1);
    }
    
    public static void quicksort(int[] array, int left, int right) {
        int index = partition(array, left, right);
        if (left < index - 1) {
            quicksort(array, left, index - 1);
        }
        if (index < right) {
            quicksort(array, index, right);
        }
    }
    
    public static int partition(int[] array, int left, int right) {
        int pivot = array[(left + right) / 2];
        
        while (left <= right) {
            while (array[left] < pivot) {
                left++;
            }
            
            while (array[right] > pivot) {
                right--;
            }
            
            if (left <= right) {
                // Swap elements.
                int temp = array[right];
                array[right] = array[left];
                array[left] = temp;
                
                left++;
                right--;
            }
        }
        return left;
    }
}

public class App {
    public static void main(String[] args) {
        int[] array = {1, 5, 6, 48, 2, 3, 7, 20, 11, 12};
        
        QuickSort.sort(array);
        for (int element: array) {
            System.out.println(element);
        }
    }
}
