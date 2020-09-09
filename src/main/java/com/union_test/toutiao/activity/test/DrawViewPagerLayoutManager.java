package com.union_test.toutiao.activity.test;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dingmouren.layoutmanagergroup.viewpager.OnViewPagerListener;

public class DrawViewPagerLayoutManager extends LinearLayoutManager {
    
    private static final String TAG = "DrawViewPagerLayoutManager";
    private PagerSnapHelper mPagerSnapHelper;
//    private OnViewPagerListener mOnViewPagerListener;
    private RecyclerView mRecyclerView;
    private int mDrift;
    private RecyclerView.OnChildAttachStateChangeListener mChildAttachStateChangeListener;

    public DrawViewPagerLayoutManager(Context context, int orientation) {
        super(context, orientation, false);
        this.mChildAttachStateChangeListener = new NamelessClass_1();
        this.init();
    }

    public DrawViewPagerLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);



        this.mChildAttachStateChangeListener = new NamelessClass_1();
        this.init();
    }

    class NamelessClass_1 implements RecyclerView.OnChildAttachStateChangeListener {
        NamelessClass_1() {
        }

        public void onChildViewAttachedToWindow(View view) {
//                if (DrawViewPagerLayoutManager.this.mOnViewPagerListener != null && DrawViewPagerLayoutManager.this.getChildCount() == 1) {
//                    DrawViewPagerLayoutManager.this.mOnViewPagerListener.onInitComplete();
//                }

        }

        public void onChildViewDetachedFromWindow(View view) {
                /*if (DrawViewPagerLayoutManager.this.mDrift >= 0) {
                    if (DrawViewPagerLayoutManager.this.mOnViewPagerListener != null) {
                        DrawViewPagerLayoutManager.this.mOnViewPagerListener.onPageRelease(true, DrawViewPagerLayoutManager.this.getPosition(view));
                    }
                } else if (DrawViewPagerLayoutManager.this.mOnViewPagerListener != null) {
                    DrawViewPagerLayoutManager.this.mOnViewPagerListener.onPageRelease(false, DrawViewPagerLayoutManager.this.getPosition(view));
                }*/

        }
    }



    private void init() {
        this.mPagerSnapHelper = new PagerSnapHelper();
    }

    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        this.mPagerSnapHelper.attachToRecyclerView(view);
        this.mRecyclerView = view;
        this.mRecyclerView.addOnChildAttachStateChangeListener(this.mChildAttachStateChangeListener);
    }

    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
    }

    public void onScrollStateChanged(int state) {
        switch(state) {
            case 0:
                View viewIdle = this.mPagerSnapHelper.findSnapView(this);
                int positionIdle = this.getPosition(viewIdle);
//                if (this.mOnViewPagerListener != null && this.getChildCount() == 1) {
//                    this.mOnViewPagerListener.onPageSelected(positionIdle, positionIdle == this.getItemCount() - 1);
//                }
                break;
            case 1:
                View viewDrag = this.mPagerSnapHelper.findSnapView(this);
                this.getPosition(viewDrag);
                break;
            case 2:
                View viewSettling = this.mPagerSnapHelper.findSnapView(this);
                this.getPosition(viewSettling);
        }

    }

    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dx;
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    public void setOnViewPagerListener(OnViewPagerListener listener) {
//        this.mOnViewPagerListener = listener;
    }
}
