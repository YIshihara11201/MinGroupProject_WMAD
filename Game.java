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
    System.out.println("* type 'moves' to list all possible moves");
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
          }
          System.out.print(board[i][j].getSymbol() + "  ");
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
//            && ((getBoard()[j][i] == null || targetPiece.isWhite != getBoard()[j][i].isWhite))) {
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

  // 6.move functionality
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
    Piece targetCell = getBoard()[targetRow][targetCol];

    if (currentCell == null) {
      throw new Exception(
          "Invalid Input, no piece in cell[" + move.substring(0, 2) + "]. Try again");
    }

    if ((getTurn()%2==1 && !currentCell.isWhite)||(getTurn()%2==0 && currentCell.isWhite)) {
      throw new Exception(
          "Invalid Input, you can't move opponent piece @[" + move.substring(0, 2) + "] . Try again");
    }

    if ((targetCell != null && currentCell.isWhite == targetCell.isWhite)&&(targetCell instanceof Pawn && !targetCell.getPosition().isEp())) {
      throw new Exception(
          "Invalid Input, cell[" + move.substring(2, 4) + "] has allies piece. Try again");
    }

    if (!currentCell.isValidMove(new Position(targetRow, targetCol))) {
      throw new Exception(
          "Invalid Input, piece[" + move.substring(0, 2) + "] can't move to [" + move.substring(2,
              4) + "]. Try again");
    }

    // 実際に動かす処理
    if (currentCell.isValidMove(new Position(targetRow, targetCol))) {
      System.out.println("OK");
      System.out.println();

      // epフラグがあがっているセルの１行先にあるPawnを動かすときはセルの仮置きpieceを削除する
      if (currRow==3
          && getBoard()[currRow][currCol].isWhite
          && getBoard()[currRow][currCol] instanceof Pawn
          && getBoard()[currRow-1][currCol].getPosition().isEp()) {
        getBoard()[currRow-1][currCol]=null;
      }
      if (currRow==4
          && !getBoard()[currRow][currCol].isWhite
          && getBoard()[currRow][currCol] instanceof Pawn
          && getBoard()[currRow+1][currCol].getPosition().isEp()) {
        getBoard()[currRow+1][currCol]=null;
      }

      // アンパッサンの処理 動く先が相手コマの残像の時、本体も同時に消す
      if(targetRow==5
          && getBoard()[currRow][currCol].isWhite
          && !getBoard()[targetRow][targetCol].isWhite
          && getBoard()[targetRow][targetCol] instanceof Pawn
          && getBoard()[targetRow][targetCol].getPosition().isEp()) {
        getBoard()[targetRow-1][targetCol] = null;
      }
      if(targetRow==2
          && !getBoard()[currRow][currCol].isWhite
          && getBoard()[targetRow][targetCol].isWhite
          && getBoard()[targetRow][targetCol] instanceof Pawn
          && getBoard()[targetRow][targetCol].getPosition().isEp()){
        getBoard()[targetRow+1][targetCol] = null;
      }

      // アンパッサンの処理 本体が取られるとき残像も同時に消す
      if(targetRow==4
          && getBoard()[currRow][currCol].isWhite
          && getBoard()[targetRow+1][targetCol] instanceof Pawn
          && getBoard()[targetRow+1][targetCol].getPosition().isEp()) {
        getBoard()[targetRow+1][targetCol] = null;
      }
      if(targetRow==3
          && !getBoard()[currRow][currCol].isWhite
          && getBoard()[targetRow-1][targetCol] instanceof Pawn
          && getBoard()[targetRow-1][targetCol].getPosition().isEp()){
        getBoard()[targetRow-1][targetCol] = null;
      }

      // コマのpositionプロパティを移動先のセルに設定する
      getBoard()[currRow][currCol].setPosition(new Position(targetRow, targetCol));

      // 目的のセルにコマを動かす
      getBoard()[targetRow][targetCol] = getBoard()[currRow][currCol];
      getBoard()[currRow][currCol] = null;
      // 移動させた後のコマ
      Piece updatedCell = getBoard()[targetRow][targetCol];

      // Pawn が初期位置から2マス動いた時は１行手前に残像を作る
      if(updatedCell.isWhite && updatedCell instanceof Pawn && ((Pawn) updatedCell).isFirstMove() && targetRow==3){
        getBoard()[targetRow-1][targetCol] = new Pawn("X", 1, true, new Position(targetRow-1, targetCol), true, false, false, false, false, null);
        getBoard()[targetRow-1][targetCol].getPosition().setEp(true);
      }else if(!updatedCell.isWhite && updatedCell instanceof Pawn && ((Pawn) updatedCell).isFirstMove() && targetRow==4){
        getBoard()[targetRow+1][targetCol] = new Pawn("X", 1, false, new Position(targetRow+1, targetCol), true, false, false, false, false, null);
        getBoard()[targetRow+1][targetCol].getPosition().setEp(true);
      }

      // 初期位置の Pawn を動かす場合は firstMoveフラグをおろす
      if (updatedCell instanceof Pawn && ((Pawn) updatedCell).isFirstMove()) {
        ((Pawn) updatedCell).setFirstMove(false);
      }

//       Pawnを動かした後、そのPawnが次に動ける範囲を設定する
      if (updatedCell instanceof Pawn) {
        //白のコマについて
        if (updatedCell.isWhite && targetRow+1<ROW_RANGE) {
          // １マス前にコマがあるかどうかチェック
          ((Pawn) updatedCell).setFront(getBoard()[targetRow+1][targetCol]!=null && !getBoard()[targetRow+1][targetCol].isWhite);
          // 斜め左前にコマがあるかどうかチェック
          ((Pawn) updatedCell).setLeftFront(targetCol-1>=0 && getBoard()[targetRow+1][targetCol-1]!=null && !getBoard()[targetRow+1][targetCol-1].isWhite);
          // 斜め右前にコマがあるかどうかチェック
          ((Pawn) updatedCell).setRightFront(targetCol+1<COL_RANGE && getBoard()[targetRow+1][targetCol+1] != null && !getBoard()[targetRow+1][targetCol+1].isWhite);
        }
        // 黒のコマについて
        if (!updatedCell.isWhite && targetRow-1>=0) {
          ((Pawn) updatedCell).setFront(getBoard()[targetRow-1][targetCol] != null && getBoard()[targetRow-1][targetCol].isWhite);
          ((Pawn) updatedCell).setLeftFront(targetCol+1<COL_RANGE && getBoard()[targetRow-1][targetCol+1] != null  && getBoard()[targetRow-1][targetCol+1].isWhite);
          ((Pawn) updatedCell).setRightFront(targetCol-1>=0 && getBoard()[targetRow-1][targetCol-1]!=null  && getBoard()[targetRow-1][targetCol-1].isWhite);
        }
      }

      // Rookを動かした後、そのRookが次に動ける範囲を設定する
      if (updatedCell instanceof Rook){
        int right = 0;
        int left = 0;
        int up = 0;
        int down = 0;
        //白のコマについて
        if (updatedCell.isWhite) {
          // Rookの右何マス先にコマがあるかチェック
          for(int i=targetCol; i<COL_RANGE-1; i++){
            if((getBoard()[targetRow][i+1]!=null
                && (getBoard()[targetRow][i+1] instanceof Pawn
                && !(getBoard()[targetRow][i+1].getPosition().isEp())))
              ||getBoard()[targetRow][i+1]!=null
            ){
              right = i-targetCol;
              if(getBoard()[targetRow][i+1].isWhite != updatedCell.isWhite) right += 1;
              break;
            } else if(i+1==COL_RANGE-1 && getBoard()[targetRow][COL_RANGE-1]==null) right = COL_RANGE-1-targetCol;
          }

          // Rookの左何マス先にコマがあるかチェック
          for(int i=targetCol; i>0; i--){
            if((getBoard()[targetRow][i-1]!=null
                && (getBoard()[targetRow][i-1] instanceof Pawn
                && !getBoard()[targetRow][i-1].getPosition().isEp()))
              ||getBoard()[targetRow][i-1]!=null
            ){
              left = targetCol-i;
              if(getBoard()[targetRow][i-1].isWhite != updatedCell.isWhite) left += 1;
              break;
            }else if(i-1==0 && getBoard()[targetRow][0]==null) left = targetCol;
          }

          // Rookの何マス上にコマがあるかチェック
          for(int i=targetRow; i<ROW_RANGE-1; i++){
            if((getBoard()[i+1][targetCol]!=null
                && (getBoard()[i+1][targetCol] instanceof Pawn
                && !getBoard()[i+1][targetCol].getPosition().isEp()))
              ||getBoard()[i+1][targetCol]!=null
            ){
              up = i-targetRow;
              if(getBoard()[i+1][targetCol].isWhite != updatedCell.isWhite) up += 1;
              break;
            }else if(i+1==ROW_RANGE-1 && getBoard()[ROW_RANGE-1][targetCol] == null) up = ROW_RANGE-1-targetRow;
          }

          // Rookの何マス下にコマがあるかチェック
          for(int i=targetRow; i>0; i--){
            if((getBoard()[i-1][targetCol]!=null
                && (getBoard()[i-1][targetCol] instanceof Pawn
                && !getBoard()[i-1][targetCol].getPosition().isEp()))
              ||getBoard()[i-1][targetCol]!=null
            ){
              down = targetRow-i;
              if(getBoard()[i-1][targetCol].isWhite != updatedCell.isWhite) down += 1;
              break;
            } else if(i-1==0 && getBoard()[0][targetCol]==null) down = targetRow;
          }

        // 黒のコマについて
        }else{
          // Rookの右何マス先にコマがあるかチェック
          for(int i=targetCol; i>0; i--){
            if((getBoard()[targetRow][i-1]!=null
                && (getBoard()[targetRow][i-1] instanceof Pawn
                && !getBoard()[targetRow][i-1].getPosition().isEp()))
              ||getBoard()[targetRow][i-1]!=null
            ){
              right = targetCol-i;
              if(getBoard()[targetRow][i-1].isWhite != updatedCell.isWhite) right += 1;
              break;
            }else if(i-1==0 &&  getBoard()[targetRow][0]==null) right = targetCol;
          }

          // Rookの左何マス先にコマがあるかチェック
          for(int i=targetCol; i<COL_RANGE-1; i++){
            if((getBoard()[targetRow][i+1]!=null
                && (getBoard()[targetRow][i+1] instanceof Pawn
                && !getBoard()[targetRow][i+1].getPosition().isEp()))
              ||getBoard()[targetRow][i+1]!=null
            ){
              left = i-targetCol;
              if(getBoard()[targetRow][i+1].isWhite != updatedCell.isWhite) left += 1;
              break;
            }else if(i+1==COL_RANGE-1 && getBoard()[targetRow][COL_RANGE-1]==null) left = COL_RANGE-1-targetCol;
          }

          // Rookの何マス上にコマがあるかチェック
          for(int i=targetRow; i>0; i--){
            if((getBoard()[i-1][targetCol]!=null
                && (getBoard()[i-1][targetCol] instanceof Pawn
                && !getBoard()[i-1][targetCol].getPosition().isEp()))
              ||getBoard()[i-1][targetCol]!=null
            ){
              up = targetRow-i;
              if(getBoard()[i-1][targetCol].isWhite != updatedCell.isWhite) up += 1;
              break;
            } else if(i-1==0 && getBoard()[0][targetCol]==null) up = targetRow;
          }

          // Rookの何マス下にコマがあるかチェック
          for(int i=targetRow; i<ROW_RANGE-1; i++){
            if((getBoard()[i+1][targetCol]!=null
                && (getBoard()[i+1][targetCol] instanceof Pawn
                && !getBoard()[i+1][targetCol].getPosition().isEp()))
              ||getBoard()[i+1][targetCol]!=null
            ){
              down = i-targetRow;
              if(getBoard()[i+1][targetCol].isWhite != updatedCell.isWhite) down += 1;
              break;
            }else if(i+1==ROW_RANGE-1 && getBoard()[ROW_RANGE-1][targetCol]==null) down = ROW_RANGE-1-targetRow;
          }
        }
        ((Rook)updatedCell).setRight(right);
        ((Rook)updatedCell).setLeft(left);
        ((Rook)updatedCell).setUp(up);
        ((Rook)updatedCell).setDown(down);
      }

      // コマを動かした先のセルが Pawn の移動範囲に含まれている場合その Pawn の移動可能範囲を設定する
      if (updatedCell.isWhite) {
        // 白のコマを動かした先のセルが、黒のPawnからみて左斜め前にある時
        if (targetRow+1<ROW_RANGE
            && targetCol-1 >= 0
            && getBoard()[targetRow+1][targetCol-1]!=null
            && getBoard()[targetRow+1][targetCol-1] instanceof Pawn
            && !getBoard()[targetRow+1][targetCol-1].isWhite) {
          ((Pawn) getBoard()[targetRow+1][targetCol-1]).setLeftFront(true);
        }
        // 白のコマを動かした先のセルが、黒のPawnからみて正面にある時
        if (targetRow+1<ROW_RANGE
            && getBoard()[targetRow+1][targetCol]!=null
            && getBoard()[targetRow+1][targetCol] instanceof Pawn
            && !getBoard()[targetRow+1][targetCol].isWhite) {
          ((Pawn) getBoard()[targetRow+1][targetCol]).setFront(true);
        }
        // 白のコマを動かした先のセルが、黒のPawnからみて右斜め前にある時
        if (targetRow+1<ROW_RANGE
            && targetCol+1<COL_RANGE
            && getBoard()[targetRow+1][targetCol+1]!=null
            && getBoard()[targetRow+1][targetCol+1] instanceof Pawn
            && !getBoard()[targetRow+1][targetCol+1].isWhite) {
          ((Pawn) getBoard()[targetRow+1][targetCol+1]).setRightFront(true);
        }

        // 白のコマを動かした先のセルが、白のPawnからみて左斜め前にある時
        if (targetRow-1>=0 && targetCol+1<COL_RANGE && getBoard()[targetRow-1][targetCol+1]!=null && getBoard()[targetRow-1][targetCol+1] instanceof Pawn && getBoard()[targetRow-1][targetCol+1].isWhite) {
          ((Pawn) getBoard()[targetRow-1][targetCol+1]).setLeftFront(true);
        }
        // 白のコマを動かした先のセルが、白のPawnからみて正面にある時
        if (targetRow-1>=0 && getBoard()[targetRow-1][targetCol]!=null && getBoard()[targetRow-1][targetCol] instanceof Pawn && getBoard()[targetRow-1][targetCol].isWhite) {
          ((Pawn) getBoard()[targetRow-1][targetCol]).setFront(true);
        }
        // 白のコマを動かした先のセルが、白のPawnからみて右斜め前にある時
        if (targetRow-1>=0 && targetCol-1>=0 && getBoard()[targetRow-1][targetCol-1]!=null && getBoard()[targetRow-1][targetCol-1] instanceof Pawn && getBoard()[targetRow-1][targetCol-1].isWhite) {
          ((Pawn) getBoard()[targetRow-1][targetCol-1]).setRightFront(true);
        }
      } else {
        // 黒のコマを動かした先のセルが、白のPawnからみて右斜め前にある時
        if (targetRow-1>=0
            && targetCol-1>=0
            && getBoard()[targetRow-1][targetCol-1]!=null
            && getBoard()[targetRow-1][targetCol-1] instanceof Pawn
            && getBoard()[targetRow-1][targetCol-1].isWhite) {
          ((Pawn) getBoard()[targetRow-1][targetCol-1]).setRightFront(true);
        }
        // 黒のコマを動かした先のセルが、白のPawnからみて正面にある時
        if (targetRow-1>=0
            && getBoard()[targetRow-1][targetCol]!=null
            && getBoard()[targetRow-1][targetCol] instanceof Pawn
            && getBoard()[targetRow-1][targetCol].isWhite) {
          ((Pawn) getBoard()[targetRow-1][targetCol]).setFront(true);
        }
        // 黒のコマを動かした先のセルが、白のPawnからみて左斜め前にある時
        if (targetRow-1>=0
            && targetCol+1<COL_RANGE
            && getBoard()[targetRow-1][targetCol+1]!=null
            && getBoard()[targetRow-1][targetCol+1] instanceof Pawn
            && getBoard()[targetRow-1][targetCol+1].isWhite) {
          ((Pawn) getBoard()[targetRow-1][targetCol+1]).setLeftFront(true);
        }

        // 黒のコマを動かした先のセルが、黒のPawnからみて右斜め前にある時
        if (targetRow+1<ROW_RANGE
            && targetCol+1<COL_RANGE
            && getBoard()[targetRow+1][targetCol+1]!=null
            && getBoard()[targetRow+1][targetCol+1] instanceof Pawn
            && !getBoard()[targetRow+1][targetCol+1].isWhite) {
          ((Pawn) getBoard()[targetRow+1][targetCol+1]).setRightFront(true);
        }

        // 黒のコマを動かした先のセルが、黒のPawnからみて正面にある時
        if (targetRow+1<ROW_RANGE
            && getBoard()[targetRow+1][targetCol]!=null
            && getBoard()[targetRow+1][targetCol] instanceof Pawn
            && !getBoard()[targetRow+1][targetCol].isWhite) {
          ((Pawn) getBoard()[targetRow+1][targetCol]).setFront(true);
        }

        // 黒のコマを動かした先のセルが、黒のPawnからみて左斜め前にある時
        if (targetRow+1<ROW_RANGE
            && targetCol-1>=0
            && getBoard()[targetRow+1][targetCol-1]!=null
            && getBoard()[targetRow+1][targetCol-1] instanceof Pawn
            && !getBoard()[targetRow+1][targetCol-1].isWhite) {
          ((Pawn) getBoard()[targetRow+1][targetCol-1]).setLeftFront(true);
        }
      }

      // コマを動かした先のセルが Rook の移動範囲に含まれている場合その Rook の移動可能範囲を設定する
      if (updatedCell.isWhite) {
        // 白のコマを動かした先のセルが、黒の Rook の左側経路に含まれる場合
        int PositionOfBlackRookLeftFromWhitePiece= -1;
        for(int i=targetCol-1; i>=0; i--) {
          if (getBoard()[targetRow][i]!=null
              && getBoard()[targetRow][i] instanceof Rook
              && !getBoard()[targetRow][i].isWhite) {
            PositionOfBlackRookLeftFromWhitePiece = i;
            break;
          }else if(getBoard()[targetRow][i]!=null
            && !(getBoard()[targetRow][i] instanceof Rook)
            && !(getBoard()[targetRow][i] instanceof Pawn && getBoard()[targetRow][i].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfBlackRookLeftFromWhitePiece != -1) {
          ((Rook)getBoard()[targetRow][PositionOfBlackRookLeftFromWhitePiece])
              .setLeft(targetCol-PositionOfBlackRookLeftFromWhitePiece);
        }

        // 白のコマを動かした先のセルが、黒の Rook の右側経路に含まれる場合
        int PositionOfBlackRookRightFromWhitePiece= -1;
        for(int i=targetCol+1; i<COL_RANGE; i++){
          if(getBoard()[targetRow][i]!=null
              && getBoard()[targetRow][i] instanceof Rook
              && !getBoard()[targetRow][i].isWhite){
            PositionOfBlackRookRightFromWhitePiece = i;
            break;
          }else if(getBoard()[targetRow][i]!=null
            && !(getBoard()[targetRow][i] instanceof Rook)
            && !(getBoard()[targetRow][i] instanceof Pawn && getBoard()[targetRow][i].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfBlackRookRightFromWhitePiece != -1) {
          ((Rook)getBoard()[targetRow][PositionOfBlackRookRightFromWhitePiece])
              .setRight(PositionOfBlackRookRightFromWhitePiece-targetCol);
        }

        // 白のコマを動かした先のセルが、黒の Rook の上側経路に含まれる場合
        int PositionOfBlackRookDownFromWhitePiece= -1;
        for(int i=targetRow+1; i<ROW_RANGE; i++){
          if(getBoard()[i][targetCol]!=null
              && getBoard()[i][targetCol] instanceof Rook
              && !getBoard()[i][targetCol].isWhite){
            PositionOfBlackRookDownFromWhitePiece = i;
            break;
          }else if(getBoard()[i][targetCol]!=null
            && !(getBoard()[i][targetCol] instanceof Rook)
            && !(getBoard()[i][targetCol] instanceof Pawn && getBoard()[i][targetCol].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfBlackRookDownFromWhitePiece != -1) {
          ((Rook)getBoard()[PositionOfBlackRookDownFromWhitePiece][targetCol])
              .setUp(targetRow-PositionOfBlackRookDownFromWhitePiece);
        }

        // 白のコマを動かした先のセルが、黒の Rook の下側経路に含まれる場合
        int PositionOfBlackRookUpFromWhitePiece= -1;
        for(int i=targetRow-1; i>=0; i--){
          if(getBoard()[i][targetCol]!=null
              && getBoard()[i][targetCol] instanceof Rook
              && !getBoard()[i][targetCol].isWhite){
            PositionOfBlackRookUpFromWhitePiece = i;
            break;
          }else if(getBoard()[i][targetCol]!=null
            && !(getBoard()[i][targetCol] instanceof Rook)
            && !(getBoard()[i][targetCol] instanceof Pawn && getBoard()[i][targetCol].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfBlackRookUpFromWhitePiece != -1) {
          ((Rook)getBoard()[PositionOfBlackRookUpFromWhitePiece][targetCol])
              .setDown(PositionOfBlackRookUpFromWhitePiece-targetRow);
        }

//        // 白のコマを動かした先のセルが、白の Rook の右側経路に含まれる場合
        int PositionOfWhiteRookLeftFromWhitePiece= -1;
        for(int i=targetCol-1; i>=0; i--){
          if(getBoard()[targetRow][i]!=null
              && getBoard()[targetRow][i] instanceof Rook
              && getBoard()[targetRow][i].isWhite){
            PositionOfWhiteRookLeftFromWhitePiece = i;
            break;
          }else if(getBoard()[targetRow][i]!=null
            && !(getBoard()[targetRow][i] instanceof Rook)
            && !(getBoard()[targetRow][i] instanceof Pawn && getBoard()[targetRow][i].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfWhiteRookLeftFromWhitePiece != -1) {
          ((Rook)getBoard()[targetRow][PositionOfWhiteRookLeftFromWhitePiece])
              .setRight(targetCol-PositionOfWhiteRookLeftFromWhitePiece-1);
        }

        // 白のコマを動かした先のセルが、白の Rook の左側経路に含まれる場合
        int PositionOfWhiteRookRightFromWhitePiece= -1;
        for(int i=targetCol+1; i<COL_RANGE; i++){
          if(getBoard()[targetRow][i]!=null
              && getBoard()[targetRow][i] instanceof Rook
              && getBoard()[targetRow][i].isWhite){
            PositionOfWhiteRookRightFromWhitePiece = i;
            break;
          }else if(getBoard()[targetRow][i]!=null
              && !(getBoard()[targetRow][i] instanceof Rook)
              && !(getBoard()[targetRow][i] instanceof Pawn && getBoard()[targetRow][i].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfWhiteRookRightFromWhitePiece != -1) {
          ((Rook)getBoard()[targetRow][PositionOfWhiteRookRightFromWhitePiece])
              .setLeft(PositionOfWhiteRookRightFromWhitePiece-targetCol-1);
        }

        // 白のコマを動かした先のセルが、白の Rook の上側経路に含まれる場合
        int PositionOfWhiteRookDownFromWhitePiece= -1;
        for(int i=targetRow-1; i>=0; i--){
          if(getBoard()[i][targetCol]!=null
              && getBoard()[i][targetCol] instanceof Rook
              && getBoard()[i][targetCol].isWhite){
            PositionOfWhiteRookDownFromWhitePiece = i;
            break;
          }else if(getBoard()[i][targetCol]!=null
              && !(getBoard()[i][targetCol] instanceof Rook)
              && !(getBoard()[i][targetCol] instanceof Pawn && getBoard()[i][targetCol].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfWhiteRookDownFromWhitePiece != -1) {
          ((Rook)getBoard()[PositionOfWhiteRookDownFromWhitePiece][targetCol])
              .setUp(targetRow-PositionOfWhiteRookDownFromWhitePiece-1);
        }

        // 白のコマを動かした先のセルが、白の Rook の下側経路に含まれる場合
        int PositionOfWhiteRookUpFromWhitePiece= -1;
        for(int i=targetRow+1; i<ROW_RANGE; i++){
          if(getBoard()[i][targetCol]!=null
              && getBoard()[i][targetCol] instanceof Rook
              && getBoard()[i][targetCol].isWhite){
            PositionOfWhiteRookUpFromWhitePiece = i;
            break;
          }else if(getBoard()[i][targetCol]!=null
              && !(getBoard()[i][targetCol] instanceof Rook)
              && !(getBoard()[i][targetCol] instanceof Pawn && getBoard()[i][targetCol].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfWhiteRookUpFromWhitePiece != -1) {
          ((Rook)getBoard()[PositionOfWhiteRookUpFromWhitePiece][targetCol])
              .setDown(PositionOfWhiteRookUpFromWhitePiece-targetRow-1);
        }

      } else {
        // 黒のコマを動かした先のセルが、黒の Rook の右側経路に含まれる場合
        int PositionOfBlackRookLeftFromBlackPiece= -1;
        for(int i=targetCol+1; i<COL_RANGE; i++){
          if(getBoard()[targetRow][i]!=null
              && getBoard()[targetRow][i] instanceof Rook
              && !getBoard()[targetRow][i].isWhite){
            PositionOfBlackRookLeftFromBlackPiece = i;
            break;
          }else if(getBoard()[targetRow][i]!=null
              && !(getBoard()[targetRow][i] instanceof Rook)
              && !(getBoard()[targetRow][i] instanceof Pawn && getBoard()[targetRow][i].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfBlackRookLeftFromBlackPiece != -1) {
          ((Rook)getBoard()[targetRow][PositionOfBlackRookLeftFromBlackPiece])
              .setRight(PositionOfBlackRookLeftFromBlackPiece-targetCol-1);
        }

        // 黒のコマを動かした先のセルが、黒の Rook の左側経路に含まれる場合
        int PositionOfBlackRookRightFromBlackPiece= -1;
        for(int i=targetCol-1; i>=0; i--){
          if(getBoard()[targetRow][i]!=null
              && getBoard()[targetRow][i] instanceof Rook
              && !getBoard()[targetRow][i].isWhite){
            PositionOfBlackRookRightFromBlackPiece = i;
            break;
          }else if(getBoard()[targetRow][i]!=null
              && !(getBoard()[targetRow][i] instanceof Rook)
              && !(getBoard()[targetRow][i] instanceof Pawn && getBoard()[targetRow][i].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfBlackRookRightFromBlackPiece != -1) {
          ((Rook)getBoard()[targetRow][PositionOfBlackRookRightFromBlackPiece])
              .setLeft(targetCol-PositionOfBlackRookRightFromBlackPiece-1);
        }

        // 黒のコマを動かした先のセルが、黒の Rook の上側経路に含まれる場合
        int PositionOfBlackRookDownFromBlackPiece= -1;
        for(int i=targetRow+1; i<ROW_RANGE; i++){
          if(getBoard()[i][targetCol]!=null
              && getBoard()[i][targetCol] instanceof Rook
              && !getBoard()[i][targetCol].isWhite){
            PositionOfBlackRookDownFromBlackPiece = i;
            break;
          }else if(getBoard()[i][targetCol]!=null
              && !(getBoard()[i][targetCol] instanceof Rook)
              && !(getBoard()[i][targetCol] instanceof Pawn && getBoard()[i][targetCol].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfBlackRookDownFromBlackPiece != -1){
          ((Rook)getBoard()[PositionOfBlackRookDownFromBlackPiece][targetCol])
              .setUp(PositionOfBlackRookDownFromBlackPiece-targetRow-1);
        }

        // 黒のコマを動かした先のセルが、黒の Rook の下側経路に含まれる場合
        int PositionOfBlackRookUpFromBlackPiece= -1;
        for(int i=targetRow-1; i>=0; i--){
          if(getBoard()[i][targetCol]!=null
              && getBoard()[i][targetCol] instanceof Rook
              && !getBoard()[i][targetCol].isWhite){
            PositionOfBlackRookUpFromBlackPiece = i;
            break;
          }else if(getBoard()[i][targetCol]!=null
              && !(getBoard()[i][targetCol] instanceof Rook)
              && !(getBoard()[i][targetCol] instanceof Pawn && getBoard()[i][targetCol].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfBlackRookUpFromBlackPiece != -1) {
          ((Rook)getBoard()[PositionOfBlackRookUpFromBlackPiece][targetCol])
              .setDown(targetRow-PositionOfBlackRookUpFromBlackPiece-1);
        }

        // 黒のコマを動かした先のセルが、白の Rook の右側経路に含まれる場合
        int PositionOfWhiteRookLeftFromBlackPiece= -1;
        for(int i=targetCol-1; i>=0; i--){
          if(getBoard()[targetRow][i]!=null
              && getBoard()[targetRow][i] instanceof Rook
              && getBoard()[targetRow][i].isWhite){
            PositionOfWhiteRookLeftFromBlackPiece = i;
            break;
          }else if(getBoard()[targetRow][i]!=null
              && !(getBoard()[targetRow][i] instanceof Rook)
              && !(getBoard()[targetRow][i] instanceof Pawn && getBoard()[targetRow][i].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfWhiteRookLeftFromBlackPiece != -1) {
          ((Rook)getBoard()[targetRow][PositionOfWhiteRookLeftFromBlackPiece])
              .setRight(targetCol-PositionOfWhiteRookLeftFromBlackPiece);
        }

        // 黒のコマを動かした先のセルが、白の Rook の左側経路に含まれる場合
        int PositionOfWhiteRookRightFromBlackPiece= -1;
        for(int i=targetCol+1; i<COL_RANGE; i++){
          if(getBoard()[targetRow][i]!=null
              && getBoard()[targetRow][i] instanceof Rook
              && getBoard()[targetRow][i].isWhite){
            PositionOfWhiteRookRightFromBlackPiece = i;
            break;
          }else if(getBoard()[targetRow][i]!=null
              && !(getBoard()[targetRow][i] instanceof Rook)
              && !(getBoard()[targetRow][i] instanceof Pawn && getBoard()[targetRow][i].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfWhiteRookRightFromBlackPiece != -1) {
          ((Rook)getBoard()[targetRow][PositionOfWhiteRookRightFromBlackPiece])
              .setLeft(PositionOfWhiteRookRightFromBlackPiece-targetCol);
        }

        // 黒のコマを動かした先のセルが、白の Rook の上側経路に含まれる場合
        int PositionOfWhiteRookUpFromBlackPiece= -1;
        for(int i=targetRow-1; i>=0; i--){
          if(getBoard()[i][targetCol]!=null
              && getBoard()[i][targetCol] instanceof Rook
              && getBoard()[i][targetCol].isWhite){
            PositionOfWhiteRookUpFromBlackPiece = i;
            break;
          }else if(getBoard()[i][targetCol]!=null
              && !(getBoard()[i][targetCol] instanceof Rook)
              && !(getBoard()[i][targetCol] instanceof Pawn && getBoard()[i][targetCol].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfWhiteRookUpFromBlackPiece != -1) {
          ((Rook)getBoard()[PositionOfWhiteRookUpFromBlackPiece][targetCol])
              .setUp(targetRow-PositionOfWhiteRookUpFromBlackPiece);
        }

        // 黒のコマを動かした先のセルが、白の Rook の下側経路に含まれる場合
        int PositionOfWhiteRookDownFromBlackPiece= -1;
        for(int i=targetRow+1; i<ROW_RANGE; i++){
          if(getBoard()[i][targetCol]!=null
              && getBoard()[i][targetCol] instanceof Rook
              && getBoard()[i][targetCol].isWhite){
            PositionOfWhiteRookDownFromBlackPiece = i;
            break;
          }else if(getBoard()[i][targetCol]!=null
              && !(getBoard()[i][targetCol] instanceof Rook)
              && !(getBoard()[i][targetCol] instanceof Pawn
              && getBoard()[i][targetCol].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfWhiteRookDownFromBlackPiece != -1) {
          ((Rook)getBoard()[PositionOfWhiteRookDownFromBlackPiece][targetCol])
              .setDown(PositionOfWhiteRookDownFromBlackPiece-targetRow);
        }
      }


//       移動前のコマが、Pawnの前3マスに隣接している場合
//       移動後に前3マスから外れる場合にはフラグを解除する
      if (currentCell.isWhite) {
        // 白のPawnから見て左斜め前にある場合
        if (currRow-1>=0
            && currCol+1<COL_RANGE
            && getBoard()[currRow-1][currCol+1] != null
            && getBoard()[currRow-1][currCol+1] instanceof Pawn
            && !getBoard()[currRow-1][currCol+1].isWhite) {
          ((Pawn) getBoard()[currRow-1][currCol+1]).setLeftFront(false);
        }
        // 白のPawnから見て正面にある場合
        if (currRow-1>=0
            && getBoard()[currRow-1][currCol] != null
            && getBoard()[currRow-1][currCol] instanceof Pawn
            && !getBoard()[currRow-1][currCol].isWhite) {
          ((Pawn) getBoard()[currRow-1][currCol]).setFront(false);
        }
        // 白のPawnから見て右斜め前にある場合
        if (currRow-1>=0
            && currCol-1>=0
            && getBoard()[currRow-1][currCol-1] != null
            && getBoard()[currRow-1][currCol-1] instanceof Pawn
            && !getBoard()[currRow-1][currCol-1].isWhite) {
          ((Pawn) getBoard()[currRow-1][currCol-1]).setRightFront(false);
        }
        // 黒のPawnから見て左斜め前にある場合
        if (currRow+1<ROW_RANGE
            && currCol-1>=0
            && getBoard()[currRow+1][currCol-1] != null
            && getBoard()[currRow+1][currCol-1] instanceof Pawn
            && !getBoard()[currRow+1][currCol-1].isWhite) {
          ((Pawn) getBoard()[currRow+1][currCol-1]).setLeftFront(false);
        }
        // 黒のPawnから見て正面にある場合
        if (currRow+1<ROW_RANGE
            && getBoard()[currRow+1][currCol] != null
            && getBoard()[currRow+1][currCol] instanceof Pawn
            && !getBoard()[currRow+1][currCol].isWhite) {
          ((Pawn) getBoard()[currRow+1][currCol]).setFront(false);
        }
        // 黒のPawnから見て右斜め前にある場合
        if (currRow+1<ROW_RANGE
            && currCol+1<COL_RANGE
            && getBoard()[currRow+1][currCol+1] != null
            && getBoard()[currRow+1][currCol+1] instanceof Pawn
            && !getBoard()[currRow+1][currCol+1].isWhite) {
          ((Pawn) getBoard()[currRow+1][currCol+1]).setRightFront(false);
        }
      }else {
        // 白のPawnから見て右斜め前にある場合
        if (currRow-1>=0
            && currCol-1>= 0
            && getBoard()[currRow-1][currCol-1]!=null
            && getBoard()[currRow-1][currCol-1] instanceof Pawn
            && getBoard()[currRow-1][currCol-1].isWhite) {
          ((Pawn) getBoard()[currRow-1][currCol-1]).setRightFront(false);
        }
        // 白のPawnから見て正面にある場合
        if (currRow-1>= 0
            && getBoard()[currRow-1][currCol]!=null
            && getBoard()[currRow-1][currCol] instanceof Pawn
            && getBoard()[currRow-1][currCol].isWhite) {
          ((Pawn) getBoard()[currRow-1][currCol]).setFront(false);
        }
        // 白のPawnから見て左斜め前にある場合
        if (currRow-1>= 0
            && currCol+1 < COL_RANGE
            && getBoard()[currRow-1][currCol+1]!=null
            && getBoard()[currRow-1][currCol+1] instanceof Pawn
            && getBoard()[currRow-1][currCol+1].isWhite) {
          ((Pawn) getBoard()[currRow-1][currCol+1]).setLeftFront(false);
        }
        // 黒のPawnから見て右斜め前にある場合
        if (currRow+1<ROW_RANGE
            && currCol+1<COL_RANGE
            && getBoard()[currRow+1][currCol+1]!=null
            && getBoard()[currRow+1][currCol+1] instanceof Pawn
            && getBoard()[currRow+1][currCol+1].isWhite) {
          ((Pawn) getBoard()[currRow+1][currCol+1]).setRightFront(false);
        }
        // 黒のPawnから見て正面にある場合
        if (currRow+1<ROW_RANGE
            && getBoard()[currRow+1][currCol]!=null
            && getBoard()[currRow+1][currCol] instanceof Pawn
            && getBoard()[currRow+1][currCol].isWhite) {
          ((Pawn) getBoard()[currRow+1][currCol]).setFront(false);
        }
        // 黒のPawnから見て左斜め前にある場合
        if (currRow+1<ROW_RANGE
            && currCol-1>=0
            && getBoard()[currRow+1][currCol-1]!=null
            && getBoard()[currRow+1][currCol-1] instanceof Pawn
            && getBoard()[currRow+1][currCol-1].isWhite) {
          ((Pawn) getBoard()[currRow+1][currCol-1]).setLeftFront(false);
        }
      }

//       移動前のコマが、Rookの前後左右移動範囲に位置している場合
//       移動後にRookの移動範囲から外れる場合には移動可能範囲を再設定する
      if (currentCell.isWhite) {
        // 白のRookから見て右側経路にある場合
        int PositionOfWhiteRookLeftFromWhitePiece= -1;
        for(int i=currCol-1; i>=0; i--) {
          if (getBoard()[currRow][i]!=null
              && getBoard()[currRow][i] instanceof Rook
              && getBoard()[currRow][i].isWhite) {
            PositionOfWhiteRookLeftFromWhitePiece = i;
            break;
          }else if(getBoard()[currRow][i]!=null
              && !(getBoard()[currRow][i] instanceof Rook)
              && !(getBoard()[currRow][i] instanceof Pawn && getBoard()[currRow][i].getPosition().isEp())
          ){
            break;
          }
        }
        if(PositionOfWhiteRookLeftFromWhitePiece != -1) {
          // 右側の経路から上下にズレるとき
          if(targetRow!=currRow){
            // currColから右進んでいく
            // Rookが移動可能な最初のマスのcolを求める
            // 求めたcolと白Rookのcolとの差を求める
            int updatedRightEnd = -1;
            for(int i=currCol; i<COL_RANGE; i++){
              if(getBoard()[currRow][i]!=null
                  && !(getBoard()[currRow][i] instanceof Pawn
                      && getBoard()[currRow][i].getPosition().isEp())){
                if(getBoard()[currRow][i].isWhite) updatedRightEnd = i-1;
                else updatedRightEnd = i;
              }
            }
            if(updatedRightEnd==-1) updatedRightEnd = COL_RANGE-1;
            if(getBoard()[currRow][PositionOfWhiteRookLeftFromWhitePiece] instanceof Rook){
              ((Rook)getBoard()[currRow][PositionOfWhiteRookLeftFromWhitePiece])
                  .setRight(updatedRightEnd-PositionOfWhiteRookLeftFromWhitePiece);
            }
          }
        }
      }

      // Pawn のプロモーションを行う
      if(targetRow==ROW_RANGE-1 && updatedCell instanceof Pawn){
        System.out.println("Promotion");
        System.out.println("---------");
        System.out.println("1: Queen");
        System.out.println("2: Rook");
        System.out.println("3: Bishop");
        System.out.println("4: Knight");
        Scanner in = new Scanner(System.in);
        int option = in.nextInt();
        Piece promotedPiece = null;
        if(option==1){
          if(updatedCell.isWhite) promotedPiece = new Queen("♕", 9, true, new Position(targetRow, targetCol));
          else promotedPiece = new Queen("♛", 9, false, new Position(targetRow, targetCol));
        }else if(option == 2){
          // left, right, top down プロパティ要修正
          // *********************************************
          // *********************************************
          // *********************************************
          // *********************************************
          if(updatedCell.isWhite) promotedPiece = new Rook("♖", 5, true, new Position(targetRow, targetCol), 0 , 0 , 0, 0);
          else promotedPiece = new Queen("♜", 5, false, new Position(targetRow, targetCol));
        }else if(option == 3){
          if(updatedCell.isWhite) promotedPiece = new Bishop("♗", 3, true, new Position(targetRow, targetCol));
          else promotedPiece = new Bishop("♝", 3, false, new Position(targetRow, targetCol));
        }else if(option == 4){
          if(updatedCell.isWhite) promotedPiece = new Knight("♘", 2, true, new Position(targetRow, targetCol));
          else promotedPiece = new Queen("♞", 2, false, new Position(targetRow, targetCol));
        }
        ((Pawn) updatedCell).promote(promotedPiece);
        System.out.println();
      }

      printBoard();
      System.out.println();
    }
  }

  // helper functions
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
    board[1][0] = new Pawn("♙", 1, true, new Position(1, 0), true, false, false, false,false, null);
    board[1][1] = new Pawn("♙", 1, true, new Position(1, 1), true, false, false, false,false,  null);
    board[1][2] = new Pawn("♙", 1, true, new Position(1, 2), true, false, false, false,false, null);
    board[1][3] = new Pawn("♙", 1, true, new Position(1, 3), true, false, false, false,false, null);
    board[1][4] = new Pawn("♙", 1, true, new Position(1, 4), true, false, false, false,false, null);
    board[1][5] = new Pawn("♙", 1, true, new Position(1, 5), true, false, false, false,false, null);
    board[1][6] = new Pawn("♙", 1, true, new Position(1, 6), true, false, false, false,false, null);
    board[1][7] = new Pawn("♙", 1, true, new Position(1, 7), true, false, false, false,false, null);

    board[6][0] = new Pawn("♟", 1, false, new Position(6, 0), true, false, false, false,false, null);
    board[6][1] = new Pawn("♟", 1, false, new Position(6, 1), true, false, false, false,false, null);
    board[6][2] = new Pawn("♟", 1, false, new Position(6, 2), true, false, false, false,false, null);
    board[6][3] = new Pawn("♟", 1, false, new Position(6, 3), true, false, false, false,false, null);
    board[6][4] = new Pawn("♟", 1, false, new Position(6, 4), true, false, false, false,false, null);
    board[6][5] = new Pawn("♟", 1, false, new Position(6, 5), true, false, false, false,false, null);
    board[6][6] = new Pawn("♟", 1, false, new Position(6, 6), true, false, false, false,false, null);
    board[6][7] = new Pawn("♟", 1, false, new Position(6, 7), true, false, false, false,false, null);
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
