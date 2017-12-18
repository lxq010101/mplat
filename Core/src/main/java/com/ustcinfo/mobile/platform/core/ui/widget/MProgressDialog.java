/******************************************************************* 
 * Copyright (c) 2015 by USTC SINOVATE SOFTWARE ,Inc. 
 * All rights reserved. 
 * 
 * This file is proprietary and confidential to USTC SINOVATE SOFTWARE. 
 * No part of this file may be reproduced, stored, transmitted, 
 * disclosed or used in any form or by any means other than as 
 * expressly provided by the written permission from the project
 * team of mobile application platform
 * 
 * 
 * Create by SunChao on 2015/04/29
 * Ver1.0
 * 
 * ****************************************************************/
package com.ustcinfo.mobile.platform.core.ui.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.ustcinfo.mobile.platform.core.R;

public class MProgressDialog extends ProgressDialog{
	
	private String message;
	
	private TextView messageView;
	
	public MProgressDialog(Context context) {
		this(context ,context.getResources().getString(R.string.loading));
	}
	
	public MProgressDialog(Context context, String message){
		super(context);
		this.message = message;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_dialog);
		messageView = (TextView) findViewById(R.id.progress_msg);
		messageView.setText(message);
	}
	
	@Override
	public void setMessage(CharSequence message) {
		messageView.setText(message);
	}

}
