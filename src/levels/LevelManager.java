package levels;

import gamestates.Gamestate;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.Game.TILES_SIZE;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int lvlIndex=0;

    public LevelManager(Game game){
        this.game = game;
       // levelSprite = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    public void loadNextLevel(){
        lvlIndex++;
        if(lvlIndex >= levels.size()){
            lvlIndex=0;
            System.out.println("No more levels! Gamme completed!");
            Gamestate.state = Gamestate.MENU;
        }

        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLvlData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }

    //inicializar todos os niveisd na arraylist
    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels(); //NB
        for(BufferedImage img:allLevels)
            levels.add(new Level(img));
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48]; //12x4 background
        for(int j =0; j<4; j++)
            for(int i=0; i<12; i++){
                int index = j*12 + i;
                levelSprite[index] = img.getSubimage(i*32, j*32, 32,32);
            }
    }

    public void draw(Graphics g, int lvlOffset){
        for(int j =0; j<Game.TILES_IN_HEIGHT; j++)
            for(int i=0; i<levels.get(lvlIndex).getLvlData()[0].length; i++){
                int index = levels.get(lvlIndex).getSpriteIndex(i,j);
                g.drawImage(levelSprite[index], TILES_SIZE*i - lvlOffset, TILES_SIZE*j,TILES_SIZE,TILES_SIZE, null);
            }


    }

    public void update(){

    }

    public Level getCurrentLevel(){
        return levels.get(lvlIndex);
    }

    public int getAmountOfLevels(){
        return levels.size();
    }
}
