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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by henrytao on 5/30/16.
 */
public class ViewTypeManager {

  private Map<String, Integer> mCache = new HashMap<>();

  private Map<Integer, Pointer> mCacheReverse = new HashMap<>();

  private int mCode = 0;

  public Pointer decode(int code) {
    if (!mCacheReverse.containsKey(code)) {
      return new Pointer(0, 0);
    }
    return mCacheReverse.get(code);
  }

  public int encode(int type, int index) {
    String key = String.format(Locale.US, "%d-%d", type, index);
    int code;
    if (!mCache.containsKey(key)) {
      code = getCode();
      mCache.put(key, code);
      mCacheReverse.put(code, new Pointer(type, index));
    } else {
      code = mCache.get(key);
    }
    return code;
  }

  private int getCode() {
    return ++mCode;
  }

  public static class Pointer {

    private final int mIndex;

    private final int mType;

    public Pointer(int type, int index) {
      mType = type;
      mIndex = index;
    }

    public int getIndex() {
      return mIndex;
    }

    public int getType() {
      return mType;
    }
  }
}
