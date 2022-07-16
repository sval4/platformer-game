import javafx.scene.image.Image;

public class FakeBlock extends Actor {

	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		if (getOneIntersectingObject(Player.class) != null) {
			getWorld().remove(this);
		}
	}

	public FakeBlock() {
		setImage(new Image("file:images/dirtblock.png", 20, 20, false, false));
	}
	
	
}
