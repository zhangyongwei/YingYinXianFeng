package com.atguigu.yingyinxianfeng.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atguigu.yingyinxianfeng.R;
import com.atguigu.yingyinxianfeng.bean.NetAudioBean;

import java.util.List;

import static android.content.ContentValues.TAG;

public class NetAudioFragmentAdapter extends BaseAdapter {


    /**
     * 视频
     */
    private static final int TYPE_VIDEO = 0;

    /**
     * 图片
     */
    private static final int TYPE_IMAGE = 1;

    /**
     * 文字
     */
    private static final int TYPE_TEXT = 2;

    /**
     * GIF图片
     */
    private static final int TYPE_GIF = 3;


    /**
     * 软件推广
     */
    private static final int TYPE_AD = 4;

    private final Context mContext;
    private final List<NetAudioBean.ListBean> datas;

    public NetAudioFragmentAdapter(Context mContext, List<NetAudioBean.ListBean> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }


    /**
     * 返回总数量
     *
     * @return
     */
    @Override
    public int getCount() {
        return datas.size();
    }

    //返回总类型数据
    @Override
    public int getViewTypeCount() {
        return 5;
    }

    /**
     * 当前item是什么类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        int itemViewType = -1;
        //根据位置，从列表中得到一个数据对象
        NetAudioBean.ListBean listBean = datas.get(position);
        String type = listBean.getType();//得到类型
        Log.e(TAG, "type===" + type);
        if ("video".equals(type)) {
            itemViewType = TYPE_VIDEO;
        } else if ("image".equals(type)) {
            itemViewType = TYPE_IMAGE;
        } else if ("text".equals(type)) {
            itemViewType = TYPE_TEXT;
        } else if ("gif".equals(type)) {
            itemViewType = TYPE_GIF;
        } else {
            itemViewType = TYPE_AD;//广播
        }
        return itemViewType;
    }






    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = initView(convertView, getItemViewType(position), datas.get(position));
        return convertView;

    }

    private View initView(View convertView, int itemViewType, NetAudioBean.ListBean mediaItem) {
        switch (itemViewType) {
            case TYPE_VIDEO://视频

                VideoHoder videoHoder;
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.all_video_item, null);
                    videoHoder = new VideoHoder(convertView);
                    convertView.setTag(videoHoder);
                } else {
                    videoHoder = (VideoHoder) convertView.getTag();
                }

                //设置数据
                videoHoder.setData(mediaItem);

                break;
            case TYPE_IMAGE://图片
                ImageHolder imageHolder;
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.all_image_item, null);
                    imageHolder = new ImageHolder(convertView);
                    convertView.setTag(imageHolder);
                } else {
                    imageHolder = (ImageHolder) convertView.getTag();
                }
                //设置数据
                imageHolder.setData(mediaItem);
                break;
            case TYPE_TEXT://文字

                TextHolder textHolder;
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.all_text_item, null);
                    textHolder = new TextHolder(convertView);

                    convertView.setTag(textHolder);
                } else {
                    textHolder = (TextHolder) convertView.getTag();
                }

                textHolder.setData(mediaItem);

                break;
            case TYPE_GIF://gif

                GifHolder gifHolder;
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.all_gif_item, null);
                    gifHolder = new GifHolder(convertView);

                    convertView.setTag(gifHolder);
                } else {
                    gifHolder = (GifHolder) convertView.getTag();
                }

                gifHolder.setData(mediaItem);

                break;
            case TYPE_AD://软件广告

                ADHolder adHolder;
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.all_ad_item, null);
                    adHolder = new ADHolder(convertView);
                    convertView.setTag(adHolder);
                } else {
                    adHolder = (ADHolder) convertView.getTag();
                }

                break;
        }
        return convertView;

    }

    class BaseViewHolder {

        ImageView ivHeadpic;
        TextView tvName;
        TextView tvTimeRefresh;
        ImageView ivRightMore;
        ImageView ivVideoKind;
        TextView tvVideoKindText;
        TextView tvShenheDingNumber;
        TextView tvShenheCaiNumber;
        TextView tvPostsNumber;
        LinearLayout llDownload;

        public BaseViewHolder(View convertView) {
            //公共的
            ivHeadpic = (ImageView) convertView.findViewById(R.id.iv_headpic);
            tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tvTimeRefresh = (TextView) convertView.findViewById(R.id.tv_time_refresh);
            ivRightMore = (ImageView) convertView.findViewById(R.id.iv_right_more);
            //bottom
            ivVideoKind = (ImageView) convertView.findViewById(R.id.iv_video_kind);
            tvVideoKindText = (TextView) convertView.findViewById(R.id.tv_video_kind_text);
            tvShenheDingNumber = (TextView) convertView.findViewById(R.id.tv_shenhe_ding_number);
            tvShenheCaiNumber = (TextView) convertView.findViewById(R.id.tv_shenhe_cai_number);
            tvPostsNumber = (TextView) convertView.findViewById(R.id.tv_posts_number);
            llDownload = (LinearLayout) convertView.findViewById(R.id.ll_download);
        }


    }
    class ADHolder{

        private TextView textView;

        public ADHolder(View converView){
            textView = (TextView) converView.findViewById(R.id.tv_name);

        }
        public void setData(NetAudioBean.ListBean mediaItem) {
            textView.setText("我是广告的内容");
        }

    }

    class GifHolder{

        private TextView textView;

        public GifHolder(View converView){
            textView = (TextView) converView.findViewById(R.id.tv_name);

        }
        public void setData(NetAudioBean.ListBean mediaItem) {
            textView.setText("我是gif的内容");
        }

    }
    class TextHolder{

        private TextView textView;

        public TextHolder(View converView){
            textView = (TextView) converView.findViewById(R.id.tv_name);

        }
        public void setData(NetAudioBean.ListBean mediaItem) {
            textView.setText("我是文字的内容");
        }

    }
    class ImageHolder{

        private TextView textView;

        public ImageHolder(View converView){
            textView = (TextView) converView.findViewById(R.id.tv_name);

        }
        public void setData(NetAudioBean.ListBean mediaItem) {
            textView.setText("我是图片的内容");
        }

    }
    class VideoHoder{

        private TextView textView;

        public VideoHoder(View converView){
            textView = (TextView) converView.findViewById(R.id.tv_name);

        }
        public void setData(NetAudioBean.ListBean mediaItem) {
            textView.setText("我是视频的内容");
        }

    }

   /* class VideoHoder extends BaseViewHolder {

        Utils utils;
        TextView tvContext;
        JCVideoPlayerStandard jcvVideoplayer;
        TextView tvPlayNums;
        TextView tvVideoDuration;
        ImageView ivCommant;
        TextView tvCommantContext;

        VideoHoder(View convertView) {
            super(convertView);
            //中间公共部分 -所有的都有
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            utils = new Utils();
            tvPlayNums = (TextView) convertView.findViewById(R.id.tv_play_nums);
            tvVideoDuration = (TextView) convertView.findViewById(R.id.tv_video_duration);
            ivCommant = (ImageView) convertView.findViewById(R.id.iv_commant);
            tvCommantContext = (TextView) convertView.findViewById(R.id.tv_commant_context);
            jcvVideoplayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.jcv_videoplayer);
        }

        public void setData(NetAudioBean.ListBean mediaItem) {

        }
    }*/
   /* *//**
     * 设置公共的数据
     *
     * @param mediaItem
     *//*
    public void setData(NetAudioBean.ListBean mediaItem) {
        if (mediaItem.getU() != null && mediaItem.getU().getHeader() != null && mediaItem.getU().getHeader().get(0) != null) {
            x.image().bind(ivHeadpic, mediaItem.getU().getHeader().get(0));
        }
        if (mediaItem.getU() != null && mediaItem.getU().getName() != null) {
            tvName.setText(mediaItem.getU().getName() + "");
        }

        tvTimeRefresh.setText(mediaItem.getPasstime());

        //设置标签
        List<NetAudioBean.ListBean.TagsEntity> tagsEntities = mediaItem.getTags();
        if (tagsEntities != null && tagsEntities.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < tagsEntities.size(); i++) {
                buffer.append(tagsEntities.get(i).getName() + " ");
            }
            tvVideoKindText.setText(buffer.toString());
        }

        //设置点赞，踩,转发

        tvShenheDingNumber.setText(mediaItem.getUp());
        tvShenheCaiNumber.setText(mediaItem.getDown() + "");
        tvPostsNumber.setText(mediaItem.getForward() + "");

    }
*/
}