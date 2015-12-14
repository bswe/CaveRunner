/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caverunner;

import java.io.*;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.AudioClip;
import javafx.scene.image.ImageView;
import javafx.scene.Group;
import static java.lang.Math.abs;
/**
 *
 * @author wcb
 */
class Cavern {
	private GraphicsContext Gc;
   Group View;
	private int Width = 0;
	private int Height = 0;
	private Block[][] Blocks;
	private int frameCounter = 0;
	private Sprite theRunner;
	private String lastKeyCode = "";
	private Sprite raygunBlast;
	private ArrayList<ActiveBlock> activeBlocks = new ArrayList<>();
   private Sprite Troll1;
   private int TotalGold = 0;

	AudioClip pling = new AudioClip ("file:Pling.mp3");
	AudioClip laserBlast = new AudioClip ("file:LaserBlast.mp3");
   private RunnerMovies theRunnerMovies = new RunnerMovies();
   private TrollMovies theTrollMovies = new TrollMovies();

	Cavern (int width, int height, GraphicsContext gc) {
		Gc = gc;
		Blocks = new Block[width][height];
		Width = width;
		Height = height;
		for (int x=0; x < Width; x++)
			for (int y=0; y < Height; y++) 
				Blocks [x] [y] = new Block (new BlockLocation (x, y));
		}
   
   Cavern (int width, int height, Group theView) {
		View = theView;
		Blocks = new Block[width][height];
		Width = width;
		Height = height;
		for (int x=0; x < Width; x++)
			for (int y=0; y < Height; y++) 
				Blocks [x] [y] = new Block (new BlockLocation (x, y));
		}
   
   private void DisplayEscape () {
      View.getChildren().remove (theRunner.View);
      for (int x=0; x < Width; x++)
			for (int y=0; y < Height; y++)
			   if (Blocks[x][y].blockImage != null) {
               if (Blocks[x][y].Type == BlockTypes.EXIT)
                  View.getChildren().add(Blocks[x][y].blockView);
               if (Blocks[x][y].Type == BlockTypes.HIDDEN_LADDER) {
                  Blocks[x][y].Type = BlockTypes.LADDER;
						Blocks[x][y].blockView.setImage(Images.getImage (BlockTypes.LADDER));
                  View.getChildren().add(Blocks[x][y].blockView);
                  }
               }
      View.getChildren().add (theRunner.View);
      }
   
   public int LoadCavernIntoView () {
      TotalGold = 0;
      View.getChildren().clear();
      View.getChildren().add (new ImageView(Images.mossWorld));
      for (int x=0; x < Width; x++)
			for (int y=0; y < Height; y++)
			   if (Blocks[x][y].blockImage != null) {
               if ((Blocks[x][y].Type != BlockTypes.EXIT) &&
                   (Blocks[x][y].Type != BlockTypes.HIDDEN_LADDER))
                  View.getChildren().add(Blocks[x][y].blockView);
               /*
               if (Blocks[x][y].IsStructural()) {
                  ImageView aView = new ImageView (Images.hard);
                  aView.setX (Blocks[x][y].Location.getX()*44);
                  aView.setY (Blocks[x][y].Location.getY()*44);
                  View.getChildren().add(aView);                  
                  }
               */
               if (Blocks[x][y].Type == BlockTypes.GOLD_1) 
                  TotalGold++;
               }
      View.getChildren().add (theRunner.View);
      View.getChildren().add (Troll1.View);
      return TotalGold;
      }

	public void addRunner (MovableLocationType location) {
		theRunner = new Sprite (location, theRunnerMovies.Facing, Direction.FACING);
		}

	public void addTroll (MovableLocationType location) {
		Troll1 = new Sprite (location, theTrollMovies.Facing, Direction.RIGHT);
	}

	public Sprite getRunner () {
		return theRunner;
		}

	public boolean setBlock (Block block) {
		if (block.Location.getX() > Width)
			return false;
		if (block.Location.getY() > Height)
			return false;
      block.blockView = new ImageView (block.blockImage);
      block.blockView.setX (block.Location.getX()*CONSTANTS.BLOCK_WIDTH);
      block.blockView.setY (block.Location.getY()*CONSTANTS.BLOCK_HEIGHT);
		Blocks[block.Location.getX()][block.Location.getY()] = block;
		return true;
		}

	public Block getBlock (BlockLocation location) {
		if ((0 > location.getX()) || (location.getX() >= Width))
			return null;
		if ((0 > location.getY()) || (location.getY() >= Height))
			return null;
		return Blocks[location.getX()][location.getY()];
		}

	private Block getBlockRunnerIsIn (MovableLocationType location) {
	   int x, y, fudgeFactor;

		if ((((location.getX() % CONSTANTS.BLOCK_WIDTH) / 4) & 1) == 1)
			fudgeFactor = 2;  // add 2 pixels to blocks where runner is on 4, 12, 20, 28, 36 pixel bounderies
		else
			fudgeFactor = 0;
		x = (location.getX() + (CONSTANTS.BLOCK_WIDTH/2) + fudgeFactor) / CONSTANTS.BLOCK_WIDTH;
		y = (location.getY() + (CONSTANTS.BLOCK_HEIGHT/2) + fudgeFactor) / CONSTANTS.BLOCK_HEIGHT;
		//System.err.println ("getBlockRunnerIsIn: x=" + x + " y=" + y);
		return Blocks[x][y];
		}

