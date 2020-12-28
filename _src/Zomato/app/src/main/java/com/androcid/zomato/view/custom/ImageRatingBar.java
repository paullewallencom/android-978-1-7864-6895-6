package com.androcid.zomato.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.androcid.zomato.R;

public class ImageRatingBar extends View {

    private static final int NO_RATING = 0;
    private static final int MAX_RATE = 9;
    private boolean isSliding;
    private float slidePosition;
    private PointF[] points;
    private float itemWidth;
    private Drawable[] ratingSmiles;
    private Drawable[] defaultSmile;
    private OnRatingSliderChangeListener listener;
    private int currentRating = NO_RATING;
    private int smileWidth, smileHeight;
    private int horizontalSpacing;
    private boolean isEnabled;
    private int rating = NO_RATING;

    public ImageRatingBar(Context context) {
        super(context);
        init();
    }

    public ImageRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ImageRatingBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init() {
        init(null);
    }

    private void init(AttributeSet attrs) {
        isSliding = false;

        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ImageRatingBar, 0, 0);
            try {
                smileWidth = ta.getDimensionPixelSize(R.styleable.ImageRatingBar_smileWidth, 0);
                smileHeight = ta.getDimensionPixelSize(R.styleable.ImageRatingBar_smileHeight, 0);
                horizontalSpacing = ta.getDimensionPixelSize(R.styleable.ImageRatingBar_horizontalSpacing, 0);
                isEnabled = ta.getBoolean(R.styleable.ImageRatingBar_enabled, true);
                rating = ta.getInt(R.styleable.ImageRatingBar_rating, NO_RATING);

                defaultSmile = new Drawable[] {
                        ResourcesCompat.getDrawable(getResources(), R.drawable.rating_gray_1, null),
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_gray_2, null),
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_gray_3, null),
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_gray_4, null),
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_gray_5, null),
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_gray_6, null),
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_gray_7, null),
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_gray_8, null),
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_gray_9, null)
                };
                ratingSmiles = new Drawable[] {
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_1, null),
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_2, null),
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_3, null),
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_4, null),
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_5, null),
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_6, null),
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_7, null),
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_8, null),
                        ResourcesCompat.getDrawable(getResources(),  R.drawable.rating_9, null)
                };

                if (smileWidth == 0)
                    smileWidth = 50;

                if (smileHeight == 0)
                    smileHeight = 50;
            } finally {
                ta.recycle();
            }
        }

        points = new PointF[MAX_RATE];
        for (int i = 0; i < MAX_RATE; i++) {
            points[i] = new PointF();
        }
        if (rating != NO_RATING)
            setRating(rating);
    }

    @Override
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
        super.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Set a listener that will be invoked whenever the users interacts with the SmileBar.
     *
     * @param listener
     *            Listener to set.
     */
    public void setOnRatingSliderChangeListener(OnRatingSliderChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            // Disable all input if the slider is disabled
            return false;
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                isSliding = true;
                slidePosition = getRelativePosition(event.getX());
                rating = (int) Math.ceil(slidePosition);
                if (listener != null && rating != currentRating) {
                    currentRating = rating;
                    listener.onPendingRating(rating);
                }
                break;
            }
            case MotionEvent.ACTION_UP:
                currentRating = NO_RATING;
                if (listener != null)
                    listener.onFinalRating((int) Math.ceil(slidePosition));
                rating = (int) Math.ceil(slidePosition);
                break;
            case MotionEvent.ACTION_CANCEL:
                currentRating = NO_RATING;
                if (listener != null)
                    listener.onCancelRating();
                isSliding = false;
                break;
            default:
                break;
        }

        invalidate();
        return true;
    }

    private float getRelativePosition(float x) {
        float position = x / itemWidth;
        position = Math.max(position, 0);
        return Math.min(position, MAX_RATE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        itemWidth = w / (float) MAX_RATE;
        updatePositions();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = smileWidth * MAX_RATE + horizontalSpacing * (MAX_RATE - 1) +
                getPaddingLeft() + getPaddingRight();
        int height = smileHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < MAX_RATE; i++) {
            PointF pos = points[i];
            canvas.save();
            canvas.translate(pos.x, pos.y);
            drawSmile(canvas, i);
            canvas.restore();
        }
    }

    private void drawSmile(Canvas canvas, int position) {

        // Draw the rated smile
        if (isSliding && position <= slidePosition) {
            Drawable[] smiles = ratingSmiles;

            int rating = (int) Math.ceil(slidePosition);
            int smileIndex = rating - 1;
            if (rating > 0)
                drawSmile(canvas, smiles[position]);
            else
                drawSmile(canvas, defaultSmile[position]);
        }
        else {
            // Draw the default smile
            drawSmile(canvas, defaultSmile[position]);
        }
    }

    private void drawSmile(Canvas canvas, Drawable smile) {
        canvas.save();
        canvas.translate(-smileWidth / 2, -smileHeight / 2);
        smile.setBounds(0, 0, smileWidth, smileHeight);
        smile.draw(canvas);
        canvas.restore();
    }

    private void updatePositions() {
        float left = 0;
        for (int i = 0; i < MAX_RATE; i++) {
            float posY = getHeight() / 2;
            float posX = left + smileWidth / 2;
            left += smileWidth;
            if (i > 0) {
                posX += horizontalSpacing;
                left += horizontalSpacing;
            } else {
                posX += getPaddingLeft();
                left += getPaddingLeft();
            }

            points[i].set(posX, posY);

        }
    }

    public void setRating(int rating) {
        if (rating < 0 || rating > MAX_RATE)
            throw new IndexOutOfBoundsException("Rating must be between 0 and " + MAX_RATE);

        this.rating = rating;
        slidePosition = (float) (rating - 0.1);
        isSliding = true;
        invalidate();
        if (listener != null)
            listener.onFinalRating(rating);
    }

    public int getRating() {
        return rating;
    }

    /**
     * A callback that notifies clients when the user starts rating, changes the rating
     * value and when the rating has ended.
     */
    public interface OnRatingSliderChangeListener {

        /**
         * Notification that the user has moved over to a different rating value.
         * The rating value is only temporary and might change again before the
         * rating is finalized.
         *
         * @param rating
         *            the pending rating. A value between 0 and 5.
         */
        void onPendingRating(int rating);

        /**
         * Notification that the user has selected a final rating.
         *
         * @param rating
         *            the final rating selected. A value between 0 and 5.
         */
        void onFinalRating(int rating);

        /**
         * Notification that the user has canceled the rating.
         */
        void onCancelRating();
    }
}
