package GroupMiniProject;

public class Queen extends Piece {


  public Queen(String symbol, int value, boolean isWhite, Position position){
    super(symbol, value, isWhite, position);
  }

  @Override
  public boolean isValidMove(Position newPosition) {


    int rowDiff = newPosition.getRow()-this.getPosition().getRow();
    int colDiff = newPosition.getCol()-this.getPosition().getCol();

      return super.isValidMove(newPosition) &&

                  (isWhite && rowDiff > 0 && colDiff == 0)
              ||  (!isWhite && rowDiff > 0 && colDiff == 0)
              ||  (isWhite && colDiff > 0 && rowDiff == 0)
              ||  (!isWhite && colDiff > 0 && rowDiff == 0)
              ||  (isWhite && colDiff == rowDiff && colDiff > 0 || !isWhite && colDiff == rowDiff && colDiff > 0);

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
