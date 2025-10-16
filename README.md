<<<<<<< HEAD
# 🏦 MyBankDB - Intelligent Transaction Management System

**MyBankDB** is a robust Java application for managing and analyzing banking transactions. It allows you to manage clients, accounts, and transactions while automatically detecting financial anomalies.

---

## 📋 Table of Contents
- [About](#-about)
- [Key Features](#-key-features)
- [Architecture](#-architecture)
- [Technologies Used](#-technologies-used)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Usage](#-usage)
- [Project Structure](#-project-structure)
- [License](#-license)

---

## 🎯 About
MyBankDB centralizes and organizes banking information to:  
- Automatically detect suspicious transactions  
- Identify inactive accounts or unusual behavior  
- Generate detailed analytical reports  

---

## ✨ Key Features

**Client Management**  
- Create, update, and delete clients  
- Advanced search by ID or name  
- Overview of balances and associated accounts  

**Account Management**  
- Supports `Checking` and `Savings` accounts  
- Manage overdraft limits and interest rates  
- Search and sort accounts by various criteria  
- **Sealed class** architecture for type-safe hierarchy  

**Transaction Analysis**  
- Record all operations (`DEPOSIT`, `WITHDRAWAL`, `TRANSFER`)  
- Filter by amount, type, date, and location  
- Automatic calculation of totals and averages  

**Anomaly Detection**  
- High amounts exceeding a configurable threshold  
- Unusual transaction locations  
- Excessive transaction frequency  
- Alerts on inactive accounts  

**Reports and Statistics**  
- Top clients by total balance  
- Monthly reports by transaction type  
- Volume and trend analysis  

**Principles**: Separation of responsibilities, immutability (Records), closed hierarchy (Sealed Classes), functional programming (Stream API).

---

## 🏗️ Architecture
Layered architecture following SOLID principles:

Presentation Layer (UI)
├─ AppMenu.java
└─ AppMain.java
↓
Service Layer
├─ ClientService.java
├─ AccountService.java
├─ TransactionService.java
└─ ReportService.java
↓
Repository Layer
├─ ClientRepository.java
├─ AccountRepository.java
└─ TransactionRepository.java
↓
Model Layer (Records & Sealed Classes)
├─ ClientRecord.java
├─ BankAccount.java
├─ CheckingAccount.java
├─ SavingsAccount.java
└─ TransactionRecord.java
↓
Database Layer
└─ PostgreSQL via JDBC
---

## 🛠️ Technologies Used

| Technology | Version | Usage |
|------------|---------|-------|
| Java       | 17+     | Main language, Records, Sealed Classes, Pattern Matching |
| JDBC       | 4.0+    | Data persistence and access |
| PostgreSQL | 13+     | Relational database |
| Stream API | Java 17 | Functional programming and data processing |
| Git        | 2.x     | Version control |

---

## 📦 Prerequisites
- **Java JDK 17+** (`java -version`)  
- **PostgreSQL 13+** (`psql --version`)  
- **Git** (`git --version`)  
- Java IDE (IntelliJ IDEA, Eclipse, or VS Code)

---

## 🚀 Installation

1. **Clone the repository**
```bash
git clone https://github.com/your-username/MyBankDB.git
cd MyBankDB
```

### 📄 License

MIT License © 2025 Rajae Elmrabet
=======
# MyBankDB---Intelligent-Transaction-Management-System
MyBankDB is a robust Java application for managing and analyzing banking transactions. It allows you to manage clients, accounts, and transactions while automatically detecting financial anomalies.
>>>>>>> 61f5b858276ae5c04669ca89127ea65eacb71f54
