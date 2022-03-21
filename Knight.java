package GroupMiniProject;

public class Knight extends Piece {

  public Knight(String symbol, int value, boolean isWhite, Position position){
    super(symbol, value, isWhite, position);
  }

  @Override
  public boolean isValidMove(Position newPosition) {
    int rowDiff = newPosition.getRow()-this.getPosition().getRow();
    int colDiff = newPosition.getCol()-this.getPosition().getCol();
    return
      super.isValidMove(newPosition) &&
               (rowDiff == 2 && colDiff == 1)
            || (colDiff == 2 && rowDiff == 1)
         ;
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
