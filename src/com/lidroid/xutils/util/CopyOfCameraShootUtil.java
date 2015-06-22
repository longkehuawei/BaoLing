package com.lidroid.xutils.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.yimeng.baolingtong.AddChildActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class CopyOfCameraShootUtil {
	public static void openCamera(Activity activity,File saveDir) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
//		String saveDir = getCacheDirectory();
		Uri outputFileUri = Uri.fromFile(saveDir);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		activity.startActivityForResult(intent, AddChildActivity.RESULT_CANCELED);
	}

	public static void compressPhoto(String saveDir) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			Bitmap bitmap = BitmapFactory.decodeFile(saveDir, options);
			// 压缩图片
			// bitmap = compressImage(bitmap,500);

			if (bitmap != null) {
				// 显示图片
				// iv.setImageBitmap(bitmap);
				// 保存图片
				FileOutputStream fos = null;
				fos = new FileOutputStream(new File(saveDir));
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
			// TODO: handle exception

		}
	}

	/**
	 * 裁剪图片
	 * 
	 * @param uri
	 */
	public static void startPhotoZoom(Activity activity, Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
		intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
		intent.putExtra("aspectY", 1);// x:y=1:1
		intent.putExtra("outputX", 200);// 图片输出大小
		intent.putExtra("outputY", 200);
		intent.putExtra("output", uri);
		intent.putExtra("outputFormat", "JPEG");// 返回格式
		activity.startActivityForResult(intent, 3);
	}

	/**
	 * 将图片image压缩成大小为 size的图片（size表示图片大小，单位是KB）
	 * 
	 * @param image
	 *            图片资源
	 * @param size
	 *            图片大小
	 * @return Bitmap
	 */
	private Bitmap compressImage(Bitmap image, int size) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int options = 100;
		// 循环判断如果压缩后图片是否大于100kb,大于继续压缩
		while (baos.toByteArray().length / 1024 > size) {
			// 重置baos即清空baos
			baos.reset();
			// 每次都减少10
			options -= 10;
			// 这里压缩options%，把压缩后的数据存放到baos中
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);

		}
		// 把压缩后的数据baos存放到ByteArrayInputStream中
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		// 把ByteArrayInputStream数据生成图片
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}
	
	 private static final String CACHE_DIRECTORY = "baoLinTong";
	 private static String childPhotoName = "childPhoto";

	/** 获得缓存目录 **/
	public static String getCacheDirectory() {
		
		String dir = getSDPath() + "/" + CACHE_DIRECTORY+"/"+childPhotoName;
//		dir = "emulated/0/baoLinTong/childPhoto";
		File file = new File(dir);
		return dir;
	}

	/** 获取SD卡路径 **/
	private static String getSDPath() {
		String sdcardPath = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdcardPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath(); // 获取根目录
		}
		if (sdcardPath != null) {
			return sdcardPath;
		} else {
			return "";
		}
	}

}
