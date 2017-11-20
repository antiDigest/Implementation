package cs6301.g1025;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeSet;

import cs6301.g1025.MDS.Pair;

public class MDSDriver {

	static int Itemidslength = 10;
	static int descriptionlength = 7;
	static int supplierslength = 7;
	static int pairslength = 7;

	static int supplierseed = 30;
	static int itemseed = 10;
	static int descriptionseed = 20;
	static int reputationseed = 40;
	static int rpriceseed = 50;
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
		addData();
		System.out.println("All maps like foreign and primary key relationships " + testMaps());

		testQueries();

	}

	static void testQueries() {
		testfindItem();

	}

	static void testfindItem() {
		// public Long[] findItem(Long[] arr) {
		Long[] arr = RandomSubArray(descriptionset.toArray(new Long[descriptionset.size()]), 50);
		Long[] res = md.findItem(arr);
		for (int i = 0; i < res.length; i++) {
			for (int j = 0; j < arr.length; j++) {
                 
			}
		}

	}

	static void addData() {

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

		System.out.println("Supplier Tree data is " + md.supplierTree);
		System.out.println("Supplier Rating data is " + md.supplierRating);
		System.out.println("PriceTable data is " + md.priceTable);

		System.out.println("Supplier + Pairs " + md.vendor);
		System.out.println("desctable data is " + md.descTable);

		System.out.println("Item description data is ");
		for (Entry<Long, Long[]> e : md.itemTree.entrySet()) {
			System.out.print("itemId : " + e.getKey() + " && description is" + Arrays.toString(e.getValue()) + ", ");
		}

		System.out.println();

	}

	static String testMaps() {

		for (Entry<Long, Float> e : md.supplierTree.entrySet()) {
			if (!md.supplierRating.containsKey(e.getValue())) {
				return "SupplierRating does not contain reputation " + e.getValue() + " which is there for supplier "
						+ e.getKey();
			} else if (!md.supplierRating.get(e.getValue()).contains(e.getKey())) {
				return "SupplierRating does not contain supplier :" + e.getKey() + " for reputation " + e.getValue();
			}
		}

		for (Entry<Long, TreeSet<Pair>> e : md.vendor.entrySet()) {
			for (Pair p : e.getValue()) {
				if (!md.priceTable.containsKey(p)) {
					return "PriceTable does not contain pair " + p + " which is there for supplier " + e.getKey();
				} else if (!md.priceTable.get(p).contains(e.getKey())) {
					return "priceTable does not contain supplier :" + e.getKey() + " for pair " + p;

				}
			}
		}
		return "PASS";
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
}
