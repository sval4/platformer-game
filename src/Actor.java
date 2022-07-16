import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;

public abstract class Actor extends ImageView {
	
	public Actor() {
		
	}
	public abstract void act(long now);
	
	public double getHeight() {
		return this.getImage().getHeight();
	}
	
	public double getWidth() {
		return this.getImage().getWidth();
		
	}
	
	public World getWorld() {
		return (World)this.getParent();
		
	}
	
	public <A extends Actor> java.util.List<A> getIntersectingObjects(java.lang.Class<A> cls) {
		List<A> arr = new ArrayList<A>();
		for (javafx.scene.Node n : getWorld().getChildren()) {
			if (cls.isInstance(n) && n.intersects(this.getBoundsInLocal())) {
				arr.add((A)n);
			}
		}
		return arr;		
	}
	
	public <A extends Actor> A getOneIntersectingObject(java.lang.Class<A> cls) {
		for (javafx.scene.Node n : getWorld().getChildren()) {
			if (cls.isInstance(n) && n.intersects(this.getBoundsInLocal())) {
				return ((A)n);
			}
		}
		return null;
	}
	
	public void move(double dx, double dy) {
		this.setX(this.getX() + dx);
		this.setY(this.getY() + dy);		
	}
	
}
