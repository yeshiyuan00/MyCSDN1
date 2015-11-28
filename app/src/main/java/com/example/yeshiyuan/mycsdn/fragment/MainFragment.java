package com.example.yeshiyuan.mycsdn.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yeshiyuan.mycsdn.R;
import com.example.yeshiyuan.mycsdn.adapter.NewsItemAdapter;
import com.example.yeshiyuan.mycsdn.dao.NewsItemDao;
import com.example.yeshiyuan.mycsdn.util.AppUtil;
import com.example.yeshiyuan.mycsdn.util.Logger;
import com.example.yeshiyuan.mycsdn.util.NetUtil;
import com.ysy.splider.CommonException;
import com.ysy.splider.Constaint;
import com.ysy.splider.NewsItem;
import com.ysy.splider.NewsItemBiz;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;

/**
 * Created by yeshiyuan on 11/18/15.
 */
public class MainFragment extends Fragment implements IXListViewRefreshListener, IXListViewLoadMore {

    private static final int LOAD_MORE = 0x110;
    private static final int LOAD_REFREASH = 0x111;

    private static final int TIP_ERROR_NO_NETWORK = 0X112;
    private static final int TIP_ERROR_SERVER = 0X113;

    /**
     * 是否是第一次进入
     */
    private boolean isFirstIn = true;

    /**
     * 是否连接网络
     */
    private boolean isConnNet = false;

    /**
     * 当前数据是否是从网络中获取的
     */
    private boolean isLoadingDataFromNetWork;


    /***
     * 默认的NewsType
     */
    private int newsType = Constaint.NEWS_TYPE_YEJIE;

    /**
     * 当前页面
     */
    private int currentPage = 1;

    /**
     * 处理新闻的业务类
     */
    private NewsItemBiz mNewsItemBiz;

    /**
     * 与数据库交互
     */
    private NewsItemDao mNewsItemDao;

    /**
     * 扩展的Listview
     */
    private XListView mXListView;

    /**
     * 数据适配器
     */
    private NewsItemAdapter mAdapter;

    /**
     * 数据
     */
    private List<NewsItem> mDatas = new ArrayList<NewsItem>();

    /**
     * 获得newType
     *
     * @param newsType
     */
    public MainFragment(int newsType) {
        this.newsType = newsType;
        Logger.e(newsType + "newsType");
        mNewsItemBiz = new NewsItemBiz();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mNewsItemDao=new NewsItemDao(getActivity());
        mAdapter = new NewsItemAdapter(getActivity(), mDatas);
        mXListView = (XListView) getView().findViewById(R.id.id_xlistView);
        mXListView.setAdapter(mAdapter);
        mXListView.setPullRefreshEnable(this);
        mXListView.setPullLoadEnable(this);
        mXListView.setRefreshTime(AppUtil.getRefreashTime(getActivity(), newsType));
        //mXListView.NotRefreshAtBegin();
        if(isFirstIn){
            /**
             * 进来时直接刷新
             */
            mXListView.startRefresh();
            isFirstIn=false;
        }else {
            mXListView.NotRefreshAtBegin();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_item_fragment_main, null);

    }

    @Override
    public void onRefresh() {
        new LoadDataTask().execute(LOAD_REFREASH);
    }

    @Override
    public void onLoadMore() {
        new LoadDataTask().execute(LOAD_MORE);
    }


    private class LoadDataTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            switch (params[0]) {
                case LOAD_MORE:
                    LoadMoreData();
                    break;
                case LOAD_REFREASH:
                    return refreashData();


            }
            return -1;

        }

        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case TIP_ERROR_NO_NETWORK:
                    Toast.makeText(getActivity(), "没有网络连接", Toast.LENGTH_SHORT).show();
                    mAdapter.addAll(mDatas);
                    mAdapter.notifyDataSetChanged();
                    break;
                case TIP_ERROR_SERVER:
                    Toast.makeText(getActivity(), "服务器错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            mXListView.setRefreshTime(AppUtil.getRefreashTime(getActivity(), newsType));
            mXListView.stopRefresh();
            mXListView.stopLoadMore();
        }
    }


    /**
     * 下拉刷新数据
     */
    private Integer refreashData() {
        if (NetUtil.checkNet(getActivity())) {
            isConnNet = true;
            // 获取最新数据
            try {
                List<NewsItem> newsItems=mNewsItemBiz.getNewsItems(newsType,currentPage);
                mAdapter.addAll(newsItems);
                isLoadingDataFromNetWork = true;
                //设置刷新时间
                AppUtil.setRefreashTime(getActivity(),newsType);
                //清空数据库
                mNewsItemDao.deleteAll(newsType);
                //存进数据库
                mNewsItemDao.add(newsItems);

            } catch (CommonException e) {
                e.printStackTrace();
                e.printStackTrace();
                isLoadingDataFromNetWork = false;
                return TIP_ERROR_SERVER;
            }
        }else {
            isConnNet=false;
            isLoadingDataFromNetWork=false;
            // TODO从数据库中加载
            List<NewsItem> newsItems=mNewsItemDao.list(newsType,currentPage);
            mDatas=newsItems;
            //mAdapter.setDatas(newsItems);
            return TIP_ERROR_NO_NETWORK;
        }
        return -1;
    }

    /**
     * 会根据当前网络情况，判断是从数据库加载还是从网络继续获取
     */
    private void LoadMoreData() {
        // 当前数据是从网络获取的
        if (isLoadingDataFromNetWork) {
            currentPage += 1;

            try {
                List<NewsItem> newsItems = mNewsItemBiz.getNewsItems(newsType, currentPage);
                mNewsItemDao.add(newsItems);
                mAdapter.addAll(newsItems);
            } catch (CommonException e) {
                e.printStackTrace();
            }
        } else {
            //从数据库中加载
            currentPage += 1;
            List<NewsItem> newsItems = mNewsItemDao.list(newsType, currentPage);
            mAdapter.addAll(newsItems);
        }
    }


}
