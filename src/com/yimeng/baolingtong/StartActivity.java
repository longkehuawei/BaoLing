package com.yimeng.baolingtong;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;



public class StartActivity extends Activity {
	private Animation myAnimation_Alpha;
	private View view;
	private SharedPreferences share = null;
	private Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = getLayoutInflater().inflate(R.layout.start_loading, null);
		setContentView(view);
		/*share = getSharedPreferences(Constants.SHARE_FILE_NAME,
				Activity.MODE_PRIVATE); // 指定操作的文件名
*/		setAnimation();
	}

	private void setAnimation() {
		myAnimation_Alpha = new AlphaAnimation(0.1f, 1.0f);
		myAnimation_Alpha.setDuration(1500);
		myAnimation_Alpha.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = null;
//				intent = new Intent(StartActivity.this,
//						LoginInActivity.class);
				intent = new Intent(StartActivity.this,
						AddChildActivity.class);
//				intent=new Intent(StartActivity.this,SlidingMenuRightActivity.class);
				startActivity(intent);
				StartActivity.this.finish();
				/*FinalHttp fh = new FinalHttp(null);
				fh.get("http://www.sz5678.com:806/xiaoyan.aspx",
						new SubscribeJobCallBack());
				if (!share.getBoolean("isFinish", false)) {
					Intent intent = null;
					intent = new Intent(StartActivity.this,
							MainActivityGroup.class);
					startActivity(intent);
					StartActivity.this.finish();
				}else{
					Toast.makeText(StartActivity.this, "服务异常",3000).show();
				}*/
				/*
				 * intent = new Intent(StartActivity.this, LoginActivity.class);
				 * startActivity(intent); StartActivity.this.finish();
				 */
			}
		});
		view.startAnimation(myAnimation_Alpha);
	}

	

}