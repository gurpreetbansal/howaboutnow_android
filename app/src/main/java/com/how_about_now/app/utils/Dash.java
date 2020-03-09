package com.how_about_now.app.utils;

import androidx.annotation.FloatRange;

public class Dash {

	public float length;

	public Dash(@FloatRange(from = 0) float length) {
		super();
		this.length = Math.max(length, 0);
	}
}