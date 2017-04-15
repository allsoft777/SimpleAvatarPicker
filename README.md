# Simple AvatarPicker

Super simple library to pick an image from the gallery or camera application.

## Screenshot
![](./gif/avatar_picker_from_camera.gif)
<br>
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

1. Pick an image from the gallery app.
```java
AvatarPicker avatarPicker = AvatarPickBuilder.build(this);
AvatarIntentBuilder builder = new AvatarIntentBuilder();
builder
      .aspectX(1)
      .aspectY(1)
      .outputX(1024)
      .outputY(1024);
avatarPicker.pickAvatarFromGallery(builder);
```

2. Pick an image from the camera app.
```java
AvatarPicker avatarPicker = AvatarPickBuilder.build(this);
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

