package com.yimeng.baolingtong;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andbase.web.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.PhotoAddUtil;
import com.yimeng.baolingtong.adpater.GuarderAdapter;
public class AddChildActivity extends Activity {
	private Spinner mSpinner;
	private Button ac_brith;
	private Calendar c ;
	protected DatePickerDialog dialog;
	private ImageView ac_image;
	private TextView saveText;
	private EditText ac_imei;
	
	private TextView guarder;
	private GridView locationList;
	private GuarderAdapter locationAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_child_layout);
	    // 初始化控件
		mSpinner = (Spinner) findViewById(R.id.ac_spinner);
		ac_brith=(Button) findViewById(R.id.ac_brith);
		ac_image=(ImageView) findViewById(R.id.ac_image);
		ac_imei=(EditText) findViewById(R.id.ac_imei);
		ac_imei.setText("358688000000158");
		saveText=(TextView) findViewById(R.id.tv_add_child_layout_pass);
		
		guarder = (TextView) findViewById(R.id.iv_add_child_layout_parent_photo);
		guarder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showGuarderSelectedInterface(AddChildActivity.this);
			}
		});
		
		//定位颜色
		locationList = (GridView) findViewById(R.id.location_list);
		final String[] data = new String[]{"颜色1","颜色2","颜色3","颜色4","颜色5","颜色6","颜色7","颜色8"};
		locationAdapter = new GuarderAdapter(AddChildActivity.this, data);
		locationList.setAdapter(locationAdapter);
		locationList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				locationAdapter.setSelectedPosition(position);
				locationAdapter.notifyDataSetChanged();
			}
		});

		// 建立数据源
		String[] mItems = new String[]{"男","女"};
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, mItems);
		//绑定 Adapter到控件
		mSpinner.setAdapter(_Adapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parent, View view,
		            int position, long id) {
		        /*String str=parent.getItemAtPosition(position).toString();
		        Toast.makeText(AddChildActivity.this, "你点击的是:"+str, 2000).show();*/
		    }
		    @Override
		    public void onNothingSelected(AdapterView<?> parent) {
		    	   
		    }
		});
		ac_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showAddChildPhotoDialog(AddChildActivity.this);
			}
		});
		saveText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addDevPost();
				//定位图标颜色的position
				int locationColorPosition = locationAdapter.getSelectedPosition();
				
			}
		});
		ac_brith.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View arg0) {
				String[] date=ac_brith.getText().toString().split("-");
				c = Calendar.getInstance();
				c.set(Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2]));
	            dialog = new DatePickerDialog(
	                AddChildActivity.this,
	                new DatePickerDialog.OnDateSetListener() {
	                	@Override
	                    public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
	                		 ac_brith.setText( year + "-" + (month+1) + "-" + dayOfMonth );
	                		 dialog.cancel();
	                    }
	                }, 
	                c.get(Calendar.YEAR), // 传入年份
	                c.get(Calendar.MONTH), // 传入月份
	                c.get(Calendar.DAY_OF_MONTH) // 传入天数
	            );
	            dialog.show();
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		showDialog();
		
	}
	@SuppressLint("NewApi") public void sendCodePost() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("otype", "getdevstatus");
        params.addBodyParameter("dev_imei", ac_imei.getText().toString());
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

                    @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                    	try {
							JSONObject obj=new JSONObject(responseInfo.result);
							if(obj.has("opresult")){
								String opresult=obj.getString("opresult");
								if("error".equals(opresult)){
									//Toast.makeText(getActivity(), "服务异常！", 1000).show();
								}else if("success".equals(opresult)){
									//Toast.makeText(getActivity(), "获取位置信息成功！", 1000).show();
									
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                    	
                    	Toast.makeText(AddChildActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                });
    }
	@SuppressLint("NewApi") public void addDevPost() {
		//"111111111111119"
        RequestParams params = new RequestParams();
        params.addBodyParameter("otype", "adddev");
        params.addBodyParameter("dev_name", "getdevstatus");
        params.addBodyParameter("name", "longpaopao");
        params.addBodyParameter("dev_sex", "1");
        params.addBodyParameter("dev_imei", ac_imei.getText().toString());
        params.addBodyParameter("Cid", "2223b4ae8f271b5a6e3e00947bc8ef50");
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

                    @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                    	try {
							JSONObject obj=new JSONObject(responseInfo.result);
							if(obj.has("opresult")){
								String opresult=obj.getString("opresult");
								if("error".equals(opresult)){
									//Toast.makeText(getActivity(), "服务异常！", 1000).show();
								}else if("success".equals(opresult)){
									//Toast.makeText(getActivity(), "获取位置信息成功！", 1000).show();
									Intent intent=new Intent(AddChildActivity.this,SlidingMenuRightActivity.class);
									intent.putExtra("imie",ac_imei.getText().toString() );
									startActivity(intent);
								}else if("existeddev".equals(opresult)){
									Intent intent=new Intent(AddChildActivity.this,SlidingMenuRightActivity.class);
									intent.putExtra("imie",ac_imei.getText().toString() );
									startActivity(intent);
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                    	
                    	//Toast.makeText(getActivity(), "获取位置信息失败！", Toast.LENGTH_LONG).show();
                    }
                });
    }
	private void showDialog() {
          LayoutInflater inflater=LayoutInflater.from(AddChildActivity.this);
          final View v1=inflater.inflate(R.layout.dialog_confirm,null);
          Button  dcon_ok=(Button) v1.findViewById(R.id.dcon_ok);
          dcon_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		  }); 
          AlertDialog.Builder dialog=new AlertDialog.Builder(AddChildActivity.this);
          dialog.setTitle("提醒");
          dialog.setView(v1);//设置使用View
          dialog.show();
        
    };
    
    private void showAddChildPhotoDialog(Context context){
    	final Dialog dialog = new Dialog(context,R.style.MyDialog);
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.add_child_photo, null);
		View photoAlbum = contentView.findViewById(R.id.photo_album);
    	View camera = contentView.findViewById(R.id.camera);
    	photoAlbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 从相册中去获取
				try {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
					intent.setType("image/*");
					startActivityForResult(intent, PhotoAddUtil.PHOTO_PICKED_WITH_DATA);
					dialog.dismiss();
				} catch (ActivityNotFoundException e) {
					showMessage("没有找到照片");
				}
			}
		});
    	camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				PhotoAddUtil.doTakePhoto(AddChildActivity.this);
				dialog.dismiss();
			}
		});
    	dialog.setContentView(contentView);
    	/*Window dialogWindow = dialog.getWindow();
    	WindowManager.LayoutParams wmlp= dialogWindow.getAttributes();
    	wmlp.x = 100; // X坐标
    	wmlp.y = 100; // Y坐标
    	wmlp.width = 300; 
    	wmlp.height = 300; 
    	//wmlp.alpha = 0.7f; // 透明度
    	dialogWindow.setAttributes(wmlp);*/
    	dialog.show();
    }
    
