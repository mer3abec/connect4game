# connect4game
Connect Four, using Java programming language

The supporting files such as Interface and UI (User Interface) were provided by the ISP teacher.

Explanation of evaluation function
Evaluation function is supported by evaluation table:
Evaluation table - each table element indicates the amount of the possible winning combinations, that can
be found by the given cell of 2d array.

private static int[][] evalTable = {

            {3, 4, 5, 7, 5, 4, 3},
            
            {4, 6, 8, 10, 8, 6, 4},
            
            {5, 8, 11, 13, 11, 8, 5},
            
            {5, 8, 11, 13, 11, 8, 5},
            
            {4, 6, 8, 10, 8, 6, 4},
            
            {3, 4, 5, 7, 5, 4, 3}};
            
            
For example, 3 in the upper left corner could win only using 3 combinations:
horizontal, vertical and diagonal right. The most important elements are in the middle of the board, that
represents the highest combination number.

Evaluation function - it goes through the board and looks up the value in the evaluation table if the value of
the board table is computer id, then it sums the value from the evaluation table for the same index.
Otherwise if it is the human id, the value is subtracted. Afterwards it returns the sum of the utility value
and sum of the tokens. The better evaluation value for the computer player is 276.
Starting Utility is 138, because total sum of the evaluation table cells is 2 x 138. The points are divided
equally for the computer and human player.

Explanation of cut-off function
If our algorithm reached the maximum depth, then we evaluate the current game status with the
evaluation function. If not then we use Utility, which return correct value of terminal state.
If no one wins then the return is 0, otherwise it returns -1000 or 1000. If computer loses return -1000 and if
the human wins it returns 1000.

Explanation of the search algorithm
For the search minimax algorithm used that is presented in the Artificial Intelligent Modern Approach Book.
When the DecideNextMoth method in the game class is called, then it creates a minimax instance. Through
the method it passes the current state of the board. Using the state of the board the algorithm expands the
game tree. The limitation for PLY is 8, in order to have a reasonable time that computer can use for the
decision.

Thr classical minimax algorithm used and cut-off the game tree with the evaluation function and maximum depth, which is
8. However, our algorithm is not perfect, because sometimes it faces the horizon effect.
If the computer player starts the game the move is made randomly.
