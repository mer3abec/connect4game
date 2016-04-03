import javafx.util.Pair;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.stream.Stream;


public class GameLogic implements IGameLogic {
    private int x = 0,y = 0, playerID,turn=0,last_x=-1,last_y=-1;
    private int [][] board;
    private ExecutorService pool = Executors.newFixedThreadPool(3);
    public GameLogic() {
        //TODO Write your implementation for this method
        System.out.println("Game logic ....");
    }
	// here we build up the board  to track game progress
    public void initializeGame(int x, int y, int playerID) {
        this.x = x;
        this.y = y;
        this.playerID = playerID; // id is 1 or 2 depends on the args we passed
        this.board = new int [y][x];
        //TODO Write your implementation for this method
        System.out.println(" initGame..... x-" + x + " y-" + y+ " id-" + playerID);
    }
	// Called after each insertion of the coin to check game state if the goal is reached by any player.
    public Winner gameFinished() {
        //TODO Write your implementation for this method
        System.out.println("Game finished.....?");
       // printBoard(board);
        //System.out.println("Minimax - " + MinMax.checkIfWon(board));
        Pair<Boolean,Integer> check = MinMax.checkIfWon(board);
        if(check == null) return Winner.NOT_FINISHED;
        if(check.getKey() && check.getValue() == 1) return Winner.PLAYER1;
        if(check.getKey() && check.getValue() == 2) return Winner.PLAYER2;
        if(check.getKey() && check.getValue() == 0) return Winner.TIE;
        return Winner.NOT_FINISHED;
    }

    // Called by p1 or p2 after coin insertion
    public void insertCoin(int column, int playerID) {

         last_y = getLastEmptyRowIndex(board,column);
         last_x = column;
         board[last_y][last_x] = playerID;
         turn++;// increment game turn
        //System.out.println("insert coin..... col -" + column + " id-"+ playerID);
    }
    // must return legal move for computer player
    public int decideNextMove() {
        System.out.println("decide next move....");
        //TODO Write your implementation for this method
        if(turn == 0) return randomMove(); // if computers first move then it is random
        if(turn == 1) return last_x; // if second move, then put on top of human move
        //pass current board and ply level (depth) and time in ms, higher depth mean harder computer game play.
        Future<Integer> minmax = pool.submit(new MinMax(board,playerID,8,10000));
        int move = 0;
        try {
            move = minmax.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return move;
    }
    // checks weather column is full true if not
    private boolean isFull(int[][] state, int col){
        return state[0][col]==0;
    }
    private int getLastEmptyRowIndex(int[][] state, int col){
        int i =0;
        for(i = 0; i < state.length; i++){
            if(state[i][col] != 0) break;
        }
        return i-1;
    }
    private void printBoard(int[][] board){
        Stream.of(board).forEach(x -> System.out.println(Arrays.toString(x)));
    }
    private int checkVertically(int[][] board){

        return 0;
    }
    private int checkIfWon(int[][] board, int id, int last_x, int last_y, int dx, int dy){
        int sum =1;
        int x=last_x+dx,y=last_y+dy;
       // System.out.println(" x-"+x + " y-"+ y + " dx-"+dx + " dy-" + dy+ " id="+id);
        while (x >=0 && x < this.x && y < this.y && y >=0 && board[y][x]==id ){
            x=x+dx;y=y+dy;sum++;
        }
        //System.out.print(" sum=" + sum );
        x=last_x-dx;y=last_y-dy;
        //System.out.println(" x-"+x + " y-"+ y + " dx-"+dx + " dy-" + dy+ " id="+id);
        while (x >=0 && x < this.x && y < this.y && y >=0 && board[y][x]==id ){
            x=x-dx;y=y-dy;sum++;
        }
        //System.out.print(" sum=" + sum );
        return sum;
    }
    private int randomMove(){
        int move=-1;
              do {
            move = 0 + (int) (Math.random() * x);

        }while (!isFull(board,move));
        return move;
    }
//        //System.out.println("res " + checkIfWon(board,last_player,last_x,last_y,1,0));
//        //check horizontally
//        int w1 = checkIfWon(board,last_player,last_x,last_y,1,0);
//        //check vertical
//        int w2 = checkIfWon(board,last_player,last_x,last_y,0,1);
//        // check diagonal from last move from top left to bottom right
//        int w3 = checkIfWon(board,last_player,last_x,last_y,1,1);
//        // check diagonal from last move from top right to bottom left
//        int w4 = checkIfWon(board,last_player,last_x,last_y,-1,1);
//
//        int max = Math.max(w1,Math.max(w2,Math.max(w3,w4)));
//        if(max >=4 && last_player==1) return Winner.PLAYER1;
//        else if (max >= 4 && last_player ==2) return Winner.PLAYER2;
//        //check for draw must be the last check
//        int count_full = 0;
//        for(int i = 0; i < x; i++){
//            if(!isFull(board,i)) count_full++;
//            System.out.println(count_full);
//        }
//        if(count_full==x) return Winner.TIE;
}
