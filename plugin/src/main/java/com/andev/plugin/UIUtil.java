package com.andev.plugin;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class UIUtil {

	public static String getTextString(Context ctx) {
		return ctx.getResources().getString(R.string.plugin_string);
	}

	public static Drawable getImageDrawable(Context ctx) {
		return ctx.getResources().getDrawable(R.drawable.order_card);
	}

	public static int getTextStringId() {
		return R.string.plugin_string;
	}

	public static int getImageDrawableId() {
		return R.drawable.order_card;
	}
}
