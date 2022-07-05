<img src="https://i.imgur.com/FvlBVwb.png"
alt="J" width="400"/>

# Go4Lunch


App that allows you to choose a restaurant from your surroundings and let all your coworkers know that you're eating there for lunch. The app helps to organize
lunch break between coworkers. You'll be able to tell the others where you are going, and see where they are going too. You will also receive a reminder via notification
everyday at 12 that will contain the restaurant you choose, where it is located and who is going with you.


# Features 

- Authentification with Gmail / Facebook and Twitter via Firebase Authentification


<img src="https://i.imgur.com/11KpQ5s.jpg" alt="J" width="300"/>

- Main Activity 

  - Map Fragment : On this Fragment, you can quickly locate the restaurants around you and identify with a custom marker where your collegues are eating 
  - Restaurants Fragment : displays a list of restaurants around you with basic details and informations.
  - Coworker Fragment : displays a list of all users and where they are eating lunch
  
  <p float="left">
  <img src="https://i.imgur.com/MzyYEh0.jpg" width="200" />
  <img src="https://i.imgur.com/ojnkGgO.jpg" width="200" /> 
  <img src="https://i.imgur.com/RpVN29f.jpg" width="200" />
</p>

- Drawer 
  
  User can access a drawer menu with his personnal informations in the header and access to his lunch, parametres and log out from his account.
  
  <img src="https://i.imgur.com/2cuOBhP.jpg"
  alt="J" width="300"/>
  
- Detailed activity
  From this view, user can see if coworkers are going to eat at this restaurant, call it, visit the website, add it to his favourite and select this place for the day.
- The notifications are managed with WorkManager and will notify the user at 12 everyday where he is eating today, where and with who (based on other users choice)

<p float="left">
  <img src="https://i.imgur.com/0vf9suA.jpg" width="300" />
  <img src="https://i.imgur.com/qpTsjM2.jpg" width="300" /> 
</p>
  
 
 # Architecture
 - MVVM with clean architecture → Repository / Usecase / viewModel
 - Architecture components → LiveData / WorkManager
 
# Libraries 
 - Firebase Auth
 - Firebase Firestore
 - Facebook SDK
 - Twitter SDK
 - EasyPermissions
 - Retrofit
 - OkHttp
 - WorkManager
 - Glide 
 - MockWebServer
 - Mockito 
 
 # Unit Testing
  - ViewModel with Mockito
  - Retrofit interface with WebMockServer
  
 
 
  
  
  
 
  
 