	private void centerRunnerInBlock (Sprite theRunner) {
		BlockLocation blockLocation;

		blockLocation = getBlockRunnerIsIn (theRunner.Location).Location;
		theRunner.Location.setX (blockLocation.getX()*CONSTANTS.BLOCK_WIDTH);
		}

	private void putRunnerOnBlock (Sprite theRunner) {
		BlockLocation blockLocation;

		blockLocation = getBlockRunnerIsIn (theRunner.Location).Location;
		theRunner.Location.setY (blockLocation.getY()*CONSTANTS.BLOCK_HEIGHT);
		}

	private boolean canRunnerMoveRight (Sprite theRunner) {
		Block thisBlock, nextBlock;
		BlockLocation location;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		location = thisBlock.Location.locationToTheRight();
		nextBlock = getBlock (location);
		if ((nextBlock == null) || (nextBlock.IsStructural())) 
			return false;
		else
			return true;
		}

	private boolean canRunnerMoveLeft (Sprite theRunner) {
		Block thisBlock, nextBlock;
		BlockLocation location;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		location = thisBlock.Location.locationToTheLeft();
		nextBlock = getBlock (location);
		if ((nextBlock == null) || (nextBlock.IsStructural())) 
			return false;
		else
			return true;
		}

	private boolean canRunnerMoveDown (Sprite theRunner) {
		Block thisBlock, nextBlock;
		BlockLocation location;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		location = thisBlock.Location.locationBelow();
		nextBlock = getBlock (location);
		if ((nextBlock == null) || (nextBlock.IsStructural())) 
			return false;
		else
			return true;
		}

	private boolean canRunnerMoveUp (Sprite theRunner) {
		Block thisBlock, nextBlock;
		BlockLocation location;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		location = thisBlock.Location.locationAbove();
		nextBlock = getBlock (location);
		if ((nextBlock == null) || (nextBlock.IsStructural())) 
			return false;
		else
			return true;
		}

	private boolean objectIsOnBlockFloor (MovableLocationType location) {
		int xLocation;

		xLocation = location.getY() % CONSTANTS.BLOCK_HEIGHT;
		if ((xLocation == 0) || (xLocation == 40))
			return true;
		else
			return false;
		}

	private boolean IsRunnerAtLadder (Sprite theRunner) {
		Block thisBlock;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		if (thisBlock.Type == BlockTypes.LADDER)
			return true;
		else
			return false;
		}

	private boolean IsRunnerAtRope (Sprite theRunner) {
		Block thisBlock;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		if (thisBlock.Type == BlockTypes.ROPE)
			return true;
		else
			return false;
		}

	private boolean IsRunnerNextToLadderOnTheRight (Sprite theRunner) {
		Block thisBlock, nextBlock;
		BlockLocation location;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		location = thisBlock.Location.locationToTheRight();
		nextBlock = getBlock (location);
		if (nextBlock.Type == BlockTypes.LADDER)
			return true;
		else
			return false;
		}

	private boolean IsRunnerNextToLadderOnTheLeft (Sprite theRunner) {
		Block thisBlock, nextBlock;
		BlockLocation location;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		location = thisBlock.Location.locationToTheLeft();
		nextBlock = getBlock (location);
		if (nextBlock.Type == BlockTypes.LADDER)
			return true;
		else
			return false;
		}

	private boolean IsRunnerNextToRopeOnTheLeft (Sprite theRunner) {
		Block thisBlock, nextBlock;
		BlockLocation location;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		location = thisBlock.Location.locationToTheLeft();
		nextBlock = getBlock (location);
		if (nextBlock.Type == BlockTypes.ROPE)
			return true;
		else
			return false;
		}

	private boolean IsRunnerNextToRopeOnTheRight (Sprite theRunner) {
		Block thisBlock, nextBlock;
		BlockLocation location;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		location = thisBlock.Location.locationToTheRight();
		nextBlock = getBlock (location);
		if (nextBlock.Type == BlockTypes.ROPE)
			return true;
		else
			return false;
		}

	private boolean IsThereSolidGroundOnTheRight (Sprite theRunner) {
		Block thisBlock, nextBlock;
		BlockLocation location;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		location = thisBlock.Location.locationToTheLowerRight();
		nextBlock = getBlock (location);
		if ((nextBlock == null) || (nextBlock.IsStructural()))
			return true;
		else
			return false;
		}

	private boolean IsThereSomethingToBlastOnTheRight (Sprite theRunner) {
		Block thisBlock, nextBlock;
		BlockLocation location;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		location = thisBlock.Location.locationToTheLowerRight();
		nextBlock = getBlock (location);
		if ((nextBlock != null) && nextBlock.Laserable())
			return true;
		else
			return false;
		}

