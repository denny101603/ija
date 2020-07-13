package project.GUI;

import javafx.scene.control.Alert;

public class AlertShower
{
    public static void impossibleMoveWarning()
    {
        showAlert("Pokusili jste se provést nemožný tah!", Alert.AlertType.WARNING);
    }

    public static void endOfGameWarning()
    {
        showAlert("Další tah není možný! Hra již skončila!", Alert.AlertType.WARNING);
    }

    public static void notationReadError()
    {
        showAlert("Nepovedlo se načíst notaci! Je ve špatném formátu nebo je problém s otevřením souboru", Alert.AlertType.ERROR);
    }

    public static void notationSaveError()
    {
        showAlert("Nepovedlo se exportovat notaci!", Alert.AlertType.ERROR);
    }

    public static void backMoveWarning()
    {
        showAlert("Další tah zpět není!", Alert.AlertType.WARNING);
    }

    public static void undoMoveWarning()
    {
        showAlert("Není možné vrátit další tah!", Alert.AlertType.WARNING);
    }

    public static void noNextMoveWarning()
    {
        showAlert("Není možné provést další tah!", Alert.AlertType.WARNING);
    }

    public static void noMoveSelectedWarning()
    {
        showAlert("Není vybraný žádný tah!", Alert.AlertType.WARNING);
    }

    public static void noRedoMoveWarning()
    {
        showAlert("Žádný uživatelský tah k obnovení!", Alert.AlertType.WARNING);
    }

    private static void showAlert(String message, Alert.AlertType type)
    {
        new Alert(type, message).showAndWait();
    }
}
