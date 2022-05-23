package chessgame.state;

import org.tinylog.Logger;

import java.util.Arrays;

/**
 * Class representing the state of the game.
 */
public class GameState {

    private static final char[][] INITIAL = {
            {'w', 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 'b'}
    };

    private char[][] board;

    private int whiteKnightRow;
    private int whiteKnightCol;
    private int blackKnightRow;
    private int blackKnightCol;

    /**
     * Returns the {@code board} of the game.
     *
     * @return the {@code board} of the game
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * Provides a default constructor for the class to invoke the parameterized one.
     */
    public GameState() {
        this(INITIAL);
    }

    /**
     * Provides a parameterized constructor for the game.
     *
     * @param initial the initial {@code board} of the game
     */
    public GameState(char[][] initial) {
        setInitial(initial);
    }

    private void setInitial(char[][] initial) {
        board = new char[8][8];
        whiteKnightRow = 0;
        whiteKnightCol = 0;
        blackKnightRow = 7;
        blackKnightCol = 7;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = initial[i][j];
            }
        }
    }

    /**
     * Checks whether the specified movement is valid or not.
     *
     * @param destinationRow the row for placing the picked piece
     * @param destinationCol the column for placing the picked piece
     * @param cell the char that is placed on the original cell
     * @return the result about whether the specified movement is valid or not
     */
    public boolean isValidMovement(int destinationRow, int destinationCol, char cell) {
        if (cell == 'w') {
            if ((Math.abs(whiteKnightRow - destinationRow) == 2 && Math.abs(whiteKnightCol - destinationCol) == 1) ||
                    (Math.abs(whiteKnightRow - destinationRow) == 1 && Math.abs(whiteKnightCol - destinationCol) == 2)) {
                return true;
            }
        } else if (cell == 'b') {
            if ((Math.abs(blackKnightRow - destinationRow) == 2 && Math.abs(blackKnightCol - destinationCol) == 1) ||
                    (Math.abs(blackKnightRow - destinationRow) == 1 && Math.abs(blackKnightCol - destinationCol) == 2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Performs the specified movement of the piece.
     *
     * @param destinationRow the row for placing the picked piece
     * @param destinationCol the column for placing the picked piece
     * @param cell the char that is placed on the original cell
     */
    public void performMovement(int destinationRow, int destinationCol, char cell) {
        if (cell == 'w') {
            Logger.debug("White knight at ({}, {}) is moved to ({}, {}).", whiteKnightRow, whiteKnightCol, destinationRow, destinationCol);
            board[destinationRow][destinationCol] = 'w';
            board[whiteKnightRow][whiteKnightCol] = 't';
            whiteKnightRow = destinationRow;
            whiteKnightCol = destinationCol;
        } else if (cell == 'b') {
            Logger.debug("Black knight at ({}, {}) is moved to ({}, {}).", whiteKnightRow, whiteKnightCol, destinationRow, destinationCol);
            board[destinationRow][destinationCol] = 'b';
            board[blackKnightRow][blackKnightCol] = 't';
            blackKnightRow = destinationRow;
            blackKnightCol = destinationCol;
        }
    }

    /**
     * Checks if all the cells have been filled.
     *
     * @return the result about whether all the cells have been filled or not
     */
    public boolean isAllFilled() {
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 0)
                    return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the specified objects are the same or not.
     *
     * @param o the object to be compared
     * @return the result about whether the specified objects are the same or not
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((o instanceof GameState gameState) && board[i][j] != gameState.getBoard()[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

}