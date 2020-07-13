package project.game.figures;


import project.common.Field;
import project.common.Figure;

public class Rook extends FigureBase implements Figure
{


    public Rook(boolean isWhite)
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
        return "Rook["+color+"]"+this.whereAmI.getCol()+":"+this.whereAmI.getRow();
    }


    @Override
    public boolean move(Field moveTo) {
        if(!canIMoveTo(moveTo))
        {
            return false;
        }

        //can moveGUI and possibly kill
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

    //Check
    public boolean canIMoveTo(Field moveTo)
    {
        if(!canIMoveBasic(this.whereAmI, moveTo))
        {
            return false;
        }

        Field.Direction direction = this.whereAmI.getDirection(moveTo);
        switch(direction) //if bad direction, return false. Strange, but seems OK and quite nice
        {
            case L:
            case R:
            case U:
            case D:
                break;
            default:
                return false; //vez nemuze do strany
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
        return 'V';
    }

}
