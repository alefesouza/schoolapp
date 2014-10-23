package aloogle.rebuapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import aloogle.rebuapp.R;
import android.app.Activity;
import aloogle.rebuapp.other.Other;

public class Clube extends Fragment {
	private Activity activity;
	private View view;

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
		this.activity = getActivity();
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.clube_frag, container, false);
		Other.setText(this.getActivity(), view, 1);
		Other.setClick(this.getActivity(), view, 1);
		return view;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		Other.setText(this.getActivity(), view, 1);
		Other.setClick(this.getActivity(), view, 1);
	}
}