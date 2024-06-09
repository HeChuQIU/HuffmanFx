package net.hechu.huffmanfx;

import java.util.*;

public class HuffmanCoding {
    private static class Node {
        char ch;
        int frequency;
        Node left = null;
        Node right = null;

        Node(char ch, int frequency, Node left, Node right) {
            this.ch = ch;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }
    }

    public static Map<Character, String> encode(String data) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : data.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(l -> l.frequency));
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue(), null, null));
        }

        while (pq.size() != 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            int sum = left.frequency + right.frequency;
            pq.add(new Node('\0', sum, left, right));
        }

        Node root = pq.peek();
        Map<Character, String> huffmanCode = new HashMap<>();
        encodeRecursive(root, "", huffmanCode);

        return huffmanCode;
    }

    private static void encodeRecursive(Node root, String str, Map<Character, String> huffmanCode) {
        if (root == null) {
            return;
        }

        if (root.left == null && root.right == null) {
            huffmanCode.put(root.ch, str);
        }

        encodeRecursive(root.left, str + '0', huffmanCode);
        encodeRecursive(root.right, str + '1', huffmanCode);
    }

    public static void main(String[] args) {
        String text = "这是一个 test string for Huffman encoding";
        Map<Character, String> huffmanCode = encode(text);

        System.out.println("Character Frequencies and Huffman Codes:");
        for (Map.Entry<Character, String> entry : huffmanCode.entrySet()) {
            System.out.println("'" + entry.getKey() + "': " + entry.getValue());
        }
    }
}