package com.app.phpblog;

import com.app.phpblog.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * 左侧划出抽屉内部fragment
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * 存放上次显示在action bar中的title
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	
	private Fragment currentFragment;
	private Fragment lastFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		// 设置抽屉
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
		
	}

	@Override
	public void onNavigationDrawerItemSelected(String title) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		currentFragment = fragmentManager.findFragmentByTag(title);
		if(currentFragment == null) {
			currentFragment = ContentFragment.newInstance(title);
			ft.add(R.id.container, currentFragment, title);
		}
		if(lastFragment != null) {
			ft.hide(lastFragment);
		}
		if(currentFragment.isDetached()){
			ft.attach(currentFragment);
		}
		ft.show(currentFragment);
		lastFragment = currentFragment;
		ft.commit();
		onSectionAttached(title);
	}
	//设置全局标题
	public void onSectionAttached(String title) {
		mTitle = title;
	}
	/**
	 * ActionBar每次保存点击过的title
	 */
	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}
/**
 * 抽屉打开后点击bar上面的菜单
 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		
		if (id == R.id.action_logout) {
			Toast.makeText(MainActivity.this, "退出登录", Toast.LENGTH_SHORT).show();
			return true;
		}
		if(id == R.id.action_about){
        	Toast.makeText(MainActivity.this, "关于作者", Toast.LENGTH_SHORT).show();
            return true;
        }
		return super.onOptionsItemSelected(item);
	}

}
