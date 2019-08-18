package leetcode.str;

/**
 * created by MSI-PC on 2017/8/13
 */
public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    //assume methods insert, search, searchPrefix are implemented as it is described
//in  https://leetcode.com/articles/implement-trie-prefix-tree/)
    private String searchLongestPrefix(String word) {
        TrieNode node = root;
        StringBuilder prefix = new StringBuilder();
        //for (int i = 0; i < word.length(); i++) {
        //    char curLetter = word.charAt(i);
        //    if (node.containsKey(curLetter) && (node.getLinks() == 1) && (!node.isEnd())) {
        //        prefix.append(curLetter);
        //        node = node.get(curLetter);
        //    } else {
        //        return prefix.toString();
        //    }
        //}
        return prefix.toString();
    }
}
