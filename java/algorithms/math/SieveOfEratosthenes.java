import java.util.List;
import java.util.ArrayList;

public class SieveOfEratosthenes {

    /*
     * Sieve of Eratosthenes.
     */
    public static List<Integer> findPrimes(int limit) {
        List<Integer> list = new ArrayList<>();
        boolean [] isComposite = new boolean[limit + 1];

        for (int i = 2; i <= limit; i++) {
            if (!isComposite [i]) {
                list.add(i);
                
                int multiple = 2;
                while (i * multiple <= limit) {
                    isComposite [i * multiple] = true;
                    multiple++;
                }
            }
        }
        return list;
    }
    
    public static void main(String[] args) {
        List<Integer> list = findPrimes(13);
        for (int item: list) {
            System.out.println(item);
        }
    }
}
