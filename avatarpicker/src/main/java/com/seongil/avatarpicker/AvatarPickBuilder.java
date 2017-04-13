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
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

/**
 * @author seong-il, kim
 * @since 17. 4. 6
 */
public class AvatarPickBuilder {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================

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
    public static AvatarPicker build(@NonNull Fragment fragment) {
        return new AvatarPickerForFragmentV4(fragment);
    }

    public static AvatarPicker build(@NonNull android.app.Fragment fragment) {
        return new AvatarPickerForFragment(fragment);
    }

    public static AvatarPicker build(@NonNull Activity activity) {
        return new AvatarPickerForActivity(activity);
    }

    public static AvatarPicker build(@NonNull AppCompatActivity appCompatActivity) {
        return new AvatarPickerForAppCompatActivity(appCompatActivity);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
    private static class AvatarPickerForFragmentV4 extends AbstractAvatarPicker {

        private final WeakReference<Fragment> mFragmentRef;

        AvatarPickerForFragmentV4(@NonNull Fragment fragment) {
            mFragmentRef = new WeakReference<>(fragment);
        }

        @NonNull
        @Override
        protected Context getContext() {
            return mFragmentRef.get().getActivity().getApplicationContext();
        }

        @Override
        protected void startActivityForResult(@NonNull Intent intent, int requestCode) {
            mFragmentRef.get().startActivityForResult(intent, requestCode);
        }
    }

    private static class AvatarPickerForFragment extends AbstractAvatarPicker {

        private final WeakReference<android.app.Fragment> mFragmentRef;

        AvatarPickerForFragment(@NonNull android.app.Fragment fragment) {
            mFragmentRef = new WeakReference<>(fragment);
        }

        @NonNull
        @Override
        protected Context getContext() {
            return mFragmentRef.get().getActivity().getApplicationContext();
        }

        @Override
        protected void startActivityForResult(@NonNull Intent intent, int requestCode) {
            mFragmentRef.get().startActivityForResult(intent, requestCode);
        }
    }

    private static class AvatarPickerForAppCompatActivity extends AbstractAvatarPicker {

        private final WeakReference<AppCompatActivity> mActivityRef;

        AvatarPickerForAppCompatActivity(@NonNull AppCompatActivity activity) {
            mActivityRef = new WeakReference<>(activity);
        }

        @NonNull
        @Override
        protected Context getContext() {
            return mActivityRef.get().getApplicationContext();
        }

        @Override
        protected void startActivityForResult(@NonNull Intent intent, int requestCode) {
            mActivityRef.get().startActivityForResult(intent, requestCode);
        }
    }

    private static class AvatarPickerForActivity extends AbstractAvatarPicker {

        private final WeakReference<Activity> mActivityRef;

        AvatarPickerForActivity(@NonNull Activity activity) {
            mActivityRef = new WeakReference<>(activity);
        }

        @NonNull
        @Override
        protected Context getContext() {
            return mActivityRef.get().getApplicationContext();
        }

        @Override
        protected void startActivityForResult(@NonNull Intent intent, int requestCode) {
            mActivityRef.get().startActivityForResult(intent, requestCode);
        }
    }
}
