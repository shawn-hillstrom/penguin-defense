package penguindefense;

import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;
import jig.Vector;

public class PlayingState extends BasicGameState {
	
	private int enemyCount = 0;
	private int time = 0;

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
		
		g.setColor(Color.red);
		
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
		
		for (Laser l : myGame.lasers) {
			g.drawLine(l.start.getX(), l.start.getY(), l.end.getX(), l.end.getY());
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
		
		time += delta;
		if (time >= 500) {
			Penguin newP = new Penguin(0, myGame.screenHeight/2, 2f, myGame);
			newP.setVelocity(1, 0);
			myGame.enemies.add(newP);
			enemyCount++;
			time = 0;
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
					if (w.getLives() <= 0) {
						Vector hashIndex = myGame.myMap.hashPos(w);
						myGame.myMap.walls[(int)hashIndex.getX()][(int)hashIndex.getY()] = null;
					}
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
		
		for (Iterator <Laser> i = myGame.lasers.iterator(); i.hasNext();) {
			Laser l = i.next();
			if (l.isActive()) {
				l.update(delta);
			} else {
				i.remove();
			}
		}
	}

	@Override
	public int getID() {
		return PenguinDefenseGame.PLAYINGSTATE;
	}

}
