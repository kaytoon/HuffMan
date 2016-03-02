

	/**
	 * HuffmanNode is a class that represents a node from a huffman tree.
	 * 
	 * @author David Lee & Mohamed Dahir
	 * @version Fall 2015
	 */
	public class HuffManNode implements Comparable<HuffManNode> {
		/**Right child node. */
		public HuffManNode myRight;
		/**Left child node. */
		public HuffManNode myLeft;
		/**Data contained in node. */
		public String myData;
		/**Frequency of data. */
		public int myFreq;

	/**
	* Constructs a new huffman node, specified by given values
	* of data, frequency, and children nodes.
	* 
	* @param data data value.
	* @param freq frequency value.
	* @param left left child.
	* @param right right child.
	*/
	public HuffManNode(String data, int freq, HuffManNode left, HuffManNode right){
		this.myRight = right;
		this.myLeft = left;
		this.myData = data;
		this.myFreq = freq;
	}


	/**
	* Constructs a new huffman node, specified by given values
	* of data, frequency, and children nodes.
	* 
	* @param freq frequency value.
	* @param left left child.
	* @param right right child.
	*/
	public HuffManNode(int freq, HuffManNode left, HuffManNode right){
		this.myRight = right;
		this.myLeft = left;
		this.myFreq = freq;
	}


	/**
	* Overridden compareTo method.
	*/
	@Override
	public int compareTo(HuffManNode other) {
		if(other instanceof HuffManNode){
			return this.myFreq - other.myFreq;
		}
		return -1;	
	}
	
}


