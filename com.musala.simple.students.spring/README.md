# musala-training
Small training project in Java


This is a very simple java program for adding, storing and retrieving students from a database. 

The program takes as input a windows path to a .json file. The file should contain a valid json string in format: { "students": [ { studentObject1 }, { studentObject2 } …  ] }. The user is then prompted to choose a database for the program to work with. The user is provided with three option to choose from – MySql Database, MongoDB Database and both. As a result of that choice, the information during the user’s interaction with the application will be saved to/retrieved from the database chosen. In addition to the .json path the user has the option to provide a student id as a second argument to the command line. If the user provides such an input and it’s a valid id (a positive integer value) the program prints detailed information about that student if such students is found in the json file or the database currently used.
The program reads the information from the json file and saves it in the database chosen. The program supports both MySql and MongoDb databases.

Prerequisites:
In order to run the project locally both MySql and MongoDb need to be setup first.

MongoDb setup.
Download and install MongoDb. This should start the MongoDb database process on port 27017.

MySql Setup
1.	Download and install XAMPP as per your Operating System.
2.	Start the XAMPP control panel.
3.	Click on the Start button next to Apache, and wait for apache to start. After Apache has started, click on the Start button next to MySQL. Wait for MySQL to start. Both Apache and MySQL should be running now.
Make sure MySql is running on port 3306 since the program is configured to run the MySql connection on that port.
