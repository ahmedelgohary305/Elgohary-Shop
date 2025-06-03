# Elgohary-Shop E-Commerce App

A modern, full-featured e-commerce Android application built with the latest Android development technologies and best practices.

## 🚀 Features

- **Product Browsing**: Browse through a wide range of products with detailed information
- **User Authentication**: Secure user registration and login system
- **Shopping Cart**: Add, remove, and manage items in your shopping cart
- **Order Management**: Place orders and track order history
- **Search & Filtering**: Advanced product search and filtering capabilities
- **Dark/Light Theme**: Seamless theme switching for better user experience
- **Offline Support**: Room database ensures app functionality even offline
- **Responsive UI**: Adaptive design that works across different screen sizes

## 🛠️ Tech Stack

### **UI & Design**
- **Jetpack Compose** - Modern declarative UI toolkit
- **Material Design 3** - Latest Material Design components
- **Dark/Light Theme Support** - Dynamic theme switching

### **Architecture & Patterns**
- **Clean Architecture** - Separation of concerns with clear layer boundaries
- **MVVM Pattern** - Model-View-ViewModel architecture pattern
- **Repository Pattern** - Centralized data access layer

### **Backend Integration**
- **Shopify Storefront API** - E-commerce backend integration
- **Apollo GraphQL** - Type-safe GraphQL client for API communication

### **Local Storage**
- **Room Database** - Local SQLite database with compile-time verification
- **DataStore** - Modern preferences storage solution

### **Dependency Injection**
- **Dagger Hilt** - Compile-time dependency injection framework

### **Concurrency**
- **Kotlin Coroutines** - Asynchronous programming
- **Flow** - Reactive programming with data streams

### **Image Loading**
- **Coil** - Fast, lightweight image loading library optimized for Compose

### **Development Language**
- **Kotlin** - 100% Kotlin codebase with modern language features

## 📱 Screenshots

<div>

### Product Detail Page
<img src="Elgohary Shop media/Detail Screen.png" alt="Product Detail" width="300"/>

*7 Chakra Bracelet with detailed description and purchase options*

### Home & Categories
<img src="Elgohary Shop media/Main Screen.png" alt="Home & Categories" width="300"/>

*Main dashboard with category navigation and latest products*

### Wishlist Management
<img src="Elgohary Shop media/Wishlist Screen.png" alt="Wishlist" width="300"/>

*Personal wishlist with saved items across different categories*

### Shopping Cart
<img src="Elgohary Shop media/Cart Screen.png" alt="Shopping Cart" width="300"/>

*Cart management with quantity controls and checkout option*

### Category Browse
<img src="Elgohary Shop media/Category Screen.png" alt="Home & Garden Category" width="300"/>

*Home & Garden category with diverse product selection*

</div>

## 🏗️ Architecture

The app follows Clean Architecture principles with three main layers:

```
├── presentation/        # UI Layer (Compose, ViewModels)
├── domain/             # Business Logic Layer (Use Cases, Entities)
└── data/              # Data Layer (Repositories, Data Sources)
```

### **Presentation Layer**
- Jetpack Compose UI components
- ViewModels for state management
- UI state handling with StateFlow

### **Domain Layer**
- Domain entities and models
- Repository interfaces

### **Data Layer**
- Repository implementations
- Remote data sources (Shopify API)
- Local data sources (Room database)
- Data mapping

## 🔧 Setup & Installation

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17 or higher
- Android SDK API level 24 or higher

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/ahmedelgohary305/Elgohary-Shop.git
   cd ecommerce-app
   ```

2. **Configure Shopify API**
   - Create a `local.properties` file in the root directory
   - Add your Shopify credentials:
   ```properties
   SHOPIFY_DOMAIN=your-shop-domain.myshopify.com
   SHOPIFY_STOREFRONT_ACCESS_TOKEN=your-storefront-access-token
   ```

3. **Build and Run**
   - Open the project in Android Studio
   - Sync the project with Gradle files
   - Run the app on an emulator or physical device

## 📦 Dependencies

### Core Dependencies
```kotlin
// UI & Compose
implementation "androidx.compose.ui:ui:$compose_version"
implementation "androidx.compose.material3:material3:$material3_version"
implementation "androidx.activity:activity-compose:$activity_compose_version"

// Architecture Components
implementation "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version"
implementation "androidx.navigation:navigation-compose:$nav_version"

// Dependency Injection
implementation "com.google.dagger:hilt-android:$hilt_version"
kapt "com.google.dagger:hilt-compiler:$hilt_version"

// Database
implementation "androidx.room:room-runtime:$room_version"
implementation "androidx.room:room-ktx:$room_version"
kapt "androidx.room:room-compiler:$room_version"

// Network
implementation "com.apollographql.apollo3:apollo-runtime:$apollo_version"
implementation "com.apollographql.apollo3:apollo-api:$apollo_version"

// Image Loading
implementation "io.coil-kt:coil-compose:$coil_version"

// Preferences
implementation "androidx.datastore:datastore-preferences:$datastore_version"
```

## 🎨 Key Features Implementation

### **Theme Management**
- Dynamic color schemes based on system settings
- Persistent theme preference using DataStore
- Smooth theme transitions

### **State Management**
- Unidirectional data flow with StateFlow
- Compose state hoisting for reusable components
- Error handling and loading states

### **Offline Capability**
- Room database caching for products and user data
- Network connectivity monitoring
- Graceful offline/online transitions

### **Performance Optimization**
- LazyColumn for efficient list rendering
- Image caching with Coil
- Coroutines for background operations
- Memory leak prevention

## 🚀 Performance

- Lazy loading for large datasets
- Image optimization and caching
- Background processing with coroutines
- Memory-efficient Compose components

## 👨‍💻 Developer

**Ahmed Mohamed**
- LinkedIn: [My LinkedIn](www.linkedin.com/in/ahmedelgohary14)
- Email: ahmedelgohary040@gmail.com

## 🙏 Acknowledgments

- Shopify for providing the Storefront API
- Google for Android development tools and libraries
- The open-source community for amazing libraries
