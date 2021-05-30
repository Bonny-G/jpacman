package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.GameFactory;
import nl.tudelft.jpacman.level.*;
import nl.tudelft.jpacman.npc.ghost.Clyde;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.npc.ghost.GhostMapParser;
import nl.tudelft.jpacman.npc.ghost.Navigation;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class PlayerCollisionsTest {
	private static MapParser mapParser;
	
	@BeforeAll
	public static void setup() {
	        PacManSprites sprites = new PacManSprites();
	LevelFactory levelFactory = new LevelFactory(
	            sprites,
	            new GhostFactory(sprites),
	           new DefaultPointCalculator());
	BoardFactory boardFactory = new BoardFactory(sprites);
	GhostFactory ghostFactory = new GhostFactory(sprites);
	//地图的生成
	mapParser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);
	}
	
	@Test
	@DisplayName("玩家吃豆子")
	@Order(1)
	void playerCollideBean() {
	        List<String> text = Lists.newArrayList(
	"##############",
	"#.#....C.....P",
	"##############");
	Level level = mapParser.parseMap(text);
	//创建Player
	Player player = new PlayerFactory(new PacManSprites()).createPacMan();
	player.setDirection(Direction.valueOf("WEST"));
	//注册player到地图中去
	level.registerPlayer(player);
	Player p = Navigation.findUnitInBoard(Player.class, level.getBoard());
	assertThat(p).isNotNull();
	assertThat(p.getDirection()).isEqualTo(Direction.valueOf("WEST"));
	level.start();
	level.move(p, Direction.WEST);
	assertThat(p.getScore()).isEqualTo(10);
	}
	
	@Test
	@DisplayName("鬼吃玩家")
	@Order(2)
	void playerCollideGhost() {
	        List<String> text = Lists.newArrayList(
	"##############",
	"#.#....CP.....",
	"##############");
	Level level = mapParser.parseMap(text);
	//在棋盘中找到魔鬼对象
	Clyde clyde = Navigation.findUnitInBoard(Clyde.class,level.getBoard());
	assertThat(clyde).isNotNull();
	assertThat(clyde.getDirection()).isEqualTo(Direction.valueOf("EAST"));
	//创建Player
	Player player = new PlayerFactory(new PacManSprites()).createPacMan();
	player.setDirection(Direction.valueOf("WEST"));
	//注册player到地图中去
	level.registerPlayer(player);
	Player p = Navigation.findUnitInBoard(Player.class,level.getBoard());
	assertThat(p).isNotNull();
	assertThat(p.getDirection()).isEqualTo(Direction.valueOf("WEST"));
	level.start();
	level.move(p, Direction.WEST);
	//assert:
	assertThat(p.isAlive()).isFalse();
	}
}
