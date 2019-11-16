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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;

import com.google.samples.dataprivacy.Injection;
import com.google.samples.dataprivacy.R;

import java.io.File;

public class ImageViewerActivity extends AppCompatActivity implements ImageSharer {

    public static final String ARGUMENT_IMAGE_PATH = "ARGUMENT_IMAGE_PATH";
    private ImageViewerFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        ImageViewerFragment fragment = (ImageViewerFragment) getSupportFragmentManager().findFragmentById(R.id.contentframe);
        String image = getIntent().getStringExtra(ARGUMENT_IMAGE_PATH);
        if (fragment == null) {

            fragment = ImageViewerFragment.newInstance();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.contentframe, fragment);
            transaction.commit();
        }

        new ImageViewerPresenter(fragment, Injection.getImageRepository(this), this, image);

    }

    /**
     * Share an image identified by its absolute path.
     *
     * @param path The absolute path to the image.
     */
    @Override
    public void shareImage(String path) {
        Toast.makeText(this, "TODO: implement sharing.", Toast.LENGTH_SHORT).show();
    }
}
