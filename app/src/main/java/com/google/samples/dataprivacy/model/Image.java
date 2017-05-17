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

package com.google.samples.dataprivacy.model;


import android.graphics.Bitmap;

/**
 * An image conists of a {@link Bitmap} and a filename.
 */
public class Image {
    private Bitmap bitmap;
    private String source;

    public Image(String source, Bitmap bitmap) {
        this.source = source;
        this.bitmap = bitmap;
    }

    public Bitmap getImage() {
        return bitmap;
    }

    public String getSource() {
        return source;
    }
}
