package org.meruvian.workshop.fragment;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.meruvian.workshop.R;
import org.meruvian.workshop.adapter.NewsAdapter;
import org.meruvian.workshop.entity.News;

/**
 * Created by Enrico_Didan on 23/12/2016.
 */

public class NewsFragment extends Fragment{
    private ListView listNews;

    private NewsAdapter newsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listNews = (ListView) view.findViewById(R.id.list_news);

        newsAdapter = new NewsAdapter(getActivity(), News.data());
        listNews.setAdapter(newsAdapter);

        listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News news = (News) adapterView.getAdapter().getItem(i);

                Bundle bundle = new Bundle();
                bundle.putString("title", news.getTitle());
                bundle.putString("content", news.getContent());
                bundle.putLong("date", news.getCreateDate());

                DetailNewsFragment detailNewsFragment = new DetailNewsFragment();
                detailNewsFragment.setArguments(bundle);

                if (getArguments() != null && getArguments().getString("screen").equals("large")){
                    getFragmentManager().beginTransaction().replace(R.id.container_inner, detailNewsFragment).commit();
                }
                else {
                    getFragmentManager().beginTransaction().replace(R.id.container, detailNewsFragment).addToBackStack(null).commit();
                }
            }
        });

        listNews.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialogActions(i);

                return true;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actions, menu);
        SearchManager searchManager = (SearchManager)
                getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(getActivity(), "Searching : " + s, Toast.LENGTH_LONG).show();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            Toast.makeText(getActivity(), getString(R.string.save), Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.action_refresh) {
            Toast.makeText(getActivity(), getString(R.string.refresh), Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.action_search) {
            Toast.makeText(getActivity(), getString(R.string.search), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void dialogActions(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.action));
        builder.setItems(new String[]{getString(R.string.edit), getString(R.string.delete)}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int location) {
                News news = (News) newsAdapter.getItem(position);
                if (location==0){
                    Toast.makeText(getActivity(), "Edit News : " + news.getTitle(), Toast.LENGTH_LONG).show();
                } else if (location==1){
                    confirmDelete(position);
                }
            }
        });
        builder.create().show();

    }

    private void confirmDelete(int position) {
        final News news = (News) newsAdapter.getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.delete));
        builder.setMessage(getString(R.string.confirm_delete)+" '"+news.getTitle()+" ?");
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "Delete News : " + news.getTitle(), Toast.LENGTH_LONG).show();
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
}
