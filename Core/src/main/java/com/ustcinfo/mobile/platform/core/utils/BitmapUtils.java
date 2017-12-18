package com.ustcinfo.mobile.platform.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {
	
	// 访问高清图片
	public static Bitmap readImageSourceBmp(File file) {
		Bitmap bmp = null ;

		Options opts = new Options() ;
		opts.inTempStorage=new byte[(int) file.length()+1024];
		opts.inDither =false ;
		opts.inPreferredConfig = null ;
		opts.inPurgeable = true ;
		opts.inInputShareable = true;
		try {
			bmp = BitmapFactory.decodeFile(file.getPath(), opts);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}
		return bmp ;
	}

	// 访问压缩图片
	public static Bitmap readImageCompressBmp(File file){
		Bitmap bmp = null ;
		Options opts = new Options() ;
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getPath(), opts);
		opts.inSampleSize = BitmapUtils.computeSampleSize(opts, -1, 600*600);
		//这里一定要将其设置回false，因为之前我们将其设置成了true
		opts.inJustDecodeBounds = false;
		try {
			bmp = BitmapFactory.decodeFile(file.getPath(), opts);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}
		return bmp ;
	}

	/**
	 * 从本地读取文件
	 * */
	public static Bitmap readImage4Local(Context context, String fileName) throws IOException{
		File file = new File(context.getFilesDir().getAbsolutePath()+fileName) ;
		Options opts = new Options() ;
		opts.inTempStorage=new byte[(int)file.length()+1024];
		opts.inDither =false ;
		opts.inPreferredConfig = null ;
		opts.inPurgeable = true ;
		opts.inInputShareable = true;
		Bitmap bm = BitmapFactory.decodeFile(file.getPath(), opts);
        return bm ;
	}


	/**
	 * 将bitmap保存为文件
	 * */
	public static void saveMyBitmap4SDCard(String imageDir, String imageName, Bitmap bitmap){
		FileOutputStream fOut = null;
		Log.d("csf", imageDir+imageName) ;
		try {
			boolean flag = false;
			File dir = new File(imageDir);
			if(!dir.exists()){
				flag =  dir.mkdirs();
				Log.d("csf", imageDir + "创建结果：" + flag);
			}
			if(!dir.isDirectory()){
				flag =  dir.mkdirs();
				Log.d("csf", imageDir + "创建结果：" + flag);
			}
			File f = new File(imageDir+imageName);
			f.createNewFile();
			fOut = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 50, fOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * 将图片存储到手机本地
	 * **/
	public static void savaMyBitmap4Loacal(String fileName, Context context, Bitmap bitmap){
		FileOutputStream fOut = null ;
		try {
			fOut = context.openFileOutput(fileName, context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public static int computeSampleSize(Options options,
	        int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength,
	            maxNumOfPixels);

	    int roundedSize;
	    if (initialSize <= 8) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }

	    return roundedSize;
	}

	private static int computeInitialSampleSize(Options options,
	        int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;

	    int lowerBound = (maxNumOfPixels == -1) ? 1 :
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 :
	            (int) Math.min(Math.floor(w / minSideLength),
	            Math.floor(h / minSideLength));

	    if (upperBound < lowerBound) {
	        return lowerBound;
	    }

	    if ((maxNumOfPixels == -1) &&
	            (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	}   


}