	private boolean IsThereSomethingToBlastOnTheLeft (Sprite theRunner) {
		Block thisBlock, nextBlock;
		BlockLocation location;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		location = thisBlock.Location.locationToTheLowerLeft();
		nextBlock = getBlock (location);
		if ((nextBlock != null) && nextBlock.Laserable())
			return true;
		else
			return false;
		}

	private boolean IsThereSolidGroundOnTheLeft (Sprite theRunner) {
		Block thisBlock, nextBlock;
		BlockLocation location;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		location = thisBlock.Location.locationToTheLowerLeft();
		nextBlock = getBlock (location);
		if ((nextBlock == null) || (nextBlock.IsStructural()))
			return true;
		else
			return false;
		}

	private boolean IsThereLadderBelow (Sprite theRunner) {
		Block thisBlock, nextBlock;
		BlockLocation location;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		location = thisBlock.Location.locationBelow();
		nextBlock = getBlock (location);
		if ((nextBlock != null) && (nextBlock.Type == BlockTypes.LADDER))
			return true;
		else
			return false;
		}

	private boolean IsThereLadderAbove (Sprite theRunner) {
		Block thisBlock, nextBlock;
		BlockLocation location;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		location = thisBlock.Location.locationAbove();
		nextBlock = getBlock (location);
		if ((nextBlock != null) && (nextBlock.Type == BlockTypes.LADDER))
			return true;
		else
			return false;
		}

	private boolean RunnerIsOnSomethingSolid (Sprite theRunner) {
		Block thisBlock, blockBelow;
		BlockLocation location;

		if ((theRunner.Image.movieType == MovieType.CLIMBING_DOWN) || 
			(theRunner.Image.movieType == MovieType.CLIMBING_UP) ||
			(theRunner.Image.movieType == MovieType.HANGING_ON_LADDER) ||
			(theRunner.Image.movieType == MovieType.LEAPING_OFF_OF_LADDER) ||
			(theRunner.Image.movieType == MovieType.LEAPING_ONTO_LADDER))
			return true;
		//System.err.println ("Cavern.RunnerIsOnSomethingSolid: image type = " + theRunner.Image.movieType);
		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		location = thisBlock.Location.locationBelow();
		blockBelow = getBlock (location);
		if (!objectIsOnBlockFloor(theRunner.Location)) 
			return false;
		if ((blockBelow != null) && 
			((blockBelow.Type != BlockTypes.LADDER) && (!blockBelow.IsStructural())))
			return false;
		putRunnerOnBlock (theRunner);
		return true;
		}

	private void checkForGold (Sprite theRunner) {
		Block thisBlock;

		thisBlock = getBlockRunnerIsIn (theRunner.Location);
		if (thisBlock.Type == BlockTypes.GOLD_1) {
			pling.play();
			thisBlock.Type = BlockTypes.EMPTY;
			thisBlock.blockImage = null;
			View.getChildren().remove (thisBlock.blockView);
         TotalGold--;
         if (TotalGold == 0) {
            DisplayEscape ();
            }
			}
		}

