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

package com.google.samples.dataprivacy.page.images;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.samples.dataprivacy.R;
import com.google.samples.dataprivacy.model.Image;
import com.google.samples.dataprivacy.util.ImagesAdapter;
import com.google.samples.dataprivacy.page.viewimage.ImageViewerActivity;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ImagesFragment extends Fragment implements ImagesContract.View,
        EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private static final String TAG = "ImagesFragment";

    private ImagesContract.Presenter mPresenter;
    private ImagesAdapter mImagesAdapter;

    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private CoordinatorLayout mCoordinatorLayout;
    private Snackbar mPermissionSnackbar;

    public ImagesFragment() {
        mImagesAdapter = new ImagesAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_images, null);
        layout.findViewById(R.id.fab_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.openTakePhoto();
            }
        });

        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mImagesAdapter);
        mRecyclerView.setHasFixedSize(true);
        mImagesAdapter.setOnImageClickListener(new ImagesAdapter.OnImageItemClickListener() {
            @Override
            public void onImageClick(String path) {
                mPresenter.openImage(path);
            }
        });

        mCoordinatorLayout = (CoordinatorLayout) layout.findViewById(R.id.images_coordinator_layout);
        mEmptyView = layout.findViewById(R.id.empty_view);

        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_images, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_import:
                mPresenter.openImportPhoto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setPresenter(ImagesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showImages(List<Image> images) {
        mEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mImagesAdapter.setImages(images);
    }

    @Override
    public void showNoImages() {
        mEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showImage(String path) {
        Intent intent = new Intent(getContext(), ImageViewerActivity.class);
        intent.putExtra(ImageViewerActivity.ARGUMENT_IMAGE_PATH, path);
        startActivity(intent);

    }

    @Override
    public void requestPermissions() {
        EasyPermissions.requestPermissions(this,
                getString(R.string.external_storage_permission_required),
                REQUEST_STORAGE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void showMissingPermissions(boolean showMessage) {
        if (mPermissionSnackbar == null && !showMessage) {
            return;
        }

        if (mPermissionSnackbar == null) {
            mPermissionSnackbar = Snackbar.make(mCoordinatorLayout, R.string.missing_permissions,
                    BaseTransientBottomBar.LENGTH_INDEFINITE);
            mPermissionSnackbar.setAction(R.string.request, new PermissionRequestClickListener());
        }
        if (showMessage) {
            mPermissionSnackbar.show();
        } else {
            mPermissionSnackbar.dismiss();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        if (requestCode != REQUEST_STORAGE_PERMISSION) {
            return;
        }
        mPresenter.onPermissionRequestResult(true);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        if (requestCode != REQUEST_STORAGE_PERMISSION) {
            return;
        }

        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            // Permanently denied. Notify presenter.
            Log.d(TAG, "Permission has been permanently denied.");
            new AppSettingsDialog.Builder(this).build().show();
        } else {
            mPresenter.onPermissionRequestResult(false);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private class PermissionRequestClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            requestPermissions();
        }
    }
}
