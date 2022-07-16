import javafx.scene.image.Image;

public class Spike extends Actor {
	
	int scrollX; 
	
	public Spike(int scrollX) {
		setImage(new Image("file:images/spikes.jpeg", 20, 10, false, false));
		this.scrollX = scrollX;
		System.out.println(scrollX);
	}
	
	public int getScrollX() {
		return scrollX;
	}

	public void changeVisible() {
		// TODO Auto-generated method stub
		System.out.println("reeee");
		this.setImage(new Image("file:images/spikes.jpeg", 20, 10, false, false));
		
	}

	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		
	}

}
