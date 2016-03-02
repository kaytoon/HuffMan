

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

/**
 * Main method that drives the program.
 * 
 * @author David Lee & Mohamed Dahir
 * @version Fall 2015
 */
public class Main {
	
	/**
	 * The main method that reads a text file and compresses 
	 * the size using huffman tree encoding.
	 * 
	 * @param args the Arguments.
	 */
	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();		
		String text = readFile("WarAndPeace.txt");
		//		String text = readFile("Beowulf.txt");
		
		CodingTree tree = new CodingTree(text);
		PrintStream output1 = new PrintStream(new File("codes.txt"));
		PrintStream output2 = new PrintStream(new File("compressed.txt"));
		output1.print(tree.codes.toString());
		output1.close();
		writeFile(tree.bits, output2);
		
		tree.frequencies.stats();
		File inFile = new File("WarAndPeace.txt");
		long bytes1 = inFile.length();
		System.out.println("Uncompressed file size: " + bytes1 + " bytes");
		File outFile = new File("compressed.txt");
		long bytes2 = outFile.length();
		System.out.println("Compressed file size: " + bytes2 + " bytes");
		System.out.println("Compression ratio: " + (float) bytes2 / bytes1 * 100 + "%\n");
	
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Running Time: " + totalTime + "ms");
		//		testComponents();
	}

	/**
	 * Reads a given file and converts it's content to string format.
	 * 
	 * @param fileName name of the file to read.
	 * @return String representation of the text contents of the file.
	 * @throws IOException 
	 */
	public static String readFile(String fileName) throws IOException {
		FileReader file = new FileReader(fileName);
		BufferedReader reader = new BufferedReader(file);
		StringBuilder sb = new StringBuilder();
		int ch;
		while((ch = reader.read()) != -1) {
			char characters = (char)ch;

			sb.append(characters);
		}
		reader.close();	
		return sb.toString();
	}

	/**
	 * Writes an encoded list of bytes to an output text file.
	 * 
	 * @param theList list of bytes
	 * @param output PrintStream for output.
	 * @throws IOException
	 */
	public static void writeFile(List<Byte> theList, PrintStream output) throws IOException {
		for (int i = 0; i < theList.size(); i++) {
			output.write(theList.get(i));
		}
		output.close();
	}
	/**
	 * Tests components of CodingTree.
	 * @throws IOException 
	 */
	public static void testComponents() throws IOException {
		CodingTree test = new CodingTree("ANNA HAS A BANANA IN A BANDANA");
		//counting frequencies
		System.out.println(test.frequencies.toString());
		//tree initialization, reading codes from tree
		System.out.println(test.codes.toString()); 
		//encoding to byte
		System.out.println(test.bits.toString());
	}

}