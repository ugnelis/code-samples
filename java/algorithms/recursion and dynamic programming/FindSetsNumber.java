import java.util.HashMap;
import java.util.Map;

/*
 * Find number of sets that add up to given sum number.
 */
public class App {
    
    /* 
     * Recursive.
     */
    public static int recursiveCount(int[] array, int total) {
        return recursiveCount(array, total, 0);
    }
    
    public static int recursiveCount(int[] array, int total, int i) {
        if (total == 0) {
            return 1;
        } else if (total < 0) {
            return 0;
        } else if (i >= array.length) {
            return 0;
        } else {
            return recursiveCount(array, total, i + 1) + recursiveCount(array, total - array[i], i + 1);
        }
    }
    
    /* 
     * Top-Down.
     */
    public static int topDownCount(int[] array, int total) {
        Map<String, Integer> memo = new HashMap<>();
        return topDownCount(array, total, 0, memo);
    }
    
    public static int topDownCount(int[] array, int total, int i, Map<String, Integer> memo) {
        String key = Integer.toString(total) + ":" + Integer.toString(i);
        if(memo.containsKey(key)) {
            return memo.get(key);
        }
        
        if (total == 0) {
            return 1;
        } else if (total < 0) {
            return 0;
        } else if (i >= array.length) {
            return 0;
        } else {
            int result = topDownCount(array, total, i + 1, memo) + topDownCount(array, total - array[i], i + 1, memo);
            memo.put(key, result);
            return result;
        }
    }
    
    public static void main(String[] args) {
        int set[] = {1, 4, 5, 10};
        int sum = 10;
        System.out.println(recursiveCount(set, sum));
        System.out.println(topDownCount(set, sum));
    }
}
