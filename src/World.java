import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

public abstract class World extends javafx.scene.layout.Pane {
	
	
	HashSet<KeyCode> s;
	
	private javafx.animation.AnimationTimer timer;
	
	public World() {
		s = new HashSet<KeyCode>();
		timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				// TODO Auto-generated method stub
				act(now);
				ArrayList<javafx.scene.Node> a = new ArrayList<javafx.scene.Node>();
				for (javafx.scene.Node n : getChildren()) {
					if (n instanceof Actor) {
						a.add(n);
					}
				}
				Iterator<javafx.scene.Node> i = a.iterator();
				while(i.hasNext()) {
					((Actor)i.next()).act(now);
				}
			}
		};
	}
	public abstract void act(long now);
	
	public <A extends Actor> java.util.List<A> getObjects(java.lang.Class<A> cls) {
		List<A> arr = new ArrayList<A>();
		for (javafx.scene.Node n : getChildren()) {
			if (cls.isInstance(n)) {
				arr.add((A)n);
			}
		}
		return arr;
	}
	
	public void remove(Actor actor) {
		this.getChildren().remove(actor);
	}
	
	public void add(Actor actor) {
		this.getChildren().add(actor);
	}
	public void start() {
		timer.start();
	}
	public void stop() {
		timer.stop();
	}
	
	public void addKey(KeyCode key) {
		s.add(key);
	}
	
	public void removeKey(KeyCode key) {
		s.remove(key);
	}
	
	public boolean isKeyDown(KeyCode key) {
		return s.contains(key);
	}
}
