package com.androcid.zomato.view.appbarlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androcid.zomato.R;

/**
 * Created by Androcid on 9/24/15.
 */
public class SmoothCollapsingToolbarLayout extends LinearLayout {

  public static boolean DEBUG = false;

  private static void log(String s, Object... args) {
    if (DEBUG) {
      Log.d("debug", String.format(s, args));
    }
  }

  protected int mAvatarId;

  protected float mCollapsedAvatarSize;

  protected float mCollapsedOffsetX;

  protected float mCollapsedOffsetY;

  protected float mCollapsedSubTitleTextSize;

  protected float mCollapsedTitleTextSize;

  protected float mCurrentRatio;

  protected float mExpandedAvatarSize;

  protected float mExpandedOffsetX;

  protected float mExpandedOffsetY;

  protected float mExpandedSubtitleTextSize;

  protected float mExpandedTitleTextSize;

  protected AppBarLayout.OnOffsetChangedListener mOnAppBarLayoutOffsetChangedListener;

  protected OnOffsetChangedListener mOnOffsetChangedListener;

  protected int mSubtitleId;

  protected int mTitleId;

  protected AppBarLayout vAppBarLayout;

  protected View vAvatar;

  protected CollapsingToolbarLayout vCollapsingToolbarLayout;

  protected TextView vSubtitle;

  protected TextView vTitle;

  protected Toolbar vToolbar;

  public SmoothCollapsingToolbarLayout(Context context) {
    super(context);
    init(null);
  }

  public SmoothCollapsingToolbarLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public SmoothCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public SmoothCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(attrs);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    initViews();
    if (!isInEditMode()) {
      mOnAppBarLayoutOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
          SmoothCollapsingToolbarLayout.this.onOffsetChanged(appBarLayout, i);
        }
      };
      getAppBarLayout().addOnOffsetChangedListener(mOnAppBarLayoutOffsetChangedListener);
    }
  }

  @Override
  protected void onDetachedFromWindow() {
    if (mOnAppBarLayoutOffsetChangedListener != null) {
      getAppBarLayout().removeOnOffsetChangedListener(mOnAppBarLayoutOffsetChangedListener);
    }
    super.onDetachedFromWindow();
  }

  public void setCollapsedAvatarSize(float collapsedAvatarSize) {
    mCollapsedAvatarSize = collapsedAvatarSize;
    updateViews();
  }

  public void setCollapsedOffsetX(float collapsedOffsetX) {
    mCollapsedOffsetX = collapsedOffsetX;
    updateViews();
  }

  public void setCollapsedOffsetY(float collapsedOffsetY) {
    mCollapsedOffsetY = collapsedOffsetY;
    updateViews();
  }

  public void setCollapsedSubTitleTextSize(float collapsedSubTitleTextSize) {
    mCollapsedSubTitleTextSize = collapsedSubTitleTextSize;
    updateViews();
  }

  public void setCollapsedTitleTextSize(float collapsedTitleTextSize) {
    mCollapsedTitleTextSize = collapsedTitleTextSize;
    updateViews();
  }

  public void setExpandedAvatarSize(float expandedAvatarSize) {
    mExpandedAvatarSize = expandedAvatarSize;
    updateViews();
  }

  public void setExpandedOffsetX(float expandedOffsetX) {
    mExpandedOffsetX = expandedOffsetX;
    updateViews();
  }

  public void setExpandedOffsetY(float expandedOffsetY) {
    mExpandedOffsetY = expandedOffsetY;
    updateViews();
  }

  public void setExpandedSubtitleTextSize(float expandedSubtitleTextSize) {
    mExpandedSubtitleTextSize = expandedSubtitleTextSize;
    updateViews();
  }

  public void setExpandedTitleTextSize(float expandedTitleTextSize) {
    mExpandedTitleTextSize = expandedTitleTextSize;
    updateViews();
  }

  public void setOnOffsetChangedListener(OnOffsetChangedListener onOffsetChangedListener) {
    mOnOffsetChangedListener = onOffsetChangedListener;
  }

  protected AppBarLayout getAppBarLayout() {
    if (vAppBarLayout == null) {
      if (getParent() instanceof CollapsingToolbarLayout && getParent().getParent() instanceof AppBarLayout) {
        vAppBarLayout = (AppBarLayout) getParent().getParent();
      } else {
        throw new IllegalStateException("Must be inside a CollapsingToolbarLayout and AppBarLayout");
      }
    }
    return vAppBarLayout;
  }

  protected CollapsingToolbarLayout getCollapsingToolbarLayout() {
    if (vCollapsingToolbarLayout == null) {
      if (getParent() instanceof CollapsingToolbarLayout) {
        vCollapsingToolbarLayout = (CollapsingToolbarLayout) getParent();
      } else {
        throw new IllegalStateException("Must be inside a CollapsingToolbarLayout");
      }
    }
    return vCollapsingToolbarLayout;
  }

  protected Toolbar getToolbar() {
    if (vToolbar == null) {
      int i = 0;
      ViewGroup parent = (ViewGroup) getParent();
      View child;
      for (int z = parent.getChildCount(); i < z; i++) {
        child = parent.getChildAt(i);
        if (child instanceof Toolbar) {
          vToolbar = (Toolbar) child;
          break;
        }
      }
      if (vToolbar == null) {
        throw new IllegalStateException("Must have Toolbar");
      }
    }
    return vToolbar;
  }

  protected float getTranslationOffset(float expandedOffset, float collapsedOffset, float ratio) {
    return expandedOffset + ratio * (collapsedOffset - expandedOffset);
  }

  protected void init(AttributeSet attrs) {
    setOrientation(HORIZONTAL);
    TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SmoothCollapsingToolbarLayout, 0, 0);
    try {
      mCollapsedOffsetX = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_collapsed_offsetX, 0);
      mCollapsedOffsetY = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_collapsed_offsetY, 0);
      mCollapsedAvatarSize = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_collapsed_avatarSize, -1);
      mCollapsedTitleTextSize = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_collapsed_titleTextSize, -1);
      mCollapsedSubTitleTextSize = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_collapsed_subtitleTextSize, -1);

      mExpandedOffsetX = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_expanded_offsetX, 0);
      mExpandedOffsetY = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_expanded_offsetY, 0);
      mExpandedAvatarSize = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_expanded_avatarSize, -1);
      mExpandedTitleTextSize = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_expanded_titleTextSize, -1);
      mExpandedSubtitleTextSize = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_expanded_subtitleTextSize, -1);

      mAvatarId = a.getResourceId(R.styleable.SmoothCollapsingToolbarLayout_sctl_avatar_id, 0);
      mTitleId = a.getResourceId(R.styleable.SmoothCollapsingToolbarLayout_sctl_title_id, 0);
      mSubtitleId = a.getResourceId(R.styleable.SmoothCollapsingToolbarLayout_sctl_subtitle_id, 0);
    } finally {
      a.recycle();
    }
  }

  protected void initViews() {
    updateViews();
    if (mAvatarId > 0) {
      vAvatar = findViewById(mAvatarId);
    }
    if (mTitleId > 0) {
      vTitle = (TextView) findViewById(mTitleId);
    }
    if (mSubtitleId > 0) {
      vSubtitle = (TextView) findViewById(mSubtitleId);
    }
  }

  protected boolean isAvatarSizeEnabled() {
    return vAvatar != null && mCollapsedAvatarSize > 0 && mExpandedAvatarSize > 0;
  }

  protected boolean isSubtitleTextSizeEnabled() {
    return vSubtitle != null && mCollapsedSubTitleTextSize > 0 && mExpandedSubtitleTextSize > 0;
  }

  protected boolean isTitleTextSizeEnabled() {
    return vTitle != null && mCollapsedTitleTextSize > 0 && mExpandedTitleTextSize > 0;
  }

  protected void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    int maxOffset = getAppBarLayout().getMeasuredHeight() - getToolbar().getMeasuredHeight();
    float ratio = Math.min(Math.abs(verticalOffset) * 1.0f / maxOffset, 1.0f);
    updateViews(ratio);
    log("test onOffsetChanged collapsing | %d | %f", verticalOffset, ratio);
  }

  protected void updateViews() {
    updateViews(mCurrentRatio);
  }

  protected void updateViews(float ratio) {
    mCurrentRatio = ratio;
    int startOffsetX = 0;
    int startOffsetY = getAppBarLayout().getMeasuredHeight() - getMeasuredHeight();
    ViewCompat.setTranslationX(this, startOffsetX + getTranslationOffset(mExpandedOffsetX, mCollapsedOffsetX, ratio));
    ViewCompat.setTranslationY(this, startOffsetY - getTranslationOffset(mExpandedOffsetY, mCollapsedOffsetY, ratio));
    if (isAvatarSizeEnabled()) {
      ViewGroup.LayoutParams params = vAvatar.getLayoutParams();
      params.height = params.width = (int) getTranslationOffset(mExpandedAvatarSize, mCollapsedAvatarSize, ratio);
    }
    if (isTitleTextSizeEnabled()) {
      vTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTranslationOffset(mExpandedTitleTextSize, mCollapsedTitleTextSize, ratio));
    }
    if (isSubtitleTextSizeEnabled()) {
      vSubtitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTranslationOffset(mExpandedSubtitleTextSize, mCollapsedSubTitleTextSize, ratio));
    }
    if (mOnOffsetChangedListener != null) {
      mOnOffsetChangedListener.onOffsetChanged(ratio);
    }
    log("test updateViews | %d | %f", (int) (mExpandedAvatarSize + ratio * (mCollapsedAvatarSize - mExpandedAvatarSize)), ratio);
  }

  public interface OnOffsetChangedListener {

    void onOffsetChanged(float ratio);
  }
}
