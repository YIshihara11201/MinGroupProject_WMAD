package GroupMiniProject;

public class King extends Piece {

  public King(String symbol, int value, boolean isWhite, Position position) {
    super(symbol, value, isWhite, position);
  }

  @Override
  public boolean isValidMove(Position newPosition) {
    return super.isValidMove(newPosition) &&
        (
            (Math.abs(this.getPosition().getCol() - newPosition.getCol()) == 1 && Math.abs(this.getPosition().getRow() - newPosition.getRow()) == 0)
                || (Math.abs(this.getPosition().getCol() - newPosition.getCol()) == 0 && Math.abs(this.getPosition().getRow() - newPosition.getRow()) == 1)
                || (Math.abs(this.getPosition().getCol() - newPosition.getCol()) == 1 && Math.abs(this.getPosition().getRow() - newPosition.getRow()) == 1)
        );
  }

  @Override
  public String toString() {
    return "King{value= " + value  + "} @[" + getPosition().toString() + "]";
  }

  @Override
  public boolean equals(Object piece) {
    if (!(piece instanceof King)) {
      return false;
    }
    if (this.value == ((King) piece).getValue()) {
      return false;
    }
    if (this.isWhite == ((King) piece).isWhite()) {
      return false;
    }
    return true;
  }

}
