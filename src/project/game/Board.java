/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * Board class.
 *
 * */
package project.game;


import project.common.Field;
import project.common.Figure;

import java.util.ArrayList;
import java.util.List;

public class Board
{
    private BoardField[][] board;
    private int size;

    /**
     * Constructor. Sets board fields and for every field sets surrounding.
     * @param size Size of the board
     */
    public Board(int size)
    {
        this.size = size;
        this.board = new BoardField[size][size];

        //Set board
        for(int r =1; r <= size; r++)
        {
            for(int c =1; c <= size; c++)
            {
                this.board[c-1][r-1] = new BoardField(c,r); //indexing must be -1, BoardField eats col row, but I use row col, so a little bit confusing, but OK:)
            }
        }

        //Set surrounding
        for(int r = 1; r <= size; r++)
        {
            for(int c =1; c <= size; c++)
            {
                this.board[c-1][r-1].setSurrounding( this.getField(c-1, r), Field.Direction.L);
                this.board[c-1][r-1].setSurrounding(this.getField(c-1, r+1), Field.Direction.LU);
                this.board[c-1][r-1].setSurrounding(this.getField(c, r+1), Field.Direction.U);
                this.board[c-1][r-1].setSurrounding(this.getField(c+1, r+1), Field.Direction.RU);
                this.board[c-1][r-1].setSurrounding(this.getField(c+1, r), Field.Direction.R);
                this.board[c-1][r-1].setSurrounding(this.getField(c+1, r-1), Field.Direction.RD);
                this.board[c-1][r-1].setSurrounding(this.getField(c-1, r-1), Field.Direction.LD);
                this.board[c-1][r-1].setSurrounding(this.getField(c, r-1), Field.Direction.D);
            }
        }
    }

    /**
     * Gets the size of the board.
     * @return size (int)
     */
    public int getSize()
    {
        return this.size;
    }

    /**
     * ¨Gets the filed of given coordinates
     * @param col column coordinate
     * @param row row coordinate
     * @return Field on coordinates or null if coordinates are not valid
     */
    public Field getField(int col, int row)
    {
        col = col -1;
        row = row -1; //indexation fix
       // System.out.printf("Getting field on index %d %d\n", col, row);
        if(col < this.size && col >= 0 && row < this.size && row >= 0) {
            return (Field) this.board[col][row];
        } else
            return null;
    }

    public Field getField(char col, int row)
    {
        int newCol = (int) col - (int) 'a' + 1; //column by char converted to col by int
        return getField(newCol, row);
    }

    /**
     * Finds all figures of the right color on the board
     * @param isWhite
     * @return list of figures of the right color
     */
    public List<Figure> GetAllFigures(boolean isWhite)
    {
        List<Figure> figures = new ArrayList<>();
        for(int i = 0; i < getSize(); i++)
        {
            for (int j = 0; j < getSize(); j++)
            {
                Field field = board[i][j];
                if (!field.isEmpty())
                    if(field.get().isWhite() == isWhite)
                        figures.add(field.get());
            }
        }
        return figures;
    }



    //TODO tady deklarovat enum pro figurky. Nebo ve Figure?

}
