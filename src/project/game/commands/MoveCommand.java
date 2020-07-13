/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * MoveCommand class.
 *
 * */
package project.game.commands;


import project.common.Command;
import project.common.Field;
import project.common.Figure;

public class MoveCommand implements Command {
    private Figure moving;
    private Figure wasKilled;
    private Field from;
    private Field to;

    public MoveCommand(Figure moving, Field from, Field to, Figure wasKilled){
        this.moving = moving;
        this.wasKilled = wasKilled;
        this.to = to;
        this.from = from;
    }
    public boolean execute() {
        return moving.move(to);
    }

    @Override
    public void undo() {
        moving.forceMove(from);
        if(wasKilled != null)
        {
            wasKilled.forceSetInGame(true);
            wasKilled.forceMove(to);
        }
    }

    public Figure getWasKilled() {
        return wasKilled;
    }

    public Figure getMoving() {
        return moving;
    }

    @Override
    public Field getTo() {
        return to;
    }

    @Override
    public Field getFrom()
    {
        return from;
    }
}