	private void upDateMovie (Sprite theActor, ActorMovies theMovies) {
		//System.err.println ("upDateMovie: entering with movie type " + theActor.Image.movieType);
		if (theActor.movieState == MovieState.PAUSED) 
			return;
		if (theActor.Image.movieType == MovieType.RUNNING) {
         if (theActor.Orientation == Direction.RIGHT) {
            if (!canRunnerMoveRight (theActor)) {
               theActor.setImage (theMovies.Turned);
               centerRunnerInBlock (theActor);
               theActor.Location.setX (theActor.Location.getX() - 8);
               return;
               }
            else if (IsRunnerAtRope (theActor)) {
               // force runner to use rope if it is present, which is how MMR did it.
               theActor.setImage (theMovies.HangingOnRope);
               theActor.Location.setX (theActor.Location.getX() + 16);
               return;
               }
            else if (!RunnerIsOnSomethingSolid (theActor)) {
               theActor.setImage (theMovies.Falling);
               theActor.Orientation = Direction.FACING;
               centerRunnerInBlock (theActor);
               return;
               }
            else if (!IsThereSolidGroundOnTheRight (theActor)) {
               if (IsRunnerNextToLadderOnTheRight (theActor)) { 
                  theActor.setImage (theMovies.LeapingOntoLadder);
                  centerRunnerInBlock (theActor);
                  return;
                  }
               }
            }
		   else {
            if (!canRunnerMoveLeft (theActor)) {
               theActor.setImage (theMovies.Turned);
               centerRunnerInBlock (theActor);
               theActor.Location.setX (theActor.Location.getX() + 8);
               return;
               }
            else if (IsRunnerAtRope (theActor)) {
               // force runner to use rope if it is present, which is how MMR did it.
               theActor.setImage (theMovies.HangingOnRope);
               theActor.Location.setX (theActor.Location.getX() - 16);
               return;
               }
            else if (!RunnerIsOnSomethingSolid (theActor)) {
               theActor.setImage (theMovies.Falling);
               theActor.Orientation = Direction.FACING;
               centerRunnerInBlock (theActor);
               return;
               }
            else if (!IsThereSolidGroundOnTheLeft (theActor)) {
               if (IsRunnerNextToLadderOnTheLeft (theActor)) {
                  theActor.setImage (theMovies.LeapingOntoLadder);
                  centerRunnerInBlock (theActor);
                  return;
                  }
               }
            }
         }
		else if (theActor.Image.movieType == MovieType.CLIMBING_DOWN) {
			if (!canRunnerMoveDown (theActor)) {
				theActor.movieState = MovieState.PAUSED;
				putRunnerOnBlock (theActor);
				return;
				}
			else if (!IsRunnerAtLadder (theActor) && !IsThereLadderBelow (theActor)) {
				theActor.setImage (theMovies.Falling);
            theActor.Orientation = Direction.FACING;
				return;
				}
			}
		else if (theActor.Image.movieType == MovieType.CLIMBING_UP) {
			if (!IsRunnerAtLadder (theActor)) {
				theActor.setImage (theMovies.Facing);
				putRunnerOnBlock (theActor);
				return;
				}
			else if (!canRunnerMoveUp (theActor)) {
				theActor.movieState = MovieState.PAUSED;
				return;
				}
			}
		else if (theActor.Image.movieType == MovieType.HANGING_ON_ROPE) {
         if (theActor.Orientation == Direction.RIGHT) {
            if (!IsRunnerAtRope (theActor)) {
               if (RunnerIsOnSomethingSolid (theActor)) {
                  theActor.setImage (theMovies.Running);
                  putRunnerOnBlock (theActor);
                  return;
                  }
               else {
                  theActor.setImage (theMovies.Falling);
                  theActor.Orientation = Direction.FACING;
                  centerRunnerInBlock (theActor);
                  return;
                  }
               }
            else if (!canRunnerMoveRight (theActor)) {
               theActor.movieState = MovieState.PAUSED;
               theActor.Location.setX (theActor.Location.getX() + 16);
               return;
               }
            else if (!IsThereSolidGroundOnTheRight (theActor)) {
               if (IsRunnerNextToLadderOnTheRight (theActor)) { 
                  theActor.setImage (theMovies.LeapingOntoLadder);
                  centerRunnerInBlock (theActor);
                  return;
                  }
               }
            }
         else {
            if (!IsRunnerAtRope (theActor)) {
               if (RunnerIsOnSomethingSolid (theActor)) {
                  theActor.setImage (theMovies.Running);
                  putRunnerOnBlock (theActor);
                  return;
                  }
               else {
                  theActor.setImage (theMovies.Falling);
                  theActor.Orientation = Direction.FACING;
                  centerRunnerInBlock (theActor);
                  return;
                  }
               }
            else if (!canRunnerMoveLeft (theActor)) {
               theActor.movieState = MovieState.PAUSED;
               theActor.Location.setX (theActor.Location.getX() - 16);
               return;
               }
            else if (!IsThereSolidGroundOnTheLeft (theActor)) {
               if (IsRunnerNextToLadderOnTheLeft (theActor)) {
                  theActor.setImage (theMovies.LeapingOntoLadder);
                  centerRunnerInBlock (theActor);
                  return;
                  }
               }
            }
         }
		else if (theActor.Image.movieType == MovieType.HANGING_ON_LADDER) {
         // set the last key to null to cause the key processor to reprocess any key held down before the end of the leap onto the ladder 
         lastKeyCode = null;
         }
      else if ((theActor.Image.movieType != MovieType.FALLING) && !RunnerIsOnSomethingSolid (theActor)) {
			theActor.setImage (theMovies.Falling);
         theActor.Orientation = Direction.FACING;
			centerRunnerInBlock (theActor);
			return;
			}
		theActor.Location.setX (theActor.Location.getX() + (theActor.Image.xDelta * theActor.getScale()));
		theActor.Location.setY (theActor.Location.getY() + (theActor.Image.yDelta * theActor.getScale()));
		theActor.Image = theActor.Image.nextImage;
      
		//System.err.println ("upDateMovie: leaving with movie type " + theRunner.Image.movieType);
		}

	private void processDebuggingKeyPresses (ArrayList<String> keysPressed) {
		if (keysPressed.contains ("R")) {
			// dump runner debug info
			System.err.println ("RUNNER INFO:");
			System.err.println ("	location is " + theRunner.Location.getX() + ", " + theRunner.Location.getY());
			Block thisBlock = getBlockRunnerIsIn (theRunner.Location);
			System.err.println ("	in block " + thisBlock.Location.getX() + ", " + thisBlock.Location.getY());
			System.err.println ("	offset in block is " + theRunner.Location.getX() % CONSTANTS.BLOCK_WIDTH + ", " + theRunner.Location.getY() % CONSTANTS.BLOCK_HEIGHT);
			System.err.println ("	movie type " + theRunner.Image.movieType);
			System.err.println ("	movie state is " + theRunner.movieState);
			System.err.println ("	orientation is " + theRunner.Orientation);
			keysPressed.remove ("R");
			if (keysPressed.isEmpty())
				// always have something at the head of the queue
				keysPressed.add(CONSTANTS.NO_KEY_PRESSED);
			}
		}
   
