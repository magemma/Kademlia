
/**
 * @author gemma
 *
 */

import java.util.TreeSet;

public class Node {
	Key id;
	RoutingTable routingTable;
	Key bootstrapId;

	/**
	 * This constructor creates the **first** node in the network
	 * 
	 * @param id
	 * @param m
	 */
	public Node(Key id, int m) {
		this.id = id;
		// Whenever a node is created his routing table is empty
		this.routingTable = new RoutingTable(m);
	}

	/**
	 * This constructor initializes a Node with its identifier, the id of his
	 * bootstrap node and the total amount of nodes in the network as soon as it is
	 * filled
	 * 
	 * @param id
	 * @param bootstrapId
	 * @param m
	 */
	public Node(Key id, Key bootstrapId, int m) {
		this.id = id;
		this.bootstrapId = bootstrapId;
		// Whenever a node is created his routing table is empty
		this.routingTable = new RoutingTable(m);
	}

	/**
	 * This function looks for the closes k peers in the routing table, then updates
	 * it using the visited nodes information and returns the result
	 * 
	 * @param key
	 * @param visited
	 * @return ans -> the nearest nodes to key
	 */
	public TreeSet<Key> FIND_NODE(Key callerID, Key key, TreeSet<Key> visited, int k, int m) {
		// System.out.print("\ncallerID 1: ");
		// callerID.printKey();
		TreeSet<Key> ans = key.bestResults(this.routingTable.allNodes(), k);
		// System.out.print("\ncallerID 2: ");
		// callerID.printKey();
		TreeSet<Key> visitedBis = new TreeSet<Key>();
		// System.out.print("\ncallerID 3: ");
		// callerID.printKey();
		visitedBis.addAll(visited);
		// System.out.print("\ncallerID 4: ");
		// callerID.printKey();
		visitedBis.add(callerID);
		// System.out.print("\ncallerID 5: ");
		// callerID.printKey();
		this.routingTable.update(visitedBis, this.id, k, m);
		// System.out.print("\ncallerID 6: ");
		// callerID.printKey();
		// System.out.print("\ndentro FIND_NODE:");
		// Coordinator.printTree(ans);
		return ans;
	}
}
