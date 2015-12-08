package caverunner;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.scene.image.Image;
import java.nio.file.Paths;
import java.io.File;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;
import javafx.scene.input.MouseEvent;
import java.io.*;
import javafx.scene.media.AudioClip;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.Iterator;

enum ItemTypes {
   GOLD,
   BOMB,
   TRAP
   }

enum MovieType {
	RUNNING,
	STANDING,
	FACING_YOU,
	HANGING_ON_LADDER,
	CLIMBING_DOWN,
	CLIMBING_UP,
	FALLING,
	LEAPING_ONTO_LADDER,
	LEAPING_OFF_OF_LADDER,
	FIRING_RAYGUN,
	RAYGUN_BLAST,
	HANGING_ON_ROPE,
	TROLL_RUNNING
	}

class MovieImage {
	MovieType movieType;
	Image theImage;
	MovieImage nextImage;
	int xDelta, yDelta;

	MovieImage (Image image, MovieType type, MovieImage next, int x, int y) {
		movieType = type;
		theImage = image;
		nextImage = next;
		xDelta = x;
		yDelta = y;
		}
	}

class Images {
	static MovieImage RunnerRunning;
	static MovieImage RunnerTurned;
	static MovieImage RunnerFacing;
	static MovieImage RunnerLeapingOntoLadder;
	static MovieImage RunnerLeapingOffOfLadder;
	static MovieImage firingRaygun;
	static MovieImage raygunBlast;
	static MovieImage RunnerHangingOnLadder;
	static MovieImage RunnerClimbingDown;
	static MovieImage RunnerClimbingUp;
	static MovieImage RunnerFalling;
	static MovieImage RunnerHangingOnRope;
	static MovieImage R_TrollRunning;
	static Image white;
	static Image mossWorld;
	static Image L_digable_surface_on_nothing;
	static Image R_digable_surface_on_nothing;
	static Image C_digable_surface_on_nothing1;
	static Image C_digable_surface_on_nothing2;
	static Image single_digable_surface_on_nothing;
	static Image visableLadder;
	static Image hiddenLadder;
	static Image rope;
	static Image gold1;
	static Image exit;
	static Image eraser;
         
