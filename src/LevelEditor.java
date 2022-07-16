import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LevelEditor extends World{
	Stage stage;
	Game game;
	ArrayList<String> worldStrings = new ArrayList<String>();

	ArrayList<ArrayList<ImageView>> mapImages = new ArrayList<ArrayList<ImageView>>();

	String held = "N";
	// KEY: BLOCK: D for dirt
	// SPIKE: S
	// BOULDER: B
	// FAKEBLOCK: F
	// AIR: A

	ImageView heldA = null;
	Group g;
	int yOffset = 0;
	int scrollX = 0;
	ArrayList<Object> noMove;

	int mapSize = 13;

	public LevelEditor(Stage s, Game g) {
		stage = s;
		game = g;
		
		
		this.requestFocus();
		this.start();

	}

	public void temp() {
		// y 0 to 30 and x 0 to 80 will be back to main menu button, y 260 to 300 is save, rest of that area is block selection, 
		
		// selection area will be from x 0 to x 70: 10, 20, 10, 20, 10
		// start map area at 100, end at 380

		g = new Group();
		
		ImageView background = new ImageView(new Image("file:images/levelEditorBackground.png", 400, 300, false, false));
		
		g.getChildren().add(background);
		
		Block blockIcon = new Block();
		blockIcon.setX(15);
		blockIcon.setY(40);
		g.getChildren().add(blockIcon);

		FakeBlock fakeBlockIcon = new FakeBlock();
		fakeBlockIcon.setX(45);
		fakeBlockIcon.setY(40);
		g.getChildren().add(fakeBlockIcon);

		Spike spikeIcon = new Spike(0);
		spikeIcon.setX(15);
		spikeIcon.setY(80);
		g.getChildren().add(spikeIcon);

		Boulder boulderIcon = new Boulder();
		boulderIcon.setX(45);
		boulderIcon.setY(70);
		g.getChildren().add(boulderIcon);
		
		ImageView airImage = new ImageView(new Image("file:images/air.png", 20, 20, false, false));
		airImage.setX(15);
		airImage.setY(100);
		g.getChildren().add(airImage);

		noMove = new ArrayList<Object>();

		noMove.add(blockIcon);
		noMove.add(fakeBlockIcon);
		noMove.add(spikeIcon);
		noMove.add(boulderIcon);
		

		//
		class EditorMouseHandler implements EventHandler<MouseEvent> {

			@Override
			public void handle(MouseEvent event) {
				double x = event.getX();
				double y = event.getY();
				// Mouse over area from x 80 to x 100 to scroll left
				// Mouse over area from x 380 to x 400 to scroll right
				if (x >= 80 && x <= 100) {
					if (scrollX > 0) {
						scrollX--;
						for (int i = 0; i < g.getChildren().size(); i++) {
							boolean skip = false;
							for (int j = 0; j < noMove.size(); j++) {
								if (g.getChildren().get(i) == noMove.get(j)) {
									skip = true;
								}
							}
							if (skip) {
								continue;
							}
							if (g.getChildren().get(i) != null && g.getChildren().get(i) instanceof ImageView && g.getChildren().get(i) != background && g.getChildren().get(i) != airImage) {
								((ImageView) g.getChildren().get(i))
										.setX(((ImageView) g.getChildren().get(i)).getX() + 20);
							}
						}
						updateMapVisibility();
					}
					/*
					 * for (int r = 0; r < mapImages.size(); r++) { for (int c = 0; c <
					 * mapImages.get(r).size(); c++) { if (mapImages.get(r).get(c) != null) {
					 * mapImages.get(r).get(c).setX(mapImages.get(r).get(c).getX() - 20); } } }
					 */

				} else if (x >= 380 && x <= 400) {
					if (scrollX < 100) {
						scrollX++;
						for (int i = 0; i < g.getChildren().size(); i++) {
							boolean skip = false;
							for (int j = 0; j < noMove.size(); j++) {
								if (g.getChildren().get(i) == noMove.get(j)) {
									skip = true;
								}
							}
							if (skip) {
								continue;
							}
							if (g.getChildren().get(i) != null && g.getChildren().get(i) instanceof ImageView && g.getChildren().get(i) != background  && g.getChildren().get(i) != airImage) {
								((ImageView) g.getChildren().get(i))
										.setX(((ImageView) g.getChildren().get(i)).getX() - 20);
							}
						}
						updateMapVisibility();
					}
					/*
					 * for (int r = 0; r < mapImages.size(); r++) {
					 * 
					 * for (int c = 0; c < mapImages.get(r).size(); c++) { if
					 * (mapImages.get(r).get(c) != null) {
					 * mapImages.get(r).get(c).setX(mapImages.get(r).get(c).getX() + 20); } }
					 * 
					 * 
					 * }
					 */

				}
				if (event.getEventType() == MouseEvent.MOUSE_CLICKED || (event.getButton() == MouseButton.SECONDARY
						&& event.getEventType() == MouseEvent.MOUSE_DRAGGED)) {
					if (x >= 80 && x <= 380 && heldA != null /* implicit held != "N" */) {
						// Clicked within building area with something
						int col = (((int) x) - 100) / 20;
						int row = ((int) y) / 20;
						if (col >= 0 && row >= 0 && row <= 14 && col <= mapSize) {
							if (col + scrollX + 1 >= worldStrings.size()) {
								mapSize = col + scrollX + 1;
								extendWorld(col + scrollX + 2);
							}
							g.getChildren().remove(mapImages.get(row).get(col + scrollX));
							mapImages.get(row).set(col + scrollX, null);
							if (!held.equals("A")) {
								ImageView a = copyHeld(heldA);
								a.setX(col * 20 + 100);
								a.setY(row * 20 + (yOffset % 20));
								g.getChildren().add(a);
								mapImages.get(row).set(col + scrollX, a);
							}
							worldStrings.set(row,
									worldStrings.get(row).substring(0, col + scrollX) + held + worldStrings.get(row)
											.substring(col + scrollX + 1, worldStrings.get(row).length()));
							// printWorldString();
						}

					} else if (x >= blockIcon.getX() && x <= blockIcon.getX() + blockIcon.getWidth()
							&& y >= blockIcon.getY() && y <= blockIcon.getY() + blockIcon.getHeight()) {
						setHeld("D");
						if (heldA != null) {
							g.getChildren().remove(heldA);
						}
						heldA = new Block();
						heldA.setX(event.getX() - 10);
						heldA.setY(event.getY() - 10);
						g.getChildren().add(heldA);
						yOffset = 0;
					} else if (x >= fakeBlockIcon.getX() && x <= fakeBlockIcon.getX() + fakeBlockIcon.getWidth()
							&& y >= fakeBlockIcon.getY() && y <= fakeBlockIcon.getY() + fakeBlockIcon.getHeight()) {
						setHeld("F");
						if (heldA != null) {
							g.getChildren().remove(heldA);
						}
						heldA = new FakeBlock();
						heldA.setX(event.getX() - 10);
						heldA.setY(event.getY() - 10);
						g.getChildren().add(heldA);
						yOffset = 0;
					} else if (x >= spikeIcon.getX() && x <= spikeIcon.getX() + spikeIcon.getWidth()
							&& y >= spikeIcon.getY() - 10 && y <= spikeIcon.getY() + spikeIcon.getHeight()) {
						setHeld("S");
						if (heldA != null) {
							g.getChildren().remove(heldA);
						}
						heldA = new Spike(0);
						heldA.setX(event.getX() - 10);
						heldA.setY(event.getY() - 10);
						g.getChildren().add(heldA);
						yOffset = 10;
					} else if (x >= boulderIcon.getX() && x <= boulderIcon.getX() + boulderIcon.getWidth()
							&& y >= boulderIcon.getY() && y <= boulderIcon.getY() + boulderIcon.getHeight()) {
						setHeld("B");
						if (heldA != null) {
							g.getChildren().remove(heldA);
						}
						heldA = new Boulder();
						heldA.setX(event.getX() - 10);
						heldA.setY(event.getY() - 10);
						g.getChildren().add(heldA);
						yOffset = 0;
					} else if (x >= airImage.getX() && x <= airImage.getX() + 20 && y >= airImage.getY() && y <= airImage.getY() + 20) {
						setHeld("A");
						if (heldA != null) {
							g.getChildren().remove(heldA);
						}
						heldA = copyHeld(airImage);
						heldA.setX(event.getX() - 10);
						heldA.setY(event.getY() - 10);
						g.getChildren().add(heldA);
						yOffset = 0;
					} else if (x >= 0 && x <= 80 && y >= 260 && y <= 300) {
						// SAVE
						System.out.println("SAVE");
						String blocks = "Blocks: ";
						String fakeBlocks = "Fake Blocks: ";
						String spikes = "Spikes: ";
						String boulders = "Boulders: ";
						for (int r = 0; r < worldStrings.size(); r++) {
							for (int c = 0; c < worldStrings.get(r).length(); c++) {
								char letter = worldStrings.get(r).charAt(c);
								if (letter == 'D') {
									blocks += "(" + r + "," + c + ") ";
								} else if (letter == 'F') {
									fakeBlocks += "(" + r + "," + c + ") ";
								} else if (letter == 'S') {
									spikes += "(" + r + "," + c + ") ";
								} else if (letter == 'B') {
									boulders += "(" + r + "," + c + ") ";
								}
							}
						}
						//
						JFileChooser chooser = new JFileChooser();
						chooser.setCurrentDirectory(new File("/maps"));
						chooser.showSaveDialog(null);
						File a = chooser.getSelectedFile();
						FileWriter writer = null;
						if(a != null) {
							try {
								writer = new FileWriter(chooser.getSelectedFile());
							} catch (IOException e) {
								e.printStackTrace();
								return;
							}
							try {
								writer.write(blocks + "\r\n");
								writer.write(fakeBlocks + "\r\n");
								writer.write(spikes + "\r\n");
								writer.write(boulders + "\r\n");
							} catch (IOException e) {
								try {
									writer.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								e.printStackTrace();
								return;
							}
							
							try {
								writer.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
					} else if (x >= 0 && x <= 80 && y >= 0 && y <= 30) {
						game.changeState(0);
					}
				}
				if (event.getEventType() == MouseEvent.MOUSE_MOVED
						|| event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					if (heldA != null) {
						heldA.setX(event.getX() - 10);
						heldA.setY(event.getY() - 10 + yOffset);
					}
				}
			}

		}
		

		class EditorKeyboardHandler implements EventHandler<KeyEvent> {

			@Override
			public void handle(KeyEvent event) {
				KeyCode code = event.getCode();
				if (code.equals(KeyCode.M)) {
					game.changeState(0);
				}
				if (code.equals(KeyCode.ESCAPE)) {
					g.getChildren().remove(heldA);
					heldA = null;
					held = "N";
				}
				if (code.equals(KeyCode.RIGHT) && scrollX < 100) {
					scrollX++;
					for (int i = 0; i < g.getChildren().size(); i++) {
						boolean skip = false;
						for (int j = 0; j < noMove.size(); j++) {
							if (g.getChildren().get(i) == noMove.get(j)) {
								skip = true;
							}
						}
						if (skip) {
							continue;
						}
						if (g.getChildren().get(i) != null && g.getChildren().get(i) instanceof ImageView  && g.getChildren().get(i) != background && g.getChildren().get(i) != airImage) {
							((ImageView) g.getChildren().get(i)).setX(((ImageView) g.getChildren().get(i)).getX() - 20);
						}
					}
					updateMapVisibility();
				}
				if (code.equals(KeyCode.LEFT) && scrollX > 0) {
					scrollX--;
					for (int i = 0; i < g.getChildren().size(); i++) {
						boolean skip = false;
						for (int j = 0; j < noMove.size(); j++) {
							if (g.getChildren().get(i) == noMove.get(j)) {
								skip = true;
							}
						}
						if (skip) {
							continue;
						}
						if (g.getChildren().get(i) != null && g.getChildren().get(i) instanceof ImageView && g.getChildren().get(i) != background && g.getChildren().get(i) != airImage) {
							((ImageView) g.getChildren().get(i)).setX(((ImageView) g.getChildren().get(i)).getX() + 20);
						}
					}
					updateMapVisibility();
				}
			}

		}

		for (int r = 0; r < 15; r++) {
			ArrayList<ImageView> l = new ArrayList<ImageView>();
			for (int c = 0; c < 14; c++) {
				l.add(null);
			}
			mapImages.add(l);
		}

		for (int i = 0; i < 15; i++) {
			worldStrings.add(new String("AAAAAAAAAAAAAAA"));
		}

		ImageView temp = new ImageView(new Image("file:images/block.png", 20, 20, false, false));
		temp.setX(100);
		temp.setY(100);

		g.getChildren().add(temp);

		Scene editorScene = new Scene(g, 400, 300);
		EditorMouseHandler h = new EditorMouseHandler();
		editorScene.setOnMouseClicked(h);
		editorScene.setOnMouseMoved(h);
		editorScene.setOnMouseDragged(h);
		editorScene.setOnKeyPressed(new EditorKeyboardHandler());
		stage.setScene(editorScene);
	}

	public ImageView copyHeld(ImageView i) {
		ImageView copy = new ImageView(i.getImage());
		copy.setX(i.getX());
		copy.setY(i.getY());
		return copy;
	}

	public void setHeld(String s) {
		held = s;
	}

	// k is the total length of the world after extension
	public void extendWorld(int k) {
		while (worldStrings.get(0).length() < k) {
			for (int i = 0; i < mapImages.size(); i++) {
				mapImages.get(i).add(null);
				worldStrings.set(i, worldStrings.get(i) + "A");
			}
		}
	}

	public void updateMapVisibility() {
		for (int r = 0; r < mapImages.size(); r++) {
			for (int c = 0; c < mapImages.get(r).size(); c++) {
				ImageView view = mapImages.get(r).get(c);
				if (view != null) {
					if (view.getX() <= 80 || view.getX() >= 370) {
						view.setVisible(false);
					} else {
						view.setVisible(true);
					}
				}
			}
		}
		/*
		 * for (int r = 0; r < mapImages.size(); r++) { for (int c = 0; c < scrollX;
		 * c++) { if (c > mapSize) { break; } ImageView view = mapImages.get(r).get(c);
		 * if (view != null) { view.setVisible(false); } } for (int c = scrollX; c <
		 * mapImages.size() - 1; c++) { ImageView view = mapImages.get(r).get(c); if
		 * (view != null) { view.setVisible(true); } } } for (int r = 0; r <
		 * mapImages.size(); r++) { if (scrollX + 13 > mapSize) { break; } ImageView
		 * view = mapImages.get(r).get(scrollX + 13); if (view != null) {
		 * view.setVisible(false); } }
		 */
		/*
		 * for (int r = 0; r < mapImages.size(); r++) { for (int c = 0; c <
		 * mapImages.get(r).size(); c++) { ImageView view = mapImages.get(r).get(c); if
		 * (view != null && view.getX() < 80) { view.setVisible(false); } else if (view
		 * != null) { view.setVisible(true); } } }
		 */
	}

	// Testing code
	public void printWorldString() {
		for (int i = 0; i < worldStrings.size(); i++) {
			System.out.println(worldStrings.get(i));
		}
		System.out.println("-----------");
	}
	

	

	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		
	}
}
