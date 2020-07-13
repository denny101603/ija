/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * TestingReaderWriter class.
 *
 * */
package project.game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project.common.IReaderWriter;

import java.util.Arrays;

public class TestingReaderWriter implements IReaderWriter
{
    private String vstup[] = {"1. a3 d6\n",
            "2. d3 Jc6\n",
            "3. e2e4 e7e5\n",
            "4. Sf1c4 Dd8f6\n"}; //todo sach, mat, posledni tah jen jedne figurky

    public String GetLine(int line)
    {
        if(line >= vstup.length)
            return null;
        return vstup[line];
    }

    @Override
    public boolean Save(String gameNotation, String filename)
    {
        System.out.println(gameNotation);
        return true;
    }

    public ObservableList<String> GetNotation()
    {
        ObservableList<String> ret = FXCollections.observableArrayList();
        ret.addAll(Arrays.asList(vstup));
        return ret;
    }

}
