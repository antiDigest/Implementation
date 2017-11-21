
package cs6301.g1025;

import java.util.*;
import java.util.Map.Entry;

public class MDS {

	static HashMap<Long, HashSet<Long>> itemTree = new HashMap<>();
	static HashMap<Long, TreeSet<Long>> descTable = new HashMap<>();
	static HashMap<Long, Float> supplierTree = new HashMap<>();
	static TreeMap<Float, TreeSet<Long>> supplierRating = new TreeMap<>();

	static HashMap<Long, TreeSet<Pair>> vendor = new HashMap<>();
	static TreeMap<Pair, TreeSet<Long>> priceTable = new TreeMap<>();

	public MDS() {
	}

	public static class Pair implements Comparable<Pair> {
		long id;
		int price;

		public Pair(long id, int price) {
			this.id = id;
			this.price = price;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			Pair o = (Pair) obj;
			if (id == o.id && this.price == o.price) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public int compareTo(Pair o) {
			if (o.id == this.id) {
				if (this.price > o.price) {
					return 1;
				} else if (this.price < o.price) {
					return -1;
				} else
					return 0;
			} else {

				Long id1 = o.id;
				return -id1.compareTo(this.id);

			}
		}

		@Override
		public String toString() {
			return "item id is " + this.id + " && price is " + this.price;
		}

	}

	/*
	 * add a new item. If an entry with the same id already exists, the new
	 * description is merged with the existing description of the item. Returns
	 * true if the item is new, and false otherwise.
	 */
	public boolean add(Long id, Long[] description) {

		HashSet<Long> desc = null;
		HashSet<Long> flag = itemTree.get(id);
		if (flag == null) {
			desc = new HashSet<Long>();
			itemTree.put(id, desc);
		} else {
			desc = flag;
		}
		for (long d : description) {
			TreeSet<Long> set = descTable.get(d);
			if (set == null) {
				set = new TreeSet<>();
			}
			set.add(id);
			descTable.put(d, set);
			desc.add(d);
		}
		if (flag == null)
			return true;
		else
			return false;

	}

	/*
	 * add a new supplier (Long) and their reputation (float in [0.0-5.0],
	 * single decimal place). If the supplier exists, their reputation is
	 * replaced by the new value. Return true if the supplier is new, and false
	 * otherwise.
	 */
	public boolean add(Long supplier, float reputation) {
		if (supplierTree.containsKey(supplier)) {
			removeFromSupplierTree(supplier);
			add(supplier, reputation);
		} else {
			supplierTree.put(supplier, reputation);
			TreeSet<Long> set = supplierRating.get(reputation);
			if (set == null) {
				set = new TreeSet<>();
			}
			set.add(supplier);
			supplierRating.put(reputation, set);
			return true;
		}
		return true;
	}

	/*
	 * Utility method removes the value from supplier tree
	 */
	public void removeFromSupplierTree(Long supplier) {
		Float rep = supplierTree.remove(supplier);
		if (rep != null) {
			TreeSet<Long> set = supplierRating.get(rep);
			if (set.size() > 1) {
				set.remove(supplier);
			} else {
				supplierRating.remove(rep);
			}
		}
	}

	/*
	 * Utility Function
	 */
	void addPriceTableEntry(Pair p, Long supplier) {
		TreeSet<Long> pt = priceTable.getOrDefault(p, new TreeSet<Long>());
		pt.add(supplier);
		priceTable.put(p, pt);

	}

