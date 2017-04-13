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

package com.seongil.avatarpicker.intentbuilder;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * @author seong-il, kim
 * @since 17. 4. 6
 */
public class AvatarIntentBuilder {

    // ========================================================================
    // constants
    // ========================================================================
    private static final int INVALID_VALUE = -1;
    public static final int DEFAULT_OUTPUT_X_PIXEL = 512;
    public static final int DEFAULT_OUTPUT_Y_PIXEL = 512;

    private static final String PREDEFINED_KEY_ASPECT_X = "aspectX";
    private static final String PREDEFINED_KEY_ASPECT_Y = "aspectY";
    public static final String PREDEFINED_KEY_OUTPUT_X = "outputX";
    public static final String PREDEFINED_KEY_OUTPUT_Y = "outputY";
    private static final String PREDEFINED_KEY_RETURN_DATA = "return-data";
    private static final String PREDEFINED_KEY_NO_FACE_DETECTION = "noFaceDetection";

    // ========================================================================
    // fields
    // ========================================================================
    private int aspectX = INVALID_VALUE;
    private int aspectY = INVALID_VALUE;
    private int outputX = DEFAULT_OUTPUT_X_PIXEL;
    private int outputY = DEFAULT_OUTPUT_Y_PIXEL;
    private boolean noFaceDetection = true;
    private String type = "image/*";
    private Uri outputUri;

    // ========================================================================
    // constructors
    // ========================================================================

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    public AvatarIntentBuilder aspectX(int aspectX) {
        this.aspectX = aspectX;
        return this;
    }

    public AvatarIntentBuilder aspectY(int aspectY) {
        this.aspectY = aspectY;
        return this;
    }

    public AvatarIntentBuilder outputX(int outputX) {
        this.outputX = outputX;
        return this;
    }

    public AvatarIntentBuilder outputY(int outputY) {
        this.outputY = outputY;
        return this;
    }

    public AvatarIntentBuilder noFaceDetection(boolean noFaceDetection) {
        this.noFaceDetection = noFaceDetection;
        return this;
    }

    public AvatarIntentBuilder type(String type) {
        this.type = type;
        return this;
    }

    public AvatarIntentBuilder outputUri(Uri uri) {
        this.outputUri = uri;
        return this;
    }

    private Intent build(Intent intent) {
        if (aspectX != INVALID_VALUE) {
            intent.putExtra(PREDEFINED_KEY_ASPECT_X, aspectX);
        }

        if (aspectY != INVALID_VALUE) {
            intent.putExtra(PREDEFINED_KEY_ASPECT_Y, aspectY);
        }

        if (outputX != INVALID_VALUE) {
            intent.putExtra(PREDEFINED_KEY_OUTPUT_X, outputX);
        }

        if (outputY != INVALID_VALUE) {
            intent.putExtra(PREDEFINED_KEY_OUTPUT_Y, outputY);
        }

        if (!TextUtils.isEmpty(type)) {
            intent.setType(type);
        }

        if (outputUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        }

        intent.putExtra(PREDEFINED_KEY_RETURN_DATA, true);
        intent.putExtra(PREDEFINED_KEY_NO_FACE_DETECTION, noFaceDetection);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

    public Intent buildGalleryApp() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return build(intent);
    }

    public Intent buildCameraApp() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(PREDEFINED_KEY_RETURN_DATA, true);
        if (outputUri == null) {
            throw new IllegalArgumentException("Output uri is null.");
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        return intent;
    }

    public Intent buildCropApp() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent = build(intent);
        if (outputUri == null || TextUtils.isEmpty(type)) {
            throw new IllegalArgumentException("You must set the valid output uri and type.");
        }
        intent.setDataAndType(outputUri, type);
        return intent;
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
