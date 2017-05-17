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
import android.support.annotation.NonNull;

import com.google.samples.dataprivacy.storage.ImagesRepository;

public class ImageViewerPresenter implements ImageViewerContract.Presenter {

    private final String mImage;
    private ImageViewerContract.View mView;
    private ImagesRepository mImagesRepository;
    private ImageSharer mImageSharer;


    public ImageViewerPresenter(@NonNull ImageViewerContract.View view, @NonNull ImagesRepository repository, @NonNull ImageSharer imageSharer, @NonNull String image) {
        this.mView = view;
        this.mImagesRepository = repository;
        this.mImageSharer = imageSharer;
        this.mImage = image;

        view.setPresenter(this);
    }

    @Override
    public void start() {
        showImage();
    }

    @Override
    public void showImage() {
        Bitmap bitmap = mImagesRepository.getImage(mImage);
        mView.displayImage(bitmap);
    }

    @Override
    public void deleteImage() {
        mImagesRepository.deleteImage(mImage);
        mView.displayImageDeleted();
    }

    @Override
    public void shareImage() {
        mImageSharer.shareImage(mImage);
    }
}
