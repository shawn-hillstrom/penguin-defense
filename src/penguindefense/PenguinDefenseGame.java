package penguindefense;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Penguin Defense is a tile-based, tower defense game. Your objective: to defend your
 * town from ravenous penguins trying to steal your fish.
 * 
 * This class represents an entire game of Penguin Defense and contains all resources,
 * entities, and other important information concerning the current game.
 * 
 * The state-based game has three states, StartUp, Playing, and GameOver. A player
 * progresses through these states based on game conditions and user input.
 */
public class PenguinDefenseGame extends StateBasedGame {
	
	// state variables
	public static final int STARTUPSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	
	// screen size
	public final int screenWidth;
	public final int screenHeight;
	
	// resources
	
	// entities

	/**
	 * Constructor for a game of Penguin Defense.
	 * 
	 * @param name
	 * - the name of the game, to be displayed in the window
	 * @param height
	 * - the height (in pixels) of the game window
	 * @param width
	 * - the width (in pixels) of the game window
	 */
	public PenguinDefenseGame(String name, int width, int height) {
		super(name);
		screenWidth = width;
		screenHeight = height;
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new StartUpState());
	}

	public static void main(String args[]) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new PenguinDefenseGame("Penguin Defense", 1280, 720));
			app.setDisplayMode(1280, 720, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
}
