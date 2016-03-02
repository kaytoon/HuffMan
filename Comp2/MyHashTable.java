

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * MyHashTable is a class that represents a huffman tree of characters
 * that can encode characters from a string into smaller sized bit 
 * representation.
 * 
 * @author David Lee & Mohamed Dahir
 * @version Fall 2015
 */
public class MyHashTable<K, V> {
	/**Number of buckets. */
	Integer capacity;
	
	/**Buckets. */
	public CodeNode[] table;
	
	/**Number of entries. */
	public int size;
	
	/**Set of Keys. */
	public Set<String> myKeySet;
	
	/**Histogram array of probes. */
	public int[] histogram;

	/**
	* Constructs a Hash Table specified by capacity (2^15=32768 buckets).
	* 
	* @param theCapacity number of buckets.
	*/
	public MyHashTable(int theCapacity) {
		this.capacity = theCapacity;
		table = new CodeNode[theCapacity];
		size = 0;
		myKeySet = new HashSet<String>();
		histogram = new int[500];
	}

	/**
	* Updates or adds the newValue to the bucket hash. If hash(key) 
	* is full uses quadratic probing to find the next available bucket.
	* 
	* @param searchKey Key.
	* @param newValue Value.
	*/
	void put(K searchKey, V newValue)
	{
		int index = hash(searchKey);
		if (containsKey(searchKey)) {
			CodeNode tempNode = FindNode(searchKey, index);
			tempNode.value = newValue;
		} else {
			CodeNode newNode = new CodeNode(searchKey, newValue);
			int probe = 0;
			if (table[index] == null) {
				table[index] = newNode;
				histogram[probe] = histogram[probe] + 1;
			} else {
				int quad = 0;
				while (table[index] != null) {
					//linear probing
//					quad = 1;
					//quadratic probing
					quad += 1;
					index += quad * quad;
					if (index > capacity - 1) {
						index = (index - capacity);
					}
					probe++;
				}
				table[index] = newNode;
				histogram[probe] = histogram[probe] + 1;
			}
			myKeySet.add((String) searchKey);
			size++;
		}
	}


	/**
	* Return a value for the specified key from the bucket has(searchKey)
	* if hash(searchKey) doesn't contain the value use linear probing to find
	* the appropriate value.
	* 
	* @param searchKey Key.
	*/
	public Object get(K searchKey)
	{
		int index = hash(searchKey);
		CodeNode n = FindNode(searchKey, index);
		if (n == null)
			return null;
		else
			return n.value;
	}

	/**
	* Returns if the table contains the key already.
	* 
	* @param searchKey Key.
	*/	public boolean containsKey(K searchKey)
	{
		int index = hash(searchKey);

		return (FindNode(searchKey, index) != null);
	}

	/**
	* Finds the node with the given key and index. 
	* Returns null if does not exist in table.
	* 
	* @param K Key.
	* @param index the index.
	*/
	private CodeNode FindNode(K key, int index)
	{
		CodeNode node = table[(int) index];
		int quad = 0;
		while (node != null && !(node.key.equals(key))) {
			//linear probing
//			quad = 1
			//quadratic probing
			quad += 1;
			index += quad * quad;
			if (index > capacity - 1) {
				index = (index - capacity);
//				index = 0;
			}
			node = table[(int) index];
		}
		return node;
	}

	/**
	* A private method that takes a key and 
	* returns an integer in the range [0-->capacity].
	* 
	* @param K Key.
	*/
	private int hash(K key) {
		int hash = 0;
		int index;
		for (int i = 0; i < ((String) key).length(); i++) {
			hash = Math.abs(31 * hash + (int) ((String) key).charAt(i));
		}
		index = hash % capacity;
		return index;
	}

	/**
	* Displays a stat block for data in the hash table including
	* # of entries, # of buckets, histogram of probes, fill %,
	* max linear probability, and average linear probability.
	* 
	* @param K Key.
	*/	
	public void stats() {
		System.out.println("Hash Table Stats\n===================== ");
		System.out.println("Number of Entries: " + size);
		System.out.println("Number of Buckets: " + capacity);
		System.out.println("Histogram of Probes: " + Arrays.toString(histogram));
		System.out.println("Fill Percentage: " + (float) size / capacity * 100 + "%");
		int max = 0;
		double average = 0;
		for (int i = 0; i < histogram.length - 1; i++) {
			if (histogram[i] > 0) {
				max = i;
			}
			average += histogram[i] * i;
		}
		System.out.println("Max Quadratic Probe: " + max);
		System.out.println("Average Quadratic Probe: " + (float) average / size + "\n");
	}
	
	/**
	 * Returns the set of keys in the table.
	 * 
	 * @return set of Keys.
	 */
	public Set<String> keySet() {
		return this.myKeySet;
	}
	
	/**
	 * Gets the String representation of the table.
	 * 
	 * @return String HashTable.
	 */
	public String toString() {
		return table.toString();
	}
}


	/**
	 * CodeNode is a class that represents a Node with Key and Value pairs.
	 * 
	 * @author David Lee & Mohamed Dahir
	 * @version Fall 2015
	 */
	class CodeNode<K, V> {
		protected Object key;
		protected Object value;
	
		/**Constructor. */
		protected CodeNode(K k, V v) {
			key = k;
			value = v;
		}
	}