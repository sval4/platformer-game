

import java.io.File;

import javax.swing.JFileChooser;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Game extends Application{
	
	Map map;
	Player p;
	Image[] animations;
	static final double moveDistance = 5;
	Group g;
	AnimationTimer timer;
	long delay = 0;
	long previousTime;
	int counterUp = 0;
	int counterDown = 0;
	boolean left = true;
	boolean right = true;
	int deathCounter = 0;
	Text deathCounterText;
	final double MAP_WIDTH = 400;
	final double MAP_HEIGHT = 300;
	static int level = 1;
	Media media;
	MediaPlayer mediaPlayer;
	MediaView mediaView;
	boolean notPaused = true;
	Text levelText;
	public boolean custom = false;
	File selectedFile = null;
	double currNum = 50;
	
	// 0 - Main Menu
	// 1 - Playing
	// 2 - Pause
	// 3 - ???
	int gameState = 0;
	
	Stage stage;
	
	Scene mainGameScene;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage st) throws Exception {
		stage = st;
		media = new Media(new File("music/Firebrand.mp3").toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		mediaView = new MediaView(mediaPlayer);
		mediaPlayer.setVolume(0.5);
		
		previousTime = 0;
		st.setTitle("Trap Adventure");
		map = new Map(level);
		map.setPrefSize(MAP_WIDTH, MAP_HEIGHT);
		g = new Group(map);
		mainGameScene = new Scene(g, MAP_WIDTH, MAP_HEIGHT);
		st.setScene(mainGameScene);
		map.updateMap();
		p = new Player();
		p.setX(200);
		p.setY(180);
		map.add(p);
		
		timer = new AnimationHandler(st);
		
		changeState(gameState);

		//
		map.setOnKeyPressed(new KeyboardHandler());
		map.setOnKeyReleased(new ReleaseKeyboardHandler());
		
		
		st.show();
	}
	
	public void restart(Stage st) {
		map = null;
		g = null;
		deathCounterText = null;
		levelText = null;
		p = null;
		if (!custom) {
			map = new Map(level);
		} else {
			if(loadFile() != null) {
				map = new Map(selectedFile);
			}else {
				changeState(0);
			}
		}
		if(selectedFile == null && gameState == 1) {
			map.setPrefSize(MAP_WIDTH, MAP_HEIGHT);
			g = new Group(map);
			mainGameScene = new Scene(g, MAP_WIDTH, MAP_HEIGHT);
			st.setScene(mainGameScene);
			map.updateMap();
			
			
			deathCounterText = new Text();
			deathCounterText.setText("Death Count: " + deathCounter);
			deathCounterText.setFont(new Font(20));
			deathCounterText.setX(5);
			deathCounterText.setY(20);
			map.getChildren().add(deathCounterText);
			
			levelText = new Text();
			levelText.setText("Level " + level);
			levelText.setFont(new Font(20));
			levelText.setX(map.getWidth() - 75);
			levelText.setY(20);
			map.getChildren().add(levelText);
			
			p = new Player();
			p.setX(200);
			p.setY(180);
			map.add(p);
			System.out.println(map.getChildren());
			
			map.setOnKeyPressed(new KeyboardHandler());
			map.setOnKeyReleased(new ReleaseKeyboardHandler());
			
			map.requestFocus();
			map.start();
		}
	}
	
	public static void nextLevel() {
		level++;
	}
	
	public static int getLevel() {
		return level;
	}
	
	public File loadFile() {
		JFileChooser chooser = new JFileChooser();
		//chooser.setCurrentDirectory(new File("/maps"));
		chooser.showOpenDialog(null);
		selectedFile = chooser.getSelectedFile();
		return chooser.getSelectedFile();
		
	}
	
	public void changeState(int state) {
		gameState = state;
		switch (gameState) {
		case 0:
			// Title
			mediaPlayer.pause();
			mediaPlayer.play();
			left = true;
			right = true;
			map.stop();
			timer.stop();
			ImageView titleImage = new ImageView(new Image("file:images/titleV2.png", 400, 300, false, false));
			Group titleGroup = new Group(titleImage);
			Scene titleScene = new Scene(titleGroup, 400, 300);
			titleScene.setOnMouseClicked(new MouseHandler());
			titleScene.setOnKeyPressed(new KeyboardHandler());
			stage.setScene(titleScene);
			break;
		case 1:
			// Playing
			stage.setScene(mainGameScene);
			map.start();
			map.requestFocus();
			timer.start();
			if(notPaused)
			restart(stage);
			break;
		case 2:
			// Paused
			notPaused = false;
			left = true;
			right = true;
			map.stop();
			timer.stop();
			ImageView pauseLayer = new ImageView(new Image("file:images/pauseScrnTemp.png", 400, 300, false, false));
			pauseLayer.setOpacity(0.9);	
			Group pauseGroup = new Group(pauseLayer);
			Scene pauseScene = new Scene(pauseGroup, 400, 300);
			pauseScene.setOnKeyPressed(new KeyboardHandler());
			stage.setScene(pauseScene);
			break;
		case 3:
			// Accessible from main menu
			new LevelEditor(stage, this).temp();
			break;
		}

	}
	
	
	private class ReleaseKeyboardHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent event) {
			if(map.isKeyDown(KeyCode.LEFT) && event.getCode().equals(KeyCode.LEFT)) {
				map.removeKey(KeyCode.LEFT);
				left = true;
			}
			if(map.isKeyDown(KeyCode.RIGHT) && event.getCode().equals(KeyCode.RIGHT)) {
				map.removeKey(KeyCode.RIGHT);
				right = true;
			}
			if(map.isKeyDown(KeyCode.UP) && event.getCode().equals(KeyCode.UP)) {
				map.removeKey(KeyCode.UP);
			}
			if(left && right) {
				p.count = 0;
				p.setImage(new Image("file:images/stand.png", 20, 20, false, false));
			}
		}
		
	}
	
	private class MouseHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent m) {
			System.out.println(m.getX() + ", " + m.getY());
			if (gameState == 0) {
				if (m.getX() >= 20 && m.getX() <= 140 && m.getY() >= 240 && m.getY() <= 280) {
					custom = false;
					System.out.println("clicked play");
					changeState(1);
				}else if(m.getX() >= 152 && m.getX() <= 303 && m.getY() >= 240 && m.getY() <= 282) {
					changeState(3);
				}else if(m.getX() >= 7 && m.getX() <= 88 && m.getY() >= 7 && m.getY() <= 47) {
					Text t = new Text(0, 25, "Right arrow to move right\nLeft arrow to move left\nUp arrow to jump\nM key to go to title screen\nP key to pause the game\n(pause only works when the player is on the ground)\n\nAVOID THE TRAPS AT ALL COST!\n\n In game volume:");
					Slider slider = new Slider();
					slider.setPrefWidth(150);
					slider.setPrefHeight(100);
					slider.setTranslateX(50);
					slider.setTranslateY(185);
					
					slider.setMin(0);
					slider.setMax(100);
					slider.setValue(currNum);
					slider.setShowTickLabels(true);
					slider.setShowTickMarks(true);
					slider.setMajorTickUnit(10);
					slider.setMinorTickCount(0);
					slider.setBlockIncrement(10);
					
					slider.valueProperty().addListener(new SliderHandler());
					Group group = new Group();
					group.getChildren().add(t);
					group.getChildren().add(slider);
					Stage s = new Stage();
					Scene scene = new Scene(group, 300, 300);
					s.setTitle("Settings");
					s.setScene(scene);
					s.show();

					
				} else if(m.getX() >= 280 && m.getX() <= 395 && m.getY() >= 8 && m.getY() <= 50) {
					custom = true;
					changeState(1);
				}
			}
		}
		
	}
	
	private class SliderHandler implements ChangeListener<Number>{

		@Override
		public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
			// TODO Auto-generated method stub
			mediaPlayer.setVolume(((double) arg2 / 100.0));
			currNum = (double)arg2;
		}
		
	}
	
	private class KeyboardHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent event) {
			if(gameState == 1) {
				if (event.getCode().equals(KeyCode.UP)) {
					map.addKey(KeyCode.UP);	
				}
				if (event.getCode().equals(KeyCode.RIGHT)) {
					right = false;
					map.addKey(KeyCode.RIGHT);
				} else if (event.getCode().equals(KeyCode.LEFT)) {
					left = false;
					map.addKey(KeyCode.LEFT);
				}
			}

			if(event.getCode().equals(KeyCode.SPACE)) {
				System.out.println(p.jumping);
			}
			//The player can only pause if they are on the ground and not dead
			if (event.getCode().equals(KeyCode.P) && gameState == 1 && !p.jumping && !p.dead) {
				mediaPlayer.pause();
				map.removeKey(KeyCode.LEFT);
				map.removeKey(KeyCode.RIGHT);
				map.removeKey(KeyCode.UP);
				p.count = 0;
				p.setImage(new Image("file:images/stand.png", 20, 20, false, false));
				changeState(2);
			} else if (event.getCode().equals(KeyCode.P) && gameState == 2) {
				mediaPlayer.play();
				changeState(1);
			}
			if (event.getCode().equals(KeyCode.M) && gameState != 0 /* TEMPORARY, CHANGE FROM L */) {
				changeState(0);
				deathCounter = 0;
				level = 1;
			}
		}
	}
	
	private class AnimationHandler extends AnimationTimer{
		Stage st;
		public AnimationHandler(Stage s) {
			st = s;
		}
		@Override
		public void handle(long now) {
			if((now - previousTime) >= delay) {
				//move to the next level
				if(left && right) {
					p.count = 0;
					p.setImage(new Image("file:images/stand.png", 20, 20, false, false));
				}
				if(p.jumping) {
					p.setImage(new Image("file:images/walk2.png", 20, 20, false, false));
				}				
				previousTime = now;
				if(p.win) {
					notPaused = true;
					level++;
					levelText.setText("Level " + level);
					if (level < 3) {
						restart(st);
					} else {
						level = 1;
						changeState(0);
					}
				}
				if(p.dead) {
					notPaused = true;
					deathCounter++;
					deathCounterText.setText("Death Count: " + deathCounter);
					/*GaussianBlur g = new GaussianBlur();
					g.setRadius(50);
					p.setEffect(g);*/
					restart(st);
				}
		    }
		}
	}
	
}