	static void InitImages () {
		// any MovieImage past into the MovieImage constructor should be already initialized before it is used or it will be null
		MovieImage theLastImage;

		// load the troll running right images into a movie loop
		R_TrollRunning = new MovieImage (new Image ("file:Images/R_troll_running_1.png"), MovieType.TROLL_RUNNING, null, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/R_troll_running_11.png"), MovieType.TROLL_RUNNING, R_TrollRunning, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/R_troll_running_10.png"), MovieType.TROLL_RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/R_troll_running_9.png"), MovieType.TROLL_RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/R_troll_running_8.png"), MovieType.TROLL_RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/R_troll_running_7.png"), MovieType.TROLL_RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/R_troll_running_6.png"), MovieType.TROLL_RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/R_troll_running_5.png"), MovieType.TROLL_RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/R_troll_running_4.png"), MovieType.TROLL_RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/R_troll_running_3.png"), MovieType.TROLL_RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/R_troll_running_2.png"), MovieType.TROLL_RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		R_TrollRunning.nextImage = theLastImage;
		
		// load the runner images for running into a movie loop
		RunnerRunning = new MovieImage (new Image ("file:Images/runner_running_1.png"), MovieType.RUNNING, null, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_11.png"), MovieType.RUNNING, RunnerRunning, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_10.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_9.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_8.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_7.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_6.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_5.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_4.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_3.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_2.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		RunnerRunning.nextImage = theLastImage;
		
		// load the runner images for hanging on rope into a movie loop
		RunnerHangingOnRope = new MovieImage (new Image ("file:Images/runner_on_rope_1.png"), MovieType.HANGING_ON_ROPE, null, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_on_rope_6.png"), MovieType.HANGING_ON_ROPE, RunnerHangingOnRope, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_on_rope_5.png"), MovieType.HANGING_ON_ROPE, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_on_rope_4.png"), MovieType.HANGING_ON_ROPE, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_on_rope_3.png"), MovieType.HANGING_ON_ROPE, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_on_rope_2.png"), MovieType.HANGING_ON_ROPE, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		RunnerHangingOnRope.nextImage = theLastImage;
		
		// load the runner falling images into a movie loop
		RunnerFalling = new MovieImage (new Image("file:Images/runner_falling_1.png"), MovieType.FALLING, null, 0, CONSTANTS.FALLING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_falling_5.png"), MovieType.FALLING, RunnerFalling, 0, CONSTANTS.FALLING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_falling_4.png"), MovieType.FALLING, theLastImage, 0, CONSTANTS.FALLING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_falling_1.png"), MovieType.FALLING, theLastImage, 0, CONSTANTS.FALLING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_falling_3.png"), MovieType.FALLING, theLastImage, 0, CONSTANTS.FALLING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_falling_2.png"), MovieType.FALLING, theLastImage, 0, CONSTANTS.FALLING_FRAME_DISTANCE);
		RunnerFalling.nextImage = theLastImage;
		
		// load the runner standing still images and have them point to themselves because they aren't moving (one image movie)
		RunnerTurned = new MovieImage (new Image("file:Images/runner_turned.png"), MovieType.STANDING, null, 0, 0);
		RunnerTurned.nextImage = RunnerTurned;
		RunnerFacing = new MovieImage (new Image("file:Images/runner_facing.png"), MovieType.FACING_YOU, null, 0, 0);
		RunnerFacing.nextImage = RunnerFacing;
		
		// load the runner climbing down image and have it point to itself because it doesn't change
		RunnerClimbingDown = new MovieImage (new Image("file:Images/runner_down_ladder.png"), MovieType.CLIMBING_DOWN, null, 0, CONSTANTS.CLIMBING_FRAME_DISTANCE);
		RunnerClimbingDown.nextImage = RunnerClimbingDown;

		// load the runner hanging on the ladder image and have it point to itself because it doesn't change; set the delta to 0 because it doesn't move
		RunnerHangingOnLadder = new MovieImage (new Image("file:Images/runner_down_ladder.png"), MovieType.HANGING_ON_LADDER, null, 0, 0);
		RunnerHangingOnLadder.nextImage = RunnerHangingOnLadder;
		
		// load the raygun blast into a movie strip (not a movie loop)
		theLastImage = new MovieImage (new Image ("file:Images/raygun_blast_8.png"), MovieType.RAYGUN_BLAST, null, 0, 0);
		theLastImage = new MovieImage (new Image ("file:Images/raygun_blast_7.png"), MovieType.RAYGUN_BLAST, theLastImage, 0, 8);
		theLastImage = new MovieImage (new Image ("file:Images/raygun_blast_6.png"), MovieType.RAYGUN_BLAST, theLastImage, 0, 8);
		theLastImage = new MovieImage (new Image ("file:Images/raygun_blast_5.png"), MovieType.RAYGUN_BLAST, theLastImage, 0, 8);
		theLastImage = new MovieImage (new Image ("file:Images/raygun_blast_4.png"), MovieType.RAYGUN_BLAST, theLastImage, 0, 8);
		theLastImage = new MovieImage (new Image ("file:Images/raygun_blast_3.png"), MovieType.RAYGUN_BLAST, theLastImage, 0, 8);
		theLastImage = new MovieImage (new Image ("file:Images/raygun_blast_2.png"), MovieType.RAYGUN_BLAST, theLastImage, 0, 8);
		raygunBlast = theLastImage = new MovieImage (new Image ("file:Images/raygun_blast_1.png"), MovieType.RAYGUN_BLAST, theLastImage, 0, 8);
		
		// load the runner images for firing raygun into a movie strip (not a movie loop)
		theLastImage = new MovieImage (new Image ("file:Images/raygun_runner_10.png"), MovieType.FIRING_RAYGUN, RunnerTurned, 0, 0);
		theLastImage = new MovieImage (new Image ("file:Images/raygun_runner_9.png"), MovieType.FIRING_RAYGUN, theLastImage, 0, 0);
		theLastImage = new MovieImage (new Image ("file:Images/raygun_runner_8.png"), MovieType.FIRING_RAYGUN, theLastImage, 0, 0);
		theLastImage = new MovieImage (new Image ("file:Images/raygun_runner_7.png"), MovieType.FIRING_RAYGUN, theLastImage, 0, 0);
		theLastImage = new MovieImage (new Image ("file:Images/raygun_runner_6.png"), MovieType.FIRING_RAYGUN, theLastImage, 0, 0);
		theLastImage = new MovieImage (new Image ("file:Images/raygun_runner_5.png"), MovieType.FIRING_RAYGUN, theLastImage, 0, 0);
		theLastImage = new MovieImage (new Image ("file:Images/raygun_runner_4.png"), MovieType.FIRING_RAYGUN, theLastImage, 0, 0);
		theLastImage = new MovieImage (new Image ("file:Images/raygun_runner_3.png"), MovieType.FIRING_RAYGUN, theLastImage, 0, 0);
		theLastImage = new MovieImage (new Image ("file:Images/raygun_runner_2.png"), MovieType.FIRING_RAYGUN, theLastImage, 0, 0);
		firingRaygun = theLastImage = new MovieImage (new Image ("file:Images/raygun_runner_1.png"), MovieType.FIRING_RAYGUN, theLastImage, 0, 0);
		
		// load the runner images for leaping onto a ladder into a movie strip (not a movie loop)
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_1.png"), MovieType.LEAPING_ONTO_LADDER, RunnerHangingOnLadder, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_5.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_4.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_3.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_2.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		RunnerLeapingOntoLadder = new MovieImage (new Image ("file:Images/runner_ladder_leap_1.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		
		// load the runner images for leaping off of a ladder into a movie strip (not a movie loop)
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_1.png"), MovieType.LEAPING_OFF_OF_LADDER, RunnerRunning, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_5.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_4.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_3.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_2.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		RunnerLeapingOffOfLadder = new MovieImage (new Image ("file:Images/runner_ladder_leap_1.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		
		// load the runner images for climbing up a ladder into a movie loop
		RunnerClimbingUp = new MovieImage (new Image ("file:Images/runner_climbing_1.png"), MovieType.CLIMBING_UP, null, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_climbing_6.png"), MovieType.CLIMBING_UP, RunnerClimbingUp, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_climbing_5.png"), MovieType.CLIMBING_UP, theLastImage, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_climbing_4.png"), MovieType.CLIMBING_UP, theLastImage, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_climbing_3.png"), MovieType.CLIMBING_UP, theLastImage, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_climbing_2.png"), MovieType.CLIMBING_UP, theLastImage, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE);
		RunnerClimbingUp.nextImage = theLastImage;
		
		// load backgrounds
		white = new Image ("file:Images/white_background.png");
		mossWorld = new Image ("file:Images/moss_world.png");
		
		// load the various block images
		L_digable_surface_on_nothing = new Image ("file:Images/L_digable_surface_on_nothing.png");
		R_digable_surface_on_nothing = new Image ("file:Images/R_digable_surface_on_nothing.png");
		C_digable_surface_on_nothing1 = new Image ("file:Images/C_digable_surface_on_nothing1.png");
		C_digable_surface_on_nothing2 = new Image ("file:Images/C_digable_surface_on_nothing2.png");
		single_digable_surface_on_nothing = new Image ("file:Images/single_digable_surface_on_nothing.png");
		visableLadder = new Image ("file:Images/visable_ladder.png");
		hiddenLadder = new Image ("file:Images/hidden_ladder.png");
		rope = new Image ("file:Images/rope.png");
		gold1 = new Image ("file:Images/gold1.png");
		exit = new Image ("file:Images/exit.png");
		eraser = new Image ("file:Images/eraser.png");
		}
   }

