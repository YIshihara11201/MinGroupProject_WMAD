package GroupMiniProject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game {

  public static final int ROW_RANGE = 8;
  public static final int COL_RANGE = 8;

  private Piece[][] board;
  private int turn = 1;
  private Pawn epPiece = null;

  private Integer[] resultRecord = new Integer[]{0, 0};


  // Constructor creates an empty board
  public Game() {
    initialize();
  }

  public int getTurn() {
    return turn;
  }

  public void setTurn(int turn) {
    this.turn = turn;
  }

  public Pawn getEpPiece() {
    return epPiece;
  }

  public void setEpPiece(Pawn epPiece) {
    this.epPiece = epPiece;
  }

  public Integer[] getResultRecord() {
    return resultRecord;
  }

  public void setResultRecord(Integer[] resultRecord) {
    this.resultRecord = resultRecord;
  }

  public Piece[][] getBoard() {
    return board;
  }

  public void setBoard(Piece[][] board) {
    this.board = board;
  }

  // 1.help functionality
  public void showHelp() {
    System.out.println("* type 'help' for help");
    System.out.println("* type 'board' to see the board again");
    System.out.println("* type 'resign' to resign");
    System.out.println("* type 'square' (e.g. b1, e2) to list possible moves for that square");
    System.out.println("* type 'UCI' (e.g. b1c3, e7e8) to make a move");
    System.out.println("* type 'quit' to end game");
    System.out.println();
  }

  // 2.board display functionality
  public void printBoard() {
    for (int i = ROW_RANGE - 1; i >= 0; i--) {
      for (int j = 0; j < COL_RANGE; j++) {
        if (board[i][j] != null) {
          if(board[i][j] instanceof Pawn && ((Pawn) board[i][j]).isPromoted()){
            System.out.print(((Pawn) board[i][j]).getNewPiece().getSymbol() + "  ");
          }else{
            System.out.print(board[i][j].getSymbol() + "  ");
          }
        } else {
          System.out.print("•  ");
        }
        if (j == COL_RANGE - 1) {
          System.out.println(" |" + (i + 1));
        }
      }
    }
    System.out.println("-  -  -  -  -  -  -  -");
    System.out.println("a  b  c  d  e  f  g  h");
    System.out.println();
  }

  // 3.resign functionality
  public void resign() {
    String winner = "White";
    if (getTurn() % 2 == 1) {
      winner = "Black";
      setResultRecord(new Integer[]{getResultRecord()[0], getResultRecord()[1] + 1});
    } else {
      setResultRecord(new Integer[]{getResultRecord()[0] + 1, getResultRecord()[1]});
    }
    System.out.println(
        "Game over - " + getResultRecord()[0] + "-" + getResultRecord()[1] + " - " + winner
            + " won by resignation");
    System.out.println();
    initialize();
  }

  // 4.display available cells
  public void showAvailableCell(String position) throws Exception {

    int col = convertColFromAlphabetToNum(position.substring(0, 1));
    int row;
    try {
      row = Integer.parseInt(position.substring(1, 2)) - 1;
    } catch (NumberFormatException e) {
      throw new Exception("Invalid Input, input [a1-h8] for showing available cells. Try again");
    }

    if (col == -1 || row < 0 || row >= ROW_RANGE) {
      throw new Exception("Invalid Input, input [a1-h8] for showing available cells. Try again");
    }

    if (getBoard()[row][col] == null) {
      throw new Exception("Invalid Input, no piece in the cell. Try again");
    }

    Piece targetPiece = getBoard()[row][col];

    System.out.println(targetPiece);
    List<String> validCells = new ArrayList<>();
    for (int i = 0; i < COL_RANGE; i++) {
      for (int j = 0; j < ROW_RANGE; j++) {
        if (targetPiece.isValidMove(new Position(j, i))){
//            && (getBoard()[j][i] == null || targetPiece.isWhite != getBoard()[j][i].isWhite)) {
          String colStr = convertColFromNumToAlphabet(i);
          String rowStr = Integer.toString(j + 1);
          validCells.add(colStr + rowStr);
        }
      }
    }
    System.out.println("Possible moves for " + position + ": ");
    System.out.println(Arrays.toString(validCells.toArray()));
    System.out.println();
  }

  // 5.move functionality
  public void movePiece(String move) throws Exception {
    int currCol = convertColFromAlphabetToNum(move.substring(0, 1));
    int targetCol = convertColFromAlphabetToNum(move.substring(2, 3));
    int currRow;
    int targetRow;
    try {
      currRow = Integer.parseInt(move.substring(1, 2)) - 1;
      targetRow = Integer.parseInt(move.substring(3, 4)) - 1;
    } catch (NumberFormatException e) {
      throw new Exception(
          "Invalid Input, input [x1y1-x8y8] (x must not be y) for moving piece. Try again");
    }

    if (currCol == -1 || targetCol == -1
        || currRow < 0 || currRow >= ROW_RANGE
        || targetRow < 0 || targetRow >= ROW_RANGE || (currRow==targetRow&&currCol==targetCol)
    ) {
      throw new Exception(
          "Invalid Input, input [x1y1-x8y8] (x must not be y) for moving piece. Try again");
    }

    Piece currentCell = getBoard()[currRow][currCol];
    if(currentCell instanceof Pawn && ((Pawn) currentCell).getNewPiece() != null){
      currentCell = ((Pawn)getBoard()[currRow][currCol]).getNewPiece();
    }
    Piece targetCell = getBoard()[targetRow][targetCol];

    if (currentCell == null) {
      throw new Exception("Invalid Input, no piece in cell[" + move.substring(0, 2) + "]. Try again");
    }

    if ((getTurn()%2==1 && !currentCell.isWhite)||(getTurn()%2==0 && currentCell.isWhite)) {
      throw new Exception(
          "Invalid Input, you can't move opponent piece @[" + move.substring(0, 2) + "] . Try again");
    }

    if ((targetCell != null && currentCell.isWhite == targetCell.isWhite)) {
      throw new Exception(
          "Invalid Input, cell[" + move.substring(2, 4) + "] has allies piece. Try again");
    }

    if (!currentCell.isValidMove(new Position(targetRow, targetCol))) {
      throw new Exception(
          "Invalid Input, piece[" + move.substring(0, 2) + "] can't move to [" + move.substring(2, 4) + "]. Try again");
    }

    // move piece here
    if (currentCell.isValidMove(new Position(targetRow, targetCol))) {
      System.out.println("OK");
      System.out.println();

      // en passant setting
      checkFirstMoveAndEnPassant(currRow, currCol, targetRow);

      // set position property
      getBoard()[currRow][currCol].setPosition(new Position(targetRow, targetCol));

      // move to target cell
      getBoard()[targetRow][targetCol] = getBoard()[currRow][currCol];
      getBoard()[currRow][currCol] = null;

      if(targetRow==0 || targetRow==ROW_RANGE-1){
        promotePawn(targetRow, targetCol);
      }


      //****** TODO ********
      // renew setting for moving piece
      setSurroundingStateOfMovingPawn(targetRow, targetCol);
      setSurroundingStateOfMovingRook(targetRow, targetCol);

      //****** TODO ********
      // renew setting for surrounding state for pieces affected by moving piece
      setSurroundingStateOfOtherPawn(currRow, currCol, targetRow, targetCol);
      setSurroundingStateOfOtherRook(currRow, currCol, targetRow, targetCol);

      printBoard();
      System.out.println();
    }
  }

  // helper functions

  public void checkFirstMoveAndEnPassant(int currRow, int currCol,int targetRow){
    //　２ターン前に動かしたポーンのepフラグがtrueの場合フラグをおろす
    if(epPiece!=null && epPiece.isEpFlag()){
      if((getTurn()%2==1 && epPiece.isWhite) || (getTurn()%2==0 && !epPiece.isWhite)){
        setEpPiece(null);
      }
    }
    if(getBoard()[currRow][currCol] instanceof  Pawn) {
      // epフラグがtrueのPawnを動かすときはフラグをおろす
      if ((currRow == 3 || currRow == 4)
          && ((Pawn) getBoard()[currRow][currCol]).isEpFlag()) {
        ((Pawn) getBoard()[currRow][currCol]).setEpFlag(false);
      }

      // Pawnを初期位置から2マス動かす時は、動かすPawnに対してepフラグをあげる
      if ((targetRow == 3 || targetRow == 4)
          && ((Pawn) getBoard()[currRow][currCol]).isFirstMove()) {
        ((Pawn) getBoard()[currRow][currCol]).setEpFlag(true);
      }

      // 初期位置の Pawn を動かす場合は firstMoveフラグをおろす
      if (((Pawn) getBoard()[currRow][currCol]).isFirstMove()) {
        ((Pawn) getBoard()[currRow][currCol]).setFirstMove(false);
      }
    }
  }

  public void setSurroundingStateOfMovingPawn(int targetRow, int targetCol){
    // Pawnを動かした後、そのPawnが次に動ける範囲を設定する
    // アンパッサンの処理も記述
    if (getBoard()[targetRow][targetCol] instanceof Pawn && !((Pawn) getBoard()[targetRow][targetCol]).isPromoted()) {

      // 動く先が相手Pawnの残像の時、本体も同時に消す
      if(targetRow==5
          && getBoard()[targetRow-1][targetCol] instanceof Pawn
          && ((Pawn) getBoard()[targetRow-1][targetCol]).isEpFlag()) {
        getBoard()[targetRow-1][targetCol] = null;
      }
      if(targetRow==2
          && getBoard()[targetRow+1][targetCol] instanceof Pawn
          && ((Pawn) getBoard()[targetRow+1][targetCol]).isEpFlag()){
        getBoard()[targetRow+1][targetCol] = null;
      }

      //白のコマについて
      if (getBoard()[targetRow][targetCol].isWhite && targetRow+1<ROW_RANGE) {
        // １マス前にコマがあるかどうかチェック
        ((Pawn) getBoard()[targetRow][targetCol]).setFront(getBoard()[targetRow+1][targetCol]!=null && !getBoard()[targetRow+1][targetCol].isWhite);
        // 斜め左前にコマがあるかどうかチェック
        ((Pawn) getBoard()[targetRow][targetCol]).setLeftFront(targetCol-1>=0 && getBoard()[targetRow+1][targetCol-1]!=null && !getBoard()[targetRow+1][targetCol-1].isWhite);
        // 斜め右前にコマがあるかどうかチェック
        ((Pawn) getBoard()[targetRow][targetCol]).setRightFront(targetCol+1<COL_RANGE && getBoard()[targetRow+1][targetCol+1] != null && !getBoard()[targetRow+1][targetCol+1].isWhite);

        // 動かすPawnのepフラグがtrueかつ移動したセルの右隣に黒のPawnがある場合、その黒PawnのrightFrontフラグを立てる
        if(
            ((Pawn) getBoard()[targetRow][targetCol]).isEpFlag()
                && targetCol+1<COL_RANGE
                && getBoard()[targetRow][targetCol+1]!=null
                && getBoard()[targetRow][targetCol+1] instanceof Pawn
                && !getBoard()[targetRow][targetCol+1].isWhite
        ){
          ((Pawn) getBoard()[targetRow][targetCol+1]).setRightFront(true);
        }

        // 動かすPawnのepフラグがtrueかつ移動したセルの左隣に黒のPawnがある場合、その黒PawnのleftFrontフラグを立てる
        if(
            ((Pawn) getBoard()[targetRow][targetCol]).isEpFlag()
                && targetCol-1>=0
                && getBoard()[targetRow][targetCol-1]!=null
                && getBoard()[targetRow][targetCol-1] instanceof Pawn
                && !getBoard()[targetRow][targetCol-1].isWhite
        ){
          ((Pawn) getBoard()[targetRow][targetCol-1]).setLeftFront(true);
        }
      }
      // 黒のコマについて
      if (!getBoard()[targetRow][targetCol].isWhite && targetRow-1>=0) {
        ((Pawn) getBoard()[targetRow][targetCol]).setFront(getBoard()[targetRow-1][targetCol] != null && getBoard()[targetRow-1][targetCol].isWhite);
        ((Pawn) getBoard()[targetRow][targetCol]).setLeftFront(targetCol+1<COL_RANGE && getBoard()[targetRow-1][targetCol+1] != null  && getBoard()[targetRow-1][targetCol+1].isWhite);
        ((Pawn) getBoard()[targetRow][targetCol]).setRightFront(targetCol-1>=0 && getBoard()[targetRow-1][targetCol-1]!=null  && getBoard()[targetRow-1][targetCol-1].isWhite);
        // 動かすPawnのepフラグがtrueかつ移動したセルの右隣に白のPawnがある場合、その白PawnのrightFrontフラグを立てる
        if(
            ((Pawn) getBoard()[targetRow][targetCol]).isEpFlag()
                && targetCol-1>=0
                && getBoard()[targetRow][targetCol-1]!=null
                && getBoard()[targetRow][targetCol-1] instanceof Pawn
                && getBoard()[targetRow][targetCol-1].isWhite
        ){
          ((Pawn) getBoard()[targetRow][targetCol-1]).setRightFront(true);
        }

        // 動かすPawnのepフラグがtrueかつ移動したセルの左隣に黒のPawnがある場合、その黒PawnのleftFrontフラグを立てる
        if(
            ((Pawn) getBoard()[targetRow][targetCol]).isEpFlag()
                && targetCol+1<COL_RANGE
                && getBoard()[targetRow][targetCol+1]!=null
                && getBoard()[targetRow][targetCol+1] instanceof Pawn
                && getBoard()[targetRow][targetCol+1].isWhite
        ){
          ((Pawn) getBoard()[targetRow][targetCol-1]).setLeftFront(true);
        }
      }
    }
  }

  public void setSurroundingStateOfMovingRook(int targetRow, int targetCol){
    if (getBoard()[targetRow][targetCol] instanceof Rook
        ||(getBoard()[targetRow][targetCol] instanceof Pawn
          && ((Pawn) getBoard()[targetRow][targetCol]).getNewPiece() instanceof Rook)
    ){
      int right = 0;
      int left = 0;
      int up = 0;
      int down = 0;
      //白のコマについて
      if (getBoard()[targetRow][targetCol].isWhite) {
        // Rookの右何マス先にコマがあるかチェック
        for(int i=targetCol; i<COL_RANGE-1; i++){
          if(getBoard()[targetRow][i+1]!=null){
            right = i-targetCol;
            if(getBoard()[targetRow][i+1].isWhite != getBoard()[targetRow][targetCol].isWhite) right += 1;
            break;
          } else if(i+1==COL_RANGE-1 && getBoard()[targetRow][COL_RANGE-1]==null) right = COL_RANGE-1-targetCol;
        }

        // Rookの左何マス先にコマがあるかチェック
        for(int i=targetCol; i>0; i--){
          if(getBoard()[targetRow][i-1]!=null){
            left = targetCol-i;
            if(getBoard()[targetRow][i-1].isWhite != getBoard()[targetRow][targetCol].isWhite) left += 1;
            break;
          }else if(i-1==0 && getBoard()[targetRow][0]==null) left = targetCol;
        }

        // Rookの何マス上にコマがあるかチェック
        for(int i=targetRow; i<ROW_RANGE-1; i++){
          if(getBoard()[i+1][targetCol]!=null){
            up = i-targetRow;
            if(getBoard()[i+1][targetCol].isWhite != getBoard()[targetRow][targetCol].isWhite) up += 1;
            break;
          }else if(i+1==ROW_RANGE-1 && getBoard()[ROW_RANGE-1][targetCol] == null) up = ROW_RANGE-1-targetRow;
        }

        // Rookの何マス下にコマがあるかチェック
        for(int i=targetRow; i>0; i--){
          if(getBoard()[i-1][targetCol]!=null){
            down = targetRow-i;
            if(getBoard()[i-1][targetCol].isWhite != getBoard()[targetRow][targetCol].isWhite) down += 1;
            break;
          } else if(i-1==0 && getBoard()[0][targetCol]==null) down = targetRow;
        }

        // 黒のコマについて
      }else{
        // Rookの右何マス先にコマがあるかチェック
        for(int i=targetCol; i>0; i--){
          if(getBoard()[targetRow][i-1]!=null){
            right = targetCol-i;
            if(getBoard()[targetRow][i-1].isWhite != getBoard()[targetRow][targetCol].isWhite) right += 1;
            break;
          }else if(i-1==0 &&  getBoard()[targetRow][0]==null) right = targetCol;
        }

        // Rookの左何マス先にコマがあるかチェック
        for(int i=targetCol; i<COL_RANGE-1; i++){
          if(getBoard()[targetRow][i+1]!=null){
            left = i-targetCol;
            if(getBoard()[targetRow][i+1].isWhite != getBoard()[targetRow][targetCol].isWhite) left += 1;
            break;
          }else if(i+1==COL_RANGE-1 && getBoard()[targetRow][COL_RANGE-1]==null) left = COL_RANGE-1-targetCol;
        }

        // Rookの何マス上にコマがあるかチェック
        for(int i=targetRow; i>0; i--){
          if(getBoard()[i-1][targetCol]!=null){
            up = targetRow-i;
            if(getBoard()[i-1][targetCol].isWhite != getBoard()[targetRow][targetCol].isWhite) up += 1;
            break;
          } else if(i-1==0 && getBoard()[0][targetCol]==null) up = targetRow;
        }

        // Rookの何マス下にコマがあるかチェック
        for(int i=targetRow; i<ROW_RANGE-1; i++){
          if(getBoard()[i+1][targetCol]!=null){
            down = i-targetRow;
            if(getBoard()[i+1][targetCol].isWhite != getBoard()[targetRow][targetCol].isWhite) down += 1;
            break;
          }else if(i+1==ROW_RANGE-1 && getBoard()[ROW_RANGE-1][targetCol]==null) down = ROW_RANGE-1-targetRow;
        }
      }
      if(getBoard()[targetRow][targetCol] instanceof Pawn
          && ((Pawn) getBoard()[targetRow][targetCol]).getNewPiece() instanceof Rook){
        ((Rook) ((Pawn) getBoard()[targetRow][targetCol]).getNewPiece()).setRight(right);
        ((Rook) ((Pawn) getBoard()[targetRow][targetCol]).getNewPiece()).setLeft(left);
        ((Rook) ((Pawn) getBoard()[targetRow][targetCol]).getNewPiece()).setUp(up);
        ((Rook) ((Pawn) getBoard()[targetRow][targetCol]).getNewPiece()).setDown(down);
      }else{
        ((Rook)getBoard()[targetRow][targetCol]).setRight(right);
        ((Rook)getBoard()[targetRow][targetCol]).setLeft(left);
        ((Rook)getBoard()[targetRow][targetCol]).setUp(up);
        ((Rook)getBoard()[targetRow][targetCol]).setDown(down);
      }
    }
  }

  public void setSurroundingStateOfOtherPawn(int currRow, int currCol, int targetRow, int targetCol){
    // pattern1 コマを動かした先のセルが Pawn の移動範囲に含まれている場合その Pawn の移動可能範囲を設定する
    if (getBoard()[targetRow][targetCol].isWhite) {
      // 白のコマを動かした先のセルが、黒のPawnからみて左斜め前にある時
      if (targetRow+1<ROW_RANGE && targetCol-1 >= 0
          && getBoard()[targetRow+1][targetCol-1]!=null
          && getBoard()[targetRow+1][targetCol-1] instanceof Pawn
          && !((Pawn) getBoard()[targetRow+1][targetCol-1]).isPromoted()
          && !getBoard()[targetRow+1][targetCol-1].isWhite) {
        ((Pawn) getBoard()[targetRow+1][targetCol-1]).setLeftFront(true);
      }
      // 白のコマを動かした先のセルが、黒のPawnからみて正面にある時
      if (targetRow+1<ROW_RANGE
          && getBoard()[targetRow+1][targetCol]!=null
          && getBoard()[targetRow+1][targetCol] instanceof Pawn
          && !((Pawn) getBoard()[targetRow+1][targetCol]).isPromoted()
          && !getBoard()[targetRow+1][targetCol].isWhite) {
        ((Pawn) getBoard()[targetRow+1][targetCol]).setFront(true);
      }
      // 白のコマを動かした先のセルが、黒のPawnからみて右斜め前にある時
      if (targetRow+1<ROW_RANGE && targetCol+1<COL_RANGE
          && getBoard()[targetRow+1][targetCol+1]!=null
          && getBoard()[targetRow+1][targetCol+1] instanceof Pawn
          && !((Pawn) getBoard()[targetRow+1][targetCol+1]).isPromoted()
          && !getBoard()[targetRow+1][targetCol+1].isWhite) {
        ((Pawn) getBoard()[targetRow+1][targetCol+1]).setRightFront(true);
      }
      // 白のコマを動かした先のセルが、白のPawnからみて左斜め前にある時
      if (targetRow-1>=0 && targetCol+1<COL_RANGE
          && getBoard()[targetRow-1][targetCol+1]!=null
          && getBoard()[targetRow-1][targetCol+1] instanceof Pawn
          && !((Pawn) getBoard()[targetRow-1][targetCol+1]).isPromoted()
          && getBoard()[targetRow-1][targetCol+1].isWhite) {
        ((Pawn) getBoard()[targetRow-1][targetCol+1]).setLeftFront(true);
      }
      // 白のコマを動かした先のセルが、白のPawnからみて正面にある時
      if (targetRow-1>=0
          && getBoard()[targetRow-1][targetCol]!=null
          && getBoard()[targetRow-1][targetCol] instanceof Pawn
          && !((Pawn) getBoard()[targetRow-1][targetCol]).isPromoted()
          && getBoard()[targetRow-1][targetCol].isWhite) {
        ((Pawn) getBoard()[targetRow-1][targetCol]).setFront(true);
      }
      // 白のコマを動かした先のセルが、白のPawnからみて右斜め前にある時
      if (targetRow-1>=0
          && targetCol-1>=0
          && getBoard()[targetRow-1][targetCol-1]!=null
          && getBoard()[targetRow-1][targetCol-1] instanceof Pawn
          && !((Pawn) getBoard()[targetRow-1][targetCol-1]).isPromoted()
          && getBoard()[targetRow-1][targetCol-1].isWhite) {
        ((Pawn) getBoard()[targetRow-1][targetCol-1]).setRightFront(true);
      }
    } else {
      // 黒のコマを動かした先のセルが、白のPawnからみて右斜め前にある時
      if (targetRow-1>=0 && targetCol-1>=0
          && getBoard()[targetRow-1][targetCol-1]!=null
          && getBoard()[targetRow-1][targetCol-1] instanceof Pawn
          && !((Pawn) getBoard()[targetRow-1][targetCol-1]).isPromoted()
          && getBoard()[targetRow-1][targetCol-1].isWhite) {
        ((Pawn) getBoard()[targetRow-1][targetCol-1]).setRightFront(true);
      }
      // 黒のコマを動かした先のセルが、白のPawnからみて正面にある時
      if (targetRow-1>=0
          && getBoard()[targetRow-1][targetCol]!=null
          && getBoard()[targetRow-1][targetCol] instanceof Pawn
          && !((Pawn) getBoard()[targetRow-1][targetCol]).isPromoted()
          && getBoard()[targetRow-1][targetCol].isWhite) {
        ((Pawn) getBoard()[targetRow-1][targetCol]).setFront(true);
      }
      // 黒のコマを動かした先のセルが、白のPawnからみて左斜め前にある時
      if (targetRow-1>=0 && targetCol+1<COL_RANGE
          && getBoard()[targetRow-1][targetCol+1]!=null
          && getBoard()[targetRow-1][targetCol+1] instanceof Pawn
          && !((Pawn) getBoard()[targetRow-1][targetCol+1]).isPromoted()
          && getBoard()[targetRow-1][targetCol+1].isWhite) {
        ((Pawn) getBoard()[targetRow-1][targetCol+1]).setLeftFront(true);
      }

      // 黒のコマを動かした先のセルが、黒のPawnからみて右斜め前にある時
      if (targetRow+1<ROW_RANGE && targetCol+1<COL_RANGE
          && getBoard()[targetRow+1][targetCol+1]!=null
          && getBoard()[targetRow+1][targetCol+1] instanceof Pawn
          && !((Pawn) getBoard()[targetRow+1][targetCol+1]).isPromoted()
          && !getBoard()[targetRow+1][targetCol+1].isWhite) {
        ((Pawn) getBoard()[targetRow+1][targetCol+1]).setRightFront(true);
      }

      // 黒のコマを動かした先のセルが、黒のPawnからみて正面にある時
      if (targetRow+1<ROW_RANGE
          && getBoard()[targetRow+1][targetCol]!=null
          && getBoard()[targetRow+1][targetCol] instanceof Pawn
          && !((Pawn) getBoard()[targetRow+1][targetCol]).isPromoted()
          && !getBoard()[targetRow+1][targetCol].isWhite) {
        ((Pawn) getBoard()[targetRow+1][targetCol]).setFront(true);
      }

      // 黒のコマを動かした先のセルが、黒のPawnからみて左斜め前にある時
      if (targetRow+1<ROW_RANGE && targetCol-1>=0
          && getBoard()[targetRow+1][targetCol-1]!=null
          && getBoard()[targetRow+1][targetCol-1] instanceof Pawn
          && !((Pawn) getBoard()[targetRow+1][targetCol-1]).isPromoted()
          && !getBoard()[targetRow+1][targetCol-1].isWhite) {
        ((Pawn) getBoard()[targetRow+1][targetCol-1]).setLeftFront(true);
      }
    }

    // pattern2 移動前のコマが、Pawnの前3マスに隣接している場合
    // 移動後に前3マスから外れる場合にはフラグを解除する
    if (getBoard()[targetRow][targetCol].isWhite) {
      // 白のPawnから見て左斜め前にある場合
      if (currRow-1>=0 && currCol+1<COL_RANGE
          && getBoard()[currRow-1][currCol+1] != null
          && getBoard()[currRow-1][currCol+1] instanceof Pawn
          && !((Pawn) getBoard()[currRow-1][currCol+1]).isPromoted()
          && !getBoard()[currRow-1][currCol+1].isWhite) {
        ((Pawn) getBoard()[currRow-1][currCol+1]).setLeftFront(false);
      }
      // 白のPawnから見て正面にある場合
      if (currRow-1>=0
          && getBoard()[currRow-1][currCol] != null
          && getBoard()[currRow-1][currCol] instanceof Pawn
          && !((Pawn) getBoard()[currRow-1][currCol]).isPromoted()
          && !getBoard()[currRow-1][currCol].isWhite) {
        ((Pawn) getBoard()[currRow-1][currCol]).setFront(false);
      }
      // 白のPawnから見て右斜め前にある場合
      if (currRow-1>=0 && currCol-1>=0
          && getBoard()[currRow-1][currCol-1] != null
          && getBoard()[currRow-1][currCol-1] instanceof Pawn
          && !((Pawn) getBoard()[currRow-1][currCol-1]).isPromoted()
          && !getBoard()[currRow-1][currCol-1].isWhite) {
        ((Pawn) getBoard()[currRow-1][currCol-1]).setRightFront(false);
      }
      // 黒のPawnから見て左斜め前にある場合
      if (currRow+1<ROW_RANGE && currCol-1>=0
          && getBoard()[currRow+1][currCol-1] != null
          && getBoard()[currRow+1][currCol-1] instanceof Pawn
          && !((Pawn) getBoard()[currRow+1][currCol-1]).isPromoted()
          && !getBoard()[currRow+1][currCol-1].isWhite) {
        ((Pawn) getBoard()[currRow+1][currCol-1]).setLeftFront(false);
      }
      // 黒のPawnから見て正面にある場合
      if (currRow+1<ROW_RANGE
          && getBoard()[currRow+1][currCol] != null
          && getBoard()[currRow+1][currCol] instanceof Pawn
          && !((Pawn) getBoard()[currRow+1][currCol]).isPromoted()
          && !getBoard()[currRow+1][currCol].isWhite) {
        ((Pawn) getBoard()[currRow+1][currCol]).setFront(false);
      }
      // 黒のPawnから見て右斜め前にある場合
      if (currRow+1<ROW_RANGE && currCol+1<COL_RANGE
          && getBoard()[currRow+1][currCol+1] != null
          && getBoard()[currRow+1][currCol+1] instanceof Pawn
          && !((Pawn) getBoard()[currRow+1][currCol+1]).isPromoted()
          && !getBoard()[currRow+1][currCol+1].isWhite) {
        ((Pawn) getBoard()[currRow+1][currCol+1]).setRightFront(false);
      }
    }else {
      // 白のPawnから見て右斜め前にある場合
      if (currRow-1>=0 && currCol-1>= 0
          && getBoard()[currRow-1][currCol-1]!=null
          && getBoard()[currRow-1][currCol-1] instanceof Pawn
          && !((Pawn) getBoard()[currRow-1][currCol-1]).isPromoted()
          && getBoard()[currRow-1][currCol-1].isWhite) {
        ((Pawn) getBoard()[currRow-1][currCol-1]).setRightFront(false);
      }
      // 白のPawnから見て正面にある場合
      if (currRow-1>= 0
          && getBoard()[currRow-1][currCol]!=null
          && getBoard()[currRow-1][currCol] instanceof Pawn
          && !((Pawn) getBoard()[currRow-1][currCol]).isPromoted()
          && getBoard()[currRow-1][currCol].isWhite) {
        ((Pawn) getBoard()[currRow-1][currCol]).setFront(false);
      }
      // 白のPawnから見て左斜め前にある場合
      if (currRow-1>= 0 && currCol+1 < COL_RANGE
          && getBoard()[currRow-1][currCol+1]!=null
          && getBoard()[currRow-1][currCol+1] instanceof Pawn
          && !((Pawn) getBoard()[currRow-1][currCol+1]).isPromoted()
          && getBoard()[currRow-1][currCol+1].isWhite) {
        ((Pawn) getBoard()[currRow-1][currCol+1]).setLeftFront(false);
      }
      // 黒のPawnから見て右斜め前にある場合
      if (currRow+1<ROW_RANGE
          && currCol+1<COL_RANGE && getBoard()[currRow+1][currCol+1]!=null
          && getBoard()[currRow+1][currCol+1] instanceof Pawn
          && !((Pawn) getBoard()[currRow+1][currCol+1]).isPromoted()
          && getBoard()[currRow+1][currCol+1].isWhite) {
        ((Pawn) getBoard()[currRow+1][currCol+1]).setRightFront(false);
      }
      // 黒のPawnから見て正面にある場合
      if (currRow+1<ROW_RANGE
          && getBoard()[currRow+1][currCol]!=null
          && getBoard()[currRow+1][currCol] instanceof Pawn
          && !((Pawn) getBoard()[currRow+1][currCol]).isPromoted()
          && getBoard()[currRow+1][currCol].isWhite) {
        ((Pawn) getBoard()[currRow+1][currCol]).setFront(false);
      }
      // 黒のPawnから見て左斜め前にある場合
      if (currRow+1<ROW_RANGE && currCol-1>=0
          && getBoard()[currRow+1][currCol-1]!=null
          && getBoard()[currRow+1][currCol-1] instanceof Pawn
          && !((Pawn) getBoard()[currRow+1][currCol-1]).isPromoted()
          && getBoard()[currRow+1][currCol-1].isWhite) {
        ((Pawn) getBoard()[currRow+1][currCol-1]).setLeftFront(false);
      }
    }
  }

  public void setSurroundingStateOfOtherRook(int currRow, int currCol, int targetRow, int targetCol){

    // set movable area for Rook when its available area is affected by other piece's move

    // when target cell hits black Rook's left-hand movable area
    int AbsColOfBlackRookLeftFromTargetPiece = -1;
    int MovableColOfBlackRookRight = -1;
    for(int i=targetCol-1; i>=0; i--) {
      if (getBoard()[targetRow][i]!=null
          && (getBoard()[targetRow][i] instanceof Rook
            || (getBoard()[targetRow][i] instanceof Pawn && ((Pawn) getBoard()[targetRow][i]).getNewPiece() instanceof Rook))
          && !getBoard()[targetRow][i].isWhite
      ){
        AbsColOfBlackRookLeftFromTargetPiece = i;
        if(getBoard()[targetRow][targetCol].isWhite){
          MovableColOfBlackRookRight = i;
        }
        else {
          MovableColOfBlackRookRight = i-1;
        }
        break;
      }else if(getBoard()[targetRow][i]!=null && !(getBoard()[targetRow][i] instanceof Rook)){
        break;
      }
    }
    if(AbsColOfBlackRookLeftFromTargetPiece != -1) {

      if(getBoard()[targetRow][AbsColOfBlackRookLeftFromTargetPiece] instanceof Rook){
        ((Rook)getBoard()[targetRow][AbsColOfBlackRookLeftFromTargetPiece])
            .setLeft(targetCol-MovableColOfBlackRookRight);
      }else if(
          getBoard()[targetRow][AbsColOfBlackRookLeftFromTargetPiece] instanceof Pawn
          && ((Pawn) getBoard()[targetRow][AbsColOfBlackRookLeftFromTargetPiece]).getNewPiece() instanceof Rook
      ){
        ((Rook)((Pawn) getBoard()[targetRow][AbsColOfBlackRookLeftFromTargetPiece])
            .getNewPiece())
            .setLeft(targetCol-MovableColOfBlackRookRight);
      }
    }

    // // when target cell hits black Rook's right-hand movable area
    int AbsColOfBlackRookRightFromTargetPiece = -1;
    int MovableColOfBlackRookLeft = -1;
    for(int i=targetCol+1; i<COL_RANGE; i++){
      if(getBoard()[targetRow][i]!=null
          && (getBoard()[targetRow][i] instanceof Rook
            || (getBoard()[targetRow][i] instanceof Pawn && ((Pawn) getBoard()[targetRow][i]).getNewPiece() instanceof Rook))
          && !getBoard()[targetRow][i].isWhite
      ){
        AbsColOfBlackRookRightFromTargetPiece = i;
        if(getBoard()[targetRow][targetCol].isWhite) MovableColOfBlackRookLeft= i;
        else MovableColOfBlackRookLeft = i-1;
        break;
      }else if(getBoard()[targetRow][i]!=null && !(getBoard()[targetRow][i] instanceof Rook)){
        break;
      }
    }
    if(AbsColOfBlackRookRightFromTargetPiece != -1) {

      if(getBoard()[targetRow][AbsColOfBlackRookRightFromTargetPiece] instanceof Rook){
        ((Rook)getBoard()[targetRow][AbsColOfBlackRookRightFromTargetPiece])
            .setRight(MovableColOfBlackRookLeft-targetCol);
      }else if(
          getBoard()[targetRow][AbsColOfBlackRookRightFromTargetPiece] instanceof Pawn
          && ((Pawn) getBoard()[targetRow][AbsColOfBlackRookRightFromTargetPiece]).getNewPiece() instanceof Rook
      ){
        ((Rook)((Pawn) getBoard()[targetRow][AbsColOfBlackRookRightFromTargetPiece])
            .getNewPiece())
            .setRight(MovableColOfBlackRookLeft-targetCol);
      }
    }

    // // when target cell hits black Rook's upper movable area
    int AbsColOfBlackRookUpFromTargetPiece = -1;
    int MovableRowOfBlackRookDown = -1;
    for(int i=targetRow+1; i<ROW_RANGE; i++){
      if(getBoard()[i][targetCol]!=null
          && (getBoard()[i][targetCol] instanceof Rook
            || (getBoard()[i][targetCol] instanceof Pawn && ((Pawn) getBoard()[i][targetCol]).getNewPiece() instanceof Rook))
          && !getBoard()[i][targetCol].isWhite
      ){
        AbsColOfBlackRookUpFromTargetPiece = i;
        if(getBoard()[targetRow][targetCol].isWhite) {
          MovableRowOfBlackRookDown= i;
        }
        else MovableRowOfBlackRookDown = i-1;
        break;
      }else if(getBoard()[i][targetCol]!=null && !(getBoard()[i][targetCol] instanceof Rook)){
        break;
      }
    }
    if(AbsColOfBlackRookUpFromTargetPiece != -1) {
      if (getBoard()[AbsColOfBlackRookUpFromTargetPiece][targetCol] instanceof Rook) {
        ((Rook)getBoard()[AbsColOfBlackRookUpFromTargetPiece][targetCol])
            .setUp(MovableRowOfBlackRookDown-targetRow);
      }else if(
          getBoard()[AbsColOfBlackRookUpFromTargetPiece][targetCol] instanceof Pawn
          && ((Pawn) getBoard()[AbsColOfBlackRookUpFromTargetPiece][targetCol]).getNewPiece() instanceof Rook
      ){
        ((Rook)((Pawn) getBoard()[AbsColOfBlackRookUpFromTargetPiece][targetCol])
            .getNewPiece())
            .setUp(MovableRowOfBlackRookDown-targetRow);
      }
    }

    // // when target cell hits black Rook's lower movable area
    int AbsColOfBlackRookDownFromTargetPiece = -1;
    int MovableRowOfBlackRookUp = -1;
    for(int i=targetRow-1; i>=0; i--){
      if(getBoard()[i][targetCol]!=null
          && (getBoard()[i][targetCol] instanceof Rook
              || (getBoard()[i][targetCol] instanceof Pawn && ((Pawn) getBoard()[i][targetCol]).getNewPiece() instanceof Rook))
          && !getBoard()[i][targetCol].isWhite
      ){
        AbsColOfBlackRookDownFromTargetPiece = i;
        if(getBoard()[targetRow][targetCol].isWhite) MovableRowOfBlackRookUp= i-1;
        else MovableRowOfBlackRookUp = i;
        break;
      }else if(getBoard()[i][targetCol]!=null && !(getBoard()[i][targetCol] instanceof Rook)){
        break;
      }
    }
    if(AbsColOfBlackRookDownFromTargetPiece != -1) {
      if(getBoard()[AbsColOfBlackRookDownFromTargetPiece][targetCol] instanceof Rook){
        ((Rook)getBoard()[AbsColOfBlackRookDownFromTargetPiece][targetCol])
            .setDown(targetRow-MovableRowOfBlackRookUp);
      }else if(
          getBoard()[AbsColOfBlackRookDownFromTargetPiece][targetCol] instanceof Pawn
          && ((Pawn) getBoard()[AbsColOfBlackRookDownFromTargetPiece][targetCol]).getNewPiece() instanceof Rook
      ){
        ((Rook)((Pawn) getBoard()[AbsColOfBlackRookDownFromTargetPiece][targetCol])
            .getNewPiece())
            .setDown(targetRow-MovableRowOfBlackRookUp);
      }
    }

    // when target cell hits white Rook's right-hand movable area
    int AbsColOfWhiteRookLeftFromTargetPiece = -1;
    int MovableColOfWhiteRookRight = -1;
    for(int i=targetCol-1; i>=0; i--){
      if(getBoard()[targetRow][i]!=null
          && (getBoard()[targetRow][i] instanceof Rook
            || (getBoard()[targetRow][i] instanceof Pawn && ((Pawn) getBoard()[targetRow][i]).getNewPiece() instanceof Rook))
          && getBoard()[targetRow][i].isWhite
      ){
        AbsColOfWhiteRookLeftFromTargetPiece = i;
        if(getBoard()[targetRow][targetCol].isWhite) MovableColOfWhiteRookRight= i;
        else MovableColOfWhiteRookRight = i-1;
        break;
      }else if(getBoard()[targetRow][i]!=null && !(getBoard()[targetRow][i] instanceof Rook)){
        break;
      }
    }
    if(AbsColOfWhiteRookLeftFromTargetPiece != -1) {
      if(getBoard()[targetRow][AbsColOfWhiteRookLeftFromTargetPiece] instanceof Rook){
        ((Rook)getBoard()[targetRow][AbsColOfWhiteRookLeftFromTargetPiece])
            .setRight(targetCol-MovableColOfWhiteRookRight);
      }else if(
          getBoard()[targetRow][AbsColOfWhiteRookLeftFromTargetPiece] instanceof Pawn
          && ((Pawn) getBoard()[targetRow][AbsColOfWhiteRookLeftFromTargetPiece]).getNewPiece() instanceof Rook
      ){
        ((Rook)((Pawn)getBoard()[targetRow][AbsColOfWhiteRookLeftFromTargetPiece])
            .getNewPiece())
            .setRight(targetCol-MovableColOfWhiteRookRight);
      }
    }

    // when target cell hits white Rook's left-hand movable area
    int AbsColOfWhiteRookRightFromTargetPiece = -1;
    int MovableColOfWhiteRookLeft = -1;
    for(int i=targetCol+1; i<COL_RANGE; i++){
      if(getBoard()[targetRow][i]!=null
          && (getBoard()[targetRow][i] instanceof Rook
            || (getBoard()[targetRow][i] instanceof Pawn && ((Pawn) getBoard()[targetRow][i]).getNewPiece() instanceof Rook))
          && getBoard()[targetRow][i].isWhite
      ){
        AbsColOfWhiteRookRightFromTargetPiece = i;
        if(getBoard()[targetRow][targetCol].isWhite) MovableColOfWhiteRookLeft= i-1;
        else MovableColOfWhiteRookLeft = i;
        break;
      }else if(getBoard()[targetRow][i]!=null && !(getBoard()[targetRow][i] instanceof Rook)){
        break;
      }
    }
    if(AbsColOfWhiteRookRightFromTargetPiece != -1) {
      if(getBoard()[targetRow][AbsColOfWhiteRookRightFromTargetPiece] instanceof Rook){
        ((Rook)getBoard()[targetRow][AbsColOfWhiteRookRightFromTargetPiece])
            .setLeft(MovableColOfWhiteRookLeft-targetCol);
      }else if(
          getBoard()[targetRow][AbsColOfWhiteRookRightFromTargetPiece] instanceof Pawn
          && ((Pawn) getBoard()[targetRow][AbsColOfWhiteRookRightFromTargetPiece]).getNewPiece() instanceof Rook
      ){
        ((Rook)((Pawn)getBoard()[targetRow][AbsColOfWhiteRookRightFromTargetPiece])
            .getNewPiece())
            .setLeft(MovableColOfWhiteRookLeft-targetCol);
      }
    }

    // when target cell hits white Rook's upper movable area
    int AbsRowOfWhiteRookDownFromTargetPiece = -1;
    int MovableRowOfWhiteRookUp = -1;
    for(int i=targetRow-1; i>=0; i--){
      if(getBoard()[i][targetCol]!=null
          && (getBoard()[i][targetCol] instanceof Rook
            || (getBoard()[i][targetCol] instanceof Pawn && ((Pawn) getBoard()[i][targetCol]).getNewPiece() instanceof Rook))
          && getBoard()[i][targetCol].isWhite
      ){
        AbsRowOfWhiteRookDownFromTargetPiece = i;
        if(getBoard()[targetRow][targetCol].isWhite) {
          MovableRowOfWhiteRookUp= i+1;
        }
        else MovableRowOfWhiteRookUp = i;
        break;
      }else if(getBoard()[i][targetCol]!=null && !(getBoard()[i][targetCol] instanceof Rook)){
        break;
      }
    }
    if(AbsRowOfWhiteRookDownFromTargetPiece != -1) {
      if(getBoard()[AbsRowOfWhiteRookDownFromTargetPiece][targetCol] instanceof Rook){
        System.out.println(AbsRowOfWhiteRookDownFromTargetPiece);
        ((Rook)getBoard()[AbsRowOfWhiteRookDownFromTargetPiece][targetCol])
            .setUp(targetRow-MovableRowOfWhiteRookUp);
      }else if(
          getBoard()[AbsRowOfWhiteRookDownFromTargetPiece][targetCol] instanceof Pawn
          && ((Pawn) getBoard()[AbsRowOfWhiteRookDownFromTargetPiece][targetCol]).getNewPiece() instanceof Rook
      ){
        ((Rook)((Pawn)getBoard()[AbsRowOfWhiteRookDownFromTargetPiece][targetCol])
            .getNewPiece())
            .setUp(targetRow-MovableRowOfWhiteRookUp);
      }
    }

    // when target cell hits white Rook's lower movable area
    int AbsRowOfWhiteRookUpFromTargetPiece = -1;
    int MovableRowOfWhiteRookDown = -1;
    for(int i=targetRow+1; i<ROW_RANGE; i++){
      if(getBoard()[i][targetCol]!=null
          && (getBoard()[i][targetCol] instanceof Rook
            || (getBoard()[i][targetCol] instanceof Pawn && ((Pawn) getBoard()[i][targetCol]).getNewPiece() instanceof Rook))
          && getBoard()[i][targetCol].isWhite
      ){
        AbsRowOfWhiteRookUpFromTargetPiece = i;
        if(getBoard()[targetRow][targetCol].isWhite) MovableRowOfWhiteRookDown= i-1;
        else MovableRowOfWhiteRookDown = i;
        break;
      }else if(getBoard()[i][targetCol]!=null && !(getBoard()[i][targetCol] instanceof Rook)){
        break;
      }
    }
    if(AbsRowOfWhiteRookUpFromTargetPiece != -1) {
      if(getBoard()[AbsRowOfWhiteRookUpFromTargetPiece][targetCol] instanceof Rook){
        ((Rook)getBoard()[AbsRowOfWhiteRookUpFromTargetPiece][targetCol])
            .setDown(MovableRowOfWhiteRookDown-targetRow);
      }else if(
          getBoard()[AbsRowOfWhiteRookUpFromTargetPiece][targetCol] instanceof Pawn
          && ((Pawn) getBoard()[AbsRowOfWhiteRookUpFromTargetPiece][targetCol]).getNewPiece() instanceof Rook
      ) {
        ((Rook)((Pawn) getBoard()[AbsRowOfWhiteRookUpFromTargetPiece][targetCol])
            .getNewPiece())
            .setDown(MovableRowOfWhiteRookDown-targetRow);
      }
    }


    // set movable area for a Rook when other piece leaves from available area of the Rook

    // when moving piece leaves from upper area of white Rook or lower area of black Rook
    int RowOfRookWhiteDownOrBlackUpFromCurrentCell= -1;
    for(int i=currRow-1; i>=0; i--) {
      if (getBoard()[i][currCol]!=null
          &&(getBoard()[i][currCol] instanceof Rook
            || (getBoard()[i][currCol] instanceof Pawn && ((Pawn) getBoard()[i][currCol]).getNewPiece() instanceof Rook))
      ) {
        RowOfRookWhiteDownOrBlackUpFromCurrentCell = i;
        break;
      }else if(getBoard()[i][currCol]!=null && !(getBoard()[i][currCol] instanceof Rook)){
        break;
      }
    }
    if(RowOfRookWhiteDownOrBlackUpFromCurrentCell != -1) {
      if(targetCol!=currCol){
        int updatedRowOfWhiteUpOrBlackDownForRook = -1;
        for(int i=RowOfRookWhiteDownOrBlackUpFromCurrentCell+1; i<ROW_RANGE; i++){
          if(getBoard()[i][currCol]!=null){
            if(getBoard()[RowOfRookWhiteDownOrBlackUpFromCurrentCell][currCol].isWhite!=getBoard()[i][currCol].isWhite){
              updatedRowOfWhiteUpOrBlackDownForRook = i;
            }
            else {
              updatedRowOfWhiteUpOrBlackDownForRook = i-1;
            }
          }
        }
        if(updatedRowOfWhiteUpOrBlackDownForRook==-1){
          updatedRowOfWhiteUpOrBlackDownForRook = ROW_RANGE-1;
        }

        if(getBoard()[RowOfRookWhiteDownOrBlackUpFromCurrentCell][currCol].isWhite){
          if(getBoard()[RowOfRookWhiteDownOrBlackUpFromCurrentCell][currCol] instanceof Rook){
            ((Rook)getBoard()[RowOfRookWhiteDownOrBlackUpFromCurrentCell][currCol])
                .setUp(updatedRowOfWhiteUpOrBlackDownForRook-RowOfRookWhiteDownOrBlackUpFromCurrentCell);
          }else{
            ((Rook)((Pawn) getBoard()[RowOfRookWhiteDownOrBlackUpFromCurrentCell][currCol])
                .getNewPiece())
                .setUp(updatedRowOfWhiteUpOrBlackDownForRook-RowOfRookWhiteDownOrBlackUpFromCurrentCell);
          }
        }else{
          if(getBoard()[RowOfRookWhiteDownOrBlackUpFromCurrentCell][currCol] instanceof Rook){
            ((Rook)getBoard()[RowOfRookWhiteDownOrBlackUpFromCurrentCell][currCol])
                .setDown(updatedRowOfWhiteUpOrBlackDownForRook-RowOfRookWhiteDownOrBlackUpFromCurrentCell);
          }else{
            ((Rook)((Pawn) getBoard()[RowOfRookWhiteDownOrBlackUpFromCurrentCell][currCol])
                .getNewPiece())
                .setDown(updatedRowOfWhiteUpOrBlackDownForRook-RowOfRookWhiteDownOrBlackUpFromCurrentCell);
          }
        }
      }
    }

    // when moving piece leaves from lower area of white Rook or upper area of black Rook
    int RowOfRookWhiteUpOrBlackDownFromCurrentCell= -1;
    for(int i=currRow+1; i<ROW_RANGE; i++) {
      if (getBoard()[i][currCol]!=null
          &&(getBoard()[i][currCol] instanceof Rook
          || (getBoard()[i][currCol] instanceof Pawn && ((Pawn) getBoard()[i][currCol]).getNewPiece() instanceof Rook))
      ) {
        RowOfRookWhiteUpOrBlackDownFromCurrentCell = i;
        break;
      }else if(getBoard()[i][currCol]!=null && !(getBoard()[i][currCol] instanceof Rook)){
        break;
      }
    }
    if(RowOfRookWhiteUpOrBlackDownFromCurrentCell != -1) {

      if(targetCol!=currCol){
        int updatedRowOfWhiteDownOrBlackUpForRook = -1;
        for(int i=RowOfRookWhiteUpOrBlackDownFromCurrentCell-1; i>=0; i--){
          if(getBoard()[i][currCol]!=null){
            if(getBoard()[RowOfRookWhiteUpOrBlackDownFromCurrentCell][currCol].isWhite!=getBoard()[i][currCol].isWhite){
              updatedRowOfWhiteDownOrBlackUpForRook = i-1;
            }
            else updatedRowOfWhiteDownOrBlackUpForRook = i;
          }
        }
        if(updatedRowOfWhiteDownOrBlackUpForRook==-1) {
          updatedRowOfWhiteDownOrBlackUpForRook = 0;
        }

        if(getBoard()[RowOfRookWhiteUpOrBlackDownFromCurrentCell][currCol].isWhite){
          if(getBoard()[RowOfRookWhiteUpOrBlackDownFromCurrentCell][currCol] instanceof Rook){
            ((Rook)getBoard()[RowOfRookWhiteUpOrBlackDownFromCurrentCell][currCol])
                .setDown(RowOfRookWhiteUpOrBlackDownFromCurrentCell-updatedRowOfWhiteDownOrBlackUpForRook);
          }else{
            ((Rook)((Pawn) getBoard()[RowOfRookWhiteUpOrBlackDownFromCurrentCell][currCol])
                .getNewPiece())
                .setDown(RowOfRookWhiteUpOrBlackDownFromCurrentCell-updatedRowOfWhiteDownOrBlackUpForRook);
          }
        }else{
          if(getBoard()[RowOfRookWhiteUpOrBlackDownFromCurrentCell][currCol] instanceof Rook){
            ((Rook)getBoard()[RowOfRookWhiteUpOrBlackDownFromCurrentCell][currCol])
                .setUp(RowOfRookWhiteUpOrBlackDownFromCurrentCell-updatedRowOfWhiteDownOrBlackUpForRook);
          }else{
            ((Rook)((Pawn) getBoard()[RowOfRookWhiteUpOrBlackDownFromCurrentCell][currCol])
                .getNewPiece())
                .setDown(RowOfRookWhiteUpOrBlackDownFromCurrentCell-updatedRowOfWhiteDownOrBlackUpForRook);
          }
        }
      }
    }

    // when moving piece leaves from right-hand area of white Rook or left-hand area of black Rook
    int ColOfRookWhiteLeftOrBlackRightFromCurrentCell= -1;
    for(int i=currCol-1; i>=0; i--) {
      if (getBoard()[currRow][i]!=null
          && getBoard()[currRow][i] instanceof Rook
          || (getBoard()[currRow][i] instanceof Pawn && ((Pawn) getBoard()[currRow][i]).getNewPiece() instanceof Rook)
      ) {
        ColOfRookWhiteLeftOrBlackRightFromCurrentCell = i;
        break;
      }else if(getBoard()[currRow][i]!=null && !(getBoard()[currRow][i] instanceof Rook)){
        break;
      }
    }
    if(ColOfRookWhiteLeftOrBlackRightFromCurrentCell != -1) {

      if(targetRow!=currRow){
        int updatedColOfWhiteRightOrBlackLeftForRook = -1;
        for(int i=ColOfRookWhiteLeftOrBlackRightFromCurrentCell+1; i<COL_RANGE; i++){
          if(getBoard()[currRow][i]!=null){
            if(getBoard()[currRow][ColOfRookWhiteLeftOrBlackRightFromCurrentCell].isWhite != getBoard()[currRow][i].isWhite) {
              updatedColOfWhiteRightOrBlackLeftForRook = i;
            }
            else updatedColOfWhiteRightOrBlackLeftForRook = i-1;
          }
        }
        if(updatedColOfWhiteRightOrBlackLeftForRook==-1) updatedColOfWhiteRightOrBlackLeftForRook = COL_RANGE-1;

        if(getBoard()[currRow][ColOfRookWhiteLeftOrBlackRightFromCurrentCell].isWhite){
          if(getBoard()[currRow][ColOfRookWhiteLeftOrBlackRightFromCurrentCell] instanceof Rook){
            ((Rook)getBoard()[currRow][ColOfRookWhiteLeftOrBlackRightFromCurrentCell])
                .setRight(updatedColOfWhiteRightOrBlackLeftForRook-ColOfRookWhiteLeftOrBlackRightFromCurrentCell);
          }else{
            ((Rook)((Pawn) getBoard()[currRow][ColOfRookWhiteLeftOrBlackRightFromCurrentCell])
                .getNewPiece())
                .setRight(updatedColOfWhiteRightOrBlackLeftForRook-ColOfRookWhiteLeftOrBlackRightFromCurrentCell);
          }
        }else{
          if(getBoard()[currRow][ColOfRookWhiteLeftOrBlackRightFromCurrentCell] instanceof Rook){
            ((Rook)getBoard()[currRow][ColOfRookWhiteLeftOrBlackRightFromCurrentCell])
                .setLeft(updatedColOfWhiteRightOrBlackLeftForRook-ColOfRookWhiteLeftOrBlackRightFromCurrentCell);
          }else{
            ((Rook)((Pawn) getBoard()[currRow][ColOfRookWhiteLeftOrBlackRightFromCurrentCell])
                .getNewPiece())
                .setLeft(updatedColOfWhiteRightOrBlackLeftForRook-ColOfRookWhiteLeftOrBlackRightFromCurrentCell);
          }
        }
      }
    }

    // when moving piece leaves from left area of white Rook or right area of black Rook
    int ColOfRookWhiteRightOrBlackLeftFromCurrentCell= -1;
    for(int i=currCol+1; i<COL_RANGE; i++) {
      if (getBoard()[currRow][i]!=null
          && getBoard()[currRow][i] instanceof Rook
          || (getBoard()[currRow][i] instanceof Pawn && ((Pawn) getBoard()[currRow][i]).getNewPiece() instanceof Rook)
      ) {
        ColOfRookWhiteRightOrBlackLeftFromCurrentCell = i;
        break;
      }else if(getBoard()[currRow][i]!=null && !(getBoard()[currRow][i] instanceof Rook)){
        break;
      }
    }
    if(ColOfRookWhiteRightOrBlackLeftFromCurrentCell != -1) {
      if(targetRow!=currRow){
        int updatedColOfWhiteLeftOrBlackRightForRook = -1;
        for(int i=ColOfRookWhiteRightOrBlackLeftFromCurrentCell-1; i>=0; i--){
          if(getBoard()[currRow][i]!=null){
            if(getBoard()[currRow][ColOfRookWhiteRightOrBlackLeftFromCurrentCell].isWhite != getBoard()[currRow][i].isWhite) {
              updatedColOfWhiteLeftOrBlackRightForRook = i-1;
            }
            else updatedColOfWhiteLeftOrBlackRightForRook = i;
          }
        }

        if(updatedColOfWhiteLeftOrBlackRightForRook==-1) updatedColOfWhiteLeftOrBlackRightForRook = 0;

        if(getBoard()[currRow][ColOfRookWhiteRightOrBlackLeftFromCurrentCell].isWhite){
          if(getBoard()[currRow][ColOfRookWhiteRightOrBlackLeftFromCurrentCell] instanceof Rook){
            ((Rook)getBoard()[currRow][ColOfRookWhiteRightOrBlackLeftFromCurrentCell])
                .setLeft(ColOfRookWhiteRightOrBlackLeftFromCurrentCell-updatedColOfWhiteLeftOrBlackRightForRook);
          }else{
            ((Rook)((Pawn) getBoard()[currRow][ColOfRookWhiteRightOrBlackLeftFromCurrentCell])
                .getNewPiece())
                .setLeft(ColOfRookWhiteRightOrBlackLeftFromCurrentCell-updatedColOfWhiteLeftOrBlackRightForRook);
          }

        }else{
          if(getBoard()[currRow][ColOfRookWhiteRightOrBlackLeftFromCurrentCell] instanceof Rook){
            ((Rook)getBoard()[currRow][ColOfRookWhiteRightOrBlackLeftFromCurrentCell])
                .setRight(ColOfRookWhiteRightOrBlackLeftFromCurrentCell-updatedColOfWhiteLeftOrBlackRightForRook);
          }else {
            ((Rook)((Pawn) getBoard()[currRow][ColOfRookWhiteRightOrBlackLeftFromCurrentCell])
                .getNewPiece())
                .setRight(ColOfRookWhiteRightOrBlackLeftFromCurrentCell-updatedColOfWhiteLeftOrBlackRightForRook);
          }
        }
      }
    }
  }

  public void promotePawn(int targetRow, int targetCol){
    if(targetRow==ROW_RANGE-1
        && getBoard()[targetRow][targetCol] instanceof Pawn
        && !((Pawn) getBoard()[targetRow][targetCol]).isPromoted()
    ){
      int option = -1;
      Scanner in = new Scanner(System.in);
      while(option != 2){
        System.out.println("Promotion");
        System.out.println("---------");
//        System.out.println("1: Queen");
        System.out.println("2: Rook");
//        System.out.println("3: Bishop");
//        System.out.println("4: Knight");
        option = in.nextInt();
        if(option != -2){
          System.out.println("Invalid number, select 2");
        }
      }

      Piece promotedPiece = null;
      if(option==1){
        if(getBoard()[targetRow][targetCol].isWhite) promotedPiece = new Queen("♕", 9, true, new Position(targetRow, targetCol));
        else promotedPiece = new Queen("♛", 9, false, new Position(targetRow, targetCol));
      }else if(option == 2){
        if(getBoard()[targetRow][targetCol].isWhite) promotedPiece = new Rook("♖", 5, true, new Position(targetRow, targetCol), 0 , 0 , 0, 0);
        else promotedPiece = new Rook("♜", 5, true, new Position(targetRow, targetCol), 0 , 0 , 0, 0);
      }else if(option == 3){
        if(getBoard()[targetRow][targetCol].isWhite) promotedPiece = new Bishop("♗", 3, true, new Position(targetRow, targetCol));
        else promotedPiece = new Bishop("♝", 3, false, new Position(targetRow, targetCol));
      }else if(option == 4){
        if(getBoard()[targetRow][targetCol].isWhite) promotedPiece = new Knight("♘", 2, true, new Position(targetRow, targetCol));
        else promotedPiece = new Queen("♞", 2, false, new Position(targetRow, targetCol));
      }
      ((Pawn) getBoard()[targetRow][targetCol]).promote(promotedPiece);
      System.out.println();
    }
  }

  public void initialize () {
    board = new Piece[ROW_RANGE][COL_RANGE];
    board[0][0] = new Rook("♖", 5, true, new Position(0, 0), 0, 6, 0, 0);
//    board[0][1] = new Knight("♘", 2, true, new Position(0, 1));
//    board[0][2] = new Bishop("♗", 3, true, new Position(0, 2));
//    board[0][3] = new Queen("♕", 9, true, new Position(0, 3));
//    board[0][4] = new King("♔", 1000, true, new Position(0, 4));
//    board[0][5] = new Bishop("♗", 3, true, new Position(0, 5));
//    board[0][6] = new Knight("♘", 2, true, new Position(0, 6));
    board[0][7] = new Rook("♖", 5, true, new Position(0, 7), 6, 0, 0, 0);
    board[1][0] = new Pawn("♙", 1, true, new Position(1, 0), true, false, false, false,false, false, null);
    board[1][1] = new Pawn("♙", 1, true, new Position(1, 1), true, false, false, false,false, false, null);
    board[1][2] = new Pawn("♙", 1, true, new Position(1, 2), true, false, false, false,false, false, null);
    board[1][3] = new Pawn("♙", 1, true, new Position(1, 3), true, false, false, false,false, false, null);
    board[1][4] = new Pawn("♙", 1, true, new Position(1, 4), true, false, false, false,false, false, null);
//    board[1][5] = new Pawn("♙", 1, true, new Position(1, 5), true, false, false, false,false, false, null);
    board[1][5] = new Pawn("♟", 1, false, new Position(1, 5), false, false, false, false,false, false, null);
    board[1][6] = new Pawn("♙", 1, true, new Position(1, 6), true, false, false, false,false, false, null);
    board[1][7] = new Pawn("♙", 1, true, new Position(1, 7), true, false, false, false,false, false, null);

    board[6][0] = new Pawn("♟", 1, false, new Position(6, 0), true, false, false, false,false, false, null);
    board[6][1] = new Pawn("♟", 1, false, new Position(6, 1), true, false, false, false,false, false, null);
    board[6][2] = new Pawn("♟", 1, false, new Position(6, 2), true, false, false, false,false, false, null);
//    board[6][3] = new Pawn("♟", 1, false, new Position(6, 3), true, false, false, false,false, false, null);
    board[6][3] = new Pawn("♙", 1, true, new Position(6, 3), false, false, false, false,false, false, null);
    board[6][4] = new Pawn("♟", 1, false, new Position(6, 4), true, false, false, false,false, false, null);
    board[6][5] = new Pawn("♟", 1, false, new Position(6, 5), true, false, false, false,false, false, null);
    board[6][6] = new Pawn("♟", 1, false, new Position(6, 6), true, false, false, false,false, false, null);
    board[6][7] = new Pawn("♟", 1, false, new Position(6, 7), true, false, false, false,false, false, null);
    board[7][0] = new Rook("♜", 5, false, new Position(7, 0), 6 ,0 ,0 , 0);
//    board[7][1] = new Knight("♞", 2, false, new Position(7, 1));
//    board[7][2] = new Bishop("♝", 3, false, new Position(7, 2));
//    board[7][3] = new Queen("♛", 9, false, new Position(7, 3));
//    board[7][4] = new King("♚", 1000, false, new Position(7, 4));
//    board[7][5] = new Bishop("♝", 3, false, new Position(7, 5));
//    board[7][6] = new Knight("♞", 2, false, new Position(7, 6));
    board[7][7] = new Rook("♜", 5, false, new Position(7, 7), 0, 6, 0, 0);

    setBoard(board);
  }

  public int convertColFromAlphabetToNum (String colAlphabet){
    int colNumber = -1;
    if (colAlphabet.equals("a")) {
      colNumber = 0;
    } else if (colAlphabet.equals("b")) {
      colNumber = 1;
    } else if (colAlphabet.equals("c")) {
      colNumber = 2;
    } else if (colAlphabet.equals("d")) {
      colNumber = 3;
    } else if (colAlphabet.equals("e")) {
      colNumber = 4;
    } else if (colAlphabet.equals("f")) {
      colNumber = 5;
    } else if (colAlphabet.equals("g")) {
      colNumber = 6;
    } else if (colAlphabet.equals("h")) {
      colNumber = 7;
    }
    return colNumber;
  }

  public String convertColFromNumToAlphabet ( int colNumber){
    String colString = null;
    if (colNumber == 0) {
      colString = "a";
    } else if (colNumber == 1) {
      colString = "b";
    } else if (colNumber == 2) {
      colString = "c";
    } else if (colNumber == 3) {
      colString = "d";
    } else if (colNumber == 4) {
      colString = "e";
    } else if (colNumber == 5) {
      colString = "f";
    } else if (colNumber == 6) {
      colString = "g";
    } else if (colNumber == 7) {
      colString = "h";
    }

    return colString;
  }

}
