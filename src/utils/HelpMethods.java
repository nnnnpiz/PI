package utils;

import Probabilities.Probability;
import entities.Crabby;
import entities.Shark;
import main.Game;
import objects.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static Probabilities.Probability.generateRandomNormalV2;
import static utils.Constants.EnemyConstants.CRABBY;
import static utils.Constants.EnemyConstants.SHARK;
import static utils.Constants.ObjectConstants.*;

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
        int maxWidth = lvlData[0].length*Game.TILES_SIZE; //width do nivel

        if(x<0 || x >= maxWidth)
            return true;

        if( y<0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y /Game.TILES_SIZE;

        return IsTileSolid((int)xIndex, (int)yIndex,lvlData);


    } //check se é TILE ou se a pos esta dentro da gamewindow


    public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData){
        return IsSolid(p.getHitbox().x + p.getHitbox().width/2, p.getHitbox().y + p.getHitbox().height/2, lvlData); //getHitbox.x da o corner left do sprite logo precisamos d adicionar (p ficar no centro)
    } //desta forma mal tocar na parede vai detetar q tocou no "lvl" e vai desaparecer

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

    public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox,
                                             Rectangle2D.Float secondHitbox, int yTile){

        int firstXTile = (int)firstHitbox.x / Game.TILES_SIZE;
        int secondXTile = (int) secondHitbox.x /Game.TILES_SIZE;

        if(firstXTile > secondXTile)
            return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData); //IGUAL AO isSightClear MAS em vez de dar check se tds os tiles sao walkable usamos o IsAllTilesClear
        else
            return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);

    }

    public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData){ //checking se nao ha solid tiles de um ponto para outro pq do shoot do cannon
        for(int i =0; i<xEnd-xStart; i++)
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;

        return true;
    }

    //estamos a dar check em cada TIle que esta enntre o player e o inimigo.
    public static boolean IsAllTilesWalkable (int xStart, int xEnd, int y, int[][] lvlData){
        if(IsAllTilesClear(xStart,xEnd, y, lvlData))
            for(int i =0; i<xEnd-xStart; i++) {
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
       // list.add(new Crabby(200*Game.TILES_SIZE, 200*Game.TILES_SIZE));
        for(int j =0; j<img.getHeight();j++)
            for(int i =0; i<img.getWidth(); i++){
                Color color = new Color(img.getRGB(i,j));
                int value = color.getGreen();//usamos o GREEN do pixel para posicionar um inimigo la
                if(value == CRABBY) //se value == 0 ou seja é um CRABBY
                    list.add(new Crabby(i*Game.TILES_SIZE, j*Game.TILES_SIZE)); //adicionamos um CRABBY nessa posiçao p meter na lista! crabbies com posiçoes
            }
        return list;
    }

    public static ArrayList<Shark> GetSharks(BufferedImage img){
        ArrayList<Shark> list = new ArrayList<>();
        list.add(new Shark(3000, 300));

        for(int j =0; j<img.getHeight();j++)
            for(int i =0; i<img.getWidth(); i++){
                Color color = new Color(img.getRGB(i,j));
                int value = color.getGreen();//usamos o GREEN do pixel para posicionar um inimigo la
                if(value == SHARK) //se value == 0 ou seja é um CRABBY
                    list.add(new Shark(i*Game.TILES_SIZE, j*Game.TILES_SIZE));
            }
        return list;
    }

    //usar outro metodo igual  a este q spawna o heroi smp no mm sitio
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

    public static ArrayList<Potion> GetPotions(BufferedImage img){
        ArrayList<Potion> list = new ArrayList<>();

        /*
        for(int j =0; j<img.getHeight();j++)
            for(int i =0; i<img.getWidth(); i++){
                Color color = new Color(img.getRGB(i,j));
                int value = color.getBlue();//usamos o AZUL do pixel para posicionar um POTION LA, a azul tem pixeis diferentes!
                if(value == RED_POTION || value == BLUE_POTION)
                    list.add(new Potion(i*Game.TILES_SIZE, j*Game.TILES_SIZE, value));
            }
        */
        list.add(new Potion(985, 393, RED_POTION));
        list.add(new Potion(1243,323, BLUE_POTION));
        return list;
    }


    public static ArrayList<GameContainer> GetContainers(BufferedImage img){
        ArrayList<GameContainer> list = new ArrayList<>();

        for(int j =0; j<img.getHeight();j++)
            for(int i =0; i<img.getWidth(); i++){
                Color color = new Color(img.getRGB(i,j));
                int value = color.getBlue();//usamos o AZUL do pixel para posicionar um POTION LA, a azul tem pixeis diferentes! NOS ASSUMIS VALORES DE 1,2,3 PARA CADA PIXEL
                //valores RGC de azul: 0 => red potion. 1=> blue_potion, 2 => barrel 3 =>box. isto sao as constantes q representam accada tipo de obj
                if(value == BOX || value == BARREL)
                    list.add(new GameContainer(i*Game.TILES_SIZE, j*Game.TILES_SIZE, value));

            }
        return list;
    }

    //p probabilty; int = 2 => barrel. int =3 => Box.
    public static ArrayList<GameContainer> GetContainersVA(BufferedImage img){
        ArrayList<GameContainer> list = new ArrayList<>();

        while(true) {
            int containerX = (int) generateRandomNormalV2(50, 20);
            int containerX2 = (int) generateRandomNormalV2(50, 20);
            System.out.println("containerX p BARREL: " + containerX);
            System.out.println("/ containerX2 p BOX " + containerX2);
            // MAX = 3100, MIN 60!
            if (containerX * Game.TILES_SIZE > 3100 || containerX2*Game.TILES_SIZE > 3100)
               continue;
            else if (containerX * Game.TILES_SIZE == 0 || containerX2*Game.TILES_SIZE == 0){
                containerX=1;
                containerX2=2;
            }

            if(IsGcVAInBounds(containerX) && IsGcVAInBounds(containerX2)){
                list.add(new GameContainer(containerX * Game.TILES_SIZE, 580, 2));
                list.add(new GameContainer(containerX2 * Game.TILES_SIZE, 580, 3));
                return list;
            } //se for false volta a iterar pelo while e recalcula pos ate ter uma valida
        }
    }

    //Colina: Min=380 , Max=760 //Trap: Min=970, Max=1220, entre os dois valores n pode spawnar
    public static boolean IsGcVAInBounds(int x){
        int aux= x*Game.TILES_SIZE;
        if(aux >380 && aux<760)
            return false;
        else if(aux > 970 && aux <1220)
            return false;
        else
            return true;
    }

    public static ArrayList<Spike> GetSpikes(BufferedImage img){
        ArrayList<Spike> list = new ArrayList<>();

        for(int j =0; j<img.getHeight();j++)
            for(int i =0; i<img.getWidth(); i++){
                Color color = new Color(img.getRGB(i,j));
                int value = color.getBlue();
                if(value ==SPIKE)
                    list.add(new Spike(i*Game.TILES_SIZE, j*Game.TILES_SIZE, SPIKE));

            }
        return list;
    }


    //ir buscar os cannons ao nivel p meter la
    public static ArrayList<Cannon> GetCannons(BufferedImage img){
        ArrayList<Cannon> list = new ArrayList<>();

        for(int j =0; j<img.getHeight();j++)
            for(int i =0; i<img.getWidth(); i++){
                Color color = new Color(img.getRGB(i,j));
                int value = color.getBlue(); //valor 5 e 6 no RBG BLUE é p cannoN!
                if(value ==CANNON_LEFT || value == CANNON_RIGHT)
                    list.add(new Cannon(i*Game.TILES_SIZE, j*Game.TILES_SIZE, value));

            }
        return list;
    }



    public static void GetEnemiesVA(BufferedImage img,int diff,ArrayList<Crabby> c,ArrayList<Shark> s,ArrayList<Cannon> canhão){
        c.clear();
        s.clear();
        canhão.clear();
        if (diff == 0){
            for(int i = 1500;i < 2000;i=i+300){
                int enemy = Probability.whatEnemie();
                if(enemy == 1)
                    c.add(new Crabby(i, 300));
                if(enemy == 2)
                    s.add(new Shark(i, 300));
                if(enemy == 3)
                    canhão.add(new Cannon(i,580, 5));
            }
        }

        if (diff == 1){
            for(int i = 1500;i < 2200;i=i+300){
                int enemy = Probability.whatEnemie();
                if(enemy == 1)
                    c.add(new Crabby(i, 300));
                if(enemy == 2)
                    s.add(new Shark(i, 300));
                if(enemy == 3)
                    canhão.add(new Cannon(i,580, 5));
            }
        }

        if (diff == 2){
            for(int i = 1500;i < 2500;i=i+300){
                int enemy = Probability.whatEnemie();
                if(enemy == 1)
                    c.add(new Crabby(i, 300));
                if(enemy == 2)
                    s.add(new Shark(i, 300));
                if(enemy == 3)
                    canhão.add(new Cannon(i,580, 5));
            }
        }

        if (diff == 3){
            for(int i = 1500;i < 3000;i=i+300){
                int enemy = Probability.whatEnemie();
                if(enemy == 1)
                    c.add(new Crabby(i, 300));
                if(enemy == 2)
                    s.add(new Shark(i, 300));
                if(enemy == 3)
                    canhão.add(new Cannon(i,580, 5));
            }
        }

        c.add(new Crabby(2900, 300));

    }










}
