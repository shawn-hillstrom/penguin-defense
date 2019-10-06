package penguindefense;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;

/**
 * This state is active before the game starts. A player can see the map, and can
 * transition into the next state (PlayingState) by pressing any key.
 * 
 * Transitions from (Initialization), GameOverState.
 * 
 * Transitions to PlayingState.
 */
public class StartUpState extends BasicGameState {

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		
		PenguinDefenseGame myGame = (PenguinDefenseGame)game;
		
		myGame.myMap = new GameMap(myGame.screenWidth/2, myGame.screenHeight/2);
		myGame.myMap.generate();
		
		myGame.enemies = new ArrayList<Penguin>(1000);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
//		PenguinDefenseGame myGame = (PenguinDefenseGame)game;
		
		g.drawImage(ResourceManager.getImage(PenguinDefenseGame.IMG_BACKGROUND), 0, 0);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		Input input = container.getInput();
		PenguinDefenseGame myGame = (PenguinDefenseGame)game;
		
		if (input.isKeyDown(Input.KEY_SPACE))
			myGame.enterState(PenguinDefenseGame.PLAYINGSTATE);
	}

	@Override
	public int getID() {
		return PenguinDefenseGame.STARTUPSTATE;
	}

}
