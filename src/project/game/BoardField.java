/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * BoardField class.
 *
 * */
package project.game;


import project.common.Field;
import project.common.Figure;

public class BoardField implements Field
{
    private boolean empty;
    private Figure currentDisk;
    public Field[] surrounding;
    private int myCol;
    private int myRow;

    /**
     * Constructor. Sets proper coordinates.
     * @param row row coordinate
     * @param col col coordinate
     */
    public BoardField(int col, int row) {
        this.empty = true;
        this.currentDisk = null;
        this.surrounding = new Field[8];
        this.myCol = col;
        this.myRow = row;
    }


    /**
     * Debug function which prints surrounding
     * note funkce jen pro debug
     */
    public void printSurrounding() {
        System.out.printf("Kamen na souradnicich: %d %d \n", this.myCol, this.myRow);
        System.out.print("D:");
        if (((BoardField) this.surrounding[Direction.D.ordinal()]) != null)
            System.out.printf("%d %d\n", ((BoardField) this.surrounding[Direction.D.ordinal()]).myCol, ((BoardField) this.surrounding[Direction.D.ordinal()]).myRow);
        System.out.print("L:");
        if (((BoardField) this.surrounding[Direction.L.ordinal()]) != null)
            System.out.printf("%d %d\n", ((BoardField) this.surrounding[Direction.L.ordinal()]).myCol, ((BoardField) this.surrounding[Direction.L.ordinal()]).myRow);
        System.out.print("LD:");
        if (((BoardField) this.surrounding[Direction.LD.ordinal()]) != null)
            System.out.printf("%d %d\n", ((BoardField) this.surrounding[Direction.LD.ordinal()]).myCol, ((BoardField) this.surrounding[Direction.LD.ordinal()]).myRow);
        System.out.print("LU:");
        if (((BoardField) this.surrounding[Direction.LU.ordinal()]) != null)
            System.out.printf("%d %d\n", ((BoardField) this.surrounding[Direction.LU.ordinal()]).myCol, ((BoardField) this.surrounding[Direction.LU.ordinal()]).myRow);
        System.out.print("R:");
        if (((BoardField) this.surrounding[Direction.R.ordinal()]) != null)
            System.out.printf("%d %d\n", ((BoardField) this.surrounding[Direction.R.ordinal()]).myCol, ((BoardField) this.surrounding[Direction.R.ordinal()]).myRow);
        System.out.print("RD:");
        if (((BoardField) this.surrounding[Direction.RD.ordinal()]) != null)
            System.out.printf("%d %d\n", ((BoardField) this.surrounding[Direction.RD.ordinal()]).myCol, ((BoardField) this.surrounding[Direction.RD.ordinal()]).myRow);
        System.out.print("RU:");
        if (((BoardField) this.surrounding[Direction.RU.ordinal()]) != null)
            System.out.printf("%d %d\n", ((BoardField) this.surrounding[Direction.RU.ordinal()]).myCol, ((BoardField) this.surrounding[Direction.RU.ordinal()]).myRow);
        System.out.print("U:");
        if (((BoardField) this.surrounding[Direction.U.ordinal()]) != null)
            System.out.printf("%d %d\n", ((BoardField) this.surrounding[Direction.U.ordinal()]).myCol, ((BoardField) this.surrounding[Direction.U.ordinal()]).myRow);
    }

    /**
     * Sets one field of surrounding in given direction
     * @param field Field which has to be set as a neighbor
     * @param direction Given direction
     */
    public void setSurrounding(Field field, Direction direction) {
        this.surrounding[direction.ordinal()] = field;
    }

    /**
     * Adds next field. Same functionality as setSurrounding, so probably duplicit
     * @param dirs Given direction
     * @param field Field which has to be set as a neighbor
     */
    public void addNextField(Field.Direction dirs, Field field) {
        this.surrounding[dirs.ordinal()] = field;
    }

    /**
     * Gets the current disk
     * @return Current disk on this field or null
     */
    public Figure get() {
        return this.currentDisk;
    }

    /**
     * Returns if field is empty
     * @return true/false if field is empty
     */
    public boolean isEmpty() {
        return this.empty;
    }

    /**
     * Returns next field from its surrounding in given direction
     * @param dirs Given direction
     * @return Field in direction of dirs or null, if there is no field
     */
    public Field nextField(Field.Direction dirs) {
        return (Field) this.surrounding[dirs.ordinal()];
    }

    /**
     * Puts disk on this field, if possible
     * @param disk disk to put
     * @return true/false if disk was really placed
     */
    public boolean put(Figure disk) {
        this.currentDisk = disk;
        this.currentDisk.setInGame(true);
        disk.setPosition(this);
        this.empty = false;
        return true;
    }

    /**
     * Remove disk from this field if possible
     * @param disk disk to remove
     * @return true/false if disk was really removed
     */
    public boolean remove(Figure disk) {
        if (this.currentDisk.equals(disk)) {
            this.currentDisk.setInGame(false);
            this.currentDisk.setPosition(null);
            this.currentDisk = null;
            this.empty = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the column of field
     * @return column number (indexed from 1)
     */
    public int getCol() {
        return this.myCol;
    }

    public char getColAsChar()
    {
        return (char) (myCol + 'a' - 1);
    }

    @Override
    public Field getNeigbour(int indexToSurrounding) {
        return this.surrounding[indexToSurrounding];
    }


    //checks if the field contains a figure of the opposite color of isWhite
    public boolean containsEnemy(boolean isWhite) {
        return !this.isEmpty() && this.get().isWhite() != isWhite;
    }


    @Override
    public void kill() {
        if(this.empty)
        {
            return;
        }
        this.currentDisk.setInGame(false);
        this.currentDisk.setPosition(null);
        this.currentDisk = null;
        this.empty = true;
    }

    /**
     * Gets the current row field
     * @return row number (indexed from 1)
     */
    public int getRow(){
        return this.myRow;
    }

    /**
     * Function checks in what direction is field given by parameter
     * @param field Field which direction to this field we want to know
     * @return Direction or null, if field == this
     */
    public Direction getDirection(Field field)
    {
        if(((BoardField)field).myCol < this.myCol)
        {
            if(((BoardField)field).myRow < this.myRow)
            {
                return Direction.LD;
            }
            else if(((BoardField)field).myRow == this.myRow)
            {
                return Direction.L;
            }
            else //(((BoardField)field).myRow > this.myRow)
            {
                return Direction.LU;
            }
        }
        else if(((BoardField)field).myCol == this.myCol)
        {
            if(((BoardField)field).myRow < this.myRow)
            {
                return Direction.D;
            }
            else if(((BoardField)field).myRow > this.myRow)
            {
                return Direction.U;
            }
            else //the same disk
            {
                return null;
            }
        }
        else //(((BoardField)field).myCol > this.myCol)
        {
            if(((BoardField)field).myRow < this.myRow)
            {
                return Direction.RD;
            }
            else if(((BoardField)field).myRow == this.myRow)
            {
                return Direction.R;
            }
            else //(((BoardField)field).myRow > this.myRow)
            {
                return Direction.RU;
            }
        }

    }

}
