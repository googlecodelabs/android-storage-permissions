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

package com.google.samples.dataprivacy.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.google.samples.dataprivacy.model.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Repository of images that stores data on the device. The initial implementation is using
 * the external storage directory (see {@link Environment#getExternalStorageDirectory()}.
 */
public class LocalImagesRepository implements ImagesRepository {

    private static final String TAG = "LocalImagesRepository";
    private static final String PATH = "secureimages/";

    private File mStorage;

    public LocalImagesRepository(Context context) {
        File externalStorage = Environment.getExternalStorageDirectory();
        mStorage = new File(externalStorage, PATH);

        if (!mStorage.exists()) {
            if (!mStorage.mkdirs()) {
                Log.e(TAG, "Could not create storage directory: " + mStorage.getAbsolutePath());

            }
        }
    }

    /**
     * Generates a file name for the png image and stores it in local storage.
     *
     * @param image The bitmap to store.
     * @return The name of the image file.
     */
    @Override
    public String saveImage(Bitmap image) {
        final String fileName = UUID.randomUUID().toString() + ".png";
        File file = new File(mStorage, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            image.compress(Bitmap.CompressFormat.PNG, 85, fos);
        } catch (IOException e) {
            Log.e(TAG, "Error during saving of image: " + e.getMessage());
            return null;
        }

        return fileName;
    }

    /**
     * Deletes the given image.
     *
     * @param fileName Filename of the image to delete.
     */
    @Override
    public void deleteImage(String fileName) {
        File file = new File(fileName);
        if (!file.delete()) {
            Log.e(TAG, "File could not be deleted: " + fileName);
        }
    }

    /**
     * Returns a list of all images stored in this repository.
     * An {@link Image} contains a {@link Bitmap} and a string with its filename.
     *
     * @return
     */
    @Override
    public List<Image> getImages() {
        File[] files = mStorage.listFiles();
        if (files == null) {
            Log.e(TAG, "Could not list files.");
            return null;
        }
        ArrayList<Image> list = new ArrayList<>(files.length);
        for (File f : files) {
            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            list.add(new Image(f.getAbsolutePath(), bitmap));
        }
        return list;
    }

    /**
     * Loads the given file as a bitmap.
     */
    @Override
    public Bitmap getImage(String path) {
        File file = new File(path);
        if (!file.exists()) {
            Log.e(TAG, "File could not opened. It does not exist: " + path);

            return null;
        }

        return BitmapFactory.decodeFile(file.getAbsolutePath());

    }
}
