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
import javafx.util.Pair;

enum ItemTypes {
   GOLD,
   BOMB,
   TRAP
   }

enum EnvironmentCodes {
   N,        // ord(0) 
   T,        // ord(1)
   R,        // ord(2)
   TR,       // ord(3)
   B,        // ord(4)
   TB,       // ord(5)
   BR,       // ord(6)
   TBR,      // ord(7)
   L,        // ord(8)
   TL,       // ord(9)
   LR,       // ord(10)
   TLR,      // ord(11)
   BL,       // ord(12)
   TBL,      // ord(13)
   BLR,      // ord(14)
   TBLR,     // ord(15)
   }

enum BlockTypes {  
   SOFT,          // ord(0) 
   SOFT_T,        // ord(1)
   SOFT_R,        // ord(2)
   SOFT_TR,       // ord(3)
   SOFT_B,        // ord(4)
   SOFT_TB,       // ord(5)
   SOFT_BR,       // ord(6)
   SOFT_TBR,      // ord(7)
   SOFT_L,        // ord(8)
   SOFT_TL,       // ord(9)
   SOFT_LR,       // ord(10)
   SOFT_TLR,      // ord(11)
   SOFT_BL,       // ord(12)
   SOFT_TBL,      // ord(13)
   SOFT_BLR,      // ord(14)
   SOFT_TBLR,     // ord(15)
	RUNNER,        // this item is only used for conveying that runner is selected from palette in the level editor
	TROLL,         // this item is only used for conveying that troll is selected from palette in the level editor
   ERASER,        // this item is only used to access the image in the Images class
   HOLE,          // this is used by trolls when searching for a way to drop down
	EMPTY,
	LADDER,
	HIDDEN_LADDER,
	ROPE,
	EXIT,
	GOLD_1
   }

class Images {
	static Image mossWorld;
	static Image hard;
   static Image runner;
   static Image troll;
   static Image empty;
	private static Image soft;
	private static Image soft_T;
	private static Image soft_R;
	private static Image soft_TR;
	private static Image soft_B;
	private static Image soft_TB;
	private static Image soft_BR;
	private static Image soft_TBR;
	private static Image soft_L;
	private static Image soft_TL;
	private static Image soft_LR;
	private static Image soft_TLR;
	private static Image soft_BL;
	private static Image soft_TBL;
	private static Image soft_BLR;
	private static Image soft_TBLR;
	private static Image visableLadder;
	private static Image hiddenLadder;
	private static Image rope;
	private static Image gold1;
	private static Image exit;
	private static Image eraser;
         
	static void Init () {
		// load backgrounds
		mossWorld = new Image ("file:Images/moss_world.png");
      
      // load the actor images for the editor 
		runner = new Image ("file:Images/runner_facing.png");
		troll = new Image ("file:Images/troll_running_6.png");
      
		// load the various block images
		hard = new Image ("file:Images/hard.png");
		soft = new Image ("file:Images/soft.png");
		soft_T = new Image ("file:Images/soft_T.png");
		soft_R = new Image ("file:Images/soft_R.png");
		soft_TR = new Image ("file:Images/soft_TR.png");
		soft_B = new Image ("file:Images/soft_B.png");
		soft_TB = new Image ("file:Images/soft_TB.png");
		soft_BR = new Image ("file:Images/soft_BR.png");
		soft_TBR = new Image ("file:Images/soft_TBR.png");
		soft_L = new Image ("file:Images/soft_L.png");
		soft_TL = new Image ("file:Images/soft_TL.png");
		soft_LR = new Image ("file:Images/soft_LR_1.png");
		soft_TLR = new Image ("file:Images/soft_TLR.png");
		soft_BL = new Image ("file:Images/soft_BL.png");
		soft_TBL = new Image ("file:Images/soft_TBL.png");
		soft_BLR = new Image ("file:Images/soft_BLR.png");
		soft_TBLR = new Image ("file:Images/soft_TBLR.png");
		visableLadder = new Image ("file:Images/visable_ladder.png");
		hiddenLadder = new Image ("file:Images/hidden_ladder.png");
		rope = new Image ("file:Images/rope.png");
		gold1 = new Image ("file:Images/gold1.png");
		exit = new Image ("file:Images/exit.png");
		eraser = new Image ("file:Images/eraser.png");
      
      // load the empty image for the troll that captures the runner
		empty = new Image ("file:Images/empty.png");      
      }
   
