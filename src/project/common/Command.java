/*
 *  FIT VUT
* Project for IJA, 2018/2019
* Authors: Jan Beran (xberan43)
*           Daniel Bubenicek (xbuben05)
*
* Interface for Command.
*
* */
package project.common;

public interface Command {
    public boolean execute();
    public void undo();
    public Field getTo();
    Field getFrom();
    public Figure getWasKilled();
    public Figure getMoving();
}
