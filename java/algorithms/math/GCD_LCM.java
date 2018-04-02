public class App {
    /*
     * Greatest Common Divisor. Euclidean algorithm.
     */
    public static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    /*
     * Least Common Multiple.
     */
    public static int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }

    public static void main(String[] args) {
        int a = 1112;
        int b = 695;
        System.out.printf("GDC = %d\n", gcd(a, b));
        System.out.printf("LCM = %d\n", lcm(a, b));
    }
}