   static Image getImage (BlockTypes blockType) {
      switch (blockType) {
         case LADDER: return visableLadder;
         case HIDDEN_LADDER: return hiddenLadder;
         case SOFT: return soft;
         case SOFT_T: return soft_T;
         case SOFT_R: return soft_R;
         case SOFT_TR: return soft_TR;
         case SOFT_B: return soft_B;
         case SOFT_TB: return soft_TB;
         case SOFT_BR: return soft_BR;
         case SOFT_TBR: return soft_TBR;
         case SOFT_L: return soft_L;
         case SOFT_TL: return soft_TL;
         case SOFT_LR: return soft_LR;
         case SOFT_TLR: return soft_TLR;
         case SOFT_BL: return soft_BL;
         case SOFT_TBL: return soft_TBL;
         case SOFT_BLR: return soft_BLR;
         case SOFT_TBLR: return soft_TBLR;
         case ROPE: return rope;
         case EXIT: return exit;
         case GOLD_1: return gold1;
         case ERASER: return eraser;
         }
      return null;
      }
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
   BEING_CAPTURED
   }

class MovieImage {
	MovieType movieType;
	Image theImage;
	MovieImage nextImage;
	int xDelta, yDelta;
   int RepeatCount;

	MovieImage (Image image, MovieType type, MovieImage next, int x, int y) {
		movieType = type;
		theImage = image;
		nextImage = next;
		xDelta = x;
		yDelta = y;
      RepeatCount = 0;
		}
   
   	MovieImage (Image image, MovieType type, MovieImage next, int x, int y, int repeatCount) {
		movieType = type;
		theImage = image;
		nextImage = next;
		xDelta = x;
		yDelta = y;
      RepeatCount = repeatCount;
		}
	}

class ActorMovies {
   MovieImage Running;
   MovieImage Turned;
   MovieImage Facing;
   MovieImage LeapingOntoLadder;
   MovieImage LeapingOffOfLadder;
   MovieImage HangingOnLadder;
   MovieImage ClimbingDown;
   MovieImage ClimbingUp;
   MovieImage Falling;
   MovieImage HangingOnRope;
   }

class RunnerMovies extends ActorMovies {
   MovieImage firingRaygun;
   MovieImage raygunBlast;
   MovieImage Captured;

	RunnerMovies () {
		// any MovieImage that is passed into the MovieImage constructor should be already initialized before it is used or it will be null
		MovieImage theLastImage;
		
		// load the runner images for running into a movie loop
		Running = new MovieImage (new Image ("file:Images/runner_running_1.png"), MovieType.RUNNING, null, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_11.png"), MovieType.RUNNING, Running, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_10.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_9.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_8.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_7.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_6.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_5.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_4.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_3.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_running_2.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		Running.nextImage = theLastImage;
		
		// load the runner images for hanging on rope into a movie loop
		HangingOnRope = new MovieImage (new Image ("file:Images/runner_on_rope_1.png"), MovieType.HANGING_ON_ROPE, null, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_on_rope_6.png"), MovieType.HANGING_ON_ROPE, HangingOnRope, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_on_rope_5.png"), MovieType.HANGING_ON_ROPE, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_on_rope_4.png"), MovieType.HANGING_ON_ROPE, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_on_rope_3.png"), MovieType.HANGING_ON_ROPE, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_on_rope_2.png"), MovieType.HANGING_ON_ROPE, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE, 0);
		HangingOnRope.nextImage = theLastImage;
		
		// load the runner falling images into a movie loop
		Falling = new MovieImage (new Image("file:Images/runner_falling_1.png"), MovieType.FALLING, null, 0, CONSTANTS.FALLING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_falling_5.png"), MovieType.FALLING, Falling, 0, CONSTANTS.FALLING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_falling_4.png"), MovieType.FALLING, theLastImage, 0, CONSTANTS.FALLING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_falling_1.png"), MovieType.FALLING, theLastImage, 0, CONSTANTS.FALLING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_falling_3.png"), MovieType.FALLING, theLastImage, 0, CONSTANTS.FALLING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_falling_2.png"), MovieType.FALLING, theLastImage, 0, CONSTANTS.FALLING_FRAME_DISTANCE);
		Falling.nextImage = theLastImage;
		
		// load the runner standing still images and have them point to themselves because they aren't moving (one image movie)
		Turned = new MovieImage (new Image("file:Images/runner_turned.png"), MovieType.STANDING, null, 0, 0);
		Turned.nextImage = Turned;
		Facing = new MovieImage (new Image("file:Images/runner_facing.png"), MovieType.FACING_YOU, null, 0, 0);
		Facing.nextImage = Facing;
		
		// load the runner climbing down image and have it point to itself because it doesn't change
		ClimbingDown = new MovieImage (new Image("file:Images/runner_down_ladder.png"), MovieType.CLIMBING_DOWN, null, 0, CONSTANTS.CLIMBING_FRAME_DISTANCE);
		ClimbingDown.nextImage = ClimbingDown;

