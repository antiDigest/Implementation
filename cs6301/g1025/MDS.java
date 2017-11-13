
package cs6301.g1025;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.*;

public class MDS {

    static TreeMap<Long, Long[]> itemTree = new TreeMap<>();
    static HashMap<Long, TreeSet<Long>> descTable = new HashMap<>();

    static TreeMap<Long, Float> supplierTree = new TreeMap<>();
    static HashMap<Float, TreeSet<Long>> supplierRating = new HashMap<>();

    static TreeMap<Long, Pair[]> vendor = new TreeMap<>();
    static HashMap<Pair, TreeSet<Long>> priceTable = new HashMap<>();

    public MDS() {
    }

    public static class Pair {
        long id;
        int price;

        public Pair(long id, int price) {
            this.id = id;
            this.price = price;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj==null) return false;
            Pair o = (Pair) obj;
            if (id == o.id && price == o.price){
                return true;
            } else {
                return false;
            }
        }
    }

    /* add a new item.  If an entry with the same id already exists,
       the new description is merged with the existing description of
       the item.  Returns true if the item is new, and false otherwise.
    */
    public boolean add(Long id, Long[] description) {
        Long[] desc = itemTree.get(id);
        boolean flag = false;
        if(desc != null){
            int size = desc.length + description.length;
            long[] d = new long[size];
            int k = 0;
            for(int i=0;i<desc.length;i++){
                d[k++] = desc[i];
            }
            for(int i=0;i<description.length;i++){
                d[k++] = description[i];
            }
            itemTree.remove(id);
            itemTree.put(id, description);
            flag = true;
        } else {
            itemTree.put(id, description);
        }
        for(long d: description) {
            TreeSet<Long> set = descTable.get(d);
            if (set == null) {
                set = new TreeSet<>();
                set.add(id);
                descTable.put(d, set);
            } else {
                set.add(id);
                descTable.remove(d);
                descTable.put(d, set);
            }
        }
        return !flag;
    }

    /* add a new supplier (Long) and their reputation (float in
       [0.0-5.0], single decimal place). If the supplier exists, their
       reputation is replaced by the new value.  Return true if the
       supplier is new, and false otherwise.
    */
    public boolean add(Long supplier, float reputation) {
        Float rep = supplierTree.get(supplier);
        if(rep.compareTo(null) == 0){
            supplierTree.remove(supplier);
            supplierTree.put(supplier, reputation);
            return false;
        } else {
            supplierTree.put(supplier, reputation);
            TreeSet<Long> set = supplierRating.get(reputation);
            if(set == null) {
                set = new TreeSet<>();
                set.add(supplier);
                supplierRating.put(reputation, set);
            } else {
                set.add(supplier);
                supplierRating.remove(reputation);
                supplierRating.put(reputation, set);
            }
        }
        return true;
    }

    /* add products and their prices at which the supplier sells the
      product.  If there is an entry for the price of an id by the
      same supplier, then the price is replaced by the new price.
      Returns the number of new entries created.
    */
    public int add(Long supplier, Pair[] idPrice) {
        Pair[] pairs = vendor.get(supplier);
        int count = 0;
        if(pairs != null){
            vendor.remove(supplier);
            vendor.put(supplier, idPrice);
            return 0;
        } else {
            vendor.put(supplier, idPrice);
        }

        for(Pair ip: idPrice){
            TreeSet<Long> set = priceTable.get(ip);
            if(set == null) {
                set = new TreeSet<>();
                set.add(supplier);
                priceTable.put(ip, set);
            } else {
                set.add(supplier);
                priceTable.remove(ip);
                priceTable.put(ip, set);
            }
            count++;
        }

        return count;
    }

    /**
     * return an array with the description of id.  Return null if
     * there is no item with this id.
    */
    public Long[ ] description(Long id) {
        return itemTree.get(id);
    }

    /**
     * given an array of Longs, return an array of items whose
     * description contains one or more elements of the array, sorted
     * by the number of elements of the array that are in the item's
     * description (non-increasing order).
    */
    public Long[ ] findItem(Long[ ] arr) {

        class Frame implements Comparable<Frame> {
            Long id;
            int count;

            Frame(long id, int count){
                this.id = id;
                this.count = count;
            }

            @Override
            public int compareTo(Frame o) {
                return ((Integer)this.count).compareTo(o.count);
            }

        }

        HashMap<Long, Integer> frames = new HashMap<>();

        for(Long val: arr){
            TreeSet<Long> set = descTable.get(val);
            if(set!=null){
                for(Long id: set){
                    int count = frames.get(id);
                    if(count == 0){
                        frames.put(id, 1);
                    } else {
                        frames.remove(id);
                        frames.put(id, count+1);
                    }
                }
            }
        }

        List<Frame> allFrames = new LinkedList<>();

        for(HashMap.Entry<Long, Integer> key: frames.entrySet()){
            allFrames.add(new Frame(key.getKey(), key.getValue()));
        }

        Frame[] f = (Frame[]) allFrames.toArray();
        Arrays.sort(f);

        Long[] newArr = new Long[f.length];
        for(int i=0;i<f.length;i++){
            newArr[i] = f[i].id;
        }
        return newArr;
    }

    /**
     *  given a Long n, return an array of items whose description
     *  contains n, which have one or more suppliers whose reputation
     *  meets or exceeds the given minimum reputation, that sell that
     *  item at a price that falls within the price range [minPrice,
     *  maxPrice] given.  Items should be sorted in order of their
     *  minimum price charged by a supplier for that item
     *  (non-decreasing order).
    */
    public Long[ ] findItem(Long n, int minPrice, int maxPrice, float minReputation) {

        List<PriceFrame> allVendors = new ArrayList<>();

        TreeSet<Long> ids = descTable.get(n);
        for(Long id: ids) {
            PriceFrame[] arr = findSupplierFrames(id, minReputation);
            Collections.addAll(allVendors, arr);
        }

        PriceFrame[] arr = (PriceFrame[]) allVendors.toArray();
        Arrays.sort(arr);

        Long[] newArr = new Long[arr.length];
        for(int i=0;i<arr.length;i++){
            newArr[i] = arr[i].vendorId;
        }
        return newArr;
    }

    class PriceFrame implements Comparable<PriceFrame> {
        long id;
        long vendorId;
        int price;

        PriceFrame(long id, long vendorId, int price){
            this.id = id;
            this.vendorId = vendorId;
            this.price = price;
        }

        @Override
        public int compareTo(PriceFrame o) {
            return ((Integer)price).compareTo(o.price);
        }
    }

    /* given an id, return an array of suppliers who sell that item,
      ordered by the price at which they sell the item (non-decreasing order).
    */
    public Long[ ] findSupplier(Long id) {

        PriceFrame[] arr = findSupplierFrames(id, (float)0.0);

        Long[] newArr = new Long[arr.length];
        for(int i=0;i<arr.length;i++){
            newArr[i] = arr[i].vendorId;
        }
        return newArr;
    }

    PriceFrame[] findSupplierFrames(Long id, float minReputation){
        List<PriceFrame> allVendors = new ArrayList<>();

        for(HashMap.Entry<Pair, TreeSet<Long>> key: priceTable.entrySet()){
            Pair p = key.getKey();
            TreeSet<Long> vendors = key.getValue();
            if(p.id == id) {
                for(Long vendor: vendors){
                    float rep = supplierTree.get(vendor);
                    if(rep >= minReputation){
                        allVendors.add(new PriceFrame(p.id, vendor, p.price));
                    }
                }
            }
        }

        PriceFrame[] arr = (PriceFrame[]) allVendors.toArray();
        Arrays.sort(arr);
        return arr;
    }

    /* given an id and a minimum reputation, return an array of
      suppliers who sell that item, whose reputation meets or exceeds
      the given reputation.  The array should be ordered by the price
      at which they sell the item (non-decreasing order).
    */
    public Long[ ] findSupplier(Long id, float minReputation) {
        PriceFrame[] arr = findSupplierFrames(id, minReputation);

        Long[] newArr = new Long[arr.length];
        for(int i=0;i<arr.length;i++){
            newArr[i] = arr[i].vendorId;
        }
        return newArr;
    }

    /** find suppliers selling 5 or more products, who have the same
       identical profile as another supplier: same reputation, and,
       sell the same set of products, at identical prices.  This is a
       rare operation, so do not do additional work in the other
       operations so that this operation is fast.  Creative solutions
       that are elegant and efficient will be awarded excellence credit.
       Return array of suppliers satisfying above condition.  Make sure
       that each supplier appears only once in the returned array.
    */
    public Long[ ] identical() {

        class EqualFrame {
            float reputation;
            Pair[] allPairs;
            long supplier;

            EqualFrame(float reputation, Pair[] pairs, long supplier){
                this.reputation = reputation;
                this.allPairs = pairs;
                this.supplier = supplier;
            }

            @Override
            public boolean equals(Object obj) {
                return super.equals(obj);
            }
        }

        HashMap<EqualFrame, TreeSet<Long>> allVendors = new HashMap<>();
        for(HashMap.Entry<Long, Pair[]> key: vendor.entrySet()) {
            Pair[] pairs = key.getValue();
            if(pairs.length >= 5){
                Long v = key.getKey();
                EqualFrame ef = new EqualFrame(supplierTree.get(v), pairs, v); //TODO -- test this part
                TreeSet<Long> set = allVendors.get(ef);
                if (set == null) {
                    set = new TreeSet<>();
                    set.add(v);
                    allVendors.put(ef, set);
                } else {
                    set.add(v);
                    allVendors.remove(v);
                    allVendors.put(ef, set);
                }
            }
        }

        return null;
    }

    /* given an array of ids, find the total price of those items, if
       those items were purchased at the lowest prices, but only from
       sellers meeting or exceeding the given minimum reputation.
       Each item can be purchased from a different seller.
    */
    public int invoice(Long[ ] arr, float minReputation) {
        int totalPrice = 0;
        for(Long id: arr){
            PriceFrame pf = findSupplierFrames(id, minReputation)[0];
            totalPrice += pf.price;
        }
        return totalPrice;
    }

    /** remove all items, all of whose suppliers have a reputation that
       is equal or lower than the given maximum reputation.  Returns
       an array with the items removed.
    */
    public Long[ ] purge(float maxReputation) {

        List<Long> allVendors = new ArrayList<>();

        for(HashMap.Entry<Float, TreeSet<Long>> key: supplierRating.entrySet()){
            float rep = key.getKey();
            if (rep < maxReputation){
                TreeSet<Long> set = key.getValue();
                for (Long supplier : set) {
                    supplierTree.remove(supplier);
                    Pair[] pairs = vendor.get(supplier);
                    for (Pair pair : pairs) {
                        TreeSet<Long> suppliers = priceTable.get(pair);
                        if (suppliers.size() > 1)
                            suppliers.remove(supplier);
                        else
                            priceTable.remove(pair);
                    }
                }
                allVendors.addAll(set);
            }
        }

        return (Long[]) allVendors.toArray();
    }

    /** remove item from storage.  Returns the sum of the Longs that
       are in the description of the item deleted (or 0, if such an id
       did not exist).
    */
    public Long remove(Long id) {
        Long[] desc = itemTree.get(id);
        long sum = 0L;
        for(long d: desc){
            TreeSet<Long> ids = descTable.get(desc);
            if(ids.size() > 1)
                ids.remove(id);
            else
                descTable.remove(desc);
            sum += d;
        }

        TreeMap<Long, Pair[]> tempVendor = new TreeMap<>();
        for(HashMap.Entry<Long, Pair[]> key: vendor.entrySet()){
            Pair[] pairs = key.getValue();
            Pair[] newPairs = new Pair[pairs.length];
            int index = 0;
            for(Pair p: pairs){
                if(p.id != id){
                    newPairs[index++] = p;
                }
            }
            tempVendor.put(key.getKey(), newPairs);
        }
        vendor.clear();
        vendor = tempVendor;

        List<Pair> toBeRemoved = new LinkedList<>();
        for(HashMap.Entry<Pair, TreeSet<Long>> key: priceTable.entrySet()){
            TreeSet<Long> vendors = key.getValue();
            Pair pair = key.getKey();
            if(pair.id == id)
                toBeRemoved.add(pair);
        }
        for(Pair pair: toBeRemoved){
            priceTable.remove(pair);
        }

        return sum;
    }

    /** remove from the given id's description those elements that are
       in the given array.  It is possible that some elements of the
       array are not part of the item's description.  Return the
       number of elements that were actually removed from the description.
    */
    public int remove(Long id, Long[ ] arr) {

        Long[] desc = itemTree.get(id);
        Long[] newDesc = new Long[desc.length];

        if(desc == null)
            return 0;

        Arrays.sort(desc);
        Arrays.sort(arr);

        int i=0, j=0;
        int count=0, index=0;
        while(i<desc.length && j<arr.length){
            if(desc[i] < arr[j]){
                newDesc[index++] = desc[i];
                i++;
            } else if (desc[i] > arr[j]){
                j++;
            } else {
                Long d = desc[i];
                TreeSet<Long> set = descTable.get(d);
                if(set.size() > 1){
                    set.remove(id);
                } else {
                    descTable.remove(d);
                }
                i++; j++; count++;
            }
        }

        itemTree.remove(id);
        itemTree.put(id, newDesc);

        return count;
    }

    /** remove the elements of the array from the description of all
       items.  Return the number of items that lost one or more terms
       from their descriptions.
    */
    public int removeAll(Long[ ] arr) {
        int count=0;
        for(HashMap.Entry<Long, Long[]> key: itemTree.entrySet()){
            Long id = key.getKey();
            if(remove(id, arr) > 0){
                count++;
            }
        }
        return count;
    }
}