package com.ustcinfo.mobile.platform.core.utils;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by 学祺 on 2017/9/25.
 */
@GlideModule
public class GlideConfigModule extends AppGlideModule {
    /**
     * MemorySizeCalculator类通过考虑设备给定的可用内存和屏幕大小想出合理的默认大小.
     * 通过LruResourceCache进行缓存。
     * @param context
     * @param builder
     */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
    }
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

}