		// load the runner hanging on the ladder image and have it point to itself because it doesn't change; set the delta to 0 because it doesn't move
		HangingOnLadder = new MovieImage (new Image("file:Images/runner_down_ladder.png"), MovieType.HANGING_ON_LADDER, null, 0, 0);
		HangingOnLadder.nextImage = HangingOnLadder;
		
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
		theLastImage = new MovieImage (new Image ("file:Images/raygun_runner_10.png"), MovieType.FIRING_RAYGUN, Turned, 0, 0);
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
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_1.png"), MovieType.LEAPING_ONTO_LADDER, HangingOnLadder, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_5.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_4.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_3.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_2.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		LeapingOntoLadder = new MovieImage (new Image ("file:Images/runner_ladder_leap_1.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		
		// load the runner images for leaping off of a ladder into a movie strip (not a movie loop)
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_1.png"), MovieType.LEAPING_OFF_OF_LADDER, Running, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_5.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_4.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_3.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		theLastImage = new MovieImage (new Image ("file:Images/runner_ladder_leap_2.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		LeapingOffOfLadder = new MovieImage (new Image ("file:Images/runner_ladder_leap_1.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE, 0);
		
		// load the runner images for climbing up a ladder into a movie loop
		ClimbingUp = new MovieImage (new Image ("file:Images/runner_climbing_1.png"), MovieType.CLIMBING_UP, null, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_climbing_6.png"), MovieType.CLIMBING_UP, ClimbingUp, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_climbing_5.png"), MovieType.CLIMBING_UP, theLastImage, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_climbing_4.png"), MovieType.CLIMBING_UP, theLastImage, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_climbing_3.png"), MovieType.CLIMBING_UP, theLastImage, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE);
		theLastImage = new MovieImage (new Image ("file:Images/runner_climbing_2.png"), MovieType.CLIMBING_UP, theLastImage, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE);
		ClimbingUp.nextImage = theLastImage;
      
      // load the images for the runner being captured by troll into a movie strip, and run at half speed (not a movie loop)
		theLastImage = new MovieImage (new Image ("file:Images/Death33.png"), MovieType.BEING_CAPTURED, null, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death32.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death31.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death30.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death29.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death28.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death27.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death26.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death25.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death24.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death23.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death22.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death21.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death20.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death19.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death18.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death17.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death16.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death15.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death14.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death13.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death12.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death11.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death10.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death9.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death8.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death7.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death6.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death5.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death4.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death3.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		theLastImage = new MovieImage (new Image ("file:Images/Death2.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		Captured = new MovieImage (new Image ("file:Images/Death1.png"), MovieType.BEING_CAPTURED, theLastImage, 0, 0, 1);
		}
   }

class TrollMovies extends ActorMovies {

   TrollMovies () {
		// any MovieImage that is passed into the MovieImage constructor should be already initialized before it is used or it will be null
		MovieImage theLastImage;
		
		// load the troll images for running into a movie loop
		Running = new MovieImage (new Image ("file:Images/troll_running_1.png"), MovieType.RUNNING, null, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_running_11.png"), MovieType.RUNNING, Running, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_running_10.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_running_9.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_running_8.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_running_7.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_running_6.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_running_5.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_running_4.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_running_3.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_running_2.png"), MovieType.RUNNING, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		Running.nextImage = theLastImage;
		
		// load the troll images for hanging on rope into a movie loop
		HangingOnRope = new MovieImage (new Image ("file:Images/troll_on_rope_1.png"), MovieType.HANGING_ON_ROPE, null, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_on_rope_10.png"), MovieType.HANGING_ON_ROPE, HangingOnRope, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_on_rope_9.png"), MovieType.HANGING_ON_ROPE, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_on_rope_8.png"), MovieType.HANGING_ON_ROPE, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_on_rope_7.png"), MovieType.HANGING_ON_ROPE, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_on_rope_6.png"), MovieType.HANGING_ON_ROPE, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_on_rope_5.png"), MovieType.HANGING_ON_ROPE, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_on_rope_4.png"), MovieType.HANGING_ON_ROPE, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_on_rope_3.png"), MovieType.HANGING_ON_ROPE, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_on_rope_2.png"), MovieType.HANGING_ON_ROPE, theLastImage, CONSTANTS.RUNNING_FRAME_DISTANCE/2, 0);
		HangingOnRope.nextImage = theLastImage;
		
