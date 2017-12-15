package com.example.administrator.typewritetextviewdemo;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 模拟打字机效果
 */
@SuppressLint("AppCompatCustomView")
public class TypeWriteTextView extends TextView {
    /**
     * 打字间隔
     */
    private static final int TYPE_TIME_INTERVAL = 100;
    private static final String KEY_TEXT = "KEY_TEXT";
    private static final String DEFAULT_DATA = "DEFAULT_DATA";


    private MediaPlayer mMediaPlayer;
    private OnTypeListener mTypeListener;
    private ValueAnimator mAnimatorTypeText;
    private int mTextLength;
    private String mText;

    public TypeWriteTextView(Context context) {
        this(context, null);
    }

    public TypeWriteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public TypeWriteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        release();
        mAnimatorTypeText = new ValueAnimator();
        mAnimatorTypeText.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                //value的值会重复所以要加判断，避免文字重复打印
                if (getText().length() - 1 < value) {
                    if (value == 0) {
                        if (mTypeListener != null) {
                            mTypeListener.onTypeStart();
                        }
                    }
                    startTypeAudio();
                    append(mText.substring(value, value + 1));
                }
                if (value >= mTextLength - 1) {
                    if (mTypeListener != null) {
                        mTypeListener.onTypeStop();
                    }
                    release();

                }
            }
        });
        try {
            stopTypeAudio();
            mMediaPlayer = MediaPlayer.create(getContext(), R.raw.type_in);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setTypeListener(OnTypeListener typeListener) {
        mTypeListener = typeListener;
    }

    public void bindText(String text) {
        setText("");
        initView();
        mTextLength = text.length();
        mText = text;
        mAnimatorTypeText.setIntValues(0, mTextLength - 1);
        mAnimatorTypeText.setDuration(mTextLength * TYPE_TIME_INTERVAL);
        mAnimatorTypeText.start();

    }

    /**
     * 播放打字声音
     */

    private void startTypeAudio() {
        if (mMediaPlayer != null) {
            stopTypeAudio();
            mMediaPlayer.start();
        }
    }


    /**
     * 停止播放打字声音
     */
    private void stopTypeAudio() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mAnimatorTypeText != null && mAnimatorTypeText.isRunning()) {
            mAnimatorTypeText.cancel();
            mAnimatorTypeText = null;
        }
    }
    /**
     * 切换程序或者按下home键直接setText()
     */
    @Override
    public Parcelable onSaveInstanceState() {
        release();
        Bundle bundle = new Bundle();
        Parcelable superData = super.onSaveInstanceState();
        bundle.putParcelable(DEFAULT_DATA, superData);
        bundle.putString(KEY_TEXT, mText);
        setText(mText);
        return bundle;
    }

    /**
     * 后台杀死回复
     * @param state
     */
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        mText = bundle.getString(KEY_TEXT);
        setText(mText);
        Parcelable superData = bundle.getParcelable(DEFAULT_DATA);
        super.onRestoreInstanceState(superData);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        release();
    }

    public interface OnTypeListener {
        public void onTypeStart();

        public void onTypeStop();
    }
}
