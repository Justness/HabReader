/*
Copyright 2012-2013 Andrey Zaytsev, Sergey Ivanov

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

import net.meiolania.apps.habrahabr.AbstractionFragmentActivity;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.posts.PostShowFragment;
import net.meiolania.apps.habrahabr.fragments.posts.PostsCommentsFragment;
import net.meiolania.apps.habrahabr.ui.TabListener;
import net.meiolania.apps.habrahabr.ui.popup.TransparentPanel;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;

public class PostsShowActivity extends AbstractionFragmentActivity {
    public final static String EXTRA_URL = "url";
    public final static String EXTRA_TITLE = "title";
    private String url;
    private String title;
    private int currentTab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	if (savedInstanceState != null)
	    currentTab = savedInstanceState.getInt("currentTab");

	loadExtras();
	showActionBar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);

	outState.putInt("currentTab", getSupportActionBar().getSelectedTab().getPosition());
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
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	/*
	 * Post tab
	 */
	Bundle arguments = new Bundle();
	arguments.putString(PostShowFragment.URL_ARGUMENT, url);

	Tab tab = actionBar.newTab().setText(R.string.post).setTag("post")
		.setTabListener(new TabListener<PostShowFragment>(this, "post", PostShowFragment.class, arguments));
	actionBar.addTab(tab, (currentTab == 0) ? true : false);

	/*
	 * Comments tab
	 */
	arguments = new Bundle();
	arguments.putString(PostsCommentsFragment.URL_ARGUMENT, url);

	tab = actionBar.newTab().setText(R.string.comments).setTag("comments")
		.setTabListener(new TabListener<PostsCommentsFragment>(this, "comments", PostsCommentsFragment.class, arguments));
	actionBar.addTab(tab, (currentTab == 1) ? true : false);
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

    public boolean onKeyUp(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_MENU) {
	    View popup = (View) findViewById(R.id.view1);
	    Animation animShow = AnimationUtils.loadAnimation(this, R.anim.popup_show);
	    Animation animHide = AnimationUtils.loadAnimation(this, R.anim.popup_hide);
	    if (popup.getVisibility() != View.VISIBLE) {
		popup.setVisibility(View.VISIBLE);
		popup.startAnimation(animShow);
	    } else {
		popup.startAnimation(animHide);
		popup.setVisibility(View.GONE);
	    }
	    return true;
	} else {
	    return super.onKeyUp(keyCode, event);
	}
    }
}