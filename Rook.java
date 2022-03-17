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
    return super.isValidMove(newPosition) &&
        (
          isWhite && getLeft()>=1 && (this.getPosition().getCol()-newPosition.getCol())<=getLeft() && (this.getPosition().getCol()-newPosition.getCol())>=1 && (this.getPosition().getRow()-newPosition.getRow())==0
          || isWhite && getRight()>=1 && (newPosition.getCol()-this.getPosition().getCol())<=getRight() && (newPosition.getCol()-this.getPosition().getCol())>=1 && (this.getPosition().getRow()-newPosition.getRow())==0
          || isWhite && getUp()>=1 && (newPosition.getCol()-this.getPosition().getCol())==0 && (newPosition.getRow()-this.getPosition().getRow())<=getUp() && (newPosition.getRow()-this.getPosition().getRow())>=1
          || isWhite && getDown()>=1 && (newPosition.getCol()-this.getPosition().getCol())==0 && (this.getPosition().getRow()-newPosition.getRow())<=getUp() && (this.getPosition().getRow()-newPosition.getRow())>=1
          || !isWhite && getLeft()>=1 && (newPosition.getCol()-this.getPosition().getCol())<=getLeft() && (newPosition.getCol()-this.getPosition().getCol())>=1 && (this.getPosition().getRow()-newPosition.getRow())==0
          || !isWhite && getRight()>=1 && (this.getPosition().getCol()-newPosition.getCol())<=getRight() && (this.getPosition().getCol()-newPosition.getCol())>=1 && (this.getPosition().getRow()-newPosition.getRow())==0
          || !isWhite && getUp()>=1 && (newPosition.getCol()-this.getPosition().getCol())==0 && (this.getPosition().getRow()-newPosition.getRow())<=getUp() && (this.getPosition().getRow()-newPosition.getRow())>=1
          || !isWhite && getDown()>=1 && (newPosition.getCol()-this.getPosition().getCol())==0 && (newPosition.getRow()-this.getPosition().getRow())<=getUp() && (newPosition.getRow()-this.getPosition().getRow())>=1
          || newPosition.isEp() && isWhite && newPosition.getCol()-this.getPosition().getCol()<=getRight() && newPosition.getCol()-this.getPosition().getCol()>=1 && newPosition.getRow()-this.getPosition().getRow()==0
          || newPosition.isEp() && isWhite && this.getPosition().getCol()-newPosition.getCol()<=getLeft() && this.getPosition().getCol()-newPosition.getCol()>=1 && newPosition.getRow()-this.getPosition().getRow()==0
          || newPosition.isEp() && isWhite && this.getPosition().getCol()-newPosition.getCol()==0 && newPosition.getRow()-this.getPosition().getRow()<=getUp() && newPosition.getRow()-this.getPosition().getRow()>=1
          || newPosition.isEp() && isWhite && this.getPosition().getCol()-newPosition.getCol()==0 && this.getPosition().getRow()-newPosition.getRow()<=getDown() && this.getPosition().getRow()-newPosition.getRow()>=1
          || newPosition.isEp() && !isWhite && this.getPosition().getCol()-newPosition.getCol()<=getRight() && this.getPosition().getCol()-newPosition.getCol()>=1 && newPosition.getRow()-this.getPosition().getRow()==0
          || newPosition.isEp() && !isWhite && newPosition.getCol()-this.getPosition().getCol()<=getLeft() && newPosition.getCol()-this.getPosition().getCol()>=1 && newPosition.getRow()-this.getPosition().getRow()==0
          || newPosition.isEp() && !isWhite && this.getPosition().getCol()-newPosition.getCol()==0 && this.getPosition().getRow()-newPosition.getRow()<=getUp() && this.getPosition().getRow()-newPosition.getRow()>=1
          || newPosition.isEp() && !isWhite && this.getPosition().getCol()-newPosition.getCol()==0 && newPosition.getRow()-this.getPosition().getRow()<=getDown() && newPosition.getRow()-this.getPosition().getRow()>=1
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
