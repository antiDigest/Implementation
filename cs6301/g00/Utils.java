package cs6301.g00;

public class Utils {

    public Integer[] getRandomArray(int size) {
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }

        Shuffle.shuffle(arr);
        return arr;

    }


}
