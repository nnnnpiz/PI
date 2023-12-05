package utils;

import entities.Crabby;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.CRABBY;

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

    private static boolean IsSolid(float x, float y, int[][] lvlData){ //precisamos de saber o quao big é o nivel mesmo total
        int maxWidth = lvlData[0].length*Game.TILES_SIZE; //width do nivel todo

        if(x<0 || x >= maxWidth)
            return true;

        if( y<0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y /Game.TILES_SIZE;

        return IsTileSolid((int)xIndex, (int)yIndex,lvlData);


    } //check se é TILE ou se a pos esta dentro da gamewindow

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData){
        int value = lvlData[yTile][xTile];

        if(value >= 48 || value < 0 || value != 11)//check se é um tile. 11 é um transparent tile
            return true;
        return false;
    }

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

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if(xSpeed >0)
            return IsSolid(hitbox.x +hitbox.width + xSpeed , hitbox.y + hitbox.height + 1, lvlData);
        else
            return IsSolid(hitbox.x + xSpeed , hitbox.y + hitbox.height + 1, lvlData);
    }

    //estamos a dar check em cada TIle que esta enntre o player e o inimigo.
    public static boolean IsAllTilesWalkable (int xStart, int xEnd, int y, int[][] lvlData){
        for(int i =0; i<xEnd-xStart; i++) {
            if (IsTileSolid(xStart + i, y, lvlData)) //enctramos algo solid
                return false;
            if(!IsTileSolid(xStart+i,y+1,lvlData)) //y+1 para darmos check no tile abaixo ou seja p ver se tem pit ou n!
                return false;
        }
        return true;
    }

    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox,
                                       Rectangle2D.Float secondHitbox, int yTile){
        int firstXTile = (int)firstHitbox.x / Game.TILES_SIZE;
        int secondXTile = (int) secondHitbox.x /Game.TILES_SIZE;

        if(firstXTile > secondXTile)
            return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);

    }

    public static int[][] GetLevelData(BufferedImage img){
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];

        for(int j =0; j<img.getHeight();j++)
            for(int i =0; i<img.getWidth(); i++){
                Color color = new Color(img.getRGB(i,j));
                int value = color.getRed();
                if(value >= 48)
                    value =0;
                lvlData[j][i] = value;
            }
        return lvlData; //wtv value o red for vai ser o index p aquele sprite.
    }

    public static ArrayList<Crabby> GetCrabs(BufferedImage img){
        ArrayList<Crabby> list = new ArrayList<>();

        for(int j =0; j<img.getHeight();j++)
            for(int i =0; i<img.getWidth(); i++){
                Color color = new Color(img.getRGB(i,j));       //TODO VVAMOS METER UMA V.A DE GERAR INIMIIGOS AQUI
                int value = color.getGreen();//usamos o GREEN do pixel para posicionar um inimigo la
                if(value == CRABBY) //se value == 0 ou seja é um CRABBY
                    list.add(new Crabby(i*Game.TILES_SIZE, j*Game.TILES_SIZE)); //adicionamos um CRABBY nessa posiçao p meter na lista! crabbies com posiçoes
            }
        return list;
    }

    //TODO usar outro metodo igual  a este q spawna o heroi smp no mm sitio
    public static Point GetPlayerSpawn(BufferedImage img){
        for(int j =0; j<img.getHeight();j++)
            for(int i =0; i<img.getWidth(); i++){
                Color color = new Color(img.getRGB(i,j));
                int value = color.getGreen();
                if(value == 100) //n vamos ter 100 inimigos logo player vai ser valor de green 100
                    return new Point(i*Game.TILES_SIZE, j*Game.TILES_SIZE);

            }
        return new Point(1*Game.TILES_SIZE, 1*Game.TILES_SIZE);
    }
}
