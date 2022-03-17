package GroupMiniProject;

public class Position {
  private int row;
  private int col;
  private boolean ep;

  public Position(int row, int col){
    setRow(row);
    setCol(col);
    setEp(false);
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }

  public boolean isEp() {
    return ep;
  }

  public void setEp(boolean ep) {
    this.ep = ep;
  }

  @Override
  public String toString() {
    return
        "row: " + (row+1) + ", col: " + convertColFromNumToAlphabet(col);
  }

  public String convertColFromNumToAlphabet(int colNumber){
    String colString = null;
    if(colNumber == 0) colString = "a";
    else if(colNumber == 1) colString = "b";
    else if(colNumber == 2) colString = "c";
    else if(colNumber == 3) colString = "d";
    else if(colNumber == 4) colString = "e";
    else if(colNumber == 5) colString = "f";
    else if(colNumber == 6) colString = "g";
    else if(colNumber == 7) colString = "h";

    return colString;
  }
}
