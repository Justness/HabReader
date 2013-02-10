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

package net.meiolania.apps.habrahabr.fragments.posts.loader;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.PostsFullData;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class PostShowLoader extends AsyncTaskLoader<PostsFullData> {
    private String url;
    private Context context;

    public PostShowLoader(Context context, String url) {
	super(context);
	this.context = context;
	this.url = url;
    }

    @Override
    public PostsFullData loadInBackground() {
	PostsFullData data = new PostsFullData();

	try {
	    Document document = Jsoup.connect(url).get();

	    Element head = document.select("head").first();
	    Element head2 = document.head();
	    Element title = document.select("span.post_title").first();
	    Element hubs = document.select("div.hubs").first();
	    Element content = document.select("div.content").first();
	    Element date = document.select("div.published").first();
	    Element author = document.select("div.author > a").first();
	    Element infopanel = document.select("div.infopanel").first();

	    if (title != null) {
		data.setUrl(url);
		data.setDataHead(head2.html());
		data.setTitle(title.text());
		data.setHubs(hubs.text());
		data.setContent(content.html());
		data.setDate(date.text());
		data.setAuthor(author.text());
		data.setInfoPanel(infopanel.html());
	    } else
		data.setContent(context.getString(R.string.error_404));
	} catch (IOException e) {
	}

	return data;
    }

}