   private void processKeyPresses (ArrayList<String> keysPressed) {
      // use the key that is on the front of the input fifo (oldest pressed key that is still pressed)
      String code = keysPressed.get (0);
         
      if (lastKeyCode == code)
         return;
		lastKeyCode = code;
      // user changed the keyboard input so
		// if the runner is moving in response to the last user keypress user stop him where he is
      if (theRunner.Image.movieType == MovieType.RUNNING) 
         theRunner.setImage (theRunnerMovies.Turned);
      else if ((theRunner.Image.movieType == MovieType.CLIMBING_DOWN) ||
               (theRunner.Image.movieType == MovieType.CLIMBING_UP) ||
               (theRunner.Image.movieType == MovieType.HANGING_ON_ROPE))
         theRunner.movieState = MovieState.PAUSED;
		// some key is pressed so see if it is a runner control key
      if (code == CONSTANTS.PAGE_UP) {  // right side ray gun
         if (IsThereSomethingToBlastOnTheRight (theRunner)) {
            laserBlast.play();
            theRunner.setImage (theRunnerMovies.firingRaygun);
            theRunner.Orientation = Direction.RIGHT;
            centerRunnerInBlock (theRunner);
            raygunBlast = new Sprite (new MovableLocationType(theRunner.Location.getX()+44, theRunner.Location.getY()), theRunnerMovies.raygunBlast, Direction.RIGHT);
            View.getChildren().add (raygunBlast.View);
            }
         }
      if (code == CONSTANTS.HOME) {     // left side ray gun
         if (IsThereSomethingToBlastOnTheLeft (theRunner)) {
            laserBlast.play();
            theRunner.setImage (theRunnerMovies.firingRaygun);
            theRunner.Orientation = Direction.LEFT;
            centerRunnerInBlock (theRunner);
            raygunBlast = new Sprite (new MovableLocationType(theRunner.Location.getX()-44, theRunner.Location.getY()), theRunnerMovies.raygunBlast, Direction.LEFT);
            View.getChildren().add (raygunBlast.View);
            }
         }
      else if (code == CONSTANTS.RIGHT_ARROW) {
         if (canRunnerMoveRight (theRunner)) {
            if ((theRunner.Image.movieType == MovieType.CLIMBING_DOWN) ||
                (theRunner.Image.movieType == MovieType.CLIMBING_UP) ||
                (theRunner.Image.movieType == MovieType.HANGING_ON_LADDER)) {
               theRunner.setImage (theRunnerMovies.LeapingOffOfLadder);
               theRunner.Orientation = Direction.RIGHT;
               putRunnerOnBlock (theRunner);
               }
            else if (theRunner.Image.movieType == MovieType.HANGING_ON_ROPE) {
               theRunner.movieState = MovieState.PLAYING;
               theRunner.Orientation = Direction.RIGHT;
               }
            else {
               theRunner.setImage (theRunnerMovies.Running);
               theRunner.Orientation = Direction.RIGHT;
               }
            }
         }
      else if (code == CONSTANTS.LEFT_ARROW) {
         if (canRunnerMoveLeft (theRunner)) {
            if ((theRunner.Image.movieType == MovieType.CLIMBING_DOWN) ||
                (theRunner.Image.movieType == MovieType.CLIMBING_UP) ||
                (theRunner.Image.movieType == MovieType.HANGING_ON_LADDER)) {
               theRunner.setImage (theRunnerMovies.LeapingOffOfLadder);
               theRunner.Orientation = Direction.LEFT;
               putRunnerOnBlock (theRunner);
               }
            else if (theRunner.Image.movieType == MovieType.HANGING_ON_ROPE) {
               theRunner.movieState = MovieState.PLAYING;
               theRunner.Orientation = Direction.LEFT;
               }
            else {
               theRunner.setImage (theRunnerMovies.Running);
               theRunner.Orientation = Direction.LEFT;
               }
            }
         }
      else if (code == CONSTANTS.CLEAR) {
         if (theRunner.Image.movieType != MovieType.CLIMBING_DOWN) {
            if (IsRunnerAtLadder (theRunner) || IsThereLadderBelow (theRunner)) {
               theRunner.setImage (theRunnerMovies.ClimbingDown);
               theRunner.Orientation = Direction.RIGHT;
               centerRunnerInBlock (theRunner);
               }
            else if (canRunnerMoveDown (theRunner)) {
               theRunner.setImage (theRunnerMovies.Falling);
               theRunner.Orientation = Direction.FACING;
               centerRunnerInBlock (theRunner);
               }
            }
         else if (canRunnerMoveDown (theRunner))
            theRunner.movieState = MovieState.PLAYING;
         }
      else if (code == CONSTANTS.UP_ARROW) {
         if (theRunner.Image.movieType != MovieType.CLIMBING_UP) {
            if (IsRunnerAtLadder (theRunner)) {
               theRunner.setImage (theRunnerMovies.ClimbingUp);
               theRunner.Orientation = Direction.RIGHT;
               centerRunnerInBlock (theRunner);
               }
            }
         else if (canRunnerMoveUp (theRunner))
            theRunner.movieState = MovieState.PLAYING;
         }
      }

