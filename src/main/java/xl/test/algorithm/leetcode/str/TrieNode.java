package xl.test.algorithm.leetcode.str;

/**
 * created by MSI-PC on 2017/8/13
 */
public class TrieNode {

    // R links to node children
    private TrieNode[] links;

    private final int R = 26;

    private boolean isEnd;

    // number of children non null links
    private int size;
    public void put(char ch, TrieNode node) {
        links[ch -'a'] = node;
        size++;
    }

    public int getLinks() {
        return size;
    }
    //assume methods containsKey, isEnd, get, put are implemented as it is described
    //in  https://leetcode.com/articles/implement-trie-prefix-tree/)

}
