//// コマを動かした先のセルが、白の Rook の下側経路に含まれる場合
//    int RowOfWhiteRookUpFromTargetPiece= -1;
//        for(int i=targetRow+1; i<ROW_RANGE; i++){
//    if(getBoard()[i][targetCol]!=null
//    && (getBoard()[i][targetCol] instanceof Rook
//    || (getBoard()[i][targetCol] instanceof Pawn && ((Pawn) getBoard()[i][targetCol]).getNewPiece() instanceof Rook))
//    && getBoard()[i][targetCol].isWhite
//    ){
//    if(getBoard()[targetRow][targetCol].isWhite) RowOfWhiteRookUpFromTargetPiece= i-1;
//    else RowOfWhiteRookUpFromTargetPiece = i;
//    break;
//    }else if(getBoard()[i][targetCol]!=null && !(getBoard()[i][targetCol] instanceof Rook)){
//    break;
//    }
//    }
//    if(RowOfWhiteRookUpFromTargetPiece != -1) {
//    if(getBoard()[RowOfWhiteRookUpFromTargetPiece][targetCol] instanceof Rook){
//    ((Rook)getBoard()[RowOfWhiteRookUpFromTargetPiece][targetCol])
//    .setDown(RowOfWhiteRookUpFromTargetPiece-targetRow);
//    }else if(((Pawn) getBoard()[RowOfWhiteRookUpFromTargetPiece][targetCol]).getNewPiece() instanceof Rook) {
//    ((Rook) ((Pawn) getBoard()[targetRow][RowOfWhiteRookUpFromTargetPiece])
//    .getNewPiece())
//    .setDown(RowOfWhiteRookUpFromTargetPiece - targetRow);
//    }
//    }