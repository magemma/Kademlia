
/**
 * @author gemma
 *
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Vector;

public class RoutingTable {
	// The routing table is represented as a Vector of TreeSets
	Vector<TreeSet<Key>> routingTable;
	// Notice that routingTable[i] contains those nodes which share
	// p=m-i-1 characters with the owner of such routing table (i starts from 0)

	/**
	 * This function returns the hash set of all the nodes present in the routing
	 * table
	 * 
	 * @return
	 */
	public HashSet<Key> allNodes() {
		HashSet<Key> res = new HashSet<Key>();
		for (int i = 0; i < this.routingTable.size(); i++) {
			java.util.Iterator<Key> iterator = this.routingTable.get(i).iterator();
			while (iterator.hasNext()) {
				res.add(iterator.next());
			}
		}
		return res;
	}

	/**
	 * This function updates the routing table inserting all the elements in ts if
	 * they fit in the corresponding row
	 * 
	 * @param ts
	 * @param nodeID
	 * @param k
	 */
	public void update(TreeSet<Key> ts, Key nodeID, int k, int m) {
		java.util.Iterator<Key> iterator = ts.iterator();
		while (iterator.hasNext()) {
			Key el = iterator.next();
			if (el.equals(nodeID))
				continue;
			// Find the bucket in which the element of ts should be
			int sharedDigits = el.sharedPrefix(nodeID);
			int i = m - sharedDigits - 1;
			// If it is not present and there is room insert it
			if ((!this.routingTable.get(i).contains(el)) && (this.routingTable.get(i).size() < k)) {
				this.routingTable.get(i).add(el);
			}
		}
		return;
	}

	/**
	 * This function prints the routing table
	 * 
	 * @param key
	 */
	public void print(Key key) {
		System.out.print("\n\n~~~~~Routing table of ");
		key.printKey();
		for (int i = 0; i < this.routingTable.size(); i++) {
			System.out.print("\nBucket " + i + ": ");
			TreeSet<Key> ts = this.routingTable.get(i);
			if (ts.isEmpty())
				continue;
			java.util.Iterator<Key> iterator = ts.iterator();
			while (iterator.hasNext()) {
				Key currKey = iterator.next();
				currKey.printKey();
				System.out.print("; ");
			}
		}
	}

	/**
	 * This function stores the routing table on a file
	 * 
	 * @param key      the id of the owner of the rt
	 * @param map      the hash map key->int
	 * @param fileName the file where to write
	 * @throws IOException
	 */
	public void store(Key key, HashMap<Key, Integer> map, String fileName) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
		for (int i = 0; i < this.routingTable.size(); i++) {
			TreeSet<Key> ts = this.routingTable.get(i);
			if (ts.isEmpty())
				continue;
			int from = map.get(key);
			java.util.Iterator<Key> iterator = ts.iterator();
			while (iterator.hasNext()) {
				Key currKey = iterator.next();
				int to = map.get(currKey);
				writer.write(from + ";" + to + "\n");
			}
		}
		writer.close();
	}

	public RoutingTable(int m) {
		this.routingTable = new Vector<TreeSet<Key>>(m);
		for (int i = 0; i < m; i++) {
			this.routingTable.add(new TreeSet<Key>());
		}
	}
}
