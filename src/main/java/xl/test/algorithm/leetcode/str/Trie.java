package xl.test.algorithm.leetcode.str;

import org.junit.Test;

import java.util.List;

/**
 * 实现一个 Trie (前缀树)，包含 insert, search, 和 startsWith 这三个操作。
 *
 * 示例:
 *
 * Trie trie = new Trie();
 *
 * trie.insert("apple");
 * trie.search("apple");   // 返回 true
 * trie.search("app");     // 返回 false
 * trie.startsWith("app"); // 返回 true
 * trie.insert("app");
 * trie.search("app");     // 返回 true
 *
 * 说明:
 *
 *     你可以假设所有的输入都是由小写字母 a-z 构成的。
 *     保证所有输入均为非空字符串。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/implement-trie-prefix-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * 2019/10/21 继续实现...
 *
 * created by MSI-PC on 2017/8/13
 */
public class Trie {

    private TrieNode root = new TrieNode();

    public static Trie buildTrie(List<String> wordDict) {
        Trie trie = new Trie();
        for (String s : wordDict) {
            trie.insert(s);
        }
        return trie;
    }

    public boolean wordBreak(String s) {
        return wordBreak0(root, s);
    }

    private boolean wordBreak0(TrieNode node, String s) {
        for (int i = 0; i < s.length(); i++) {
            TrieNode child = node.getChild(s.charAt(i));
            if (child == null) {
                // 没匹配到
                return false;
            } else if (child.isEnd()) {
                // 当前字符串匹配结束
                if (wordBreak0(root, s.substring(i + 1, s.length()))) {
                    return true;
                }
                node = child;
            } else {
                if (i == s.length() - 1) {
                    // 匹配完却没匹配到
                    return false;
                }
                node = child;
            }
        }
        return true;
    }


    @Test
    public void trie () {
        Trie trie = new Trie();
        trie.insert("apple");
        System.out.println(trie.search("apple"));   // 返回 true
        System.out.println(trie.search("app"));    // 返回 false
        System.out.println(trie.startsWith("app")); // 返回 true
        trie.insert("app");
        System.out.println(trie.search("app"));     // 返回 true
    }

    public void insert (String s) {
        if (s == null || s.length() == 0) {
            return;
        }
        TrieNode node = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            TrieNode childNode = node.getChild(c);
            if (childNode == null) {
                TrieNode tempNode = new TrieNode();
                node.put(c, tempNode);
                node = tempNode;
            } else {
                node = childNode;
            }
            if (i == s.length() - 1) {
                node.setEnd(true);
            }
        }
    }

    public boolean search (String s) {
        TrieNode trieNode = searchLongestPrefix(s);
        return trieNode != null && trieNode.isEnd();
    }


    public boolean startsWith (String s) {
        return searchLongestPrefix(s) != null;
    }


    private TrieNode searchLongestPrefix(String word) {
        TrieNode node = root;
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char curLetter = word.charAt(i);
            TrieNode childNode = node.getChild(curLetter);
            if (childNode != null) {
                node = childNode;
            } else {
                return null;
            }
        }
        return node;
    }
}
