package com.atleusdigital.android.nerdlauncher;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.Menu;

public class NerdLauncherActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		Fragment fragment = new NerdLauncherFragment();
		return fragment;
	}

}
