package project;
/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * GameFactory class.
 *
 * */

import project.common.Game;
import project.game.Board;
import project.game.ChessGame;

import java.io.IOException;

public abstract class GameFactory {
    public GameFactory()
    {

    }

    public static Game createChessGame(Board board, String filename) throws IOException
    {
        return new ChessGame(board, filename);
    }


    public static Game createChessGame(Board board)
    {
        return new ChessGame(board);
    }
}
