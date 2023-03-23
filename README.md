# weddingapp

The wedding industry is a massive and growing market worldwide, but planning and managing a wedding can be stressful and time-consuming. This project aims to create a wedding app that helps to solve common issues related to wedding events.

The wedding planning app will be designed to make it easy for couples to plan and organize their wedding events. The app will provide a platform for couples to create a checklist of tasks that need to be done before, during, and after the wedding. The app will also offer a comprehensive directory of vendors such as florists, photographers, caterers, and other service providers that can be used for the wedding event.

In addition to the vendor directory, the wedding planning app will also offer a platform for couples to manage their guest lists, invitations, and RSVPs. The app will have features that allow couples to customize their wedding invitations, track responses, and send reminders to guests who haven't responded.

Furthermore, the app will provide a timeline and countdown to the wedding day, allowing couples to stay on track with their wedding planning. The app will also offer a budget calculator, which will help couples to stay within their budget, and manage all wedding expenses. The app will also provide a platform for guests to share photos and comments during and after the wedding, creating a virtual wedding album.

Overall, this wedding planning app will provide a one-stop solution for couples to plan, organize, and manage their wedding events, reducing stress and saving time.

The following requirements are needed to run this project

## Technologies
- **Programming Language**: Java
- **Framework**: Spring Boot
- **Database**: Postgres
- **Messaging**: Kafka
- **Testing**: JUnit, Mockito
- **Build**: Maven, Docker
- **CI/CD**: Docker, Jenkins, AWS
- **IDE**: Apache NetBeans 12.4

Getting Started
Prerequisites
You will need the following software installed on your local machine:

Docker
Docker Compose
Starting the Services
To start the microservices, navigate to the project directory and run the following command:

Copy code
docker-compose up
This will start the following services:

Discovery Service (Eureka) at http://localhost:8761
Gateway Service (API Gateway) at http://localhost:8008
User Service at http://localhost:8082
Wedding Microservice at http://localhost:8083
Grafana at http://localhost:3000
Prometheus at http://localhost:9090
Jenkins at http://localhost:8080
PgAdmin at http://localhost:5050
Mailhog at http://localhost:8025
You can access each service by navigating to its corresponding URL.

Stopping the Services
To stop the services, press Ctrl+C in the terminal window where the docker-compose up command was executed.

Configuration
By default, the following ports are used:

Discovery Service (Eureka) - 8761
Gateway Service (API Gateway) - 8008
User Service - 8082
Wedding Microservice - 8083
Grafana - 3000
Prometheus - 9090
Jenkins - 8080
PgAdmin - 5050
Mailhog - 8025
You can modify the ports used by editing the docker-compose.yml file.

Built With
Docker
Postgres
Java
Spring Boot
Eureka
API Gateway
User Service
Wedding Microservice
Grafana
Prometheus
Jenkins
PgAdmin
Mailhog
Authors
David Tega
License
This project is licensed under the MIT License - see the LICENSE.md file for details.