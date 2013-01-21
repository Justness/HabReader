/*
Copyright 2012 Andrey Zaytsev

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.AbstractionSlidingActivity;
import net.meiolania.apps.habrahabr.fragments.events.EventsShowFragment;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;

public class EventsShowActivity extends AbstractionSlidingActivity {
    public final static String EXTRA_URL = "url";
    public final static String EXTRA_TITLE = "title";
    private String url;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	loadExtras();
	showActionBar();
	loadInfo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case android.R.id.home:
	    Intent intent = new Intent(this, EventsActivity.class);
	    if (NavUtils.shouldUpRecreateTask(this, intent)) {
		TaskStackBuilder.create(this).addNextIntent(intent).startActivities();
		finish();
	    } else
		NavUtils.navigateUpTo(this, intent);
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    private void loadExtras() {
	Uri habraUrl = getIntent().getData();
	if (habraUrl != null)
	    url = habraUrl.toString();
	else
	    url = getIntent().getStringExtra(EXTRA_URL);
	title = getIntent().getStringExtra(EXTRA_TITLE);
    }

    private void showActionBar() {
	ActionBar actionBar = getSupportActionBar();
	actionBar.setDisplayHomeAsUpEnabled(true);
	actionBar.setTitle(title);
    }

    private void loadInfo() {
	EventsShowFragment fragment = new EventsShowFragment();

	Bundle arguments = new Bundle();
	arguments.putString(EventsShowFragment.URL_ARGUMENT, url);

	fragment.setArguments(arguments);

	FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
	fragmentTransaction.replace(android.R.id.content, fragment);
	fragmentTransaction.commit();
    }

    @Override
    protected OnClickListener getConnectionDialogListener() {
	return new OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		finish();
	    }
	};
    }

}