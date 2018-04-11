import java.util.List;
import java.util.ArrayList;

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


/**
 * Reference http://www.growingwiththeweb.com/2015/06/bucket-sort.html
 */
class BucketSort {
    private static final int DEFAULT_BUCKET_SIZE = 5;

    public static void sort(int[] array) {
        sort(array, DEFAULT_BUCKET_SIZE);
    }
    
    public static void sort(int[] array, int bucketSize) {
        if (array.length == 0) {
            return;
        }
        
        // Find minimum and maximum values.
        int minValue = array[0];
        int maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
            } else if (array[i] > maxValue) {
                maxValue = array[i];
            }
        }
        
        // Initialise buckets.
        int bucketCount = (maxValue - minValue) / bucketSize + 1;
        List<List<Integer>> buckets = new ArrayList<List<Integer>>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<Integer>());
        }
        
        // Distribute input array values into buckets.
        for (int i = 0; i < array.length; i++) {
            buckets.get((array[i] - minValue) / bucketSize).add(array[i]);
        }
        
        // Sort buckets and place back into input array.
        int currentIndex = 0;
        for (int i = 0; i < buckets.size(); i++) {
            int[] bucketArray = new int[buckets.get(i).size()];
            bucketArray = buckets.get(i)
                .stream()
                .mapToInt(Integer::intValue)
                .toArray();
            InsertionSort.sort(bucketArray);
            for (int j = 0; j < bucketArray.length; j++) {
                array[currentIndex++] = bucketArray[j];
            }
        }
    }
}

public class App {
    public static void main(String[] args) {
        int[] array = {1, 5, 6, 48, 2, 3, 7, 20, 11, 12};
        
        BucketSort.sort(array);
        for (int element: array) {
            System.out.println(element);
        }
    }
}
