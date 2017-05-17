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
import com.google.samples.dataprivacy.storage.ImagesRepository;

import java.util.List;

public class ImagesPresenter implements ImagesContract.Presenter {

    private ImagesContract.View mView;
    private ImagesRepository mImagesRepository;
    private PictureTaker mPictureTaker;
    private ImageImporter mImageImporter;

    public ImagesPresenter(ImagesRepository imagesRepository, ImagesContract.View imagesView) {
        mImagesRepository = imagesRepository;
        mView = imagesView;

        mView.setPresenter(this);
    }

    @Override
    public void start() {
        showImages();
    }

    private void showImages() {
        List<Image> images = mImagesRepository.getImages();
        if (images == null || images.isEmpty()) {
            mView.showNoImages();
        } else {
            mView.showImages(images);
        }
    }

    @Override
    public void openTakePhoto() {
        // mView.showTakePhotoUi();
        if (mPictureTaker == null) {
            return;
        }
        mPictureTaker.takePicture();
    }

    @Override
    public void openImportPhoto() {
        if (mImageImporter == null) {
            return;
        }
        mImageImporter.importImage();
    }

    @Override
    public void openImage(String path) {
        mView.showImage(path);
    }

    @Override
    public void onImportImage(Bitmap image) {
        mImagesRepository.saveImage(image);
        showImages();
    }

    @Override
    public void onPhotoTaken(Bitmap image) {
        mImagesRepository.saveImage(image);
        showImages();
    }


    @Override
    public void setPictureTaker(PictureTaker pictureTaker) {
        mPictureTaker = pictureTaker;
    }

    @Override
    public void clearPictureTaker() {
        mPictureTaker = null;
    }

    @Override
    public void setImageImporter(ImageImporter imageImporter) {
        mImageImporter = imageImporter;
    }

    @Override
    public void clearImageImporter() {
        mImageImporter = null;
    }

    @Override
    public void onPermissionRequestResult(boolean isGranted) {
        if (isGranted) {
            mView.showMissingPermissions(false);
            showImages();
        } else {
            mView.showMissingPermissions(true);
        }
    }
}
