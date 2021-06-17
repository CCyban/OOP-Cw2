package Classes;

import Classes.Quiz.Result;
import Classes.Quiz.Test;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Banks {
    // Loads the data into the ObservableList
    public static void loadQuestionBank(Boolean useDialogOnSuccess, Boolean useDialogOnFailure, ObservableList observableListToLoadInto) {
        // Running an attempt to retrieve the data from the questionBank
        List retrievedData = Translating.deserialiseList("questionBank.ser", useDialogOnSuccess, useDialogOnFailure);
        if (retrievedData != null) {    // If successful then replace the currently used data with the loaded data
            observableListToLoadInto.clear();
            observableListToLoadInto.addAll(retrievedData);
        }
    }

    // Saves the data from the ObservableList
    public static void saveQuestionBank(Boolean useDialogOnSuccess, Boolean useDialogOnFailure, ObservableList observableListToSave) {
        // Sending the data from the ObservableList to be serialised as a questionBank file
        Translating.serialiseObject(observableListToSave.stream().toList() ,"questionBank.ser", useDialogOnSuccess, useDialogOnFailure);
    }

    public static void loadTestBank(Boolean useDialogOnSuccess, Boolean useDialogOnFailure, ObservableList observableListToLoadInto) {
        // Running an attempt to retrieve the data from the questionBank
        List retrievedData = Translating.deserialiseList("testBank.ser", useDialogOnSuccess, useDialogOnFailure);
        if (retrievedData != null) {    // If successful then replace the currently used data with the loaded data
            observableListToLoadInto.clear();
            observableListToLoadInto.addAll(retrievedData);
        }
    }

    public static void saveTestBank(Boolean useDialogOnSuccess, Boolean useDialogOnFailure, ObservableList observableListToSave) {
        // Sending the data from the ObservableList to be serialised as a testBank file
        Translating.serialiseObject(observableListToSave.stream().toList() ,"testBank.ser", useDialogOnSuccess, useDialogOnFailure);
    }

    // Loads the data into the ObservableList
    public static void loadResultBank(Boolean useDialogOnSuccess, Boolean useDialogOnFailure, ObservableList observableListToLoadInto) {
        // Running an attempt to retrieve the data from the resultBank
        List retrievedData = Translating.deserialiseList("resultBank.ser", useDialogOnSuccess, useDialogOnFailure);
        if (retrievedData != null) {    // If successful then replace the currently used data with the loaded data
            observableListToLoadInto.clear();
            observableListToLoadInto.addAll(retrievedData);
        }
    }

    // Sending the data from the ObservableList to be serialised as a resultBank file
    public static void saveResultBank(Boolean useDialogOnSuccess, Boolean useDialogOnFailure, ObservableList observableListToSave) {
        Translating.serialiseObject(observableListToSave.stream().toList() ,"resultBank.ser", useDialogOnSuccess, useDialogOnFailure);
    }

    // Updating the data in the resultBank file if any
    public static void updateResultBank(Boolean useDialogOnSuccess, Boolean useDialogOnFailure, Boolean isNewResult ,Result resultToSave) {
        List<Result> resultsList = new ArrayList<>();   // Creation of empty result bank

        // Running an attempt to retrieve the data from the resultBank
        loadResultBank(false, true, FXCollections.observableList(resultsList));

        if (isNewResult) {
            resultsList.add(resultToSave); // Simply adds result to the result bank
        }
        else {
            Result resultToUpdate = resultsList.stream()    // Finds the result to update
                    .filter(result -> resultToSave.getResultUUID().equals(result.getResultUUID()))
                    .findFirst()
                    .orElse(null);
            resultToUpdate.setResultData(resultToSave.getResultData()); // Updates the resultData of the result
        }

        // Sending the list data to be serialised as a resultBank file
        Translating.serialiseObject(resultsList, "resultBank.ser", useDialogOnSuccess, useDialogOnFailure);
    }

    public static void loadUserBank(Boolean useDialogOnSuccess, Boolean useDialogOnFailure, ObservableList observableListToLoadInto) {
        // Running an attempt to retrieve the data from the userBank
        List retrievedData = Translating.deserialiseList("userBank.ser", useDialogOnSuccess, useDialogOnFailure);
        if (retrievedData != null) {    // If successful then replace the currently used data with the loaded data
            observableListToLoadInto.clear();
            observableListToLoadInto.addAll(retrievedData);
        }
    }

    public static void saveUserBank(Boolean useDialogOnSuccess, Boolean useDialogOnFailure, ObservableList observableListToSave) {
        // Sending the data from the ObservableList to be serialised as a userBank file
        Translating.serialiseObject(observableListToSave.stream().toList() ,"userBank.ser", useDialogOnSuccess, useDialogOnFailure);
    }

    public static void loadClassBank(Boolean useDialogOnSuccess, Boolean useDialogOnFailure, ObservableList observableListToLoadInto) {
        // Running an attempt to retrieve the data from the classBank
        List retrievedData = Translating.deserialiseList("classBank.ser", useDialogOnSuccess, useDialogOnFailure);
        if (retrievedData != null) {    // If successful then replace the currently used data with the loaded data
            observableListToLoadInto.clear();
            observableListToLoadInto.addAll(retrievedData);
        }
    }

    public static void saveClassBank(Boolean useDialogOnSuccess, Boolean useDialogOnFailure, ObservableList observableListToSave) {
        // Sending the data from the ObservableList to be serialised as a classBank file
        Translating.serialiseObject(observableListToSave.stream().toList() ,"classBank.ser", useDialogOnSuccess, useDialogOnFailure);
    }

    public static void generateBanksIfNotFound() {
        if (Translating.deserialiseList("questionBank.ser", false, false) == null) // If there is no questionBank.ser
        {
            saveQuestionBank(false, false, FXCollections.observableArrayList());   // Creates empty questionBank
        }
        if (Translating.deserialiseList("testBank.ser", false, false) == null)    // If there is no testBank.ser
        {
            saveTestBank(false, false, FXCollections.observableArrayList());       // Creates empty testBank
        }
        if (Translating.deserialiseList("resultBank.ser", false, false) == null)  // If there is no resultBank.ser
        {
            saveResultBank(false, false, FXCollections.observableArrayList());     // Creates empty resultBank
        }
        if (Translating.deserialiseList("userBank.ser", false, false) == null)  // If there is no userBank.ser
        {
            saveUserBank(false, false, FXCollections.observableArrayList());     // Creates empty userBank
        }
        if (Translating.deserialiseList("classBank.ser", false, false) == null)  // If there is no classBank.ser
        {
            saveClassBank(false, false, FXCollections.observableArrayList());     // Creates empty classBank
        }
    }
}
