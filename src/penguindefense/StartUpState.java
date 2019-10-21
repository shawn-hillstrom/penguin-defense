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
	
	private int map = 1;
	private int mapLoaded;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		
		PenguinDefenseGame myGame = (PenguinDefenseGame)game;
		
		myGame.setGameVars(0, 200);
		myGame.deathToll = 0;
		
		myGame.myMap = new GameMap(myGame.screenWidth/2, myGame.screenHeight/2, myGame);
		mapLoaded = 0;
		generateMap(myGame);
		
		myGame.obj = new Objective(myGame.screenWidth - 96, myGame.screenHeight/2, 1000);
		
		myGame.enemies = new ArrayList<Penguin>(myGame.maxEnemies);
		
		myGame.lasers = new ArrayList<Laser>(myGame.myMap.maxTurrets);
	}
	
	/**
	 * Generate a new map if the loaded map and selected map don't line up.
	 * 
	 * @param myGame
	 * - reference to the game object
	 */
	private void generateMap(PenguinDefenseGame myGame) {
		if (mapLoaded != map) {
			if (map == 1) {
				myGame.myMap.generate(myGame.map1);
			} else if (map == 2) {
				myGame.myMap.generate(myGame.map2);
			} else if (map == 3) {
				myGame.myMap.generate(myGame.map3);
			}
			myGame.myMap.dijkstra();
			mapLoaded = map;
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		PenguinDefenseGame myGame = (PenguinDefenseGame)game;
		
		g.drawImage(ResourceManager.getImage(PenguinDefenseGame.IMG_BACKGROUND), 0, 0);
		
		// render tiles
		for (Tile[] l : myGame.myMap.map) {
			for (Tile t : l) {
				t.render(g);
			}
		}
		
		myGame.obj.render(g);
		
		g.drawImage(ResourceManager.getImage(PenguinDefenseGame.IMG_BANNER_START), 220, 180);
		g.setColor(Color.darkGray);
		g.drawString("Press space to start...", 220, 540);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		Input input = container.getInput();
		PenguinDefenseGame myGame = (PenguinDefenseGame)game;
		
		// check for space bar
		if (input.isKeyPressed(Input.KEY_SPACE))
			myGame.enterState(PenguinDefenseGame.PLAYINGSTATE);
		
		// check for map switching
		if (input.isKeyPressed(Input.KEY_LEFT)) {
			if (map == 1) {
				map = 3;
			} else {
				map -= 1;
			}
		} else if (input.isKeyPressed(Input.KEY_RIGHT)) {
			if (map == 3) {
				map = 1;
			} else {
				map += 1;
			}
		}
		
		// generate a new map if necessary
		generateMap(myGame);
	}

	@Override
	public int getID() {
		return PenguinDefenseGame.STARTUPSTATE;
	}

}
