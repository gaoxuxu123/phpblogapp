package com.app.phpblog;


import com.app.phpblog.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 点击抽屉菜单展示
 * 内容fragment
 */
public class ContentFragment extends Fragment {
	
	private static final String ARG_SECTION_TITLE = "section_title";

	/**
	 * 返回根据title参数创建的fragment
	 */
	public static ContentFragment newInstance(String title) {
		ContentFragment fragment = new ContentFragment();
		Bundle args = new Bundle();
		args.putString(ARG_SECTION_TITLE, title);
		fragment.setArguments(args);
		return fragment;
	}
	public ContentFragment() {
		
	}
	/**
	 * 创建展示内容的View
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//展示视图数据
		String type = getArguments().getString(ARG_SECTION_TITLE);
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		TextView textView = (TextView) rootView.findViewById(R.id.section_label);
		
		if(type.equals("全部文章")){
			
			textView.setText("全部文章");
			//return inflater.inflate(R.layout.activity_banner, container, false);
			
		}else{
			
			textView.setText(type);
		}
		return rootView;
		
	}
}