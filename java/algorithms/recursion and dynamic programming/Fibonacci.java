class Fibonacci {

    /* 
     * Top-Down Dynamic Programming.
     * O(n^2).
     */
    public static int recursive(int number) {
        if (number == 0 || number == 1) {
            return number;
        }
        
        return recursive(number - 2) + recursive(number - 1);
    }
    
    /* 
     * Top-Down Dynamic Programming.
     * O(n).
     */
    public static int topDownDynamic(int number) {
        return topDownDynamic(number, new int[number + 1]);
    }
    
    private static int topDownDynamic(int number, int[] memo) {
        if (number == 0 || number == 1) {
            return number;
        }
        
        if (memo[number] == 0) {
            memo[number] = topDownDynamic(number - 2, memo) + topDownDynamic(number - 1, memo);
        }
        
        return memo[number];
    }
    
    /* 
     * Bottom-Up Dynamic Programming.
     * O(n).
     */
    public static int bottomUpDynamic(int number) {
        int memo[] = new int[number];
        memo[0] = 0;
        memo[1] = 1;
        
        for (int i = 2; i < number; i++) {
            memo[i] = memo[i - 2] + memo[i - 1];
        }
        return memo[number - 2] + memo[number - 1];
    }
    
    /* 
     * Bottom-Up Dynamic Programming. Without memory.
     * O(n).
     */
    public static int bottomUpDynamicWithoutMemory(int number) {
        if (number == 0 || number == 1) {
            return number;
        }
        int a = 0;
        int b = 1;
        
        for (int i = 2; i < number; i++) {
            int temp = a + b;
            a = b;
            b = temp;
        }
        return a + b;
    }
}

public class App {
    
    public static void main(String[] args) {
        System.out.println(Fibonacci.recursive(7));
        System.out.println(Fibonacci.recursive(7));
        System.out.println(Fibonacci.bottomUpDynamic(7));
        System.out.println(Fibonacci.bottomUpDynamicWithoutMemory(7));
    }
}
