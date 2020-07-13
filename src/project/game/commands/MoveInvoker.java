/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * MoveInvoker class.
 *
 * */
package project.game.commands;

import project.common.Command;
import project.common.Invoker;

import java.util.Stack;

public class MoveInvoker implements Invoker
{
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

    public MoveInvoker(){
        undoStack = new Stack<Command>();
        redoStack = new Stack<Command>();
    }


    public Command execute(Command cmd)
    {
        boolean succeeded = cmd.execute();
        if(succeeded)
        {
            undoStack.push(cmd);
            return cmd;
        }
        else
        {
            return null;
        }
    }

    public Command undo(boolean redoEnabled) {
        if(!undoStack.empty()) {
            Command cmd = undoStack.pop();
            cmd.undo();
            if(redoEnabled)
            {
                redoStack.push(cmd);
            }
            return cmd;
        }
        return null;
    }

    /**
     * Method redos rule from redoStack - just redirects cmd from redostack to execute.
     * @return cmd or null
     */
    @Override
    public Command redo() {
        if(redoStack.empty())
            return null;
        return this.execute(redoStack.pop());

    }

    @Override
    public void deleteRedoStack() {
        this.redoStack.clear();
    }
}
