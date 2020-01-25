package spell;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector {
  private Trie myTree = new Trie();
  private String suggestion = "";
//  private Trie test = new Trie();

  @Override
  public void useDictionary(String dictionaryFileName) throws IOException {
    File file = new File(dictionaryFileName);
    Scanner sc = new Scanner(file);
    while (sc.hasNext()) {
      String word = sc.next();
      word = word.toLowerCase();
      // System.out.println(word);
      myTree.add(word);
//      test.add(word);
//      test.add(word);
    }
//    System.out.println(myTree.toString()); // toString test
//    System.out.printf("wordCount : %d\n", myTree.getWordCount()); // wordCount test
//    System.out.printf("nodeCount : %d\n", myTree.getNodeCount()); // nodeCount test
//    System.out.println(test.toString());
//    System.out.println(myTree.equals(test));
//    System.out.println(test.hashCode());
//    System.out.println(myTree.hashCode());
  }

  @Override
  public String suggestSimilarWord(String inputWord) {
    /*
     * 1. use inputWord to generate first edit suggestion words.
     * 2. with the list of words, find it from the trie and put that in foundInTree
     * 3. from foundList see which one has the most freq.
     */
    inputWord = inputWord.toLowerCase();
    if (myTree.find(inputWord) != null) {
      if (myTree.find(inputWord).getValue() > 0) {
        return inputWord;
      }
    }

    TreeSet<String> possibleWords = new TreeSet<>();
    TreeSet<String> possibleWordsTwo = new TreeSet<>();
    TreeSet<String> foundInTree = new TreeSet<>();

    // Distance 1
    listPossibleWords(possibleWords, inputWord);
//    System.out.printf("possibleWords : %s\n", possibleWords.toString()); // test
    findInTree(possibleWords, foundInTree);
//    System.out.printf("foundInTree : %s\n", foundInTree.toString()); // test
    if(setSuggestion(foundInTree)) {
//      System.out.printf("suggestion: %s\n", this.suggestion); // test
      return this.suggestion;
    }

    // Distance 2
    for (String eachWord : possibleWords) {
      listPossibleWords(possibleWordsTwo, eachWord);
//      System.out.printf("possibleWords from %s : %s\n", eachWord, possibleWordsTwo.toString()); // test
    }
    foundInTree.clear();
    findInTree(possibleWordsTwo, foundInTree);
//    System.out.printf("foundInTree2 : %s\n", foundInTree.toString()); // test
    if(setSuggestion(foundInTree)) {
//      System.out.printf("suggestion2: %s\n", this.suggestion); // test
      return this.suggestion;
    }
    return null;
  }

  private void listPossibleWords(TreeSet<String> possibleWords, String word) {
    deletion(possibleWords, word);
    transposition(possibleWords, word);
    alteration(possibleWords, word);
    insertion(possibleWords, word);
  }

  private void deletion(TreeSet<String> possibleWords, String word) {
    for (int i = 0; i < word.length(); i++) {
      StringBuilder newWord = new StringBuilder(word);
      newWord.deleteCharAt(i);
      possibleWords.add(newWord.toString());
    }
  }
  private void transposition(TreeSet<String> possibleWords, String word) {
    for (int i = 0; i < word.length() - 1; i++) {
      StringBuilder newWord = new StringBuilder(word);
      String changeWords = word.substring(i, i + 2);
      newWord.setCharAt(i, changeWords.charAt(1));
      newWord.setCharAt(i+1, changeWords.charAt(0));
      possibleWords.add(newWord.toString());
    }
  }
  private void alteration(TreeSet<String> possibleWords, String word) {
    for (int i = 0; i < word.length(); i++) {
      for (int w = 0; w < 26; w++) {
        StringBuilder newWord = new StringBuilder(word);
        newWord.setCharAt(i,(char)('a'+w));
        possibleWords.add(newWord.toString());
      }
    }

  }
  private void insertion(TreeSet<String> possibleWords, String word) {
    for (int i = 0; i <= word.length(); i++) {
      for (int w = 0; w < 26; w++) {
        StringBuilder newWord = new StringBuilder(word);
        newWord.insert(i,(char)('a'+w));
        possibleWords.add(newWord.toString());
      }
    }
  }

  private void findInTree(TreeSet<String> possibleWords, TreeSet<String> foundInTree) {
    for (String thisWord : possibleWords) {
      if (myTree.find(thisWord) != null) {
        foundInTree.add(thisWord);
      }
    }
  }

  private boolean setSuggestion(TreeSet<String> foundInTree){
    int oneFreq = 0;
    if(foundInTree.isEmpty()) {
      return false;
    }
    for(String eachWord : foundInTree) {
      if (myTree.find(eachWord).getValue() > oneFreq){
        this.suggestion = eachWord;
        oneFreq = myTree.find(eachWord).getValue();
      }
    }
    return true;
  }

}



