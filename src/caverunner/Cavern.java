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

/**
 *
 * @author wcb
 */
class Cavern {
	private GraphicsContext Gc;
   Group View;
	private int Width = 0;
	private int Height = 0;
	private BlockType[][] Blocks;
	private int frameCounter = 0;
	private Runner theRunner;
	private String lastKeyCode = "";
	private RaygunBlast raygunBlast;
	private ArrayList<ActiveBlock> activeBlocks = new ArrayList<>();
   private Runner Troll1;
   private int TotalGold = 0;

	AudioClip pling = new AudioClip ("file:Pling.mp3");
	AudioClip laserBlast = new AudioClip ("file:LaserBlast.mp3");

	//Runner Troll1 = new Runner (new MovableLocationType(0, 132), Images.R_TrollRunning);
	// test runners
	/*
	Runner Runner3 = new Runner (new MovableLocationType(0, 220), Images.R_RunnerImages);
	Runner Runner4 = new Runner (new MovableLocationType(1188, 308), Images.L_RunnerImages);
	Runner Runner5 = new Runner (new MovableLocationType(0, 396), Images.R_RunnerImages);
	Runner Runner6 = new Runner (new MovableLocationType(1188, 484), Images.L_RunnerImages);
	Runner Runner7 = new Runner (new MovableLocationType(0, 572), Images.R_RunnerImages);
	Runner Runner8 = new Runner (new MovableLocationType(1188, 660), Images.L_RunnerImages);
	*/

	Cavern (int width, int height, GraphicsContext gc) {
		Gc = gc;
		Blocks = new BlockType[width][height];
		Width = width;
		Height = height;
		for (int x=0; x < Width; x++)
			for (int y=0; y < Height; y++) 
				Blocks [x] [y] = new BlockType (new BlockLocationType (x, y));
		}
   
   Cavern (int width, int height, Group theView) {
		View = theView;
		Blocks = new BlockType[width][height];
		Width = width;
		Height = height;
		for (int x=0; x < Width; x++)
			for (int y=0; y < Height; y++) 
				Blocks [x] [y] = new BlockType (new BlockLocationType (x, y));
      //Troll1 = new Runner (new MovableLocationType(0, 132), Images.R_TrollRunning);
		}
   
   private void DisplayEscape () {
      View.getChildren().remove (theRunner.theirView);
      for (int x=0; x < Width; x++)
			for (int y=0; y < Height; y++)
			   if (Blocks[x][y].blockImage != null) {
               if (Blocks[x][y].Type == BlockTypes.EXIT)
                  View.getChildren().add(Blocks[x][y].blockView);
               if (Blocks[x][y].Type == BlockTypes.HIDDEN_LADDER) {
                  Blocks[x][y].Type = BlockTypes.LADDER;
						Blocks[x][y].blockView.setImage(Images.visableLadder);
                  View.getChildren().add(Blocks[x][y].blockView);
                  }
               }
      View.getChildren().add (theRunner.theirView);
      }
   
   public int LoadCavernIntoView () {
      View.getChildren().clear();
      View.getChildren().add (new ImageView(Images.mossWorld));
      for (int x=0; x < Width; x++)
			for (int y=0; y < Height; y++)
			   if (Blocks[x][y].blockImage != null) {
               if ((Blocks[x][y].Type != BlockTypes.EXIT) &&
                   (Blocks[x][y].Type != BlockTypes.HIDDEN_LADDER))
                  View.getChildren().add(Blocks[x][y].blockView);
               if (Blocks[x][y].Type == BlockTypes.GOLD_1) 
                  TotalGold++;
               }
      View.getChildren().add (theRunner.theirView);
      //View.getChildren().add (Troll1.theirView);
      return TotalGold;
      }

	public void addRunner (MovableLocationType location) {
		theRunner = new Runner (location, Images.C_RunnerStanding);
		}

