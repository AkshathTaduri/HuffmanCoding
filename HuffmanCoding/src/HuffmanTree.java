import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;


class Node implements Comparable<Node>{
	int weight;
	Character val;
	Node left, right;
	
	public Node() {}
	
	public String toString() {
		if (val != null) {
			return "" + val;
		}
		else {
			return "" + weight;
		}
	}
	@Override
	public int compareTo(Node node) {
		// TODO Auto-generated method stub
		return weight - node.weight;
	}
	
}
 
public class HuffmanTree {
	Queue<Node> tree;
	HashMap<String,Integer> decodeMap;
	HashMap<Integer,String> encodeMap;
	
	public HuffmanTree(int[] count) {
		tree = new PriorityQueue<>();
		for (int i = 0;i < count.length;i++) {
			if (count[i] != 0) {
				Node temp = new Node();
				temp.weight = count[i];
				temp.val = (char)i;
				tree.add(temp);
			}
		}
		Node eof = new Node();
		eof.weight = 1;
		eof.val = (char)256;
		tree.add(eof);
		
		while (tree.size() != 1) {
			Node min1 = Collections.min(tree);
			tree.remove(min1);
			Node min2 = Collections.min(tree);
			tree.remove(min2);
			Node branch = new Node();
			branch.weight = min1.weight + min2.weight;
			branch.left = min1;
			branch.right = min2;
			tree.add(branch);
		}
		TreePrinter.printTree(getRoot());
	}
	
	public void write(String fileName) {
		PrintWriter diskFile = null;
		try {
			diskFile = new PrintWriter(new File(fileName));
			encodeMap = new HashMap<Integer, String>();
			write(getRoot(),"", diskFile, encodeMap);
			diskFile.close();
			for (int charVal: encodeMap.keySet()) {
				System.out.println((char)charVal + " " + encodeMap.get(charVal));
			}
		}
		catch (IOException io){
			System.out.println("Could not create file: " + fileName);
		}
		
	}
	
	private void write(Node node, String binaryPath, PrintWriter diskFile, HashMap<Integer, String> encodeMap) {
		if (node == null) {
			return;
		}
		if (node.val != null) {
			diskFile.println((int)node.val);
			diskFile.println(binaryPath);
			encodeMap.put((int)node.val, binaryPath);
		}
		write(node.left, binaryPath + "0", diskFile, encodeMap);
		write(node.right, binaryPath + "1", diskFile, encodeMap);
	}
	
	public void writeShort(BitOutputStream out, String fileName) {
		try {
			Scanner in = new Scanner(new File(fileName + ".txt"));
			while (in.hasNextLine()) {
				String s = in.nextLine();
				if (in.hasNextLine()) {
					s += "\n";
				}
				System.out.println(s);
				for (int i = 0;i < s.length();i++) {
					String binaryPath = encodeMap.get((int)s.charAt(i));
					for (int j = 0;j < binaryPath.length();j++) {
						System.out.println(Integer.parseInt("" + binaryPath.charAt(j)));
						out.writeBit(Integer.parseInt("" + binaryPath.charAt(j)));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Node getRoot() {
		return tree.peek();
	}
	
	public HuffmanTree(String codeFile){
		try {
			Scanner in = new Scanner(new File(codeFile));
			decodeMap = new HashMap<String, Integer>();
			while (in.hasNextLine()) {
				int n = Integer.parseInt(in.nextLine());
				String code = in.nextLine();
				decodeMap.put(code,n);
			}
			Node root = new Node();
			root.weight = 0;
			tree = new PriorityQueue<>();
			tree.add(root);
			treeBuilder(root, "", decodeMap);
			
			for (String path: decodeMap.keySet()) {
				System.out.println(path + " " + decodeMap.get(path));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void treeBuilder(Node node, String binaryPath, HashMap<String, Integer> decodeMap) {
		if (node == null) {
			return;
		}
		if (decodeMap.containsKey(binaryPath)) {
			int n = decodeMap.get(binaryPath);
			node.val = (char)n;
			node.weight = 0;
			return;
		}
		node.left = new Node();
		node.right = new Node();
		treeBuilder(node.left, binaryPath + "0", decodeMap);
		treeBuilder(node.right, binaryPath + "1", decodeMap);
	}
	
	public void decode(BitInputStream in, String outFile) {
		try {
			PrintWriter diskFile = new PrintWriter(new File(outFile));
			decode(in, diskFile, getRoot());
			diskFile.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//work on this
	private void decode(BitInputStream in, PrintWriter diskFile, Node node) {
		if (node == null) {
			return;
		}
		if (node.left == null && node.right == null) {
			if ((int)node.val == 256) {
				return;
			}
			else {
				diskFile.print(node.val);
				node = getRoot();
			}
		}
		int binary = in.readBit();
		if (binary == 0) {
			decode(in,diskFile,node.left);
		}
		else if (binary == 1) {
			decode(in,diskFile,node.right);
		}
	}
}
