package GroupMiniProject;

public class Pawn extends Piece {

  private boolean isFirstMove;
  private boolean leftFront;
  private boolean front;
  private boolean rightFront;
  private boolean epFlag;
  private boolean promoted;
  private Piece newPiece;

  public Pawn(String symbol, int value, boolean isWhite, Position position,
      boolean isFirstMove, boolean leftFront, boolean front, boolean rightFront, boolean epFlag,
      boolean promoted, Piece newPiece) {
    super(symbol, value, isWhite, position);
    this.setFirstMove(isFirstMove);
    this.setLeftFront(leftFront);
    this.setFront(front);
    this.setRightFront(rightFront);
    this.setEpFlag(epFlag);
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

  public boolean isEpFlag() { return epFlag; }

  public void setEpFlag(boolean epFlag) { this.epFlag = epFlag; }

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
    int rowDiff = Math.abs(newPosition.getRow()-this.getPosition().getRow());
    int colDiff = Math.abs(newPosition.getCol()-this.getPosition().getCol());
    if(getNewPiece() instanceof Rook){
      return super.isValidMove(newPosition) &&
          (
              isWhite && ((Rook)getNewPiece()).getLeft()>=1 && colDiff<=((Rook)getNewPiece()).getLeft() && colDiff>=1 && rowDiff==0
              || isWhite &&  ((Rook)getNewPiece()).getRight()>=1 && colDiff<=((Rook)getNewPiece()).getRight() && colDiff>=1 && rowDiff==0
              || isWhite &&  ((Rook)getNewPiece()).getUp()>=1 && colDiff==0 && rowDiff<=((Rook)getNewPiece()).getUp() && rowDiff>=1
              || isWhite &&  ((Rook)getNewPiece()).getDown()>=1 && colDiff==0 && rowDiff<=((Rook)getNewPiece()).getDown() && rowDiff>=1
              || !isWhite &&  ((Rook)getNewPiece()).getLeft()>=1 && colDiff<=((Rook)getNewPiece()).getLeft() && colDiff>=1 && rowDiff==0
              || !isWhite &&  ((Rook)getNewPiece()).getRight()>=1 && colDiff<=((Rook)getNewPiece()).getRight() &&colDiff>=1 && rowDiff==0
              || !isWhite &&  ((Rook)getNewPiece()).getUp()>=1 && colDiff==0 && rowDiff<=((Rook)getNewPiece()).getUp() && rowDiff>=1
              || !isWhite &&  ((Rook)getNewPiece()).getDown()>=1 && colDiff==0 && rowDiff<=((Rook)getNewPiece()).getDown() && rowDiff>=1
          );
    }else {
      return super.isValidMove(newPosition)
          && (
          isFirstMove() && isWhite && rowDiff==2 && colDiff==0
              || isFirstMove() && !isWhite && rowDiff==2 && colDiff==0
              || !isFront() && isWhite && rowDiff==1 && colDiff==0
              || !isFront() && !isWhite && rowDiff==1 && colDiff==0
              || isLeftFront() && isWhite && rowDiff==1 && colDiff==1
              || isLeftFront() && !isWhite && rowDiff==1 && colDiff==1
              || isRightFront() && isWhite && rowDiff==1 && colDiff==1
              || isRightFront() && !isWhite && rowDiff==1 && colDiff==1
      );
    }
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
