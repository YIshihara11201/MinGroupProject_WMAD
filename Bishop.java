package GroupMiniProject;

public class Bishop extends Piece {

  private int duLeft;
  private int duRight;
  private int ddLeft;
  private int ddRight;


  public Bishop(String symbol, int value, boolean isWhite, Position position, int duLeft, int duRight, int ddLeft, int ddRight) {
    super(symbol, value, isWhite, position);
    setDuLeft(duLeft);
    setDuRight(duRight);
    setDdLeft(ddLeft);
    setDdRight(ddRight);
  }

  public int getDuLeft() {
    return duLeft;
  }

  public void setDuLeft(int duLeft) {
    this.duLeft = duLeft;
  }

  public int getDuRight() {
    return duRight;
  }

  public void setDuRight(int duRight) {
    this.duRight = duRight;
  }

  public int getDdLeft() {
    return ddLeft;
  }

  public void setDdLeft(int ddLeft) {
    this.ddLeft = ddLeft;
  }

  public int getDdRight() {
    return ddRight;
  }

  public void setDdRight(int ddRight) {
    this.ddRight = ddRight;
  }

  @Override
  public boolean isValidMove(Position newPosition) {
    return super.isValidMove(newPosition)
        &&(getPosition().getRow()-newPosition.getRow() == 0 && getPosition().getCol()-newPosition.getCol() == 0

        );

  }

  @Override
  public String toString() {
    return "Bishop{value= " + value  + "} @[" + getPosition().toString() + "]";
  }

  @Override
  public boolean equals(Object piece) {
    if (!(piece instanceof Bishop)) {
      return false;
    }
    if (this.value == ((Bishop) piece).getValue()) {
      return false;
    }
    if (this.isWhite == ((Bishop) piece).isWhite()) {
      return false;
    }
    return true;
  }

}