		// load the troll falling images into a movie loop
		Falling = new MovieImage (new Image("file:Images/troll_falling_1.png"), MovieType.FALLING, null, 0, CONSTANTS.FALLING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_falling_6.png"), MovieType.FALLING, Falling, 0, CONSTANTS.FALLING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_falling_5.png"), MovieType.FALLING, theLastImage, 0, CONSTANTS.FALLING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_falling_4.png"), MovieType.FALLING, theLastImage, 0, CONSTANTS.FALLING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_falling_3.png"), MovieType.FALLING, theLastImage, 0, CONSTANTS.FALLING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_falling_2.png"), MovieType.FALLING, theLastImage, 0, CONSTANTS.FALLING_FRAME_DISTANCE/2);
		Falling.nextImage = theLastImage;
		

		// load the troll standing still images and have them point to themselves because they aren't moving (one image movie)
		Turned = new MovieImage (new Image("file:Images/troll_turned.png"), MovieType.STANDING, null, 0, 0);
		Turned.nextImage = Turned;
      // need this for movie updater to work, but troll never faces you so set him to turned
		Facing = new MovieImage (new Image("file:Images/troll_turned.png"), MovieType.FACING_YOU, null, 0, 0);
		Facing.nextImage = Facing;
		// load the troll hanging on the ladder image and have it point to itself because it doesn't change; set the delta to 0 because it doesn't move
		HangingOnLadder = new MovieImage (new Image("file:Images/troll_climbing_1.png"), MovieType.HANGING_ON_LADDER, null, 0, 0);
		HangingOnLadder.nextImage = HangingOnLadder;
      
		// load the runner images for leaping onto a ladder into a movie strip (not a movie loop)
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_1.png"), MovieType.LEAPING_ONTO_LADDER, HangingOnLadder, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_11.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_10.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_9.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_8.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_7.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_6.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_5.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_4.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_3.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_2.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		LeapingOntoLadder = new MovieImage (new Image ("file:Images/troll_ladder_leap_1.png"), MovieType.LEAPING_ONTO_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		
		// load the runner images for leaping off of a ladder into a movie strip (not a movie loop)
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_1.png"), MovieType.LEAPING_OFF_OF_LADDER, Running, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_11.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_10.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_9.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_8.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_7.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_6.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_5.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_4.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_3.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		theLastImage = new MovieImage (new Image ("file:Images/troll_ladder_leap_2.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		LeapingOffOfLadder = new MovieImage (new Image ("file:Images/troll_ladder_leap_1.png"), MovieType.LEAPING_OFF_OF_LADDER, theLastImage, CONSTANTS.LEAPING_FRAME_DISTANCE/2, 0);
		
		// load the runner images for climbing a ladder into a movie loop
		ClimbingUp = new MovieImage (new Image ("file:Images/troll_climbing_1.png"), MovieType.CLIMBING_UP, null, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_10.png"), MovieType.CLIMBING_UP, ClimbingUp, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_9.png"), MovieType.CLIMBING_UP, theLastImage, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_8.png"), MovieType.CLIMBING_UP, theLastImage, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_7.png"), MovieType.CLIMBING_UP, theLastImage, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_6.png"), MovieType.CLIMBING_UP, theLastImage, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_5.png"), MovieType.CLIMBING_UP, theLastImage, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_4.png"), MovieType.CLIMBING_UP, theLastImage, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_3.png"), MovieType.CLIMBING_UP, theLastImage, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_2.png"), MovieType.CLIMBING_UP, theLastImage, 0, -CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		ClimbingUp.nextImage = theLastImage;
      
		// load the runner images for climbing a ladder into a movie loop
		ClimbingDown = new MovieImage (new Image ("file:Images/troll_climbing_1.png"), MovieType.CLIMBING_DOWN, null, 0, CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_10.png"), MovieType.CLIMBING_DOWN, ClimbingDown, 0, CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_9.png"), MovieType.CLIMBING_DOWN, theLastImage, 0, CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_8.png"), MovieType.CLIMBING_DOWN, theLastImage, 0, CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_7.png"), MovieType.CLIMBING_DOWN, theLastImage, 0, CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_6.png"), MovieType.CLIMBING_DOWN, theLastImage, 0, CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_5.png"), MovieType.CLIMBING_DOWN, theLastImage, 0, CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_4.png"), MovieType.CLIMBING_DOWN, theLastImage, 0, CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_3.png"), MovieType.CLIMBING_DOWN, theLastImage, 0, CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		theLastImage = new MovieImage (new Image ("file:Images/troll_climbing_2.png"), MovieType.CLIMBING_DOWN, theLastImage, 0, CONSTANTS.CLIMBING_FRAME_DISTANCE/2);
		ClimbingDown.nextImage = theLastImage;
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
   UP,
   DOWN,
   RIGHT,
   LEFT,
   FACING,
   AWAY;

   static public Direction HorizontalDirection (int value) {
      if (value < 0)
         return LEFT;
      else
         return RIGHT;
   }
   
   static public Direction VerticalDirection (int value) {
      if (value < 0)
         return UP;
      else
         return DOWN;
   }
   
   static public Direction Opposite (Direction theDirection) {
      if (theDirection == UP)
         return DOWN;
      else if (theDirection == DOWN)
         return UP;
      else if (theDirection == RIGHT)
         return LEFT;
      else if (theDirection == LEFT)
         return RIGHT;
      else if (theDirection == FACING)
         return AWAY;
      else 
         return FACING;
      }   
   } 

