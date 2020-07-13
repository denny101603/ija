/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * ChessGame class.
 *
 * */
package project.game;


import javafx.collections.ObservableList;
import project.EndOfGameException;
import project.ImpossibleMoveException;
import project.common.Command;
import project.common.Field;
import project.common.Figure;
import project.common.Game;
import project.game.commands.MoveCommand;
import project.game.commands.MoveInvoker;
import project.game.figures.*;

import java.io.IOException;
import java.util.List;

public class ChessGame implements Game {

    private Board chessBoard;
    MoveInvoker invoker;
    private Parser parser;
    private InnerGameNotation gameNotation;
    private int moveIndex; //cislo aktualniho tahu
    private Figure whiteKing;
    private Figure blackKing;

    public ChessGame(Board board, String filename) throws IOException
    {
        commonConstructor(board);

        parser = new Parser(new NotationReaderWriter(filename), chessBoard);
        gameNotation = parser.ParseGameToInner();
        if(gameNotation == null)
        {
            throw new IOException();
        }
    }

    public ChessGame(Board board)
    {
        commonConstructor(board);
        parser = new Parser(new NotationReaderWriter(null), board);
        gameNotation = new InnerGameNotation();
    }

    /**
     * spolecna cast pro oba konstruktory
     * @param board
     */
    private void commonConstructor(Board board)
    {
        whiteKing = new King(true);
        blackKing = new King(false);

        moveIndex = 0;
        chessBoard = board;
        SetBoard(chessBoard);
        invoker = new MoveInvoker();
    }

