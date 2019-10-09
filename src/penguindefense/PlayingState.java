package penguindefense;

import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;

public class PlayingState extends BasicGameState {
	
	private int enemyCount = 0;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		container.setSoundOn(true);
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
		
		myGame.obj.render(g);
		
		for (Penguin p : myGame.enemies) {
			p.render(g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		PenguinDefenseGame myGame = (PenguinDefenseGame)game;
		Input input = container.getInput();
		
		if (enemyCount < 1) {
			Penguin newP = new Penguin(0, myGame.screenHeight/2, 2f, myGame);
			newP.setVelocity(1, 0);
			myGame.enemies.add(newP);
			enemyCount++;
		}
		
		for (Tile[] l : myGame.myMap.map) {
			for (Tile t : l) {
				t.update(delta, input);
			}
		}
		
		for (Wall[] l : myGame.myMap.walls) {
			for (Wall w : l) {
				if (w != null) {
					w.update(delta);
				}
			}
		}
		
		for (Turret[] l : myGame.myMap.turrets) {
			for (Turret t : l) {
				if (t != null) {
					t.update(delta);
				}
			}
		}
		
		myGame.obj.update(delta);
		
		for (Iterator <Penguin> i = myGame.enemies.iterator(); i.hasNext();) {
			Penguin p = i.next();
			p.update(delta);
		}
	}

	@Override
	public int getID() {
		return PenguinDefenseGame.PLAYINGSTATE;
	}

}