enum SpriteType {
   RUNNER,
   TROLL,
   RAYGUN_BLAST
}

class Sprite {
	MovableLocationType Location;
	MovieImage Image;
	MovieState movieState;
   ImageView View;
   Direction Orientation;
   SpriteType Type;

	Sprite (MovableLocationType location, MovieImage image, Direction orientation, SpriteType type) {
		Location = location;
		Image = image;
		movieState = MovieState.PLAYING;
      View = new ImageView (Image.theImage);
      View.setX (Location.getX());
      View.setY (Location.getY());
      Orientation = orientation;
      Type = type;
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

class BlockLocation {
	// the location of a image block within a cavern
	// Location 0, 0 is upper left corner of cavern; each block is 44 x 44 pixels
	private int xPos=0, yPos=0;

	BlockLocation (int x, int y) {
		xPos = x;
		yPos = y;
		}

	public int getX () {
		return xPos;
		}

	public int getY () {
		return yPos;
		}

	public BlockLocation locationToTheLeft () {
		return new BlockLocation (this.xPos-1, this.yPos);
	   }

	public BlockLocation locationToTheRight () {
		return new BlockLocation (this.xPos+1, this.yPos);
	   }

	public BlockLocation locationAbove () {
		return new BlockLocation (this.xPos, this.yPos-1);
	   }

	public BlockLocation locationBelow () {
		return new BlockLocation (this.xPos, this.yPos+1);
	   }

	public BlockLocation locationToTheLowerRight () {
		return new BlockLocation (this.xPos+1, this.yPos+1);
	   }

	public BlockLocation locationToTheLowerLeft () {
		return new BlockLocation (this.xPos-1, this.yPos+1);
	   }

	public BlockLocation locationToTheUpperRight () {
		return new BlockLocation (this.xPos+1, this.yPos-1);
	   }

	public BlockLocation locationToTheUpperLeft () {
		return new BlockLocation (this.xPos-1, this.yPos-1);
	   }
	}
	
class Block {
	BlockTypes Type = BlockTypes.EMPTY;
	Image blockImage = null;
   ImageView blockView;
	ItemTypes Item;
	BlockLocation Location;

	Block (Block block) {
		Type = block.Type;
		blockImage = block.blockImage;
      blockView = block.blockView;
		Item = block.Item;
		Location = block.Location;
		}

	Block () {
		Location = null;
		}

	Block (BlockLocation location) {
		Location = location;
		}
   
   boolean Laserable () {
      if (Type == BlockTypes.EMPTY)
         return false;
      return true;
      }
   
   boolean IsStructural () {
      switch (Type) {
         case EMPTY:
         case LADDER:
         case HIDDEN_LADDER:
         case ROPE:
         case EXIT:
         case GOLD_1: return false;
         }
      return true;
      }
   }

class ActiveBlock {
	Block theBlock;
	int delayAmount;

	ActiveBlock (Block block, int delay) {
		theBlock = block;
		delayAmount = delay;
		}
	}
 
class EditorPalette extends Stage {
	Button runnerButton, 
          trollButton,
		    digableButton, 
		    ladderButton, 
		    gold1Button,
		    hiddenLadderButton,
		    ropeButton,
		    exitButton,
		    eraserButton;
   Block editBlock;
   
   EditorPalette (Stage parentStage, Block editorBlock) {
      editBlock = editorBlock;
      runnerButton = new Button ("", new ImageView (Images.runner));
      runnerButton.setOnAction (e-> paletteButtonClicked(e));
      trollButton = new Button ("", new ImageView (Images.troll));
      trollButton.setOnAction (e-> paletteButtonClicked(e));
      digableButton = new Button ("", new ImageView (Images.getImage(BlockTypes.SOFT)));	
      digableButton.setOnAction (e-> paletteButtonClicked(e));
      ladderButton = new Button ("", new ImageView (Images.getImage(BlockTypes.LADDER)));	
      ladderButton.setOnAction (e-> paletteButtonClicked(e));
      hiddenLadderButton = new Button ("", new ImageView (Images.getImage(BlockTypes.HIDDEN_LADDER)));	
      hiddenLadderButton.setOnAction (e-> paletteButtonClicked(e));
      ropeButton = new Button ("", new ImageView (Images.getImage(BlockTypes.ROPE)));	
      ropeButton.setOnAction (e-> paletteButtonClicked(e));
      gold1Button = new Button ("", new ImageView (Images.getImage(BlockTypes.GOLD_1)));	
      gold1Button.setOnAction (e-> paletteButtonClicked(e));
      exitButton = new Button ("", new ImageView (Images.getImage(BlockTypes.EXIT)));	
      exitButton.setOnAction (e-> paletteButtonClicked(e));
      eraserButton = new Button ("", new ImageView (Images.getImage(BlockTypes.ERASER)));	
      eraserButton.setOnAction (e-> paletteButtonClicked(e));
      FlowPane palettePane = new FlowPane();
      palettePane.getChildren().addAll (runnerButton, trollButton, digableButton, ladderButton, hiddenLadderButton, ropeButton, gold1Button, exitButton, eraserButton);
      Scene paletteScene = new Scene (palettePane, 170, 146);
      this.initOwner (parentStage);
      this.setScene (paletteScene);
      this.setTitle ("Editor Palette");
      this.setResizable(false);

      // don't allow user to close the palette window
      this.setOnCloseRequest (new EventHandler<WindowEvent>() { 
         @Override public void handle (final WindowEvent windowEvent) { 
            windowEvent.consume();
            } 
         });
      }
		        
	void paletteButtonClicked (ActionEvent e) {
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
         return;
			}
		if (e.getSource() == trollButton) {
			editBlock.Type = BlockTypes.TROLL;   // indicate a troll for editor, no block is actually a runner in the game
			//System.err.println ("edit block set to troll");
         return;
			}
		else if (e.getSource() == digableButton) {
			editBlock.Type = BlockTypes.SOFT;
			//System.err.println ("edit block set to diggable");
			}
		else if (e.getSource() == ladderButton) {
			editBlock.Type = BlockTypes.LADDER;
			//System.err.println ("edit block set to visable ladder");
			}
		else if (e.getSource() == hiddenLadderButton) {
			editBlock.Type = BlockTypes.HIDDEN_LADDER;
			//System.err.println ("edit block set to hidden ladder");
			}
		else if (e.getSource() == ropeButton) {
			editBlock.Type = BlockTypes.ROPE;
			//System.err.println ("edit block set to roper");
			}
		else if (e.getSource() == gold1Button) {
			editBlock.Type = BlockTypes.GOLD_1;
			//System.err.println ("edit block set to gold");
			}
		else if (e.getSource() == exitButton) {
			editBlock.Type = BlockTypes.EXIT;
			//System.err.println ("edit block set to exit");
			}
		else if (e.getSource() == eraserButton) {
			editBlock.Type = BlockTypes.EMPTY;
			editBlock.blockImage = null;
			//System.err.println ("edit block set to eraser");
         return;
			}
		editBlock.blockImage = Images.getImage(editBlock.Type);
		}
   }
   
public class CaveRunner extends Application {
	Button runnerButton, 
          trollButton,
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
	Block editBlock = new Block();
	boolean paused = false;

