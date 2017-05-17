rem Copyright 2017 Google Inc.
rem
rem Licensed under the Apache License, Version 2.0 (the "License");
rem you may not use this file except in compliance with the License.
rem You may obtain a copy of the License at
rem
rem     http://www.apache.org/licenses/LICENSE-2.0
rem
rem Unless required by applicable law or agreed to in writing, software
rem distributed under the License is distributed on an "AS IS" BASIS,
rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
rem See the License for the specific language governing permissions and
rem limitations under the License.

rem Trigger a download of 4 files from a server.
adb shell am start -a android.intent.action.VIEW -d https://storage.googleapis.com/network-security-conf-codelab.appspot.com/download-images/1.png
adb shell am start -a android.intent.action.VIEW -d https://storage.googleapis.com/network-security-conf-codelab.appspot.com/download-images/2.png
adb shell am start -a android.intent.action.VIEW -d https://storage.googleapis.com/network-security-conf-codelab.appspot.com/download-images/3.png
adb shell am start -a android.intent.action.VIEW -d https://storage.googleapis.com/network-security-conf-codelab.appspot.com/download-images/4.png
