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

/**
 * Contract for the "image viwer" page that displays an image. It includes operations that can
 * be made on the loaded image, such as deleting or sharing it.
 */
public interface ImageViewerContract {

    interface View {
        void setPresenter(Presenter presenter);

        void displayImage(Bitmap image);

        void displayImageDeleted();

    }

    interface Presenter {

        void start();

        void showImage();

        void deleteImage();

        void shareImage();
    }
}
