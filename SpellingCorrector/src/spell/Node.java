package spell;

public class Node implements INode {

  private int frequency = 0;
  Node[] nodes = new Node[26];

  void freqPlus() {
    this.frequency++;
  }

  @Override
  public int getValue() {
    return frequency;
  }
}
