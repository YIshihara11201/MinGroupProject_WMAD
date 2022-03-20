package GroupMiniProject;

public class Queen extends Piece {

  public Queen(String symbol, int value, boolean isWhite, Position position){
    super(symbol, value, isWhite, position);
  }

  @Override
  public boolean isValidMove(Position newPosition) {
    return
        //TODO
      super.isValidMove(newPosition) &&
        (
          Math.abs(this.getPosition().getCol() - newPosition.getCol()) == Math.abs(this.getPosition().getRow() - newPosition.getRow())
          && Math.abs(this.getPosition().getCol() - newPosition.getCol()) >= 1
        ) ||
      super.isValidMove(newPosition) &&
        (
          Math.abs(this.getPosition().getCol() - newPosition.getCol()) >= 1 && Math.abs(this.getPosition().getRow() - newPosition.getRow()) == 0
          || Math.abs(this.getPosition().getCol() - newPosition.getCol()) == 0 && Math.abs(this.getPosition().getRow() - newPosition.getRow()) >= 1
        );
  }

  @Override
  public String toString() {
    return "Queen{value= " + value + "} @[" + getPosition().toString() + "]";
  }

  @Override
  public boolean equals(Object piece){
    if(!(piece instanceof Queen))return false;
    if(this.value == ((Queen) piece).getValue()) return false;
    if(this.isWhite == ((Queen) piece).isWhite()) return false;
    return true;
  }

}
