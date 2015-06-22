package com.yimeng.baolingtong;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.os.Looper;
import android.widget.Toast;



public class MyApp extends Application implements UncaughtExceptionHandler {

	public static MyApp getMyApp() {
		return myApp;
	}

	public static void setMyApp(MyApp myApp) {
		MyApp.myApp = myApp;
	}
	private Thread.UncaughtExceptionHandler mDefaultHandler;
 
	private static MyApp myApp;
	private static int mPosition;
	private static boolean isToday=true;

	public static boolean isToday() {
		return isToday;
	}

	public static void setToday(boolean isToday) {
		MyApp.isToday = isToday;
	}

	public static int getmPosition() {
		return mPosition;
	}

	public static void setmPosition(int mPosition) {
		MyApp.mPosition = mPosition;
	}

	
	public static MyApp getIntance() {
		return myApp;
	}

	public void onCreate() {
		super.onCreate();

		myApp = this;
		
		// CrashHandler ch = CrashHandler.getInstance();
		// ch.init(this);
		this.init();
	}
    
	public void init() {
//		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
//		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	public void uncaughtException(Thread thread, Throwable ex) {
//		if (!handleException(ex) && mDefaultHandler != null) {
//			// ����û�û�д�������ϵͳĬ�ϵ��쳣������������?
//			Log.d("+++++++++++++++uncaughtException");
//			mDefaultHandler.uncaughtException(thread, ex);
//		} else {
//			// Sleepһ����������
//			try {
//				if (this.mBleController != null)
//					this.mBleController.close();
//
//				Log.d("+++++++++++++++sleep");
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//
//			}
//			android.os.Process.killProcess(android.os.Process.myPid());
//			System.exit(10);
//		}

	}

	/**
	 * �Զ��������?,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����. �����߿��Ը����Լ���������Զ����쳣�����߼�?
	 * 
	 * @param ex
	 * @return true:��������˸��쳣���?;���򷵻�false
	 */
	private boolean handleException(Throwable ex) {
		if(ex!=null)
			ex.printStackTrace();
		
		// ʹ��Toast����ʾ�쳣��Ϣ
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(MyApp.this, "Sorry, this application got an error for runtime. please to restart the APP.", Toast.LENGTH_LONG)
						.show();
				Looper.loop();
			}

		}.start();

		return true;
	}

}
