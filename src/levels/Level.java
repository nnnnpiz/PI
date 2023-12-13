package levels;

import entities.Crabby;
import entities.Shark;
import main.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;
import utils.HelpMethods;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Probabilities.Probability.GenerateLvlDifficulty;
import static utils.HelpMethods.*;

public class Level {

    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crabby> crabs;
    private ArrayList<Shark> sharks;

    //LISTAS PARA MANTER OBJETOS ENTRE NIVEIS:
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private int lvlTilesWide ; //qts tiles tem o lvl em width
    private int maxTilesOffset; //offset de qts tiles conseguimos ver!!
    private int maxLvlOffsetX;
    private Point playerSpawn;

    public static int lvlDifficulty=-1;

    //dificuldade atual da ronda:
    public int getLvlDifficulty() {
        return lvlDifficulty;
    }

    public void setLvlDifficulty(int lvlDifficulty) {
        Level.lvlDifficulty = lvlDifficulty;
    }

    public Level(BufferedImage img){
        this.img = img;
        resetAux();
    }

    public void resetAux(){

        generateLvlDiff(); //todo NB
        createEnemiesVA();
        createLevelData();
        createPotions();
        createContainers();
        createSpikes();
        //createCannons(); todo nb
        calcLvlOffsets();
        calcPlayerSpawn();
    }

    private void generateLvlDiff(){
        lvlDifficulty = GenerateLvlDifficulty();
        System.out.println(lvlDifficulty+ "- nivel de dificuldade!");
    }

    private void createCannons() {
        cannons = GetCannons(img);
    }

    private void createSpikes() {
        spikes = GetSpikes(img);
    }

    private void createContainers() {
        //containers = GetContainers(img);
        containers = GetContainersVA(img);
    }

    private void createPotions() {
        potions = GetPotions(img);
    }

    private void calcPlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemiesVA(){
        crabs = new ArrayList<>();
        sharks = new ArrayList<>();
        cannons = new ArrayList<>();
        GetEnemiesVA(img, Level.lvlDifficulty, crabs, sharks, cannons);
        System.out.println(crabs.size() +" crabs size");
        System.out.println(sharks.size() +" sharks size");
        System.out.println(cannons.size() +" cannons size");
    }

    private void createEnemies() {
        crabs=GetCrabs(img);
        sharks = GetSharks(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIndex(int x,int y){
        return lvlData[y][x];
    }

    public int[][] getLvlData(){
        return lvlData;
    }

    public int getLvlOffset(){
        return maxLvlOffsetX;
    }

    public ArrayList<Crabby> getCrabs(){
        return crabs;
    }

    public ArrayList<Shark> getSharks(){
        return sharks;
    }

    public Point getPlayerSpawn(){
        return playerSpawn;
    }

    public ArrayList<Potion> getPotions(){
        return potions;
    }

    public ArrayList<GameContainer> getContainers(){
        return containers;
    }

    public ArrayList<Spike> getSpikes(){
        return spikes;
    }

    public ArrayList<Cannon> getCannons(){
        return cannons;
    }




}
