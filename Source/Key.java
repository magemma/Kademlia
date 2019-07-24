import java.util.BitSet;
import java.util.Comparator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author gemma
 *
 */
public class Key implements Comparable<Key> {
	private BitSet key;
	private int m;

	/**
	 * This function builds a Key object of length m bits from a byte array
	 * 
	 * @param b byte[]
	 * @param m int
	 * @return key
	 */
	public Key(byte[] b, int m) {
		BitSet bs = BitSet.valueOf(b);
		this.key = bs.get(0, m);
		this.m = m;
	}

	/**
	 * This function builds a Key object from a BitSet
	 * 
	 * @param b
	 */
	public Key(BitSet b, int m) {
		this.key = b;
		this.m = m;
	}

	/**
	 * This function returns a key chosen at random among those keys which share p
	 * most significant bits with this
	 * 
	 * @param p shared bits
	 * @param i subsequent bits (not necessarily unshared)
	 * @return a key satisfying the requirements
	 */
	public Key pickAtRandom(int p, int i, Random r) {
		// System.out.println("\ndentro pick");
		// System.out.print("\nid iniziale: ");
		// this.printKey();
		// System.out.println("p: " + p);
		// System.out.println("i: " + i);
		// Set the first p bits (from 0 to p-1) equal to the current key
		BitSet newKey = this.key.get(0, p);
		// Set the bit in position p to not(this[p])
		newKey.set(p, !this.key.get(p));
		if (i == 1) {
			// the current key and the one to be generated differ
			// in the last bit only, hence no random choice can be
			// performed
			Key tmp = new Key(newKey, this.m);
			// System.out.println("nuova: ");
			// tmp.printKey();
			// System.out.println("originale: ");
			// this.printKey();
			// System.out.print("\nid finale: ");
			// this.printKey();
			return tmp;
		}
		// Otherwise randomly generate the remaining i-1 bits
		int max = (int) Math.pow(2, i - 1) - 1;
		// System.out.println("max: " + max);
		int rand = r.nextInt(max);
		// System.out.println("rand: " + rand);
		BitSet bs = BitSet.valueOf(new long[] { (long) rand });
		// Assign those bits to the i-1 rightmost part of key
		// starting from position p+1 to m-1
		for (int j = p + 1; j < this.m; j++) {
			if (bs.get(j)) {
				newKey.set(j);
			}
		}
		Key tmp = new Key(newKey, this.m);
		// System.out.println("nuova: ");
		// tmp.printKey();
		// System.out.println("originale: ");
		// this.printKey();
		// System.out.print("\nid finale: ");
		// this.printKey();
		return tmp;
	}

	/**
	 * This method returns the number of bits shared as a prefix by the id of this
	 * and the key given as an input
	 * 
	 * @param ID
	 * @return length of longest common prefix
	 */
	public int sharedPrefix(Key key) {
		int i = 0;
		while (i < key.m) {
			if (key.key.get(i) != this.key.get(i))
				break;
			i++;
		}
		return i;
	}

	/**
	 * This function returns the k nearest to this ids
	 * 
	 * @param set the input
	 * @param k   the number of elements of the output
	 * @return the set of the closest k
	 */
	public TreeSet<Key> bestResults(Set<Key> set, int k) {
		Comparator<Key> comp = (Key k1, Key k2) -> (k2.sharedPrefix(this) - k1.sharedPrefix(this));
		TreeSet<Key> ts = new TreeSet<Key>(comp);
		if (set.size() < k) {
			ts.addAll(set);
			return ts;
		}
		java.util.Iterator<Key> iterator = set.iterator();
		while (iterator.hasNext()) {
			ts.add(iterator.next());
		}
		TreeSet<Key> newTs = new TreeSet<Key>(comp);
		iterator = ts.iterator();
		int i = 0;
		while (iterator.hasNext() && i < k) {
			newTs.add(iterator.next());
			i++;
		}
		return newTs;
	}

	/**
	 * This function overrides the comparator function in order to allow sorting
	 * based on key number
	 */
	@Override
	public int compareTo(Key key) {
		if (this.key.equals(key.key)) {
			return 0;
		}
		// If they are not equal xor them
		BitSet res = (BitSet) key.key.clone();
		BitSet copy = (BitSet) this.key.clone();
		copy.xor(res);
		// Pick the most significant differing bit
		// The owner of such a bit is the greatest
		int firstDifferent = res.length() - 1;
		if (firstDifferent == -1)
			return 0;
		return key.key.get(firstDifferent) ? 1 : -1;
	}

	/*
	 * public boolean equals(Key key) { return this.key.equals(key.key); }
	 */

	@Override
	public boolean equals(Object key) {
		return this.key.equals(((Key) key).key);
	}

	public void printKey() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < this.m; i++) {
			s.append(this.key.get(i) == true ? 1 : 0);
		}
		System.out.print(s);
	}
}
