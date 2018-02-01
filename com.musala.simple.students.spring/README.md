# musala-training
Small training project in Java


This is a very simple java web application for adding, storing and retrieving students from a database . 

The program supports both MySql and MongoDb databases. The app uses Spring boot to configure the REST endpoints and carry out 
the communication between front-end and the database. The front-end is currently using just a static HTML 
for testing purposes. In the short term it will be substituted by Angular2 UI.

Prerequisites:
In order to run the project locally both MySql and MongoDb need to be setup first.

MongoDb setup.
Download and install MongoDb. This should start the MongoDb database process on port 27017.

MongoDb download: https://www.mongodb.com/download-center#community
MongoDb setup: https://docs.mongodb.com/manual/tutorial/install-mongodb-on-windows/

MySql Setup
1.	Download and install XAMPP as per your Operating System.
	https://www.apachefriends.org/download.html
2.	Start the XAMPP control panel.
3.	Click on the Start button next to Apache, and wait for apache to start. After Apache has started, click on the Start button next to MySQL. Wait for MySQL to start. Both Apache and MySQL should be running now.
Make sure MySql is running on port 3306 since the program is configured to run the MySql connection on that port.
