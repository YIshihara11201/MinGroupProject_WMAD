package GroupMiniProject;

public class Knight extends Piece {

  public Knight(String symbol, int value, boolean isWhite, Position position){
    super(symbol, value, isWhite, position);
  }

  @Override
  public boolean isValidMove(Position newPosition) {
    return
      super.isValidMove(newPosition) &&
          (
              Math.abs(this.getPosition().getCol() - newPosition.getCol()) == 2 && Math.abs(this.getPosition().getRow() - newPosition.getRow()) == 1
              || Math.abs(this.getPosition().getCol() - newPosition.getCol()) == 1 && Math.abs(this.getPosition().getRow() - newPosition.getRow()) == 2
          );
  }

  @Override
  public String toString() {
    return "Knight{value= " + value  + "} @[" + getPosition().toString() + "]";
  }

  @Override
  public boolean equals(Object piece){
    if(!(piece instanceof Knight))return false;
    if(this.value == ((Knight) piece).getValue()) return false;
    if(this.isWhite == ((Knight) piece).isWhite()) return false;
    return true;
  }

}
