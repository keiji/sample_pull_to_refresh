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

package jp.co.c_lis.sample.pulltorefresh;

import jp.co.c_lis.sample.pulltorefresh.view.OverScrollListView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * pull-to-refreshのサンプル実装.
 * 
 * @author keiji_ariyama
 */
public class MainActivity extends Activity implements OverScrollListView.OnOverscrollListener {
    private static final String LOG_TAG = "MainActivity";
    private static final boolean DEBUG_FLG = true;

    private TextView mHeader = null;
    private TextView mFooter = null;
    private OverScrollListView mListView = null;

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHeader = (TextView) findViewById(R.id.tv_header);
        mFooter = (TextView) findViewById(R.id.tv_footer);

        mListView = (OverScrollListView) findViewById(R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setListener(this);
    }

    /**
     * リストに表示する内容を定義.
     */
    private BaseAdapter mAdapter = new BaseAdapter() {

        // 表示するデータ
        private final String[] array = new String[] {
                "red", "blue", "orange", "pink", "gray", "green", "yellow", "brown", "black",
                "white", "cyan", "magenta",
        };

        @Override
        public int getCount() {
            return array.length;
        }

        @Override
        public Object getItem(int position) {
            return array[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = (TextView) View.inflate(MainActivity.this, R.layout.list_row, null);
            tv.setText((String) getItem(position));

            return tv;
        }

    };

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    /*
     * (non-Javadoc)
     * @see
     * jp.co.c_lis.sample.pulltorefresh.view.OverScrollListView.OnOverScrollListener
     * #onOverScrollBy(int, int, int)
     */
    @Override
    public void onOverScrollBy(int direction, int distance, int maxDistance) {

        int l = 0;
        int t = 0;
        int r = 0;
        int b = 0;

        // オーバースクロールしている割合(%)
        int overscrollParcent = 0;
        if (maxDistance != 0) {
            overscrollParcent = distance * 100 / maxDistance;
        }

        TextView target = null;
        if (direction == OverScrollListView.UP) {
            target = mHeader;
            t = target.getTop();
            b = t + distance;

            // 一定以上オーバースクロールしていれば、表示を変更
            if (overscrollParcent > 50) {
                target.setText("refresh");
            } else {
                target.setText("header");
            }
        } else if (direction == OverScrollListView.DOWN) {
            target = mFooter;
            b = target.getBottom();
            t = b - distance;

            // 一定以上オーバースクロールしていれば、表示を変更
            if (overscrollParcent > 50) {
                target.setText("next");
            } else {
                target.setText("footer");
            }
        }

        l = target.getLeft();
        r = target.getRight();

        // 配置を変更
        target.layout(l, t, r, b);
        target.invalidate();
    }
}
