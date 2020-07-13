package project.game.figures;


import project.common.Field;
import project.common.Figure;

public class Bishop extends FigureBase implements Figure
{
    public Bishop(boolean isWhite)
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
        return "Bishop["+color+"]"+this.whereAmI.getCol()+":"+this.whereAmI.getRow();
    }

    public boolean lal()
    {
        return false;
    }

   //done
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

    //checked direction and is empty of all field except moveTo i
    public boolean canIMoveTo(Field moveTo)
    {
        if(!canIMoveBasic(whereAmI, moveTo))
        {
            return false;
        }
        //Bishop can go only diagonal
        Field.Direction direction = this.whereAmI.getDirection(moveTo);
        switch(direction)
        {
            case LU:
            case RU:
            case LD:
            case RD:
                break;
            default:
                return false;
        }
        Field neighborInDirection = this.whereAmI.nextField(direction);
        while (neighborInDirection != moveTo)
        {
            if(!neighborInDirection.isEmpty())
                return false;
            neighborInDirection = neighborInDirection.nextField(direction);
        }
        return true;
    }

    @Override
    public char getType()
    {
        return 'S';
    }
}
