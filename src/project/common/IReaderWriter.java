/*
 *  FIT VUT
 *  Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * Interface for ReaderWriter.
 *
 * */
package project.common;

import javafx.collections.ObservableList;

public interface IReaderWriter
{
    String GetLine(int line);
    boolean Save(String gameNotation, String filename);
    ObservableList<String> GetNotation();

}
