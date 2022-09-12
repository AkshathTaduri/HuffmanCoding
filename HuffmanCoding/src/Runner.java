
public class Runner {
	public static void main(String[] args) {
//		String text = "happy hip hop";
//		int[] count = new int[256];
//		for (int i = 0;i < text.length();i++) {
//			int index = (int)text.charAt(i);
//			count[index]++;
//		}
//		HuffmanTree huffmanTree = new HuffmanTree("H:\\JavaFiles\\test.txt");
//		TreePrinter.printTree(huffmanTree.getRoot());
		//HuffmanCompressor.compress("short");
//		HuffmanCompressor.expand("short.code", "short");
		HuffmanCompressor.compress("happy");
		HuffmanCompressor.expand("happy.code", "happy");
	}
}