	void addGroundStrip (Cavern theCavern, int xPos, int yPos, int count, boolean sparse) {
		Block block;
		int x = xPos;
		for (int Counter=0; Counter < count; Counter++) {
			if (sparse && (Counter & 1) == 0) {
				x++; continue;
				}
			block = theCavern.getBlock (new BlockLocation (x++, yPos));
			if (Counter == 0) 
            block.Type = BlockTypes.SOFT_R;            
			else if (Counter == count-1)
            block.Type = BlockTypes.SOFT_L;            
			else
	         block.Type = BlockTypes.SOFT_LR;            
         block.blockImage = Images.getImage(block.Type);
			theCavern.setBlock (block);
		   }
		}

	void addLadder (Cavern theCavern, int xPos, int yPos, int count) {
		Block block;
		int y = yPos;
		for (int Counter=0; Counter < count; Counter++) {
			block = theCavern.getBlock (new BlockLocation (xPos, y++));
			block.Type = BlockTypes.LADDER;
			block.blockImage = Images.getImage(block.Type);
			theCavern.setBlock (block);
		   }
		}

	void createTestCavern (Cavern theCavern) {
		addGroundStrip (theCavern, 0, 0, 27, true);
		addGroundStrip (theCavern, 0, 2, 2, false);
		addGroundStrip (theCavern, 3, 2, 24, false);
		addGroundStrip (theCavern, 0, 4, 27, false);
		addGroundStrip (theCavern, 0, 6, 27, true);
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
      theCavern.addTroll (new MovableLocationType(88, 420));
		}
   
	public Stage CreatePaletteStage (Stage theStage) {
      Stage paletteStage = new EditorPalette (theStage, editBlock);
      return paletteStage;
      }
		
