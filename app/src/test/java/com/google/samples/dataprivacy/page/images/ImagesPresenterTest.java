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

import com.google.samples.dataprivacy.storage.ImagesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class ImagesPresenterTest {


    private ImagesPresenter mPresenter;

    @Mock
    private ImagesRepository mRepository;

    @Mock
    private ImageImporter mImporter;

    @Mock
    private PictureTaker mPictureTaker;

    @Mock
    private ImagesContract.View mView;

    @Mock
    private Bitmap mMockedBitmap;

    @Before
    public void setUp() throws Exception {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        mPresenter = new ImagesPresenter(mRepository, mView);
    }

    @Test
    public void takePhoto() throws Exception {
        mPresenter.setPictureTaker(mPictureTaker);
        mPresenter.openTakePhoto();
        verify(mPictureTaker).takePicture();
    }

    @Test
    public void importImage() throws Exception {
        mPresenter.setImageImporter(mImporter);
        mPresenter.openImportPhoto();

        verify(mImporter).importImage();

    }

    @Test
    public void openImage() throws Exception {
        final String image = "1.png";
        mPresenter.openImage(image);
        verify(mView).showImage(image);

    }

}