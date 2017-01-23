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
   private ArrayList<Sprite> Trolls = new ArrayList();
   private int TotalGold = 0;
   private boolean Running = false;

   AudioClip pling = new AudioClip("file:Pling.mp3");
   AudioClip laserBlast = new AudioClip("file:LaserBlast.mp3");
   AudioClip bumpIntoSomething = new AudioClip("file:BumpIntoSomething.mp3");
   AudioClip runnerCapture = new AudioClip("file:LodeRunnerCapture.mp3");
   private RunnerMovies theRunnerMovies = new RunnerMovies();
   private TrollMovies theTrollMovies = new TrollMovies();

   Cavern(int width, int height, GraphicsContext gc) {
      Gc = gc;
      Blocks = new Block[width][height];
      Width = width;
      Height = height;
      for (int x = 0; x < Width; x++) {
         for (int y = 0; y < Height; y++) {
            Blocks[x][y] = new Block(new BlockLocation(x, y));
         }
      }
   }

   Cavern(int width, int height, Group theView) {
      View = theView;
      Blocks = new Block[width][height];
      Width = width;
      Height = height;
      for (int x = 0; x < Width; x++) {
         for (int y = 0; y < Height; y++) {
            Blocks[x][y] = new Block(new BlockLocation(x, y));
         }
      }
   }

   private void DisplayEscape() {
      View.getChildren().remove(theRunner.View);
      for (int x = 0; x < Width; x++) {
         for (int y = 0; y < Height; y++) {
            if (Blocks[x][y].blockImage != null) {
               if (Blocks[x][y].Type == BlockTypes.EXIT) {
                  View.getChildren().add(Blocks[x][y].blockView);
               }
               if (Blocks[x][y].Type == BlockTypes.HIDDEN_LADDER) {
                  Blocks[x][y].Type = BlockTypes.LADDER;
                  Blocks[x][y].blockView.setImage(Images.getImage(BlockTypes.LADDER));
                  View.getChildren().add(Blocks[x][y].blockView);
               }
            }
         }
      }
      View.getChildren().add(theRunner.View);
   }

   public int LoadCavernIntoView() {
      TotalGold = 0;
      View.getChildren().clear();
      View.getChildren().add(new ImageView(Images.mossWorld));
      for (int x = 0; x < Width; x++) {
         for (int y = 0; y < Height; y++) {
            if (Blocks[x][y].blockImage != null) {
               if ((Blocks[x][y].Type != BlockTypes.EXIT)
                       && (Blocks[x][y].Type != BlockTypes.HIDDEN_LADDER)) {
                  View.getChildren().add(Blocks[x][y].blockView);
               }
               /*
               if (Blocks[x][y].IsStructural()) {
                  ImageView aView = new ImageView (Images.hard);
                  aView.setX (Blocks[x][y].Location.getX()*44);
                  aView.setY (Blocks[x][y].Location.getY()*44);
                  View.getChildren().add(aView);                  
                  }
                */
               if (Blocks[x][y].Type == BlockTypes.GOLD_1) {
                  TotalGold++;
               }
            }
         }
      }
      View.getChildren().add(theRunner.View);
      for (Sprite troll : Trolls) {
         View.getChildren().add(troll.View);
      }
      return TotalGold;
   }

   public void addRunner(MovableLocationType location) {
      theRunner = new Sprite(location, theRunnerMovies.Facing, Direction.FACING, SpriteType.RUNNER);
   }

   public void addTroll(MovableLocationType location) {
      Sprite newTroll = new Sprite(location, theTrollMovies.Facing, Direction.RIGHT, SpriteType.TROLL);
      Trolls.add(newTroll);
   }

   public void removeTroll(MovableLocationType location) {
      for (Sprite troll : Trolls) {
         if ((troll.Location.getX() == location.getX()) && (troll.Location.getY() == location.getY())) {
            Trolls.remove(troll);
            break;
         }
      }
   }

   public Sprite getRunner() {
      return theRunner;
   }

   public boolean setBlock(Block block) {
      if (block.Location.getX() > Width) {
         return false;
      }
      if (block.Location.getY() > Height) {
         return false;
      }
      block.blockView = new ImageView(block.blockImage);
      block.blockView.setX(block.Location.getX() * CONSTANTS.BLOCK_WIDTH);
      block.blockView.setY(block.Location.getY() * CONSTANTS.BLOCK_HEIGHT);
      Blocks[block.Location.getX()][block.Location.getY()] = block;
      return true;
   }

   public Block getBlock(BlockLocation location) {
      if ((0 > location.getX()) || (location.getX() >= Width)) {
         return null;
      }
      if ((0 > location.getY()) || (location.getY() >= Height)) {
         return null;
      }
      return Blocks[location.getX()][location.getY()];
   }

   private Block getBlockActorIsIn(MovableLocationType location) {
      int x, y, fudgeFactor;

      if ((((location.getX() % CONSTANTS.BLOCK_WIDTH) / 4) & 1) == 1) {
         fudgeFactor = 2;  // add 2 pixels to blocks where runner is on 4, 12, 20, 28, 36 pixel bounderies
      } else {
         fudgeFactor = 0;
      }
      x = (location.getX() + (CONSTANTS.BLOCK_WIDTH / 2) + fudgeFactor) / CONSTANTS.BLOCK_WIDTH;
      y = (location.getY() + (CONSTANTS.BLOCK_HEIGHT / 2) + fudgeFactor) / CONSTANTS.BLOCK_HEIGHT;
      //System.err.println ("getBlockActorIsIn: x=" + x + " y=" + y);
      return Blocks[x][y];
   }

   private void centerActorInBlock(Sprite theActor) {
      BlockLocation blockLocation;

      blockLocation = getBlockActorIsIn(theActor.Location).Location;
      theActor.Location.setX(blockLocation.getX() * CONSTANTS.BLOCK_WIDTH);
   }

   private void putActorOnBlock(Sprite theActor) {
      BlockLocation blockLocation;

      blockLocation = getBlockActorIsIn(theActor.Location).Location;
      theActor.Location.setY(blockLocation.getY() * CONSTANTS.BLOCK_HEIGHT);
   }

   private boolean TrollNextToTroll(Sprite thisTroll, Direction direction) {
      if (thisTroll.Type != SpriteType.TROLL) {
         return false;
      }
      MovableLocationType thisTrollLocation = thisTroll.Location;
      for (Sprite otherTroll : Trolls) {
         if (otherTroll == thisTroll) // looking for any other trolls, not this troll
         {
            continue;
         }
         int Delta;
         MovableLocationType otherTrollLocation = otherTroll.Location;
         switch (direction) {
            case RIGHT:
               Delta = thisTrollLocation.getY() - otherTrollLocation.getY();
               if ((abs(Delta) < 30)) {
                  Delta = thisTrollLocation.getX() - otherTrollLocation.getX();
                  if ((Delta < 0) && (Delta > -30)) {
                     return true;
                  }
               }
               break;
            case LEFT:
               Delta = thisTrollLocation.getY() - otherTrollLocation.getY();
               if ((abs(Delta) < 30)) {
                  Delta = otherTrollLocation.getX() - thisTrollLocation.getX();
                  if ((Delta < 0) && (Delta > -30)) {
                     return true;
                  }
               }
               break;
            case DOWN:
               Delta = thisTrollLocation.getX() - otherTrollLocation.getX();
               if ((abs(Delta) < 30)) {
                  Delta = thisTrollLocation.getY() - otherTrollLocation.getY();
                  if ((Delta < 0) && (Delta > -30)) {
                     return true;
                  }
               }
               break;
            case UP:
               Delta = thisTrollLocation.getX() - otherTrollLocation.getX();
               if ((abs(Delta) < 30)) {
                  Delta = otherTrollLocation.getY() - thisTrollLocation.getY();
                  if ((Delta < 0) && (Delta > -30)) {
                     return true;
                  }
               }
               break;
         }
      }
      return false;
   }

   private boolean canActorMoveRight(Sprite theActor) {
      Block thisBlock, nextBlock;
      BlockLocation location;

      thisBlock = getBlockActorIsIn(theActor.Location);
      location = thisBlock.Location.locationToTheRight();
      nextBlock = getBlock(location);
      if (TrollNextToTroll(theActor, Direction.RIGHT)) {
         return false;
      } else if ((nextBlock == null) || (nextBlock.IsStructural())) {
         return false;
      } else {
         return true;
      }
   }

   private boolean canActorMoveLeft(Sprite theActor) {
      Block thisBlock, nextBlock;
      BlockLocation location;

      thisBlock = getBlockActorIsIn(theActor.Location);
      location = thisBlock.Location.locationToTheLeft();
      nextBlock = getBlock(location);
      if (TrollNextToTroll(theActor, Direction.LEFT)) {
         return false;
      } else if ((nextBlock == null) || (nextBlock.IsStructural())) {
         return false;
      } else {
         return true;
      }
   }

   private boolean canActorMoveDown(Sprite theActor) {
      Block thisBlock, nextBlock;
      BlockLocation location;

      thisBlock = getBlockActorIsIn(theActor.Location);
      location = thisBlock.Location.locationBelow();
      nextBlock = getBlock(location);
      if (TrollNextToTroll(theActor, Direction.DOWN)) {
         return false;
      } else if ((nextBlock == null) || (nextBlock.IsStructural())) {
         return false;
      } else {
         return true;
      }
   }

   private boolean canActorMoveUp(Sprite theActor) {
      Block thisBlock, nextBlock;
      BlockLocation location;

      thisBlock = getBlockActorIsIn(theActor.Location);
      location = thisBlock.Location.locationAbove();
      nextBlock = getBlock(location);
      if (TrollNextToTroll(theActor, Direction.UP)) {
         return false;
      } else if ((nextBlock == null) || (nextBlock.IsStructural())) {
         return false;
      } else {
         return true;
      }
   }

   private boolean objectIsOnBlockFloor(MovableLocationType location) {
      int xLocation;

      xLocation = location.getY() % CONSTANTS.BLOCK_HEIGHT;
      if ((xLocation == 0) || (xLocation == 40)) {
         return true;
      } else {
         return false;
      }
   }

   private boolean IsActorAtLadder(Sprite theActor) {
      Block thisBlock;

      thisBlock = getBlockActorIsIn(theActor.Location);
      if (thisBlock.Type == BlockTypes.LADDER) {
         return true;
      } else {
         return false;
      }
   }

   private boolean IsActorAtRope(Sprite theActor) {
      Block thisBlock;

      thisBlock = getBlockActorIsIn(theActor.Location);
      if (thisBlock.Type == BlockTypes.ROPE) {
         return true;
      } else {
         return false;
      }
   }

   private boolean IsActorNextToLadderOnTheRight(Sprite theActor) {
      Block thisBlock, nextBlock;
      BlockLocation location;

      thisBlock = getBlockActorIsIn(theActor.Location);
      location = thisBlock.Location.locationToTheRight();
      nextBlock = getBlock(location);
      if (nextBlock.Type == BlockTypes.LADDER) {
         return true;
      } else {
         return false;
      }
   }

   private boolean IsActorNextToLadderOnTheLeft(Sprite theActor) {
      Block thisBlock, nextBlock;
      BlockLocation location;

      thisBlock = getBlockActorIsIn(theActor.Location);
      location = thisBlock.Location.locationToTheLeft();
      nextBlock = getBlock(location);
      if (nextBlock.Type == BlockTypes.LADDER) {
         return true;
      } else {
         return false;
      }
   }

   private boolean IsActorNextToRopeOnTheLeft(Sprite theActor) {
      Block thisBlock, nextBlock;
      BlockLocation location;

      thisBlock = getBlockActorIsIn(theActor.Location);
      location = thisBlock.Location.locationToTheLeft();
      nextBlock = getBlock(location);
      if (nextBlock.Type == BlockTypes.ROPE) {
         return true;
      } else {
         return false;
      }
   }

   private boolean IsActorNextToRopeOnTheRight(Sprite theActor) {
      Block thisBlock, nextBlock;
      BlockLocation location;

      thisBlock = getBlockActorIsIn(theActor.Location);
      location = thisBlock.Location.locationToTheRight();
      nextBlock = getBlock(location);
      if (nextBlock.Type == BlockTypes.ROPE) {
         return true;
      } else {
         return false;
      }
   }

   private boolean IsThereSolidGroundOnTheRight(Sprite theActor) {
      Block thisBlock, nextBlock;
      BlockLocation location;

      thisBlock = getBlockActorIsIn(theActor.Location);
      location = thisBlock.Location.locationToTheLowerRight();
      nextBlock = getBlock(location);
      if ((nextBlock == null) || (nextBlock.IsStructural())) {
         return true;
      } else {
         return false;
      }
   }

   private boolean IsThereSomethingToBlastOnTheRight(Sprite theRunner) {
      Block thisBlock, nextBlock;
      BlockLocation location;

      thisBlock = getBlockActorIsIn(theRunner.Location);
      location = thisBlock.Location.locationToTheLowerRight();
      nextBlock = getBlock(location);
      if ((nextBlock != null) && nextBlock.Laserable()) {
         return true;
      } else {
         return false;
      }
   }

   private boolean IsThereSomethingToBlastOnTheLeft(Sprite theRunner) {
      Block thisBlock, nextBlock;
      BlockLocation location;

      thisBlock = getBlockActorIsIn(theRunner.Location);
      location = thisBlock.Location.locationToTheLowerLeft();
      nextBlock = getBlock(location);
      if ((nextBlock != null) && nextBlock.Laserable()) {
         return true;
      } else {
         return false;
      }
   }

   private boolean IsThereSolidGroundOnTheLeft(Sprite theActor) {
      Block thisBlock, nextBlock;
      BlockLocation location;

      thisBlock = getBlockActorIsIn(theActor.Location);
      location = thisBlock.Location.locationToTheLowerLeft();
      nextBlock = getBlock(location);
      if ((nextBlock == null) || (nextBlock.IsStructural())) {
         return true;
      } else {
         return false;
      }
   }

   private boolean IsThereLadderBelow(Sprite theActor) {
      Block thisBlock, nextBlock;
      BlockLocation location;

      thisBlock = getBlockActorIsIn(theActor.Location);
      location = thisBlock.Location.locationBelow();
      nextBlock = getBlock(location);
      if ((nextBlock != null) && (nextBlock.Type == BlockTypes.LADDER)) {
         return true;
      } else {
         return false;
      }
   }

   private boolean IsThereLadderAbove(Sprite theActor) {
      Block thisBlock, nextBlock;
      BlockLocation location;

      thisBlock = getBlockActorIsIn(theActor.Location);
      location = thisBlock.Location.locationAbove();
      nextBlock = getBlock(location);
      if ((nextBlock != null) && (nextBlock.Type == BlockTypes.LADDER)) {
         return true;
      } else {
         return false;
      }
   }

   private boolean ActorIsOnSomethingSolid(Sprite theActor) {
      Block thisBlock, blockBelow;
      BlockLocation location;

      if ((theActor.Image.movieType == MovieType.CLIMBING_DOWN)
              || (theActor.Image.movieType == MovieType.CLIMBING_UP)
              || (theActor.Image.movieType == MovieType.HANGING_ON_LADDER)
              || (theActor.Image.movieType == MovieType.LEAPING_OFF_OF_LADDER)
              || (theActor.Image.movieType == MovieType.LEAPING_ONTO_LADDER)) {
         return true;
      }
      //System.err.println ("Cavern.ActorIsOnSomethingSolid: image type = " + theActor.Image.movieType);
      thisBlock = getBlockActorIsIn(theActor.Location);
      location = thisBlock.Location.locationBelow();
      blockBelow = getBlock(location);
      if (!objectIsOnBlockFloor(theActor.Location)) {
         return false;
      }
      if ((blockBelow != null)
              && ((blockBelow.Type != BlockTypes.LADDER) && (!blockBelow.IsStructural()))) {
         return false;
      }
      putActorOnBlock(theActor);
      return true;
   }

   private void checkForGold(Sprite theRunner) {
      Block thisBlock;

      thisBlock = getBlockActorIsIn(theRunner.Location);
      if (thisBlock.Type == BlockTypes.GOLD_1) {
         pling.play();
         thisBlock.Type = BlockTypes.EMPTY;
         thisBlock.blockImage = null;
         View.getChildren().remove(thisBlock.blockView);
         TotalGold--;
         if (TotalGold == 0) {
            DisplayEscape();
         }
      }
   }

   private void upDateMovie(Sprite theActor, ActorMovies theMovies) {
      //System.err.println ("upDateMovie: entering with movie type " + theActor.Image.movieType);
      if (theActor.movieState == MovieState.PAUSED) {
         return;
      }
      if (theActor.Image.movieType == MovieType.RUNNING) {
         if (theActor.Orientation == Direction.RIGHT) {
            if (!canActorMoveRight(theActor)) {
               theActor.setImage(theMovies.Turned);
               centerActorInBlock(theActor);
               theActor.Location.setX(theActor.Location.getX() - 8);
               return;
            } else if (IsActorAtRope(theActor)) {
               // force runner to use rope if it is present, which is how MMR did it.
               theActor.setImage(theMovies.HangingOnRope);
               theActor.Location.setX(theActor.Location.getX() + 16);
               return;
            } else if (!ActorIsOnSomethingSolid(theActor)) {
               theActor.setImage(theMovies.Falling);
               theActor.Orientation = Direction.FACING;
               centerActorInBlock(theActor);
               return;
            } else if (!IsThereSolidGroundOnTheRight(theActor)) {
               if (IsActorNextToLadderOnTheRight(theActor)) {
                  theActor.setImage(theMovies.LeapingOntoLadder);
                  centerActorInBlock(theActor);
                  return;
               }
            }
         } else if (!canActorMoveLeft(theActor)) {
            theActor.setImage(theMovies.Turned);
            centerActorInBlock(theActor);
            theActor.Location.setX(theActor.Location.getX() + 8);
            return;
         } else if (IsActorAtRope(theActor)) {
            // force Actor to use rope if it is present, which is how MMR did it.
            theActor.setImage(theMovies.HangingOnRope);
            theActor.Location.setX(theActor.Location.getX() - 16);
            return;
         } else if (!ActorIsOnSomethingSolid(theActor)) {
            theActor.setImage(theMovies.Falling);
            theActor.Orientation = Direction.FACING;
            centerActorInBlock(theActor);
            return;
         } else if (!IsThereSolidGroundOnTheLeft(theActor)) {
            if (IsActorNextToLadderOnTheLeft(theActor)) {
               theActor.setImage(theMovies.LeapingOntoLadder);
               centerActorInBlock(theActor);
               return;
            }
         }
      } else if (theActor.Image.movieType == MovieType.CLIMBING_DOWN) {
         if (!canActorMoveDown(theActor)) {
            theActor.movieState = MovieState.PAUSED;
            putActorOnBlock(theActor);
            return;
         } else if (!IsActorAtLadder(theActor) && !IsThereLadderBelow(theActor)) {
            theActor.setImage(theMovies.Falling);
            theActor.Orientation = Direction.FACING;
            return;
         }
      } else if (theActor.Image.movieType == MovieType.CLIMBING_UP) {
         if (!IsActorAtLadder(theActor)) {
            theActor.setImage(theMovies.Facing);
            putActorOnBlock(theActor);
            return;
         } else if (!canActorMoveUp(theActor)) {
            theActor.movieState = MovieState.PAUSED;
            return;
         }
      } else if (theActor.Image.movieType == MovieType.HANGING_ON_ROPE) {
         if (theActor.Orientation == Direction.RIGHT) {
            if (!IsActorAtRope(theActor)) {
               if (ActorIsOnSomethingSolid(theActor)) {
                  theActor.setImage(theMovies.Running);
                  putActorOnBlock(theActor);
                  return;
               } else {
                  theActor.setImage(theMovies.Falling);
                  theActor.Orientation = Direction.FACING;
                  centerActorInBlock(theActor);
                  return;
               }
            } else if (!canActorMoveRight(theActor)) {
               theActor.movieState = MovieState.PAUSED;
               theActor.Location.setX(theActor.Location.getX() + 16);
               return;
            } else if (!IsThereSolidGroundOnTheRight(theActor)) {
               if (IsActorNextToLadderOnTheRight(theActor)) {
                  theActor.setImage(theMovies.LeapingOntoLadder);
                  centerActorInBlock(theActor);
                  return;
               }
            }
         } else if (!IsActorAtRope(theActor)) {
            if (ActorIsOnSomethingSolid(theActor)) {
               theActor.setImage(theMovies.Running);
               putActorOnBlock(theActor);
               return;
            } else {
               theActor.setImage(theMovies.Falling);
               theActor.Orientation = Direction.FACING;
               centerActorInBlock(theActor);
               return;
            }
         } else if (!canActorMoveLeft(theActor)) {
            theActor.movieState = MovieState.PAUSED;
            theActor.Location.setX(theActor.Location.getX() - 16);
            return;
         } else if (!IsThereSolidGroundOnTheLeft(theActor)) {
            if (IsActorNextToLadderOnTheLeft(theActor)) {
               theActor.setImage(theMovies.LeapingOntoLadder);
               centerActorInBlock(theActor);
               return;
            }
         }
      } else if ((theActor.Type == SpriteType.RUNNER) && (theActor.Image.movieType == MovieType.HANGING_ON_LADDER)) {
         // set the last key to null to cause the key processor to reprocess any key held down before the end of the leap onto the ladder 
         lastKeyCode = null;
      } else if ((theActor.Image.movieType != MovieType.FALLING) && !ActorIsOnSomethingSolid(theActor)) {
         theActor.setImage(theMovies.Falling);
         theActor.Orientation = Direction.FACING;
         centerActorInBlock(theActor);
         return;
      }
      theActor.Location.setX(theActor.Location.getX() + (theActor.Image.xDelta * theActor.getScale()));
      theActor.Location.setY(theActor.Location.getY() + (theActor.Image.yDelta * theActor.getScale()));
      theActor.Image = theActor.Image.nextImage;

      //System.err.println ("upDateMovie: leaving with movie type " + theActor.Image.movieType);
   }

   private void processDebuggingKeyPresses(ArrayList<String> keysPressed) {
      if (keysPressed.contains("R")) {
         // dump runner debug info
         System.err.println("RUNNER INFO:");
         System.err.println("	location is " + theRunner.Location.getX() + ", " + theRunner.Location.getY());
         Block thisBlock = getBlockActorIsIn(theRunner.Location);
         System.err.println("	in block " + thisBlock.Location.getX() + ", " + thisBlock.Location.getY());
         System.err.println("	offset in block is " + theRunner.Location.getX() % CONSTANTS.BLOCK_WIDTH + ", " + theRunner.Location.getY() % CONSTANTS.BLOCK_HEIGHT);
         System.err.println("	movie type " + theRunner.Image.movieType);
         System.err.println("	movie state is " + theRunner.movieState);
         System.err.println("	orientation is " + theRunner.Orientation);
         keysPressed.remove("R");
         if (keysPressed.isEmpty()) // always have something at the head of the queue
         {
            keysPressed.add(CONSTANTS.NO_KEY_PRESSED);
         }
      }
   }

   private void processKeyPresses(ArrayList<String> keysPressed) {
      // use the key that is on the front of the input fifo (oldest pressed key that is still pressed)
      String code = keysPressed.get(0);

      if (lastKeyCode == code) {
         return;
      }
      lastKeyCode = code;
      // user changed the keyboard input so
      // if the runner is moving in response to the last user keypress user stop him where he is
      if (theRunner.Image.movieType == MovieType.RUNNING) {
         theRunner.setImage(theRunnerMovies.Turned);
      } else if ((theRunner.Image.movieType == MovieType.CLIMBING_DOWN)
              || (theRunner.Image.movieType == MovieType.CLIMBING_UP)
              || (theRunner.Image.movieType == MovieType.HANGING_ON_ROPE)) {
         theRunner.movieState = MovieState.PAUSED;
      }
      // some key is pressed so see if it is a runner control key
      if (code == CONSTANTS.PAGE_UP) {  // right side ray gun
         if (IsThereSomethingToBlastOnTheRight(theRunner)) {
            laserBlast.play();
            theRunner.setImage(theRunnerMovies.firingRaygun);
            theRunner.Orientation = Direction.RIGHT;
            centerActorInBlock(theRunner);
            raygunBlast = new Sprite(new MovableLocationType(theRunner.Location.getX() + 44, theRunner.Location.getY()),
                    theRunnerMovies.raygunBlast, Direction.RIGHT, SpriteType.RAYGUN_BLAST);
            View.getChildren().add(raygunBlast.View);
         }
      }
      if (code == CONSTANTS.HOME) {     // left side ray gun
         if (IsThereSomethingToBlastOnTheLeft(theRunner)) {
            laserBlast.play();
            theRunner.setImage(theRunnerMovies.firingRaygun);
            theRunner.Orientation = Direction.LEFT;
            centerActorInBlock(theRunner);
            raygunBlast = new Sprite(new MovableLocationType(theRunner.Location.getX() - 44, theRunner.Location.getY()),
                    theRunnerMovies.raygunBlast, Direction.LEFT, SpriteType.RAYGUN_BLAST);
            View.getChildren().add(raygunBlast.View);
         }
      } else if (code == CONSTANTS.RIGHT_ARROW) {
         if (canActorMoveRight(theRunner)) {
            if ((theRunner.Image.movieType == MovieType.CLIMBING_DOWN)
                    || (theRunner.Image.movieType == MovieType.CLIMBING_UP)
                    || (theRunner.Image.movieType == MovieType.HANGING_ON_LADDER)) {
               theRunner.setImage(theRunnerMovies.LeapingOffOfLadder);
               theRunner.Orientation = Direction.RIGHT;
               putActorOnBlock(theRunner);
            } else if (theRunner.Image.movieType == MovieType.HANGING_ON_ROPE) {
               theRunner.movieState = MovieState.PLAYING;
               theRunner.Orientation = Direction.RIGHT;
            } else {
               theRunner.setImage(theRunnerMovies.Running);
               theRunner.Orientation = Direction.RIGHT;
            }
         }
      } else if (code == CONSTANTS.LEFT_ARROW) {
         if (canActorMoveLeft(theRunner)) {
            if ((theRunner.Image.movieType == MovieType.CLIMBING_DOWN)
                    || (theRunner.Image.movieType == MovieType.CLIMBING_UP)
                    || (theRunner.Image.movieType == MovieType.HANGING_ON_LADDER)) {
               theRunner.setImage(theRunnerMovies.LeapingOffOfLadder);
               theRunner.Orientation = Direction.LEFT;
               putActorOnBlock(theRunner);
            } else if (theRunner.Image.movieType == MovieType.HANGING_ON_ROPE) {
               theRunner.movieState = MovieState.PLAYING;
               theRunner.Orientation = Direction.LEFT;
            } else {
               theRunner.setImage(theRunnerMovies.Running);
               theRunner.Orientation = Direction.LEFT;
            }
         }
      } else if (code == CONSTANTS.CLEAR) {
         if (theRunner.Image.movieType != MovieType.CLIMBING_DOWN) {
            if (IsActorAtLadder(theRunner) || IsThereLadderBelow(theRunner)) {
               theRunner.setImage(theRunnerMovies.ClimbingDown);
               theRunner.Orientation = Direction.RIGHT;
               centerActorInBlock(theRunner);
            } else if (canActorMoveDown(theRunner)) {
               theRunner.setImage(theRunnerMovies.Falling);
               theRunner.Orientation = Direction.FACING;
               centerActorInBlock(theRunner);
            }
         } else if (canActorMoveDown(theRunner)) {
            theRunner.movieState = MovieState.PLAYING;
         }
      } else if (code == CONSTANTS.UP_ARROW) {
         if (theRunner.Image.movieType != MovieType.CLIMBING_UP) {
            if (IsActorAtLadder(theRunner)) {
               theRunner.setImage(theRunnerMovies.ClimbingUp);
               theRunner.Orientation = Direction.RIGHT;
               centerActorInBlock(theRunner);
            }
         } else if (canActorMoveUp(theRunner)) {
            theRunner.movieState = MovieState.PLAYING;
         }
      }
   }

   void ProcessRunner(ArrayList<String> keysPressed) {
      if (theRunner.Image.movieType == MovieType.BEING_CAPTURED) {
         // Let movie play out before processing any keyboard input
      } // FIRST: process falling runner (only gravaty is in control) and then any user input runner control change
      else if (theRunner.Image.movieType == MovieType.FALLING) {
         if (ActorIsOnSomethingSolid(theRunner)) {
            theRunner.setImage(theRunnerMovies.Facing);
            // set the last key to null to cause the key processor to reprocess any key held down before the end of the fall 
            lastKeyCode = null;
         } else if (IsActorAtRope(theRunner) && objectIsOnBlockFloor(theRunner.Location)) {
            // falling runner always grabs a rope if he has the opportunity
            theRunner.setImage(theRunnerMovies.HangingOnRope);
            theRunner.movieState = MovieState.PAUSED;
            // set the last key to null to cause the key processor to reprocess any key held down before the end of the fall 
            lastKeyCode = null;
         }
      } else if (theRunner.Image.movieType == MovieType.LEAPING_OFF_OF_LADDER) {
         // Let movie play out before processing any keyboard input
      } else if (theRunner.Image.movieType == MovieType.LEAPING_ONTO_LADDER) {
         // Let movie play out before processing any keyboard input
      } else if (theRunner.Image.movieType == MovieType.FIRING_RAYGUN) {
         // Let movie play out before processing any keyboard input
      } else {
         // check for runner control keys
         processKeyPresses(keysPressed);
      }
   }

   boolean CanTrollMove(Sprite Troll, Direction direction) {
      if (direction == Direction.RIGHT) {
         return canActorMoveRight(Troll);
      } else {
         return canActorMoveLeft(Troll);
      }
   }

   void MoveTroll(Sprite Troll, Direction direction) {
      int offset;

      if (direction == Direction.RIGHT) {
         offset = CONSTANTS.BLOCK_WIDTH;
      } else {
         offset = -CONSTANTS.BLOCK_WIDTH;
      }
      Troll.Location.setX(Troll.Location.getX() + offset);
   }

   boolean FeatureIsInBlock(Sprite Troll, BlockTypes feature) {
      Block thisBlock;

      thisBlock = getBlockActorIsIn(Troll.Location);
      switch (feature) {
         case LADDER:
            if (thisBlock.Type == BlockTypes.LADDER) {
               return true;
            }
            break;
         case HOLE:
            if (canActorMoveDown(Troll)) {
               return true;
            }
            break;
      }
      return false;
   }

   int IsFeatureReachable(Sprite Troll, Direction direction, BlockTypes feature) {
      MovableLocationType trollStartLocation = new MovableLocationType(Troll.Location.getX(), Troll.Location.getY());
      int returnDistance = CONSTANTS.WINDOW_WIDTH;   // set return variable to default value indicating that feature is not reachable
      int distance = 0;

      while (CanTrollMove(Troll, direction)) {
         MoveTroll(Troll, direction);
         distance++;
         if (FeatureIsInBlock(Troll, feature)) {
            returnDistance = distance;
            break;
         }
      }
      Troll.Location = trollStartLocation;   // set troll back to where he was
      return returnDistance;
   }

   Direction DirectionToNearestFeature(Sprite Troll, BlockTypes feature) {
      int distanceLeft, distanceRight;
      distanceLeft = IsFeatureReachable(Troll, Direction.LEFT, feature);
      distanceRight = IsFeatureReachable(Troll, Direction.RIGHT, feature);
      if (distanceLeft < distanceRight) {
         return Direction.LEFT;
      }
      if (distanceRight < distanceLeft) {
         return Direction.RIGHT;
      }
      if (distanceRight != CONSTANTS.WINDOW_WIDTH) {
         return Direction.RIGHT;   // default to going right if feature is equal distance on both sides
      }
      return Direction.AWAY;  // return AWAY if the feature is not reachable
   }

   void MoveTrollHorizontally(Sprite Troll, Direction direction) {
      if ((Troll.Image.movieType == MovieType.CLIMBING_DOWN)
              || (Troll.Image.movieType == MovieType.CLIMBING_UP)
              || (Troll.Image.movieType == MovieType.HANGING_ON_LADDER)) {
         Troll.setImage(theTrollMovies.LeapingOffOfLadder);
         Troll.Orientation = direction;
         putActorOnBlock(Troll);
      } else if (Troll.Image.movieType == MovieType.HANGING_ON_ROPE) {
         Troll.movieState = MovieState.PLAYING;
         Troll.Orientation = direction;
      } else {
         if (Troll.Image.movieType != MovieType.RUNNING) {
            Troll.setImage(theTrollMovies.Running);
         }
         Troll.movieState = MovieState.PLAYING;
         Troll.Orientation = direction;
      }
   }

   void ProcessTroll(Sprite Troll) {
      //System.err.println ("ProcessTrolls: Enterring - MovieType = " + Troll1.Image.movieType);

      if (Troll.Image.movieType == MovieType.FALLING) {
         if (ActorIsOnSomethingSolid(Troll)) {
            Troll.setImage(theTrollMovies.Turned);
         } else if (IsActorAtRope(Troll) && objectIsOnBlockFloor(Troll.Location)) {
            // falling troll always grabs a rope if they have the opportunity
            Troll.setImage(theTrollMovies.HangingOnRope);
            Troll.movieState = MovieState.PAUSED;
         } else // nothing to do if falling
         {
            return;
         }
      } else if (Troll.Image.movieType == MovieType.LEAPING_OFF_OF_LADDER) {
         // Let movie play out before processing any troll searching 
         return;
      } else if (Troll.Image.movieType == MovieType.LEAPING_ONTO_LADDER) {
         // Let movie play out before processing any troll searching
         return;
      }
      int xDelta = theRunner.Location.getX() - Troll.Location.getX();
      int yDelta = theRunner.Location.getY() - Troll.Location.getY();
      if ((abs(xDelta) < 8) && (abs(yDelta) < 8)) {
         // runner caught, execute game over code
         Troll.Image.theImage = Images.empty;
         Troll.movieState = MovieState.PAUSED;
         if (theRunner.Image.movieType != MovieType.BEING_CAPTURED) {
            theRunner.setImage(theRunnerMovies.Captured);
            theRunner.Orientation = Direction.RIGHT;
            theRunner.movieState = MovieState.PLAYING;
            runnerCapture.play();
         }
         return;
      }
      Direction PreferredX = Direction.HorizontalDirection(xDelta);
      Direction PreferredY = Direction.VerticalDirection(yDelta);
      if (abs(yDelta) >= 8) {   // check vertical delta first since climbing/going-down are ussually more difficult
         // if runner is above or below then climb or drop to get to the same level if possible
         if (PreferredY == Direction.UP) {
            if (canActorMoveUp(Troll) && IsActorAtLadder(Troll)) {
               if (Troll.Image.movieType != MovieType.CLIMBING_UP) {
                  Troll.setImage(theTrollMovies.ClimbingUp);
                  Troll.Orientation = Direction.RIGHT;
                  centerActorInBlock(Troll);
               } else {
                  Troll.movieState = MovieState.PLAYING;
               }
               return;  // took an action, so quit
            } else if ((PreferredY = DirectionToNearestFeature(Troll, BlockTypes.LADDER)) != Direction.AWAY) {  // look for closest ladder on either side
               // head for the ladder
               MoveTrollHorizontally(Troll, PreferredY);
               return;  // took an action, so quit
            }
         } else // preferred Y direction is set to DOWN
         if (canActorMoveDown(Troll)) {
            if (IsActorAtLadder(Troll) || IsThereLadderBelow(Troll)) {
               if (Troll.Image.movieType != MovieType.CLIMBING_DOWN) {
                  Troll.setImage(theTrollMovies.ClimbingDown);
                  Troll.Orientation = Direction.RIGHT;
                  centerActorInBlock(Troll);
               } else {
                  Troll.movieState = MovieState.PLAYING;
               }
            } else {
               Troll.setImage(theTrollMovies.Falling);
               Troll.Orientation = Direction.FACING;
               centerActorInBlock(Troll);
            }
            return;  // took an action, so quit
         } else if ((PreferredY = DirectionToNearestFeature(Troll, BlockTypes.HOLE)) != Direction.AWAY) {  // look for closest 'hole' on either side
            // head for the 'hole' to get a way down
            MoveTrollHorizontally(Troll, PreferredY);
            return;  // took an action, so quit
         }
      }
      if (abs(xDelta) >= 8) {
         if (PreferredX == Direction.RIGHT) {
            if (canActorMoveRight(Troll)) {
               MoveTrollHorizontally(Troll, Direction.RIGHT);
               return;
            }
         } else if (canActorMoveLeft(Troll)) {    // preferred X direction is LEFT
            MoveTrollHorizontally(Troll, Direction.LEFT);
            return;
         }
      }
      // if execution gets here then no action was taken and troll should wait where it is
      Troll.movieState = MovieState.PAUSED;
      //System.err.println ("ProcessTrolls: Leaving - MovieType = " + Troll.Image.movieType);
   }

   public int frameHandler(ArrayList<String> keysPressed) {
      // first process any runner changes that must happen (gravity has priority over user input)
      // second update any active elements to their next state, along with any of their effects on the cavern
      // third display the cavern with all of its the active elements

      // check for debugging key presses before doing anything to the runner
      processDebuggingKeyPresses(keysPressed);

      /*
      if (theRunner.Image.movieType == MovieType.BEING_CAPTURED)
         // play capture movie at half speed
         if ((frameCounter++ % 2) == 1) 
            return TotalGold;
       */
      // check for any gold to get
      checkForGold(theRunner);

      // FIRST: process the actors
      ProcessRunner(keysPressed);
      for (Sprite troll : Trolls) {
         ProcessTroll(troll);
      }

      // SECOND: update any active elements in the cavern
      upDateMovie(theRunner, theRunnerMovies);
      for (Sprite troll : Trolls) {
         upDateMovie(troll, theTrollMovies);
      }

      if (theRunner.Image.nextImage == null) {
         // at the end of the 'captured' movie so game is over
         return CONSTANTS.GAME_OVER;
      }
      // process any 'active' blocks to restore them
      for (ActiveBlock activeBlock : new ArrayList<>(activeBlocks)) {
         if (--activeBlock.delayAmount == 0) {
            Block block = getBlock(activeBlock.theBlock.Location);
            block.Type = activeBlock.theBlock.Type;
            block.blockImage = activeBlock.theBlock.blockImage;
            block.blockView.setImage(block.blockImage);
            block.Item = activeBlock.theBlock.Item;
            activeBlocks.remove(activeBlock);
         }
      }

      // THIRD: display any changes in the cavern
      // display any raygun blast before displaying the runner so raygun that the runner is holding displays on top of the blast
      if (raygunBlast != null) {
         if (raygunBlast.Image.nextImage != null) {      // still showing the blast
            raygunBlast.Image = raygunBlast.Image.nextImage;
            raygunBlast.Location.setX(raygunBlast.Location.getX() + raygunBlast.Image.xDelta);
            raygunBlast.Location.setY(raygunBlast.Location.getY() + raygunBlast.Image.yDelta);
            raygunBlast.View.setImage(raygunBlast.Image.theImage);
            raygunBlast.View.setX(raygunBlast.Location.getX());
            raygunBlast.View.setY(raygunBlast.Location.getY());
            raygunBlast.View.setScaleX(raygunBlast.getScale());
         } else {    // blast is complete, so delete the block and save it in the active blocks list to be restored later
            Block block = getBlockActorIsIn(raygunBlast.Location);
            Block newBlock = new Block(block);
            ActiveBlock activeBlock = new ActiveBlock(newBlock, 120);
            activeBlocks.add(activeBlock);
            block.blockImage = null;
            block.blockView.setImage(null);
            block.Type = BlockTypes.EMPTY;
            View.getChildren().remove(raygunBlast.View);
            raygunBlast = null;
         }
      }

      // update the trolls in the View
      for (Sprite troll : Trolls) {
         troll.View.setImage(troll.Image.theImage);
         troll.View.setX(troll.Location.getX());
         troll.View.setY(troll.Location.getY());
         troll.View.setScaleX(troll.getScale());
      }

      // update the user controlled runner in the View
      theRunner.View.setImage(theRunner.Image.theImage);
      theRunner.View.setX(theRunner.Location.getX());
      theRunner.View.setY(theRunner.Location.getY());
      theRunner.View.setScaleX(theRunner.getScale());
      return TotalGold;
   }

   public void display() {
      Gc.drawImage(Images.mossWorld, 0, 0);   // display background
      // display any blocks that have images at their correct graphical location in the window
      for (int x = 0; x < Width; x++) {
         for (int y = 0; y < Height; y++) {
            if (Blocks[x][y].blockImage != null) {
               Gc.drawImage(Blocks[x][y].blockImage,
                       Blocks[x][y].Location.getX() * CONSTANTS.BLOCK_WIDTH,
                       Blocks[x][y].Location.getY() * CONSTANTS.BLOCK_HEIGHT);
            }
         }
      }
      if (theRunner != null) {
         Gc.drawImage(theRunner.Image.theImage, theRunner.Location.getX(), theRunner.Location.getY());
      }
      for (Sprite troll : Trolls) {
         Gc.drawImage(troll.Image.theImage, troll.Location.getX(), troll.Location.getY());
      }
   }

   public void save(String fileName) {
      System.err.println("save: filename = " + fileName);
      if (!fileName.contains(".ser")) {
         fileName = fileName + ".ser";
      }
      try (OutputStream file = new FileOutputStream(fileName);
              OutputStream buffer = new BufferedOutputStream(file);
              ObjectOutput output = new ObjectOutputStream(buffer);) {
         for (int x = 0; x < Width; x++) {
            for (int y = 0; y < Height; y++) {
               output.writeObject(Blocks[x][y].Type);
            }
         }
         output.writeObject(theRunner.Location);
         for (Sprite troll : Trolls) {
            output.writeObject(troll.Location);
         }
      } catch (IOException ex) {
         System.err.println("IO exception occured in save: " + ex.toString());
      }
   }

   public boolean restore(String fileName) {
      if (!fileName.contains(".ser")) {
         fileName = fileName + ".ser";
      }
      try (InputStream file = new FileInputStream(fileName);
              InputStream buffer = new BufferedInputStream(file);
              ObjectInput input = new ObjectInputStream(buffer);) {
         for (int x = 0; x < Width; x++) {
            for (int y = 0; y < Height; y++) {
               Block block = Blocks[x][y];
               block.Type = (BlockTypes) input.readObject();
               if (block.Type == BlockTypes.EMPTY) {
                  block.blockImage = null;
               } else {
                  block.blockImage = Images.getImage(block.Type);
               }
               if (block.blockImage != null) {
                  block.blockView = new ImageView(block.blockImage);
                  block.blockView.setX(block.Location.getX() * CONSTANTS.BLOCK_WIDTH);
                  block.blockView.setY(block.Location.getY() * CONSTANTS.BLOCK_HEIGHT);
               }
            }
         }
         MovableLocationType location = (MovableLocationType) input.readObject();
         if (theRunner == null) {
            theRunner = new Sprite(location, theRunnerMovies.Facing, Direction.FACING, SpriteType.RUNNER);
         } else {
            theRunner.Location = location;
            theRunner.Image = theRunnerMovies.Facing;
            theRunner.Orientation = Direction.FACING;
         }
         Trolls.clear();
         try {
            while (true) {
               location = (MovableLocationType) input.readObject();
               Sprite newTroll = new Sprite(location, theTrollMovies.Facing, Direction.FACING, SpriteType.TROLL);
               Trolls.add(newTroll);
            }
         } catch (IOException e) {
         }
         return true;
      } catch (ClassNotFoundException ex) {
         System.err.println("ClassNotFoundException exception occured");
      } catch (IOException ex) {
         System.err.println("IO exception occured in restore: " + ex.toString());
      }
      return false;
   }

}
