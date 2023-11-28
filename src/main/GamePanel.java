package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utils.Constants.Direction.*;
import static utils.Constants.PlayerConstants.*;


public class GamePanel extends JPanel {
    private MouseInputs mouseInputs; //crio este atributo de class de forma a nao passar como MouseListener dois objetos diferentes
    private float xDelta=100, yDelta=100;
    //private Random random;
    private BufferedImage img; //usamos BufferedImage para desta forma termos numa so imagem o conjunto de sprites das animaçoes e assim podermos selecionar qual sprite usar com uma so imagem!
    private BufferedImage[][] animations; //esta e as seguintes serao as arrays c as imagens de cada "estado" do jogador de forma a percorrermos o array e mudar o sprite drawn de forma a simular movement
    //mudamos p matriz p poder ter todas as animaçoes num so load
    private int aniTick, aniIndex, aniSpeed = 15; //120 FPS / 4 animaçoes por segundo = 30. Mas ficava mt lento animaçao por isso mudei p 15.
    private int playerAction = IDLE;
    private int playerDir = -1; //pq Iddle = -1. Moving = 0,1,2,3
    private boolean moving = false;


    public GamePanel(){
        mouseInputs = new MouseInputs(this);

        importImg();
        loadAnimations();
        
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs); //clicks
        addMouseMotionListener(mouseInputs); //drags
    }

    private void loadAnimations() {
        animations = new BufferedImage[9][6]; //tamnho do array´2 vai ser consoante oq temos na img c o conj de Sprites!
        for(int j=0; j<animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                //x=i*64 pq é o x d cada sprite. y=0 pq vamos começar na 1a linha dos sprites, os outros sao p tamanho do sprite em si
                animations[j][i] = img.getSubimage(i * 64, j*40, 64, 40);
            }
        }
    }

    private void importImg() {
        //default com g.drawImage(img.getSubimage(0,0, 64,40), 0, 0, 128, 80, null);
        InputStream is = getClass().getResourceAsStream("/player_sprites.png");
        InputStream is2 = getClass().getResourceAsStream("/megaSprite_1.png");

        try {//load inputStream:
            img = ImageIO.read(is);

        } catch (IOException e) {
            System.out.println("error loading image!");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                System.out.println("error trying to close inputStream");
            }
        }
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1280,800); //imgs de 32 pixeis por 32 => 1280/32 = 40 wide. 800/32 = 25 height
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

   public void setDirection(int direction){
        this.playerDir = direction;
        moving=true;

   }

   public void setMoving(boolean moving){
        this.moving=moving;

   }

    private void updateAnimationTick() {

        aniTick++;
        if(aniTick >= aniSpeed){
            aniTick=0; //reset no tick
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(playerAction)){ //dependendo da playerAction que esta a ser corrida nos vamos ate ao ultimo index dessa linha da matriz (neste caso IDLE =5)
                aniIndex=0;
            }
        }


    }

    private void setAnimation() {
        if(moving){
            playerAction= RUNNING;
        } else {
            playerAction = IDLE;
        }
    }

    private void updatePos() {
        if(moving){
            switch(playerDir){
                case LEFT:
                    xDelta-=5;
                    break;
                case UP:
                    yDelta-=5;
                    break;
                case RIGHT:
                    xDelta+=5;
                    break;
                case DOWN:
                    yDelta+=5;
                    break;
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        updateAnimationTick();

        setAnimation();
        updatePos();
        //subImg = img.getSubimage(1*64, 8*40, 64, 40); //vamos buscar outro sprite da matriz o (1,8)
        g.drawImage(animations[playerAction][aniIndex], (int)xDelta,(int)yDelta, 256, 160, null); //4º arg. ImageObserver usado p monitorizar o estado da imagem antes de ser FULLY drawn nao vai ser usado por questoes de simplicidade de codigo
        //nb q width x height faz de "factor" e multiplica o tamanho do sprite deste q seja o dobro p ficar prop.
    }




}