    /**
     * nastavi zakladni rozestaveni figurek na board
     * @param board
     */
    private void SetBoard(Board board) {
        int max = board.getSize();

        for(int row = 1; row <= max; row++)
        {
            for(int col = 1; col <= max; col++)
            {
                switch(row){
                    case 1:
                        switch(col)
                        {
                            case 1:
                            case 8:
                                board.getField(col, row).put(new Rook(true));
                                break;
                            case 2:
                            case 7:
                                board.getField(col, row).put(new Knight(true));
                                break;
                            case 3:
                            case 6:
                                board.getField(col, row).put(new Bishop(true));
                                break;
                            case 5:
                                board.getField(col, row).put(new Queen(true));
                                break;
                            case 4:
                                board.getField(col, row).put(whiteKing);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 2:
                        board.getField(col, row).put(new Pawn(true));
                        break;
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        break;
                    case 7:
                        board.getField(col, row).put(new Pawn(false));
                        break;
                    case 8:
                        switch(col)
                        {
                            case 1:
                            case 8:
                                board.getField(col, row).put(new Rook(false));
                                break;
                            case 2:
                            case 7:
                                board.getField(col, row).put(new Knight(false));
                                break;
                            case 3:
                            case 6:
                                board.getField(col, row).put(new Bishop(false));
                                break;
                            case 5:
                                board.getField(col, row).put(new Queen(false));
                                break;
                            case 4:
                                board.getField(col, row).put(blackKing);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Tries to execute move
     * @param figure
     * @param field
     * @return null if impossible move or filled Command
     */
    private Command move(Figure figure, Field field)
    {
        if(figure == null)
            return null;
        Command cmd = new MoveCommand(figure, figure.getPositionField(), field, field.get());
        return invoker.execute(cmd);
    }

    @Override
    public void undo() { invoker.undo(false);
    }

    /**
     * Does move back in existing notation
     * @return
     */
    public Command backMove()
    {
        moveIndex--;
        Command tmp =invoker.undo(false);
        if(tmp != null)
            return tmp;

        moveIndex++;
        return null;
    }

    /**
     * Undo user's move - deletes it from notation
     * @return
     */
    public Command undoMove()
    {
        moveIndex--;
        gameNotation.DeleteMovesFromIndexToEnd(moveIndex);
        parser.FromInnerGameNotation(gameNotation);
        return invoker.undo(true);
    }

    /**
     * Overi nektere podminky, ktere jsou nutne k povoleni vraceni uzivatelskych tahu
     * @return true pokud je mozno provest uzivatelsky tah zpet
     */
    public boolean canUndo()
    {
        if(moveIndex == 0)
            return false;
        return moveIndex == gameNotation.GetSize();
    }

    @Override
    public Parser getParser() {
        return this.parser;
    }

    @Override
    public InnerGameNotation getGameNotation() {
        return this.gameNotation;
    }

    public Command redoMove()
    {
        Command cmd = invoker.redo();
        if(cmd == null)
            return null;
        gameNotation.AddMove(new InnerMoveNotation(cmd.getFrom(), cmd.getTo(), cmd.getMoving().getType()));
        parser.FromInnerGameNotation(gameNotation);
        moveIndex++;
        return cmd;
    }

    /**
     * Increases moveIndex, gets the move from gameNotation, finds the right figure to do the move and does the move
     * @return Command with the move or null if next move is not possible (there are no more notated moves)
     * @throws ImpossibleMoveException in case of no found figure to do the move from gameNotation
     */
    @Override
    public Command nextMove() throws ImpossibleMoveException, EndOfGameException
    {
        if(isEndOfGame())
            throw new EndOfGameException();

        if(moveIndex >= gameNotation.GetSize())
            return null;

        InnerMoveNotation moveNotation = gameNotation.GetMove(moveIndex++);

        if (moveNotation.fieldFrom != null)
        {
            Command cmd = move(moveNotation.fieldFrom.get(), moveNotation.fieldTo); //todo pokud zbyde cas, zkontrolovat jestli to sedi s typem figurky
            if(cmd == null)
            {
                moveIndex--;
                throw new ImpossibleMoveException(moveIndex);
            }
            else
                return cmd;
        }
        else
        {
            List<Figure> figures = chessBoard.GetAllFigures(moveNotation.isWhite);
            for (Figure figure : figures)
            {
                if (moveNotation.movingFigureType == figure.getType())
                {
                    Command cmd = move(figure, moveNotation.fieldTo);
                    if (cmd != null) //pokud se tah povedl, vratim ho
                    {
                        moveNotation.fieldFrom = cmd.getFrom();
                        return cmd;
                    }
                }
            }
            moveIndex--;
            throw new ImpossibleMoveException(moveIndex);
        }
    }

    @Override
    public int getActualMoveIndex()
    {
        return moveIndex;
    }

    /**
     * Do moves initiated by user using project.GUI
     * @param moveNotation
     * @return
     * @throws ImpossibleMoveException
     */
    @Override
    public Command doUsersMove(InnerMoveNotation moveNotation) throws ImpossibleMoveException
    {
        try
        {
            if(moveNotation.isWhite == null)
                throw new ImpossibleMoveException(moveIndex);
            if((moveIndex%2 == 1 && moveNotation.isWhite) || (moveIndex%2 == 0 && !moveNotation.isWhite))
                throw new ImpossibleMoveException(moveIndex);
            gameNotation.DeleteMovesFromIndexToEnd(moveIndex);
            invoker.deleteRedoStack();
            gameNotation.AddMove(moveNotation);
            parser.FromInnerGameNotation(gameNotation);
            return nextMove();
        }
        catch (ImpossibleMoveException e)
        {
            gameNotation.DeleteMovesFromIndexToEnd(moveIndex);
            invoker.deleteRedoStack();
            parser.FromInnerGameNotation(gameNotation);
            throw e;
        }
    }

    public ObservableList<String> getNotation()
    {
        return parser.readerWriter.GetNotation();
    }

    /**
     * Zjistuje jestli ziji oba kralove
     * @return true pokud je nejaky kral mimo hru -> je konec hry, jinak false
     */
    private boolean isEndOfGame()
    {
        if(whiteKing.isInGame() && blackKing.isInGame())
            return false;
        return true;
    }
}
