package chessgame.result;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * Interface for handling data regarding a relational database.
 */
@RegisterBeanMapper(GameResult.class)
public interface GameResultDao {

    /**
     * Creates a table and specifies some constraints on the attributes of the table.
     */
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

    /**
     * Inserts game results to a table in the relational database.
     *
     * @param gameResult an object that contains the detailed information about the procees of the game
     */
    @SqlUpdate("INSERT INTO gameresult VALUES (:id,:playerName, :stepsByPlayer, :duration, :gameEndTime)")
    void insertGameResult(@BindBean GameResult gameResult);

    /**
     * Returns all the game results in the relational database.
     *
     * @return all the game results in the relational database
     */
    @SqlQuery("SELECT * FROM gameresult ORDER BY id")
    List<GameResult> listGameResults();

    /**
     * Returns the top five results stored in the relational database.
     *
     * @return the top five results stored in the relational database
     */
    @SqlQuery("select * from (select playername , stepsbyplayer , duration , gameEndTime from gameresult order by duration) where rownum <= 5")
    List<GameResult> listTopFiveResults();

    /**
     * Returns the last ID of the game stored in the relational database for ensuring entity integrity constraint.
     *
     * @return the last ID of the game stored in the relational database for ensuring entity integrity constraint
     */
    @SqlQuery("SELECT id FROM gameresult WHERE ROWNUM <= 1 ORDER BY id DESC")
    int getLastGameID();

    /**
     * A method for deleting all of the records stored in the database.
     */
    @SqlUpdate("DELETE FROM gameresult")
    void deleteAllRows();

}
