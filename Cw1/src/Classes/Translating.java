package Classes;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.io.*;
import java.util.List;

public class Translating {
    public static void serialiseObject(Object obj, String filename, Boolean useDialogOnSuccess, Boolean useDialogOnFailure) {
        // Serialization
        try
        {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(obj);

            out.close();
            file.close();

            if (useDialogOnSuccess) {
                Dialog<ButtonType> alertSignIn = new Alert(Alert.AlertType.INFORMATION);
                alertSignIn.setTitle("Data");
                alertSignIn.setHeaderText("Saved successfully");
                alertSignIn.show();
            }
        }
        catch(Exception ex)
        {
            if (useDialogOnFailure) {
                Dialog<ButtonType> alertSignIn = new Alert(Alert.AlertType.INFORMATION);
                alertSignIn.setTitle("Data");
                alertSignIn.setHeaderText("Failed to save - System Error");
                alertSignIn.showAndWait();
            }
        }
    }

    public static List deserialiseList(String filename, Boolean useDialogOnSuccess, Boolean useDialogOnFailure) {
        // Deserialization
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            List listData = (List) in.readObject();

            in.close();
            file.close();

            if (useDialogOnSuccess) {
                Dialog<ButtonType> alertSignIn = new Alert(Alert.AlertType.INFORMATION);
                alertSignIn.setTitle("Data");
                alertSignIn.setHeaderText("Loaded successfully");
                alertSignIn.show();
            }
            return listData;
        }
        catch(Exception ex)
        {
            if (useDialogOnFailure) {
                Dialog<ButtonType> alertSignIn = new Alert(Alert.AlertType.INFORMATION);
                alertSignIn.setTitle("Data");
                alertSignIn.setHeaderText("Failed to load - No data to load or System Error");
                alertSignIn.showAndWait();
            }
            return null;
        }
    }
}
