package model;

import java.util.ArrayList;

/**
 * Created by tgdflto1 on 30/09/16.
 */
public abstract class Person {
    protected  ArrayList<Position> oldPositions = new ArrayList<>();
    protected ArrayList<Position> currentPosition;
    protected ArrayList<Position> path;
    protected int age;
    protected int speed;
    //protected Character character;


    public ArrayList<Position> getOldPositions() {
        return oldPositions;
    }

    public void setOldPositions(ArrayList<Position> oldPositions) {
        this.oldPositions = oldPositions;
    }

    public ArrayList<Position> getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(ArrayList<Position> currentPosition) {
        this.currentPosition = currentPosition;
    }

    public ArrayList<Position> getPath() {
        return path;
    }

    public void setPath(ArrayList<Position> path) {
        this.path = path;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
