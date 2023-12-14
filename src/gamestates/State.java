package gamestates;

import audio.AudioPlayer;
import main.Game;
import ui.MenuButton;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static Probabilities.Probability.generateRandomNormalV2;

public class State extends Thread {

    protected Game game;

    public State(Game game){ //superclasse p tds os gameStates
        this.game=game;
    }

    //isInside the button
    public boolean isIn(MouseEvent e, MenuButton mb){
        return mb.getBounds().contains(e.getX(),e.getY()); //da true se o mouse estiver dentro do retangulo

    }

    public Game getGame(){
        return game;
    }

    public void setGameState(Gamestate state){
        switch (state){
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
        }

        Gamestate.state = state;
    }


    /*
    private int i=0;
    @Override
    public void run(){

        while(true) {
            int cloudEvent = (int) generateRandomNormalV2(100, 35);
            i++;
            if (cloudEvent == 0) {

                //g.drawImage(tiago!)
                System.out.println("EVENTO ESPECIAL AGORA!!!!!!!!!!!!!" + " " + i + "vezes");
                i = 0;

                try {
                    Thread.sleep(90000);
                } catch (InterruptedException e) {
                    System.out.println("erro no sleep da thread do evento especial");
                }

            }
        }

    }
    */
}
