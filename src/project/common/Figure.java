/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * Interface for Figure.
 *
 * */
package project.common;

public interface Figure {


    boolean isWhite = false;


    String getState();

    Field getPositionField();

    boolean move(Field moveTo);
    boolean canIMoveTo(Field moveTo);
    void setPosition(Field boardField);
    void setInGame(boolean inGame);
    boolean isWhite();
    char getType();
    boolean isInGame();

    //For Undo
    void forceMove(Field moveTo);
    void forceSetInGame(boolean isIt);



}
