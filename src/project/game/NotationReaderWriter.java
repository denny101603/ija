/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * NotationReaderWriter class.
 *
 * */
package project.game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import project.common.IReaderWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Observable;

public class NotationReaderWriter implements IReaderWriter
{
    private ObservableList<String> file;
    private String filenameForReading;

    public NotationReaderWriter(String filenameForReading)
    {
        if(filenameForReading == null)
        {
            file = FXCollections.observableArrayList();
            this.filenameForReading = null;
        }
        else
        {
            file = null;
            this.filenameForReading = filenameForReading;
        }
    }

    @Override
    public String GetLine(int line)
    {
        if(file == null)
            if(!ReadFile())
                return null; //unsuccessful reading
        try
        {
            return file.get(line);
        }
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
    }

    @Override
    public boolean Save(String gameNotation, String filename)
    {
        try
        {
            Files.write(Paths.get(filename), gameNotation.getBytes());
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    private boolean ReadFile()
    {
        try
        {
            file = FXCollections.observableArrayList(Files.readAllLines(Paths.get(filenameForReading)));
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    public ObservableList<String> GetNotation()
    {
        return file;
    }
}
