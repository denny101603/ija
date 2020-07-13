/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * TabViewController class.
 *
 * */

package project.GUI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import project.EndOfGameException;
import project.GameFactory;
import project.ImpossibleMoveException;
import project.common.Command;
import project.common.Field;
import project.common.Figure;
import project.common.Game;
import project.game.Board;
import project.game.InnerMoveNotation;
import project.game.figures.*;

public class TabViewController implements Initializable {

    private int userOn;
    private Game game;
    private Board board;
    private AutoPlayThread thread; //thread for autoplay
    private int defaultAutoplaySpeed = 1000;

    //only for user's moves
    private Field from;
    private Image fromImage;
    private Field to;
    private Image toImage;


    @FXML
    private Button nextButton;
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;
    @FXML
    private Button backButton;
    @FXML
    private Button StartButton;
    @FXML
    private Button StopButton;
    @FXML
    private Button jumpButton;
    @FXML
    private Button exportButton;
    @FXML
    private Button readNotationButton;
    @FXML
    private Button AutoPlaySpeedChangeButton;

    @FXML
    private TextField AutoPlaySpeedTextField;
    @FXML
    private GridPane grid;
    @FXML
    private ListView movesListView;

    private Image blackPawn = new Image("lib/BlackPawn.png");
    private Image blackRook = new Image("lib/BlackRook.png");
    private Image blackBishop = new Image("lib/BlackBishop.png");
    private Image blackKnight = new Image("lib/BlackKnight.png");
    private Image blackKing = new Image("lib/BlackKing.png");
    private Image blackQueen = new Image("lib/BlackQueen.png");

    private Image whitePawn = new Image("lib/WhitePawn.png");

    private Image whiteRook = new Image("lib/WhiteRook.png");
    private Image whiteBishop = new Image("lib/WhiteBishop.png");
    private Image whiteKnight = new Image("lib/WhiteKnight.png");
    private Image whiteKing = new Image("lib/WhiteKing.png");
    private Image whiteQueen = new Image("lib/WhiteQueen.png");

    private Image transparent = new Image("lib/transparentImage.png");
    private Image transparentSelected = new Image("lib/transparentImageSelected.png");

    private Image blackPawnSelected = new Image("lib/BlackPawnSelected.png");
    private Image blackRookSelected = new Image("lib/BlackRookSelected.png");
    private Image blackBishopSelected = new Image("lib/BlackBishopSelected.png");
    private Image blackKnightSelected = new Image("lib/BlackKnightSelected.png");
    private Image blackKingSelected = new Image("lib/BlackKingSelected.png");
    private Image blackQueenSelected = new Image("lib/BlackQueenSelected.png");

    private Image whitePawnSelected = new Image("lib/WhitePawnSelected.png");
    private Image whiteRookSelected = new Image("lib/WhiteRookSelected.png");
    private Image whiteBishopSelected = new Image("lib/WhiteBishopSelected.png");
    private Image whiteKnightSelected = new Image("lib/WhiteKnightSelected.png");
    private Image whiteKingSelected = new Image("lib/WhiteKingSelected.png");
    private Image whiteQueenSelected = new Image("lib/WhiteQueenSelected.png");


