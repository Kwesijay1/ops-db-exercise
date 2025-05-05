# Spring Boot Inventory Database API

##  Project Overview
This Spring Boot RESTful API connects to a MySQL database hosted on Amazon RDS to provide business intelligence insights including top customers, monthly sales analytics, average order value by country, Products never Ordered, and frequent buyers. The application is deployed on an AWS EC2 instance for reliable cloud accessibility.

##  Tech Stack

| Component | Technology |
|-----------|------------|
| Backend | Java 17+, Spring Boot 3.x |
| Database | Amazon RDS (MySQL 8.0) |
| Hosting | AWS EC2 (Ubuntu 22.04 LTS) |
| Build Tool | Maven 3.8+ |
| Data Access | JDBC Template (for optimized SQL queries) |

##  Security Implementation

### Database Credential Management
- Credentials are securely stored using environment variables defined in `~/.bashrc`
- The `application.properties` file uses placeholder notation (`${DB_USERNAME}`, `${DB_PASSWORD}`)
- Environment variables are loaded at runtime to ensure sensitive information is never committed to the codebase
- Follows AWS security best practices for credential management

##  Deployment Guide

### 1. Build the Application
```bash
mvn clean package
```
This generates the deployable artifact: `target/<.jar file>`

### 2. Transfer to EC2 Instance
```bash
scp target/<.jar file> ubuntu@[YOUR-EC2-IP]:/home/ubuntu/
```

### 3. Configure Environment Variables
```bash
echo 'export DB_USERNAME=[your_database_username]' >> ~/.bashrc
echo 'export DB_PASSWORD=[your_database_password]' >> ~/.bashrc
echo 'export DB_URL=jdbc:mysql://[your-rds-endpoint]:3306/[your_database]' >> ~/.bashrc
source ~/.bashrc
```

### 4. Run the Application in the Background
```bash
sudo nano /etc/systemd/system/awsrds.service


[Unit]
Description=AWS RDS Java App
After=network.target

[Service]
User=ubuntu
WorkingDirectory=/home/ubuntu
ExecStart=/usr/bin/java -jar /home/ubuntu/<.jar file>
SuccessExitStatus=143
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target


sudo systemctl daemon-reexec
sudo systemctl daemon-reload
sudo systemctl enable awsrds
sudo systemctl start awsrds
```

### 5. Verify Deployment
```bash
curl http://[YOUR-EC2-IP]:8080/api
```

##  API Documentation

### Base URL
```
http://[YOUR-EC2-IP]:8080/api
```

### Available Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/top_customers` | GET | Returns top 10 customers by revenue 
| `/monthly_sales` | GET | Monthly sales figures for current year
| `/avg_order_by_country` | GET | Average order value by country
| `/products_never_ordered` | GET | Products never Ordered
| `/frequent_buyers` | GET | Frequent buyers by country

##  Project Structure
```
src/
├── main/
│   ├── java/com/example/awsrds/
│   │   ├── AwsrdsApplication.java        # Main application entry point
│   │   ├── controller/                   # REST controllers
│   │   │   └── DashboardController.java  # Primary API endpoints
│   │   ├── model/                        # Data models/entities
│   │   └── repository/                   # Data access components
│   └── resources/
│       ├── application.properties        # Configuration properties
│       └── schema.sql                    # Database schema (if needed)
├── test/                                 # Unit and integration tests
└── pom.xml                               # Maven dependencies
```

##  Testing

### API Testing
Use Postman, curl, or any HTTP client:

```bash
# Get top customers
curl http://[YOUR-EC2-IP]:8080/api/top_customers

# Get monthly sales data
curl http://[YOUR-EC2-IP]:8080/api/monthly_sales

# Get average order by country
curl http://[YOUR-EC2-IP]:8080/api/avg_order_by_country

# Get products_never_ordered by country
curl http://[YOUR-EC2-IP]:8080/api/products_never_ordered

# Get frequent buyers by country
curl http://[YOUR-EC2-IP]:8080/api/frequent buyers
```

### Running Unit Tests
```bash
mvn test
```

## 📈 Monitoring & Maintenance

### Viewing Logs
```bash
tail -f app.log
```

### Restarting the Service
```bash
# Find the process ID
ps aux | grep < .jar file>

# Kill the process
kill [PROCESS_ID]

```

© 2025 Your Company - All Rights Reserved