   void ProcessRunner (ArrayList<String> keysPressed) {
      // FIRST: process falling runner (only gravaty is in control) and then any user input runner control change
		if (theRunner.Image.movieType == MovieType.FALLING) {
			if (RunnerIsOnSomethingSolid (theRunner)) {
				theRunner.setImage (theRunnerMovies.Facing);
            // set the last key to null to cause the key processor to reprocess any key held down before the end of the fall 
            lastKeyCode = null;
            }
         else if (IsRunnerAtRope (theRunner) && objectIsOnBlockFloor(theRunner.Location)) {
            // falling runner always grabs a rope if he has the opportunity
            theRunner.setImage (theRunnerMovies.HangingOnRope);
            theRunner.movieState = MovieState.PAUSED;
            // set the last key to null to cause the key processor to reprocess any key held down before the end of the fall 
            lastKeyCode = null;
            }
			}
		else if (theRunner.Image.movieType == MovieType.LEAPING_OFF_OF_LADDER) {
			// Let movie play out before processing any keyboard input
			}
		else if (theRunner.Image.movieType == MovieType.LEAPING_ONTO_LADDER) {
			// Let movie play out before processing any keyboard input
			}
		else if (theRunner.Image.movieType == MovieType.FIRING_RAYGUN) {
			// Let movie play out before processing any keyboard input
			}
 		else {
         // check for runner control keys
		   processKeyPresses (keysPressed);
         }
      }
   
   void ProcessTrolls() {
      ArrayList<Direction> PrefferredDirections = new ArrayList();
			
      //System.err.println ("ProcessTrolls: Enterring - MovieType = " + Troll1.Image.movieType);
      
		if (Troll1.Image.movieType == MovieType.FALLING) {
			if (RunnerIsOnSomethingSolid (Troll1)) 
				Troll1.setImage (theTrollMovies.Turned);
         else if (IsRunnerAtRope (Troll1) && objectIsOnBlockFloor(Troll1.Location)) {
            // falling troll always grabs a rope if they has the opportunity
            Troll1.setImage (theTrollMovies.HangingOnRope);
            Troll1.movieState = MovieState.PAUSED;
            }
         else
            return;
         }
		else if (Troll1.Image.movieType == MovieType.LEAPING_OFF_OF_LADDER) {
			// Let movie play out before processing any keyboard input
         return;
			}
		else if (Troll1.Image.movieType == MovieType.LEAPING_ONTO_LADDER) {
			// Let movie play out before processing any keyboard input
         return;
			}
      int xDelta = theRunner.Location.getX() - Troll1.Location.getX();
      Direction PreferredX = Direction.HorizontalDirection(xDelta);
      int yDelta = theRunner.Location.getY() - Troll1.Location.getY();
      Direction PreferredY = Direction.VerticalDirection(yDelta);
      if ((abs(xDelta) < 8) && (abs(yDelta) < 8)) {
         // TODO: runner caught, add game over code
         Troll1.movieState = MovieState.PAUSED;
         return;
         }
      if (abs(yDelta) < 8) {
         // always prefer climbing unless troll is at the same level as the runner
         PrefferredDirections.add (PreferredX);
         PrefferredDirections.add (PreferredY);
         }
      else {
         PrefferredDirections.add (PreferredY);
         PrefferredDirections.add (PreferredX);
         }
      PrefferredDirections.add(Direction.Opposite(PrefferredDirections.get(1)));
      PrefferredDirections.add(Direction.Opposite(PrefferredDirections.get(0)));
      System.err.println ("ProcessTrolls: PrefferredDirections = " + PrefferredDirections);
      for (Direction direction : PrefferredDirections) {
         System.err.println ("ProcessTrolls: for loop - direction = " + direction);
         boolean KeepChecking = false;
         switch (direction) {
            case RIGHT:
               if (abs(xDelta) < 8) 
                  Troll1.movieState = MovieState.PAUSED;
               else if (canRunnerMoveRight (Troll1)) {
                  if ((Troll1.Image.movieType == MovieType.CLIMBING_DOWN) ||
                      (Troll1.Image.movieType == MovieType.CLIMBING_UP) ||
                      (Troll1.Image.movieType == MovieType.HANGING_ON_LADDER)) {
                     Troll1.setImage (theTrollMovies.LeapingOffOfLadder);
                     Troll1.Orientation = Direction.RIGHT;
                     putRunnerOnBlock (Troll1);
                     }
                  else if (Troll1.Image.movieType == MovieType.HANGING_ON_ROPE) {
                     Troll1.movieState = MovieState.PLAYING;
                     Troll1.Orientation = Direction.RIGHT;
                     }
                  else {
                     if (Troll1.Image.movieType != MovieType.RUNNING) 
                        Troll1.setImage (theTrollMovies.Running);
                     Troll1.movieState = MovieState.PLAYING;
                     Troll1.Orientation = Direction.RIGHT;
                     }
                  }
               else
                  KeepChecking = true;
               break;
            case LEFT:
               if (abs(xDelta) < 8) 
                  Troll1.movieState = MovieState.PAUSED;
               else if (canRunnerMoveLeft (Troll1)) {
                  if ((Troll1.Image.movieType == MovieType.CLIMBING_DOWN) ||
                      (Troll1.Image.movieType == MovieType.CLIMBING_UP) ||
                      (Troll1.Image.movieType == MovieType.HANGING_ON_LADDER)) {
                     Troll1.setImage (theTrollMovies.LeapingOffOfLadder);
                     Troll1.Orientation = Direction.LEFT;
                     putRunnerOnBlock (Troll1);
                     }
                  else if (Troll1.Image.movieType == MovieType.HANGING_ON_ROPE) {
                     Troll1.movieState = MovieState.PLAYING;
                     Troll1.Orientation = Direction.LEFT;
                     }
                  else {
                     if (Troll1.Image.movieType != MovieType.RUNNING) 
                        Troll1.setImage (theTrollMovies.Running);
                     Troll1.movieState = MovieState.PLAYING;
                     Troll1.Orientation = Direction.LEFT;
                     }
                  }
               else
                  KeepChecking = true;
               break;
            case DOWN:
               if (abs(yDelta) < 8) 
                  Troll1.movieState = MovieState.PAUSED;
               else if (canRunnerMoveDown (Troll1)) {
                  if (IsRunnerAtLadder (Troll1) || IsThereLadderBelow (Troll1)) {
                     if (Troll1.Image.movieType != MovieType.CLIMBING_DOWN) {
                        Troll1.setImage (theTrollMovies.ClimbingDown);
                        Troll1.Orientation = Direction.RIGHT;
                        centerRunnerInBlock (Troll1);
                        }
                     else
                        Troll1.movieState = MovieState.PLAYING;
                     }
                  else {
                     Troll1.setImage (theTrollMovies.Falling);
                     Troll1.Orientation = Direction.FACING;
                     centerRunnerInBlock (Troll1);
                     }
                  }
               else 
                  KeepChecking = true;
               break;
            case UP:
               if (abs(yDelta) < 8) 
                  Troll1.movieState = MovieState.PAUSED;
               else if (canRunnerMoveUp (Troll1) && IsRunnerAtLadder (Troll1)) {
                  if (Troll1.Image.movieType != MovieType.CLIMBING_UP) {
                     Troll1.setImage (theTrollMovies.ClimbingUp);
                     Troll1.Orientation = Direction.RIGHT;
                     centerRunnerInBlock (Troll1);
                     }
                  else 
                     Troll1.movieState = MovieState.PLAYING;
                  }
               else
                  KeepChecking = true;
            }
         if (!KeepChecking)
            break;
         }         
      System.err.println ("ProcessTrolls: Leaving - MovieType = " + Troll1.Image.movieType);
      }

