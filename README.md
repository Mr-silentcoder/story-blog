# story-blog
<b>story blog android</b>

<table>
  <tr>
    <td>Splash Screen</td>
     <td>Home Screen</td>
     <td>Spinner</td>
  </tr>
  <tr>
    <td><img src="/app ss/Screenshot_2022-08-18-16-10-24.png" width=270 height=480></td>
    <td><img src="/app ss/Screenshot_2022-08-18-16-10-35.png" width=270 height=480></td>
    <td><img src="/app ss/Screenshot_2022-08-18-16-10-47.png" width=270 height=480></td>
  </tr>
    <tr>
    <td>Wallet</td>
     <td>Password Reset</td>
     <td>Video Ads</td>
  </tr>
  <tr>
    <td><img src="/app ss/Screenshot_2022-08-18-16-10-56.png" width=270 height=480></td>
    <td><img src="/app ss/Screenshot_2022-08-18-16-12-58.png" width=270 height=480></td>
    <td><img src="/app ss/Screenshot_2022-08-18-16-11-10.png" width=270 height=480></td>
  </tr>
      <tr>
    <td>Login</td>
     <td>Sign Up</td>
     <td>User Profile</td>
  </tr>
  <tr>
    <td><img src="/app ss/Screenshot_2022-08-18-16-12-42.png" width=270 height=480></td>
    <td><img src="/app ss/Screenshot_2022-08-18-16-12-48.png" width=270 height=480></td>
    <td><img src="/app ss/Screenshot_2022-08-18-16-11-52.png" width=270 height=480></td>
  </tr>
        <tr>
    <td>Intractions</td>
     <td>Blog post</td>
     <td>Privacy Policy</td>
  </tr>
  <tr>
    <td><img src="/app ss/Screenshot_2022-08-18-16-11-19.png" width=270 height=480></td>
    <td><img src="/app ss/Screenshot_2022-08-18-16-12-20.png" width=270 height=480></td>
    <td><img src="/app ss/Screenshot_2022-08-18-16-11-27.png" width=270 height=480></td>
  </tr>
 </table>
 
<b>Firestore DB Rules</b>

  rules_version = '2'; <br/>
  service cloud.firestore { <br/>
      match /databases/{database}/documents {<br/>
      match /stories/{documentId} {<br/>
      allow read: if true;}<br/>
      match /users/{userId} {<br/>
      allow read, write: if request.auth != null && request.auth.uid == userId;}<br/>
      match /withdraw/{userId}{<br/>
      allow write: if request.auth.uid == userId;}<br/>
      match /report/{userId}{<br/>
      allow write: if request.auth.uid == userId;}<br/>
     }
     }<br/>


<br/>
<h2> Video </h2>

[![Video](https://img.youtube.com/vi/m66TwM11HOw/hqdefault.jpg)](https://youtu.be/m66TwM11HOw)
<br/>
