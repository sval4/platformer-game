import javafx.scene.image.Image;

public class Boulder extends Actor {

	private double dy = 0;
	public int count = 0;
	
	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		if (getY() + getHeight() >= getWorld().getHeight() /*|| getIntersectingObjects(Block.class).size() != 0*/) {
			count++;
			
		}
		if (count > 50) {
			getWorld().remove(this);
		}
		dy += 0.25;
		move(0, dy);
	}
	
	public Boulder() {
		setImage(new Image("file:images/boulder.png", 20, 20, false, false));
		
	}
	
}