	public int frameHandler (ArrayList<String> keysPressed) {
		// first process any runner changes that must happen (gravity has priority over user input)
		// second update any active elements to their next state, along with any of their effects on the cavern
		// third display the cavern with all of its the active elements

      // check for debugging key presses before doing anything to the runner
		processDebuggingKeyPresses (keysPressed);

      // check for any gold to get
		checkForGold (theRunner);

      // FIRST: process the actors
      ProcessRunner(keysPressed);
      ProcessTrolls();
       
      // SECOND: update any active elements in the cavern
		upDateMovie (theRunner, theRunnerMovies);
      //if ((frameCounter++ % 1) == 0)
      upDateMovie (Troll1, theTrollMovies);
		/*
		upDateMovie (Runner3);
		upDateMovie (Runner4);
		upDateMovie (Runner5);
		upDateMovie (Runner6);
		upDateMovie (Runner7);
		upDateMovie (Runner8);

		// reset the test runners to their starting positions to creat a looping effect
		if (++frameCounter % 145 == 0) {
		    Runner2.Location.setX(1188);
		    Runner3.Location.setX(0);
		    Runner4.Location.setX(1188);
		    Runner5.Location.setX(0);
		    Runner6.Location.setX(1188);
		    Runner7.Location.setX(0);
		    Runner8.Location.setX(1188);
			}
		*/

		// process any 'active' blocks to restore them
		for (ActiveBlock activeBlock : new ArrayList<> (activeBlocks)) {
			if (--activeBlock.delayAmount == 0) {
				Block block = getBlock (activeBlock.theBlock.Location);
				block.Type = activeBlock.theBlock.Type;
				block.blockImage = activeBlock.theBlock.blockImage;
            block.blockView.setImage(block.blockImage);
				block.Item = activeBlock.theBlock.Item;
				activeBlocks.remove (activeBlock);
				}
			}

		// THIRD: display any changes in the cavern

		// display any raygun blast before displaying the runner so raygun that the runner is holding displays on top of the blast
		if (raygunBlast != null) {
			if (raygunBlast.Image.nextImage != null) {      // still showing the blast
				raygunBlast.Image = raygunBlast.Image.nextImage;
				raygunBlast.Location.setX (raygunBlast.Location.getX() + raygunBlast.Image.xDelta);
				raygunBlast.Location.setY (raygunBlast.Location.getY() + raygunBlast.Image.yDelta);
            raygunBlast.View.setImage (raygunBlast.Image.theImage);
            raygunBlast.View.setX (raygunBlast.Location.getX());
            raygunBlast.View.setY (raygunBlast.Location.getY());
            raygunBlast.View.setScaleX (raygunBlast.getScale());
				}
			else {    // blast is complete, so delete the block and save it in the active blocks list to be restored later
				Block block =  getBlockRunnerIsIn (raygunBlast.Location);
				Block newBlock = new Block (block);
				ActiveBlock activeBlock = new ActiveBlock(newBlock, 120);
				activeBlocks.add (activeBlock);
				block.blockImage = null;
            block.blockView.setImage(null);
				block.Type = BlockTypes.EMPTY;
				View.getChildren().remove (raygunBlast.View);
				raygunBlast = null;
				}
			}
		
		// update the troll in the View
      Troll1.View.setImage (Troll1.Image.theImage);
      Troll1.View.setX (Troll1.Location.getX());
      Troll1.View.setY (Troll1.Location.getY());
      Troll1.View.setScaleX (Troll1.getScale());

		// update the user controlled runner in the View
      theRunner.View.setImage (theRunner.Image.theImage);
      theRunner.View.setX (theRunner.Location.getX());
      theRunner.View.setY (theRunner.Location.getY());
      theRunner.View.setScaleX (theRunner.getScale());
      return TotalGold;
		}

