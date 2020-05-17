package com.search.tr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder>
        implements Filterable {
    private List<Movie> movieList;
    private List<Movie> movieListFiltered;
    private ContactsAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, fullName;
        //ImageView thumbnail;


        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            fullName = view.findViewById(R.id.full_name);
            //thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected movie in callback
                    listener.onContactSelected(movieListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    MoviesAdapter(List<Movie> movieList, ContactsAdapterListener listener) {
        this.listener = listener;
        this.movieList = movieList;
        this.movieListFiltered = movieList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Movie movie = movieListFiltered.get(position);
        holder.name.setText(movie.getNormalized_name().toUpperCase());
        holder.fullName.setText(movie.getName());

//        RequestOptions options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher_round)
//                .error(R.mipmap.ic_launcher_round);
//
//        Glide.with(holder.itemView.getContext()).load(movie.getThumbNailUrl()).apply(options).into(holder.thumbnail);


    }

    @Override
    public int getItemCount() {
        return movieListFiltered ==null ? 0: movieListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    movieListFiltered = movieList;
                } else {
                    List<Movie> filteredList = new ArrayList<>();
                    for (Movie row : movieList) {

                        if (row.getNormalized_name().toLowerCase().contains(charString.toLowerCase()) || row.getUrl().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    movieListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = movieListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                movieListFiltered = (ArrayList<Movie>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Movie movie);
    }
}
