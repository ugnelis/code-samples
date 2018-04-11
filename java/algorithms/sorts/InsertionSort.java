class InsertionSort {
    public static void sort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int item = array[i];
            int indexHole = i;
            while (indexHole > 0 && array[indexHole - 1] > item) {
                array[indexHole] = array[--indexHole];
            }
            array[indexHole] = item;
        }
    }
}

public class App {
    public static void main(String[] args) {
        int[] array = {1, 5, 6, 48, 2, 3, 7, 20, 11, 12};
        
        InsertionSort.sort(array);
        for (int element: array) {
            System.out.println(element);
        }
    }
}