      BlockTypes MapEnvironmentCodeToBlockType (EnvironmentCodes code) {
      switch (code) {
         case N:    return BlockTypes.SOFT;
         case T:    return BlockTypes.SOFT_T;
         case R:    return BlockTypes.SOFT_R;
         case TR:   return BlockTypes.SOFT_TR;
         case B:    return BlockTypes.SOFT_B;
         case TB:   return BlockTypes.SOFT_TB;
         case BR:   return BlockTypes.SOFT_BR;
         case TBR:  return BlockTypes.SOFT_TBR;
         case L:    return BlockTypes.SOFT_L;
         case TL:   return BlockTypes.SOFT_TL;
         case LR:   return BlockTypes.SOFT_LR;
         case TLR:  return BlockTypes.SOFT_TLR;
         case BL:   return BlockTypes.SOFT_BL;
         case TBL:  return BlockTypes.SOFT_TBL;
         case BLR:  return BlockTypes.SOFT_BLR;
         case TBLR: return BlockTypes.SOFT_TBLR;
      }
     return null;
   }
   
   Pair<BlockTypes, ArrayList<Block>> getStructureType (Block block) {
      ArrayList<Block> blocks = new ArrayList();
      Block adjacentBlock;
		BlockLocation location;
      int environmentCode = 0;
      BlockTypes blockType;
      
      location = block.Location.locationAbove();
		adjacentBlock = editCavern.getBlock (location);
      if ((adjacentBlock != null) && adjacentBlock.IsStructural()) {
         blocks.add(adjacentBlock);
         environmentCode += 1;
         }
      location = block.Location.locationToTheRight();
		adjacentBlock = editCavern.getBlock (location);
      if ((adjacentBlock != null) && adjacentBlock.IsStructural()) {
         blocks.add(adjacentBlock);
         environmentCode += 2;
         }
      location = block.Location.locationBelow();
		adjacentBlock = editCavern.getBlock (location);
      if ((adjacentBlock != null) && adjacentBlock.IsStructural()) {
         blocks.add(adjacentBlock);
         environmentCode += 4;
         }
      location = block.Location.locationToTheLeft();
		adjacentBlock = editCavern.getBlock (location);
      if ((adjacentBlock != null) && adjacentBlock.IsStructural()) {
         blocks.add(adjacentBlock);
         environmentCode += 8;
         }
      blockType = MapEnvironmentCodeToBlockType (EnvironmentCodes.values()[environmentCode]);
      return new Pair<BlockTypes, ArrayList<Block>>(blockType, blocks);
      }

   void AddBlockToEditorCavern (Block block) {
      if (editBlock.IsStructural()) {
         Pair<BlockTypes, ArrayList<Block>> structureInfo = getStructureType (block);
         //System.err.println ("AddBlockToCavern: structureInfo = " + structureInfo.toString());
         block.Type = structureInfo.getKey();
         block.blockImage = Images.getImage(block.Type); 
         ArrayList<Block> adjacentBlocks = structureInfo.getValue();
         for (Block theBlock : adjacentBlocks) {
            structureInfo = getStructureType (theBlock);
            theBlock.Type = structureInfo.getKey();
            theBlock.blockImage = Images.getImage(theBlock.Type); 
             }
         }
      else {
         boolean wasStructural = block.IsStructural();
         block.Type = editBlock.Type;
         block.blockImage = editBlock.blockImage;  
         if (wasStructural) {
            Pair<BlockTypes, ArrayList<Block>> structureInfo = getStructureType (block);
            ArrayList<Block> adjacentBlocks = structureInfo.getValue();
            for (Block theBlock : adjacentBlocks) {
               structureInfo = getStructureType (theBlock);
               theBlock.Type = structureInfo.getKey();
               theBlock.blockImage = Images.getImage(theBlock.Type); 
             }
            }
         }
      }
   
   public static void main (String[] args) {launch (args);}

