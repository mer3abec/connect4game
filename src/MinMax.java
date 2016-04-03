import javafx.util.Pair;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * Created by maksim on 13.03.2016.
 */
public class MinMax implements Callable<Integer> {

    //Each number in the table indicates how many win combinations this cell could be involved.
    // This makes possible to evaluate the importance of each cell.
    // 3 in the upper left corner indicates that only 3 win combinations can be. Down, Left and Diagonal.
    // Function sum values for computer player and subtracts for human. Works only for 6x7 board. 6 rows 7 cols.
    private static int[][] evalTable = {
            {3, 4, 5, 7, 5, 4, 3},
            {4, 6, 8, 10, 8, 6, 4},
            {5, 8, 11, 13, 11, 8, 5},
            {5, 8, 11, 13, 11, 8, 5},
            {4, 6, 8, 10, 8, 6, 4},
            {3, 4, 5, 7, 5, 4, 3}};
    private final int[][] start_state;
    private int x, y, maxplayer, human;
    private int maxdepth,time;

    public MinMax(int[][] board, int maxplayer,int maxdepth, int time) {
        this.time=time; this.maxdepth=maxdepth;
        start_state = copyBoard(board);
        this.x = board[0].length;
        this.y = board.length;
        this.maxplayer = maxplayer;
        if (maxplayer == 2) human = 1;
        else if (maxplayer == 1) human = 2;
    }

    @Override
    public Integer call() throws Exception {
        return minimax(start_state,maxdepth,time);
    }

    /**
     *
     * @param state - current game state
     * @param maxdepth - maximum ply depth algorithm has to evaluate the game progress
     * @param time - time constraint in ms, not used. The Idea was to call eval function when time riches 10 second
     * @return - At 0 depth returns best move, at depth > 0 returns evaluated action value.
     */
    private int minimax(int[][] state, int maxdepth,int time) {
//        if(maxplayer ==1) return maxV(state,0);
//        else return minV(state,0);
        return maxV(state, 0, maxdepth,this.time);
    }

    // action = column
    private int maxV(int[][] state, int depth, int maxdepth, int time) {
//        System.out.print("----------max----------- depth- "+ depth);
//        System.out.println();
//        printBoard(state);
        //if(terminal(state)) return utility(state);
        if (depth == maxdepth) {
            //System.out.println("Max depth");
            return eval(state);
        } else {
            Pair<Boolean, Integer> res = checkIfWon(state);
            if (res != null) {
                return utility(res);
            }
        }
        int bestMove = 0;
        int bestV = Integer.MIN_VALUE;
        for (int action = 0; action < state[0].length; action++) {
            if (isFull(state, action)) { // if [0,x] is empty, then it is possible move
                //update state with action
                int v = minV(result(state, action, maxplayer), depth + 1, maxdepth,time);
                if (v >= bestV) {
                    bestV = v;
                    bestMove = action;
                }
            }

        }
        if (depth == 0) {
            System.out.println("maxV-" + bestV);
            return bestMove;
        } else return bestV;

    }

    private int minV(int[][] state, int depth, int maxdepth,int time) {

        if (depth == maxdepth) {
            //System.out.println("Max depth");
            return eval(state);
        }
        else {
            Pair<Boolean, Integer> res = checkIfWon(state);
            if (res != null) {
                return utility(res);
            }
        }
        int bestMove = 0;
        int bestV = Integer.MAX_VALUE;
        for (int action = 0; action < state[0].length; action++) {
            if (isFull(state, action)) { // if [0,x] column is empty, then it is possible move
                //update state with action
                int v = maxV(result(state, action, human), depth + 1, maxdepth, time);
                if (v <= bestV) {
                    bestV = v;
                    bestMove = action;
                }
            }

        }
        if (depth == 0) {
            System.out.println("minV-" + bestV);
            return bestMove;
        } else return bestV;

    }