class MovableLocationType implements Serializable {
	private static final long serialVersionUID = 1L;
	// the location of a runner or troll within a cavern
	// Location 0, 0 is upper left corner of cavern
	private int xPos=0, yPos=0;

	MovableLocationType (int x, int y) {
		xPos = x;
		yPos = y;
		}

	public int getX () {
		return xPos;
		}

	public int getY () {
		return yPos;
		}

	public void setX (int x) {
		xPos = x;
		}

	public void setY (int y) {
		yPos = y;
		}

	public void setXandY (int x, int y) {
		xPos = x;
		yPos = y;
		}
	}

enum MovieState {
	PLAYING,
	PAUSED
	}

enum Direction {
   RIGHT,
   LEFT,
   FACING
   }

class Sprite {
	MovableLocationType Location;
	MovieImage Image;
	MovieState movieState;
   ImageView View;
   Direction Orientation;

	Sprite (MovableLocationType location, MovieImage image, Direction orientation) {
		Location = location;
		Image = image;
		movieState = MovieState.PLAYING;
      View = new ImageView (Image.theImage);
      View.setX (Location.getX());
      View.setY (Location.getY());
      Orientation = orientation;
		}

	void setImage (MovieImage image) {
		//System.err.println ("Runner.setImage: image type = " + image.movieType);
		Image = image;
		movieState = MovieState.PLAYING;
		}

