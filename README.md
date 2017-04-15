# Simple AvatarPicker

Super simple library to pick an image from the gallery or camera application.

## Screenshot
![](./gif/avatar_picker_from_camera.gif)__
![](./gif/avatar_picker_from_gallery.gif)


Usage
-----

## Build Settings

##### Gradle
```groovy
dependencies {
    compile 'com.seongil:avatarpicker:1.0.0'
}
```
##### Maven
```xml
<dependency>
    <groupId>com.seongil</groupId>
    <artifactId>avatarpicker</artifactId>
    <version>1.0.0</version>
    <type>pom</type>
</dependency>
```

## Usage

The library is using the FileProvider to retrieve a picked image from the gallery or camera app.
So, you don't need to grant any storage permissions to your app. For the resource file of the avatar_picker_provider_paths.xml, you can use the library file or declare your own file.

1. Declare the file provider in your application.
```java
// ...

<application>
    // ...
    <provider
        android:name="android.support.v4.content.FileProvider"
        android:authorities="${applicationId}.fileprovider"
        android:exported="false"
        android:grantUriPermissions="true">
    
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/avatar_picker_provider_paths"/>
    </provider>
</application>
```

2. Use it on your client layer.
```java
private static final String AVATAR_FILE_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider";

// 2.1 Pick an image from the gallery app.

AvatarPicker avatarPicker = AvatarPickBuilder.build(this, AVATAR_FILE_PROVIDER_AUTHORITY);
AvatarIntentBuilder builder = new AvatarIntentBuilder();
builder
      .aspectX(1)
      .aspectY(1)
      .outputX(1024)
      .outputY(1024);
avatarPicker.pickAvatarFromGallery(builder);


// 2.2 Pick an image from the camera app.
 
AvatarPicker avatarPicker = AvatarPickBuilder.build(this, AVATAR_FILE_PROVIDER_AUTHORITY);
AvatarIntentBuilder builder = new AvatarIntentBuilder();
builder
      .aspectX(1)
      .aspectY(1)
      .outputX(1024)
      .outputY(1024);
avatarPicker.pickAvatarFromCamera(builder);
```

3. Retrieve a picked image.
```java
// in activity or fragment..

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    Uri uri = mAvatarPicker.onActivityResult(requestCode, resultCode, data);
    if(uri != null) {
    	renderImage(uri);
    }
    // ...
}
```

<br>

License
-------

    Copyright (C) 2017 Seong-il Kim

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

