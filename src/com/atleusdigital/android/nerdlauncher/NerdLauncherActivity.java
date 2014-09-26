package com.atleusdigital.android.nerdlauncher;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.Menu;

public class NerdLauncherActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment(int num) {
		Fragment fragment = null;
		if (num == 1) {
			fragment = new NerdLauncherFragment();
		} else {
			fragment = new NerdLauncherActivityFragment();
		}
		return fragment;
	}

}
