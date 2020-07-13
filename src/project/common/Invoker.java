/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * Interface for Invoker.
 *
 * */
package project.common;

public interface Invoker {
   public Command execute(Command cmd);
   public Command undo(boolean redoEnabled);
   public Command redo();

   void deleteRedoStack();
}
