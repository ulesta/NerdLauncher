package com.atleusdigital.android.nerdlauncher;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NerdLauncherFragment extends ListFragment {

	private static final String TAG = "NerdLauncherFragment";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent startupIntent = new Intent(Intent.ACTION_MAIN);
		startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		
		final PackageManager pm = getActivity().getPackageManager();
		
		List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);
		
		Log.i(TAG, "I've found " + activities.size() + " activities.");
		
		Collections.sort(activities, new Comparator<ResolveInfo>() {

			@Override
			public int compare(ResolveInfo a, ResolveInfo b) {
				PackageManager pm = getActivity().getPackageManager();
				
				return String.CASE_INSENSITIVE_ORDER.compare(a.loadLabel(pm).toString(),
						b.loadLabel(pm).toString());
			}
			
		});
		
		ArrayAdapter<ResolveInfo> adapter = new ArrayAdapter<ResolveInfo>(getActivity(), 
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
				ResolveInfo ri = getItem(pos);
				textView.setText(ri.loadLabel(pm));
				imageView.setImageDrawable(ri.loadIcon(pm));
				return convertView;
			}
		};
		
		setListAdapter(adapter);
		
 	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		ResolveInfo resolveInfo = (ResolveInfo)l.getAdapter().getItem(position);
		ActivityInfo activityInfo = resolveInfo.activityInfo;
		
		if (activityInfo == null) return;
		
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}
	
}
