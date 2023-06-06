
<a name="readme-top"></a>

<h1>Plantae 🍃</h1> 

<p align="center">
  <img src="https://img.shields.io/static/v1?label=Spring&message=framework&color=blue&style=for-the-badge&logo=Spring"/>
  <img src="https://img.shields.io/static/v1?label=Heroku&message=deploy&color=blue&style=for-the-badge&logo=Heroku"/>
  <img src="https://img.shields.io/static/v1?label=MongoDB&message=database&color=blue&style=for-the-badge&logo=mongodb"/>
  <img src="http://img.shields.io/static/v1?label=Java&message=17&color=red&style=for-the-badge&logo=openjdk"/>
  <img src="http://img.shields.io/static/v1?label=Vue&message=2.7.1&color=blue&style=for-the-badge&logo=v"/>
  <img src="http://img.shields.io/static/v1?label=axios&message=1.3.4&color=blue&style=for-the-badge&logo=axios"/>
  <img src="http://img.shields.io/static/v1?label=Thymeleaf&message=3.0.15&color=green&style=for-the-badge&logo=thymeleaf"/>
  <img src="http://img.shields.io/static/v1?label=Bootstrap&message=4&color=purple&style=for-the-badge&logo=bootstrap"/>
  <img src="http://img.shields.io/static/v1?label=STATUS&message=CONCLUIDO&color=GREEN&style=for-the-badge"/>
  <img src="http://img.shields.io/static/v1?label=License&message=MIT&color=green&style=for-the-badge"/>


### Tópicos 

:small_blue_diamond: [About the Project](#about-the-project)

:small_blue_diamond: [Technology Stack](#technology-stack)

:small_blue_diamond: [Features](#features)

:small_blue_diamond: [Running the Project](#running-the-project)

:small_blue_diamond: [Author](#author)

| :placard: Vitrine.Dev |     |
| -------------  | --- |
| :sparkles: Nome        | **Plantae**
| :label: Tecnologias | Spring, VueJS, Bootstrap
| :rocket: URL         |  github.com/thomazcm/plantae/


# About the Project

This project is a robust web application designed to streamline event ticket management and distribution, using Maven, Spring Boot, Vue.js, Bootstrap, and MongoDB. 

The core functionality of the web application revolves around generating PDF files for event tickets, managing the list of sold tickets, sending the tickets directly to the customer's email, and ticket validation via QR Code scanning on the event day. 
## Technology Stack

This application is built using a variety of technologies, each chosen for its strengths and compatibility with the others.

### Backend

- **Maven**: Used for managing the project's build, reporting, and documentation from a central piece of information.

- **Spring Boot**: Simplifies Java development and accelerates integration. It includes a number of additional starters for building web applications, working with MongoDB databases, sending emails, and adding security configurations. It also includes Thymeleaf for server-side Java template engine for web applications.

- **QRGen and iText**: These libraries are used for QR Code generation and PDF creation, respectively, essential for the ticketing functionality.

### Frontend

- **Vue.js**: Used for building user interfaces. The Vue.js scripts are imported from the CDN, allowing for easier updates and performance improvements.

- **Bootstrap**: Used for designing and customizing responsive mobile-first sites. The Bootstrap scripts are also imported from the CDN.

- **Axios**: This library is used for making HTTP requests from the browser. It's promise-based, and it works both in the browser and in a node.js environment.

- **jQuery**: A fast, small, and feature-rich JavaScript library used for things like HTML document traversal and manipulation, event handling, and animation.

- **FontAwesome**: This toolkit provides vector icons and social logos, giving you a scalable vector icons that can be customized in terms of size, color, drop shadow, etc.

- **Google Fonts**: A library of libre licensed fonts.

### Database

- **MongoDB**: The primary database management system. MongoDB is a source-available cross-platform document-oriented database program, classified as a NoSQL database program, using JSON-like documents with optional schemas.

This combination of technologies allows the application to handle complex tasks with ease, while remaining scalable and maintainable.

## Functionality

The core functionalities of this web application include:

- **PDF Ticket Generation**: This application generates tickets for an event in PDF format. These tickets can be easily printed or stored on a mobile device.

- **Ticket Sales Management**: This application manages a list of sold tickets, keeping track of sales and remaining inventory.

- **Email Ticket Distribution**: Once a ticket is sold, the application sends the ticket directly to the customer's email, ensuring a smooth delivery process.

- **QR Code Ticket Validation**: On the event day, tickets can be validated quickly and efficiently by scanning a QR Code, streamlining entry and reducing the chance of fraudulent tickets.

These features work together to make this application a comprehensive solution for event ticket generation, distribution, and validation.

<p align="right">(<a href="#readme-top">back to the top</a>)</p>

## Setting Up and Running the Project Locally

To clone and run this project locally, you'll need to have [Git](https://git-scm.com), [Java](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html), and [Maven](https://maven.apache.org/) installed on your computer. From your command line:

```bash
# Clone this repository
$ git clone https://github.com/thomazcm/plantae

# Go into the repository
$ cd plantae

# Install dependencies
$ mvn install

# Run the app
$ mvn spring-boot:run

Open http://localhost:8080 to view it in the browser. The page will reload if you make edits. You will also see any lint errors in the console.

<p align="right">(<a href="#readme-top">back to the top</a>)</p>
```


## License

This project is under the [MIT](./LICENSE) License. Refer to the LICENSE.md file for more details.

<p align="right">(<a href="#readme-top">back to the top</a>)</p>

## Author
<b>Thomaz Machado</b>🚀<br />
 <img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/u/71472870?s=460&u=61b426b901b8fe02e12019b1fdb67bf0072d4f00&v=4" width="100px;" alt=""/><br />
Project developed by Thomaz Machado. Get in touch!!  

[![Linkedin Badge](https://img.shields.io/badge/-Thomaz-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/thomazcm)](https://www.linkedin.com/in/thomazcm) 
[![Gmail Badge](https://img.shields.io/badge/-thomazcm@gmail.com-c14438?style=flat-square&logo=Gmail&logoColor=white&link=mailto:thomazcm@gmail.com)](mailto:thomazcm@gmail.com)
 
 <p align="right">(<a href="#readme-top">back to the top
</a>)</p>
