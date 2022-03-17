package GroupMiniProject;

public abstract class Piece {
  public static final int ROW_COL_RANGE = 8;

  protected String symbol;
  protected int value;
  protected boolean isWhite;
  protected Position position;

  public Piece(String symbol, int value, boolean isWhite, Position position) {
    this.symbol = symbol;
    this.value = value;
    this.isWhite = isWhite;
    this.position = position;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public boolean isWhite() {
    return isWhite;
  }

  public void setWhite(boolean white) {
    isWhite = white;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public boolean isValidMove(Position newPosition) {
    return newPosition.getRow() >= 0 && newPosition.getCol() >= 0
        && newPosition.getRow() < ROW_COL_RANGE && newPosition.getCol() < ROW_COL_RANGE;
  }

  @Override
  public String toString() {
    return "This is a chess piece of value: " + getValue();
  }

  @Override
  public boolean equals(Object piece) {
    if (!(piece instanceof Piece)) {
      return false;
    }
    if (this.value == ((Piece) piece).value) {
      return false;
    }
    return this.isWhite != ((Piece) piece).isWhite;
  }
}
