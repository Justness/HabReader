package net.meiolania.apps.habrahabr.fragments.posts;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.utils.HabrWebClient;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.TextView;

public class CommentDialogFragment extends SherlockDialogFragment {
    private static final String STYLESHEET = "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/style.css\" />";

    String author, comment, score, time;

    public CommentDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	author = getArguments().getString(PostsCommentsFragment.EXTRA_COMMENT_AUTHOR);
	comment = getArguments().getString(PostsCommentsFragment.EXTRA_COMMENT_BODY);
	score = getArguments().getString(PostsCommentsFragment.EXTRA_COMMENT_SCORE);
	time = getArguments().getString(PostsCommentsFragment.EXTRA_COMMENT_TIME);

	setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	View v = inflater.inflate(R.layout.fragment_comment, container);
	Preferences prefs = Preferences.getInstance(getSherlockActivity());
	
	SherlockFragmentActivity activity = getSherlockActivity();

	TextView cAuthor = (TextView) v.findViewById(R.id.comment_author);
	TextView cScore = (TextView) v.findViewById(R.id.comment_score);
	TextView cTime = (TextView) v.findViewById(R.id.comment_time);

	cAuthor.setText(author);
	cScore.setText(score);
	cTime.setText(time);
	
	Integer rating = UIUtils.parseRating(score);
	if (rating > 0)
	    cScore.setTextColor(activity.getResources().getColor(R.color.rating_positive));
	else if (rating < 0)
	    cScore.setTextColor(activity.getResources().getColor(R.color.rating_negative));
	else
	    cScore.setTextColor(activity.getResources().getColor(R.color.black));

	WebView cBody = (WebView) v.findViewById(R.id.comment_view);

	cBody.setWebViewClient(new HabrWebClient(getSherlockActivity()));
	//cBody.setBackgroundColor(0x00000000);
	cBody.getSettings().setSupportZoom(false);
	cBody.getSettings().setBuiltInZoomControls(false);
	cBody.getSettings().setDefaultZoom(ZoomDensity.FAR);
	cBody.setInitialScale(prefs.getViewScale());
	cBody.loadDataWithBaseURL(null, STYLESHEET + comment, "text/html", "UTF-8", null);
	return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
    }
}