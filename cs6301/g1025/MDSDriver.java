package cs6301.g1025;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeSet;

import cs6301.g1025.MDS.Pair;

public class MDSDriver {

	static int Itemidslength = 50000;
	static int descriptionlength = 700;
	static int supplierslength = 5000;
	static int pairslength = 5000;

	static int supplierseed = 50;
	static int itemseed = 10;
	static int descriptionseed = 70;
	static int reputationseed = 40;
	static int rpriceseed = 90;
	static int randomSubArraySeed = 60;

	static Long[] ItemId = new Long[Itemidslength];
	static Long[][] descriptionId = new Long[Itemidslength][descriptionlength];
	static Long[] supplierId = new Long[supplierslength];
	static float[] reputation = new float[supplierslength];
	static Pair[] pairs = new Pair[pairslength];

	static HashSet<Long> descriptionset = new HashSet<Long>();

	static MDS md = new MDS();

	public static void main(String[] args) {
		createData();
		addDataToMaps();
		// printMapsData();
		testMaps();
		testQueries();

	}

	static void testQueries() {
		testfindItem();
		testfindItemArguments();
		testPurge();
		testRemove();
		testRemoveArguments();
		testRemoveAll();
		testIdentical();
		testfindSupplier();
		

	}
	public static void testfindSupplier(){
		/*
		 * given an id, return an array of suppliers who sell that item, ordered by
		 * the price at which they sell the item (non-decreasing order).
		 */
		//public Long[] findSupplier(Long id) {
		Random r=new Random();
		Long Item = ItemId[r.nextInt(Itemidslength)];
		Arrays.sort(pairs);
		Long[] result=md.findSupplier(Item);
		int i=0;
		int prev=0;
		for(Pair p:pairs){
			
			if(p.id==Item){
				if(prev>p.price){
					System.out.println("testfindSupplier failed");
					return;
				}
				
				
				if(i<result.length &&!md.priceTable.get(p).contains(result[i++])){
					System.out.println("testfindSupplier failed");
					return;
				}
				prev=p.price;
				
			}
		}
	 
		System.out.println("testfindSupplier Result "+ Arrays.toString(result));
	}
	
	private static void testIdentical() {
		// TODO Auto-generated method stub
		System.out.println("Identical() operation result "+Arrays.toString(md.identical()));
		
	}

	private static void testRemoveAll() {
		/**
		 * remove the elements of the array from the description of all items.
		 * Return the number of items that lost one or more terms from their
		 * descriptions.
		 */
		//public int removeAll(Long[] arr) {
		Long[] arr=RandomSubArray(descriptionset.toArray(new Long[descriptionset.size()]),60);
		md.removeAll(arr);
		System.out.println("After public int removeAll(Long[] arr)  Operation:");
		testMaps();
		
	}

	private static void testRemoveArguments() {
		/**
		 * remove from the given id's description those elements that are in the
		 * given array. It is possible that some elements of the array are not part
		 * of the item's description. Return the number of elements that were
		 * actually removed from the description.
		 */
		//public int remove(Long id, Long[] arr) {
		Long[] arr=RandomSubArray(descriptionset.toArray(new Long[descriptionset.size()]),60);
		Random r = new Random();
		Long itemToRemove = ItemId[r.nextInt(Itemidslength)];
		md.remove(itemToRemove,arr);
		System.out.println("After public int remove(Long id, Long[] arr)  Operation:");
		testMaps();
		
		
	}

	

	private static void testRemove() {
		/**
		 * remove item from storage. Returns the sum of the Longs that are in
		 * the description of the item deleted (or 0, if such an id did not
		 * exist).
		 */
		// public Long remove(Long id) {
		Random r = new Random();
		Long itemToRemove = ItemId[r.nextInt(Itemidslength)];
		md.remove(itemToRemove);
		System.out.println("After public Long remove(Long id) Operation:");
		testMaps();
	}

	private static void testPurge() {
		// public Long[] purge(float maxReputation) {
		float maxReputation = RandomWithinLimits(0, 5, new Random());
		md.purge(maxReputation);
		System.out.println("After Purge Operation:");
		testMaps();
	}

