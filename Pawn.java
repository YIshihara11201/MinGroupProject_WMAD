package GroupMiniProject;

public class Pawn extends Piece {

  private boolean isFirstMove;
  private boolean leftFront;
  private boolean front;
  private boolean rightFront;
  private boolean promoted;
  private Piece newPiece;

  public Pawn(String symbol, int value, boolean isWhite, Position position,
      boolean isFirstMove, boolean leftFront, boolean front, boolean rightFront,
      boolean promoted, Piece newPiece) {
    super(symbol, value, isWhite, position);
    this.setFirstMove(isFirstMove);
    this.setLeftFront(leftFront);
    this.setFront(front);
    this.setRightFront(rightFront);
    this.setPromoted(promoted);
    this.setNewPiece(newPiece);
  }

  public boolean isFirstMove() {
    return isFirstMove;
  }

  public void setFirstMove(boolean firstMove) {
    isFirstMove = firstMove;
  }

  public boolean isLeftFront() {
    return leftFront;
  }

  public void setLeftFront(boolean leftFront) {
    this.leftFront = leftFront;
  }

  public boolean isFront() {
    return front;
  }

  public void setFront(boolean front) {
    this.front = front;
  }

  public boolean isRightFront() {
    return rightFront;
  }

  public void setRightFront(boolean rightFront) {
    this.rightFront = rightFront;
  }

  public boolean isPromoted() {
    return promoted;
  }

  private void setPromoted(boolean promoted) {
    this.promoted = promoted;
  }

  public Piece getNewPiece() {
    return newPiece;
  }

  private void setNewPiece(Piece newPiece) {
    this.newPiece = newPiece;
  }

  public void promote(Piece newPiece) {
    setPromoted(true);
    setNewPiece(newPiece);
  }

  @Override
  public boolean isValidMove(Position newPosition) {
    return super.isValidMove(newPosition)
      && (
          isFirstMove() && isWhite && newPosition.getRow()-this.getPosition().getRow()==2 && newPosition.getCol()-this.getPosition().getCol()==0
        || isFirstMove() && !isWhite && this.getPosition().getRow()-newPosition.getRow()==2 && newPosition.getCol()-this.getPosition().getCol()==0
        || !isFront() && isWhite && newPosition.getRow()-this.getPosition().getRow()==1 && newPosition.getCol()-this.getPosition().getCol()==0
        || !isFront() && !isWhite && this.getPosition().getRow()-newPosition.getRow()==1 && this.getPosition().getCol()-newPosition.getCol()==0
        || isLeftFront() && isWhite && newPosition.getRow()-this.getPosition().getRow()==1 && this.getPosition().getCol()-newPosition.getCol()==1
        || isLeftFront() && !isWhite && this.getPosition().getRow()-newPosition.getRow()==1 && newPosition.getCol()-this.getPosition().getCol()==1
        || isRightFront() && isWhite && newPosition.getRow()-this.getPosition().getRow()==1 && newPosition.getCol()-this.getPosition().getCol()==1
        || isRightFront() && !isWhite && this.getPosition().getRow()-newPosition.getRow()==1 && this.getPosition().getCol()-newPosition.getCol()==1
        || newPosition.isEp() && isWhite && newPosition.getRow()-this.getPosition().getRow()==1 && this.getPosition().getCol()-newPosition.getCol()==1
        || newPosition.isEp() && !isWhite && this.getPosition().getRow()-newPosition.getRow()==1 && newPosition.getCol()-this.getPosition().getCol()==1
        || newPosition.isEp() && isWhite && newPosition.getRow()-this.getPosition().getRow()==1 && newPosition.getCol()-this.getPosition().getCol()==1
        || newPosition.isEp() && !isWhite && this.getPosition().getRow()-newPosition.getRow()==1 && this.getPosition().getCol()-newPosition.getCol()==1
      );
  }

  @Override
  public String toString() {
    return "Pawn{value=" + value + "} @[" + getPosition().toString() + "]";
  }

  @Override
  public boolean equals(Object piece) {
    if (!(piece instanceof Pawn)) {
      return false;
    }
    if (this.value != ((Pawn) piece).getValue()) {
      return false;
    }
    if (this.isWhite != ((Pawn) piece).isWhite()) {
      return false;
    }
    if (isPromoted() != ((Pawn) piece).isPromoted()) {
      return false;
    }
    return getNewPiece() == ((Pawn) piece).getNewPiece();
  }

}