   int getScale () {
      if (Orientation == Direction.LEFT)
         return -1;
      return 1;
      }
	}

class BlockLocationType {
	// the location of a image block within a cavern
	// Location 0, 0 is upper left corner of cavern; each block is 44 x 44 pixels
	private int xPos=0, yPos=0;

	BlockLocationType (int x, int y) {
		xPos = x;
		yPos = y;
		}

	public int getX () {
		return xPos;
		}

	public int getY () {
		return yPos;
		}

	public BlockLocationType locationToTheLeft () {
		return new BlockLocationType (this.xPos-1, this.yPos);
	   }

	public BlockLocationType locationToTheRight () {
		return new BlockLocationType (this.xPos+1, this.yPos);
	   }

	public BlockLocationType locationAbove () {
		return new BlockLocationType (this.xPos, this.yPos-1);
	   }

	public BlockLocationType locationBelow () {
		return new BlockLocationType (this.xPos, this.yPos+1);
	   }

	public BlockLocationType locationToTheLowerRight () {
		return new BlockLocationType (this.xPos+1, this.yPos+1);
	   }

	public BlockLocationType locationToTheLowerLeft () {
		return new BlockLocationType (this.xPos-1, this.yPos+1);
	   }

	public BlockLocationType locationToTheUpperRight () {
		return new BlockLocationType (this.xPos+1, this.yPos-1);
	   }

	public BlockLocationType locationToTheUpperLeft () {
		return new BlockLocationType (this.xPos-1, this.yPos-1);
	   }
	}
	
enum BlockTypes {
	RUNNER,    // only used for conveying that runner is selected from palette in the level editor
	EMPTY,
	LADDER,
	HIDDEN_LADDER,
	DIGABLE,
	ROPE,
	EXIT,
	GOLD_1
	}

class BlockType {
	BlockTypes Type = BlockTypes.EMPTY;
	Image blockImage = null;
   ImageView blockView;
	ItemTypes Item;
	BlockLocationType Location;

	BlockType (BlockType block) {
		Type = block.Type;
		blockImage = block.blockImage;
      blockView = block.blockView;
		Item = block.Item;
		Location = block.Location;
		}

	BlockType () {
		Location = null;
		}

	BlockType (BlockLocationType location) {
		Location = location;
		}
	}

class ActiveBlock {
	BlockType theBlock;
	int delayAmount;

	ActiveBlock (BlockType block, int delay) {
		theBlock = block;
		delayAmount = delay;
		}
	}
 
public class CaveRunner extends Application {
	Stage paletteStage;
	Button runnerButton, 
		    digableButton, 
		    ladderButton, 
		    gold1Button,
		    hiddenLadderButton,
		    ropeButton,
		    exitButton,
		    eraserButton;
	AnimationTimer animationTimer; 
	Cavern editCavern;
	GraphicsContext editGc;
	BlockType editBlock = new BlockType();
	boolean paused = false;

   public static void main (String[] args) {launch (args);}

	void addGroundStrip (Cavern theCavern, int xPos, int yPos, int count, boolean sparse) {
		BlockType block;
		int x = xPos;

		for (int Counter=0; Counter < count; Counter++) {
			if (sparse && (Counter & 1) == 0) {
				x++; continue;
				}
			block = theCavern.getBlock (new BlockLocationType (x++, yPos));
			block.Type = BlockTypes.DIGABLE;
			if (Counter == 0)
				block.blockImage = Images.L_digable_surface_on_nothing;
			else if (Counter == count-1)
				block.blockImage = Images.R_digable_surface_on_nothing;
			else if ((Counter & 1) == 0)
				block.blockImage = Images.C_digable_surface_on_nothing2;
			else
				block.blockImage = Images.C_digable_surface_on_nothing1;
			theCavern.setBlock (block);
		   }
		}

