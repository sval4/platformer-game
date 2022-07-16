import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.JFileChooser;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class Map extends World {
	
	private final int[][][] blocks = {{{10, 12}, {10, 17}, {10, 18}, {10, 19}, {9, 18}, {9, 19}, {6, 22}, {10, 26}, {10, 27}, {9, 27}, {8, 27}, {10, 32}, {9, 32}, {8, 32}, {7, 32}, {10, 37}, {9, 37}, {8, 37}, {10, 42}, {9, 43}, {10, 43}, {9, 44}, {10, 44}, {10, 45}}, 
									  {{10, 12}, {10, 13}, {10, 14}, {10, 15}, {10, 18}, {9, 15}, {9, 18}, {8, 18}, {4, 18}, {7, 18}, {5, 24}, {10, 21}, {10, 22}, {10, 23}, {10, 24}, {10, 25}, {10, 26}, {10, 27}, {9, 21}, {9, 22}, {9, 23}, {9, 24}, {9, 25}, {9, 26}, {9, 27}, {8, 21}, {8, 27}, {7, 30}, {10, 33}, {9, 33}, {8, 33}, {7, 36}, {10, 39}, {9, 39}, {8, 39}, {10, 42}, {10, 43}, {10, 44}, {10, 45}, {9, 44}}};
	private final int[][][] spikes = {{{9, 12}, {8, 19}, {10, 24}, {10, 39}, {10, 40}, {8, 43}, {8, 44}}, {{9, 13}, {6, 18}, {8, 23}, {8, 25}, {7, 39}, {9, 42}, {10, 47}}};
	private final int[][] fakeblocks = {{22, 23, 28, 29, 30, 31, 33, 34, 35, 36}, {16, 17, 19, 20, 28, 29, 30, 31, 32, 34, 35, 36, 37, 38}};
	private final int[][] boulders = {{30}, {17, 33, 43}};
	
	private int[][] grid;
	int scrollX = 0;
	int previousX = 0;
	AnimationTimer timer;
	int count = 0;
	Door d;
	boolean onSpike = false;
	//private int level = 1;
	
	public Map(int level) {
		super();	
		Level l = new Level(blocks[level - 1], spikes[level - 1], fakeblocks[level - 1], null);
		grid = new int[15][60];
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 60; j++) {
				int[] a = {i, j};
				if (l.isFakeBlockRow(j) && i > 10) {
					grid[i][j] = 3; 
				} else if (l.isSpike(i, j)) {
					grid[i][j] = 2;
				} else if (l.isBlock(i, j)) {
					grid[i][j] = 1;
				} else if(i >= 4 && i < 11 && j > 9 && j < 50) {
					grid[i][j] = 0;
				} else {
					grid[i][j] = 1;
				}
			}
		}
		d = new Door(47);
		this.add(d);
	
		
	}
	
	public Map(File file) {
		super();
		int max = 0;
		//if(file != null) {
			try {
				Scanner s = new Scanner(file);
				String str = s.nextLine();
				int[][] b = new int[(str.length() - 7) / 6][2];
				System.out.println(str);
				for (int i = 0; i < str.length(); i++) {
					if (str.charAt(i) == '(') {
						System.out.println(str.charAt(i + 1));
						int[] t2;
						if (str.charAt(i + 3) == ',') {
							if (str.charAt(i + 5) == ')') {
								int[] t = {Integer.parseInt("" + str.charAt(i + 1) + str.charAt(i + 2)) + 4, Integer.parseInt("" + str.charAt(i + 4)) + 10};
								b[(i - 7) / 6] = t;
							} else {
								int[] t = {Integer.parseInt("" + str.charAt(i + 1) + str.charAt(i + 2)) + 4, Integer.parseInt("" + str.charAt(i + 4) + str.charAt(i + 5)) + 10};
								b[(i - 7) / 6] = t;
							}
						} else {
							if (str.charAt(i + 4) == ')') {
								int[] t = {Integer.parseInt("" + str.charAt(i + 1)) + 4, Integer.parseInt("" + str.charAt(i + 3)) + 10};
								b[(i - 7) / 6] = t;
							} else {
								int[] t = {Integer.parseInt("" + str.charAt(i + 1)) + 4, Integer.parseInt("" + str.charAt(i + 3) + str.charAt(i + 4)) + 10};
								b[(i - 7) / 6] = t;
							}

						}
					}
				}
				
				str = s.nextLine();
				int[] f = new int[(str.length() - 13) / 6 + 1];
				for (int i = 0; i < str.length(); i++) {
					if (str.charAt(i) == '(') {
						if (str.charAt(i + 3) == ',') {
							if (str.charAt(i + 5) == ')') {
								int t = Integer.parseInt("" + str.charAt(i + 4)) + 10;
								f[(i - 13) / 6] = t;
							} else {
								int t = Integer.parseInt("" + str.charAt(i + 4) + str.charAt(i + 5)) + 10;
								f[(i - 13) / 6] = t;
							}
						} else {
							if (str.charAt(i + 4) == ')') {
								int t = Integer.parseInt("" + str.charAt(i + 3)) + 10;
								f[(i - 13) / 6] = t;
							} else {
								int t = Integer.parseInt("" + str.charAt(i + 3) + str.charAt(i + 4)) + 10;
								f[(i - 13) / 6] = t;
							}

						}
					}
				}
				
				str = s.nextLine();
				int[][] sp = new int[(str.length() - 7) / 6][2];
				for (int i = 0; i < str.length(); i++) {
					if (str.charAt(i) == '(') {
						if (str.charAt(i + 3) == ',') {
							if (str.charAt(i + 5) == ')') {
								int[] t = {Integer.parseInt("" + str.charAt(i + 1) + str.charAt(i + 2)) + 4, Integer.parseInt("" + str.charAt(i + 4)) + 10};
								b[(i - 7) / 6] = t;
							} else {
								int[] t = {Integer.parseInt("" + str.charAt(i + 1) + str.charAt(i + 2)) + 4, Integer.parseInt("" + str.charAt(i + 4) + str.charAt(i + 5)) + 10};
								b[(i - 7) / 6] = t;
							}
						} else {
							if (str.charAt(i + 4) == ')') {
								int[] t = {Integer.parseInt("" + str.charAt(i + 1)) + 4, Integer.parseInt("" + str.charAt(i + 3)) + 10};
								b[(i - 7) / 6] = t;
							} else {
								int[] t = {Integer.parseInt("" + str.charAt(i + 1)) + 4, Integer.parseInt("" + str.charAt(i + 3) + str.charAt(i + 4)) + 10};
								b[(i - 7) / 6] = t;
							}

						}

					}
				}
				
				str = s.nextLine();
				int[] bo = new int[(str.length() - 9) / 6 + 1];
				for (int i = 0; i < str.length(); i++) {
					if (str.charAt(i) == '(') {
						if (str.charAt(i + 3) == ',') {
							if (str.charAt(i + 5) == ')') {
								int t = Integer.parseInt("" + str.charAt(i + 4)) + 10;
								f[(i - 9) / 6] = t;
							} else {
								int t = Integer.parseInt("" + str.charAt(i + 4) + str.charAt(i + 5)) + 10;
								f[(i - 9) / 6] = t;
							}
						} else {
							if (str.charAt(i + 4) == ')') {
								int t = Integer.parseInt("" + str.charAt(i + 3)) + 10;
								f[(i - 9) / 6] = t;
							} else {
								int t = Integer.parseInt("" + str.charAt(i + 3) + str.charAt(i + 4)) + 10;
								f[(i - 9) / 6] = t;
							}

						}
					}
				}
				max = 100;
				System.out.println(Arrays.deepToString(b));
				System.out.println(Arrays.deepToString(sp));
				System.out.println(Arrays.toString(f));
				System.out.println(Arrays.toString(bo));
				System.out.println(max);
				
				Level l = new Level(b, sp, f, bo);
				
				grid = new int[15][max + 20];
				for (int i = 0; i < 15; i++) {
					for (int j = 0; j < max + 20; j++) {
						int[] a = {i, j};
						if (l.isFakeBlockRow(j) && i > 10) {
							grid[i][j] = 3; 
						} else if (l.isSpike(i, j)) {
							grid[i][j] = 2;
						} else if (l.isBlock(i, j)) {
							grid[i][j] = 1;
						} else if(i >= 4 && i < 11 && j > 9 && j < max) {
							grid[i][j] = 0;
						} else {
							grid[i][j] = 1;
						}
					}
				}
				System.out.println(Arrays.deepToString(grid));
				d = new Door(max - 3);
				this.add(d);
			
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
	
		//}
		/*super();	
		Level l = new Level(blocks[level - 1], spikes[level - 1], fakeblocks[level - 1]);
		grid = new int[15][60];
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 60; j++) {
				int[] a = {i, j};
				if (l.isFakeBlockRow(j) && i > 10) {
					grid[i][j] = 3; 
				} else if (l.isSpike(i, j)) {
					grid[i][j] = 2;
				} else if (l.isBlock(i, j)) {
					grid[i][j] = 1;
				} else if(i >= 4 && i < 11 && j > 9 && j < 50) {
					grid[i][j] = 0;
				} else {
					grid[i][j] = 1;
				}
			}
		}
		d = new Door(47);
		this.add(d);
		*/
		
	}

	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		if (getWorldBoulder() == null) {
			for (int i : boulders[Game.getLevel() - 1]) {
				Boulder b = new Boulder();
				b.setY(70);
				b.setX((i - 8 - scrollX) * 20 + 160);
				this.add(b);
			}
			
		}
	}
	
	public int[][] getList(){
		return grid;
	}
	public void updateMap() {
		System.out.println(scrollX);
		for (int i = 0; i < grid.length; i++) {
			for (int j = scrollX; j < scrollX + 20; j++) {
				
				onSpike = scrollX == (j - 10) && grid[i][j] == 2;
				if (grid[i][j] == 2 && scrollX == (j - 10)) {
					Spike block = new Spike(j - 10);
					System.out.println("Hi");
					block.setY(i * 20 + 10);
					block.setX((j - scrollX) * 20);
					this.add(block);
				}
				if (i == 8) {
					if (scrollX < d.getScrollX() && scrollX > d.getScrollX() - 20) {
						d.setY(i * 20 + 5);
						d.setX((d.getScrollX() - scrollX) * 20);
						d.setVisible(true);
					} else {
						d.setVisible(false);
					}
				}
				if (previousX == scrollX) {
					if (grid[i][j] == 1) {
						Block block = new Block();
						block.setY(i * 20);
						block.setX((j - scrollX) * 20);
						this.add(block);
					} else if (grid[i][j] == 3) {
						FakeBlock block = new FakeBlock();
						block.setY(i * 20);
						block.setX((j - scrollX) * 20);
						this.add(block);
					}
				} else if (grid[i][j] != 0 && (grid[i][previousX - scrollX + j] == 0 || grid[i][previousX - scrollX + j] == 2)) {
					/*if (grid[i][j] == 2 && scrollX == 12) {
						Spike block = new Spike(j - 10);
						System.out.println(j - 10);
						block.setY(i * 20 + 10);
						block.setX((j - scrollX) * 20);
						this.add(block);
					} else {*/
					if (grid[i][j] == 1 && !onSpike) {
						Block block = new Block();
						block.setY(i * 20);
						block.setX((j - scrollX) * 20);
						this.add(block);
					}
					//}
				} else if (grid[i][j] == 3 && grid[i][previousX - scrollX + j] == 1) {
					removeAtPos((j - (scrollX)) * 20, i * 20);
					removeAtPos((j - (scrollX)) * 20, i * 20);
					FakeBlock block = new FakeBlock();
					block.setY(i * 20);
					block.setX((j - scrollX) * 20);
					this.add(block);
				} else if (grid[i][j] == 1 && grid[i][previousX - scrollX + j] == 3) {
					Block block = new Block();
					block.setY(i * 20);
					block.setX((j - scrollX) * 20);
					this.add(block);
				} else if (grid[i][j] == 2 && grid[i][previousX - scrollX + j] == 1) {
							
					removeAtPos((j - (scrollX)) * 20, i * 20);
					removeAtPos((j - (scrollX)) * 20, i * 20);
							
				} else if (grid[i][j] == 0 && grid[i][previousX - scrollX + j] != 0) {
					
					removeAtPos((j - (scrollX)) * 20, i * 20);
					removeAtPos((j - (scrollX)) * 20, i * 20);
					removeAtPos((j - (scrollX)) * 20, i * 20 + 10);
					removeAtPos((j - (scrollX)) * 20, i * 20 + 10);
					
				}

			}
		}
		previousX = scrollX;
	}
	
	public void scroll(int scroll) {
		if (scrollX + scroll >= 0 && scrollX + scroll <= grid[0].length - 20) {
			scrollX += scroll;
			//showSpikes(scrollX);
			getWorldBoulder().setX(((previousX - scrollX) * 20 + getWorldBoulder().getX()));
			updateMap();
		}
	}
	
	public void removeAtPos(int x, int y) {
		for (int k = 0; k < getChildren().size(); k++) {
			if (getChildren().get(k) instanceof ImageView && ((ImageView) getChildren().get(k)).getX() == x && ((ImageView) getChildren().get(k)).getY() == y) {
				if (getChildren().get(k) instanceof Block  || getChildren().get(k) instanceof Spike) {
					this.remove((Actor)getChildren().get(k));
					break;
				}
			}
		}
	}
	
	public void showSpikes(int x) {
		for (int k = 0; k < getChildren().size(); k++) {
			if (getChildren().get(k) instanceof Spike) {
				System.out.println("Hello");
				if (((Spike) getChildren().get(k)).getScrollX() == x) {
					((Spike) getChildren().get(k)).changeVisible();
					System.out.println("Hi");
					//System.out.println(getChildren().size());
					//break;
				}
			}
		}
	}
	
	public int getScrollX() {
		return scrollX;
	}
	
	public boolean contains(int[][] b, int[] a) {
		for (int[] o : b) {
			if (o[0] == a[0] && o[1] == a[1]) {
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(int[] b, int a) {
		for (int m : b) {
			if (m == a) {
				return true;
			}
		}
		return false;
	}
	
	public Boulder getWorldBoulder() {
		for (Node o : getChildren()) {
			if (o instanceof Boulder) {
				return (Boulder)o;
			}
		}
		return null;
	}

	
		
}
