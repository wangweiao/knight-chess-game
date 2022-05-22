package chessgame.state;

import org.tinylog.Logger;

import java.util.Arrays;

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

    public char[][] getBoard() {
        return board;
    }

    public GameState() {
        this(INITIAL);
    }

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

    public boolean isAllFilled() {
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 0)
                    return false;
            }
        }
        return true;
    }
}