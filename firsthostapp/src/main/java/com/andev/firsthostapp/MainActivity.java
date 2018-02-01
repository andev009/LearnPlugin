package com.andev.firsthostapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "MainActivity";

	TextView show_tv;
	ImageView show_iv;
	Button load_bt;
	Button launch_bt;

	File fileRelease;
	DexClassLoader classLoader;
	AssetManager mAssetManager;
	Resources mResources;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initClassLoader();
		initViews();
	}

	private void initClassLoader() {
		String filesDir = getCacheDir().getAbsolutePath();
		String filePath = filesDir + File.separator + "plugin-debug.apk";

		fileRelease = getDir("dex", 0);

		classLoader = new DexClassLoader(filePath, fileRelease.getAbsolutePath(), null, getClassLoader());

		loadResources(filePath);

		Log.d(TAG, "filePath : " + filePath);
		Log.d(TAG, "fileRelease : " + fileRelease.getAbsolutePath());
	}

	private void initViews() {
		show_tv = (TextView) findViewById(R.id.show_tv);
		show_iv = (ImageView) findViewById(R.id.show_iv);
		load_bt = (Button) findViewById(R.id.load_bt);
		launch_bt = (Button) findViewById(R.id.launch_bt);

		load_bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setContent();
			}
		});

		launch_bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setContent1();
			}
		});
	}

	private void setContent() {
		try {
			Class clazz = classLoader.loadClass("com.andev.plugin.UIUtil");
			Method method = clazz.getMethod("getTextString", Context.class);
			String str = (String) method.invoke(null, this);

			Log.d(TAG, "str:" + str);

			show_tv.setText(str);

			method = clazz.getMethod("getImageDrawable", Context.class);
			Drawable drawable = (Drawable) method.invoke(null, this);
			show_iv.setImageDrawable(drawable);
		} catch (Exception e) {
			Log.i("Loader", "error:" + Log.getStackTraceString(e));

		}
	}

	private void setContent1() {
		show_tv.setText(getTextStringId());
		show_iv.setImageResource(getImgDrawableId());
	}

	private int getTextStringId() {
		try {
			Class clazz = classLoader.loadClass("com.andev.plugin.R$string");
			Field field = clazz.getField("plugin_string");
			int resId = (int) field.get(null);
			return resId;
		} catch (Exception e) {
			Log.i("Loader", "error:" + Log.getStackTraceString(e));
		}
		return 0;
	}

	private int getImgDrawableId() {
		try {
			Class clazz = classLoader.loadClass("com.andev.plugin.R$drawable");
			Field field = clazz.getField("order_card");
			int resId = (int) field.get(null);
			return resId;
		} catch (Exception e) {
			Log.i("Loader", "error:" + Log.getStackTraceString(e));
		}
		return 0;
	}

	protected void loadResources(String dexPath) {
		try {
			AssetManager assetManager = AssetManager.class.newInstance();
			Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
			addAssetPath.invoke(assetManager, dexPath);
			mAssetManager = assetManager;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Resources superRes = super.getResources();
		superRes.getDisplayMetrics();
		superRes.getConfiguration();
		mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
	}

	@Override
	public AssetManager getAssets() {
		return mAssetManager == null ? super.getAssets() : mAssetManager;
	}

	@Override
	public Resources getResources() {
		return mResources == null ? super.getResources() : mResources;
	}
}
