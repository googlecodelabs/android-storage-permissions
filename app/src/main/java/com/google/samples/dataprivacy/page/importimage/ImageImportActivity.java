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

package com.google.samples.dataprivacy.page.importimage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.samples.dataprivacy.R;
import com.google.samples.dataprivacy.model.Image;
import com.google.samples.dataprivacy.util.ImageUtils;
import com.google.samples.dataprivacy.util.ImagesAdapter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class ImageImportActivity extends AppCompatActivity implements
        EasyPermissions.PermissionCallbacks {
    public static final String IMPORT_RESULT =
            "com.google.samples.dataprivacy.ImageImportActivity.IMPORT_RESULT";
    private static final String TAG = "ImageImportActivity";

    private static final int MAX_FILES = 25;
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private ImagesAdapter mImagesAdapter;
    private Snackbar mPermissionSnackbar;
    private CoordinatorLayout mCoordinatorLayout;

    private FileFilter mPngFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory() || pathname.getName().endsWith("png");
        }
    };

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_import);

        mEmptyView = findViewById(R.id.empty_view);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        mImagesAdapter = new ImagesAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mImagesAdapter);
        mRecyclerView.setHasFixedSize(true);
        mImagesAdapter.setOnImageClickListener(new ImagesAdapter.OnImageItemClickListener() {
            @Override
            public void onImageClick(String path) {
                returnImage(path);
            }
        });

        showEmptyList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        requirePermission();
    }

    private void requirePermission() {
        EasyPermissions.requestPermissions
                (this, "External storage access is needed to import images.",
                        REQUEST_STORAGE_PERMISSION, Manifest.permission.READ_EXTERNAL_STORAGE);
    }


    private void returnImage(String path) {
        Toast.makeText(this, "Importing image: " + path, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra(IMPORT_RESULT, path);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void loadImages() {
        if (!isExternalStorageReadable()) {
            finish();
        }

        File externalStorage = android.os.Environment.getExternalStorageDirectory();

        ArrayList<Image> images = new ArrayList<>(MAX_FILES);
        LinkedList<File> files = new LinkedList<>();
        files.add(externalStorage);


        while (!files.isEmpty() && images.size() < MAX_FILES) {
            File file = files.pop();
            if (file.isDirectory()) {
                // Read the contents of all directories and add them to the list.
                files.addAll(Arrays.asList(file.listFiles(mPngFilter)));
            } else {
                // Read in the file and add it to the list
                Bitmap b = ImageUtils.decodeSampledBitmapFromFile(file.getAbsolutePath(), 200, 200);
                images.add(new Image(file.getAbsolutePath(), b));
            }
        }

        showList(images);

    }

    private void showEmptyList() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void showList(List<Image> images) {
        if (images == null || images.isEmpty()) {
            showEmptyList();
            return;
        }

        mImagesAdapter.setImages(images);
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        if (requestCode != REQUEST_STORAGE_PERMISSION || !list.contains(READ_EXTERNAL_STORAGE)) {
            return;
        }
        showPermissionError(false);
        loadImages();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        if (requestCode != REQUEST_STORAGE_PERMISSION || !list.contains(READ_EXTERNAL_STORAGE)) {
            return;
        }

        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            // Permanently denied. Notify presenter.
            Log.d(TAG, "Permission has been permanently denied.");
            new AppSettingsDialog.Builder(this).build().show();
        } else {
            showPermissionError(true);
        }

    }

    private void showPermissionError(boolean showMessage) {
        if (mPermissionSnackbar == null && !showMessage) {
            return;
        }

        if (mPermissionSnackbar == null) {
            mPermissionSnackbar = Snackbar.make(mCoordinatorLayout, "Missing Permissions", BaseTransientBottomBar.LENGTH_INDEFINITE);
            mPermissionSnackbar.setAction("Request", new PermissionRequestClickListener());
        }
        if (showMessage) {
            mPermissionSnackbar.show();
        } else {
            mPermissionSnackbar.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private class PermissionRequestClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            requirePermission();
        }
    }
}