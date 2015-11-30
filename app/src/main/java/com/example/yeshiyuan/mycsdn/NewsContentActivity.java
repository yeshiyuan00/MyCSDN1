package com.example.yeshiyuan.mycsdn;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.yeshiyuan.mycsdn.adapter.NewContentAdapter;
import com.ysy.bean.News;
import com.ysy.splider.CommonException;
import com.ysy.splider.NewsItemBiz;

import java.util.List;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.XListView;

/**
 * Author: yeshiyuan
 * Date: 11/29/15.
 */
public class NewsContentActivity extends Activity implements IXListViewLoadMore {
    private XListView mListView;

    /**
     * 该页面的url
     */
    private String url;
    private NewsItemBiz mNewsItemBiz;
    private List<News> mDatas;

    private ProgressBar mProgressBar;
    private NewContentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);

        mNewsItemBiz=new NewsItemBiz();
        Bundle extras=getIntent().getExtras();
        url=extras.getString("url");
        mAdapter=new NewContentAdapter(this);

        mListView = (XListView) findViewById(R.id.id_listview);
        mProgressBar = (ProgressBar) findViewById(R.id.id_newsContentPro);

        mListView.setAdapter(mAdapter);
        mListView.disablePullRefreash();
        mListView.setPullLoadEnable(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = mDatas.get(position - 1);
                String imageLink = news.getImageLink();
                //Toast.makeText(NewContentActivity.this, imageLink, 1).show();
                Intent intent = new Intent(NewsContentActivity.this, ImageShowActivity.class);
                intent.putExtra("url", imageLink);
                startActivity(intent);
            }
        });

        mProgressBar.setVisibility(View.VISIBLE);
        new LoadDataTask().execute();
    }

    @Override
    public void onLoadMore() {

    }

    public void back(){
        finish();
    }

    class LoadDataTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                mDatas=mNewsItemBiz.getNews(url).getNewses();
            } catch (CommonException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(mDatas == null)
                return ;
            mAdapter.addList(mDatas);
            mAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
        }
    }
    }