	void addLadder (Cavern theCavern, int xPos, int yPos, int count) {
		BlockType block;
		int y = yPos;

		for (int Counter=0; Counter < count; Counter++) {
			block = theCavern.getBlock (new BlockLocationType (xPos, y++));
			block.Type = BlockTypes.LADDER;
			block.blockImage = Images.visableLadder;
			theCavern.setBlock (block);
		   }
		}

	Cavern createTestCavern (Group theView) {
		Cavern theCavern = new Cavern (28, 16, theView);
		addGroundStrip (theCavern, 0, 0, 27, true);
		addGroundStrip (theCavern, 0, 2, 2, false);
		addGroundStrip (theCavern, 3, 2, 24, false);
		addGroundStrip (theCavern, 0, 4, 27, false);
		addGroundStrip (theCavern, 0, 6, 27, false);
		addGroundStrip (theCavern, 0, 8, 27, false);
		addGroundStrip (theCavern, 0, 10, 27, false);
		addGroundStrip (theCavern, 0, 12, 27, false);
		addGroundStrip (theCavern, 0, 14, 27, false);
		addGroundStrip (theCavern, 6, 1, 1, false);
		addLadder (theCavern, 27, 0, 16);
		addLadder (theCavern, 2, 0, 16);
		addLadder (theCavern, 4, 0, 2);
		//theCavern.addRunner (new MovableLocationType(1100, 44));
		theCavern.addRunner (new MovableLocationType(324, 132));
		return theCavern;
		}
   
	public void paletteButtonClicked (ActionEvent e) {
		//System.err.println ("processing event " + e.toString());
		runnerButton.setStyle ("");
		digableButton.setStyle ("");
		ladderButton.setStyle ("");
		hiddenLadderButton.setStyle ("");
		ropeButton.setStyle ("");
		gold1Button.setStyle ("");
		exitButton.setStyle ("");
		eraserButton.setStyle ("");
		((Button)e.getSource()).setStyle ("-fx-base: #00ff00;");
		
		if (e.getSource() == runnerButton) {
			editBlock.Type = BlockTypes.RUNNER;   // indicate a runner for editor, no block is actually a runner in the game
			//System.err.println ("edit block set to runner");
			}
		else if (e.getSource() == digableButton) {
			editBlock.Type = BlockTypes.DIGABLE;
			editBlock.blockImage = Images.single_digable_surface_on_nothing;
			//System.err.println ("edit block set to diggable");
			}
		else if (e.getSource() == ladderButton) {
			editBlock.Type = BlockTypes.LADDER;
			editBlock.blockImage = Images.visableLadder;
			//System.err.println ("edit block set to visable ladder");
			}
		else if (e.getSource() == hiddenLadderButton) {
			editBlock.Type = BlockTypes.HIDDEN_LADDER;
			editBlock.blockImage = Images.hiddenLadder;
			//System.err.println ("edit block set to hidden ladder");
			}
		else if (e.getSource() == ropeButton) {
			editBlock.Type = BlockTypes.ROPE;
			editBlock.blockImage = Images.rope;
			//System.err.println ("edit block set to roper");
			}
		else if (e.getSource() == gold1Button) {
			editBlock.Type = BlockTypes.GOLD_1;
			editBlock.blockImage = Images.gold1;
			//System.err.println ("edit block set to gold");
			}
		else if (e.getSource() == exitButton) {
			editBlock.Type = BlockTypes.EXIT;
			editBlock.blockImage = Images.exit;
			//System.err.println ("edit block set to exit");
			}
		else if (e.getSource() == eraserButton) {
			editBlock.Type = BlockTypes.EMPTY;
			editBlock.blockImage = null;
			//System.err.println ("edit block set to eraser");
			}
		}

