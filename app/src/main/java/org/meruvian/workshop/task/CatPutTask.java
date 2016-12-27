package org.meruvian.workshop.task;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;
import org.meruvian.workshop.R;
import org.meruvian.workshop.entity.News;
import org.meruvian.workshop.rest.CatRestVariables;
import org.meruvian.workshop.service.ConnectionUtil;
import org.meruvian.workshop.service.TaskService;

import java.io.IOException;

/**
 * Created by Enrico_Didan on 27/12/2016.
 */

public class CatPutTask extends AsyncTask<News, Void, JSONObject> {
    private Context context;
    private TaskService service;

    public CatPutTask(Context context, TaskService service) {
        this.context = context;
        this.service = service;
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(CatRestVariables.NEWS_PUT_TASK1);
    }

    @Override
    protected JSONObject doInBackground(News... params) {
        JSONObject json = new JSONObject();
        try {
            json.put("id", params[0].getId());
            json.put("name", params[0].getTitle());
            json.put("subCategory", params[0].getContent());
            HttpClient httpClient = new DefaultHttpClient(ConnectionUtil.getHttpParams(15000, 15000));
            HttpPut httpPut = new HttpPut(CatRestVariables.SERVER_URL1 + "/" + params[0].getId());
            httpPut.addHeader(new BasicHeader("Content-Type", "application/json"));
            httpPut.setEntity(new StringEntity(json.toString()));
            HttpResponse response = httpClient.execute(httpPut);
            json = new JSONObject(ConnectionUtil.convertEntityToString(response.getEntity()));
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
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            if (jsonObject != null) {
                News news = new News();
                news.setId(jsonObject.getInt("id"));
                news.setContent(jsonObject.getString("subCategory"));
                news.setTitle(jsonObject.getString("name"));
                service.onSuccess(CatRestVariables.NEWS_PUT_TASK1, news);
            } else {
                service.onError(CatRestVariables.NEWS_PUT_TASK1,
                        context.getString(R.string.failed_post_news));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            service.onError(CatRestVariables.NEWS_PUT_TASK1, context.getString(R.string.failed_post_news));
        }
    }
}
