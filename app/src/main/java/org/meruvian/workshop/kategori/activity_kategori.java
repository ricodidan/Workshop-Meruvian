package org.meruvian.workshop.kategori;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.meruvian.workshop.R;
import org.meruvian.workshop.adapter.CatAdapter;
import org.meruvian.workshop.content.database.adapter.CatDatabaseAdapter;
import org.meruvian.workshop.entity.News;
import org.meruvian.workshop.rest.CatRestVariables;
import org.meruvian.workshop.service.TaskService;
import org.meruvian.workshop.task.CatDeleteTask;
import org.meruvian.workshop.task.CatGetTask;
import org.meruvian.workshop.task.CatPostTask;
import org.meruvian.workshop.task.CatPutTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Enrico_Didan on 27/12/2016.
 */

public class activity_kategori extends ActionBarActivity implements TaskService {
    CatDatabaseAdapter newsDatabaseAdapter;
    EditText subkategori, kategori;
    CatPostTask newsPostTask1;
    CatPutTask newsPutTask1;
    private ListView listNews1;
    private CatAdapter newsAdapter1;
    private News news1;
    private ProgressDialog progressDialog1;
    private CatGetTask newsGetTask1;
    private CatDeleteTask newsDeleteTask1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);

        listNews1 = (ListView) findViewById(R.id.list_news_kategori);
        subkategori = (EditText) findViewById(R.id.edit_subkategori);
        kategori = (EditText) findViewById(R.id.edit_kategori);

        newsDatabaseAdapter = new CatDatabaseAdapter(this);
        newsAdapter1 = new CatAdapter(this, new ArrayList<News>());
        listNews1.setAdapter(newsAdapter1);

        listNews1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialogList(i);

                return true;
            }
        });

        newsGetTask1 = new CatGetTask(this, this);
        newsGetTask1.execute("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                newsGetTask1 = new CatGetTask(activity_kategori.this, activity_kategori.this);
                newsGetTask1.execute(s);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            if (news1 == null) {
                news1 = new News();
            }
            news1.setStatus(1);
            news1.setContent(subkategori.getText().toString());
            news1.setTitle(kategori.getText().toString());
            news1.setCreateDate(new Date().getTime());
            if (news1.getId() == -1) {
                newsPostTask1 = new CatPostTask(this, this);
                newsPostTask1.execute(news1);
            } else {
                newsPutTask1 = new CatPutTask(this, this);
                newsPutTask1.execute(news1);
            }
            news1 = new News();
        } else if (item.getItemId() == R.id.action_refresh) {
            newsGetTask1 = new CatGetTask(this, this);
            newsGetTask1.execute("");
        }
        return true;
    }

    private void dialogList(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.action));
        builder.setItems(new String[]{getString(R.string.edit), getString(R.string.delete)}, new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int location) {
                        news1 = (News) newsAdapter1.getItem(position);
                        if (news1 != null) {
                            if (location == 0) {
                                kategori.setText(news1.getTitle());
                                subkategori.setText(news1.getContent());
                                kategori.requestFocus();
                            } else if (location == 1) {
                                confirmDelete();
                            }
                        }
                    }
                });
        builder.create().show();

    }

    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete));
        builder.setMessage(getString(R.string.confirm_delete) + " '" + news1.getTitle() + "' ?");
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                newsDeleteTask1 = new CatDeleteTask(activity_kategori.this, activity_kategori.this);
                newsDeleteTask1.execute(news1.getId() + "");
                news1 = new News();
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }


    @Override
    public void onExecute(int code) {
        if (progressDialog1 != null) {
            if (!progressDialog1.isShowing()) {
                progressDialog1.setMessage(getString(R.string.loading));
                progressDialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if (newsGetTask1 != null) {
                            newsGetTask1.cancel(true);
                        }
                    }
                });
                progressDialog1.show();
            }
        } else {
            progressDialog1 = new ProgressDialog(this);
        }

    }

    @Override
    public void onSuccess(int code, Object result) {
        if (result != null) {
            if (code == CatRestVariables.NEWS_GET_TASK1) {
                List<News> newses = (List<News>) result;
                newsAdapter1.clear();
                newsAdapter1.addNews(newses);
            } else if (code == CatRestVariables.NEWS_POST_TASK1) {
                kategori.setText("");
                subkategori.setText("");
                news1 = new News();
                News news = (News) result;
                newsAdapter1.addNews(news);
            } else if (code == CatRestVariables.NEWS_PUT_TASK1) {
                kategori.setText("");
                subkategori.setText("");
                news1 = new News();
                newsGetTask1 = new CatGetTask(this, this);
                newsGetTask1.execute("");
            } else if (code == CatRestVariables.NEWS_DELETE_TASK1) {
                newsGetTask1 = new CatGetTask(this, this);
                newsGetTask1.execute("");
            }
        }
        progressDialog1.dismiss();
    }

    @Override
    public void onCancel(int code, String message) {
        progressDialog1.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(int code, String message) {
        progressDialog1.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
