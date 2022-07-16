import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;


public class Player extends Actor{
	
	boolean canMove = true;
	int count = 0;
	Image[] animations;
	private double dx = 0;
	private double dy = 0;
	public boolean jumping = true;
	public boolean dead = false;
	public boolean deadanimation = false;
	public boolean winanimation = false;
	boolean once = true;
	int countMoveRight = 0;
	int countMoveLeft = 0;
	int leftNum = 0;
	int rightNum = 0;
	int yvel = 10;
	public boolean win = false;
	
	public Player() {
		animations = new Image[6];
		animations[0] = new Image("file:images/walk1.png", 20, 20, false, false);
		animations[1] = new Image("file:images/walk2.png", 20, 20, false, false);
		animations[2] = new Image("file:images/walk3.png", 20, 20, false, false);
		animations[3] = new Image("file:images/walk4.png", 20, 20, false, false);
		animations[4] = new Image("file:images/walk5.png", 20, 20, false, false);
		animations[5] = new Image("file:images/walk6.png", 20, 20, false, false);
		setImage(new Image("file:images/stand.png", 20, 20, false, false));
	}

	@Override
	public void act(long now) {
		if (getY() + getHeight() > getWorld().getHeight() + 20) {
			dead = true;
		}
		if (winanimation) {
			if (yvel > -11) {
				setImage(new Image("file:images/walk2.png", 20, 20, false, false));
				move(0, -yvel);
				yvel--;
			} else {
				this.setVisible(false);
				winanimation = false;
				win = true;
				getWorld().remove(this);
			}
		} else if (deadanimation) {
			if (getY() + getHeight() - 20 < getWorld().getHeight()) {
				move(0, -yvel);
				yvel--;
			}
		} else if (!dead) {
			Boulder bo = getOneIntersectingObject(Boulder.class);
			if (bo != null) {
				deadanimation = true;
			}
			canMove = true;
			if (getWorld().isKeyDown(KeyCode.UP)) {
				boolean canJump = false;
				// Find if the player intersects a block that is lower than them and not to the left or right of them (to stop wall climb)
				for(int i = 0; i < getIntersectingObjects(Block.class).size(); i++) {
					Block b = getIntersectingObjects(Block.class).get(i);
					if (b.getY() > getY() && b.getX() + b.getWidth() > getX() && b.getX() < getX() + getWidth()) {
						canJump = true;
					}
				}
				// If at least one block that meets the above conditions are met, jump.
				if (canJump) {
					jump();
				}
				
			}
			if(getWorld().isKeyDown(KeyCode.LEFT)) {
				leftNum++;
				for(int i = 0; i < getIntersectingObjects(Block.class).size(); i++) {
					if(getIntersectingObjects(Block.class).get(i).getY() <= getY() &&
							(getIntersectingObjects(Block.class).get(i).getY() + getIntersectingObjects(Block.class).get(i).getImage().getHeight()) >= getY() &&
							getIntersectingObjects(Block.class).get(i).getX() <= (getX() - getImage().getWidth()) && (getIntersectingObjects(Block.class).get(i).getX() + getIntersectingObjects(Block.class).get(i).getImage().getWidth()) >= (getX() - getImage().getWidth())) {
						canMove = false;
						break;
					}else {
						canMove = true;
					}
				}
				
				for (int i = 0; i < getIntersectingObjects(Block.class).size(); i++) {
					Block b = getIntersectingObjects(Block.class).get(i);
					// if (future pos places left edge of character inside a block AND the block is also vertically within the player, stop the player from moving (THIS FIXES ISSUES WITH HEAD BUMP / RISE TO TOP OF FLOOR WHEN HITTING EDGE OF BLOCK) )
					if (getX() - 1 < b.getX() + b.getWidth() && getX() - 1 > b.getX() &&
							((b.getY() < getY() + getHeight() && b.getY() > getY()) || (b.getY() + b.getHeight() > getY() && b.getY() + b.getHeight() < getY() + getHeight()))) {
						canMove = false;
					}
				}
				setScaleX(-1);
				if(!jumping) {
					setImage(animations[count / 3]);
					count++;
					if(count >= 18) {
						count = 0;
					}
				}
				if(canMove) {
					if(jumping) {
						if(countMoveLeft <= 6 && leftNum % 3 == 0) {
							((Map) getWorld()).scroll(-1);
							countMoveLeft++;
						}
					}else {
						if(leftNum % 3 == 0) {
							((Map) getWorld()).scroll(-1);
						}
					}
				}
			}else if(getWorld().isKeyDown(KeyCode.RIGHT)) {
				rightNum++;
				for(int i = 0; i < getIntersectingObjects(Block.class).size(); i++) {
					if(getIntersectingObjects(Block.class).get(i).getY() <= getY() &&
							(getIntersectingObjects(Block.class).get(i).getY() + getIntersectingObjects(Block.class).get(i).getImage().getHeight()) >= getY() &&
							getIntersectingObjects(Block.class).get(i).getX() <= (getX() + getImage().getWidth()) &&
							(getIntersectingObjects(Block.class).get(i).getX() + getIntersectingObjects(Block.class).get(i).getImage().getWidth()) >= (getX() + getImage().getWidth())) {
						canMove = false;
						break;
					}else {
						canMove = true;
					}
				}
				
				for (int i = 0; i < getIntersectingObjects(Block.class).size(); i++) {
					Block b = getIntersectingObjects(Block.class).get(i);
					// if (future pos places right edge of character inside a block AND the block is also vertically within the player), stop the player from moving
					if (getX() + getWidth() + 1 > b.getX() && getX() + getWidth() + 1 < b.getX() + b.getWidth() &&
							((b.getY() < getY() + getHeight() && b.getY() > getY()) || (b.getY() + b.getHeight() > getY() && b.getY() + b.getHeight() < getY() + getHeight()))) {
						canMove = false;
					}
				}
				setScaleX(1);
				if(!jumping) {
					setImage(animations[count / 3]);
					count++;
					if(count >= 18) {
						count = 0;
					}
				}
				if(canMove) {
					if(jumping) {
						if(countMoveRight <= 6 && rightNum % 3 == 0) {
							((Map) getWorld()).scroll(1);
							countMoveRight++;
						}
					}else {
						if(rightNum % 3 == 0) {
							((Map) getWorld()).scroll(1);
						}
					}
				}
			}
			for (int i = 0; i < getIntersectingObjects(Block.class).size(); i++) {
				Block b = getIntersectingObjects(Block.class).get(i);
				if (b.getY() + b.getHeight() > getY() && b.getY() < getY() + getHeight() && b.getX() + b.getWidth() > getX()
						&& b.getX() < getX() + getWidth()) {
					setY(b.getY() + b.getHeight());
					dy = 0;
					//System.out.println("head bumped");
				}
			}
			move(dx, -dy);
			boolean bBelow = false;
			for (int i = 0; i < getIntersectingObjects(Block.class).size(); i++) {
				Block b = getIntersectingObjects(Block.class).get(i);
				if (b.getY() > getY() && b.getX() + b.getWidth() > getX() && b.getX() < getX() + getWidth()) {
					bBelow = true;
				}
			}
			if (!bBelow) {
				dy -= 0.5;
			} else {
				dy = 0;
				// Fall if you aren't touching a block
				if (getIntersectingObjects(Block.class).size() == 0) {
					//System.out.println("test");
					dy -= 0.5;
				} else if(!dead){
					boolean blockBelow = false;
					for (int i1 = 0; i1 < getIntersectingObjects(Block.class).size(); i1++) {
						Block b = getIntersectingObjects(Block.class).get(i1);
						if (b.getY() > getY() && b.getX() + b.getWidth() > getX() && b.getX() < getX() + getWidth()) {
							blockBelow = true;
						}
					}
					if (blockBelow) {
						dy = 0;
						jumping = false;
						countMoveRight = 0;
						countMoveLeft = 0;
						if(once) {
							setImage(new Image("file:images/stand.png", 20, 20, false, false));
							once = false;
						}
					} else {
						dy -= 0.5;
					}
					move (dx, -dy);
					for (int i = 0; i < getIntersectingObjects(Block.class).size(); i++) {
						Block b = getIntersectingObjects(Block.class).get(i);
						// If the player intersects a block above their feet but below their head, push
						// them to the top of the block.
						if (b.getY() < getY() + getHeight() && b.getY() > getY() && b.getX() + b.getWidth() > getX()
								&& b.getX() < getX() + getWidth()) {
							setY(b.getY() - getHeight());
							dy = 0;
						}
					}
					for (int i = 0; i < getIntersectingObjects(Spike.class).size(); i++){
						Spike b = getIntersectingObjects(Spike.class).get(i);
						if (b instanceof Spike && getX() >= b.getX() && (getX() + getWidth()) <= (b.getX() + b.getWidth())) {
							deadanimation = true;
						}
					}
				}
			}
		}
	}

	public void jump() {
		jumping = true;
		setImage(new Image("file:images/walk2.png", 20, 20, false, false));
		dy = 7;
		once = true;
	}

	public void changeDx(int dx) {
		this.dx = dx;
	}

	public void move(double dx, double dy) {
		this.setX(this.getX() + dx);
		this.setY(this.getY() + dy);
	}

}