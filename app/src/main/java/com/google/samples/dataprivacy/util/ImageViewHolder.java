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

package com.google.samples.dataprivacy.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.samples.dataprivacy.R;
import com.google.samples.dataprivacy.model.Image;


public class ImageViewHolder extends RecyclerView.ViewHolder {

    protected ImageView imageView;

    public ImageViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image);
    }

    public static ImageViewHolder newInstance(@NonNull ViewGroup parent) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_card, parent, false));
    }

    public void bind(Image image) {

        imageView.setImageBitmap(image.getImage());
        imageView.setTag(image.getSource());

    }
}
