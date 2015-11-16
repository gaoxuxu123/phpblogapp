package com.app.phpblog;

import java.util.List;
import java.io.File;  
import java.util.ArrayList;  
import java.util.concurrent.Executors;  
import java.util.concurrent.ScheduledExecutorService;  
import java.util.concurrent.TimeUnit;  

import com.app.phpblog.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import entity.AdDomain;
import entity.Article;
  
import android.graphics.Bitmap;  
import android.os.Bundle;  
import android.os.Handler;  
import android.os.Parcelable;  
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;  
import android.support.v4.view.ViewPager;  
import android.support.v4.view.ViewPager.OnPageChangeListener;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.view.ViewGroup;  
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;  
import android.widget.ImageView.ScaleType;  
import android.widget.ListView;
import android.widget.TextView; 
import android.widget.Toast;

public class BannerActivity extends Fragment {
	
	 ListView listView;  //声明一个ListView对象  
	 private List<Article> mlistInfo = new ArrayList<Article>();  //声明一个list，动态存储要显示的信息  
	
	public static String IMAGE_CACHE_PATH = "imageloader/Cache"; // 图片缓存路径  
	  
    private ViewPager adViewPager;  
    private List<ImageView> imageViews;// 滑动的图片集合  
  
    private List<View> dots; // 图片标题正文的那些点  
    private List<View> dotList;  
  
    private TextView tv_date;  
    private TextView tv_title;  
    private TextView tv_topic_from;  
    private TextView tv_topic;  
    private int currentItem = 0; // 当前图片的索引号  
    // 定义的五个指示点  
    private View dot0;  
    private View dot1;  
    private View dot2;  
    private View dot3;  
    private View dot4;  
  
    // 定时任务  
    private ScheduledExecutorService scheduledExecutorService;  
  
    // 异步加载图片  
    private ImageLoader mImageLoader;  
    private DisplayImageOptions options;  
  
    // 轮播banner的数据  
    private List<AdDomain> adList;  
  
