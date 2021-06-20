package Classes;

import Classes.Account.SysAdmin;
import Classes.Account.User;
import Classes.Quiz.Question;
import Classes.Quiz.Result;
import Enums.AccountType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DataPersistence {
    public static List loadBank(String bankName) {

        Connection c = null;
        PreparedStatement pstmt = null;
        List listData = null;

        try {
            java.lang.Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:SQLITE Database.db");  // Database to open

            pstmt = c.prepareStatement("SELECT * FROM SerialisedBanks WHERE BankName = ?");

            // Filling in the param on the prepared statement
            pstmt.setString(1, bankName);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("BankName"));
                System.out.println(rs.getBinaryStream("SerialisedBankBlob"));

                if (rs.getBinaryStream("SerialisedBankBlob") == null) { // If there isn't anything stored inside the selected bank
                    rs.close();
                    pstmt.close();
                    c.close();
                    return null;
                }

                ObjectInputStream inputStream = new ObjectInputStream(rs.getBinaryStream("SerialisedBankBlob"));    // Column to read

                // Deserialization of the object
                listData = (List) inputStream.readObject();

                // Close to avoid memory leaks
                inputStream.close();
                rs.close();
                pstmt.close();
                c.close();
            }
        }
        catch (Exception ex) {
            System.err.println("Exception from loading a bank");
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );
            System.exit(0);
        }
        System.out.println("Loaded");
        return listData;
    }

    public static void saveBank(String bankName, Object bankToSave) {
        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            java.lang.Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:SQLITE Database.db");  // Database to open

            pstmt = c.prepareStatement("UPDATE SerialisedBanks SET SerialisedBankBlob = ? WHERE BankName = ?;");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);

            // Serialization of the object
            out.writeObject(bankToSave);
            out.flush();

            // Filling in the params on the prepared statement
            pstmt.setBytes(1, baos.toByteArray());
            pstmt.setString(2, bankName);

            // Executing the prepared statement
            pstmt.executeUpdate();

            /*
            // Close to avoid memory leaks
            out.close();
            pstmt.close();
            c.close();

             */
        }
        catch (Exception ex) {
            System.err.println("Exception from saving a bank");
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        System.out.println("Saved");
    }

    // Updating the data in the resultBank file if any
    public static void updateResultBank(Boolean isNewResult , Result resultToSave) {
        ArrayList<Result> resultsList = new ArrayList<>();   // Creation of empty result bank

        // Running an attempt to retrieve the data from the resultBank
        resultsList.addAll(loadBank("resultBank"));

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

        // Saving the updated result bank
        saveBank("resultBank", resultsList);
    }

    public static void defaultDataIfNoAdminExists() {
        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            java.lang.Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:SQLITE Database.db");  // Database to open

            pstmt = c.prepareStatement("SELECT * FROM SerialisedBanks WHERE BankName = ?");

            // Filling in the param on the prepared statement
            pstmt.setString(1, "userBank");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                if (rs.getBinaryStream("SerialisedBankBlob") == null) { // If there isn't anything stored inside the user bank
                    addDefaults(c);
                    pstmt.close();
                    return;
                }

                ObjectInputStream inputStream = new ObjectInputStream(rs.getBinaryStream("SerialisedBankBlob"));    // Column to read

                // Deserialization of the object
                List<User> listData = (List) inputStream.readObject();

                User someUser = (listData.stream()
                        .filter(user -> AccountType.SysAdmin.equals((user).getAccountType()))
                        .findFirst()
                        .orElse(null));

                if (someUser == null) { // If the database has no admins, add a default one
                    addDefaults(c);
                    pstmt.close();
                    return;
                }

            }
            pstmt.close();
        }
        catch (Exception ex) {
            addDefaults(c);
        }
        System.out.println("Checked for default admins");
        System.out.println();
    }

    protected static void addDefaults(Connection c) {
        try { c.close(); } catch (Exception e) { /* Force close potentially problematic connection to open this new one */ }

        List emptyList = FXCollections.observableArrayList().stream().toList();
        saveBank("questionBank", emptyList);
        saveBank("testBank", emptyList);
        saveBank("resultBank", emptyList);
        saveBank("classBank", emptyList);


        ObservableList userBank = FXCollections.observableArrayList();
        userBank.add(new SysAdmin("Default", "SysAdmin", "Admin", "123"));
        saveBank("userBank", userBank.stream().toList());
    }
}