	static void testfindItem() {
		// public Long[] findItem(Long[] arr) {
		Long[] arr = RandomSubArray(descriptionset.toArray(new Long[descriptionset.size()]), 50);

		Long[] res = md.findItem(arr);
		HashSet<Long> tsarr = new HashSet<Long>();
		for (long d : arr) {
			tsarr.add(d);
		}
		int prev = Integer.MAX_VALUE;
		int curr = 0;
		for (int i = 0; i < res.length; i++) {
			curr = 0;
			for (long d : tsarr) {
				if (md.itemTree.get(res[i]).contains(d)) {
					curr++;
				}
			}
			if (curr > prev) {
				System.out.println("public Long[] findItem(Long[] arr) - failed");
				return;
			}
			curr = prev;
		}
		System.out.println("public Long[] findItem(Long[] arr) - passed");

	}

	static void testfindItemArguments() {
		// Long[] findItem(Long n, int minPrice, int maxPrice, float
		// minReputation) {
		/**
		 * given a Long n, return an array of items whose description contains
		 * n, which have one or more suppliers whose reputation meets or exceeds
		 * the given minimum reputation, that sell that item at a price that
		 * falls within the price range [minPrice, maxPrice] given. Items should
		 * be sorted in order of their minimum price charged by a supplier for
		 * that item (non-decreasing order).
		 */
		Long[] arr = descriptionset.toArray(new Long[descriptionset.size()]);
		Random r = new Random();
		long n = arr[r.nextInt(arr.length)];
		int maxprice = pairs[r.nextInt(pairs.length)].price;
		int minprice = r.nextInt(maxprice);

		float minReputation = RandomWithinLimits(0, 5, r);
		Long[] res = md.findItem(n, minprice, maxprice, minReputation);
		HashSet<Long> minReputationSuppliersSet = new HashSet<Long>();

		for (Entry<Float, TreeSet<Long>> e : md.supplierRating.subMap(minReputation, true, (float) 5, true)
				.entrySet()) {
			minReputationSuppliersSet.addAll(e.getValue());

		}

		int prev = Integer.MIN_VALUE;

		for (Long item : res) {
			if (!md.descTable.containsKey(n) || !md.descTable.get(n).contains(item)) {

				System.out.println("findItem(Long n, int minPrice, int maxPrice, float minReputation)-failed");
				return;
			}
			int local = 0;
			boolean flag = false;
			for (Entry<Pair, TreeSet<Long>> e : md.priceTable.subMap(new Pair(item, minprice), new Pair(item, maxprice))
					.entrySet()) {
				for (Long supplier : e.getValue()) {
					if (minReputationSuppliersSet.contains(supplier)) {
						flag = true;
						local = e.getKey().price;
						break;
					}
				}

			}
			if (!flag) {
				System.out.println("findItem(Long n, int minPrice, int maxPrice, float minReputation)-failed");
				return;
			}
			if (local < prev) {
				System.out.println("findItem(Long n, int minPrice, int maxPrice, float minReputation)-failed");
				return;
			}
			prev = local;

		}

		System.out.println("findItem(Long n, int minPrice, int maxPrice, float minReputation)-passed");
	}

	static void testMaps() {

		for (Entry<Long, HashSet<Long>> e : md.itemTree.entrySet()) {
			for (Long d : e.getValue()) {
				if (!md.descTable.containsKey(d)) {
					System.out.println(
							"descriptionTable does not contain  " + d + " which is there for item " + e.getKey());
					return;
				} else if (!md.descTable.get(d).contains(e.getKey())) {
					System.out
							.println("descriptionTable does not contain item :" + e.getKey() + " for description " + d);
					return;
				}
			}
		}

		for (Entry<Long, Float> e : md.supplierTree.entrySet()) {
			if (!md.supplierRating.containsKey(e.getValue())) {
				System.out.println("SupplierRating does not contain reputation " + e.getValue()
						+ " which is there for supplier " + e.getKey());
				return;
			} else if (!md.supplierRating.get(e.getValue()).contains(e.getKey())) {
				System.out.println(
						"SupplierRating does not contain supplier :" + e.getKey() + " for reputation " + e.getValue());
				return;
			}
		}

		for (Entry<Long, TreeSet<Pair>> e : md.vendor.entrySet()) {
			for (Pair p : e.getValue()) {
				if (!md.priceTable.containsKey(p)) {
					System.out.println(
							"PriceTable does not contain pair " + p + " which is there for supplier " + e.getKey());
					return;
				} else if (!md.priceTable.get(p).contains(e.getKey())) {
					System.out.println("priceTable does not contain supplier :" + e.getKey() + " for pair " + p);
					return;
				}
			}
		}
		System.out.println("All Maps Data Valid");
	}

