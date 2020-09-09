package com.union_test.toutiao.activity.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTDrawFeedAd;
import com.union_test.toutiao.R;
import com.union_test.toutiao.config.TTAdManagerHolder;

import java.util.ArrayList;
import java.util.List;

public class DrawFullScreenVideoActivity extends AppCompatActivity {

    private static final String TAG = "DrawFullScreenVideoActi";

    private RecyclerView recycler;
    private DrawAdapter drawAdapter;
    private TTAdNative mTTAdNative;
    private List<Item> drawDatas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_full_screen_video);
        initView();
        initData();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                loadDrawNativeAd();
            }
        },1000);
    }

    private void initData() {
        mTTAdNative = TTAdManagerHolder.get().createAdNative(this);
        //在合适的时机申请权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题
        TTAdManagerHolder.get().requestPermissionIfNecessary(this);
    }

    private void initView() {
        recycler = findViewById(R.id.recycler);
//        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
//        recycler.setLayoutManager(new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL));
        recycler.setLayoutManager(new DrawViewPagerLayoutManager(this, OrientationHelper.VERTICAL));
        drawAdapter = new DrawAdapter();
        recycler.setAdapter(drawAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadDrawNativeAd() {
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("901121709")
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setAdCount(2) //请求广告数量为1到3条
                .build();
        Log.e(TAG, "loadDrawNativeAd() called");
        mTTAdNative.loadDrawFeedAd(adSlot, new TTAdNative.DrawFeedAdListener() {

            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "onError() called with: i = [" + i + "], s = [" + s + "]");
            }

            @Override
            public void onDrawFeedAdLoad(List<TTDrawFeedAd> list) {
                Log.e(TAG, "onDrawFeedAdLoad: " + (list != null ? list.size() : 0));
                drawDatas.clear();
                for (TTDrawFeedAd tt : list) {
                    Log.e(TAG, "TTDrawFeedAd: ----------------------------------------");
                    Log.e(TAG, "getTitle: " + tt.getTitle());
                    Log.e(TAG, "getDescription: " + tt.getDescription());
                    Log.e(TAG, "getButtonText: " + tt.getButtonText());

                    drawDatas.add(new Item(tt));
                }
                drawAdapter.notifyDataSetChanged();
            }
        });
    }

    class DrawAdapter extends RecyclerView.Adapter<DrawAdapter.AdapterViewHolder>{
        @NonNull
        @Override
        public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_draw, parent, false);
            return new AdapterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
            Item item = drawDatas.get(position);
            holder.videoLayout.removeAllViews();
            holder.videoLayout.addView(item.ad.getAdView());;
        }

        @Override
        public int getItemCount() {
            return drawDatas.size();
        }

        public class AdapterViewHolder extends RecyclerView.ViewHolder{
            FrameLayout videoLayout;
            public AdapterViewHolder(View itemView) {
                super(itemView);
                videoLayout = itemView.findViewById(R.id.video_layout);
            }
        }
    }

    class Item {
        public TTDrawFeedAd ad;
        public Item(TTDrawFeedAd ad) {
            this.ad = ad;
        }
    }

}
