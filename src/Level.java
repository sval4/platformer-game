
public class Level {

	private int[][] blocks;
	private int[][] spikes;
	private int[] fakeblocks;
	private int[] boulders;
	//private int door;
	
	public Level(int[][] b, int[][] s, int[] f, int[] bo) {
		boulders = bo;
		spikes = s;
		//door = d;
		blocks = b;
		fakeblocks = f;
	}
	
	public boolean isBlock(int i, int j) {
		for (int[] o : blocks) {
			if (o[0] == i && o[1] == j) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isSpike(int i, int j) {
		for (int[] o : spikes) {
			if (o[0] == i && o[1] == j) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isFakeBlockRow(int r) {
		for (int o : fakeblocks) {
			if (o == r) {
				return true;
			}
		}
		return false;
	}
	
	
	
}
