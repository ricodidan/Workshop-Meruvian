package org.meruvian.workshop.task;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.meruvian.workshop.R;
import org.meruvian.workshop.rest.CatRestVariables;
import org.meruvian.workshop.service.ConnectionUtil;
import org.meruvian.workshop.service.TaskService;

import java.io.IOException;

/**
 * Created by Enrico_Didan on 27/12/2016.
 */

public class CatDeleteTask extends AsyncTask<String, Void, Boolean> {
    private TaskService service;
    private Context context;

    public CatDeleteTask(TaskService service, Context context) {
        this.service = service;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(CatRestVariables.NEWS_DELETE_TASK1);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            HttpClient httpClient = new DefaultHttpClient(ConnectionUtil.getHttpParams(15000, 15000));
            HttpDelete httpDelete = new HttpDelete(CatRestVariables.SERVER_URL1 + "/" + params[0]);
            HttpResponse response = httpClient.execute(httpDelete);
            return response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean check) {
        if (check) {
            service.onSuccess(CatRestVariables.NEWS_DELETE_TASK1, true);
        } else {
            service.onError(CatRestVariables.NEWS_DELETE_TASK1, context.getString(R.string.failed_delete_news));
        }
    }
}
