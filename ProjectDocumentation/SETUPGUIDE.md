> Starting as a Java beginner with no prior Spring Boot or MongoDB experience is very common, and this project is an excellent way to learn modern backend development. Here’s a practical, beginner-friendly overview to help get started:

## What Is Spring Boot + MongoDB REST API?
- **Spring Boot** is a Java-based framework that simplifies building web applications and APIs by providing out-of-the-box tools for common tasks, making Java web development much easier and less code-heavy.

- **MongoDB** is a popular, easy-to-use NoSQL database that stores data in flexible, JSON-like documents.

- **REST API** means your application can let other programs create, read, update, and delete data over the web using standardized HTTP “endpoints,” typically accessed via URLs.

- Together, these technologies let you build apps that save data, return it as JSON, and allow clients (websites, apps, auditors) to interact with your data securely.

## Core Building Blocks—Easy Definitions
- Project Structure: Your project is a folder containing source files and a “build” descriptor (Maven or Gradle) that lists needed libraries.

- Model: Java classes (User, Product) that represent data to store in MongoDB.

- Repository: Java interface that handles database operations, using pre-built methods (no SQL required).

- Controller: Java class where API endpoints are defined (URLs and HTTP handling).

- Security Configuration: Java setup for user login, protection, and roles.

- Application Properties: Simple config text file telling Spring Boot how to connect to MongoDB.

## How to Start—Beginner Steps
1. Go to Spring Initializer
- Fill in basic fields (Project type: Maven, Language: Java, Spring Boot version). select Spring Boot version 3.5.5. This is the latest stable (non-SNAPSHOT, non-Milestone) release and is fully supported by most guides, libraries, and community documentation.

- Add dependencies: Spring Web, Spring Data MongoDB, Spring Security.

- Click “Generate,” then unzip the downloaded folder.

2. Install Java and IDE

- Get Java JDK and an editor like IntelliJ IDEA or Eclipse.

- Open your new project folder in your IDE.

3. Install & Run MongoDB

- Download MongoDB Community Server and run it with default settings.

- MongoDB stores data locally in a single folder, no configuration needed for a beginner.
> This is the ideal setup! The first terminal is running the MongoDB server (mongod), which keeps your database active and accepting connections. The second terminal is running the MongoDB shell (mongosh), allowing you to interact directly with the database: create, view, update, and delete records.
````
(first terminal to simply start the MongoDB server)

mongod --dbpath ~/data/db 

This command starts the main database service and must remain open for your database to work.
````

````
(second terminal) 

mongosh 

This is your "database console," letting you run MongoDB commands to practice, debug, and see data directly.
````

> What You Can Do Now
Experiment in mongosh:
````
db
// test (return the name of the current database)

show dbs
// admin   40.00 KiB (lists all available databases in your MongoDB server)
// config  60.00 KiB
// local   40.00 KiB

use test
// already on db test

db.users.insertOne({ name: "Demo User" })
// {
//  acknowledged: true,
//  insertedId: ObjectId('68befd944cdae4983204199c')
// }

db.users.find()
// [ { _id: ObjectId('68befd944cdae4983204199c'), name: 'Demo User' } ]
````
> Leave the mongod window ALONE (don’t close it while working—otherwise your database shuts down).

4. Run Your Spring Boot App

- Build and Run From Command Line
      Open Terminal.

Change into your project directory (where your pom.xml is located):

````
cd ~/LetsPlay-Java
````
Run the application using Maven:
````
mvn spring-boot:run
````
Wait for the terminal to display 
````
Tomcat started on port 8080—your app is now live at http://localhost:8080.
````
- (Optional) Run From the IDE
If “Run” is available in your IDE:

Open the file DemoApplication.java.

If visible, click the green “run” icon or right-click the file and choose Run 'DemoApplication.main()'.

If not available, use the command-line method above.

## Summary Table
| Service	     | Purpose	  | Default Port | Where to set |
|--------------|-----------|-------| -------------|
| MongoDB      | Database	 | 27017	| application.properties|
| Spring Boot  | 	REST API web server | 	8080 |	application.properties |

---

# Reference

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.5/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.5/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.5/reference/web/servlet.html)
* [Spring Security](https://docs.spring.io/spring-boot/3.5.5/reference/web/spring-security.html)
* [Spring Data MongoDB](https://docs.spring.io/spring-boot/3.5.5/reference/data/nosql.html#data.nosql.mongodb)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

---

