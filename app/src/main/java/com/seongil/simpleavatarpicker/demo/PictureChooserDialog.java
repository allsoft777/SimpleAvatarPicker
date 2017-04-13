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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.seongil.recyclerviewlife.model.common.RecyclerViewItem;
import com.seongil.simpleavatarpicker.demo.adapter.PictureChooserMenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author seong-il, kim
 * @since 17. 3. 31
 */
public class PictureChooserDialog extends DialogFragment {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private List<SingleTextViewData> mMenuList;
    private PictureChooserDialogListener mCallerCallback;

    // ========================================================================
    // constructors
    // ========================================================================
    public static synchronized PictureChooserDialog newInstance() {
        return new PictureChooserDialog();
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
          @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picture_chooser, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallerCallback = (PictureChooserDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupMenu();
        renderMenuListView(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        if (window != null) {
            ViewGroup.LayoutParams params = window.getAttributes();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes((android.view.WindowManager.LayoutParams) params);
        }
        super.onResume();
    }

    // ========================================================================
    // methods
    // ========================================================================
    private void setupMenu() {
        mMenuList = new ArrayList<>();
        mMenuList.add(new SingleTextViewData(getString(R.string.picture_chooser_menu_default)));
        mMenuList.add(new SingleTextViewData(getString(R.string.picture_chooser_menu_chooser_from_gallery)));
        mMenuList.add(new SingleTextViewData(getString(R.string.picture_chooser_menu_take_pic)));
    }

    @SuppressWarnings("unchecked")
    private void renderMenuListView(View view) {
        RecyclerView listView = (RecyclerView) view.findViewById(R.id.recycler_view);
        PictureChooserMenuAdapter adapter =
              new PictureChooserMenuAdapter(getActivity().getLayoutInflater(), this::handleClickItem);
        adapter.addLastCollection(mMenuList);
        final LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        listView.setAdapter(adapter);
        listView.setHasFixedSize(true);
        listView.setFocusable(false);
    }

    private void handleClickItem(
          @NonNull RecyclerView.ViewHolder vh,
          @NonNull RecyclerViewItem info, final int position) {

        switch (position) {
            case 0:
                mCallerCallback.onClearImage();
                break;
            case 1:
                mCallerCallback.onTakeImageFromGallery();
                break;
            case 2:
                mCallerCallback.onTakeImageFromCamera();
                break;
            default:
                throw new IllegalArgumentException("Invalid position");
        }
        dismiss();
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
    public interface PictureChooserDialogListener {

        void onClearImage();

        void onTakeImageFromGallery();

        void onTakeImageFromCamera();
    }
}
