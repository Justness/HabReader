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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.users.UsersFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;

public class UsersSearchActivity extends AbstractionActivity {
    public final static String URL = "http://habrahabr.ru/search/?target_type=users&order_by=relevance&q=%query%";
    public final static String EXTRA_QUERY = "query";
    private String query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	loadExtras();
	showActionBar();
	loadSearchedPeople();
    }

    private void loadExtras() {
	query = getIntent().getStringExtra(EXTRA_QUERY);
    }

    private void showActionBar() {
	ActionBar actionBar = getSupportActionBar();
	actionBar.setTitle(R.string.people_search);
	actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void loadSearchedPeople() {
	UsersFragment fragment = new UsersFragment();

	Bundle arguments = new Bundle();
	arguments.putString(UsersFragment.URL_ARGUMENT, getUrl());

	fragment.setArguments(arguments);

	FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
	fragmentTransaction.replace(android.R.id.content, fragment);
	fragmentTransaction.commit();
    }

    private String getUrl() {
	try {
	    return URL.replace("%query%", URLEncoder.encode(query, "UTF-8"));
	} catch (UnsupportedEncodingException e) {
	    return URL.replace("%query%", query);
	}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case android.R.id.home:
	    Intent intent = new Intent(this, UsersActivity.class);
	    if (NavUtils.shouldUpRecreateTask(this, intent)) {
		TaskStackBuilder.create(this).addNextIntent(intent).startActivities();
		finish();
	    } else
		NavUtils.navigateUpTo(this, intent);
	    return true;
	}
	return super.onOptionsItemSelected(item);
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