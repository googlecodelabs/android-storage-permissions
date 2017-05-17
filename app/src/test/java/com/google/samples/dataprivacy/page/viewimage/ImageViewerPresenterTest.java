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

import com.google.samples.dataprivacy.storage.ImagesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

public class ImageViewerPresenterTest {

    private ImageViewerPresenter mPresenter;

    @Mock
    private ImagesRepository mRepository;

    @Mock
    private ImageSharer mSharer;

    @Mock
    private ImageViewerContract.View mView;


    @Before
    public void setUp() throws Exception {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        mPresenter = new ImageViewerPresenter(mView, mRepository, mSharer, "1.png");
    }

    @Test
    public void openImage() throws Exception {
        final String image = "1.png";

        mPresenter.showImage();
        verify(mView).displayImage(any(Bitmap.class));
    }

    @Test
    public void deleteImage() throws Exception {
        mPresenter.deleteImage();
        verify(mRepository).deleteImage(anyString());
    }

    @Test
    public void shareImage() throws Exception {
        mPresenter.shareImage();
        verify(mSharer).shareImage(anyString());
    }

}