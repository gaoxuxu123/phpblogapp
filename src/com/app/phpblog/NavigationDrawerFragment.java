package com.app.phpblog;

import java.util.ArrayList;
import java.util.List;

import com.app.phpblog.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 用于管理交互和展示抽屉导航的Fragment。
 * 参考<a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * 设计向导</a> 
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * 存放选中item的位置
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * 存放用户是否需要默认开启drawer的key
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * 宿主activity实现的回调接口的引用
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * 将action bar和drawerlayout绑定的组件
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;
    //设置侧边栏默认的选中
    private int mCurrentSelectedPosition = 1;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private static final String BARNAME = "全部文章";
    private String mTitle;
    private List<DrawerListItem> mData = new ArrayList<DrawerListItem>();

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 通过这个flag判断用户是否已经知道drawer了，第一次启动应用显示出drawer（抽屉），之后启动应用默认将其隐藏
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 设置该fragment拥有自己的actionbar action item
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        View headerView = inflater.inflate(R.layout.list_header, null);
        mDrawerListView.addHeaderView(headerView);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        String[] itemTitle = getResources().getStringArray(R.array.item_title);
        int[] itemIconRes = {
    		R.drawable.ic_drawer_home,
    		R.drawable.ic_drawer_explore,
    		R.drawable.ic_drawer_follow,
    		R.drawable.ic_drawer_collect,
	        R.drawable.ic_drawer_draft,
	        R.drawable.ic_drawer_search,
	        R.drawable.ic_drawer_question,
	        R.drawable.ic_drawer_setting};
        
        for (int i = 0; i < itemTitle.length; i++) {
			DrawerListItem item = new DrawerListItem(getResources().getDrawable(itemIconRes[i]), itemTitle[i]);
			mData.add(item);
			
		}
        selectItem(mCurrentSelectedPosition);
        DrawerListAdapter adapter = new DrawerListAdapter(this.getActivity(), mData);
        mDrawerListView.setAdapter(adapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return mDrawerListView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }
    /**
     * 设置导航drawer
     *
     * @param fragmentId   fragmentent的id
     * @param drawerLayout fragment的容器
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        ActionBar actionBar = getActionBar();
        //修改原始图标
        actionBar.setHomeButtonEnabled(true); 
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setLogo(R.drawable.ic_drawer);
        actionBar.setTitle(BARNAME);
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* 宿主 */
                mDrawerLayout,                    /* DrawerLayout 对象 */
                R.drawable.ic_drawer,             /* 替换actionbar上的'Up'图标 */
                R.string.navigation_drawer_open,  
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // 调用 onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
                }

                getActivity().supportInvalidateOptionsMenu(); //调用 onPrepareOptionsMenu()
            }
        };

        // 如果是第一次进入应用，显示抽屉
        /*if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }*/

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            //没有点击任何菜单的时候,可以设置一个默认的显示菜单
        	if(mCurrentSelectedPosition == 1){
        		// 跳转其他的Fragment
        		getFragmentManager().beginTransaction().replace(R.id.container, new BannerActivity(),null).commit();
        		
        		Toast.makeText(getActivity(), "给我一个flagment", Toast.LENGTH_LONG).show();
        	}
        	
        	mTitle = mData.get(position - 1).getTitle();
            mCallbacks.onNavigationDrawerItemSelected(mTitle);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 当系统配置改变时调用DrawerToggle的改变配置方法（例如横竖屏切换会回调此方法）
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //当抽屉打开时显示应用全局的actionbar设置
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
    /**
     * 抽屉关闭的时候点击bar的事件处理
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //点击小铃铛时的触发事件	
        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "我是小铃铛", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(item.getItemId() == R.id.action_logout){
        	Toast.makeText(getActivity(), "退出登录", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(item.getItemId() == R.id.action_about){
        	Toast.makeText(getActivity(), "关于作者", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 当抽屉打开时显示应用全局的actionbar设置
     * 第一次打开抽屉的
     * 头部的显示标题
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        //actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(mTitle);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * 宿主activity要实现的回调接口
     * 用于activity与该fragment之间通讯
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * 当drawer中的某个item被选择是调用该方法
         */
        void onNavigationDrawerItemSelected(String title);
    }
    
}
