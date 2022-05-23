package chessgame.result;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameResult {

    private String name;

    private int step;

    private Long duration;

    private String time;

}
