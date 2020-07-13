package project.game.figures;


import project.common.Field;
import project.common.Figure;

public class Pawn extends FigureBase implements Figure
{
    private boolean firstMove = true;
    public Pawn(boolean isWhite)
    {
        this.isItWhite = isWhite;
        this.inGame = false;
        this.whereAmI = null;
    }

    public String getState()
    {
        String color;
        if(isWhite())
        {
            color = "W";
        }
        else
        {
            color = "B";
        }
        return "Pawn["+color+"]"+this.whereAmI.getCol()+":"+this.whereAmI.getRow();
    }
    public void setFirstMove(boolean value)
    {
        this.firstMove = value;
    }
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

    //both two possibilities checked (straight and diagonal)
    //NOTE Only here canIMove checks even moveTo (because it is necessary to check legality of moveGUI)
    public boolean canIMoveTo(Field moveTo) {
        //common
        if(!canIMoveBasic(this.whereAmI, moveTo))
        {
            return false;
        }
        //Check if going only by one row
        if(((isWhite() && (moveTo.getRow() == this.whereAmI.getRow()+1)) ||
                (!isWhite() && (moveTo.getRow() == this.whereAmI.getRow()-1))) || (firstMove && ((isWhite() && (moveTo.getRow() == this.whereAmI.getRow()+2)) ||
                (!isWhite() && (moveTo.getRow() == this.whereAmI.getRow()-2)))))
        {
            firstMove = false;
        }
        else
        {
            return false;
        }

        Field.Direction direction = this.whereAmI.getDirection(moveTo);
        boolean moveStraight = (isWhite() && direction == Field.Direction.U) || (!isWhite() && direction == Field.Direction.D);
        //go straight without kill
        if(moveStraight && this.whereAmI.nextField(direction).isEmpty())
        {
            return true;
        }
        else //check diagonal direction and enemy on moveTo.
        {
            switch(direction)
            {
                case LU:
                case LD:
                case RU:
                case RD:
                    return !this.whereAmI.nextField(direction).isEmpty() && (this.whereAmI.nextField(direction).get().isWhite() != this.whereAmI.get().isWhite());
                default:
                    return false;
            }
        }
    }

    @Override
    public char getType()
    {
        return 'P';
    }
}
