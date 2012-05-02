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

package net.meiolania.apps.habrahabr.fragments;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.CommentsAdapter;
import net.meiolania.apps.habrahabr.data.CommentsData;
import net.meiolania.apps.habrahabr.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;

public class QaCommentsFragment extends SherlockListFragment{
    public final static String LOG_TAG = "QaCommentsFragment";
    protected final ArrayList<CommentsData> commentsDatas = new ArrayList<CommentsData>();
    protected CommentsAdapter commentsAdapter;
    protected String url;

    public QaCommentsFragment(){
    }

    public QaCommentsFragment(String url){
        this.url = url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        commentsAdapter = new CommentsAdapter(getSherlockActivity(), commentsDatas);
        setListAdapter(commentsAdapter);
        loadList();
    }

    protected void loadList(){
        new LoadComments().execute();
        if(!UIUtils.isHoneycombOrHigher())
            Toast.makeText(getSherlockActivity(), R.string.loading_comments, Toast.LENGTH_SHORT).show();
    }
    
    protected final class LoadComments extends AsyncTask<Void, Void, Void>{
        
        @Override
        protected Void doInBackground(Void... params){
            try{
                Document document = Jsoup.connect(url).get();
                Elements answers = document.select("div.answer");
                for(Element answer : answers){
                    CommentsData commentsData = new CommentsData();
                    
                    Element name = answer.select("a.username").first();
                    Element message = answer.select("div.message").first();
                    
                    commentsData.setAuthor(name.text());
                    commentsData.setComment(message.text());
                    commentsData.setLevel(0);
                    
                    commentsDatas.add(commentsData);
                    
                    Elements comments = answer.select("div.comment_item");
                    for(Element comment : comments){
                        commentsData = new CommentsData();
                        
                        name = comment.select("span.info > a").first();
                        message = comment.select("span.text").first();
                        
                        commentsData.setAuthor(name.text());
                        commentsData.setComment(message.text());
                        commentsData.setLevel(1);
                        
                        commentsDatas.add(commentsData);
                    }
                }
            }
            catch(IOException e){
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            getSherlockActivity().runOnUiThread(new Runnable(){
                public void run(){
                    if(!isCancelled())
                        commentsAdapter.notifyDataSetChanged();
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                }
            });
        }

    }

}