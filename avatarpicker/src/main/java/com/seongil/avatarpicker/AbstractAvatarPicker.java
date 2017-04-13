/*
 * Copyright (C) 2017 Seong-il Kim <kims172@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seongil.avatarpicker;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.seongil.avatarpicker.intentbuilder.AvatarIntentBuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author seong-il, kim
 * @since 17. 4. 4
 */
public abstract class AbstractAvatarPicker implements AvatarPicker {

    // ========================================================================
    // constants
    // ========================================================================
    private static final String TAG = "[Avatar Picker] ";

    private static final int REQ_CODE_IDENTIFICATOR = 0b111001000; // 456 (decimal)
    private static final int REQ_CODE_PICK_AVATAR_FROM_GALLERY = REQ_CODE_IDENTIFICATOR + (1 << 10);
    private static final int REQ_CODE_PICK_AVATAR_FROM_CAMERA = REQ_CODE_IDENTIFICATOR + (1 << 11);
    private static final int REQ_CODE_CROP_AVATAR = REQ_CODE_IDENTIFICATOR + (1 << 12);

    // the authority is defined on the AndroidManifes.xml file.
    private static final String FILEPROVIDER_AUTHORITY = "com.seongil.avatarpicker.fileprovider";

    // the files path is defined in the file of the provider paths in the res/xml/
    private static final String FILES_PATH_ALIAS = "my_thumbnails";
    private static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";

    // ========================================================================
    // fields
    // ========================================================================
    private Uri mUri;
    private AvatarIntentBuilder mIntentBuilder;

    // ========================================================================
    // constructors
    // ========================================================================

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @NonNull
    protected abstract Context getContext();

    protected abstract void startActivityForResult(@NonNull Intent intent, int requestCode);

    @Override
    public void pickAvatarFromCamera(@Nullable AvatarIntentBuilder builder) {
        createTempFile();
        mIntentBuilder = builder != null ? builder : getDefaultBuilder();
        mIntentBuilder.outputUri(mUri);
        final Intent intent = mIntentBuilder.buildCameraApp();
        try {
            startActivityForResult(intent, REQ_CODE_PICK_AVATAR_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            showToastMsg("Your device doesn't support to capturing image.");
        }
    }

    @Override
    public void pickAvatarFromGallery(@Nullable AvatarIntentBuilder builder) {
        createTempFile();
        mIntentBuilder = builder != null ? builder : getDefaultBuilder();
        Intent intent = mIntentBuilder.buildGalleryApp();
        try {
            startActivityForResult(intent, REQ_CODE_PICK_AVATAR_FROM_GALLERY);
        } catch (ActivityNotFoundException e) {
            showToastMsg("Your device have no gallery app.");
        }
    }

    // ========================================================================
    // methods
    // ========================================================================
    private void startActivityToCropImage() {
        mIntentBuilder.outputUri(mUri);
        Intent intent = mIntentBuilder.buildCropApp();
        try {
            startActivityForResult(intent, REQ_CODE_CROP_AVATAR);
        } catch (ActivityNotFoundException e) {
            showToastMsg("Your device is not support to crop image.");
        }
    }

    @NonNull
    private ContentResolver getContentResolver() {
        return getContext().getContentResolver();
    }

    private void createTempFile() {
        final File directory = getDirectoryFile();
        final String timeStamp = new SimpleDateFormat(TIMESTAMP_FORMAT, Locale.KOREA).format(new Date());
        try {
            File tempFile = File.createTempFile(timeStamp, ".jpeg", directory);
            mUri = FileProvider.getUriForFile(getContext(), FILEPROVIDER_AUTHORITY, tempFile);
        } catch (IllegalArgumentException | IOException e) {
            showToastMsg(e.getMessage());
        }
    }

    @NonNull
    private File getDirectoryFile() {
        return createOrGetDirectoryFromFileSystem(FILES_PATH_ALIAS);
    }

    @CheckResult
    @Nullable
    @Override
    public Uri onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri result = null;
        if (resultCode != Activity.RESULT_OK) {
            return null;
        }
        final boolean isValidRequestCode = (requestCode & REQ_CODE_IDENTIFICATOR) > 0;
        if (!isValidRequestCode) {
            return null;
        }

        if (requestCode == REQ_CODE_PICK_AVATAR_FROM_CAMERA) {
            startActivityToCropImage();
        } else if (requestCode == REQ_CODE_CROP_AVATAR) {
            result = mUri;
        } else if (requestCode == REQ_CODE_PICK_AVATAR_FROM_GALLERY) {
            result = handlePickedImageFromGallery(data);
        }
        return result;
    }

    @Nullable
    private Uri handlePickedImageFromGallery(@NonNull Intent data) {
        Bitmap bitmap = data.getParcelableExtra("data");
        if (bitmap != null) {
            saveAvatarImgToFileSystemStorage(bitmap);
            return mUri;
        }
        if (data.getData() == null) {
            return null;
        }

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            if (bitmap == null) {
                showToastMsg("Retrieved bitmap is invalid.");
                return null;
            }
            saveAvatarImgToFileSystemStorage(bitmap);

            // Some application doesn't support the crop property, so ask it with the CROP action again.
            startActivityToCropImage();
        } catch (IOException e) {
            showToastMsg(e.getMessage());
        }
        return null;
    }

    private void saveAvatarImgToFileSystemStorage(@NonNull Bitmap bitmap) {
        final File tempFile = new File(mUri.getPath());
        final File file = new File(getDirectoryFile(), tempFile.getName());
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            showToastMsg(e.getMessage());
        }
    }

    private void showToastMsg(String msg) {
        Toast.makeText(getContext(), TAG + msg, Toast.LENGTH_SHORT).show();
    }

    private AvatarIntentBuilder getDefaultBuilder() {
        AvatarIntentBuilder builder = new AvatarIntentBuilder();
        builder
              .aspectX(1)
              .aspectY(1)
              .outputX(AvatarIntentBuilder.DEFAULT_OUTPUT_X_PIXEL)
              .outputY(AvatarIntentBuilder.DEFAULT_OUTPUT_Y_PIXEL);
        return builder;
    }

    private File createOrGetDirectoryFromFileSystem(@NonNull String dirName) {
        final File dir = new File(getContext().getFilesDir(), dirName);
        if (dir.exists()) {
            return dir;
        }

        final boolean result = dir.mkdirs();
        if (!result) {
            Log.d(TAG, "Failed to create a directory on the filesystem.");
        }
        return dir;
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
