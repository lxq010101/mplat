package com.ustcinfo.mobile.platform.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;

import com.ustcinfo.mobile.platform.core.config.MConfig;
import com.ustcinfo.mobile.platform.core.constants.Constants;
import com.ustcinfo.mobile.platform.core.core.MApplication;
import com.ustcinfo.mobile.platform.core.interfaces.FileCallBack;
import com.ustcinfo.mobile.platform.core.log.Logger;
import com.ustcinfo.mobile.platform.core.model.UserInfo;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 实现图片的异步载入显示
 * 
 */
public class AsyncImageLoader {
	
	//软引用对象，在响应内存需要时，由垃圾回收器决定是否清除此对象。软引用对象最常用于实现内存敏感的缓存。
	private HashMap<String, SoftReference<Drawable>> imageCache;
    
	private static AsyncImageLoader mInstance ;
	
	private String imageDir ;
	
	
    
    public static synchronized AsyncImageLoader get(){
    	if(mInstance == null){
    		mInstance = new AsyncImageLoader();
    	}
    	return mInstance ;
    }

	private AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	    imageDir = getImgPath() ;
		createImgHideFile(imageDir) ;
	}
	
	public String getImgPath(){
    	String path = null ;
    	if (SDCardUtils.isHavaExternalStorage()) {
    		path = Environment.getExternalStorageDirectory().toString() + Constants.PATH_DOWNLOAD_IMG;
    		File f= new File(path) ;
    		if(!f.exists())
    			f.mkdirs() ;
    	}else{
    		path = MApplication.getApplication().getCacheDir().getAbsolutePath() ;
    	}
    	return path ;
	 }
	
	 public void loadIconByAppId(Context context ,ImageView imgView, String id, Drawable defaultDrawable) {
		Drawable cacheImage = loadDrawable(context, id, imgView, new ImageCallback() {
			@Override
			public void imageLoaded(Drawable drawable, ImageView imageView, String imageName) {
				imageView.setImageDrawable(drawable);
			}
		});

		if (cacheImage != null) {
			imgView.setImageDrawable(cacheImage);
		} else if (defaultDrawable != null) {
			imgView.setImageDrawable(defaultDrawable);
		}
	}
	

	public Drawable loadDrawable(final Context context,final String iconId,
			final ImageView imageView,final ImageCallback imagecallback) {
		Drawable imgDrawable = null ;
		if(TextUtils.isEmpty(iconId))
			return null ;
		
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				imagecallback.imageLoaded((Drawable) msg.obj, imageView, iconId) ;
			}
		};

		// 从缓存中读取
		if (imageCache.containsKey(iconId)) {
			SoftReference<Drawable> softReference = imageCache.get(iconId);
		    imgDrawable = softReference.get();
			if (imgDrawable != null) {
				return imgDrawable;
			}
		}
		
		File f = new File(imageDir+iconId) ;
		if(isFileExist(f)){
			Bitmap b = BitmapUtils.readImageCompressBmp(f) ;
			Drawable drawable = new BitmapDrawable(context.getResources(),b) ;
			imageCache.put(iconId, new SoftReference<Drawable>(drawable));
			Message message = handler.obtainMessage(0, drawable);
			handler.sendMessage(message);
		}else{
			String url = MConfig.get("downloadAppIcon")+"?"+"appId="+iconId+"&ticket="+ UserInfo.get().getTicket() ;
			Logger.d("img load", "load app icon :"+url);
			MHttpClient.get().getFileByUrl(url, new FileCallBack() {
				
				@Override
				public void onResposne(File file) {
					//返回压缩图片
					Bitmap bmp = BitmapUtils.readImageCompressBmp(file) ;
					Drawable drawable = new BitmapDrawable(context.getResources(),bmp) ;
					imageCache.put(iconId, new SoftReference<Drawable>(drawable));
					imagecallback.imageLoaded(drawable, imageView, iconId);
				}
				
				@Override
				public void onError(String msg) {
				}
				
				@Override
				public void inProgress(int progress) {
				}
			}, new File(imageDir, iconId));
		}
		
		return imgDrawable;
	}
		
	public void createImgHideFile(String dir){
		//创建图片隐藏文件
		File imgHideFile = new File(dir+".nomedia");
		if(!imgHideFile.exists()){
			try{
				imgHideFile.createNewFile();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable drawable, ImageView imageView, String imageName);
	}
	
	/**
	 * 判断当前目录是否存在此文件
	 * 不存在或者文件大小为0的则返回false
	 * */
	private boolean isFileExist(File file){
		long len = file.length();
		if(file.exists() && len > 0){
			return true ;
		}
		return false ;
	}
	
}