	public void start (Stage theStage) {
      // some earlier test code not ready to delete just yet
      //System.out.println ("Enterring start method");
		//AudioClip sound = new AudioClip("file:LodeRunnerCapture.mp3");
		//sound.play();
      
		// create the menu bar and its menu items
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
    
		// create the status bar from a horizontal box and load the text items into it
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

		// create the application window that holds the main game window, status bar, and menu bar
		VBox centerView = new VBox();    // main game window
		BorderPane gameLayout = new BorderPane();     //application window
		gameLayout.setRight (null);
		gameLayout.setLeft (null);
		gameLayout.setCenter (centerView); // we add the centerview first and we never change it, instead we put it's changeable contents in a vbox and change out the vbox content.
		gameLayout.setBottom (statusBar);
		gameLayout.setTop (menuBar);   // note: the game layout is the last thing added to the borderpane so it will always stay on top if the border pane is resized.

		// now load the scene into the stage and initialize the the stage
		Scene theScene = new Scene (gameLayout, CONSTANTS.WINDOW_WIDTH-10, CONSTANTS.WINDOW_HEIGHT+31);
		theStage.setScene (theScene);
		theStage.setResizable(false);
		theStage.setTitle ("Cave Runner");
         		
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
                   
		// create level editor view
		Canvas editCanvas = new Canvas (CONSTANTS.WINDOW_WIDTH, CONSTANTS.WINDOW_HEIGHT);
		Group EditView = new Group();
		EditView.getChildren().add (editCanvas);
		editGc = editCanvas.getGraphicsContext2D();
		editCavern = new Cavern (28, 16, editGc);

		// init graphics
		Images.Init();
      
      // init cavern
      // create game view
		Group gameView = new Group();
      Cavern theCavern = new Cavern (28, 16, gameView);
 
		// handle mouse input from the scene in the editor mode
		theScene.setOnMousePressed (new EventHandler<MouseEvent>() {
			@Override public void handle (MouseEvent event) {
				//System.err.println ("mouse press detected at " + event.getSceneX() + ", " + event.getSceneY());
            if (modeText.getText() == "Playing Game")
               // mouse not valid input device while in play mode
               return;
				if (editBlock.Type == BlockTypes.RUNNER)
					editCavern.addRunner (new MovableLocationType (((int)event.getSceneX() / CONSTANTS.BLOCK_WIDTH) * CONSTANTS.BLOCK_WIDTH, 
                                                              (((int)event.getSceneY() - 26) / CONSTANTS.BLOCK_HEIGHT) * CONSTANTS.BLOCK_HEIGHT));
            else if (editBlock.Type == BlockTypes.TROLL)
					editCavern.addTroll (new MovableLocationType (((int)event.getSceneX() / CONSTANTS.BLOCK_WIDTH) * CONSTANTS.BLOCK_WIDTH, 
                                                             (((int)event.getSceneY() - 26) / CONSTANTS.BLOCK_HEIGHT) * CONSTANTS.BLOCK_HEIGHT));
				else {
               if (editBlock.Type == BlockTypes.EMPTY) 
                  // check for erasure of a troll
                  editCavern.removeTroll (new MovableLocationType (((int)event.getSceneX() / CONSTANTS.BLOCK_WIDTH) * CONSTANTS.BLOCK_WIDTH, 
                                                                   (((int)event.getSceneY() - 26) / CONSTANTS.BLOCK_HEIGHT) * CONSTANTS.BLOCK_HEIGHT));
					Block block = editCavern.getBlock (new BlockLocation ((int)event.getSceneX() / CONSTANTS.BLOCK_WIDTH, 
                                                                     ((int)event.getSceneY() - 26) / CONSTANTS.BLOCK_HEIGHT));
               AddBlockToEditorCavern (block);
					}
				editCavern.display();
				}
		   });

		// debugging code, can delete this later if not needed
      /*
      theScene.setOnMouseReleased (new EventHandler<MouseEvent>() {
			@Override public void handle (MouseEvent event) {
				//System.err.println ("mouse release detected at " + event.getSceneX() + ", " + event.getSceneY());
				}
		   });
      */

		theScene.setOnMouseDragged (new EventHandler<MouseEvent>() {
			@Override public void handle (MouseEvent event) {
				//System.err.println ("mouse drag detected at " + event.getSceneX() + ", " + event.getSceneY());
            if (modeText.getText() == "Playing Game")
               // mouse not valid input device while in play mode
               return;
				if ((editBlock.Type != BlockTypes.RUNNER) && (editBlock.Type != BlockTypes.TROLL)) {
					Block block = editCavern.getBlock (new BlockLocation ((int)event.getSceneX() / CONSTANTS.BLOCK_WIDTH, 
                                                                             ((int)event.getSceneY() - 26) / CONSTANTS.BLOCK_HEIGHT));
               AddBlockToEditorCavern (block);
					}
				editCavern.display();
				}
		   });

		// create editor palette window
		Stage paletteStage = CreatePaletteStage (theStage);
      
		// create the methods to process menu item selections
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
	    
		
      //createTestCavern (theCavern);
      String fileName = "test2.ser";
		if (!theCavern.restore (fileName)) {
         System.out.println ("Cavern file " + fileName + " does not exist");
         return;
         }
      int TotalGold = theCavern.LoadCavernIntoView ();
      goldText.setText ("Gold to get " + TotalGold);
 		theStage.show();
      
		// start the app with the "playing game" window
		playGameItem.fire();
		}
	}