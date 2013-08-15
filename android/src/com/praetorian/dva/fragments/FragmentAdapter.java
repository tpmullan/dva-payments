package com.praetorian.dva.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface FragmentAdapter 
{
	public View getView();
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState);
}
