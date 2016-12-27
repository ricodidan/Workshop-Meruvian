package org.meruvian.workshop.task;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.meruvian.workshop.R;
import org.meruvian.workshop.entity.News;
import org.meruvian.workshop.rest.CatRestVariables;
import org.meruvian.workshop.service.ConnectionUtil;
import org.meruvian.workshop.service.TaskService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Enrico_Didan on 27/12/2016.
 */

public class CatGetTask extends AsyncTask<String, Void, JSONArray> {
    public Context context;
    public TaskService service;

    public CatGetTask(Context context, TaskService service) {
        this.service = service;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(CatRestVariables.NEWS_GET_TASK1);
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        JSONArray json = null;
        try {
            HttpClient httpClient = new DefaultHttpClient(ConnectionUtil.getHttpParams(15000, 15000));
            HttpGet httpGet = new HttpGet(CatRestVariables.SERVER_URL1 + "?title=" + params[0]);
            httpGet.setHeader("Content-Type", "application/json");
            HttpResponse response = httpClient.execute(httpGet);
            json = new JSONArray(ConnectionUtil.convertEntityToString(response.getEntity()));
        } catch (IOException e) {
            json = null;
            e.printStackTrace();
        } catch (JSONException e) {
            json = null;
            e.printStackTrace();
        } catch (Exception e) {
            json = null;
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        try {
            if (jsonArray.length() > 0) {
                List<News> newses = new ArrayList<News>();
                for (int index = 0; index < jsonArray.length(); index++) {
                    JSONObject json = jsonArray.getJSONObject(index);
                    News news = new News();
                    news.setId(json.getInt("id"));
                    news.setTitle(json.getString("name"));
                    news.setContent(json.getString("subCategory"));
                    newses.add(news);
                }
                service.onSuccess(CatRestVariables.NEWS_GET_TASK1, newses);
            } else {
                service.onError(CatRestVariables.NEWS_GET_TASK1, context.getString(R.string.empty_news));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            service.onError(CatRestVariables.NEWS_GET_TASK1,
                    context.getString(R.string.failed_recieve_news));
        }
    }
}
