package com.atleusdigital.android.nerdlauncher;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NerdLauncherActivityFragment extends ListFragment {

	private static final String TAG = "NerdLauncherFragment";
	private final int MAX_TASKS = 100;
	
	ArrayAdapter<RunningTaskInfo> adapter;
	List<RunningTaskInfo> activities;
	ActivityManager am;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent startupIntent = new Intent(Intent.ACTION_MAIN);
		startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		
		am = (ActivityManager) getActivity().getSystemService(Activity.ACTIVITY_SERVICE);
		activities = am.getRunningTasks(MAX_TASKS);
		
		
		Log.i(TAG, "I've found " + activities.size() + " activities.");
		for (int i = 0; i < activities.size(); i++) {
			Log.i(TAG, "[Activity]: " + activities.get(i).topActivity);
		}
		
		adapter = new ArrayAdapter<RunningTaskInfo>(getActivity(), 
				0, 
				activities) {
			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {
				// check if view is non-existent
				// we do this because ArrayAdapter takes in a TextView, but we don't have one to supply
				if (convertView == null) {
					convertView = getActivity().getLayoutInflater()
							.inflate(R.layout.view_list_item, null);
				}

				
				ImageView imageView = (ImageView)convertView.findViewById(R.id.label_icon);
				TextView textView = (TextView)convertView.findViewById(R.id.label_text);
				RunningTaskInfo ri = getItem(pos);
				textView.setText(ri.topActivity.toString());
				/*BitmapDrawable drawable = new BitmapDrawable(getResources(), ri.thumbnail);
				imageView.setImageDrawable(drawable);*/
				return convertView;
			}
		};
		
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
 	}

	@SuppressLint("NewApi")
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		ActivityManager am = (ActivityManager) getActivity().getSystemService(Activity.ACTIVITY_SERVICE);
		RunningTaskInfo ri = (RunningTaskInfo)l.getAdapter().getItem(position);
		
		if (ri == null) return;
		
		// moves task to front making it visible to the user
		am.moveTaskToFront(ri.id, ActivityManager.MOVE_TASK_WITH_HOME, null);
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, "onPause!");
		activities.removeAll(activities);
		List<RunningTaskInfo> temp = am.getRunningTasks(MAX_TASKS);
		activities.addAll(temp);
		adapter.notifyDataSetChanged();
	}
	
	
}