	/*
	 * add products and their prices at which the supplier sells the product. If
	 * there is an entry for the price of an id by the same supplier, then the
	 * price is replaced by the new price. Returns the number of new entries
	 * created.
	 */
	public int add(Long supplier, Pair[] idPrice) {

		int count = 0;
		TreeSet<Pair> vendorPairs = vendor.get(supplier);
		for (Pair p : idPrice) {
			if (vendorPairs != null) {
				TreeSet<Pair> toadd = (TreeSet<Pair>) vendorPairs.subSet(new Pair(p.id, Integer.MIN_VALUE), true,
						new Pair(p.id, Integer.MAX_VALUE), true);
				Pair p1 = null;
				if (!toadd.isEmpty()) {
					p1 = toadd.first();
					if (!p1.equals(p)) {
						TreeSet<Long> t = priceTable.get(p1);
						if (t.size() > 1) {
							t.remove(supplier);
						} else {
							priceTable.remove(p1);
						}
						toadd.first().price = p.price;
						addPriceTableEntry(p, supplier);
					}

				} else {
					vendorPairs.add(p);
					addPriceTableEntry(p, supplier);
					count++;
				}

			} else {
				vendorPairs = vendor.getOrDefault(supplier, new TreeSet<Pair>());
				vendorPairs.add(p);
				vendor.put(supplier, vendorPairs);
				addPriceTableEntry(p, supplier);
				count++;
			}

		}
		return count;

	}

	/**
	 * return an array with the description of id. Return null if there is no
	 * item with this id.
	 */
	public Long[] description(Long id) {
		HashSet<Long> t = itemTree.get(id);
		if (t == null)
			return null;
		else
			return t.toArray(new Long[t.size()]);
	}

	/**
	 * given an array of Longs, return an array of items whose description
	 * contains one or more elements of the array, sorted by the number of
	 * elements of the array that are in the item's description (non-increasing
	 * order).
	 */
	// Overall time complexity nlogn
	public Long[] findItem(Long[] arr) {

		HashMap<Long, Integer> itemCounts = new HashMap<>();

		for (Long val : arr) {
			TreeSet<Long> set = descTable.get(val);
			if (set != null) {
				for (Long id : set) {
					Integer count = itemCounts.getOrDefault(id, 0);
					count = count + 1;
					itemCounts.put(id, count);
				}
			}
		}
		Set<Entry<Long, Integer>> entries = itemCounts.entrySet();
		Comparator<Entry<Long, Integer>> valueComparator = new Comparator<Entry<Long, Integer>>() {
			@Override
			public int compare(Entry<Long, Integer> e1, Entry<Long, Integer> e2) {
				Integer v1 = e1.getValue();
				Integer v2 = e2.getValue();
				return v2.compareTo(v1);
			}
		};
		List<Entry<Long, Integer>> listOfEntries = new ArrayList<Entry<Long, Integer>>(entries);
		Collections.sort(listOfEntries, valueComparator);
		Long[] newArr = new Long[listOfEntries.size()];
		int i = 0;
		for (Entry<Long, Integer> e : listOfEntries) {
			newArr[i++] = e.getKey();
		}
		return newArr;
	}

	class PriceFrame implements Comparable<PriceFrame> {
		long id;
		int price;

		PriceFrame(long id, int price) {
			this.id = id;
			this.price = price;
		}

		@Override
		public int compareTo(PriceFrame o) {
			return ((Integer) price).compareTo(o.price);
		}
	}

	/**
	 * given a Long n, return an array of items whose description contains n,
	 * which have one or more suppliers whose reputation meets or exceeds the
	 * given minimum reputation, that sell that item at a price that falls
	 * within the price range [minPrice, maxPrice] given. Items should be sorted
	 * in order of their minimum price charged by a supplier for that item
	 * (non-decreasing order).
	 */
	public Long[] findItem(Long n, int minPrice, int maxPrice, float minReputation) {

		TreeSet<PriceFrame> res = new TreeSet<PriceFrame>();
		TreeSet<Long> ids = descTable.get(n);

		for (Long id : ids) {
			boolean flag = false;
			for (Entry<Pair, TreeSet<Long>> e : priceTable.subMap(new Pair(id, minPrice), new Pair(id, maxPrice))
					.entrySet()) {
				for (Long supplier : e.getValue()) {
					if (supplierTree.get(supplier) >= minReputation) {
						res.add(new PriceFrame(id, e.getKey().price));
						flag = true;
						break;
					}
				}
				if (flag) {
					break;
				}

			}

		}
		Long[] items = new Long[res.size()];
		int i = 0;
		for (PriceFrame pf : res) {
			items[i++] = pf.id;
		}
		return items;
	}

