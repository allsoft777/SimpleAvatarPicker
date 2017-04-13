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

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.seongil.avatarpicker.intentbuilder.AvatarIntentBuilder;

/**
 * @author seong-il, kim
 * @since 17. 4. 6
 */
public interface AvatarPicker {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    void pickAvatarFromCamera(@Nullable AvatarIntentBuilder builder);

    void pickAvatarFromGallery(@Nullable AvatarIntentBuilder builder);

    Uri onActivityResult(int requestCode, int resultCode, Intent data);
}
