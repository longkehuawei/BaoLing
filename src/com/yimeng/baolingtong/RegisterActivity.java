package com.yimeng.baolingtong;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andbase.web.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class RegisterActivity extends Activity {
	private EditText rl_name;
	private EditText tv_auto_email;
	private EditText rl_pass;
	private EditText rl_confirm_passcon;
	private Button rl_reg;
	private LinearLayout rl_title;
	//private EditText rl_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		rl_name=(EditText) findViewById(R.id.rl_name);
		tv_auto_email=(EditText) findViewById(R.id.tv_auto_email);
		rl_pass=(EditText) findViewById(R.id.rl_pass);
		rl_confirm_passcon=(EditText) findViewById(R.id.rl_confirm_passcon);
		rl_reg=(Button) findViewById(R.id.rl_reg_);
		rl_title=(LinearLayout) findViewById(R.id.rl_title);
		rl_title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		rl_reg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(rl_name.getText().toString().trim()) ){
					Toast.makeText(RegisterActivity.this, "用户名不能为空！", 1000).show();
					return;
				}
				if(TextUtils.isEmpty(tv_auto_email.getText().toString().trim()) ){
					Toast.makeText(RegisterActivity.this, "邮箱不能为空！", 1000).show();
					return;
				}
				if(TextUtils.isEmpty(rl_pass.getText().toString().trim()) ){
					Toast.makeText(RegisterActivity.this, "密码不能为空！", 1000).show();
					return;
				}
				if(!rl_confirm_passcon.getText().toString().trim().equals(rl_pass.getText().toString().trim())){
					Toast.makeText(RegisterActivity.this, "两次输入的密码不一致，请重新输入！", 1000).show();
					return;
				}
				testPost();
			}
		});
		
	}
	public void testPost() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("otype", "add");
        params.addBodyParameter("name", rl_name.getText().toString().trim());
        params.addBodyParameter("u_email", tv_auto_email.getText().toString().trim());
        params.addBodyParameter("u_pwd", rl_pass.getText().toString());
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
									Toast.makeText(RegisterActivity.this, "服务异常！", 1000).show();
								}else if("success".equals(opresult)){
									Intent intent=new Intent(RegisterActivity.this,SlidingMenuRightActivity.class);
									startActivity(intent);
									finish();
									Toast.makeText(RegisterActivity.this, "注册成功！", 1000).show();
								}else if("register".equals(opresult)){
									Toast.makeText(RegisterActivity.this, "已经注册！", 1000).show();
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                    	Toast.makeText(RegisterActivity.this, "注册失败！", 1000).show();
                    }
                });
    }
	

}
