package project.game.figures;


import project.common.Field;
import project.common.Figure;

//TODO kdo muze koho preskakovat
public class FigureBase {
    protected boolean isItWhite;
    protected boolean inGame;
    protected Field whereAmI;

    public boolean isInGame()
    {
        return inGame;
    }

    public void forceMove(Field moveTo) {
        if(this.whereAmI != null)
        {
            this.whereAmI.remove((Figure) this);
        }
        else
        {
            forceSetInGame(true);
        }
        moveTo.put((Figure) this);
        checkIfPawnsFirst(moveTo);
    }

    private void checkIfPawnsFirst(Field moveTo) {
        if(this.getClass() == Pawn.class)
        {
            if((this.isWhite() && moveTo.getRow() == 2) || (!this.isWhite() && moveTo.getRow() == 7 ))
            {
                ((Pawn)this).setFirstMove(true);
            }
        }

    }

    public void forceSetInGame(boolean isIt) {
        this.inGame = isIt;
    }

    /**
     * Sets if current disk is in game
     * @param isIt true/false according to real state
     */
    public void setInGame(boolean isIt)
    {
        this.inGame = isIt;
    }

    /**
     * Function returns if the current disk is white
     * @return true/false according to real state
     */
    public boolean isWhite()
    {
        return this.isItWhite;
    }

    /**
     * Function sets the position of the disk to the given field.
     * @param field Given field in which we want to place disk
     */
    public void setPosition(Field field)
    {
        this.whereAmI = field;
    }

    public Field getPositionField()
    {
        return this.whereAmI;
    }

    //Common funcionality derived from canIMove - no redundant code.
    // Always called from figure-specific canIMove
    protected boolean canIMoveBasic(Field from, Field moveTo)
    {
        if(this.whereAmI == moveTo)
        {
            return false; //Cannot make zero-moveGUI (written in forum)
        }
        if(!this.inGame)
        {
            return false; //Have to be in game :)
        }
        return true;
    }
}
