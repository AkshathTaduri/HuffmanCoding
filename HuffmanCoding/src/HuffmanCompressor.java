import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class HuffmanCompressor {
	public static void compress(String filename) {
		try {
			Scanner in = new Scanner(new File(filename + ".txt"));
			int[] count = new int[256];
			while (in.hasNextLine()) {
				String line = in.nextLine();
				for (int i = 0;i < line.length();i++) {
					count[(int)line.charAt(i)]++;
				}
			}
			HuffmanTree huffmanTree = new HuffmanTree(count);
			huffmanTree.write(filename + ".code");
			BitOutputStream out = new BitOutputStream(filename + ".short");
			huffmanTree.writeShort(out, filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void expand(String codeFile, String fileName) {
		HuffmanTree huffmanTree = new HuffmanTree(codeFile);
		BitInputStream in = new BitInputStream(fileName + ".short");
		huffmanTree.decode(in, fileName + ".new");
	}
}
