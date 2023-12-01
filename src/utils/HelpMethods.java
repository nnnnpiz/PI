package utils;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class HelpMethods {

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData){

        //top left e bottom right first q damos check.
        if(!IsSolid(x,y,lvlData))
            if(!IsSolid(x+width, y+height, lvlData))
                if(!IsSolid(x+width, y, lvlData))
                    if(!IsSolid(x, y+height,lvlData))
                        return true;

        return  false;

    }

    private static boolean IsSolid(float x, float y, int[][] lvlData){

        if(x<0 || x >= Game.GAME_WIDTH)
            return true;

        if( y<0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y /Game.TILES_SIZE;

        int value = lvlData[(int)yIndex][(int)xIndex];

        if(value >= 48 || value < 0 || value != 11)//check se é um tile. 11 é um transparent tile
            return true;
        return false;


    } //check se é TILE ou se a pos esta dentro da gamewindow

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed){

        int currentTile = (int) (hitbox.x/Game.TILES_SIZE); //currenttile q o player esta on

        //check se foi colide p direita ou esq
        if(xSpeed > 0){
            //right
            int tileXPos = currentTile * Game.TILES_SIZE; //pixel value p o currentTile
            int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset -1;
        } else {
            //left
            return currentTile * Game.TILES_SIZE;
        }

    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed){

        int currentTile = (int) (hitbox.y/Game.TILES_SIZE);

        if(airSpeed > 0){ //falling / touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset-1;
        } else {
            //jumping
            return currentTile * Game.TILES_SIZE;
        }

    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData){
        //check no pixel abaixo bottomlefft e bottom right (corners). se os dois n forem solid estamos no ar
        if(!IsSolid(hitbox.x, hitbox.y + hitbox.height+1, lvlData ))
            if(!IsSolid(hitbox.x + hitbox.width,hitbox.y + hitbox.height+1, lvlData ))
                return false;

        return true; //estsamos no chao
    }
}
