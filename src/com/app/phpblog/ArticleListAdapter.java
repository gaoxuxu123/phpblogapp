package com.app.phpblog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entity.Article;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ArticleListAdapter extends BaseAdapter {

	private Context context;
	private List<Article> lists;
	
	public ArticleListAdapter(Context context,List<Article> list) {
		super();
		this.context = context;
		this.lists   = list;
	}

	@Override
	public int getCount() {
		
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		 //优化ListView  
        if(convertView==null){  
        	
        	ArticleCache cache = new ArticleCache(); 
        	convertView=LayoutInflater.from(context).inflate(R.layout.article_list_item, null);  
        	cache.titleView   = (TextView) convertView.findViewById(R.id.title);
        	cache.pubtimeView = (TextView) convertView.findViewById(R.id.createtime);
        	convertView.setTag(cache);  
        }
        ArticleCache cache = (ArticleCache) convertView.getTag();
        Article a = lists.get(position);
        cache.titleView.setText(a.getTitle());
        cache.pubtimeView.setText(a.getPubtime());
		return convertView;
	}
	//元素的缓冲类,用于优化ListView
	private static class ArticleCache{
		
		public TextView titleView,pubtimeView;
		
	}
    //填充文章信息
    public static List<Article> setArticleInfo(){
    	
    	List<Article> list = new ArrayList<Article>();
    	for (int i = 0;i < 10 ;i++){
    		
    		Article article = new Article();
    		article.setId(i);
    		article.setTitle("文章"+i);
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
    		String time = sdf.format(new Date());
    		article.setPubtime(time);
    		article.setBrief("暂无");
    		//article.setAvatar("http://img.my.csdn.net/uploads/201211/19/1353291080_5216.jpg");
    		article.setBad(i);
    		article.setGoods(i+1);
    		article.setHits(i+100);
    		list.add(article);
    	}
    	return list;
    }

}
