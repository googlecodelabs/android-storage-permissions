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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.samples.dataprivacy.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * A RecyclerView adapter that displays {@link Image}s. Includes a callback for when an item has
 * been clicked.
 */
public class ImagesAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    private List<Image> mImages = new ArrayList<>(0);
    private OnImageItemClickListener mListener;

    public ImagesAdapter() {
    }

    public void setImages(List<Image> images) {
        mImages = images;
        notifyDataSetChanged();
    }

    public void setOnImageClickListener(OnImageItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ImageViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        holder.bind(mImages.get(position));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener == null) {
                    return;
                }
                mListener.onImageClick(mImages.get(position).getSource());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImages.size();


    }

    public interface OnImageItemClickListener {
        void onImageClick(String path);
    }
}
