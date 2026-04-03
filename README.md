# Expense Track Android App
A mobile application designed to help users track daily expenses, manage budgets, and gain insights into their spending habits.

## UI

### Authentication
<p>
  <img src="https://github.com/user-attachments/assets/29e8f827-0476-4520-96cd-151b7b9f8383" width="250"/>
  <img src="https://github.com/user-attachments/assets/c3b02bae-800f-4932-b519-28ae747689a1" width="250"/>
</p>

### Main Features
<p>
  <img src="https://github.com/user-attachments/assets/d4543860-9d40-4449-82d7-21726c03ba3c" width="250"/>
  <img src="https://github.com/user-attachments/assets/e854c477-89da-44ae-bd18-c0b0bbd8f380" width="250"/>
  <img src="https://github.com/user-attachments/assets/0e65c6cb-424e-4c3c-a659-c48d530643f2" width="250"/>
</p>

<p>
  <img src="https://github.com/user-attachments/assets/37a6c42f-7324-42cb-a198-5bb0c98caff1" width="250"/>
  <img src="https://github.com/user-attachments/assets/2da5d379-2cfe-4d78-9e4a-182b052452db" width="250"/>
</p>

### Advanced Features
<p>
  <img src="https://github.com/user-attachments/assets/54c766f9-117d-4038-b9dd-fa75eb99b6aa" width="250"/>
  <img src="https://github.com/user-attachments/assets/dd94e207-ecff-437a-bce7-5c3593c6779b" width="250"/>
</p>


## Features
- User authentication (Login & Registration)
- Expense tracking and categorization
- Data visualization using charts
- Budget management and monitoring
- OCR-based receipt scanning (Maybank digital receipts)
- Monthly spending prediction (machine learning-based)
- User profile management

## Tech Stack
- Kotlin
- Jetpack Compose
- Firebase Firestore

## Setup Instructions
1. Clone the repo
   https://github.com/blankirito/Expense-Tracking.git
2. Open in Android Studio
3. Configure Firebase:
   - Add your `google-services.json` file
4. Build and run the app

## Architecture
This project follows the MVVM (Model-View-ViewModel) architecture:
- View: Built with Jetpack Compose for UI rendering
- ViewModel: Handles UI state and business logic
- Repository: Manages data operations and Firebase integration
- Firebase Firestore: Acts as the backend database

## Future Improvements
- train own OCR model & prediction model
- multi-authentication method
- merge Add Expense Page & Scan Page, add new function (income, transfer)

## Problem & Solution
### Problem
student and adult struggle with tracking their daily expense manually and will make mistake while record manually.

### Solution
This application provides a simple and intuitive way to log and monitor their daily spending.

