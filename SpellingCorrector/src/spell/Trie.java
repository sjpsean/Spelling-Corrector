package spell;

public class Trie implements ITrie {
  private Node root = new Node();
  private int wordCount = 0;
  private int nodeCount = 1;
  private int forHash = 1;

  @Override
  public void add(String word) {
    /* read word
     * for every character
     *   Node rootTemp;
     *   see if the Node for that index is null
     *   if null
     *    node[index of that char].add one node. Node's child will have one more child
     *   if not null
     *    rootTemp = node[index of that char].node[index of next char]
     */
    Node tempRoot = root;
    for (int i = 0; i < word.length(); i++) {
      char ch = word.charAt(i);
      int idx = ch-'a';
      if (tempRoot.nodes[idx] == null) {
        tempRoot.nodes[idx] = new Node();
        nodeCount++;
      }

      if (i == word.length()-1){
        tempRoot.nodes[idx].freqPlus();
        forHash += idx;
        // count only the first time it is being saved.
        if (tempRoot.nodes[idx].getValue() == 1){
          wordCount++;
        }
      }
      tempRoot = tempRoot.nodes[ch-'a'];
    }
  }

  @Override
  public Node find(String word) {
    Node currentRoot = root;
    for (int i = 0; i < word.length(); i++) {
      char c = word.charAt(i);
      int idx = c - 'a';
      if (currentRoot.nodes[idx] != null) {
        currentRoot = currentRoot.nodes[idx];
      }
      else {
        return null;
      }
    }
    if (currentRoot.getValue() > 0) {
      return currentRoot;
    }
    return null;
  }

  @Override
  public int getWordCount() {
    return wordCount;
  }
  @Override
  public int getNodeCount() {
    return nodeCount;
  }

  @Override
  public int hashCode() {
//    int nodeInRoot = 0;
//    for (int i = 0; i < 26; i++) {
//      if (root.nodes[i] != null) {
//        return nodeCount * wordCount ^ i;
//      }
//    }
////    return nodeCount * wordCount ^ nodeInRoot;
    return nodeCount ^ wordCount * forHash;
  }


  @Override
  public boolean equals(Object o) {
    if (o == null) {return false;}
    if (o == this) {return true;}
    if (o.getClass() != this.getClass()){return false;}
    Trie trie = (Trie) o; // typecast o to trie.
    // compare nodeCount and wordCount before we dig in
    if (trie.getNodeCount() != this.getNodeCount() || trie.getWordCount() != this.getWordCount()) {
      return false;
    }
    return equals_Helper(this.root, trie.root);
  }
  private boolean equals_Helper(Node n, Node m) {
    /*
     * if it is false, set isEqual to false and get out
     * if not,
     * go through all the children node and see if they have any difference.
     * As soon as we find a difference, isEqual = false and return;
     *   if there is a node that both of tries have,
     *    do the same thing to their children nodes.
     */
    Node[] nChildren = n.nodes;
    Node[] mChildren = m.nodes;
    Node nChild;
    Node mChild;
    boolean output = true;
    for (int i = 0; i < 26; i++) {
      nChild = nChildren[i];
      mChild = mChildren[i];
      if (nChild != null && mChild == null) {
        return false;
      }
      if (nChild == null && mChild != null) {
        return false;
      }
      if (nChild != null) {
        if (nChild.getValue() != mChild.getValue()) {
          return false;
        }

        output = equals_Helper(nChild, mChild);
      }
    }

    return output;
  }

  @Override
  public String toString() {
    /*
     * 1. check each node in root if any of the children is not null
     * 2. if null -> pass
     * 3. if not null -> do the same thing. check every nodes in that node
     * 4. when reaching the end, add that string to the output.
     */
    StringBuilder currentWord = new StringBuilder();
    StringBuilder output = new StringBuilder();
    toString_Helper(root, currentWord, output);
    return output.toString();
  }
  private void toString_Helper(Node n, StringBuilder currentWord, StringBuilder output) {
    if (n == null) {
      return;
    }
    if (n.getValue() > 0) {
//      output.append(currentWord.toString()).append(" : ").append(n.getValue()).append("\n");
      output.append(currentWord.toString()).append("\n");
    }
    Node[] children = n.nodes;
    for (int i = 0; i < 26; i++) {
      Node child = children[i];
      if (child != null) {
        char ch =(char)('a'+i);
        currentWord.append(ch);
        toString_Helper(child, currentWord, output);
        currentWord.deleteCharAt(currentWord.length()-1);
      }
    }
  }
}


