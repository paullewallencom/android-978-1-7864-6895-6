/*
 * Copyright 2016 "Henry Tao <hi@henrytao.me>"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.androcid.zomato.view.recyclerview.adapter;


import com.androcid.zomato.view.recyclerview.config.Constants;
import com.androcid.zomato.view.recyclerview.config.Visibility;

/**
 * Created by henrytao on 8/16/15.
 */
public interface MultiStateAdapter {

  void addOnVisibilityChanged(OnVisibilityChangedListener onVisibilityChangedListener);

  @Visibility
  int getVisibility(int position);

  @Visibility
  int getVisibility(int index, Constants.Type type);

  void setVisibility(int position, @Visibility int visibility);

  void setVisibility(int index, @Visibility int visibility, Constants.Type type);

  interface OnVisibilityChangedListener {

    void onVisibilityChanged(MultiStateAdapter adapter, int position, @Visibility int visibility);
  }
}
