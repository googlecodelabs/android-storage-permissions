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


import android.graphics.Bitmap;

import com.google.samples.dataprivacy.model.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * In Memory implementation of an ImagesRepository that stores
 * all data only in memory.
 */
public class InMemoryImagesRepository implements ImagesRepository {

    private HashMap<String, Bitmap> mImageMap = new HashMap<>();

    @Override
    public String saveImage(Bitmap image) {
        final String key = UUID.randomUUID().toString() + ".png";
        mImageMap.put(key, image);

        return key;
    }

    @Override
    public void deleteImage(String path) {
        mImageMap.remove(path);
    }

    @Override
    public List<Image> getImages() {
        Iterator<Map.Entry<String, Bitmap>> iterator = mImageMap.entrySet().iterator();
        ArrayList<Image> images = new ArrayList<>(mImageMap.size());
        while (iterator.hasNext()) {
            Map.Entry<String, Bitmap> entry = iterator.next();
            images.add(new Image(entry.getKey(), entry.getValue()));
        }

        return images;
    }

    @Override
    public Bitmap getImage(String path) {
        return mImageMap.get(path);
    }
}
