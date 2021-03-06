package br.com.zup.omdbdesafio.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.zup.omdbdesafio.R;
import br.com.zup.omdbdesafio.model.domain.Filmes;
import br.com.zup.omdbdesafio.view.fragment.MainFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


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
        myViewHolder.onBind(mList.get(position));
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
        @BindView(R.id.img_film)  ImageView imgFilm;
        @BindView(R.id.txt_title) TextView tvTitle;
        @BindView(R.id.txt_year)  TextView tvYear;
        @BindView(R.id.cardView)  CardView content;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(final Filmes filmes){
            tvTitle.setText(filmes.getTitle());
            tvYear.setText("( "+filmes.getYear()+" )");

            Picasso.with(itemView.getContext())
                    .load(filmes.getPoster())
                    .into(imgFilm);
        }

        @OnClick
        public void itemSelected(){
            if(fragment != null)
                fragment.showDetailFilme(mList.get(getAdapterPosition()));
        }

    }
}
