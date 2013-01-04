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
import net.meiolania.apps.habrahabr.slidemenu.MenuFragment;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public abstract class AbstractionActivity extends SlidingFragmentActivity {
	Preferences preferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		preferences = Preferences.getInstance(this);
		if (preferences.getFullScreen())
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getScreenPref();

		if (!ConnectionUtils.isConnected(this)) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle(R.string.error);
			dialog.setMessage(getString(R.string.no_connection));
			dialog.setPositiveButton(R.string.close,
					getConnectionDialogListener());
			dialog.setCancelable(false);
			dialog.show();
		}

		/*
		 * Workaround for
		 * "java.lang.IllegalStateException: Both setBehindContentView must be called in onCreate in addition to setContentView."
		 */
		setContentView(R.layout.empty_for_slidemenu);

		showSlideMenu();

		getSupportActionBar().setHomeButtonEnabled(true);
		setSupportProgressBarIndeterminateVisibility(false);
	}

	@Override
	protected void onResume() {
		if (preferences.getFullScreen())
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		else
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getScreenPref();
		super.onResume();
	}

	private void getScreenPref() {
		if (preferences.getKeepScreen()) {
			getWindow()
					.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		} else {
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		if (getSlidingMenu().isMenuShowing())
			toggle();
		else
			super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.global_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		case R.id.preferences:
			startActivity(new Intent(this, PreferencesActivity.class));
			return true;
		case R.id.more_applications:
			Uri uri = Uri
					.parse("https://play.google.com/store/apps/developer?id=Andrey+Zaytsev");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void showSlideMenu() {
		setBehindContentView(R.layout.slide_menu);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.slide_menu, new MenuFragment()).commit();

		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		slidingMenu.setShadowDrawable(R.drawable.sm_shadow);
		slidingMenu.setShadowWidth(50);
		slidingMenu.setFadeDegree(0.2f);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setMenu(R.layout.slide_menu);
	}

	protected abstract OnClickListener getConnectionDialogListener();

}