    private Handler handler = new Handler() {  
        public void handleMessage(android.os.Message msg) {  
            adViewPager.setCurrentItem(currentItem);  
        };  
    };  
    public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    		
    	   initImageLoader();  
           // 获取图片加载实例  
           mImageLoader = ImageLoader.getInstance();  
           options = new DisplayImageOptions.Builder()  
		                   .showStubImage(R.drawable.top_banner_android)  
		                   .showImageForEmptyUri(R.drawable.top_banner_android)  
		                   .showImageOnFail(R.drawable.top_banner_android)  
		                   .cacheInMemory(true).cacheOnDisc(true)  
		                   .bitmapConfig(Bitmap.Config.RGB_565)  
		                   .imageScaleType(ImageScaleType.EXACTLY).build();  
           View view = inflater.inflate(R.layout.activity_banner, container, false);
           initAdData(view);  
           startAd();  
           //文章列表信息填充
           listView=(ListView)view.findViewById(R.id.listView);
           mlistInfo = ArticleListAdapter.setArticleInfo();
           for (Article article : mlistInfo) {
        	   
        	   System.out.println(article.getId());
           }
           listView.setAdapter(new ArticleListAdapter(getActivity(),mlistInfo));
           //处理Item的点击事件  
           listView.setOnItemClickListener(new OnItemClickListener(){  
               public void onItemClick(AdapterView<?> parent, View view,int position, long id) { 

            	   //通过position获取所点击的对象  
            	   Article getObject = mlistInfo.get(position);  
            		//获取信息id  
                   int infoId = getObject.getId(); 
                   //获取信息标题  
                   String infoTitle = getObject.getTitle(); 
                   //获取信息时间
                   String infoDetails = getObject.getPubtime();    
                   //Toast显示测试  
                   Toast.makeText(getActivity(), "信息ID:"+infoId,Toast.LENGTH_SHORT).show();  
               }  
           });
   		return view;
    };
    /** 
     * 初始化ImageLoader 
     */  
    private void initImageLoader() {  
        File cacheDir = com.nostra13.universalimageloader.utils.StorageUtils  
                .getOwnCacheDirectory(getActivity(),  
                        IMAGE_CACHE_PATH);  
  
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()  
                .cacheInMemory(true).cacheOnDisc(true).build();  
  
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(  
                getActivity()).defaultDisplayImageOptions(defaultOptions)  
                .memoryCache(new LruMemoryCache(12 * 1024 * 1024))  
                .memoryCacheSize(12 * 1024 * 1024)  
                .discCacheSize(32 * 1024 * 1024).discCacheFileCount(100)  
                .discCache(new UnlimitedDiscCache(cacheDir))  
                .threadPriority(Thread.NORM_PRIORITY - 2)  
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();  
        ImageLoader.getInstance().init(config);  
    }  
  
    /** 
     * 初始化广告数据 
     */  
    private void initAdData(View view) {  
        // 广告数据  
        adList = getBannerAd();  
  
        imageViews = new ArrayList<ImageView>();  
  
        // 点  
        dots = new ArrayList<View>();  
        dotList = new ArrayList<View>();  
        dot0 = view.findViewById(R.id.v_dot0);  
        dot1 = view.findViewById(R.id.v_dot1);  
        dot2 = view.findViewById(R.id.v_dot2);  
        dot3 = view.findViewById(R.id.v_dot3);  
        dot4 = view.findViewById(R.id.v_dot4);  
        dots.add(dot0);  
        dots.add(dot1);  
        dots.add(dot2);  
        dots.add(dot3);  
        dots.add(dot4);  
          
        tv_date = (TextView) view.findViewById(R.id.tv_date);  
        tv_title = (TextView) view.findViewById(R.id.tv_title);  
        tv_topic_from = (TextView) view.findViewById(R.id.tv_topic_from);  
        tv_topic = (TextView) view.findViewById(R.id.tv_topic);  
  
        adViewPager = (ViewPager) view.findViewById(R.id.vp);  
        adViewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器  
        // 设置一个监听器，当ViewPager中的页面改变时调用  
        adViewPager.setOnPageChangeListener(new MyPageChangeListener());  
        addDynamicView();  
    }  
  
    private void addDynamicView() {  
        // 动态添加图片和下面指示的圆点  
        // 初始化图片资源  
        for (int i = 0; i < adList.size(); i++) {  
            ImageView imageView = new ImageView(getActivity());  
            // 异步加载图片  
            mImageLoader.displayImage(adList.get(i).getImgUrl(), imageView,  
                    options);  
            imageView.setScaleType(ScaleType.CENTER_CROP);  
            imageViews.add(imageView);  
            dots.get(i).setVisibility(View.VISIBLE);  
            dotList.add(dots.get(i));  
        }  
    }  
  
    private void startAd() {  
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();  
        // 当Activity显示出来后，每两秒切换一次图片显示  
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2,  
                TimeUnit.SECONDS);  
    }  
  
    private class ScrollTask implements Runnable {  
  
        @Override  
        public void run() {  
            synchronized (adViewPager) {  
                currentItem = (currentItem + 1) % imageViews.size();  
                handler.obtainMessage().sendToTarget();  
            }  
        }  
    }  
  
    private class MyPageChangeListener implements OnPageChangeListener {  
  
        private int oldPosition = 0;  
  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
              
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
              
        }  
  
        @Override  
        public void onPageSelected(int position) {  
            currentItem = position;  
            AdDomain adDomain = adList.get(position);  
            tv_title.setText(adDomain.getTitle()); // 设置标题  
            tv_date.setText(adDomain.getDate());  
            tv_topic_from.setText(adDomain.getTopicFrom());  
            tv_topic.setText(adDomain.getTopic());  
            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);  
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);  
            oldPosition = position;  
        }  
    }  
  
    private class MyAdapter extends PagerAdapter {  
  
        @Override  
        public int getCount() {  
            return adList.size();  
        }  
  
        @Override  
        public Object instantiateItem(ViewGroup container, int position) {  
            ImageView iv = imageViews.get(position);  
            ((ViewPager) container).addView(iv);  
            final AdDomain adDomain = adList.get(position);  
            // 在这个方法里面设置图片的点击事件  
            iv.setOnClickListener(new OnClickListener() {  
  
                @Override  
                public void onClick(View v) {  
                    // 处理跳转逻辑  
                }  
            });  
            return iv;  
        }  
  
        @Override  
        public void destroyItem(View arg0, int arg1, Object arg2) {  
            ((ViewPager) arg0).removeView((View) arg2);  
        }  
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0 == arg1;  
        }  
  
        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {  
  
        }  
  
        @Override  
        public Parcelable saveState() {  
            return null;  
        }  
  
        @Override  
        public void startUpdate(View arg0) {  
  
        }  
  
        @Override  
        public void finishUpdate(View arg0) {  
  
        }  
  
    }  
  
    /** 
     * 轮播广播模拟数据 
     *  
     * @return 
     */  
    public static List<AdDomain> getBannerAd() {  
        List<AdDomain> adList = new ArrayList<AdDomain>();  
  
        AdDomain adDomain = new AdDomain();  
        adDomain.setId("108078");  
        adDomain.setDate("3月4日");  
        adDomain.setTitle("我和令计划只是同姓");  
        adDomain.setTopicFrom("阿宅");  
        adDomain.setTopic("我想知道令狐安和令计划有什么关系？");  
        adDomain.setImgUrl("http://g.hiphotos.baidu.com/image/w%3D310/sign=bb99d6add2c8a786be2a4c0f5708c9c7/d50735fae6cd7b8900d74cd40c2442a7d9330e29.jpg");  
        adDomain.setAd(false);  
        adList.add(adDomain);  
  
        AdDomain adDomain2 = new AdDomain();  
        adDomain2.setId("108078");  
        adDomain2.setDate("3月5日");  
        adDomain2.setTitle("我和令计划只是同姓");  
        adDomain2.setTopicFrom("小巫");  
        adDomain2.setTopic("“我想知道令狐安和令计划有什么关系？”");  
        adDomain2  
                .setImgUrl("http://g.hiphotos.baidu.com/image/w%3D310/sign=7cbcd7da78f40ad115e4c1e2672e1151/eaf81a4c510fd9f9a1edb58b262dd42a2934a45e.jpg");  
        adDomain2.setAd(false);  
        adList.add(adDomain2);  
  
        AdDomain adDomain3 = new AdDomain();  
        adDomain3.setId("108078");  
        adDomain3.setDate("3月6日");  
        adDomain3.setTitle("我和令计划只是同姓");  
        adDomain3.setTopicFrom("旭东");  
        adDomain3.setTopic("“我想知道令狐安和令计划有什么关系？”");  
        adDomain3  
                .setImgUrl("http://e.hiphotos.baidu.com/image/w%3D310/sign=392ce7f779899e51788e3c1572a6d990/8718367adab44aed22a58aeeb11c8701a08bfbd4.jpg");  
        adDomain3.setAd(false);  
        adList.add(adDomain3);  
  
        AdDomain adDomain4 = new AdDomain();  
        adDomain4.setId("108078");  
        adDomain4.setDate("3月7日");  
        adDomain4.setTitle("我和令计划只是同姓");  
        adDomain4.setTopicFrom("小软");  
        adDomain4.setTopic("“我想知道令狐安和令计划有什么关系？”");  
        adDomain4  
                .setImgUrl("http://d.hiphotos.baidu.com/image/w%3D310/sign=54884c82b78f8c54e3d3c32e0a282dee/a686c9177f3e670932e4cf9338c79f3df9dc55f2.jpg");  
        adDomain4.setAd(false);  
        adList.add(adDomain4);  
  
        AdDomain adDomain5 = new AdDomain();  
        adDomain5.setId("108078");  
        adDomain5.setDate("3月8日");  
        adDomain5.setTitle("我和令计划只是同姓");  
        adDomain5.setTopicFrom("大熊");  
        adDomain5.setTopic("“我想知道令狐安和令计划有什么关系？”");  
        adDomain5  
                .setImgUrl("http://e.hiphotos.baidu.com/image/w%3D310/sign=66270b4fe8c4b7453494b117fffd1e78/0bd162d9f2d3572c7dad11ba8913632762d0c30d.jpg");  
        adDomain5.setAd(true); // 代表是广告  
        adList.add(adDomain5);  
        return adList;  
    }  

}
