package com.yimeng.baolingtong;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;

import com.ab.cache.image.AbImageBaseCache;
import com.ab.fragment.AbFragment;
import com.ab.image.AbImageLoader;
import com.ab.model.AbMenuItem;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbToastUtil;
import com.andbase.web.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yimeng.baolingtong.adpater.LeftMenuAdapter;

public class MainMenuFragment extends AbFragment {

	private SlidingMenuRightActivity mActivity = null;
	private ExpandableListView mMenuListView;
	private ArrayList<String> mGroupName = null;
	private ArrayList<ArrayList<AbMenuItem>> mChilds = null;
	private ArrayList<AbMenuItem> mChild1 = null;
	private ArrayList<AbMenuItem> mChild2 = null;
	private LeftMenuAdapter mAdapter;
	private OnChangeViewListener mOnChangeViewListener;
	private AbImageLoader mAbImageLoader = null;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = (SlidingMenuRightActivity) this.getActivity();
		View view = inflater.inflate(R.layout.main_menu, null);
		mMenuListView = (ExpandableListView) view.findViewById(R.id.menu_list);
		Button cacheClearBtn = (Button) view.findViewById(R.id.cacheClearBtn);

		cacheClearBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AbDialogUtil.showProgressDialog(mActivity, 0, "正在清空缓存...");
				AbTask task = AbTask.newInstance();
				// 定义异步执行的对�?
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskListener() {

					@Override
					public void update() {
						AbDialogUtil.removeDialog(mActivity);
						AbToastUtil.showToast(mActivity, "缓存已清空完?");
					}

					@Override
					public void get() {
						try {
							AbFileUtil.clearDownloadFile();
							AbImageBaseCache.getInstance().clearBitmap();
						} catch (Exception e) {
							AbToastUtil.showToastInThread(mActivity,
									e.getMessage());
						}
					};
				});
				task.execute(item);

			}
		});

		mGroupName = new ArrayList<String>();
		mChild1 = new ArrayList<AbMenuItem>();
		mChild2 = new ArrayList<AbMenuItem>();

		ArrayList<ArrayList<AbMenuItem>> mChilds = new ArrayList<ArrayList<AbMenuItem>>();
		mChilds.add(mChild1);
		mChilds.add(mChild2);
		/*TextView userName=new TextView(getActivity());
		userName.setText("小明");
		userName.setTextColor(getResources().getColor(R.color.white));
		userName.setTextSize(20);*/
		LinearLayout userlayout=(LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.menu_headview, null);
		mMenuListView.addHeaderView(userlayout);
		mAdapter = new LeftMenuAdapter(mActivity, mGroupName, mChilds);
		mMenuListView.setAdapter(mAdapter);
		for (int i = 0; i < mGroupName.size(); i++) {
			mMenuListView.expandGroup(i);
		}

		mMenuListView.setOnGroupClickListener(new OnGroupClickListener() {

			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});

		mMenuListView.setOnChildClickListener(new OnChildClickListener() {

			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if (mOnChangeViewListener != null) {
					mOnChangeViewListener.onChangeView(groupPosition,
							childPosition);
				}
				return true;
			}
		});

		// 图片的下�?
		mAbImageLoader = new AbImageLoader(mActivity);

		initMenu();

		return view;
	}
	
	public interface OnChangeViewListener {
		public abstract void onChangeView(int groupPosition, int childPosition);
	}

	public void setOnChangeViewListener(
			OnChangeViewListener onChangeViewListener) {
		mOnChangeViewListener = onChangeViewListener;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void initMenu() {
		mGroupName.clear();
		mChild1.clear();
		mChild2.clear();

		mGroupName.add("对象设置");
		mGroupName.add("其他");

		AbMenuItem m0 = new AbMenuItem();
		m0.setText("亲情号码");
		mChild1.add(m0);

		AbMenuItem m1 = new AbMenuItem();
		m1.setText("历史位置");
		mChild1.add(m1);

		AbMenuItem m3 = new AbMenuItem();
		m3.setText("所有对象位置");
		mChild1.add(m3);

		AbMenuItem m4 = new AbMenuItem();
		m4.setText("省电模式");
		mChild1.add(m4);
		AbMenuItem m5 = new AbMenuItem();
		m5.setText("碰撞检测等级");
		mChild1.add(m5);
		AbMenuItem m6 = new AbMenuItem();
		m6.setText("电子栅栏");
		mChild1.add(m6);

		AbMenuItem m7 = new AbMenuItem();
		m7.setText("对象管理");
		mChild2.add(m7);

		AbMenuItem m8 = new AbMenuItem();
		m8.setText("公告");
		mChild2.add(m8);

		AbMenuItem m9 = new AbMenuItem();
		m9.setText("关于");
		mChild2.add(m9);

		
		mAdapter.notifyDataSetChanged();
		for (int i = 0; i < mGroupName.size(); i++) {
			mMenuListView.expandGroup(i);
		}


		/*
		 * final String shareStr = this.getResources().getString(
		 * R.string.share_desc);
		 */
		setOnChangeViewListener(new OnChangeViewListener() {

			@Override
			public void onChangeView(int groupPosition, int childPosition) {
				if (groupPosition == 0) {
					if (childPosition == 0) {
						/*
						 * // 联系�? if (application.mUser == null) {
						 * mActivity.toLogin(mActivity.FRIEND_CODE); } else {
						 * Intent intent = new Intent(mActivity,
						 * ContacterActivity.class);
						 * mActivity.startActivity(intent); }
						 */
					} else if (childPosition == 1) {
						// 我的消息
						/*
						 * Intent intent = new Intent(mActivity,
						 * MessageActivity.class); startActivity(intent);
						 */
					} else if (childPosition == 2) {
						// 程序案例
						/*
						 * Intent intent = new Intent(mActivity,
						 * DemoMainActivity.class); startActivity(intent);
						 */
					} else if (childPosition == 3) {
						// 应用游戏
						// mActivity.showApp();
					}
				} else if (groupPosition == 1) {
					if (childPosition == 0) {
						// 选项、赞助作�?
						// mActivity.showApp();
					} else if (childPosition == 1) {
						// 推荐

					} else if (childPosition == 2) {/*
													 * if (mUser != null) {
													 * AbDialogUtil
													 * .showAlertDialog
													 * (mActivity, "注销",
													 * "确定要注�?该用户吗?", new
													 * AbDialogOnClickListener()
													 * {
													 * 
													 * @Override public void
													 * onPositiveClick() { // 注销
													 * application
													 * .clearLoginParams();
													 * initMenu();
													 * mActivity.stopIMService
													 * (); }
													 * 
													 * @Override public void
													 * onNegativeClick() { //
													 * TODO Auto-generated
													 * method stub
													 * 
													 * }
													 * 
													 * });
													 * 
													 * } else { // 关于 Intent
													 * intent = new
													 * Intent(mActivity,
													 * AboutActivity.class);
													 * startActivity(intent); }
													 */
					} else if (childPosition == 3) {/*
													 * if (application.mUser !=
													 * null) { // 关于 Intent
													 * intent = new
													 * Intent(mActivity,
													 * AboutActivity.class);
													 * startActivity(intent); }
													 * else { // �? }
													 */
					}
				}
			}

		});

	}

	

}
