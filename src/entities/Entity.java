package entities;

//abstract => nao posso criar um objeto dela. dou lhe extends e store values cm posiçao, vida etc (conceitos de POO)
public abstract class Entity {

    protected float x,y,width,height; //protected pq se fosse private so podiamos usar x e y nesta classe mm que extendida. Protected => so as que dao extend nesta classe podem usar os atributos dela

    public Entity(float x, float y, int width, int height){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
    }

}
