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

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.posts.PostsBestFragment;
import net.meiolania.apps.habrahabr.fragments.posts.PostsCorporateFragment;
import net.meiolania.apps.habrahabr.fragments.posts.PostsThematicFragment;
import net.meiolania.apps.habrahabr.ui.TabListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;

public class PostsActivity extends AbstractionActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	showActionBar();
    }

    private void showActionBar() {
	ActionBar actionBar = getSupportActionBar();
	actionBar.setDisplayHomeAsUpEnabled(true);
	actionBar.setTitle(R.string.posts);
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	Preferences preferences = Preferences.getInstance(this);
	int selectedTab = preferences.getPostsDefaultTab();

	/* Best tab */
	Tab tab = actionBar.newTab();
	tab.setText(R.string.best);
	tab.setTag("best");
	tab.setTabListener(new TabListener<PostsBestFragment>(this, "best", PostsBestFragment.class));
	actionBar.addTab(tab, (selectedTab == 0 ? true : false));

	/* Thematic tab */
	tab = actionBar.newTab();
	tab.setText(R.string.thematic);
	tab.setTag("thematic");
	tab.setTabListener(new TabListener<PostsThematicFragment>(this, "thematic", PostsThematicFragment.class));
	actionBar.addTab(tab, (selectedTab == 1 ? true : false));

	/* Corporate tab */
	tab = actionBar.newTab();
	tab.setText(R.string.corporate);
	tab.setTag("corporate");
	tab.setTabListener(new TabListener<PostsCorporateFragment>(this, "corporate", PostsCorporateFragment.class));
	actionBar.addTab(tab, (selectedTab == 2 ? true : false));
    }

    @Override
    protected OnClickListener getConnectionDialogListener() {
	return new OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	    }
	};
    }

}