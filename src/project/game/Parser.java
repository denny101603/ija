/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * Parser class.
 *
 * */
package project.game;

import javafx.collections.ObservableList;
import project.common.Field;
import project.common.IReaderWriter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser
{
    IReaderWriter readerWriter;
    Board board;
    String pattern1 = "^";
    String patternShort = "\\. ([S,K,V,D,J])?([a-h])([1-8])[+#]? ([S,K,V,D,J])?([a-h])([1-8])[+#]?$";
    String patternLong = "\\. ([S,K,V,D,J])?([a-h])([1-8])([a-h])([1-8])[+#]? ([S,K,V,D,J])?([a-h])([1-8])([a-h])([1-8])[+#]?$";
    //todo promena pěšec ->??

    public Parser(IReaderWriter readerWriter, Board board)
    {
        this.readerWriter = readerWriter;
        this.board = board;
    }

    /**
     * Converts official notation of game to InnerGameNotation
     * @return InnerGameNotation on success, null otherwise
     */
    public InnerGameNotation ParseGameToInner()
    {
        InnerGameNotation gameNotation = new InnerGameNotation();
        String line;
        Pattern r;
        Matcher m;
        int i;
        for(i = 0; (line = readerWriter.GetLine(i)) != null; i++)
        {
            r = Pattern.compile(pattern1 + Integer.toString(i+1) + patternShort);
            m = r.matcher(line);
            if(m.find())
            {
                Field to = board.getField(m.group(2).charAt(0), Integer.parseInt(m.group(3)));
                Field to2 = board.getField(m.group(5).charAt(0), Integer.parseInt(m.group(6)));
                InnerMoveNotation moveNotation = new InnerMoveNotation(null, to, getFigureType(m.group(1)));
                InnerMoveNotation moveNotation2 = new InnerMoveNotation(null, to2, getFigureType(m.group(4)));
                gameNotation.AddMove(moveNotation);
                gameNotation.AddMove(moveNotation2);
            }
            else
            {
                r = Pattern.compile(pattern1 + Integer.toString(i+1) + patternLong);
                m = r.matcher(line);
                if(m.find())
                {
                    Field from = board.getField(m.group(2).charAt(0), Integer.parseInt(m.group(3)));
                    Field from2 = board.getField(m.group(7).charAt(0), Integer.parseInt(m.group(8)));
                    Field to = board.getField(m.group(4).charAt(0), Integer.parseInt(m.group(5)));
                    Field to2 = board.getField(m.group(9).charAt(0), Integer.parseInt(m.group(10)));

                    InnerMoveNotation moveNotation = new InnerMoveNotation(from, to, getFigureType(m.group(1)));
                    InnerMoveNotation moveNotation2 = new InnerMoveNotation(from2, to2, getFigureType(m.group(6)));
                    gameNotation.AddMove(moveNotation);
                    gameNotation.AddMove(moveNotation2);
                } //TODO ukonceni tahem bileho
                else //notace neodpovida pravidlum
                    return null;
            }
        }
        if(i == 0) //unsuccessful reading
            return null;
        return gameNotation;
    }

    /**
     * Converts InnerGameNotation to official notation and saves it to file
     * @param gameNotation
     * @param filename
     * @return true if success, false otherwise
     */
    public boolean SaveGameNotation(InnerGameNotation gameNotation, String filename)
    {
        return readerWriter.Save(FromInnerGameNotation(gameNotation), filename);
    }

    /**
     * Converts innerGameNotation to official notation (string), also used for reloading observable list for ListView
     * @param gameNotation
     * @return String with official notation of game
     */
    public String FromInnerGameNotation(InnerGameNotation gameNotation)
    {
        ObservableList<String> notation = readerWriter.GetNotation();
        notation.clear();
        String line;
        StringBuilder retStr = new StringBuilder();
        for(int i = 0;(line = FromInnerMoveNotation(gameNotation, i)) != null;i = i + 2)
        {
            notation.add(line);
            retStr.append(line);
        }

        return retStr.toString();
    }

    /**
     * Converts two innerMoveNotations from InnerGameNotation to string - official notation
     * @param gameNotation Must have not null Fields from and to
     * @param whiteMoveIndex Index of moveGUI of white figure
     * @return string line with long notation
     */
    public String FromInnerMoveNotation(InnerGameNotation gameNotation, int whiteMoveIndex)
    {
        if(whiteMoveIndex%2 != 0)
            return null;
        InnerMoveNotation move1;
        InnerMoveNotation move2;
        try
        {
            move1 = gameNotation.GetMove(whiteMoveIndex);
        }
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
        try
        {
            move2 = gameNotation.GetMove(whiteMoveIndex+1);
        }
        catch (IndexOutOfBoundsException e)
        {
            move2 = null;
        }

        StringBuilder retStr = new StringBuilder(Integer.toString(whiteMoveIndex/2+1) + ". ");
        getHalfMoveNotation(move1, retStr);
        retStr.append(" ");
        if(move2 != null)
        {
            getHalfMoveNotation(move2, retStr);
        }
        retStr.append(System.getProperty("line.separator"));

        return retStr.toString();
    }

    /**
     *
     * @param move
     * @param retStr
     */
    private void getHalfMoveNotation(InnerMoveNotation move, StringBuilder retStr)
    {
        if(move.movingFigureType != 'P')
            retStr.append(move.movingFigureType);
        retStr.append(move.fieldFrom.getColAsChar());
        retStr.append(move.fieldFrom.getRow());
        retStr.append(move.fieldTo.getColAsChar());
        retStr.append(move.fieldTo.getRow());
    }

    /**
     * From char in string fromGroup gets the char meaning a figure type
     * @param fromGroup
     * @return 'P' if string is null, other chars if not null
     */
    private char getFigureType(String fromGroup)
    {
        if(fromGroup != null)
            return fromGroup.charAt(0);
        else
            return 'P'; //pesak
    }
}
