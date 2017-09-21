package cs6301.g00;

public class Utils {
<<<<<<< HEAD
    public Integer[] getRandomArray(int size) {
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }

        Shuffle.shuffle(arr);
        return arr;

    }
=======
	public Integer[] getRandomArray(int size){
		Integer[] arr = new Integer[size];
		for(int i=0; i<size; i++){
			arr[i] = i;
		}
		
		Shuffle.shuffle(arr);
		return arr;
		
	}
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900

}
