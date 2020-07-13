/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * Interface for Field.
 *
 * */
package project.common;

public interface Field
{
    Field[] surrounding = new Field[8];

    void addNextField(Field.Direction dirs, Field field);
    Figure get();
    boolean isEmpty();
    Field nextField(Field.Direction dirs);
    boolean put(Figure disk);
    boolean remove(Figure disk);
    Direction getDirection(Field field);
    int getRow();
    int getCol();
    boolean containsEnemy(boolean isWhite);
    char getColAsChar();
    Field getNeigbour(int indexToSurrounding);
    void kill();


    /**
     * Enumeration for directions. Its method .ordinal() used to index field surrounding.
     */
    public enum Direction
    {
        D, L, LD, LU, R, RD, RU, U
    }

    static Field.Direction valueOf(String name)
    {
        //Vrátí enum konstantu s daným jménem
        return null;
    }

    static Field.Direction[] values()
    {
        //Vrátí pole obsahující konstanty v pořadí, v jakém byly nadeklarovány
        return null;
    }

}