//    private int selectedGuarder = 0;
    private void showGuarderSelectedInterface(Context context){
    	final Dialog dialog = new Dialog(context,R.style.MyDialog);
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.guarder_selected, null);
		final GridView guarderList = (GridView) contentView.findViewById(R.id.guarder_list);
		final String[] data = new String[]{"爸爸","妈妈","爷爷","奶奶","叔叔","阿姨","外婆","外公","其他"};
		final GuarderAdapter adapter = new GuarderAdapter(context, data);
		guarderList.setAdapter(adapter);
		
		View cancel = contentView.findViewById(R.id.cancel);
		View confirm = contentView.findViewById(R.id.confirm);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				guarder.setText(adapter.getItem(adapter.getSelectedPosition()));
			}
		});
		dialog.setContentView(contentView);
		
		guarderList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				showMessage("position="+position);
				adapter.setSelectedPosition(position);
//				guarderList.requestLayout();
//				guarderList.invalidate();
				adapter.notifyDataSetInvalidated();
				
//				selectedGuarder = position;
			}
		});
		
    	dialog.show();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
    	if (resultCode != RESULT_OK){
			return;
		}
		try {
			switch (requestCode) {
			case PhotoAddUtil.PHOTO_PICKED_WITH_DATA:
				Uri uri = mIntent.getData();
				String currentFilePath = PhotoAddUtil.getPath(AddChildActivity.this, uri);
//				Bitmap bm = BitmapFactory.decodeFile(currentFilePath);
//				ac_image.setImageBitmap(bm);
				PhotoAddUtil.startPhotoZoom(this, currentFilePath);
				break;
			case PhotoAddUtil.CAMERA_WITH_DATA:
				String currentFilePath2 = PhotoAddUtil.getCurrentPhotoPath();
				PhotoAddUtil.startPhotoZoom(this, currentFilePath2);
				break;
			case PhotoAddUtil.CAMERA_CROP_DATA:
				String path = PhotoAddUtil.getCurrentPhotoPath();
				Bitmap bm2 = BitmapFactory.decodeFile(path);
				ac_image.setImageBitmap(bm2);
				break;
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void showMessage(String text){
    	Toast.makeText(AddChildActivity.this, text, Toast.LENGTH_SHORT).show();
    }

}
