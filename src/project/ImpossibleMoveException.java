/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * ImpossibleMoveException class.
 *
 * */
package project;


public class ImpossibleMoveException extends Exception
{
    private int moveIndex;

    public ImpossibleMoveException(int moveIndex)
    {
        this.moveIndex = moveIndex;
    }

    public ImpossibleMoveException()
    {
        moveIndex = -1; //jen aby tam neco bylo, kdybych to nahodou prece jen nekde pouzil
    }

    public int getMoveIndex()
    {
        return moveIndex;
    }
}
