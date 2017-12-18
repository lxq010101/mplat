package com.ustcinfo.mobile.platform.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ustcinfo.mobile.platform.ability.presenter.common.BasePresenter;
import com.ustcinfo.mobile.platform.ui.activity.BaseActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseFragment extends com.ustcinfo.mobile.platform.ability.fragment.common.BaseFragment {
	
	public String TAG ;
	
	public BaseActivity mActivity ;

	protected Unbinder bind;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view= super.onCreateView(inflater, container, savedInstanceState);
		initView(view, savedInstanceState);
		bind = ButterKnife.bind(this, view);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = (BaseActivity)getActivity();
		TAG = "Fragment-->"+this.getClass().getSimpleName() ;

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		bind.unbind();
	}

	@Override
	protected int getLayoutId() {
		return 0;
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {

	}

	@Override
	protected void initRequest() {

	}

	@Override
	protected BasePresenter createPresenter() {
		return null;
	}
}
