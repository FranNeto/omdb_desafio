package br.com.zup.omdbdesafio.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import br.com.zup.omdbdesafio.R;
import br.com.zup.omdbdesafio.model.domain.Filmes;
import br.com.zup.omdbdesafio.view.fragment.MainFragment;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    private List<Filmes> mList;
    private LayoutInflater mLayoutInflater;
    private Context activity;
    private MainFragment fragment;

    public MainAdapter(Context activity, List<Filmes> mList, MainFragment fragment) {
        this.mList = mList;
        this.activity = activity;
        this.fragment = fragment;
        mLayoutInflater = LayoutInflater.from(activity);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        MyViewHolder mvh = null;
        View v = mLayoutInflater.inflate(R.layout.content_item, viewGroup, false);

        if (v != null) {
            mvh = new MyViewHolder(v);
        }

        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int position) {

        if ((position % 2) == 0) {
            myViewHolder.content.setBackgroundResource(R.color.wildSand);
        } else {
            myViewHolder.content.setBackgroundResource(R.color.white);
        }

        myViewHolder.tvTitle.setText(mList.get(position).getTitle());


        myViewHolder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment != null) {
                    fragment.showDetailFilme(mList.get(position));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }

        return mList.size();
    }

    public void update(List<Filmes> mList) {
        this.mList = mList;
        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;

        public RelativeLayout content;


        public MyViewHolder(View itemView) {
            super(itemView);

            tvTitle   = (TextView) itemView.findViewById(R.id.txt_title);
            content = (RelativeLayout) itemView.findViewById(R.id.relative_content);

        }

    }
}
