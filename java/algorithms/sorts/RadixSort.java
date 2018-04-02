class RadixSort {
    public static void sort(int[] array) {
        int maxValue = getMaxValue(array);
        
        // Run cicle for every digit.
        for (int i = 1; maxValue / i > 0; i *= 10) {
            // Use counting sort for every digit.
            countingSort(array, i);
        }
    }
    
    private static void countingSort(int[] array, int place) {
        int[] result = new int[array.length];
        int[] count = new int[10];
        
        // Store count of occurrences.
        for (int i = 0; i < array.length; i++) {
            int digit = getDigit(array[i], place);
            count[digit]++;
        }
        
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }
        
        // Build the result array.
        for (int i = array.length - 1; i >= 0; i--) {
            int digit = getDigit(array[i], place);
            
            result[count[digit] - 1] = array[i];
            count[digit]--;
        }
        
        // Copy result to array.
        for (int i = 0; i < array.length; i++) {
            array[i] = result[i];
        }
    }
    
    private static int getMaxValue(int[] array) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
    
    private static int getDigit(int value, int digitPlace) {
        return (value / digitPlace) % 10;
    }
}

public class App {
    public static void main(String[] args) {
        int[] array = {1, 5, 6, 48, 2, 3, 7, 20, 11, 12};
        
        RadixSort.sort(array);
        for (int element: array) {
            System.out.println(element);
        }
    }
}
