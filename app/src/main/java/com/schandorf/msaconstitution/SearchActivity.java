package com.schandorf.msaconstitution;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    List<ConstitutionPOJO> constitutionPOJOList;
    EditText searchEditText;
    SearchAdapter searchAdapter;
    RecyclerView searchRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchEditText = findViewById(R.id.searchText);
        searchRecycler = findViewById(R.id.search_recyclerView);
        final FontChangeCrawler fontChangeCrawler = new FontChangeCrawler(getAssets(),"Candara.ttf");
        fontChangeCrawler.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
       // this.setTitle("Search");
        Intent intent = getIntent();
        constitutionPOJOList = (List<ConstitutionPOJO>) intent.getSerializableExtra("list");
        //Toast.makeText(this, constitutionPOJOList.toString(), Toast.LENGTH_SHORT).show();

        searchAdapter = new SearchAdapter(constitutionPOJOList,SearchActivity.this,fontChangeCrawler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        searchRecycler.setLayoutManager(linearLayoutManager);
        searchRecycler.setAdapter(searchAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              List<ConstitutionPOJO> newList =   searchResult(s.toString(),constitutionPOJOList);
                SearchAdapter newAdapter = new SearchAdapter(newList,SearchActivity.this,fontChangeCrawler);
                searchRecycler.setAdapter(newAdapter);
                newAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private List<ConstitutionPOJO> searchResult(String searchTerm, List<ConstitutionPOJO> sourceList)
    {
        List<ConstitutionPOJO> results = new ArrayList<>();
        for (ConstitutionPOJO cpp : sourceList)
        {
            if(cpp.getTitle().contains(searchTerm) || cpp.getContent().contains(searchTerm))
            {
                results.add(cpp);
            }
        }
        return results;
    }
}