    public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane)
    {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
        for (Node node : childrens) {
            Integer myrow = GridPane.getRowIndex(node);
            if(myrow == null)
                myrow = 0;
            Integer mycol = GridPane.getColumnIndex(node);
            if(mycol== null)
                mycol = 0;
            if(mycol == column && myrow == row)
            {
                result = node;
                break;
            }
        }
        return result;
    }

    /**
     * Po kliknuti na tlacitko zobrazi filechooser a nastavi novou hru podle nactene notace
     * @param e
     */
    @FXML
    public void ReadNotationClicked(ActionEvent e)
    {
        File file = showFileChooser(false);
        if(file != null)
            setGameFromNotation(file);
    }

    /**
     * Spusti autoplay.
     * @param e
     */
    @FXML
    public void StartAutoPlay(ActionEvent e)
    {
        disableAutoplayButtons(true);
        thread = new AutoPlayThread(this, getAutoPlaySpeed());
        thread.start();
    }

    /**
     * Ziska z textFieldu rychlost prehravani, pri spatnem formatu do nej nastavi defaultAutoplaySpeed
     * @return pozadovanou rychlost nebo defaultAutoplaySpeed pri spatnem formatu
     */
    private int getAutoPlaySpeed()
    {
        try
        {
            int speed = Integer.parseInt(AutoPlaySpeedTextField.getText());
            if (speed <= 0)
                throw new NumberFormatException();
            return speed;
        }
        catch (NumberFormatException e)
        {
            AutoPlaySpeedTextField.setText(String.valueOf(defaultAutoplaySpeed));
            return defaultAutoplaySpeed;
        }
    }

    /**
     * Vypne autoplay.
     * @param e
     */
    @FXML
    public void StopAutoPlay(ActionEvent e)
    {
        disableAutoplayButtons(false);
        thread.myStop();
    }

    /**
     * Zmeni rychlost prehravani za behu, pokud se neprehrava, nedela nic
     * @param e
     */
    @FXML
    public void AutoPlaySpeedChangeButtonClicked(ActionEvent e)
    {
        try
        {
            thread.setSpeed(getAutoPlaySpeed());
        }
        catch (Exception ite)
        {
            return;
        }

    }

    /**
     * Provede dalsi tah.
     * @param e
     */
    @FXML
    public void NextMoveButtonClicked(ActionEvent e)
    {
        DoNextMove(true);
    }

    /**
     * Zajistuje zpracovani kliknuti na imageView. Do fieldu from a to nastavi policka na ktera bylo kliknuto.
     */
    EventHandler<MouseEvent> imageClickedEventHandler = new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent event)
        {
            ImageView clickedImage = (ImageView) event.getSource();
            Field clickedField = board.getField(GridPane.getColumnIndex(clickedImage)+1, GridPane.getRowIndex(clickedImage)+1);
            if(from == null) //zvolena teprve prvni souradnice, jeste se bude cekat na druhou
            {
                from = clickedField;
                fromImage = clickedImage.getImage();
                clickedImage.setImage(getImageSelectedByClass(clickedField.get()));
            }
            else
            {
                if(from == clickedField)
                {
                    clickedImage.setImage(fromImage);
                    toImage = null;
                    fromImage = null;
                    to = null;
                    from = null;
                    return;
                }

                to = clickedField;
                TryUsersMove();
                toImage = null;
                fromImage = null;
                to = null;
                from = null;
            }
        }
    };


    /**
     * Pokusi se vykonat tah zadany uzivatelem
     */
    private void TryUsersMove()
    {
        try
        {
            if(from.get() == null)
                throw new ImpossibleMoveException(game.getActualMoveIndex());

            InnerMoveNotation moveNotation = new InnerMoveNotation(from, to, from.get().getType());
            moveGUI(game.doUsersMove(moveNotation), false);
            userOn++;
        }
        catch (EndOfGameException eofg)
        {
            AlertShower.endOfGameWarning();
            ((ImageView)getNodeByRowColumnIndex(from.getRow()-1, from.getCol()-1, grid)).setImage(fromImage);
        }
        catch (ImpossibleMoveException ime)
        {
            AlertShower.impossibleMoveWarning();
            ((ImageView)getNodeByRowColumnIndex(from.getRow()-1, from.getCol()-1, grid)).setImage(fromImage);
        }
    }

    /**
     * Pokusi se provest dalsi tah podle notace.
     * @param showAlerts false, pokud se nema zobrazovat alert (v podstate jen kvuli autoplay)
     * @return uspesnost provedeni tahu
     */
    public boolean DoNextMove(boolean showAlerts)
    {
        try
        {
            Command cmd = game.nextMove();
            if (cmd == null)
            {
                if(showAlerts)
                    AlertShower.noNextMoveWarning();
                return false;
            }
            moveGUI(cmd, false);
            return true;
        }
        catch (EndOfGameException eofg)
        {
            if(showAlerts)
                AlertShower.endOfGameWarning();
        }
        catch (ImpossibleMoveException ime)
        {
            if(showAlerts)
                AlertShower.impossibleMoveWarning();
        }
        return false;
    }

    /**
     * Rozestavi Imageviews se spravnymi obrazky na sachovnici.
     */
    private void setBasicPositions()
    {
        ImageView im;
        for(int row = 0; row < 8; row++)
        {
            for(int col = 0; col < 8; col++)
            {
                Node existingImageView = getNodeByRowColumnIndex(row, col, grid); //in case of loading new game in existing tab
                if (existingImageView == null)
                {
                    im = new ImageView();
                    grid.add(im,col, row);
                }
                else
                {
                    im = (ImageView) existingImageView;
                }

                im.setPreserveRatio(true);
                im.setFitHeight(100);
                im.setPickOnBounds(true);
                im.setOnMouseClicked(imageClickedEventHandler);
                im.setImage(transparent);

                switch(row){
                    case 0:
                        switch(col)
                        {
                            case 0:
                            case 7:
                                im.setImage(whiteRook);
                                break;
                            case 1:
                            case 6:
                                im.setImage(whiteKnight);
                                break;
                            case 2:
                            case 5:
                                im.setImage(whiteBishop);
                                break;
                            case 4:
                                im.setImage(whiteQueen);
                                break;
                            case 3:
                                im.setImage(whiteKing);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 1:
                        im.setImage(whitePawn);
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        break;
                    case 6:
                        im.setImage(blackPawn);
                        break;
                    case 7:
                        switch(col)
                        {
                            case 0:
                            case 7:
                                im.setImage(blackRook);
                                break;
                            case 1:
                            case 6:
                                im.setImage(blackKnight);
                                break;
                            case 2:
                            case 5:
                                im.setImage(blackBishop);
                                break;
                            case 4:
                                im.setImage(blackQueen);
                                break;
                            case 3:
                                im.setImage(blackKing);
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
     * Po kliknuti na tlacitko vrati uzivateluv tah.
     * @param e
     */
    @FXML
    public void UndoMoveButtonClicked(ActionEvent e)
    {
        DoUndoMove();
    }

    /**
     * Po kliknuti na tlacitko udela krok zpet v notaci.
     * @param e
     */
    @FXML
    public void BackMoveButtonClicked(ActionEvent e)
    {
        DoBackMove();
    }

    /**
     * Obnovi uzivatelsky tah.
     * @param e
     */
    @FXML
    public void RedoMoveButtonClicked(ActionEvent e)
    {
        DoRedoMove();
    }

    /**
     * Zobrazi filechooser a pokusi se ulozit notaci.
     * @param e
     */
    @FXML
    public void ExportButtonClicked(ActionEvent e)
    {
        File file = showFileChooser(true);
        if(file == null)
            return;

        if(!game.getParser().SaveGameNotation(game.getGameNotation(), file.toString()))
        {
            AlertShower.notationSaveError();
        }
    }

    /**
     * Zobrazi okno pro otevreni/ulozeni souboru s notaci
     * @param save pokud true, potom se otevre okno pro ulozeni, pokud false, otevre se okno pro vyber souboru
     * @return null pokud byla akce zrusena, jinak File
     */
    private File showFileChooser(boolean save)
    {
        Stage stage = new Stage();
        final FileChooser fileChooser = new FileChooser();
        if(save)
        {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
            return fileChooser.showSaveDialog(stage);
        }
        else
            return fileChooser.showOpenDialog(stage);
    }


    /**
     * Obnovi uzivatelsky tah
     */
    private void DoRedoMove()
    {
        Command cmd = game.redoMove();
        if(cmd != null)
        {
            moveGUI(cmd, false);
            userOn++;
        }
        else
        {
            AlertShower.noRedoMoveWarning();
        }
    }

    /**
     * Udela krok zpet podle notace.
     */
    private void DoBackMove()
    {
        Command cmd = game.backMove();
        if(cmd == null)
        {
            AlertShower.backMoveWarning();
            return;
        }
        moveGUI(cmd, true);
    }

    /**
     * Vrati uzivatelsky tah.
     */
    private void DoUndoMove()
    {
        if(game.canUndo())
        {
            if(userOn != 0)
            {
                Command cmd = game.undoMove();
                moveGUI(cmd, true);
                userOn--;
                return;
            }
        }
        AlertShower.undoMoveWarning();
    }

    /**
     * Získá označený tah z listView a přesune hru před něj
     * @param e
     */
    @FXML
    public void MoveFromListViewSelectedHandle(ActionEvent e)
    {
        int selectedIndex  = movesListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex < 0)
        {
            AlertShower.noMoveSelectedWarning();
            return;
        }
        int indexToGo = 2 * selectedIndex;

        if(game.getActualMoveIndex() < indexToGo)
        {
            while(game.getActualMoveIndex() < indexToGo)
                DoNextMove(true);
        }
        else
        {
            while(game.getActualMoveIndex() > indexToGo)
            {
                DoBackMove();
            }
        }
    }


    public void moveGUI(Command cmd, boolean isUndo)
    {
        if(isUndo)
        {
            //nodeFrom is got from cmd.getTo() and vice versa
            ImageView nodeFrom = (ImageView) getNodeByRowColumnIndex(cmd.getTo().getRow()-1, cmd.getTo().getCol()-1, grid);
            ImageView nodeTo = (ImageView) getNodeByRowColumnIndex(cmd.getFrom().getRow()-1, cmd.getFrom().getCol()-1, grid);
            nodeTo.setImage(getImageByClass(cmd.getFrom().get()));
            //nodeTo.setImage(nodeFrom.getImage());
            Figure wasKilled = cmd.getWasKilled();
                if(wasKilled != null)
                {
                    nodeFrom.setImage(getImageByClass(wasKilled));
                }
                else
                {
                    nodeFrom.setImage(transparent);
                }
        }
        else
        {
            ImageView nodeTo = (ImageView) getNodeByRowColumnIndex(cmd.getTo().getRow()-1, cmd.getTo().getCol()-1, grid);
            ImageView nodeFrom = (ImageView) getNodeByRowColumnIndex(cmd.getFrom().getRow()-1, cmd.getFrom().getCol()-1, grid);
            nodeTo.setImage(getImageByClass(cmd.getTo().get()));
            //nodeTo.setImage(nodeFrom.getImage());
            nodeFrom.setImage(transparent);
        }

        SetActualMoveOnListView();
    }

    private Image getImageSelectedByClass(Figure figure) {
        Image returnImage = null;
        if(figure == null)
            return returnImage;
        if(figure.getClass() == Pawn.class)
        {
            returnImage = figure.isWhite() ? whitePawnSelected : blackPawnSelected;
        }
        else if(figure.getClass() == Rook.class)
        {
            returnImage = figure.isWhite() ? whiteRookSelected : blackRookSelected;
        }
        else if(figure.getClass() == Knight.class)
        {
            returnImage = figure.isWhite() ? whiteKnightSelected : blackKnightSelected;
        }
        else if(figure.getClass() == Bishop.class)
        {
            returnImage = figure.isWhite() ? whiteBishopSelected : blackBishopSelected;
        }
        else if(figure.getClass() == Queen.class)
        {
            returnImage = figure.isWhite() ? whiteQueenSelected : blackQueenSelected;
        }
        else if(figure.getClass() == King.class)
        {
            returnImage = figure.isWhite() ? whiteKingSelected : blackKingSelected;
        }
        return returnImage;
    }
    private Image getImageByClass(Figure figure)
    {
        Image returnImage = null;
        if(figure == null)
            return returnImage;
        if(figure.getClass() == Pawn.class)
        {
            returnImage = figure.isWhite() ? whitePawn : blackPawn;
        }
        else if(figure.getClass() == Rook.class)
        {
            returnImage = figure.isWhite() ? whiteRook : blackRook;
        }
        else if(figure.getClass() == Knight.class)
        {
            returnImage = figure.isWhite() ? whiteKnight : blackKnight;
        }
        else if(figure.getClass() == Bishop.class)
        {
            returnImage = figure.isWhite() ? whiteBishop : blackBishop;
        }
        else if(figure.getClass() == Queen.class)
        {
            returnImage = figure.isWhite() ? whiteQueen : blackQueen;
        }
        else if(figure.getClass() == King.class)
        {
            returnImage = figure.isWhite() ? whiteKing : blackKing;
        }
        return returnImage;
    }

    /**
     * zahájí novou hru podle předané notace
     * @param notationFile
     */
    private void setGameFromNotation(File notationFile)
    {
        try
        {
            userOn = 0;
            board = new Board(8);
            if(notationFile != null)
                game = GameFactory.createChessGame(board,notationFile.toString());
            else
                game = GameFactory.createChessGame(board);

            setChessBoardGUI();

            movesListView.setItems(game.getNotation());
            movesListView.getSelectionModel().select(0);

            AutoPlaySpeedTextField.setText("1000");
        }
        catch (IOException e)
        {
            AlertShower.notationReadError();
            game = GameFactory.createChessGame(board);
        }
    }

    /**
     * Provede vsechny potrebne upravy gui tykajici se sachovnice a tlacitek pred startem nove hry
     */
    private void setChessBoardGUI()
    {
        BackgroundImage bi = new BackgroundImage(new Image("lib/whiteField.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        grid.setBackground(new Background(bi));

        disableAutoplayButtons(false);

        setBasicPositions();
    }

    /**
     * Povoli nebo zakaze tlacitka, ktera mohou byt ovlivnena autoplayem
     * @param isAutoplayActive
     */
    public void disableAutoplayButtons(boolean isAutoplayActive)
    {
        StartButton.setDisable(isAutoplayActive);
        StopButton.setDisable(!isAutoplayActive);
        readNotationButton.setDisable(isAutoplayActive);
    }

    /**
     * Nacte novou hru bez notace.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        setGameFromNotation(null);
    }

    /**
     * Na Listview s notaci oznaci aktualni tah
     */
    private void SetActualMoveOnListView()
    {
        movesListView.getSelectionModel().select(game.getActualMoveIndex()/2);
    }
}