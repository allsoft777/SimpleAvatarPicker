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

package com.seongil.simpleavatarpicker.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding2.view.RxView;
import com.seongil.avatarpicker.AvatarPickBuilder;
import com.seongil.avatarpicker.AvatarPicker;
import com.seongil.avatarpicker.intentbuilder.AvatarIntentBuilder;
import com.seongil.simpleavatarpicker.demo.PictureChooserDialog.PictureChooserDialogListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author seong-il, kim
 * @since 17. 4. 4
 */
public class MainActivity extends AppCompatActivity implements PictureChooserDialogListener {

    // ========================================================================
    // constants
    // ========================================================================
    private static final String AVATAR_FILE_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider";

    // ========================================================================
    // fields
    // ========================================================================
    private ProgressBar mProgressBar;
    private ImageView mProfileThumbnail;
    private AvatarPicker mAvatarPicker;

    // ========================================================================
    // constructors
    // ========================================================================

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(getResources().getString(R.string.app_name));
            setSupportActionBar(toolbar);
        }

        mAvatarPicker = AvatarPickBuilder.build(this, AVATAR_FILE_PROVIDER_AUTHORITY);
        mProfileThumbnail = (ImageView) findViewById(R.id.user_profile_thumbnail);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_bar);

        RxView
              .clicks(mProfileThumbnail)
              .throttleFirst(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
              .subscribe(o -> showPicturePickerMenuDialog());

        renderImage("http://cfile25.uf.tistory.com/image/262EA640568FDABE08957B");
    }

    @Override
    public void onPause() {
        dismissPictureChooserDialog();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = mAvatarPicker.onActivityResult(requestCode, resultCode, data);
        if (uri != null) {
            hideAvatarLoadingView();
            renderImage(uri.toString());
        }

        if (resultCode != Activity.RESULT_OK) {
            hideAvatarLoadingView();
        }
    }

    @Override
    public void onClearImage() {
        renderImage("");
    }

    @Override
    public void onTakeImageFromGallery() {
        showAvatarLoadingView();
        AvatarIntentBuilder builder = new AvatarIntentBuilder();
        builder
              .aspectX(1)
              .aspectY(1)
              .outputX(1024)
              .outputY(1024);
        mAvatarPicker.pickAvatarFromGallery(builder);
    }

    @Override
    public void onTakeImageFromCamera() {
        showAvatarLoadingView();
        AvatarIntentBuilder builder = new AvatarIntentBuilder();
        builder
              .aspectX(1)
              .aspectY(1)
              .outputX(1024)
              .outputY(1024);
        mAvatarPicker.pickAvatarFromCamera(builder);
    }

    // ========================================================================
    // methods
    // ========================================================================
    private void renderImage(String uri) {
        Glide
              .with(getApplicationContext())
              .load(uri != null && uri.startsWith("http") ? uri : Uri.parse(uri))
              .fitCenter()
              .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
              .placeholder(R.drawable.user_profile_placeholder)
              .into(mProfileThumbnail);
    }

    private void showAvatarLoadingView() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideAvatarLoadingView() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void showPicturePickerMenuDialog() {
        PictureChooserDialog dlg = PictureChooserDialog.newInstance();
        dlg.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_Window_NoMinWidth);
        dlg.show(getSupportFragmentManager(), "fragment_dialog");
    }

    private void dismissPictureChooserDialog() {
        Fragment prev = getSupportFragmentManager().findFragmentByTag("fragment_dialog");
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismissAllowingStateLoss();
        }
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
