
/**
 * @author gemma
*/

import java.util.Random;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Coordinator {
	HashMap<Key, Node> peers;// Dictionary of peers: (key=id, value=Node class)
	int n;// Number of peers that will populate the network
	int m;// The key-length
	int k;// numbers of nodes in each k-bucket
	int collisions;// Number of collisions
	Random rng;

	/**
	 * This function generates a couple (IPv4, port), hashes it using sha1 and
	 * generates a key with such an hash (truncated to m bits)
	 * 
	 * @return the key corresponding to the couple (IPv4, port)
	 * @throws NoSuchAlgorithmException
	 */
	private Key generateV1() throws NoSuchAlgorithmException {
		// Generate ipv4 address at random
		String ipAddr = this.rng.nextInt(256) + "." + this.rng.nextInt(256) + "." + this.rng.nextInt(256) + "."
				+ this.rng.nextInt(256);
		// Generate port at random (only dynamic ports - from 49152 to 65535)
		int minPort = 49152;
		int maxPort = 65535;
		int port = minPort + (int) (Math.random() * ((maxPort - minPort) + 1));
		String ipv4 = ipAddr + "/" + Integer.toString(port);
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encodedHash = digest.digest(ipv4.getBytes(StandardCharsets.UTF_8));
		return new Key(encodedHash, this.m);
	}

	/**
	 * This function returns true if this = key, false otherwise. Moreover this
	 * function increments the counter "collisions" when one takes place
	 * 
	 * @param key
	 * @return the truth value of this ? key
	 */
	private boolean checkDuplicate(Key key) {
		boolean bool = this.peers.containsKey(key);
		// bool is true if the key is present in peers
		if (bool)
			this.collisions++;
		return !bool;
	}

	/**
	 * This function generates a new key, different from those already assigned to
	 * other peers in the peers HashMap
	 * 
	 * @return the unique key generated
	 * @throws NoSuchAlgorithmException
	 */
	private Key generateUniqueKey() throws NoSuchAlgorithmException {
		boolean unique = false;
		Key key = null;
		while (!unique) {
			key = this.generateV1();
			unique = this.checkDuplicate(key);
		}
		return key;
	}

	/**
	 * This method performs the following operation: A first node, whose identifier
	 * is selected at random, is allocated in the data structure, with an empty.
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public void Initialize(Random random) throws NoSuchAlgorithmException {
		this.rng = random;
		// Inizialize the peers data structure
		this.peers = new HashMap<Key, Node>(this.n);
		// Set the number of collisions to 0
		this.collisions = 0;
		// Generate **unique** key at random
		Key key = this.generateUniqueKey();
		// Build a Node for that key
		// Obs: no bootstrap node needed
		Node newPeer = new Node(key, this.m);
		this.peers.put(key, newPeer);
		// System.out.print("Init peer: ");
		// key.printKey();
	}

	/**
	 * This function picks an id at random among those present in the HashMap peers
	 * and returns it in order to make it become the bootstrap node
	 * 
	 * @return
	 */
	private Key assignBootstrapNode() {
		ArrayList<Key> keysAsArray = new ArrayList<Key>(this.peers.keySet());
		Random r = new Random();
		return keysAsArray.get(r.nextInt(keysAsArray.size()));
	}

	/**
	 * This function does the following: n-1 times 1) generates a new peer and
	 * initializes it 2) assigns a bootstrap node to it 3) for each k-bucket (m
	 * times) I) select at random a key II) ask FIND_NODE() to the bootstrap node
	 * III) collect results and recur until the set of the best k converges 4)
	 * update the routing table using that set
	 * 
	 * @throws NoSuchAlgorithmException
	 */

	public void fillNetwork() throws NoSuchAlgorithmException {
		// System.out.println("Inizio filling of the network");
		// Until the n-1 nodes are added to the network
		for (int j = 0; j < this.n - 1; j++) {
			// Generate **unique** key at random
			Key newPeerID = this.generateUniqueKey();
			System.out.print("\n" + (j + 1) + "-th peer: ");
			newPeerID.printKey();
			// Pick the key of the bootstrap node
			Key bootstrapKey = this.assignBootstrapNode();
			// System.out.print("\tBootstrap node: ");
			// bootstrapKey.printKey();
			// Build a Node for that key
			Node newPeer = new Node(newPeerID, bootstrapKey, this.m);
			// Fill each single k-bucket of newPeer's routing table
			for (int i = 0; i < this.m; i++) {
				// Generate IDs of a key for the i-th bucket
				// Notice that the buckets are m and that i represents
				// 1 + the number of bits that follow the shared bits (denoted as p)
				// p is in [0, m-1]. if p = m the keys are the same
				Key targetKey = newPeerID.pickAtRandom(this.m - i - 1, i + 1, this.rng);
				while (newPeerID.equals(targetKey)) {
					targetKey = newPeerID.pickAtRandom(this.m - i - 1, i + 1, this.rng);
				}
				TreeSet<Key> bestTree = new TreeSet<Key>();
				// 1) Ask the bootstrap node the k peers nearest to targetKey
				bestTree = this.peers.get(bootstrapKey).FIND_NODE(newPeerID, targetKey, new TreeSet<Key>(), this.k,
						this.m);
				// 1bis) take the best k among bestTree U {bootstrapKey}
				bestTree.add(bootstrapKey);
				bestTree = targetKey.bestResults(bestTree, k);
				// 2) Call FIND_NODE on those best k returned by the boostrapNode
				TreeSet<Key> newTree = new TreeSet<Key>();
				java.util.Iterator<Key> iterator = bestTree.iterator();
				while (iterator.hasNext()) {
					Key currKey = iterator.next();
					if (currKey.equals(newPeerID)) {
						continue;
					}
					// If the key is equal to the peer's ID that's the best fit
					if (currKey.equals(targetKey)) {
						newTree.addAll(
								this.peers.get(currKey).FIND_NODE(newPeerID, targetKey, bestTree, this.k - 1, this.m));
					} else {
						newTree.addAll(
								this.peers.get(currKey).FIND_NODE(newPeerID, targetKey, bestTree, this.k, this.m));
					}
				}
				newTree.addAll(bestTree);
				newTree = targetKey.bestResults(newTree, k);
				// Recur until newTree and bestTree are equal
				while (!bestTree.equals(newTree)) {
					// If they are different update the best and recur on it
					bestTree.clear();
					bestTree.addAll(newTree);
					iterator = bestTree.iterator();
					while (iterator.hasNext()) {
						Key currKey = iterator.next();
						if (currKey.equals(newPeerID))
							continue;
						// If the key is equal to the peer's ID that's the best fit
						if (currKey.equals(targetKey)) {
							newTree.addAll(this.peers.get(currKey).FIND_NODE(newPeerID, targetKey, bestTree, this.k - 1,
									this.m));
						} else {
							newTree.addAll(
									this.peers.get(currKey).FIND_NODE(newPeerID, targetKey, bestTree, this.k, this.m));
						}
					}
					// Phase completed
					newTree.addAll(bestTree);
					newTree = targetKey.bestResults(newTree, this.k);
				}
				// We have a set of the best (at most) k nodes
				// for the targetKey. We update the routing table with this info
				newPeer.routingTable.update(newTree, newPeerID, this.k, this.m);
			}
			// Add peer to the network
			this.peers.put(newPeerID, newPeer);
		}
	}

	private HashMap<Key, Integer> mapToInt() {
		HashMap<Key, Integer> map = new HashMap<Key, Integer>();
		Iterator<Entry<Key, Node>> iterator = this.peers.entrySet().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			map.put(iterator.next().getKey(), i);
			i++;
		}
		return map;
	}

	/**
	 * This constructor assigns the fields of the coordinator.
	 * 
	 * @param n
	 * @param m
	 * @param k
	 */
	public Coordinator(int m, int n, int k) {
		this.m = m;
		this.k = k;
		this.n = n;
		this.collisions = 0;
	}

	/**
	 * This function saves the rt of all the peers on a file
	 * 
	 * @param fn the name of the file
	 * @throws IOException
	 */
	private void save(String fn) throws IOException {
		HashMap<Key, Integer> map = this.mapToInt();
		Iterator<Entry<Key, Node>> iterator = this.peers.entrySet().iterator();
		// System.out.print("\n~~~~~~~~~~~~~~~~~Routing tables~~~~~~~~~~~~~~~~~");
		while (iterator.hasNext()) {
			Node currNode = iterator.next().getValue();
			// currNode.routingTable.print(currNode.id);
			currNode.routingTable.store(currNode.id, map, fn);
		}
	}

	public static void main(String[] args) {
		// Set seed for random numbers
		Random random = new Random();
		int m = Integer.valueOf(args[0]);
		int n = Integer.valueOf(args[1]);
		int k = Integer.valueOf(args[2]);
		String fileName = args[3];
		System.out.println(m + " " + n + " " + k + " " + fileName);
		Coordinator coord = new Coordinator(m, n, k);
		try {
			coord.Initialize(random);
			coord.fillNetwork();
			// Store the network on a csv
			coord.save(fileName);
		} catch (NoSuchAlgorithmException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
