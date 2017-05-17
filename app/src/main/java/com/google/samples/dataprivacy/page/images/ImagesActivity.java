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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.samples.dataprivacy.Injection;
import com.google.samples.dataprivacy.R;
import com.google.samples.dataprivacy.page.importimage.ImageImportActivity;
import com.google.samples.dataprivacy.util.ImageUtils;

import java.io.File;
import java.io.IOException;


public class ImagesActivity extends AppCompatActivity implements PictureTaker, ImageImporter {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_IMPORT = 2;

    private static final String TAG = ImagesActivity.class.getSimpleName();

    private ImagesContract.Presenter mPresenter;
    private ImagesContract.View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        ImagesFragment imagesFragment =
                (ImagesFragment) getSupportFragmentManager().findFragmentById(R.id.contentframe);
        if (imagesFragment == null) {
            imagesFragment = new ImagesFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.contentframe, imagesFragment);
            transaction.commit();
        }

        mView = imagesFragment;
        mPresenter = new ImagesPresenter(Injection.getImageRepository(this), mView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.setPictureTaker(this);
        mPresenter.setImageImporter(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.clearPictureTaker();
        mPresenter.clearImageImporter();
    }

    @Override
    public void takePicture() {
        // Fire off a ACTION_IMAGE_CAPTURE intent to launch a camera app.
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Image capture intent result
            onImageCapture(data.getExtras());

        } else if (requestCode == REQUEST_IMAGE_IMPORT
                && resultCode == Activity.RESULT_OK && data != null) {
            // Image import intent result
            // The ACTION_GET_CONTENT Intent returns a URI pointing to the file.
            // It does not return the file itself. Extract the URI
            // from the Intent and process it.
            Uri uri = data.getData();
            importUriImage(uri);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onImageImport(Bundle extras) {
        String path = extras.getString(ImageImportActivity.IMPORT_RESULT);
        if (path != null) {
            Log.d(TAG, "Photo successfully imported.");
            loadImageFromPath(path);
        }
    }

    private void onImageCapture(Bundle extras) {
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        Log.d(TAG, "Photo successfully taken.");
        mPresenter.onPhotoTaken(imageBitmap);
    }

    /**
     * Load the image referenced by the path.
     *
     * @param imagePath
     */
    private void loadImageFromPath(String imagePath) {
        File file = new File(imagePath);

        //TODO - handle file access exceptions, not found etc.
        Bitmap b = ImageUtils.decodeSampledBitmapFromFile(file.getAbsolutePath(), 200, 200);

        mPresenter.onImportImage(b);
    }

    @Override
    public void importImage() {
        // Use an ACTION_GET_CONTENT intent to select a file using the system's
        // file browser.
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // Filter files only to those that can be "opened" and directly accessed
        // as a stream.
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Only show images.
        intent.setType("image/*");

        startActivityForResult(intent, REQUEST_IMAGE_IMPORT);
    }

    /**
     * Uses the {@link MediaStore} content provider to load the {@link Uri}
     * as a Bitmap.
     * Next, notifies the presenter to import this image.
     *
     * @param uri Points to the image to create.
     */
    private void importUriImage(@NonNull Uri uri) {
        try {
            // Use the MediaStore to load the image.
            Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            if (image != null) {
                // Tell the presenter to import this image.
                mPresenter.onImportImage(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error: " + e.getMessage() + "Could not open URI: "
                    + uri.toString());
        }
    }
}