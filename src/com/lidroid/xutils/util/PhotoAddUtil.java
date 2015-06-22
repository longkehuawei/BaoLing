package com.lidroid.xutils.util;

import java.io.File;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.ab.util.AbFileUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;

public class PhotoAddUtil {
	/* 用来标识请求照相功能的requestCode */
	public static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的requestCode */
	public static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 用来标识请求裁剪图片后的requestCode */
	public static final int CAMERA_CROP_DATA = 3022;
	
	/* 拍照的照片存储位置 */
	private static File PHOTO_DIR = null;
	// 照相机拍照得到的图片
	private static File mCurrentPhotoFile;
	
	private static void initPhotoDir(Activity activity){
		//初始化图片保存路径
		String photo_dir = AbFileUtil.getImageDownloadDir(activity);
		if(AbStrUtil.isEmpty(photo_dir)){
			AbToastUtil.showToast(activity,"存储卡不存在");
		}else{
			PHOTO_DIR = new File(photo_dir);
		}
	}
	
	/**
	 * 拍照获取图片
	 */
	public static void doTakePhoto(Activity activity) {
		try {
			String mFileName = System.currentTimeMillis() + ".jpg";
			if(PHOTO_DIR == null){
				initPhotoDir(activity);
			}
			mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
			activity.startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
			AbToastUtil.showToast(activity,"未找到系统相机程序");
		} finally{
		}
	}
	
	/**
	 * 开发宝 从相册得到的url转换为SD卡中图片路径
	 *//*
	public static String getPath(Activity activity,Uri uri) {
		if(AbStrUtil.isEmpty(uri.getAuthority())){
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}*/
	
	
	/*
	 * 4.4,如果使用上面pick的原生方法来选图,返回的uri还是正常的,但如果用ACTION_GET_CONTENT的方法,返回的uri跟4.3
	 * 是完全不一样的,4.3返回的是带文件路径的,而4.4返回的却是
	 * content://com.android.providers.media.documents/document/image:3951这样的,
	 * 没有路径,只有图片编号的uri.这就导致接下来无法根据图片路径来裁剪的步骤了.
	 */public static String getPath(final Context context, final Uri uri) {  
		  
	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  
	  
		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	} 
	
	public static String getDataColumn(Context context, Uri uri, String selection,  
	        String[] selectionArgs) {  
	  
	    Cursor cursor = null;  
	    final String column = "_data";  
	    final String[] projection = { column };  
	  
	    try {  
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
	                null);  
	        if (cursor != null && cursor.moveToFirst()) {  
	            final int index = cursor.getColumnIndexOrThrow(column);  
	            return cursor.getString(index);  
	        }  
	    } finally {  
	        if (cursor != null)  
	            cursor.close();  
	    }  
	    return null;  
	}  
	
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is ExternalStorageProvider. 
	 */  
	public static boolean isExternalStorageDocument(Uri uri) {  
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is DownloadsProvider. 
	 */  
	public static boolean isDownloadsDocument(Uri uri) {  
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is MediaProvider. 
	 */  
	public static boolean isMediaDocument(Uri uri) {  
	    return "com.android.providers.media.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is Google Photos. 
	 */  
	public static boolean isGooglePhotosUri(Uri uri) {  
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
	}
	
	/**
	 * 裁剪图片
	 * 
	 * @param uri
	 */
	public static void startPhotoZoom(Activity activity, String currentFilePath) {
		
		if(!AbStrUtil.isEmpty(currentFilePath)){
			mCurrentPhotoFile = new File(currentFilePath);
			Uri uri = Uri.fromFile(mCurrentPhotoFile);
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.putExtra("PATH", currentFilePath);
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
			intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
			intent.putExtra("aspectY", 1);// x:y=1:1
			intent.putExtra("outputX", 200);// 图片输出大小
			intent.putExtra("outputY", 200);
			intent.putExtra("output", uri);
			intent.putExtra("outputFormat", "jpg");// 返回格式
			activity.startActivityForResult(intent, CAMERA_CROP_DATA);
        }else{
        	AbToastUtil.showToast(activity,"未在存储卡中找到这个文件");
        }
	}
	
	public static String getCurrentPhotoPath(){
		return mCurrentPhotoFile.getPath();
	}

}
