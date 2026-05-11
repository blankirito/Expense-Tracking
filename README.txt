Expense Track Android App
A mobile application designed to help users track daily expenses, manage budgets, and gain insights into their spending habits.

Features
- User authentication (Login & Registration)
- Expense tracking and categorization
- Data visualization using charts
- Budget management and monitoring
- OCR-based receipt scanning (Maybank digital receipts)
- Monthly spending prediction (machine learning-based)
- User profile management

Tech Stack
- Kotlin
- Jetpack Compose
- Firebase Firestore

Setup Instructions
1. Clone the repo
   https://github.com/blankirito/Expense-Tracking.git
2. Open in Android Studio
3. Configure Firebase:
   - Add your `google-services.json` file
4. Build and run the app

Architecture
This project follows the MVVM (Model-View-ViewModel) architecture:
- View: Built with Jetpack Compose for UI rendering
- ViewModel: Handles UI state and business logic
- Repository: Manages data operations and Firebase integration
- Firebase Firestore: Acts as the backend database

Future Improvements
- train own OCR model & prediction model
- multi-authentication method
- merge Add Expense Page & Scan Page, add new function (income, transfer)

Problem & Solution
- Problem
student and adult struggle with tracking their daily expense manually and will make mistake while record manually.

- Solution
This application provides a simple and intuitive way to log and monitor their daily spending.