	static void addDataToMaps() {

		// public boolean add(Long id, Long[] description) {
		for (int i = 0; i < ItemId.length; i++) {
			md.add(ItemId[i], descriptionId[i]);
		}

		// public boolean add(Long supplier, float reputation) {
		for (int i = 0; i < supplierId.length; i++) {
			md.add(supplierId[i], reputation[i]);
		}

		// add(Long supplier, Pair[] idPrice) {
		for (int i = 0; i < pairs.length; i++) {
			Pair[] pairsubarray = RandomSubArray(pairs, randomSubArraySeed);
			md.add(supplierId[i], pairsubarray);
		}

	}

	static void printMapsData() {

		System.out.println("Supplier Tree data is " + md.supplierTree);
		System.out.println("Supplier Rating data is " + md.supplierRating);
		System.out.println("PriceTable data is " + md.priceTable);

		System.out.println("Supplier + Pairs " + md.vendor);
		System.out.println("desctable data is " + md.descTable);

		System.out.println("Item description data is ");
		for (Entry<Long, HashSet<Long>> e : md.itemTree.entrySet()) {
			System.out.print("itemId : " + e.getKey() + " && description is " + e.getValue() + ", ");
		}

		System.out.println();
	}

	static void createData() {

		Random rItem = new Random(itemseed);
		Random rDescription = new Random(descriptionseed);
		Random rsupplier = new Random(supplierseed);
		Random rreputation = new Random(reputationseed);
		Random rPrice = new Random(rpriceseed);

		for (int i = 0; i < Itemidslength; i++) {
			ItemId[i] = (long) rItem.nextInt(Itemidslength * 2);
			for (int j = 0; j < descriptionlength; j++) {
				descriptionId[i][j] = (long) rDescription.nextInt(descriptionlength * 2);
				descriptionset.add(descriptionId[i][j]);
			}
		}

		for (int i = 0; i < supplierslength; i++) {
			supplierId[i] = (long) rsupplier.nextInt(supplierslength * 2);
			reputation[i] = RandomWithinLimits(0, 5, rreputation);
		}

		Long[] selectedItems = arrayShuffle(ItemId, pairslength, rPrice);

		for (int i = 0; i < selectedItems.length; i++) {
			Pair p = new Pair(selectedItems[i], rPrice.nextInt(selectedItems.length * 2));
			pairs[i] = p;
		}

	}

	static Long[] arrayShuffle(Long[] itemId, int pricelength, Random rprice) {

		Long selectItemIds[] = itemId.clone();
		for (int i = 0; i < pricelength; i++) {
			long temp = selectItemIds[i];
			int randomselection = (int) (pricelength + rprice.nextDouble() * (itemId.length - pricelength - 1));
			selectItemIds[i] = selectItemIds[randomselection];
			selectItemIds[randomselection] = temp;
		}
		Long[] b1 = Arrays.copyOfRange(selectItemIds, 0, pricelength);
		return b1;

	}

	static <T> T[] RandomSubArray(T[] array, int seed) {
		Random r = new Random(seed);
		int to = (int) RandomWithinLimits(0, array.length, r);
		int from = (int) RandomWithinLimits(0, to, r);
		return Arrays.copyOfRange(array, from, to);
	}

	static float RandomWithinLimits(float low, float high, Random r) {

		float res = low + (float) (r.nextFloat() * (high - low));
		DecimalFormat df = new DecimalFormat("#.0");
		float randomNum = Float.parseFloat(df.format(res));
		return randomNum;

	}

}
