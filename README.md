# OOP-Cw1

## Contents
1. [Description](#Description)
2. [Starting](#Starting)
3. [Usage](#Usage)
7. [Tests](#Tests)

## Description 
- Cw1 utilises CRUD (Create, Read, Update, Delete) functions for a persistent storage application regarding questions, tests & their results.

## Starting
- Note: This tutorial uses an IDE called IntelliJ IDEA.
- Download the ZIP of this repository's master branch.
- ![image](https://user-images.githubusercontent.com/47162481/119997860-157fe600-bfc8-11eb-96ba-f8b9ae6b228b.png)
- Download a JavaFX SDK. They can be found here: https://gluonhq.com/products/javafx/. I recommend getting version 16 because that is what I used within development.
- ![image](https://user-images.githubusercontent.com/47162481/119998175-6099f900-bfc8-11eb-8f38-af80ebf4fa7a.png)
- Extract both downloaded ZIP files.
- Open the extracted **OOP-Cw1-master** folder > Open the **Cw1** folder > Open the **src** folder > Open **Main.java** using IntelliJ IDEA.
- You should have opened OOP-Cw1-master\Cw1\src\Main.java in IntelliJ IDEA at this point.
- In IntelliJ, go to **File** > **Project Structure**.
- Go to the Libraries tab, press the add button and select Java.
- ![image](https://user-images.githubusercontent.com/47162481/120001773-0a2eb980-bfcc-11eb-9bb6-2e38f7624be0.png)
- Go inside the extracted JavaFX SDK folder, open the **javafx-sdk-16** folder & select the **lib** folder inside it.
- ![image](https://user-images.githubusercontent.com/47162481/120001989-43ffc000-bfcc-11eb-81c3-d9b9ae6585e2.png)
- A dialog window as shown will appear, press OK.
- ![image](https://user-images.githubusercontent.com/47162481/120002266-8cb77900-bfcc-11eb-9171-ee25a733d723.png)
- Now that you have added the JavaFX lib folder to the project structure, you can finalise the changes and exit the **Project Structure** window by pressing the **OK** button.
- On the top-right, press the **Add Configuration...** button.
- ![image](https://user-images.githubusercontent.com/47162481/120002753-094a5780-bfcd-11eb-8402-0a63b592fb2c.png)
- Then press **Add new run configuration...** and select **Application**
- ![image](https://user-images.githubusercontent.com/47162481/120003031-43b3f480-bfcd-11eb-9a1d-52048fa3263d.png)
- You can give the name field any name you want it to use, I recommend Cw1 because it is a fitting name.
- In the **Main class** field, type **Main**.
- Press **Modify options**, then press **Add VM options**
- ![image](https://user-images.githubusercontent.com/47162481/120003654-dce30b00-bfcd-11eb-80e6-4cffc5eb79f3.png)
- In the VM options field, paste this in `--module-path "\path\to\javafx-sdk-16\lib" --add-modules javafx.controls,javafx.fxml`. Change the path inside it to the one used for your extracted JavaFX lib folder.
- For example you may want to use `--module-path "C:\Users\Administrator\Downloads\openjfx-16_windows-x64_bin-sdk\javafx-sdk-16\lib" --add-modules javafx.controls,javafx.fxml` if you extracted it in your downloads folder.
- The final state of the window should look something like this:
- ![image](https://user-images.githubusercontent.com/47162481/120004779-15371900-bfcf-11eb-8fdf-c09976d78805.png)
- Press **OK** to finalise the configurations & exit the window.
- Press the green play button on the top-right to run it
- ![image](https://user-images.githubusercontent.com/47162481/120005134-765eec80-bfcf-11eb-9baa-39fe23daf983.png)
- It should now be running & showing this screen on top of the IDE
- ![image](https://user-images.githubusercontent.com/47162481/120005206-8c6cad00-bfcf-11eb-852d-e266fa52703a.png)

## Usage
When you have run the application, it will start with a login screen. For the purposes of Cw1, any account number (or just pressing enter) will let you through.
Once through, you will notice 4 tabs where each tab shows a page for a specific usage as listed below.
### Question Management
This page is used for creating, editing, cloning and deleting questions (in the question bank).
### Test Management
This page is used for creating, editing and deleting tests (in the test bank). When adding or editing a test, you can add and remove questions.
### View Test Results
This page is used for viewing or editing the results (from the result bank), they can be done through the **Open/Edit Selected Result** button. Results can be deleted through the **Remove Selected Result** button.
### Do a Test
This page is used for doing a test. Once a test is finished, it will generate a result & save it into the result bank so it can be viewed/edited or deleted in the 'View Test Results' tab.

## Tests
There are 4 JUnit test classes which contains 19 unit tests in total. All the unit tests are run against the classes stored inside the Quiz package. This includes the following classes: **Answer**, **Question**, **Result** & **Test**.
### How to run the JUnit tests?
- To run all at once: Right click the UnitTests.Quiz package and press **Run 'Tests in 'UnitTests.Quiz''**.
- ![image](https://user-images.githubusercontent.com/47162481/120009506-eec7ac80-bfd3-11eb-8b9a-b94e306335ec.png)
- The results should show in the **Run** tab
- ![image](https://user-images.githubusercontent.com/47162481/120009712-29314980-bfd4-11eb-88db-f42d65744092.png)
