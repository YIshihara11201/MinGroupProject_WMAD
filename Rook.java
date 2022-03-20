package GroupMiniProject;

public class Rook extends Piece {

  private int left;
  private int right;
  private int up;
  private int down;

  public Rook(String symbol, int value, boolean isWhite, Position position, int left, int right, int up, int down) {
    super(symbol, value, isWhite, position);
    setLeft(left);
    setRight(right);
    setUp(up);
    setDown(down);
  }

  public int getLeft() {
    return left;
  }

  public void setLeft(int left) {
    this.left = left;
  }

  public int getRight() {
    return right;
  }

  public void setRight(int right) {
    this.right = right;
  }

  public int getUp() {
    return up;
  }

  public void setUp(int up) {
    this.up = up;
  }

  public int getDown() {
    return down;
  }

  public void setDown(int down) {
    this.down = down;
  }

  @Override
  public boolean isValidMove(Position newPosition) {
      int rowDiff = newPosition.getRow()-this.getPosition().getRow();
      int colDiff = newPosition.getCol()-this.getPosition().getCol();

    return super.isValidMove(newPosition) &&
        (
            (isWhite && getLeft()>=1 && Math.abs(colDiff)<=getLeft() && colDiff<=-1 && rowDiff==0)
          || (isWhite && getRight()>=1 && colDiff<=getRight() && colDiff>=1 && rowDiff==0)
          || (isWhite && getUp()>=1 && colDiff==0 && rowDiff<=getUp() && rowDiff>=1)
          || (isWhite && getDown()>=1 && colDiff==0 && Math.abs(rowDiff)<=getDown() && rowDiff<=-1)
          || (!isWhite && getLeft()>=1 && colDiff<=getLeft() && colDiff>=1 && rowDiff==0)
          || (!isWhite && getRight()>=1 && Math.abs(colDiff)<=getRight() && colDiff<=-1 && rowDiff==0)
          || (!isWhite && getUp()>=1 && colDiff==0 && Math.abs(rowDiff)<=getUp() && rowDiff<=-1)
          || (!isWhite && getDown()>=1 && colDiff==0 && rowDiff<=getDown() && rowDiff>=1)
         );
  }

  @Override
  public String toString() {
    return "Rook{value=" + value + "} @[" + getPosition().toString() + "]";
  }

  @Override
  public boolean equals(Object piece) {
    if (!(piece instanceof Rook)) {
      return false;
    }
    if (this.value == ((Rook) piece).getValue()) {
      return false;
    }
    if (this.isWhite == ((Rook) piece).isWhite()) {
      return false;
    }
    return true;
  }

}
