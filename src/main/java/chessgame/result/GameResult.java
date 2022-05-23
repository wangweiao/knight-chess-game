package chessgame.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The basic data of the game result to be stored in the database.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameResult {

    private int id;

    private String playerName;

    private int stepsByPlayer;

    private int duration;

    private String gameEndTime;

}
