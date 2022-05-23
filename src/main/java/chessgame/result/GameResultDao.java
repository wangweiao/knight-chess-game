package chessgame.result;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(GameResult.class)
public interface GameResultDao {

    @SqlUpdate("""
        create table gameresult (
            id int primary key not null,
            playerName varchar2(100) not null,
            stepsByPlayer int not null,
            duration int not null,
            gameEndTime varchar2(100) not null
        )
        """
    )

    void createTable();

    @SqlUpdate("INSERT INTO gameresult VALUES (:id,:playerName, :stepsByPlayer, :duration, :gameEndTime)")
    void insertGameResult(@BindBean GameResult gameResult);

    @SqlQuery("SELECT * FROM gameresult ORDER BY id")
    List<GameResult> listGameResults();

    @SqlQuery("select * from (select playername , stepsbyplayer , duration , gameEndTime from gameresult order by duration) where rownum <= 5")
    List<GameResult> listTopFiveResults();

    @SqlQuery("SELECT id FROM gameresult WHERE ROWNUM <= 1 ORDER BY id DESC")
    int getLastGameID();

    @SqlUpdate("DELETE FROM gameresult")
    void deleteAllRows();
}
