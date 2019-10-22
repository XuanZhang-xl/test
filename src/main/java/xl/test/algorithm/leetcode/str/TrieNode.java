package xl.test.algorithm.leetcode.str;

/**
 * created by MSI-PC on 2017/8/13
 */
public class TrieNode {

    // R links to node children
    private TrieNode[] links = null;

    private boolean isEnd = false;

    private int size = 0;

    public void put(char ch, TrieNode node) {
        if (links[ch -'a'] == null) {
            links[ch -'a'] = node;
            size++;
        }
    }

    public TrieNode getChild(char c) {
        return links[c - 'a'];
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public TrieNode() {
        links = new TrieNode[26];
    }
}
