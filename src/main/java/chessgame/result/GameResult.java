package chessgame.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
