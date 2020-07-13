package project.game.figures;

import project.common.Field;
import project.common.Figure;

public class King extends FigureBase implements Figure
{

    public King(boolean isWhite)
    {
        this.isItWhite = isWhite;
        this.inGame = false;
        this.whereAmI = null;
    }

    public String getState() {
        String color;
        if(isWhite())
        {
            color = "W";
        }
        else
        {
            color = "B";
        }
        return "King["+color+"]"+this.whereAmI.getCol()+":"+this.whereAmI.getRow();
    }

    //
    public boolean move(Field moveTo) {
        if(!canIMoveTo(moveTo))
        {
            return false;
        }
        if(moveTo.containsEnemy(this.isWhite()))
        {
            moveTo.kill();
        }
        if(moveTo.isEmpty())
        {
            this.whereAmI.remove(this);
            moveTo.put(this);
            return true;
        }
        return false;
    }

    //checks of king moves only by one field.
    public boolean canIMoveTo(Field moveTo) {
        if(!canIMoveBasic(whereAmI, moveTo))
        {
            return false;
        }
        boolean contains = false;
        for(int i = 0; i < 8; i++)
        {
            Field field = whereAmI.getNeigbour(i);
            if(field != null && field.getRow() == moveTo.getRow()) // moveTo is off-by-one
            {
                    contains = true;
                    break;
            }
        }
        return contains;
    }

    @Override
    public char getType()
    {
        return 'K';
    }
}
