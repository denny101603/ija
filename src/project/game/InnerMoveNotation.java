/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * InnerMoveNotation class.
 *
 * */
package project.game;

import project.common.Field;

public class InnerMoveNotation
{
    Field fieldFrom;
    Field fieldTo;
    char movingFigureType;
    Boolean isWhite;
    Boolean sach;
    Boolean mat;

    public InnerMoveNotation(Field from, Field to, char movingFigureType) //todo sach,mat
    {
        fieldFrom = from;
        fieldTo = to;
        this.movingFigureType = movingFigureType;
        if(from != null && from.get() != null)
            isWhite = from.get().isWhite();
        sach = null;
        mat = null;
    }
}
