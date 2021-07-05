# OOP-Cw2

## Contents
1. [Description](#Description)
2. [Starting](#Starting)
3. [Usage](#Usage)
7. [Tests](#Tests)

## Description 
- Cw2 utilises CRUD (Create, Read, Update, Delete) functions for a persistent storage application regarding questions, tests, results, classes, & users.

## Starting
- Note: This tutorial uses an IDE called IntelliJ IDEA.
- Download the ZIP of this repository's master branch.
- ![image](https://user-images.githubusercontent.com/47162481/124509270-8f766c80-ddc9-11eb-9610-c96214dc26e5.png)
- Download a JavaFX SDK. They can be found here: https://gluonhq.com/products/javafx/. I recommend getting version 16 because that is what I used within development.
- ![image](https://user-images.githubusercontent.com/47162481/119998175-6099f900-bfc8-11eb-8f38-af80ebf4fa7a.png)
- Extract both downloaded ZIP files.
- Open the extracted **OOP-Cw2-main** folder > Open the **Cw2** folder > Open the **src** folder > Open **Main.java** using IntelliJ IDEA.
- You should have opened OOP-Cw2-main\Cw2\src\Main.java in IntelliJ IDEA at this point.
- In IntelliJ, go to **File** > **Project Structure**.
- Go to the Libraries tab, press the add button and select Java.
- ![idea64_4Hp1u66TNj](https://user-images.githubusercontent.com/47162481/124509464-eda34f80-ddc9-11eb-9b8c-6b117358ddde.png)
- Go inside the extracted JavaFX SDK folder, open the **javafx-sdk-16** folder & select the **lib** folder inside it.
- ![image](https://user-images.githubusercontent.com/47162481/120001989-43ffc000-bfcc-11eb-81c3-d9b9ae6585e2.png)
- A dialog window as shown will appear, press OK.
- ![image](https://user-images.githubusercontent.com/47162481/124509524-090e5a80-ddca-11eb-93cc-ba007b0b4ec3.png)
- Now that you have added the JavaFX lib folder to the project structure, you can finalise the changes and exit the **Project Structure** window by pressing the **OK** button.
- On the top-right, press the **Add Configuration...** button.
- ![image](https://user-images.githubusercontent.com/47162481/120002753-094a5780-bfcd-11eb-8402-0a63b592fb2c.png)
- Then press **Add new run configuration...** and select **Application**
- ![image](https://user-images.githubusercontent.com/47162481/120003031-43b3f480-bfcd-11eb-9a1d-52048fa3263d.png)
- You can give the name field any name you want it to use, I recommend Cw2 because it is a fitting name.
- In the **Main class** field, type **Main**.
- Press **Modify options**, then press **Add VM options**
- ![image](https://user-images.githubusercontent.com/47162481/120003654-dce30b00-bfcd-11eb-80e6-4cffc5eb79f3.png)
- In the VM options field, paste this in `--module-path "\path\to\javafx-sdk-16\lib" --add-modules javafx.controls,javafx.fxml`. Change the path inside it to the one used for your extracted JavaFX lib folder.
- For example you may want to use `--module-path "C:\Users\Administrator\Downloads\openjfx-16_windows-x64_bin-sdk\javafx-sdk-16\lib" --add-modules javafx.controls,javafx.fxml` if you extracted it in your downloads folder.
- The final state of the window should look something like this:
- ![image](https://user-images.githubusercontent.com/47162481/124509773-64404d00-ddca-11eb-83f6-212a961dfefc.png)
- Press **OK** to finalise the configurations & exit the window.
- Press the green hammer button on the top-right to build it.
- ![image](https://user-images.githubusercontent.com/47162481/124509846-8639cf80-ddca-11eb-909e-467c82eecafd.png)
- Press the green play button on the top-right to run it.
- ![idea64_0u2wPNFVZC](https://user-images.githubusercontent.com/47162481/124509961-c39e5d00-ddca-11eb-97eb-706fd8615fe6.png)
- It should now be running & showing this screen on top of the IDE.
- ![image](https://user-images.githubusercontent.com/47162481/124509985-d1ec7900-ddca-11eb-9f56-1890e335e6e8.png)

## Usage
When you have run the application, it will start with a login screen. One SysAdmin is created by default, the username is **Admin**, and password is **123**. It will let you through to where you can access the User Management tab. From there, you can make **SysAdmin**, **Teacher** and **Student** accounts. You can then log out and log back in as one the created accounts to test the functionality they have access to.  

### Question Management
- Teachers have access.  
This page is used for creating, editing, cloning and deleting questions (in the question bank).
### Test Management
- Teachers have access.  
This page is used for creating, editing and deleting tests (in the test bank). When adding or editing a test, you can add and remove questions.
### Class Management
- Teachers have access.  
This page is used for creating, editing and deleting classes (in the class bank). When adding or editing a class, you can add and remove users.
### View Test Results
- Students have (limited) access.  
- Teachers have access.  
This page is used for viewing or editing the results (from the result bank), they can be done through the **Open/Edit Selected Result** button. Results can be deleted through the **Remove Selected Result** button.
### Do a Test
- Students have access.   
This page is used for doing a test. Once a test is finished, it will generate a result & save it into the result bank so it can be viewed/edited or deleted in the 'View Test Results' tab.
### User Management
- Only SysAdmins have access.  
This page is used for creating, editing and deleting users(in the user bank). This includes users of all types which is why it is only accessible to SysAdmins.


## Tests
There are 9 JUnit test classes which contains 43 unit tests in total. All the unit tests are run against the classes stored inside the Quiz & 'Account packages. This includes the following classes from the Quiz package: **Question**, **Test**, **Answer**, **Result** & **Class**. Whereas from the Account package it includes testing the following classes: **User**, **Student**, **Teacher** & **SysAdmin**. 
### How to run the JUnit tests?
- To run all at once: Right click the UnitTests package and press **Run 'Tests in 'UnitTests''**.
- ![image](https://user-images.githubusercontent.com/47162481/124507975-dc0c7880-ddc6-11eb-9dda-26f66463e821.png)
- To run unit tests against a specific class: Right click the class you want unit tests ran against and press the **Run 'nameOfClass'**.
- ![image](https://user-images.githubusercontent.com/47162481/124508390-be8bde80-ddc7-11eb-9704-56ea6f7a4db4.png)
- All results from running unit tests should show up in the **Run** tab, letting you know if they passed or not.
- ![image](https://user-images.githubusercontent.com/47162481/124507617-0c074c00-ddc6-11eb-9637-861946158c77.png)
