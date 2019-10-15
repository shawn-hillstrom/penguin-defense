package penguindefense;

import java.util.ArrayList;

import org.newdawn.slick.Color;
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
		
		myGame.myMap = new GameMap(myGame.screenWidth/2, myGame.screenHeight/2, myGame);
		myGame.myMap.generate();
		
		myGame.obj = new Objective(myGame.screenWidth - 96, myGame.screenHeight/2, 10);
		
		myGame.enemies = new ArrayList<Penguin>(myGame.maxEnemies);
		
		myGame.lasers = new ArrayList<Laser>(myGame.myMap.maxTurrets);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		PenguinDefenseGame myGame = (PenguinDefenseGame)game;
		
		g.setColor(Color.darkGray);
		
		g.drawImage(ResourceManager.getImage(PenguinDefenseGame.IMG_BACKGROUND), 0, 0);
		
		myGame.obj.render(g);
		
		g.drawImage(ResourceManager.getImage(PenguinDefenseGame.IMG_BANNER_START), 220, 180);
		g.drawString("Press space to start...", 220, 540);
		
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
