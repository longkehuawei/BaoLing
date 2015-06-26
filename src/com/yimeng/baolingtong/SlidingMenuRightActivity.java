package com.yimeng.baolingtong;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.view.progress.AbHorizontalProgressBar;
import com.ab.view.slidingmenu.SlidingMenu;
import com.ab.view.titlebar.AbTitleBar;
import com.igexin.sdk.PushManager;
import com.yimeng.baolingtong.menu.MenuManager;
import com.yimeng.baolingtong.menu.interfaces.OnMenuItemClickListener;
import com.yimeng.baolingtong.menu.interfaces.OnMenuItemLongClickListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class SlidingMenuRightActivity extends AbActivity implements
		OnMenuItemClickListener, OnMenuItemLongClickListener {

	private SlidingMenu menu;
	private AbTitleBar mAbTitleBar = null;
	private AbHorizontalProgressBar progressBar;
	private View menuBtn;
	public static String imie;
	public static TextView addressText;
	public static TextView time_text;

	private MainFragment mainFragment;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.sliding_menu_content);

		mAbTitleBar = this.getTitleBar();
		// mAbTitleBar.setTitleText(R.string.sliding_menu_name);
		mAbTitleBar.removeAllViews();
		imie = getIntent().getStringExtra("imie");
		// mAbTitleBar.removeViewAt(1);
		// mAbTitleBar.setLogo(R.drawable.button_selector_back);
		// SDK初始化，第三方程序启动时，都要进行SDK初始化工作
		Log.d("GetuiSdkDemo", "initializing sdk...");
		PushManager.getInstance().initialize(this.getApplicationContext());
		RelativeLayout titleLayout = (RelativeLayout) LayoutInflater.from(
				SlidingMenuRightActivity.this).inflate(R.layout.main_title_bar,
				null);
		menuBtn = titleLayout.findViewById(R.id.menuBtn);
		addressText = (TextView) titleLayout.findViewById(R.id.addressText);
		time_text = (TextView) titleLayout.findViewById(R.id.time_text);
		progressBar = (AbHorizontalProgressBar) titleLayout
				.findViewById(R.id.horizontalProgressBar);
		progressBar.setMax(100);
		progressBar.setProgress(71);
		ViewGroup.LayoutParams params = mAbTitleBar.getLayoutParams();
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		mAbTitleBar.addView(titleLayout, 0, params);
		menuBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (menu.isMenuShowing()) {
					menu.showContent();
				} else {
					menu.showMenu();
				}
			}
		});
		setTitleBarHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		// mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		// mAbTitleBar.setLogoLine(R.drawable.line);

		// 主视图的Fragment添加
		mainFragment = new MainFragment();
		getFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mainFragment).commit();

		// SlidingMenu的配�?
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow_right);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// menu视图的Fragment添加
		menu.setMenu(R.layout.sliding_menu_menu);
		getFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MainMenuFragment()).commit();

	}

	@Override
	public void onBackPressed() {
		if (MainFragment.isShowMainMenu()) {
			mainFragment.closeMainMenu();
			return;
		}
		if (menu.isMenuShowing()) {
			menu.showContent();
		} else {
			super.onBackPressed();
		}
	}

	private void initTitleRightLayout() {
		mAbTitleBar.clearRightView();
		View rightViewMenu = mInflater.inflate(R.layout.menu_btn, null);
		mAbTitleBar.addRightView(rightViewMenu);
		Button menuBtn = (Button) rightViewMenu.findViewById(R.id.menuBtn);

		menuBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (menu.isMenuShowing()) {
					menu.showContent();
				} else {
					menu.showMenu();
				}
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		initTitleRightLayout();
	}

	@Override
	public void onMenuItemClick(View clickedView, int position, boolean selected) {
		switch (position) {
		case MenuManager.CURRENT_LOCATION:

			break;
		case MenuManager.LOCATION_FREQUENCY:

			break;
		case MenuManager.SAVE_AREA:

			break;
		case MenuManager.TAKE_OFF_ALERT:

			break;
		case MenuManager.BLT_FOLLOW:

			break;
		case MenuManager.HIT_ALERT:

			break;
		}
	}

	@Override
	public void onMenuItemLongClick(View clickedView, int position,
			boolean selected) {
		// Toast.makeText(this, "menu position="+position,
		// Toast.LENGTH_SHORT).show();
	}

}
