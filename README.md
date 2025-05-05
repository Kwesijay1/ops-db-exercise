# Spring Boot Inventory Database API

##  Project Overview

This Spring Boot RESTful API connects to a MySQL database hosted on **Amazon RDS** to provide business intelligence insights such as:

- Top customers
- Monthly sales analytics
- Average order value by country
- Products never ordered
- Frequent buyers

The application is deployed on an **AWS EC2** instance for reliable and scalable cloud accessibility.

---

##  Tech Stack

| Component   | Technology                |
|------------|---------------------------|
| Backend     | Java 17+, Spring Boot 3.x |
| Database    | Amazon RDS (MySQL 8.0)    |
| Hosting     | AWS EC2 (Ubuntu 22.04 LTS)|
| Build Tool  | Maven 3.8+                |
| Data Access | JDBC Template             |

---

##  Security Implementation

### Database Credential Management

- Credentials are stored securely using environment variables in `~/.bashrc`
- `application.properties` uses `${DB_USERNAME}` and `${DB_PASSWORD}` placeholders
- Environment variables are loaded at runtime to protect sensitive information
- Follows AWS security best practices

---

##  Deployment Guide

### 1. Build the Application

```bash
mvn clean package
```

### 2. Transfer JAR to EC2

```bash
scp target/<your-jar-file>.jar ubuntu@[YOUR-EC2-IP]:/home/ubuntu/
```

### 3. Configure Environment Variables

```bash
echo 'export DB_USERNAME=your_db_user' >> ~/.bashrc
echo 'export DB_PASSWORD=your_db_pass' >> ~/.bashrc
echo 'export DB_URL=jdbc:mysql://your-rds-endpoint:3306/your_db_name' >> ~/.bashrc
source ~/.bashrc
```

### 4. Run the App as a Systemd Service

Create the service file:

```bash
sudo nano /etc/systemd/system/awsrds.service
```

Paste the following content:

```ini
[Unit]
Description=AWS RDS Java App
After=network.target

[Service]
User=ubuntu
WorkingDirectory=/home/ubuntu
ExecStart=/usr/bin/java -jar /home/ubuntu/<your-jar-file>.jar
SuccessExitStatus=143
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
```

Then run:

```bash
sudo systemctl daemon-reexec
sudo systemctl daemon-reload
sudo systemctl enable awsrds
sudo systemctl start awsrds
```

##  API Documentation
### Base URL

```
http://[YOUR-EC2-IP]/index.html
```

### Endpoints

| Endpoint                        | Method | Description                          |
|---------------------------------|--------|--------------------------------------|
| `/api/top_customers`           | GET    | Returns top 10 customers by revenue  |
| `/api/monthly_sales`           | GET    | Monthly sales figures for the year   |
| `/api/avg_order_by_country`    | GET    | Average order value by country       |
| `/api/products_never_ordered`  | GET    | Products never ordered               |
| `/api/frequent_buyers`         | GET    | Frequent buyers by country           |

---

##  Project Structure

```
src/
├── main/
│   ├── java/com/example/awsrds/
│   │   ├── AwsrdsApplication.java
│   │   ├── controller/
│   │   │   └── DashboardController.java
│   │   ├── model/
│   │   └── repository/
│   └── resources/
│       ├── application.properties
│       └── schema.sql
├── test/
└── pom.xml
```

---

##  Testing

### API Testing (using curl)

```bash
curl http://[YOUR-EC2-IP]:8080/api/top_customers
curl http://[YOUR-EC2-IP]:8080/api/monthly_sales
curl http://[YOUR-EC2-IP]:8080/api/avg_order_by_country
curl http://[YOUR-EC2-IP]:8080/api/products_never_ordered
curl http://[YOUR-EC2-IP]:8080/api/frequent_buyers
```

### Unit Testing

```bash
mvn test
```

```bash
tail -f app.log
```

### Restart Service

```bash
# Find the process ID
ps aux | grep < .jar file>


# Kill the process
kill [PROCESS_ID]

```
### Resources
```bash
 [AWS RDS Documentation](https://docs.aws.amazon.com/rds/index.html)
 [Spring Boot Documentation](https://spring.io/projects/spring-boot)
 [MySQL Documentation](https://dev.mysql.com/doc/)
 [AWS EC2 Documentation](https://docs.aws.amazon.com/ec2/index.html)
```
ps aux | grep <your-jar-file>
kill <PROCESS_ID>
```
© 2025 Your Company - All Rights Reserved
