/*
 * Copyright (C) 2012 Keiji Ariyama
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

package jp.co.c_lis.sample.pulltorefresh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

/**
 * オーバースクロールするListView.
 * 
 * @author keiji_ariyama
 */
public class OverScrollListView extends ListView {
    private static final String LOG_TAG = "OverScrollListView";
    private static final boolean DEBUG_FLG = true;

    /**
     * コンストラクタ.
     * 
     * @param context
     */
    public OverScrollListView(Context context) {
        super(context);
    }

    /**
     * コンストラクタ.
     * 
     * @param context
     * @param attrs
     */
    public OverScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * コンストラクタ.
     * 
     * @param context
     * @param attrs
     * @param defStyle
     */
    public OverScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /** オーバースクロールする最大値 */
    private int mMaxOverscrollDistance = 250;

    /**
     * オーバースクロールの最大値を設定.
     * 
     * @param overscrollDistance
     */
    public void setMaxOverscrollDistance(int overscrollDistance) {
        mMaxOverscrollDistance = overscrollDistance;
    }

    /**
     * オーバースクロールの最大値を取得.
     * 
     * @return
     */
    public int getMaxOverscrollDistance() {
        return mMaxOverscrollDistance;
    }

    private OnOverscrollListener mListener = null;

    /**
     * リスナを設定.
     * 
     * @param l
     */
    public void setListener(OnOverscrollListener l) {
        mListener = l;
    }

    /** 上方向のオーバースクロールであることを示す定数 */
    public static final int UP = 0x1;

    /** 下方向のオーバースクロールであることを示す定数 */
    public static final int DOWN = 0x2;

    /*
     * (non-Javadoc)
     * @see android.view.View#overScrollBy(int, int, int, int, int, int, int,
     * int, boolean)
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
            int scrollRangeX, int scrollRangeY, int maxOverscrollX, int maxOverscrollY,
            boolean isTouchEvent) {

        if (DEBUG_FLG) {
            Log.d(LOG_TAG, String.format("%d:%d, %d:%d, %d:%d, %d:%d",
                    deltaX, deltaY,
                    scrollX, scrollY,
                    scrollRangeX, scrollRangeY,
                    maxOverscrollX, maxOverscrollY));
        }

        if (mListener != null) {
            // オーバースクロールの方向
            int overScrollDirection = (scrollY < 0) ? UP : DOWN;
            int overScrollDistance = Math.abs(scrollY);

            mListener.onOverScrollBy(overScrollDirection, overScrollDistance,
                    mMaxOverscrollDistance);
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
                maxOverscrollX, mMaxOverscrollDistance, isTouchEvent);
    }

    /**
     * オーバースクロールイベントのリスナインターフェース.
     */
    public interface OnOverscrollListener {
        public void onOverScrollBy(int direction, int distance, int max);
    }
}
