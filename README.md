# GithubUserSample
Android sample for fetching Github users using some latest Jetpack components

## Environment and third-party libraries
* IDE: Android Studio Bumblebee | 2021.2.1 Patch 1
* Language: Kotlin 1.7.10 with coroutines 1.6.1
* Lifecyle + ViewModel + LiveData 2.5.1
* Dependency Injection: Hilt 2.42 
* Material 1.7.0, Constraintlayout 2.1.4
* Retrofit 2.9.0 + Moshi 1.13.0 + Logging Interceptor 5.0.0 for API request 
* Room 2.4.3 for database
* Paging 3.1.1 for loading Github users pages data 
* Stetho 1.5.1 for app inspector
* Glide 4.13.1 for image loading
* Espresso 3.4.0, Mockito 4.4.0, Mockk 1.12.3 for instrumentation test

### Structure

Source code is located in `src` directory

```sh
com.example.githubusersapmple
├── app
├── data
├── di
├── domain
└── ui

```

* app
    * Application layer without UI
        * android.app.Application / android.app.Service layer, etc
* data
    * Infrastructure layer
        * Put pure access methods and rules to the persistence layer such as network, storage,...
* domain
    * Handle loading/syncing data from API and local database
* di
    * DI Component, Module
* ui
    * Presentation layer
    * View, UI
    * viewmodel to handle UI interaction

### Unit test and UI test
- Notes: make sure to keep your device active during testing

```sh
cd src
./gradlew uninstallAll
./gradlew connectedAndroidTest
```

### Build And Install

Debug

```sh
cd src
./gradlew installDebug
```

Release
* Create keystore file and signing.properties file

```sh
cr src
mkdir keystore
cd keystore/
keytool -genkey -v -keystore your_release.jks -alias your_key_alias -keyalg RSA -keysize 2048 -validity 10000 -deststoretype pkcs12
```

* Edit release_signing.properties file with below format

```properties
keyAlias=(your key alias)
keyPassword=(your key password)
storeFile=(your store file name)
storePassword=(your store password)
```

* Build and install release version

```sh
./gradlew installRelease
```

### Android App Bundle

### Build

```sh
cd src
./gradlew :app:bundleRelease
```

### Install APKs file generated from app bundle

```sh
cd src/build/outputs/bundle/release
bundletool build-apks --bundle=app-release.aab --output=app-release.apks
 --ks=your_keystore_file 
 --ks-pass=pass:your_keystore_password 
 --ks-key-alias=your_key_alias 
 --key-pass=pass:your_key_password
bundletool install-apks --apks=app-release.apks 
```
