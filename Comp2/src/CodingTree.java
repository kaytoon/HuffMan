

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * CodingTree is a class that represents a huffman tree of characters
 * that can encode/decode words from a string into smaller sized bit 
 * representation.
 * 
 * @author David Lee & Mohamed Dahir
 * @version Fall 2015
 */
public class CodingTree {	
	/**Hash Table of Strings(Key) and their frequency count(Value). */
	public MyHashTable<String, Integer> frequencies;

	/**Huffman tree node. */
	public HuffManNode huffNode;

	/**Map of Strings and their encoded bit values. */
	public Map<String, String> codes;

	/**List of bytes. */
	public List<Byte> bits;
	
	/**Array of strings. */
	private String[] array;
	
	public static int mode;
	private static Map<String, String> myMap;

	/**
	 * Constructs a CodingTree that gets frequency of 
	 * character occurrences, huffman tree initialization,
	 * reads codes from huffman tree, and encodes a message.
	 * 
	 * @param message String of a text file to be encoded.
	 * @throws IOException
	 */
	public CodingTree(String message) throws IOException {
		frequencies = new MyHashTable(32768);
		codes = new HashMap<String, String>();
		bits = new ArrayList<Byte>();
		getStringFrequencies(message);
		huffNode = getCodes();
		assignBinary(huffNode, "");
		encode(message);
		decode();
	}

