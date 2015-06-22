package com.yimeng.baolingtong;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ab.fragment.AbFragment;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.CancelableCallback;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.andbase.web.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yimeng.baolingtong.menu.ContextMenuDialogFragment;
import com.yimeng.baolingtong.menu.MenuManager;
import com.yimeng.baolingtong.menu.interfaces.OnMenuItemClickListener;
import com.yimeng.baolingtong.menu.interfaces.OnMenuItemLongClickListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainFragment extends AbFragment implements LocationSource,
		AMapLocationListener{
	private AMap aMap;
	private MapView mapView;
	private ImageView naviIcon;
	private static final int EXPAND = 0,COLLAPSE = 1;
	private static int currentIconState = COLLAPSE;
	private MenuManager menuManager;
	
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private Circle circle;
	private SlidingMenuRightActivity slidingMenu;
	private SharedPreferences share;
	private Double celllat;
	private Double celllong;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.locationsensorsource_activity,
				null);

		testPost();
		mapView = (MapView) view.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// �˷���������д
		
		naviIcon = (ImageView) view.findViewById(R.id.menu_icon);
		naviIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentIconState = EXPAND;
				int[] location = new int[2];
				naviIcon.getLocationOnScreen(location);
				naviIcon.getLayoutParams();
				int x = location[0];
				int y = location[1];
				menuManager = new MenuManager(getActivity(), x
						+ naviIcon.getWidth(), y + naviIcon.getHeight() / 2);
				menuManager.show(MainFragment.this.getFragmentManager());
				naviIcon.setImageResource(R.drawable.navi_icon_collapse);
				
			}
		});
		
		init();
		return view;
	}
	
	public static boolean isShowMainMenu(){
		if(currentIconState == COLLAPSE){
			return false;
		} else if(currentIconState == EXPAND){
			return true;
		}
		return false;
	}
	
	public void closeMainMenu(){
		currentIconState = COLLAPSE;
		naviIcon.setImageResource(R.drawable.navi_icon_expand);
	}

	/**
	 * ��ʼ��AMap����
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		slidingMenu = (SlidingMenuRightActivity) activity;
		share = slidingMenu.getSharedPreferences("longke",
				Activity.MODE_PRIVATE); // 指定操作的文件名
	}

	/**
	 * ����һЩamap������
	 */
	private void setUpMap() {
		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(celllat,
				celllong), 12));// ����ָ���Ŀ��������ͼ
		aMap.addMarker(new MarkerOptions().position(
				new LatLng(celllat, celllong)).icon(
				BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		// ����һ��Բ��
		circle = aMap.addCircle(new CircleOptions()
				.center(new LatLng(celllat, celllong)).radius(4000)
				.strokeColor(Color.argb(10, 1, 1, 1))
				.fillColor(Color.argb(30, 1, 1, 1)).strokeWidth(5));

		/*
		 * circle.setFillColor(Color.argb(1, 1, 1, 1));
		 * circle.setStrokeColor(Color.argb(1, 1, 1, 1));
		 * circle.setStrokeWidth(1);
		 */
		/*
		 * // �Զ���ϵͳ��λС���� MyLocationStyle myLocationStyle = new
		 * MyLocationStyle();
		 * myLocationStyle.myLocationIcon(BitmapDescriptorFactory
		 * .fromResource(R.drawable.location_marker));// ����С�����ͼ��
		 * myLocationStyle.strokeColor(Color.BLACK);// ����Բ�εı߿���ɫ
		 * myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));//
		 * ����Բ�ε������ɫ // myLocationStyle.anchor(int,int)//����С�����ê��
		 * myLocationStyle.strokeWidth(1.0f);// ����Բ�εı߿��ϸ
		 */
		// aMap.setMyLocationStyle(myLocationStyle);

		// mListener.onLocationChanged(aLocation);// ��ʾϵͳС����

		/*
		 * aMap.setLocationSource(this);// ���ö�λ����
		 * aMap.getUiSettings().setMyLocationButtonEnabled(true);//
		 * ����Ĭ�϶�λ��ť�Ƿ���ʾ aMap.setMyLocationEnabled(true);//
		 * ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
		 */// aMap.setMyLocationType()
	}

	/**
	 * ���ݶ�����ť״̬�����ú���animateCamera��moveCamera���ı��������
	 */
	private void changeCamera(CameraUpdate update, CancelableCallback callback) {
		aMap.animateCamera(update, callback);
	}

	@SuppressLint("NewApi")
	public void testPost() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("otype", "getdevstatus");
		params.addBodyParameter("dev_imei", slidingMenu.imie);
		params.addBodyParameter("name", share.getString("userName", ""));
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, Urls.BASE_URL, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@TargetApi(Build.VERSION_CODES.HONEYCOMB)
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject obj = new JSONObject(responseInfo.result);
							if (obj.has("opresult")) {
								String opresult = obj.getString("opresult");
								if ("error".equals(opresult)) {
									Toast.makeText(getActivity(), "服务异常！", 1000)
											.show();
								} else if ("success".equals(opresult)) {
									Toast.makeText(getActivity(), "获取位置信息成功！",
											1000).show();
									if (obj.has("celllat")) {
                                       celllat=Double.parseDouble(obj.getString("celllat"));
									}
									if (obj.has("celllong")) {
										celllong=Double.parseDouble(obj.getString("celllong"));
									}
									if (obj.has("addr")) {
										slidingMenu.addressText.setText(obj.getString("addr"));
									}
									if(obj.has("utctime")){
										 long timeLng=obj.getLong("utctime");
										 java.util.Date date = new java.util.Date(timeLng);
										 java.util.Calendar cal = java.util.Calendar.getInstance();
										 cal.setTime(date);
										 slidingMenu.time_text.setText(cal.get(java.util.Calendar.YEAR)+"-"+cal.get(java.util.Calendar.MONTH)+"-"+cal.get(java.util.Calendar.DAY_OF_MONTH)+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":" +cal.get(Calendar.SECOND));
									}
									
									setUpMap();
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {

						Toast.makeText(getActivity(), "获取位置信息失败！",
								Toast.LENGTH_LONG).show();
					}
				});
	}

	/**
	 * ����������д
	 */
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * ����������д
	 */
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * ����������д
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * ����������д
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * �˷����Ѿ�����
	 */
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * ��λ�ɹ���ص�����
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// ��ʾϵͳС����
		}
	}

	/**
	 * ���λ
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy
					.getInstance(getActivity());

			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	/**
	 * ֹͣ��λ
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

}
