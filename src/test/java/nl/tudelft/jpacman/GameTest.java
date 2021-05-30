package nl.tudelft.jpacman;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.ghost.Navigation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameTest {
	private Launcher launcher = new Launcher();
	
	@Test
	void startGame() {
	launcher.withMapFile("/board.txt");
	launcher.launch();
	//模拟点击开始
	launcher.getGame().start();
	assertThat(launcher.getGame().isInProgress()).isTrue();
	}
	
	@Test
	void pauseGame() {
	launcher.withMapFile("/board.txt");
	launcher.launch();
	//模拟点击开始
	launcher.getGame().start();
	assertThat(launcher.getGame().isInProgress()).isTrue();
	//模拟点击暂停
	launcher.getGame().stop();
	assertThat(launcher.getGame().isInProgress()).isFalse();
	}
	
	@Test
	void keepPlayingGame() {
	launcher.withMapFile("/board.txt");
	launcher.launch();
	//模拟点击开始
	launcher.getGame().start();
	assertThat(launcher.getGame().isInProgress()).isTrue();
	//模拟点击暂停
	launcher.getGame().stop();
	assertThat(launcher.getGame().isInProgress()).isFalse();
	//模拟点击继续
	launcher.getGame().start();
	assertThat(launcher.getGame().isInProgress()).isTrue();
	}
	
	
}