	/**
	 * Gets the frequency of character occurrences and saves to map.
	 * 
	 * @param Message String of a text file to be encoded.
	 * @throws IOException
	 */
	private void getStringFrequencies(String Message) throws IOException{
		StringBuilder builder = new StringBuilder();
		int freq = 1;
		char[] messageArray = Message.toCharArray();	
		int i = 0;
		while (i < messageArray.length) {
			if (checkChar(messageArray[i]) == true) {
				while (checkChar(messageArray[i]) == true) {
					builder.append(messageArray[i]);
					i++;
				}
			} else {
				builder.append(messageArray[i]);
				i++;
			}
			if (frequencies.containsKey(builder.toString())) {
				freq = (int) frequencies.get(builder.toString()) + 1;
			}
			frequencies.put(builder.toString(), freq);
			freq = 1;
			builder = new StringBuilder();
		}
		System.out.println("");
	}
	private boolean checkChar(char theChar) {
		if ((theChar >= '0' && theChar <= '9') || (theChar >= 'A' && theChar <= 'Z') || 
				(theChar >= 'a' && theChar <= 'z') || theChar == '\'' || theChar == '-') {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Huffman Tree initialization and binary encoding for character keys.
	 * 
	 * @return HuffManNode root node of the huffman tree.
	 */
	private HuffManNode getCodes() {
		array = new String[frequencies.size];
		frequencies.keySet().toArray(array);
		PriorityQueue<HuffManNode> trees = new PriorityQueue<HuffManNode>();
		for (int i = 0; i < frequencies.size; i++) {
			HuffManNode node = new HuffManNode(array[i], (int) frequencies.get(array[i]), null, null);
			trees.offer(node);
		}
//		System.out.println(trees.size());
		while (trees.size() > 1) {
			// two trees with least frequency
			HuffManNode a = trees.poll();
			HuffManNode b = trees.poll();
			// create into new node and re-insert into the priority queue
			HuffManNode huff = new HuffManNode(a.myFreq + b.myFreq, a, b);
			trees.offer(huff);
		}
		HuffManNode node = trees.poll();
		return node;
	}

	/**
	 * Assigns binary bit values for each string from huffman tree.
	 * 
	 * @param trees Huffman Tree
	 */
	private void assignBinary(HuffManNode root, String code){
		if (root.myLeft != null) {
			assignBinary(root.myLeft, code + '0');
		}
		if (root.myRight != null) {
			assignBinary(root.myRight, code + '1');
		}
		if (root.myRight == null && root.myLeft == null) {
			codes.put(root.myData, code);
		}
	}

	
	/**
	 * Encodes a text file using codes from huffman tree
	 * into list of bytes.
	 * 
	 * @param message String of a text file to be encoded.
	 */
	private void encode(String message) {
		//builder for input message string
		StringBuilder builder = new StringBuilder();
		//builder for output encoded string
		StringBuilder sb = new StringBuilder();
		char[] messageArray = message.toCharArray();

		int pointer = 0;
		while (pointer < messageArray.length) {
			if (checkChar(messageArray[pointer]) == true) {
				while (checkChar(messageArray[pointer]) == true) {
					builder.append(messageArray[pointer]);
					pointer++;
				}
			} else {
				builder.append(messageArray[pointer]);
				pointer++;
			}
			String s = codes.get(builder.toString());
			sb.append(s);
			builder = new StringBuilder();

		}
		
		String binary = sb.toString();
		char[] array = binary.toCharArray();
		mode = array.length % 8;
		String emptyString = "";
		for (int i = 0; i < array.length; i++) {
			mode--;
			if(i>=array.length-mode){
				if (emptyString.length() < 8) {
					emptyString = emptyString + array[i];
				} else {
					System.out.println(emptyString);
					Byte b = (byte) Integer.parseInt(emptyString,2);
					bits.add(b);
					emptyString = "";
					emptyString = emptyString + array[i];
				}
			}
			else {
				if (emptyString.length() < 8 ) {
					emptyString = emptyString + array[i];
				} else {
					Byte b = (byte) Integer.parseInt(emptyString,2);	
					bits.add(b);
					emptyString = "";
					emptyString = emptyString + array[i];
				}
			}
		}
		for(int i = 0; i < mode;i++){
			if(emptyString.length()< 8){
				emptyString = emptyString+array[i];
			}
			emptyString="";
			emptyString = emptyString+array[i];
		}
		Byte b = (byte) Integer.parseInt(emptyString,2);
		bits.add(b);
	}
	
	
	/**
	 * Decodes an encoded text file into its original text.
	 * 
	 * @param message String of a text file to be encoded.
	 */
	private static void decode() throws IOException{
		PrintStream original = new PrintStream(new File("decoded.txt"));
		myMap = new HashMap<String, String>();
		FileReader codes = new FileReader("codes.txt");
		BufferedReader codesReader = new BufferedReader(codes);
		FileInputStream compressedReader = new FileInputStream("compressed.txt");
		StringBuilder codesBuilder = new StringBuilder();
		StringBuilder compressedBuilder = new StringBuilder();
		StringBuilder originalText = new StringBuilder();
		int ch;
		while((ch = codesReader.read()) !=-1) {
			char chara = (char)ch;
			codesBuilder.append(chara);
		}
		String st = ""+codesBuilder;
		ArrayList<String> newString = new ArrayList<String>();
		String[] s = st.split(", ");
		for(int l = 0; l<s.length;l++){
			newString.add(s[l]);
		}
		String c = "";
		for(int l = 0; l<newString.size();l++ ){
			c = newString.get(l);
			int k =c.indexOf("=");
			if(c.contains("{")){
				myMap.put(c.substring(k+1, c.length()),c.substring(1,k));
			} else if(c.contains("==")){
				myMap.put(c.substring(k+2, c.length()),c.substring(0,k+1));
			}
			else if(c.contains("}")){

				myMap.put(c.substring(k+1, c.length()-1),c.substring(0,k));
			}
			else if(k>0){
				String[] parts= c.split("=", 2);
				myMap.put(parts[1] ,parts[0]);
			}
		}
		Byte text;
		while(compressedReader.available() >0) {
			text =   (byte) compressedReader.read();
			String string = Integer.toBinaryString(text);
			if(string.length() > 8){
				string = string.substring(string.length()-8, string.length());
			}
			if(compressedReader.available() > 0){
				for(int i = string.length(); i < 8;i++){
					string = "0"+string;
				}

			} else {
				for(int i = string.length(); i <mode;i++){
					string = "0"+string;
				}
			}
			compressedBuilder.append(string);
		}
		String k = "";
		for( int i = 0; i < compressedBuilder.length();i++){
			 k = k+""+compressedBuilder.charAt(i);
			if(myMap.containsKey(k)){
				String value = myMap.get(k);
				k = "";
				originalText.append(value);
			}
		}
		original.print(originalText);
		original.close();
	}


}