	/*
	 * given an id, return an array of suppliers who sell that item, ordered by
	 * the price at which they sell the item (non-decreasing order).
	 */
	public Long[] findSupplier(Long id) {

		ArrayList<Long> res = new ArrayList<>();
		for (Entry<Pair, TreeSet<Long>> e : priceTable
				.subMap(new Pair(id, Integer.MIN_VALUE), new Pair(id, Integer.MAX_VALUE)).entrySet()) {
			res.addAll(e.getValue());
		}
		return res.toArray(new Long[res.size()]);

	}

	/*
	 * given an id and a minimum reputation, return an array of suppliers who
	 * sell that item, whose reputation meets or exceeds the given reputation.
	 * The array should be ordered by the price at which they sell the item
	 * (non-decreasing order).
	 */
	public Long[] findSupplier(Long id, float minReputation) {
		ArrayList<Long> res = new ArrayList<>();
		for (Entry<Pair, TreeSet<Long>> e : priceTable
				.subMap(new Pair(id, Integer.MIN_VALUE), new Pair(id, Integer.MAX_VALUE)).entrySet()) {
			for (Long t : e.getValue()) {
				if (supplierTree.get(t) >= minReputation) {
					res.add(t);
				}
			}
		}

		return res.toArray(new Long[res.size()]);
	}

	/**
	 * find suppliers selling 5 or more products, who have the same identical
	 * profile as another supplier: same reputation, and, sell the same set of
	 * products, at identical prices. This is a rare operation, so do not do
	 * additional work in the other operations so that this operation is fast.
	 * Creative solutions that are elegant and efficient will be awarded
	 * excellence credit. Return array of suppliers satisfying above condition.
	 * Make sure that each supplier appears only once in the returned array.
	 */
	public Long[] identical() {

		class EqualFrame {
			float reputation;
			TreeSet<Pair> allPairs;

			EqualFrame(float reputation, TreeSet<Pair> pairs) {
				this.reputation = reputation;
				this.allPairs = pairs;
			}

			@Override
			public boolean equals(Object obj) {
				EqualFrame ef = (EqualFrame) obj;
				return this.allPairs.equals(ef.allPairs) && this.reputation == ef.reputation;
			}
		}
		HashMap<EqualFrame, TreeSet<Long>> allVendors = new HashMap<>();
		for (Entry<Long, TreeSet<Pair>> key : vendor.entrySet()) {
			TreeSet<Pair> pairs = key.getValue();
			if (pairs.size() >= 5) {
				Long v = key.getKey();
				EqualFrame ef = new EqualFrame(supplierTree.get(v), pairs);
				TreeSet<Long> set = allVendors.getOrDefault(ef, new TreeSet<Long>());
				set.add(v);
				allVendors.put(ef, set);

			}
		}
		int maxSize = 0;
		TreeSet<Long> maxResult = null;
		for (Entry<EqualFrame, TreeSet<Long>> e : allVendors.entrySet()) {
			if (maxSize < e.getValue().size()) {
				maxSize = e.getValue().size();
				maxResult = e.getValue();
			}
		}
		return  maxResult.toArray(new Long[maxResult.size()]);
	}

