package entities;

import gamestates.Playing;
import levels.Level;
import utils.LoadSave;

import static Probabilities.Probability.funPoissonSingle;
import static utils.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private ArrayList<Crabby> crabbies = new ArrayList<>(); //Lista com todos os crabs
    private BufferedImage[][] sharkArr;
    private ArrayList<Shark> sharks = new ArrayList<>();


    public EnemyManager(Playing playing){
        this.playing=playing;
        loadEnemyImgs();

    }

    public ArrayList<Crabby> getCrabbies() {
        return crabbies;
    }

    public ArrayList<Shark> getSharksies() {
        return sharks;
    }

    public void killAll(){
        for ( Crabby c : crabbies)
            c.hurt(c.getCurrentHealth());
        for ( Shark s : sharks)
            s.hurt(s.getCurrentHealth());
    }

    public void loadEnemies(Level level) {
        crabbies = level.getCrabs();
        sharks = level.getSharks();
    }

    public void update(int[][] lvlData, Player player){
        boolean isAnyActive = false; //deixar a false!
        for(Crabby c : crabbies)
            if(c.isActive()){
                c.update(lvlData, player);
                isAnyActive = true;
            }

        for(Shark s : sharks)
            if(s.isActive()){
                s.update(lvlData, player);
                isAnyActive=true;
            }


        if(!isAnyActive) {
            //playing.setLevelCompleted(true);
            //mudar de nivel mas para o mesmo para dar um efeito de rondas !
            playing.setLevelCompleted(true);
        }
    }

    public void draw(Graphics g, int xLvlOffset){
        drawCrabs(g, xLvlOffset);
        drawSharks(g,xLvlOffset);
    }

    private void drawSharks(Graphics g, int xLevelOffset){
        for(Shark s: sharks)
            if(s.isActive()) {
                g.drawImage(sharkArr[s.getState()][s.getAniIndex()],
                        (int) (s.getHitbox().x - SHARK_DRAWOFFSET_X) - xLevelOffset + s.flipX(),
                        (int) (s.getHitbox().y - SHARK_DRAWOFFSET_Y),
                        SHARK_WIDTH * s.flipW(), SHARK_HEIGHT, null);
                //s.drawHitbox(g, xLevelOffset);// for debugging hitbox
                //s.drawAttackBox(g, xLevelOffset); //for debugging attackbox
            }
    }

    private void drawCrabs(Graphics g, int xLevelOffset) {
        for(Crabby c:crabbies)
            if(c.isActive()) { //so dou draw se tiver active
                g.drawImage(crabbyArr[c.getState()][c.getAniIndex()],
                        (int) (c.getHitbox().x - CRABBY_DRAWOFFSET_X) - xLevelOffset + c.flipX(),
                        (int) (c.getHitbox().y - CRABBY_DRAWOFFSET_Y),
                        CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);
                //c.drawHitbox(g, xLvlOffset); for debugging crab hitbox
                //c.drawAttackBox(g, xLevelOffset);
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crabby c : crabbies)
            if(c.isActive())
                if(c.getCurrentHealth() > 0)
                    if (attackBox.intersects(c.getHitbox())) { //se der true nos tocamos no inimigo!
                        c.hurt(
                            funPoissonSingle()*6 //VA poisson de crit do player!
                        );
                        return;
                    }

        for (Shark s : sharks)
            if(s.isActive())
                if(s.getCurrentHealth() > 0)
                    if (attackBox.intersects(s.getHitbox())) { //se der true nos tocamos no inimigo!
                        s.hurt(
                                funPoissonSingle()*6
                        ); //VA poisson
                        return;
                    }
    }

    private void loadEnemyImgs(){
        crabbyArr = new BufferedImage[5][9]; //5 estados diferentes
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
        for(int j=0; j< crabbyArr.length; j++)
            for(int i=0; i<crabbyArr[j].length; i++)
                crabbyArr[j][i] = temp.getSubimage(i*CRABBY_WIDTH_DEFAULT, j*CRABBY_HEIGHT_DEFAULT , CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);


        sharkArr = new BufferedImage[5][8];
        BufferedImage temp_shark = LoadSave.GetSpriteAtlas(LoadSave.SHARK_SPRITE);
        for(int j=0; j< sharkArr.length; j++)
            for(int i=0; i<sharkArr[j].length; i++)
                sharkArr[j][i] = temp_shark.getSubimage(i*SHARK_WIDTH_DEFAULT, j*SHARK_HEIGHT_DEFAULT , SHARK_WIDTH_DEFAULT, SHARK_HEIGHT_DEFAULT);

    }

    public void resetAllEnemies(){
        for(Crabby c: crabbies)
            c.resetEnemy();

        for(Shark s: sharks)
            s.resetEnemy();
    }


}