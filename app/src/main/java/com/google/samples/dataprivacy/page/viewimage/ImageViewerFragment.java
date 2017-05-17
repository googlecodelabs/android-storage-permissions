/*
 * Copyright 2017 Google Inc. All rights reserved.
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

package com.google.samples.dataprivacy.page.viewimage;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.samples.dataprivacy.R;

/**
 * A fragment displaying an image. See {@link ImageViewerContract}.
 * Delete and share functions are exposed through toolbar items.
 */
public class ImageViewerFragment extends Fragment implements ImageViewerContract.View {

    private ImageView mImageView;
    private ImageViewerContract.Presenter mPresenter;

    public static ImageViewerFragment newInstance() {
        return new ImageViewerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_image_viewer, container, false);
        mImageView = (ImageView) layout.findViewById(R.id.image_view);

        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) layout.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_image_viewer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                mPresenter.deleteImage();
                return true;
            case R.id.action_share:
                mPresenter.shareImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(ImageViewerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void displayImage(Bitmap image) {
        mImageView.setImageBitmap(image);
    }

    @Override
    public void displayImageDeleted() {
        getActivity().finish();
    }

}
