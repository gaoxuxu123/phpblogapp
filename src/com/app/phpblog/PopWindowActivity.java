package com.app.phpblog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class PopWindowActivity extends Fragment {
	private PopupWindow popupWindow;
	private View parent;
	/**菜单弹出来时候的菜单项图案*/
	private int[] images = { R.drawable.i1, R.drawable.i2, R.drawable.i3,
			R.drawable.i4, R.drawable.i5, R.drawable.i6, R.drawable.i7,
			R.drawable.i8 };
	/**菜单弹出来时候的菜单项文字*/
	private String[] names = { "搜索", "文件管理", "下载管理", "全屏", "网址", "书签", "加入书签",
			"分享页面" };
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View contentView = inflater.inflate(R.layout.popwindow, null);
		/**网格布局界面*/
		GridView gridView = (GridView) contentView.findViewById(R.id.gridView);
		/**设置网格布局的适配器*/
		gridView.setAdapter(getAdapter());
		/**设置网格布局的菜单项点击时候的Listener*/
		gridView.setOnItemClickListener(new ItemClickListener());
		/**初始化PopupWindow*/
		popupWindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);// 取得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		/**设置PopupWindow弹出和退出时候的动画效果*/
		popupWindow.setAnimationStyle(R.style.animation);
		parent = contentView.findViewById(R.id.container);
		return contentView;
	}
	private final class ItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();//关闭
			}
		}
	}
	/**返回网格布局的适配器*/
	private ListAdapter getAdapter() {
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < images.length; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("image", images[i]);
			item.put("name", names[i]);
			data.add(item);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), data,
				R.layout.grid_item, new String[] { "image", "name" },
				new int[] { R.id.imageView, R.id.textView });
		return simpleAdapter;
	}

	public void openPopWindow(View v) {
		/**设置PopupWindow弹出后的位置*/
		popupWindow.showAtLocation(parent, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	}
}
