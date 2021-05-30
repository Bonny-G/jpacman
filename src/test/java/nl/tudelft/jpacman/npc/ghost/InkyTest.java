package nl.tudelft.jpacman.npc.ghost;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.google.common.collect.Lists;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InkyTest {
	
    private static MapParser mapParser;

    //实例化对象.
    @BeforeAll
    public static void setup() {
        //用于角色显示
        PacManSprites sprites = new PacManSprites();
        LevelFactory levelFactory = new LevelFactory(
                sprites,
                new GhostFactory(sprites),
                mock(PointCalculator.class));
        BoardFactory boardFactory = new BoardFactory(sprites);
        GhostFactory ghostFactory = new GhostFactory(sprites);
        mapParser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);
    }

    @Test
    @DisplayName("地图没有Player对象")
    @Order(1)
    void departWithoutPlayer() {
        List<String> text = Lists.newArrayList(
                "###########",
                "#I   B    #",
                "###       #",
                "###  ######");
        Level level = mapParser.parseMap(text);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());
        assertThat(level.isAnyPlayerAlive()).isFalse();

        //act:
        Optional<Direction> opt = inky.nextAiMove();

        //assert:
        assertThat(opt.isPresent()).isFalse();
    }

    @Test
    @DisplayName("地图中没有Blinky对象")
    @Order(2)
    void departWithoutBlinky() {
        List<String> text = Lists.newArrayList(
                "###########",
                "# I       #",
                "###       #",
                "######## P#");
        Level level = mapParser.parseMap(text);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);
        
        assertThat(blinky).isNull();
        
        //act:
        Optional<Direction> opt = inky.nextAiMove();

        //assert:
        assertThat(opt.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Inky和Blinky在Player后面,Inky到达不了Player")
    @Order(3)
    void pathWithoutInky() {
        List<String> text = Lists.newArrayList(
                "###########",
                "# I # B   #",
                "######### #",
                "#######  P#");
        Level level = mapParser.parseMap(text);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);

        //act:
        Optional<Direction> opt = inky.nextAiMove();

        //assert:
        assertThat(opt.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Inky和Blinky在Player后面,Inky到达Player")
    @Order(4)
    void pathWithInky() {
        List<String> text = Lists.newArrayList(
                "###########",
                "# I   B   #",
                "######### #",
                "#######  P#");
        Level level = mapParser.parseMap(text);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);

        //act:
        Optional<Direction> opt = inky.nextAiMove();

        //assert:
        assertThat(opt.get()).isEqualTo(Direction.valueOf("EAST"));
    }
    
    @Test
    @DisplayName("Blinky在Pac-Man身后,Inky在前面,Inky倾向于远离吃豆人")
    @Order(5)
    void InkyAwayPlayer() {
        List<String> text = Lists.newArrayList(
                "###########",
                "# I  B    #",
                "#####     #",
                "#  P#######");
        Level level = mapParser.parseMap(text);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);

        //act:
        Optional<Direction> opt = inky.nextAiMove();

        //assert:
        assertThat(opt.get()).isEqualTo(Direction.valueOf("EAST"));
    }

}



