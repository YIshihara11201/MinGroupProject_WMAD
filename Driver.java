package GroupMiniProject;

import java.util.Scanner;

public class Driver {
  public static void main(String[] args) {
    Game game = new Game();
    Scanner in = new Scanner(System.in);
    game.printBoard();
    while(true){
      if(game.getTurn()%2 == 1){
        System.out.println("White to move");
      }else{
        System.out.println("Black to move");
      }
      System.out.print("Enter UCI (type 'help' for help): ");
      String input = in.next();

      if(input.equals("help")) {
        game.showHelp();
      }
      else if(input.equals("board")) {
        game.printBoard();
      }
      else if(input.equals("resign")) {
        game.resign();
      }else if(input.equals("quit")){
        System.out.println("Thank you for playing!!");
        break;
      }
      else if(input.equals("moves")){
        //to be updated

      }else if(input.length()==2){
        try{
          game.showAvailableCell(input);
        }catch(Exception e){
          System.out.println(e.getMessage());
          System.out.println();
        }
      }else if(input.length()==4){
        try{
          game.movePiece(input);
          game.setTurn(game.getTurn()+1);
        }catch(Exception e){
          System.out.println(e.getMessage());
          System.out.println();
        }
      }else{
        System.out.println("Invalid input, try again");
        System.out.println();
      }
    }
  }
}
