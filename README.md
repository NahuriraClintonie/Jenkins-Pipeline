
# pahappa-web-template
Template Project For Web using Spring 3 with JSF and primefaces 7

# Jenkins-Pipeline
A project demonstrating a Jenkins pipeline project.

### Project Structure
This project is a maven project with the following structure:
```
pahappa-web-template [parent]
├── pom.xml [parent pom]
├── README.md
├── Backend [module]
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── org.pahappa.systems
│   │   │   │       ├──core -> Contains base classes for the project using the Pahappa Framework
│   │   │   │       ├──utils -> A collection of utility classes
│   │   │   │       ├──models // Define your project models here
│   │   │   │       ├──services // Define your project services here
├── Frontend [module]
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── org.pahappa.systems
│   │   │   │       ├──api -> Contains the REST API for the project, implement your API here
│   │   │   │       │     ├──restresources -> Contains the REST resources for the project, implement your REST resources here e.g UserResource
│   │   │   │       │     ├──dto -> Contains the DTOs for the project, implement your DTOs here e.g UserDTO
│   │   │   │       ├──web -> Contains the web interface for the project, implement your web interface here
│   │   │   │       │     ├──controllers -> Contain base controllers for the project
│   │   │   │       │     ├──core -> Contains base files  e.g Converters, Base Dialogs, and component render
│   │   │   │       │     ├──views -> Contains the views for the project (Managed Beans)
│   │   │   │       │   │   ├──dashboard -> Contains the views for the dashboard
│   │   │   │       │   │   ├──users -> Contains the base views user management (users, roles, permissions)
│   │   │   │       │   │   ├──app //add your project views here e.g. (products, orders, etc)
│   │   │   ├── resources -> contains project configuration files e.g applicationContext.xml, Hibernate.cfg.xml, etc 
│   │   │   ├── webapp -> contains the web resources. This is the web application for the project
│   │   │   │       ├──ExternalViews -> Contains pages we want accessible to the public e.g. login, register, reset password, landing etc //You can configure your landing page in the web.xml
│   │   │   │       ├──WEB-INF -> Contains the web configuration files e.g web.xml, faces-config.xml, etc
│   │   │   │       ├──resources -> Contains the web resources e.g css, js, images, etc
│   │   │   │       ├──pages -> Contains the web pages for the application // Most of the pages contained in here are restricted to authenticated users
│   │   │   │       │   ├──dashboard -> Contains the pages for the dashboard
│   │   │   │       │   ├──users -> Contains the pages for user management (users, roles, permissions)
│   │   │   │       │   ├──app //add your project pages here e.g. (products, orders, etc)
│   │   │   │       │   ├──californiatemplate -> Contains the pages for the californiatemplate
│   │   │   │       │   │   ├──template.xhtml -> The template for the application// Use this template in your pages. Add your custom css and js here
│   │   │   │       │   │   ├──sidebar.xhtml -> The sidebar for the application // Add your custom menu items here
│   │   │   │       │   │   ├──other template files ....


```

### How to use
1. Clone the project
2. Import the project into your IDE
3. Modify the paren pom.xml to suite your project
```xml 

    <groupId>org.pahappa.systems</groupId>
    <artifactId>pahappa-web-template</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>pom</packaging>
    <name>pahappa-web-template</name>
    <modules>
        <module>Backend</module>
        <module>Frontend</module>
    </modules>

    <properties>
        <parent.name>pahappa-web-template</parent.name>
<!--   ...other properties....-->
   </properties>
```

Modify the `name` and `parent.name` properties to suite your project

They will use the `parent.name` property to set the submodule name e.g. if `parent.name` is 'myproject' then the submodule will be `myproject-Backend` and `myproject-Frontend`


4. Configure your database connection in `Frontend/src/main/resources/applicationContext-server.xml`

```xml

<!--	Datasource configuration-->

	<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="com.mysql.jdbc.Driver" />
		<property name="url" value="jdbc:mysql://localhost/db_app_name?createDatabaseIfNotExist=true" />
		<property name="username" value="pahappa" />
		<property name="password" value="pass@2020@Pahappa" />
	</bean>

```
Modify the `url`, `username` and `password` properties to suite your database connection


5. Configure your tomcat server in your IDE. You should be able to run this application with no further configuration

### Customization
1. The application comes with the default California template.
2. Change app name in the `Frontend/src/main/java/org/pahappa/systems/web/controllers/GenericController.java` file
```java
    private final String APPLICATION_NAME = "APP NAME";
```


Note: This project is still under development. Feel free to contribute to the project

### Requirements
1. Java 8
2. Maven
3. Tomcat 8.5+
4. MySQL 5.7+
5. IDE (Intellij IDEA, Netbeans, Eclipse)
6. Git
7. Lombok plugin for your IDE—https://projectlombok.org/setup/overview: This is required for the project to compile, build and run successfully. Lombok enables us to use `@Getters` and `@Setters` annotations on our classes without having to write the getters and setters ourselves.
8. Primefaces 7.0
9. Spring 3.0
10. JSF 2.0
11. Hibernate 3.6
12. California Template v3.0.0


### Pahappa Web-Utils
```xml

<dependency>
    <groupId>org.pahappa.systems</groupId>
    <artifactId>pahappa-web-utils</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

```


### Getting Help
To report a specific problem or feature request, open a new issue on Github.
For questions, suggestions, or anything else, email me at `Conrad@pahappa.com`

### Author
Name: Nahurira Clintonie
<br>
Email: clinton@pahappa.com
