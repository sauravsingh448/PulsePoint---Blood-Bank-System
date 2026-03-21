PulsePoint - Blood Bank Management System
📌 Project Overview

PulsePoint is a Blood Bank Management System designed to simplify the process of blood donation, request handling, and inventory management.
This system allows donors, recipients, and admins to interact efficiently in a secure and scalable environment.

The project is built using Spring Boot (Backend) and follows modern development practices like JWT Authentication, OTP Verification, and Role-Based Access Control.

🚀 Features
👤 User Features
User Registration & Login
Login using Email / Phone + OTP
Forgot Password (Reset via Email Link)
View Blood Availability
Request Blood
Update Profile

🏥 Admin Features
Manage Blood Banks
Create & Manage Donation Camps
Manage Blood Inventory
Approve / Reject Requests
Role-based access control

🔐 Security Features
JWT Authentication
Role-Based Authorization (ADMIN, DONOR, RECIPIENT)
OTP Verification (Email / Phone)
Secure Password Reset via Token

🛠️ Tech Stack
Backend:
Java
Spring Boot
Spring Security
Spring Data JPA
Hibernate

Database:
MySQL
Tools & APIs:
Postman (API Testing)
SMTP (Email Service)

JWT (Authentication)
📂 Project Structure
bloodbank-project
│── controller
│── service
│   ├── interface
│   └── implementation
│── repository
│── entity
│── security
│── config

⚙️ Installation & Setup
1️⃣ Clone the Repository
git clone https://github.com/your-username/bloodbank-project.git
cd bloodbank-project
2️⃣ Configure Database (MySQL)

Update application.yml:

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bloodbankdb
    username: root
    password: your_password
3️⃣ Configure Email (SMTP)
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your_email@gmail.com
    password: your_app_password
    
4️⃣ Run Application
mvn spring-boot:run

Server will start on:

http://localhost:8080
🔑 API Endpoints (Sample)
🔹 Authentication
Register
POST /auth/register
Login
POST /auth/login
Send OTP
POST /auth/send-otp
Verify OTP
POST /auth/verify-otp
Forgot Password
POST /auth/forgot-password
Reset Password
POST /auth/reset-password

🔄 Authentication Flow
User Register/Login
        ↓
JWT Token Generated
        ↓
Token used for API Requests
        ↓
Secure Access to Resources

📸 Future Enhancements:
Payment Integration for Donations
Real-time Blood Availability Tracking
Mobile App Integration
SMS OTP Integration (Production)
Dashboard Analytics
This project is designed with future scalability and AI integration in mind

🎯 Purpose of Project: 
This project is developed as a placement-ready backend project to demonstrate:
Real-world problem solving
Secure authentication system
Scalable backend architecture

👨‍💻 Author
Saurav Kumar Singh
📧 Email: sauravsinghk842@gmail.com
🔗 GitHub: [https://github.com/your-username](https://github.com/sauravsingh448/PulsePoint---Blood-Bank-System)

⭐ Support

If you like this project:

⭐ Star the repository
🍴 Fork it
🛠️ Contribute

📢 Final Note

This project is a complete production-level backend system with modern authentication and security features.
Perfect for showcasing in interviews and placements 🚀
