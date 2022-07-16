import javafx.scene.image.Image;

public class Door extends Actor {
	
	private int scrollx;
	private int numComplete;
	
	public Door(int scrollX) {
		setImage(new Image("file:images/doorclose.png", 40, 55, false, false));
		scrollx = scrollX;
		numComplete = 0;
	}
	
	public int getScrollX() {
		return scrollx;
	}

	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		Player p = getOneIntersectingObject(Player.class);
		if (p != null) {
			setImage(new Image("file:images/dooropen.png", 40, 64, false, false));
			if (this.getX() - p.getX() < 5 && p.getY() - this.getY() > 30) {
				p.winanimation = true;
				numComplete++;
			}
		}
	}

}

