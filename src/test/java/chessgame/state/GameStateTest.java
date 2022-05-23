package chessgame.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    private GameState initialState;
    private GameState onePosibleGoalState;
    private char[][] INITIAL;
    private char[][] GOAL;

    @BeforeEach
    void setUp() {
        initialState = new GameState();
        GOAL = new char[][]{
                {'w', 't', 't', 't', 't', 't', 't', 't'},
                {'t', 't', 't', 't', 't', 't', 't', 't'},
                {'t', 't', 't', 't', 't', 't', 't', 't'},
                {'t', 't', 't', 't', 't', 't', 't', 't'},
                {'t', 't', 't', 't', 't', 't', 't', 't'},
                {'t', 't', 't', 't', 't', 't', 't', 't'},
                {'t', 't', 't', 't', 't', 't', 't', 't'},
                {'t', 't', 't', 't', 't', 't', 't', 'b'}
        };
        onePosibleGoalState = new GameState(GOAL);

        INITIAL = new char[][]{
                {'w', 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 'b'}
        };
    }

    @Test
    void testGetBoard() {
        GameState gameState = new GameState();
        boolean isBoardEquals = true;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (INITIAL[i][j] != gameState.getBoard()[i][j]) {
                    isBoardEquals = false;
                }
            }
        }
        assertTrue(isBoardEquals);

        isBoardEquals = true;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (GOAL[i][j] != gameState.getBoard()[i][j]) {
                    isBoardEquals = false;
                }
            }
        }
        assertFalse(isBoardEquals);
    }

    @Test
    void testGameState() {
        assertEquals(initialState, new GameState());
        assertEquals(initialState, new GameState(INITIAL));

        assertNotEquals(onePosibleGoalState, new GameState());
        assertEquals(onePosibleGoalState, new GameState(GOAL));
    }

    @Test
    void tsetIsValidMovement() {
        GameState gameState = new GameState();
        assertTrue(gameState.isValidMovement(1, 2, 'w'));
        assertFalse(gameState.isValidMovement(2, 2, 'w'));
        assertTrue(gameState.isValidMovement(6, 5, 'b'));
        assertFalse(gameState.isValidMovement(5, 5, 'b'));
    }

    @Test
    void testPerformMovement() {
        GameState gameState = new GameState();
        gameState.performMovement(1, 2, 'w');
        assertEquals('w', gameState.getBoard()[1][2]);
        assertNotEquals('b', gameState.getBoard()[1][2]);

        gameState.performMovement(6, 5, 'b');
        assertEquals('b', gameState.getBoard()[6][5]);
        assertNotEquals('w', gameState.getBoard()[6][5]);
    }

    @Test
    void testIsAllFilled() {
        assertFalse(initialState.isAllFilled());
        assertTrue(onePosibleGoalState.isAllFilled());
    }

}
