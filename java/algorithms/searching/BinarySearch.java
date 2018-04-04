class BinarySearch {
    public static int find(int[] array, int x) {
        int low = 0;
        int high = array.length - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;
            if (array[mid] < x) {
                low = mid + 1;
            } else if (array[mid] > x) {
                high = mid - 1;
            } else {
                return mid;
            }
        }

        return -1;
    }
}

public class App {
    public static void main(String[] args) {
		int[] array = {1, 2, 3, 5, 6, 7, 11, 12, 20, 48};
        
        System.out.println(BinarySearch.find(array, 11));
    }
}