	/*
	 * given an array of ids, find the total price of those items, if those
	 * items were purchased at the lowest prices, but only from sellers meeting
	 * or exceeding the given minimum reputation. Each item can be purchased
	 * from a different seller.
	 */
	public int invoice(Long[] arr, float minReputation) {
		int totalPrice = 0;
		boolean flag = false;
		for (Long id : arr) {
			flag = false;
			for (Entry<Pair, TreeSet<Long>> e : priceTable
					.subMap(new Pair(id, Integer.MIN_VALUE), new Pair(id, Integer.MAX_VALUE)).entrySet()) {
				for (Long t : e.getValue()) {
					if (supplierTree.get(t) >= minReputation) {
						totalPrice += e.getKey().price;
						flag = true;
						break;
					}
				}
				if (flag) {
					break;
				}
			}
		}

		return totalPrice;
	}

	/**
	 * remove all items, all of whose suppliers have a reputation that is equal
	 * or lower than the given maximum reputation. Returns an array with the
	 * items removed.
	 */
	public Long[] purge(float maxReputation) {
		List<Long> allVendors = new ArrayList<>();
		for (Map.Entry<Float, TreeSet<Long>> key : supplierRating.subMap(0f, true, maxReputation, true).entrySet()) {
			TreeSet<Long> set = key.getValue();
			for (Long supplier : set) {
				TreeSet<Pair> pairs = vendor.remove(supplier);
				if (pairs != null) {
					for (Pair pair : pairs) {
						TreeSet<Long> suppliers = priceTable.get(pair);
						if (suppliers.size() > 1)
							suppliers.remove(supplier);
						else
							priceTable.remove(pair);
					}
				}
			}
			allVendors.addAll(set);
		}

		return allVendors.toArray(new Long[allVendors.size()]);
	}

	/**
	 * remove item from storage. Returns the sum of the Longs that are in the
	 * description of the item deleted (or 0, if such an id did not exist).
	 */
	public Long remove(Long id) {
		HashSet<Long> desc = itemTree.remove(id);
		long sum = 0L;
		for (long d : desc) {
			removeFromDescTableKey(d, id);
			sum += d;
		}

		SortedMap<Pair, TreeSet<Long>> removePairsFromPriceTable = priceTable.subMap(new Pair(id, Integer.MIN_VALUE),
				true, new Pair(id, Integer.MAX_VALUE), true);
		for (Entry<Pair, TreeSet<Long>> e : removePairsFromPriceTable.entrySet()) {
			for (Long supplier : e.getValue()) {
				TreeSet<Pair> t = vendor.get(supplier);
				if (t != null) {
					TreeSet<Pair> removePairsFromVendor = (TreeSet<Pair>) t.subSet(new Pair(id, Integer.MIN_VALUE),
							true, new Pair(id, Integer.MAX_VALUE), true);
					t.removeAll(removePairsFromVendor);
				}

			}
			priceTable.remove(e.getKey());
		}
		return sum;
	}

	/**
	 * remove from the given id's description those elements that are in the
	 * given array. It is possible that some elements of the array are not part
	 * of the item's description. Return the number of elements that were
	 * actually removed from the description.
	 */
	public int remove(Long id, Long[] arr) {
		HashSet<Long> itemDesc = itemTree.get(id);
		int res = 0;
		if (itemDesc == null)
			return res;
		for (Long d : arr) {
			if (itemDesc.remove(d)) {
				descTable.get(d);
				removeFromDescTableKey(d, id);
				res++;
			}

		}
		return res;
	}

	/**
	 * Utility function removes one item from the value of desctable with the
	 * given key desc.
	 */

	void removeFromDescTableKey(Long desc, Long item) {
		TreeSet<Long> itemSet = descTable.get(desc);
		itemSet.remove(item);
		if (itemSet.size() == 0) {
			descTable.remove(desc);
		}

	}

	/**
	 * remove the elements of the array from the description of all items.
	 * Return the number of items that lost one or more terms from their
	 * descriptions.
	 */
	public int removeAll(Long[] arr) {
		int count = 0;
		for (Entry<Long, HashSet<Long>> key : itemTree.entrySet()) {
			Long id = key.getKey();
			if (remove(id, arr) > 0) {
				count++;
			}
		}
		return count;
	}
}