	public void start (Stage theStage) {
		//AudioClip sound = new AudioClip("file:LaserBlast.mp3");
		//sound.play();

		theStage.setTitle ("Cave Runner");
         
		// create the mode menu
		Menu modeMenu = new Menu ("Mode");
		MenuItem playGameItem = new MenuItem ("Play Game");
		MenuItem levelEditItem = new MenuItem ("Edit Levels");
		modeMenu.getItems().addAll (playGameItem, levelEditItem);
		// create the file menu
		Menu fileMenu = new Menu ("File");
		MenuItem saveItem = new MenuItem ("Save");
		MenuItem restoreItem = new MenuItem ("Restore");
		fileMenu.getItems().addAll (saveItem, restoreItem);
		// create menu bar
		MenuBar menuBar = new MenuBar();
		// load all menus into menu bar
		menuBar.getMenus().addAll (fileMenu, modeMenu);
    
		// create a status bar from a horizontal box
		HBox statusBar = new HBox();
		statusBar.setSpacing (10);
		Text modeText = new Text ("Playing Game");
		Text speedText = new Text ("speed 5");
		Text goldText = new Text ("");
		statusBar.getChildren().addAll (modeText, speedText, goldText);
      // we set a background color on the status bar, because we can't rely on the scene background color 
		// because, if the scene is sized small, the status bar will start to overlay the game view 
		// and if we don't explicitly set the statusBar background the center view will start
		// to bleed through the transparent background of the statusBar.
		statusBar.setStyle ("-fx-background-color: cornsilk"); 

      // create game view
		//Canvas gameCanvas = new Canvas (CONSTANTS.WINDOW_WIDTH, CONSTANTS.WINDOW_HEIGHT);
		Group gameView = new Group();
		//gameView.getChildren().add (gameCanvas);
		//GraphicsContext gameGc = gameCanvas.getGraphicsContext2D();

		// create level editor view
		Canvas editCanvas = new Canvas (CONSTANTS.WINDOW_WIDTH, CONSTANTS.WINDOW_HEIGHT);
		Group EditView = new Group();
		EditView.getChildren().add (editCanvas);
		editGc = editCanvas.getGraphicsContext2D();
		editCavern = new Cavern (28, 16, editGc);

		// create main window view that holds the main window, status bar, and menu bar
		BorderPane gameLayout = new BorderPane();
		VBox centerView = new VBox();
		gameLayout.setCenter (centerView); // we add the centerview first and we never change it, instead we put it's changeable contents in a vbox and change out the vbox content.
		gameLayout.setBottom (statusBar);
		gameLayout.setRight (null);
		gameLayout.setLeft (null);
		gameLayout.setTop (menuBar);   // note the game layout is the last thing added to the borderpane so it will always stay on top if the border pane is resized.

		// now load scene into the stage
		Scene theScene = new Scene (gameLayout, CONSTANTS.WINDOW_WIDTH-10, CONSTANTS.WINDOW_HEIGHT+31);
		theStage.setScene (theScene);
		theStage.setResizable(false);
		
      // create fifo queue for holding key presses, and initialize it to empty
		ArrayList<String> keysPressed = new ArrayList<String>();
		keysPressed.add(CONSTANTS.NO_KEY_PRESSED);
 
      theScene.setOnKeyPressed (
         new EventHandler<KeyEvent>() {
            public void handle (KeyEvent e) {
               String code = e.getCode().toString();
               //System.err.println ("keypressed event=" + code);
               // only add one entry for each key type that is pressed
               if (keysPressed.contains (code))
                  return;
               keysPressed.remove (CONSTANTS.NO_KEY_PRESSED);
               keysPressed.add (code);
               }
            }
         );

      theScene.setOnKeyReleased (
         new EventHandler<KeyEvent>() {
            public void handle (KeyEvent e) {
               String code = e.getCode().toString();
               if (keysPressed.contains (code))
                  keysPressed.remove (code);
               if (keysPressed.isEmpty())
                  // always have something at the head of the queue
                  keysPressed.add(CONSTANTS.NO_KEY_PRESSED);
               }
            }
         );
                   
		// init graphics and cavern
		Images.InitImages();
		Cavern theCavern = createTestCavern (gameView);
		theCavern.restore ("test.ser");
      int TotalGold = theCavern.LoadCavernIntoView ();
      goldText.setText ("Gold to get " + TotalGold);
 		theStage.show();
 
		// create editor palette window
		runnerButton = new Button ("", new ImageView (Images.RunnerFacing.theImage));
		runnerButton.setOnAction (e-> paletteButtonClicked(e));
		digableButton = new Button ("", new ImageView (Images.single_digable_surface_on_nothing));	
		digableButton.setOnAction (e-> paletteButtonClicked(e));
		ladderButton = new Button ("", new ImageView (Images.visableLadder));	
		ladderButton.setOnAction (e-> paletteButtonClicked(e));
		hiddenLadderButton = new Button ("", new ImageView (Images.hiddenLadder));	
		hiddenLadderButton.setOnAction (e-> paletteButtonClicked(e));
		ropeButton = new Button ("", new ImageView (Images.rope));	
		ropeButton.setOnAction (e-> paletteButtonClicked(e));
		gold1Button = new Button ("", new ImageView (Images.gold1));	
		gold1Button.setOnAction (e-> paletteButtonClicked(e));
		exitButton = new Button ("", new ImageView (Images.exit));	
		exitButton.setOnAction (e-> paletteButtonClicked(e));
		eraserButton = new Button ("", new ImageView (Images.eraser));	
		eraserButton.setOnAction (e-> paletteButtonClicked(e));
		FlowPane palettePane = new FlowPane();
		palettePane.getChildren().addAll (runnerButton, digableButton, ladderButton, hiddenLadderButton, ropeButton, gold1Button, exitButton, eraserButton);
		Scene paletteScene = new Scene (palettePane, 170, 146);
		paletteStage = new Stage();
		paletteStage.initOwner (theStage);
		paletteStage.setScene (paletteScene);
		paletteStage.setTitle ("Editor Palette");
		paletteStage.setResizable(false);
		
		// handle mouse input
		theScene.setOnMousePressed (new EventHandler<MouseEvent>() {
			@Override public void handle (MouseEvent event) {
				//System.err.println ("mouse press detected at " + event.getSceneX() + ", " + event.getSceneY());
				if (editBlock.Type == BlockTypes.RUNNER)
					editCavern.addRunner (new MovableLocationType (((int)event.getSceneX() / CONSTANTS.BLOCK_WIDTH) * CONSTANTS.BLOCK_WIDTH, 
                                                              (((int)event.getSceneY() - 26) / CONSTANTS.BLOCK_HEIGHT) * CONSTANTS.BLOCK_HEIGHT));
				else {
					BlockType block = editCavern.getBlock (new BlockLocationType ((int)event.getSceneX() / CONSTANTS.BLOCK_WIDTH, 
                                                                             ((int)event.getSceneY() - 26) / CONSTANTS.BLOCK_HEIGHT));
					block.Type = editBlock.Type;
					block.blockImage = editBlock.blockImage;
					}
				editCavern.display();
				}
		   });

		theScene.setOnMouseReleased (new EventHandler<MouseEvent>() {
			@Override public void handle (MouseEvent event) {
				//System.err.println ("mouse release detected at " + event.getSceneX() + ", " + event.getSceneY());
				}
		   });

		theScene.setOnMouseDragged (new EventHandler<MouseEvent>() {
			@Override public void handle (MouseEvent event) {
				//System.err.println ("mouse drag detected at " + event.getSceneX() + ", " + event.getSceneY());
				if (editBlock.Type != BlockTypes.RUNNER) {
					BlockType block = editCavern.getBlock (new BlockLocationType ((int)event.getSceneX() / CONSTANTS.BLOCK_WIDTH, 
                                                                             ((int)event.getSceneY() - 26) / CONSTANTS.BLOCK_HEIGHT));
					block.Type = editBlock.Type;
					block.blockImage = editBlock.blockImage;
					}
				editCavern.display();
				}
		   });

		// don't allow user to close the palette window
		paletteStage.setOnCloseRequest (new EventHandler<WindowEvent>() { 
			@Override public void handle (final WindowEvent windowEvent) { 
				windowEvent.consume();
            } 
			});
		
		// process menu item selections
		saveItem.setOnAction (new EventHandler<ActionEvent>() {
			public void handle (ActionEvent event) {
				if (editCavern == null) {
					Alert alert = new Alert (AlertType.ERROR);
					alert.setTitle ("Save Cavern Error");
					alert.setHeaderText("There is no editted cavern to save!");
					alert.setContentText("Please select the cavern editor and create a cavern first.");
					alert.showAndWait();
					return;
					}
				else if (editCavern.getRunner() == null) {
					Alert alert = new Alert (AlertType.ERROR);
					alert.setTitle ("Save Cavern Error");
					alert.setHeaderText("There is no runner in the editor cavern!");
					alert.setContentText("Please add a runner to the cavern first.");
					alert.showAndWait();
					return;				
					}
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle ("Save Cavern");
            String current = System.getProperty ("user.dir");
            //System.out.println ("Current working directory in Java : " + current);
            fileChooser.setInitialDirectory (new File(current));
				File file = fileChooser.showSaveDialog (theStage);
				if (file != null) 
					editCavern.save (file.getName());
				}
			});

		restoreItem.setOnAction (new EventHandler<ActionEvent>() {
			public void handle (ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle ("Restore Cavern");
            String current = System.getProperty ("user.dir");
            //System.out.println ("Current working directory in Java : " + current);
            fileChooser.setInitialDirectory (new File(current));
				File file = fileChooser.showOpenDialog (theStage);
				if (file != null) 
					if (modeText.getText() == "Playing Game") {
						theCavern.restore (file.getName());
                  theCavern.LoadCavernIntoView ();
                  }
					else {
						editCavern.restore (file.getName());
						editCavern.display();
					}
				}
			});

		playGameItem.setOnAction (new EventHandler<ActionEvent>() {
			public void handle (ActionEvent event) {
            // here we perform a centerview vbox content swap.
				centerView.getChildren().clear();  
				centerView.getChildren().add (gameView);
				modeText.setText ("Playing Game");
				// close the editor palette window
				paletteStage.close();
            // start the game animation engine
				animationTimer.start();
				}
			});

		levelEditItem.setOnAction (new EventHandler<ActionEvent>() {
			public void handle (ActionEvent event) {
            // stop the game animation engine
				animationTimer.stop();
				// show the editor palette window
				paletteStage.show();
            // here we perform a centerview vbox content swap.
				centerView.getChildren().clear();  
				centerView.getChildren().add (EditView);
				editCavern.display();
				modeText.setText ("Editing Level");
				}
			});

		animationTimer = new AnimationTimer() {
			int FrameCounter = 0;
			int frameRateDivider = CONSTANTS.DEFAULT_FRAME_RATE_DIVIDER;
         int TotalGold;

			public void handle (long currentNanoTime) { 
				// check for game control key presses
				if (keysPressed.contains ("F1")) {
					paused = !paused;
					keysPressed.remove ("F1");
					if (keysPressed.isEmpty())
						// always have something at the head of the queue
						keysPressed.add (CONSTANTS.NO_KEY_PRESSED);
					System.err.println ("paused = " + paused);
					}
				if (keysPressed.contains ("F6")) {
					frameRateDivider--;
					if (frameRateDivider <= 0)
						frameRateDivider = 1;
					speedText.setText ("speed " + frameRateDivider);
					keysPressed.remove ("F6");
					if (keysPressed.isEmpty())
						// always have something at the head of the queue
						keysPressed.add (CONSTANTS.NO_KEY_PRESSED);
					//System.err.println ("frame rate divider = " + frameRateDivider);
					}
				else if (keysPressed.contains ("F5")) {
					frameRateDivider++;
					speedText.setText ("speed " + frameRateDivider);
					keysPressed.remove ("F5");
					if (keysPressed.isEmpty())
						// always have something at the head of the queue
						keysPressed.add (CONSTANTS.NO_KEY_PRESSED);
					//System.err.println ("frame rate divider = " + frameRateDivider);
					}
				if (FrameCounter++ % frameRateDivider != 0)
				    return;
				if (!paused) {
					TotalGold = theCavern.frameHandler(keysPressed);
               goldText.setText ("Gold to get " + TotalGold);
               }
				}
			};
	    
		// start the app with the "playing game" window
		playGameItem.fire();
		}
	}