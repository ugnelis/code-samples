class BubbleSort {
    public static void sort(int[] array) {
        for (int i = 0; i < array.length; ++i) {
            for (int j = 1; j < (array.length - i); ++j) {
                if (array[j - 1] > array[j]) {
                    int temp = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = temp; 
                }
            }
        }
    }
}

public class App {
    public static void main(String[] args) {
        int[] array = {1, 5, 6, 48, 2, 3, 7, 20, 11, 12};
        
        BubbleSort.sort(array);
        for (int element: array) {
            System.out.println(element);
        }
    }
}