	public void display () {
		Gc.drawImage (Images.mossWorld, 0, 0);   // display background
		// display any blocks that have images at their correct graphical location in the window
		for (int x=0; x < Width; x++)
			for (int y=0; y < Height; y++)
				if (Blocks[x][y].blockImage != null) 
					Gc.drawImage (Blocks[x][y].blockImage, 
									  Blocks[x][y].Location.getX()*CONSTANTS.BLOCK_WIDTH,
									  Blocks[x][y].Location.getY()*CONSTANTS.BLOCK_HEIGHT);
		if (theRunner != null)
			Gc.drawImage (theRunner.Image.theImage, theRunner.Location.getX(), theRunner.Location.getY());
		if (Troll1 != null)
			Gc.drawImage (Troll1.Image.theImage, Troll1.Location.getX(), Troll1.Location.getY());
		}

	public void save (String fileName) {
		System.err.println ("save: filename = " + fileName);
		if (!fileName.contains (".ser"))
			fileName = fileName + ".ser";
		try (OutputStream file = new FileOutputStream (fileName);
			  OutputStream buffer = new BufferedOutputStream (file);
			  ObjectOutput output = new ObjectOutputStream (buffer);) {
			for (int x=0; x < Width; x++)
				for (int y=0; y < Height; y++) {
					output.writeObject (Blocks[x][y].Type);
               }
			output.writeObject (theRunner.Location);
         if (Troll1 != null)
            output.writeObject (Troll1.Location);
			}
		catch (IOException ex) {
			System.err.println ("IO exception occured in save: " + ex.toString());
			}			
		}
	
	public void restore (String fileName) {
		if (!fileName.contains (".ser"))
			fileName = fileName + ".ser";
		try (InputStream file = new FileInputStream (fileName);
			  InputStream buffer = new BufferedInputStream (file);
			  ObjectInput input = new ObjectInputStream (buffer);) {
			for (int x=0; x < Width; x++)
				for (int y=0; y < Height; y++) {
               Block block = Blocks[x][y];
					block.Type = (BlockTypes)input.readObject();
					if (block.Type == BlockTypes.EMPTY) 
						block.blockImage = null;
               else
                  block.blockImage = Images.getImage(block.Type);
               if (block.blockImage != null) {
                  block.blockView = new ImageView (block.blockImage);
                  block.blockView.setX (block.Location.getX()*CONSTANTS.BLOCK_WIDTH);
                  block.blockView.setY (block.Location.getY()*CONSTANTS.BLOCK_HEIGHT);
                  }
					}
			MovableLocationType location = (MovableLocationType)input.readObject();
			if (theRunner == null)
				theRunner = new Sprite (location, theRunnerMovies.Facing, Direction.FACING);
			else
				theRunner.Location = location;
			location = (MovableLocationType)input.readObject();
			if (Troll1 == null)
				Troll1 = new Sprite (location, theTrollMovies.Facing, Direction.FACING);
			else
				Troll1.Location = location;
			}
		catch (ClassNotFoundException ex) {
			System.err.println ("ClassNotFoundException exception occured");
			}
		catch (IOException ex) {
			System.err.println ("IO exception occured in restore: " + ex.toString());
			}
		}

	}
