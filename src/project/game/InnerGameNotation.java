/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * InnerGameNotation class.
 *
 * */
package project.game;

import java.util.ArrayList;
import java.util.List;

public class InnerGameNotation
{
    private List<InnerMoveNotation> gameNotation;

    public InnerGameNotation()
    {
        gameNotation = new ArrayList<>();
    }

    /**
     * Adds move to gameNotation
     * @param move
     */
    public void AddMove(InnerMoveNotation move)
    {
        if(gameNotation.size() % 2 == 0) //prave tahne bily
            move.isWhite = true;
        else
            move.isWhite = false;

        gameNotation.add(move);
    }

    /**
     * Returns move with the index
     * @param indexOfMove index of move
     * @return
     */
    public InnerMoveNotation GetMove(int indexOfMove)
    {
        return gameNotation.get(indexOfMove);
    }

    public int GetSize()
    {
        return gameNotation.size();
    }

    /**
     * deletes all moves after index (including move on indexFrom)
     * @param indexFrom
     */
    public void DeleteMovesFromIndexToEnd(int indexFrom)
    {
        if (GetSize() > indexFrom)
        {
            gameNotation.subList(indexFrom, GetSize()).clear();
        }

    }
}
