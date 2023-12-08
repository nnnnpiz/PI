package objects;

import entities.Player;
import gamestates.Playing;
import levels.Level;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.ObjectConstants.*;
import static utils.Constants.Projectiles.CANNON_BALL_HEIGHT;
import static utils.Constants.Projectiles.CANNON_BALL_WIDTH;
import static utils.HelpMethods.CanCannonSeePlayer;
import static utils.HelpMethods.IsProjectileHittingLevel;

public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage spikeImg, cannonBallImg;
    private BufferedImage[] cannonImgs;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectiles = new ArrayList<>(); //n vamos usar o Level pq o level nao determina quantos projectiles vamos ter!smp que o cannon der shoot damos add a esta array e damos draw no projectile! e update array



    public ObjectManager(Playing playing){
        this.playing= playing;
        loadImgs(); //p a bufferedimg array
    }

    //player toca num spike:
    public void checkSpikesTouched(Player p){
        for(Spike s:spikes)
            if(s.getHitbox().intersects(p.getHitbox()))
                p.kill();
    }


    public void checkObjectTouched(Rectangle2D.Float hitbox){
        //se o jogador tocar numa poçap
        for(Potion p: potions)
            if(p.isActive()) {
                if (hitbox.intersects(p.getHitbox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
    }

    public void applyEffectToPlayer(Potion p){
        //se tocarmos na oçao vamos aqui e dependende fad poçao fazemos algo
        if(p.getObjType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }

    public void checkObjectHit(Rectangle2D.Float attackbox){ //para os GameCOntainers( box e barrel)
        for(GameContainer gc:containers)
            if(gc.isActive() && !gc.doAnimation){
                if(gc.getHitbox().intersects(attackbox)){
                    gc.setAnimation(true); //hitted foi destruido vamos animar!
                    //dropa um item ccacda objeto:
                    int type=0;
                    if(gc.getObjType() == BARREL)
                        type=1; //TODO IMPLEMENTAR AQUI VA'S DOS ITEMS Q SAI NO BARREL OU BOX! AQUI SE O OBJTYPE HITTED É UM BARREL VOU ADICIONAR UMA POTION NOVA QUE VAI DROPAR COM O 1. E 1=BLUE_POTION
                    potions.add(new Potion((int)(gc.getHitbox().x+gc.getHitbox().width/2),
                            (int)(gc.getHitbox().y - gc.getHitbox().height/2),
                            type)); //adicionar novo item q vou dropar a lista de potions
                    return;
                }
            }

    }


    //temos uma potions array e containers. nos damos store no level qd criamos o level. temos que fazer:
    public void loadObjects(Level newLevel) {
        //isto p qd dermos restart voltar ao estado inicial
        potions = new ArrayList<>(newLevel.getPotions());
        containers=new ArrayList<>(newLevel.getContainers()); //vai buscar as listas smp q damos load no novo level.
        spikes = newLevel.getSpikes(); //n precisamos de fazer uma copia dos arrays cm ta em cima pq nos nnc damos reset nos spikes nem ha spikes spawning during the game
        cannons = newLevel.getCannons();
        projectiles.clear(); //limpamos a lista smp q começamos um novo nivel //TODO isto pode ser fonte do problema de nao disparar entre "RONDAS"
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs = new BufferedImage[2][7];

        for(int j=0; j<potionImgs.length; j++)
            for(int i =0; i< potionImgs[j].length; i++)
                potionImgs[j][i] = potionSprite.getSubimage(12*i, 16*j,12,16);



        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][8];

        for(int j=0; j<containerImgs.length; j++)
            for(int i =0; i< containerImgs[j].length; i++)
                containerImgs[j][i] = containerSprite.getSubimage(40*i, 30*j,40,30);

        spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);

        //Load sprite imgs p o cannon:
        cannonImgs = new BufferedImage[7];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);

        for(int i=0; i< cannonImgs.length; i++)
            cannonImgs[i] = temp.getSubimage(i*40, 0 ,40,26);

        cannonBallImg= LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);

    }

    //SO DAMOS UPDATE SE TAO ACTIVE
    public void update(int[][] lvlData, Player player){
        for(Potion p: potions)
            if(p.isActive())
                p.update();

        for(GameContainer gc:containers)
            if(gc.isActive())
                gc.update();

        updateCannon(lvlData, player);
        updateProjectiles(lvlData, player);
    }

    private void updateProjectiles(int[][] lvlData, Player player) {
        for(Projectile p: projectiles)
            if(p.isActive()){
                p.updatePos();

                if(p.getHitbox().intersects(player.getHitbox())){
                    player.changeHealth(-25);
                    p.setActive(false); //qd atinje um player n deve estar active naymore
                } else if(IsProjectileHittingLevel(p, lvlData))
                    p.setActive(false);
            }
    }

    private boolean isPlayerInRange(Cannon c, Player player) {
        int absValue =(int) Math.abs(player.getHitbox().x - c.getHitbox().x); //valor absoluto p dar a distancia
        return absValue <= Game.TILES_SIZE*5; //true logo esta in Range!
    }

    private boolean isPlayerInFrontOfCannon(Cannon c, Player player) {
        if(c.getObjType() == CANNON_LEFT){
            if(c.getHitbox().x > player.getHitbox().x) //SIGNIFICA QUE O PLAYER ESTA NO LEFT SIDE DA HITBOX DO CANNON!
                return true;

        } else if(c.getHitbox().x < player.getHitbox().x) //player esta a direita do cannon!
            return true;

        return false;
    }

    private void updateCannon(int[][] lvlData, Player player) {
        for(Cannon c:cannons) {
            if(!c.doAnimation)
                if(c.getTileY() == player.getTileY())
                    if(isPlayerInRange(c,player))
                        if(isPlayerInFrontOfCannon(c,player))
                            if(CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY()))
                                c.setAnimation(true); //temos q dar shoot no cannon numa animaçao do cannon especifica p ficar syncd se nao ele da shoot mal ve o player:


            c.update(); //tds os cannons estao active
            if(c.getAniIndex() == 4 && c.getAniTick() == 0) //animaçao do cannon a disparar!! o c.getAniTick() == 0 é para so disparar 1 ball q é o start da animaçao do cannon
                shootCannon(c);
        }

        //if the cannon is not animating: (se tiver annimating ja esta shooting!)
        /* 1) check if tileY is same
            2) Check if player is in range
            3) check if player is infront of cannon
            4) check line of sight
            5) shoot the cannoN!

         */
    }

    private void shootCannon(Cannon c) {
        int dir=1; //cannon ball p direita
        if(c.getObjType() == CANNON_LEFT)
            dir=-1;//p esq

        projectiles.add(new Projectile((int) c.getHitbox().x, (int) c.getHitbox().y, dir));
    }


    public void draw(Graphics g, int xLvlOffset){
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g,xLvlOffset);
        drawCannons(g, xLvlOffset);
        drawProjectiles(g, xLvlOffset);
    }

    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for(Projectile p:projectiles)
            if(p.isActive())
                g.drawImage(cannonBallImg, (int)(p.getHitbox().x - xLvlOffset), (int)(p.getHitbox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for(Cannon c:cannons){
            int x = (int) (c.getHitbox().x - xLvlOffset);
            int width = CANNON_WIDTH;

            if(c.getObjType() == CANNON_RIGHT){ //se o cannon esta facing the right:
                x +=width;
                width *=-1; //estamos a virar para a direita. PARA A ESQ É O DEFAULT
            }
            g.drawImage(cannonImgs[c.getAniIndex()], x, (int)(c.getHitbox().y), width, CANNON_HEIGHT, null);

        }

    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for(Spike s:spikes)
            g.drawImage(spikeImg, (int)(s.getHitbox().x -xLvlOffset), (int)(s.getHitbox().y - s.getyDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for(GameContainer gc:containers)
            if(gc.isActive()){
                int type =0;
                if(gc.getObjType() == BARREL)
                    type = 1; //se for 1 é para na matriz o x ser =1 que é p ir buscar o index certo do y da imagem!

                g.drawImage(containerImgs[type][gc.getAniIndex()],
                        (int)(gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset),
                        (int)(gc.hitbox.y - gc.getyDrawOffset()),
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT,
                        null);

            }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for(Potion p: potions)
            if(p.isActive()){
                int type=0; //blue é 0. das constants! red é 1
                if(p.getObjType() == RED_POTION)
                    type=1;

                g.drawImage(potionImgs[type][p.getAniIndex()],
                        (int)(p.getHitbox().x - p.getxDrawOffset() - xLvlOffset),
                        (int)(p.hitbox.y - p.getyDrawOffset()),
                        POTION_WIDTH,
                        POTION_HEIGHT,
                        null);


            }
    }

    public void resetAllObjects(){
        loadObjects(playing.getLevelManager().getCurrentLevel());

        for(Potion p: potions)
            p.reset();

        for(GameContainer gc:containers)
            gc.reset();

        for(Cannon c:cannons)
            c.reset();
    }



}