	public Runner getRunner () {
		return theRunner;
		}

	public boolean setBlock (BlockType Block) {
		if (Block.Location.getX() > Width)
			return false;
		if (Block.Location.getY() > Height)
			return false;
      Block.blockView = new ImageView (Block.blockImage);
      Block.blockView.setX (Block.Location.getX()*CONSTANTS.BLOCK_WIDTH);
      Block.blockView.setY (Block.Location.getY()*CONSTANTS.BLOCK_HEIGHT);
		Blocks[Block.Location.getX()][Block.Location.getY()] = Block;
		return true;
		}

	public BlockType getBlock (BlockLocationType location) {
		if ((0 > location.getX()) || (location.getX() >= Width))
			return null;
		if ((0 > location.getY()) || (location.getY() >= Height))
			return null;
		return Blocks[location.getX()][location.getY()];
		}

	private BlockType getBlockRunnerIsIn (MovableLocationType location) {
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

	private void centerRunnerInBlock (Runner theRunner) {
		BlockLocationType blockLocation;

		blockLocation = getBlockRunnerIsIn (theRunner.theirLocation).Location;
		theRunner.theirLocation.setX (blockLocation.getX()*CONSTANTS.BLOCK_WIDTH);
		}

	private void putRunnerOnBlock (Runner theRunner) {
		BlockLocationType blockLocation;

		blockLocation = getBlockRunnerIsIn (theRunner.theirLocation).Location;
		theRunner.theirLocation.setY (blockLocation.getY()*CONSTANTS.BLOCK_HEIGHT);
		}

	private boolean canRunnerMoveRight (Runner theRunner) {
		BlockType thisBlock, nextBlock;
		BlockLocationType location;

		thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
		location = thisBlock.Location.locationToTheRight();
		nextBlock = getBlock (location);
		if ((nextBlock == null) || (nextBlock.Type == BlockTypes.DIGABLE)) 
			return false;
		else
			return true;
		}

	private boolean canRunnerMoveLeft (Runner theRunner) {
		BlockType thisBlock, nextBlock;
		BlockLocationType location;

		thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
		location = thisBlock.Location.locationToTheLeft();
		nextBlock = getBlock (location);
		if ((nextBlock == null) || (nextBlock.Type == BlockTypes.DIGABLE)) 
			return false;
		else
			return true;
		}

	private boolean canRunnerMoveDown (Runner theRunner) {
		BlockType thisBlock, nextBlock;
		BlockLocationType location;

		thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
		location = thisBlock.Location.locationBelow();
		nextBlock = getBlock (location);
		if ((nextBlock == null) || (nextBlock.Type == BlockTypes.DIGABLE)) 
			return false;
		else
			return true;
		}

	private boolean canRunnerMoveUp (Runner theRunner) {
		BlockType thisBlock, nextBlock;
		BlockLocationType location;

		thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
		location = thisBlock.Location.locationAbove();
		nextBlock = getBlock (location);
		if ((nextBlock == null) || (nextBlock.Type == BlockTypes.DIGABLE)) 
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

	private boolean IsRunnerAtLadder (Runner theRunner) {
		BlockType thisBlock;

		thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
		if (thisBlock.Type == BlockTypes.LADDER)
			return true;
		else
			return false;
		}

	private boolean IsRunnerAtRope (Runner theRunner) {
		BlockType thisBlock;

		thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
		if (thisBlock.Type == BlockTypes.ROPE)
			return true;
		else
			return false;
		}

	private boolean IsRunnerNextToLadderOnTheRight (Runner theRunner) {
		BlockType thisBlock, nextBlock;
		BlockLocationType location;

		thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
		location = thisBlock.Location.locationToTheRight();
		nextBlock = getBlock (location);
		if (nextBlock.Type == BlockTypes.LADDER)
			return true;
		else
			return false;
		}

	private boolean IsRunnerNextToLadderOnTheLeft (Runner theRunner) {
		BlockType thisBlock, nextBlock;
		BlockLocationType location;

		thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
		location = thisBlock.Location.locationToTheLeft();
		nextBlock = getBlock (location);
		if (nextBlock.Type == BlockTypes.LADDER)
			return true;
		else
			return false;
		}

	private boolean IsRunnerNextToRopeOnTheLeft (Runner theRunner) {
		BlockType thisBlock, nextBlock;
		BlockLocationType location;

		thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
		location = thisBlock.Location.locationToTheLeft();
		nextBlock = getBlock (location);
		if (nextBlock.Type == BlockTypes.ROPE)
			return true;
		else
			return false;
		}

	private boolean IsRunnerNextToRopeOnTheRight (Runner theRunner) {
		BlockType thisBlock, nextBlock;
		BlockLocationType location;

		thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
		location = thisBlock.Location.locationToTheRight();
		nextBlock = getBlock (location);
		if (nextBlock.Type == BlockTypes.ROPE)
			return true;
		else
			return false;
		}

	private boolean IsThereSolidGroundOnTheRight (Runner theRunner) {
		BlockType thisBlock, nextBlock;
		BlockLocationType location;

		thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
		location = thisBlock.Location.locationToTheLowerRight();
		nextBlock = getBlock (location);
		if ((nextBlock == null) || (nextBlock.Type == BlockTypes.DIGABLE))
			return true;
		else
			return false;
		}

	private boolean IsThereSolidGroundOnTheLeft (Runner theRunner) {
		BlockType thisBlock, nextBlock;
		BlockLocationType location;

		thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
		location = thisBlock.Location.locationToTheLowerLeft();
		nextBlock = getBlock (location);
		if ((nextBlock == null) || (nextBlock.Type == BlockTypes.DIGABLE))
			return true;
		else
			return false;
		}

	private boolean IsThereLadderBelow (Runner theRunner) {
		BlockType thisBlock, nextBlock;
		BlockLocationType location;

		thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
		location = thisBlock.Location.locationBelow();
		nextBlock = getBlock (location);
		if ((nextBlock != null) && (nextBlock.Type == BlockTypes.LADDER))
			return true;
		else
			return false;
		}

	private boolean IsThereLadderAbove (Runner theRunner) {
		BlockType thisBlock, nextBlock;
		BlockLocationType location;

		thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
		location = thisBlock.Location.locationAbove();
		nextBlock = getBlock (location);
		if ((nextBlock != null) && (nextBlock.Type == BlockTypes.LADDER))
			return true;
		else
			return false;
		}

	private boolean RunnerIsOnSomethingSolid (Runner theRunner) {
		BlockType thisBlock, blockBelow;
		BlockLocationType location;

		if ((theRunner.theirImage.movieType == MovieType.CLIMBING_DOWN) || 
			(theRunner.theirImage.movieType == MovieType.CLIMBING_UP) ||
			(theRunner.theirImage.movieType == MovieType.HANGING_ON_LADDER) ||
			//(theRunner.theirImage.movieType == MovieType.HANGING_ON_ROPE_RIGHT) ||
			//(theRunner.theirImage.movieType == MovieType.HANGING_ON_ROPE_LEFT) ||
			(theRunner.theirImage.movieType == MovieType.LEAPING_LEFT_OFF_OF_LADDER) ||
			(theRunner.theirImage.movieType == MovieType.LEAPING_LEFT_ONTO_LADDER) ||
			(theRunner.theirImage.movieType == MovieType.LEAPING_RIGHT_OFF_OF_LADDER) ||
			(theRunner.theirImage.movieType == MovieType.LEAPING_RIGHT_ONTO_LADDER))
			return true;
		//System.err.println ("Cavern.RunnerIsOnSomethingSolid: image type = " + theRunner.theirImage.movieType);
		thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
		location = thisBlock.Location.locationBelow();
		blockBelow = getBlock (location);
		if (!objectIsOnBlockFloor(theRunner.theirLocation)) 
			return false;
		if ((blockBelow != null) && 
			((blockBelow.Type != BlockTypes.LADDER) && (blockBelow.Type != BlockTypes.DIGABLE)))
			return false;
		putRunnerOnBlock (theRunner);
		return true;
		}

	private void checkForGold (Runner theRunner) {
		BlockType thisBlock;

		thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
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

	private void upDateMovie (Runner theRunner) {
		//System.err.println ("upDateMovie: entering with movie type " + theRunner.theirImage.movieType);
		if (theRunner.movieState == MovieState.PAUSED) 
			return;
		if (theRunner.theirImage.movieType == MovieType.RUNNING_RIGHT) {
			if (!canRunnerMoveRight (theRunner)) {
				theRunner.setImage (Images.R_RunnerStanding);
				centerRunnerInBlock (theRunner);
				theRunner.theirLocation.setX (theRunner.theirLocation.getX() - 8);
				return;
				}
         else if (IsRunnerAtRope (theRunner)) {
            // force runner to use rope if it is present, which is how MMR did it.
            theRunner.setImage (Images.R_RunnerHangingOnRope);
            theRunner.theirLocation.setX (theRunner.theirLocation.getX() + 16);
            return;
            }
         else if (!RunnerIsOnSomethingSolid (theRunner)) {
            theRunner.setImage (Images.RunnerFalling);
            centerRunnerInBlock (theRunner);
            return;
            }
 			else if (!IsThereSolidGroundOnTheRight (theRunner)) {
				if (IsRunnerNextToLadderOnTheRight (theRunner)) { 
					theRunner.setImage (Images.R_RunnerLeapingOntoLadder);
					centerRunnerInBlock (theRunner);
					return;
					}
            }
			}
		else if (theRunner.theirImage.movieType == MovieType.RUNNING_LEFT) {
			if (!canRunnerMoveLeft (theRunner)) {
				theRunner.setImage (Images.L_RunnerStanding);
				centerRunnerInBlock (theRunner);
				theRunner.theirLocation.setX (theRunner.theirLocation.getX() + 8);
				return;
				}
         else if (IsRunnerAtRope (theRunner)) {
            // force runner to use rope if it is present, which is how MMR did it.
            theRunner.setImage (Images.L_RunnerHangingOnRope);
            theRunner.theirLocation.setX (theRunner.theirLocation.getX() - 16);
            return;
            }
         else if (!RunnerIsOnSomethingSolid (theRunner)) {
            theRunner.setImage (Images.RunnerFalling);
            centerRunnerInBlock (theRunner);
            return;
            }
			else if (!IsThereSolidGroundOnTheLeft (theRunner)) {
				if (IsRunnerNextToLadderOnTheLeft (theRunner)) {
					theRunner.setImage (Images.L_RunnerLeapingOntoLadder);
					centerRunnerInBlock (theRunner);
					return;
					}
				}
			}
		else if (theRunner.theirImage.movieType == MovieType.CLIMBING_DOWN) {
			if (!canRunnerMoveDown (theRunner)) {
				theRunner.movieState = MovieState.PAUSED;
				putRunnerOnBlock (theRunner);
				return;
				}
			else if (!IsRunnerAtLadder (theRunner) && !IsThereLadderBelow (theRunner)) {
				theRunner.setImage (Images.RunnerFalling);
				return;
				}
			}
		else if (theRunner.theirImage.movieType == MovieType.CLIMBING_UP) {
			if (!IsRunnerAtLadder (theRunner)) {
				theRunner.setImage (Images.C_RunnerStanding);
				putRunnerOnBlock (theRunner);
				return;
				}
			else if (!canRunnerMoveUp (theRunner)) {
				theRunner.movieState = MovieState.PAUSED;
				return;
				}
			}
		else if (theRunner.theirImage.movieType == MovieType.HANGING_ON_ROPE_LEFT) {
			if (!IsRunnerAtRope (theRunner)) {
            if (RunnerIsOnSomethingSolid (theRunner)) {
               theRunner.setImage (Images.L_RunnerImages);
               putRunnerOnBlock (theRunner);
               return;
               }
            else {
               theRunner.setImage (Images.RunnerFalling);
               centerRunnerInBlock (theRunner);
               return;
               }
            }
			else if (!canRunnerMoveLeft (theRunner)) {
				theRunner.movieState = MovieState.PAUSED;
				theRunner.theirLocation.setX (theRunner.theirLocation.getX() - 16);
				return;
            }
			else if (!IsThereSolidGroundOnTheLeft (theRunner)) {
				if (IsRunnerNextToLadderOnTheLeft (theRunner)) {
					theRunner.setImage (Images.L_RunnerLeapingOntoLadder);
					centerRunnerInBlock (theRunner);
					return;
					}
				}
         }
		else if (theRunner.theirImage.movieType == MovieType.HANGING_ON_ROPE_RIGHT) {
			if (!IsRunnerAtRope (theRunner)) {
            if (RunnerIsOnSomethingSolid (theRunner)) {
               theRunner.setImage (Images.R_RunnerImages);
               putRunnerOnBlock (theRunner);
               return;
               }
            else {
               theRunner.setImage (Images.RunnerFalling);
               centerRunnerInBlock (theRunner);
               return;
               }
            }
			else if (!canRunnerMoveRight (theRunner)) {
				theRunner.movieState = MovieState.PAUSED;
				theRunner.theirLocation.setX (theRunner.theirLocation.getX() + 16);
				return;
            }
 			else if (!IsThereSolidGroundOnTheRight (theRunner)) {
				if (IsRunnerNextToLadderOnTheRight (theRunner)) { 
					theRunner.setImage (Images.R_RunnerLeapingOntoLadder);
					centerRunnerInBlock (theRunner);
					return;
					}
            }
         }
		else if (theRunner.theirImage.movieType == MovieType.HANGING_ON_LADDER) {
         // set the last key to null to cause the key processor to reprocess any key held down before the end of the leap onto the ladder 
         lastKeyCode = null;
         }
      else if ((theRunner.theirImage.movieType != MovieType.FALLING) && !RunnerIsOnSomethingSolid (theRunner)) {
			theRunner.setImage (Images.RunnerFalling);
			centerRunnerInBlock (theRunner);
			return;
			}
		theRunner.theirLocation.setX (theRunner.theirLocation.getX() + theRunner.theirImage.xDelta);
		theRunner.theirLocation.setY (theRunner.theirLocation.getY() + theRunner.theirImage.yDelta);
		theRunner.theirImage = theRunner.theirImage.nextImage;
      
		//System.err.println ("upDateMovie: leaving with movie type " + theRunner.theirImage.movieType);
		}

	private void processDebuggingKeyPresses (ArrayList<String> keysPressed) {
		if (keysPressed.contains ("R")) {
			// dump runner debug info
			System.err.println ("RUNNER INFO:");
			System.err.println ("	location is " + theRunner.theirLocation.getX() + ", " + theRunner.theirLocation.getY());
			BlockType thisBlock = getBlockRunnerIsIn (theRunner.theirLocation);
			System.err.println ("	in block " + thisBlock.Location.getX() + ", " + thisBlock.Location.getY());
			System.err.println ("	offset in block is " + theRunner.theirLocation.getX() % CONSTANTS.BLOCK_WIDTH + ", " + theRunner.theirLocation.getY() % CONSTANTS.BLOCK_HEIGHT);
			System.err.println ("	movie type " + theRunner.theirImage.movieType);
			System.err.println ("	movie state is " + theRunner.movieState);
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
      if (theRunner.theirImage.movieType == MovieType.RUNNING_RIGHT) 
         theRunner.setImage (Images.R_RunnerStanding);
      else if (theRunner.theirImage.movieType == MovieType.RUNNING_LEFT)
         theRunner.setImage (Images.L_RunnerStanding);
      else if ((theRunner.theirImage.movieType == MovieType.CLIMBING_DOWN) ||
               (theRunner.theirImage.movieType == MovieType.CLIMBING_UP) ||
               (theRunner.theirImage.movieType == MovieType.HANGING_ON_ROPE_LEFT) ||
               (theRunner.theirImage.movieType == MovieType.HANGING_ON_ROPE_RIGHT))
         theRunner.movieState = MovieState.PAUSED;
		// some key is pressed so see if it is a runner control key
      if (code == CONSTANTS.PAGE_UP) {  // right side ray gun
         laserBlast.play();
         theRunner.setImage (Images.R_firingRaygun);
         centerRunnerInBlock (theRunner);
         raygunBlast = new RaygunBlast (new MovableLocationType(theRunner.theirLocation.getX()+44, theRunner.theirLocation.getY()), Images.R_raygunBlast);
         View.getChildren().add (raygunBlast.theView);
         }
      if (code == CONSTANTS.HOME) {     // left side ray gun
         laserBlast.play();
         theRunner.setImage (Images.L_firingRaygun);
         centerRunnerInBlock (theRunner);
         raygunBlast = new RaygunBlast (new MovableLocationType(theRunner.theirLocation.getX()-44, theRunner.theirLocation.getY()), Images.L_raygunBlast);
         View.getChildren().add (raygunBlast.theView);
         }
      else if (code == CONSTANTS.RIGHT_ARROW) {
         if (canRunnerMoveRight (theRunner)) {
            if ((theRunner.theirImage.movieType == MovieType.CLIMBING_DOWN) ||
                (theRunner.theirImage.movieType == MovieType.CLIMBING_UP) ||
                (theRunner.theirImage.movieType == MovieType.HANGING_ON_LADDER)) {
               theRunner.setImage (Images.R_RunnerLeapingOffOfLadder);
               putRunnerOnBlock (theRunner);
               }
            else if (theRunner.theirImage.movieType == MovieType.HANGING_ON_ROPE_RIGHT)
               theRunner.movieState = MovieState.PLAYING;
            else 
               theRunner.setImage (Images.R_RunnerImages);
            }
         }
      else if (code == CONSTANTS.LEFT_ARROW) {
         if (canRunnerMoveLeft (theRunner)) {
            if ((theRunner.theirImage.movieType == MovieType.CLIMBING_DOWN) ||
                (theRunner.theirImage.movieType == MovieType.CLIMBING_UP) ||
                (theRunner.theirImage.movieType == MovieType.HANGING_ON_LADDER)) {
               theRunner.setImage (Images.L_RunnerLeapingOffOfLadder);
               putRunnerOnBlock (theRunner);
               }
            else if (theRunner.theirImage.movieType == MovieType.HANGING_ON_ROPE_LEFT)
               theRunner.movieState = MovieState.PLAYING;
            else 
               theRunner.setImage (Images.L_RunnerImages);
            }
         }
      else if (code == CONSTANTS.CLEAR) {
         if (theRunner.theirImage.movieType != MovieType.CLIMBING_DOWN) {
            if (IsRunnerAtLadder (theRunner) || IsThereLadderBelow (theRunner)) {
               theRunner.setImage (Images.RunnerClimbingDown);
               centerRunnerInBlock (theRunner);
               }
            else if (canRunnerMoveDown (theRunner)) {
               theRunner.setImage (Images.RunnerFalling);
               centerRunnerInBlock (theRunner);
               }
            }
         else if (canRunnerMoveDown (theRunner))
            theRunner.movieState = MovieState.PLAYING;
         }
      else if (code == CONSTANTS.UP_ARROW) {
         if (theRunner.theirImage.movieType != MovieType.CLIMBING_UP) {
            if (IsRunnerAtLadder (theRunner)) {
               theRunner.setImage (Images.RunnerClimbingUp);
               centerRunnerInBlock (theRunner);
               }
            }
         else if (canRunnerMoveUp (theRunner))
            theRunner.movieState = MovieState.PLAYING;
         }
      }

	public int frameHandler (ArrayList<String> keysPressed) {
		// first process any runner changes that must happen (gravity has priority over user input)
		// second update any active elements to their next state, along with any of their effects on the cavern
		// third display the cavern with all of its the active elements

      // check for debugging key presses before doing anything to the runner
		processDebuggingKeyPresses (keysPressed);

      // check for any gold to get
		checkForGold (theRunner);

		// FIRST: process falling runner (only gravaty is in control) and then any user input runner control change
		if (theRunner.theirImage.movieType == MovieType.FALLING) {
			if (RunnerIsOnSomethingSolid (theRunner)) {
				theRunner.setImage (Images.C_RunnerStanding);
            // set the last key to null to cause the key processor to reprocess any key held down before the end of the fall 
            lastKeyCode = null;
            }
         else if (IsRunnerAtRope (theRunner) && objectIsOnBlockFloor(theRunner.theirLocation)) {
            // falling runner always grabs a rope if he has the opportunity
            theRunner.setImage (Images.R_RunnerHangingOnRope);
            theRunner.movieState = MovieState.PAUSED;
            // set the last key to null to cause the key processor to reprocess any key held down before the end of the fall 
            lastKeyCode = null;
            }
			}
		else if (theRunner.theirImage.movieType == MovieType.LEAPING_RIGHT_ONTO_LADDER) {
			// Let movie play out before processing any keyboard input
			}
		else if (theRunner.theirImage.movieType == MovieType.LEAPING_RIGHT_OFF_OF_LADDER) {
			// Let movie play out before processing any keyboard input
			}
		else if (theRunner.theirImage.movieType == MovieType.LEAPING_LEFT_ONTO_LADDER) {
			// Let movie play out before processing any keyboard input
			}
		else if (theRunner.theirImage.movieType == MovieType.LEAPING_LEFT_OFF_OF_LADDER) {
			// Let movie play out before processing any keyboard input
			}
		else if (theRunner.theirImage.movieType == MovieType.FIRING_RAYGUN_RIGHT) {
			// Let movie play out before processing any keyboard input
			}
		else if (theRunner.theirImage.movieType == MovieType.FIRING_RAYGUN_LEFT) {
			// Let movie play out before processing any keyboard input
			}
  		else {
         // check for runner control keys
		   processKeyPresses (keysPressed);
         }

		// SECOND: update any active elements in the cavern
		upDateMovie (theRunner);
		//upDateMovie (Troll1);
		/*
		upDateMovie (Runner3);
		upDateMovie (Runner4);
		upDateMovie (Runner5);
		upDateMovie (Runner6);
		upDateMovie (Runner7);
		upDateMovie (Runner8);

		// reset the test runners to their starting positions to creat a looping effect
		if (++frameCounter % 145 == 0) {
		    Runner2.theirLocation.setX(1188);
		    Runner3.theirLocation.setX(0);
		    Runner4.theirLocation.setX(1188);
		    Runner5.theirLocation.setX(0);
		    Runner6.theirLocation.setX(1188);
		    Runner7.theirLocation.setX(0);
		    Runner8.theirLocation.setX(1188);
			}
		*/

		// process any 'active' blocks to restore them
		for (ActiveBlock activeBlock : new ArrayList<> (activeBlocks)) {
			if (--activeBlock.delayAmount == 0) {
				BlockType block = getBlock (activeBlock.theBlock.Location);
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
			//Gc.drawImage (raygunBlast.theImage.theImage, raygunBlast.theLocation.getX(), raygunBlast.theLocation.getY());
			if (raygunBlast.theImage.nextImage != null) {      // still showing the blast
				raygunBlast.theImage = raygunBlast.theImage.nextImage;
				raygunBlast.theLocation.setX (raygunBlast.theLocation.getX() + raygunBlast.theImage.xDelta);
				raygunBlast.theLocation.setY (raygunBlast.theLocation.getY() + raygunBlast.theImage.yDelta);
            raygunBlast.theView.setImage (raygunBlast.theImage.theImage);
            raygunBlast.theView.setX (raygunBlast.theLocation.getX());
            raygunBlast.theView.setY (raygunBlast.theLocation.getY());
				}
			else {    // blast is complete, so delete the block and save it in the active blocks list to be restored later
				BlockType block =  getBlockRunnerIsIn (raygunBlast.theLocation);
				BlockType newBlock = new BlockType(block);
				ActiveBlock activeBlock = new ActiveBlock(newBlock, 120);
				activeBlocks.add (activeBlock);
				block.blockImage = null;
            block.blockView.setImage(null);
				block.Type = BlockTypes.EMPTY;
				View.getChildren().remove (raygunBlast.theView);
				raygunBlast = null;
				}
			}
		
		// update the troll in the View
      /*
      Troll1.theirView.setImage (Troll1.theirImage.theImage);
      Troll1.theirView.setX (Troll1.theirLocation.getX());
      Troll1.theirView.setY (Troll1.theirLocation.getY());
      */

		// update the user controlled runner in the View
      theRunner.theirView.setImage (theRunner.theirImage.theImage);
      theRunner.theirView.setX (theRunner.theirLocation.getX());
      theRunner.theirView.setY (theRunner.theirLocation.getY());
      
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
			Gc.drawImage (theRunner.theirImage.theImage, theRunner.theirLocation.getX(), theRunner.theirLocation.getY());
		}

	public void save (String fileName) {
		System.err.println ("save: filename = " + fileName);
		if (!fileName.contains (".ser"))
			fileName = fileName + ".ser";
		try (OutputStream file = new FileOutputStream (fileName);
			  OutputStream buffer = new BufferedOutputStream (file);
			  ObjectOutput output = new ObjectOutputStream (buffer);) {
			for (int x=0; x < Width; x++)
				for (int y=0; y < Height; y++)
					output.writeObject (Blocks[x][y].Type);
			output.writeObject (theRunner.theirLocation);
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
               BlockType block = Blocks[x][y];
					block.Type = (BlockTypes)input.readObject();
					switch (block.Type) {
						case EMPTY: block.blockImage = null; break;
						case LADDER: block.blockImage = Images.visableLadder; break;
						case DIGABLE: block.blockImage = Images.single_digable_surface_on_nothing; break;
						case ROPE: block.blockImage = Images.rope; break;
						case EXIT: block.blockImage = Images.exit; break;
						case HIDDEN_LADDER: block.blockImage = Images.hiddenLadder; break;
						case GOLD_1: block.blockImage = Images.gold1; break;
						}
               if (block.blockImage != null) {
                  block.blockView = new ImageView (block.blockImage);
                  block.blockView.setX (block.Location.getX()*CONSTANTS.BLOCK_WIDTH);
                  block.blockView.setY (block.Location.getY()*CONSTANTS.BLOCK_HEIGHT);
                  }
					}
			MovableLocationType location = (MovableLocationType)input.readObject();
			if (theRunner == null)
				theRunner = new Runner (location, Images.C_RunnerStanding);
			else
				theRunner.theirLocation = location;
			}
		catch (ClassNotFoundException ex) {
			System.err.println ("ClassNotFoundException exception occured");
			}
		catch (IOException ex) {
			System.err.println ("IO exception occured in restore: " + ex.toString());
			}
		}

	}
