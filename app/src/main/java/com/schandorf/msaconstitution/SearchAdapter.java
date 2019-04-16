package com.schandorf.msaconstitution;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Schandorf on 8/22/2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<ConstitutionPOJO> constitutionPOJOList;
   private Context context;
   private FontChangeCrawler fontChangeCrawler;

    public SearchAdapter(List<ConstitutionPOJO> constitutionPOJOList, Context context, FontChangeCrawler fontChangeCrawler) {
        this.constitutionPOJOList = constitutionPOJOList;
        this.context = context;
        this.fontChangeCrawler = fontChangeCrawler;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,parent,false);
        fontChangeCrawler.replaceFonts((ViewGroup) v);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder holder, int position) {
        ConstitutionPOJO cp = constitutionPOJOList.get(position);
        String titleText = cp.getArticle()+" - "+cp.getTitle();
        final int id = cp.getId()-1;
        holder.search_text.setText(titleText);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MainActivity.class);
                intent.putExtra("id",id);
                context.startActivity(intent);
                ((SearchActivity) context).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return constitutionPOJOList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView search_text;
        LinearLayout linearLayout;
        ViewHolder(View itemView) {
            super(itemView);
            search_text = itemView.findViewById(R.id.text_search);
            linearLayout = itemView.findViewById(R.id.search_linear_layout);
        }
    }
}
