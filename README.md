# 📁 FYLZ | Cloud File Upload & Search System

A Java full-stack web application where users can upload, search, filter, and download files securely using AWS S3. Built with **React (frontend)** and **Spring Boot (backend)**.

---

## 🚀 Tech Stack

### Frontend 🖥️
- React.js (Hooks + Context)
- Tailwind CSS for modern UI
- Lucide-react for icons
- Token-based auth (JWT)

### Backend ⚙️
- Spring Boot (Java)
- Spring Security for JWT Authentication
- AWS S3 SDK for file storage
- PostgreSQL (or any JDBC-compatible DB)

---

## ✨ Key Features

- 🔐 User authentication with JWT
- 📁 Upload files (documents, images, archives) to AWS S3
- 🔍 Full-text search by file name or uploaded user
- 🗂️ Filters by file types (PDFs, Images, ZIPs, etc.)
- 📥 Secure file downloads via **signed S3 URLs**
- 📊 Dashboard: View own files and global search
- 🎨 Clean, responsive frontend built with Tailwind

---

## 📂 REST API Endpoints (Spring Boot)

| Method | Endpoint                    | Description                        |
|--------|-----------------------------|------------------------------------|
| POST   | `/api/auth/login`           | Login and receive JWT              |
| POST   | `/api/auth/SIGNUP`        | Register a new user                |
| GET    | `/api/files/my-files`       | Fetch current user's files         |
| GET    | `/api/files/all`            | Admin/global file list             |
| POST   | `/api/files/upload`         | Upload a file (multipart + metadata) |
| GET    | `/api/files/download/{key}` | Generate and return presigned URL  |

---

## 📸 UI Preview

> Add a screenshot here (e.g. of the search dashboard)
1. Signup Page

![image](https://github.com/user-attachments/assets/1e934597-94ea-43e7-90d1-8f347eef4b7c)

2. Login Page

![image](https://github.com/user-attachments/assets/4fd5c44e-e542-4805-9792-c415cf9721d6)

3. Uplaod Page
   
![image](https://github.com/user-attachments/assets/d78ed804-616f-4f40-914f-e34540054325)

4. User Recent page
   
![image](https://github.com/user-attachments/assets/eb4b7b64-19f3-42ca-b26e-ae95f6a82898)

5. Searcg any File on cloud
    
![image](https://github.com/user-attachments/assets/bdd2ba24-cdc6-4bb4-b17d-8ae4ea90865e)


---


