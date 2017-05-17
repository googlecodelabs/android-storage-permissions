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

import android.graphics.Bitmap;

import com.google.samples.dataprivacy.model.Image;

import java.util.List;

/**
 * Contract that describes the "images" page that displays a list of all images stored within
 * the app.
 * It includes options to trigger taking a photo, viewing a photo
 * (see  {@link com.google.samples.dataprivacy.page.viewimage.ImageViewerContract}) and sharing an
 * image.
 */
public interface ImagesContract {
    interface View {
        void setPresenter(Presenter presenter);

        /**
         * Display the list of images on screen.
         */
        void showImages(List<Image> images);

        /**
         * Display a message that no images can be displayed.
         */
        void showNoImages();

        /**
         * Open the image viewer page for an image.
         */
        void showImage(String Path);

        /**
         * Request all necessary permissions required for running the app.
         */
        void requestPermissions();

        /**
         * Display an error message explaining that required permissions are missing.
         */
        void showMissingPermissions(boolean showMessage);

    }

    interface Presenter {

        /**
         * Start operation. Should be called to start the presenter.
         */
        void start();

        /**
         * The 'take photo' option has been triggered. Open the take photo screen.
         */
        void openTakePhoto();

        /**
         * The 'import photo' option has been triggered. Open the import photo screen.
         */
        void openImportPhoto();

        /**
         * An image has been selected to be viewed. Opens the image viwer page.
         */
        void openImage(String path);

        /**
         * An image has been selected for import. Stores this {@link Bitmap} in the repository.
         */
        void onImportImage(Bitmap image);

        /**
         * A photo has been taken. Store this {@link Bitmap} in the repository.
         */
        void onPhotoTaken(Bitmap image);

        /**
         * A result for a permission request has been received.
         * @param isGranted True if all required permissions have been granted. False otherwise.
         */
        void onPermissionRequestResult(boolean isGranted);

        /**
         * Sets the picture taker that can open the take photo screen.
         */
        void setPictureTaker(PictureTaker pictureTaker);

        /**
         * Clears the photo taker. See {@link #setPictureTaker(PictureTaker)}.
         */
        void clearPictureTaker();

        /**
         * Sets the image importer that opens the import image screen.
         */
        void setImageImporter(ImageImporter imageImporter);

        /**
         * Clears the image importer. See {@link #setImageImporter(ImageImporter)}.
         */
        void clearImageImporter();
    }

}
