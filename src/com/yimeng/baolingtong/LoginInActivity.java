package com.yimeng.baolingtong;

import org.json.JSONException;
import org.json.JSONObject;

import com.andbase.web.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginInActivity extends Activity {
	private Button registerBtn;
	private Button am_btn_load;
	private EditText am_name;
	private EditText am_pass;
	private SharedPreferences share;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_in_layout);
		share = super.getSharedPreferences("longke",
				Activity.MODE_PRIVATE); // 指定操作的文件名
		registerBtn=(Button) findViewById(R.id.am_btn_register);
		am_name=(EditText) findViewById(R.id.am_name);
		am_pass=(EditText) findViewById(R.id.am_pass);
		am_name.setText("123456");
		am_pass.setText("123");
		registerBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(LoginInActivity.this,RegisterActivity.class);
				startActivity(intent);
				
			}
		});
		am_btn_load=(Button) findViewById(R.id.am_btn_load);
		am_btn_load.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				testPost();
			}
		});
	}
	public void testPost() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("otype", "login");
        params.addBodyParameter("name", am_name.getText().toString().trim());
        params.addBodyParameter("u_pwd", am_pass.getText().toString().trim());
        params.addBodyParameter("Cid", "123");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,Urls.BASE_URL
                ,
                params,
                new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                    	try {
							JSONObject obj=new JSONObject(responseInfo.result);
							if(obj.has("opresult")){
								String opresult=obj.getString("opresult");
								if("error".equals(opresult)){
									Toast.makeText(LoginInActivity.this, "服务异常！", 1000).show();
								}else if("success".equals(opresult)){
									share.edit().putString("userName", am_name.getText().toString().trim()).commit();
									Toast.makeText(LoginInActivity.this, "登录成功！", 1000).show();
									Intent intent=new Intent(LoginInActivity.this,AddChildActivity.class);
									startActivity(intent);
									finish();
								}else if("noname".equals(opresult)){
									Toast.makeText(LoginInActivity.this, "用户名未注册！", 1000).show();
								}else if("pwderror".equals(opresult)){
									Toast.makeText(LoginInActivity.this, "密码错误！", 1000).show();
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                    	Toast.makeText(LoginInActivity.this, "注册失败！", 1000).show();
                    }
                });
    }

}
