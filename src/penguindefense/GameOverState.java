package penguindefense;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;

public class GameOverState extends BasicGameState {
	
	private boolean win = false;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		
	}
	
	public void setWin(boolean b) {
		win = b;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
PenguinDefenseGame myGame = (PenguinDefenseGame)game;
		
		g.drawImage(ResourceManager.getImage(PenguinDefenseGame.IMG_BACKGROUND), 0, 0);
		
		for (Wall[] l : myGame.myMap.walls) {
			for (Wall w : l) {
				if (w != null) {
					w.render(g);
				}
			}
		}
		
		for (Tile[] l : myGame.myMap.map) {
			for (Tile t : l) {
				t.render(g);
			}
		}
		
		for (Turret[] l : myGame.myMap.turrets) {
			for (Turret t : l) {
				if (t != null) {
					t.render(g);
				}
			}
		}
		
		g.setColor(Color.darkGray);
		g.drawString("Score: " + myGame.score, 10, 30);
		g.drawString("Press space to continue...", 220, 450);
		
		if (win) {
			g.drawImage(ResourceManager.getImage(PenguinDefenseGame.IMG_BANNER_VICTORY), 220, 270);
		} else {
			g.drawImage(ResourceManager.getImage(PenguinDefenseGame.IMG_BANNER_DEFEAT), 260, 270);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		Input input = container.getInput();
		PenguinDefenseGame myGame = (PenguinDefenseGame)game;
		
		if (input.isKeyPressed(Input.KEY_SPACE))
			myGame.enterState(PenguinDefenseGame.STARTUPSTATE);
	}

	@Override
	public int getID() {
		return PenguinDefenseGame.GAMEOVERSTATE;
	}

}