    /**
     *
     * @param pair - 2 tuple containing [boolean,int] indicating [win condition, playerID]
     * @return - returns utility value of terminal state for computer player
     */
    private int utility(Pair<Boolean, Integer> pair) {
        int res = -1;
        if (pair.getKey() && pair.getValue() == maxplayer) res = 1000;
        else if (pair.getKey() && pair.getValue() == human) res = -1000;
        else if (pair.getKey() && pair.getValue() == 0) res = 0;
        return res;
    }
    // prints board to console
    public void printBoard(int[][] board) {
        Stream.of(board).forEach(x -> System.out.println(Arrays.toString(x)));
    }
    // copy board to a new int[][] array
    public int[][] copyBoard(int[][] board) {
        int[][] state = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[i].length; j++)
                state[i][j] = board[i][j];
        return state;
    }

    /**
     *  <p>apply action to the state and transit/return new state</p>
     * @param state - current state
     * @param action - state to transit from
     * @param player - playersID
     * @return
     */
    private int[][] result(int[][] state, int action, int player) {
        int[][] newState = copyBoard(state);
        int y = getLastEmptyRowIndex(state, action);
        newState[y][action] = player;
//        System.out.println();
//        System.out.println("-------------result-----------------");
//        printBoard(newState);
//        System.out.println();

        return newState;
    }


    private int getLastEmptyRowIndex(int[][] state, int col) {
        int i = 0;
        for (i = 0; i < state.length; i++) {
            if (state[i][col] != 0) break;
        }
        return i - 1;
    }
    // return pair (boolean,int) - (true,playerID) indicating winner (true,0) if nobody won
    public static Pair<Boolean, Integer> checkIfWon(int[][] board) {
        int col = board[0].length;
        int row = board.length;
        //check horizontal
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col - 3; x++) {
                if ((board[y][x] != 0) && (board[y][x + 1] == board[y][x]) && (board[y][x + 2] == board[y][x]) && (board[y][x + 3] == board[y][x])) {
                    return new Pair<>(true, board[y][x]);
                }
            }
        }
        // vertical check
        for (int y = 0; y < row - 3; y++) {
            for (int x = 0; x < col; x++) {
                if ((board[y][x] != 0) && (board[y + 1][x] == board[y][x]) && (board[y + 2][x] == board[y][x]) && (board[y + 3][x] == board[y][x])) {
                    return new Pair<>(true, board[y][x]);
                }
            }
        }
        // diagonal check \
        for (int x = 0; x < col - 3; x++) {
            for (int y = 0; y < row - 3; y++) {
                if ((board[y][x] != 0) && (board[y + 1][x + 1] == board[y][x]) && (board[y + 2][x + 2] == board[y][x]) && (board[y + 3][x + 3] == board[y][x])) {
                    return new Pair<>(true, board[y][x]);
                }
            }
        }
        // diagonal check /
        for (int x = col - 1; x > 2; x--) {
            for (int y = 0; y < row - 3; y++) {
                if ((board[y][x] != 0) && (board[y + 1][x - 1] == board[y][x]) && (board[y + 2][x - 2] == board[y][x]) && (board[y + 3][x - 3] == board[y][x])) {
                    return new Pair<>(true, board[y][x]);
                }
            }
        }

        // tie check, returns true,0 if no one won
        if (tie(board)) return new Pair<>(true, 0);
        return null;
    }
    // check is there are possible moves on the board
    private static boolean tie(int[][] board) {
        int count_full = 0;
        for (int i = 0; i < board[0].length; i++) {
            if (!isFull(board, i)) count_full++;
        }
        return count_full == board[0].length;
    }

    // checks weather column is full true if not
    private static boolean isFull(int[][] state, int col) {
        return state[0][col] == 0;
    }
    // evaluates best value for the state if maximum depth reached
    public int eval(int[][] board) {
        int x = board[0].length;
        int y = board.length;
        int utility = 138;
        int sum = 0;
        for (int i = 0; i < y; i++)
            for (int j = 0; j < x; j++)
                if (board[i][j] == maxplayer)
                    sum += evalTable[i][j];
                else if (board[i][j] == human)
                    sum -= evalTable[i][j];
        return utility + sum;
    }
}
