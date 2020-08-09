package com.vidya.axxesssearchimageassignment.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.vidya.axxesssearchimageassignment.R;
import com.vidya.axxesssearchimageassignment.activity.ImageViewScreenActivity;
import com.vidya.axxesssearchimageassignment.model.Datum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vidya.
 */
public class SearchByImageAdapter extends RecyclerView.Adapter<SearchByImageAdapter.ViewHolder> implements Filterable {
    private List<Datum> listdata;
    private List<Datum> mImageFilteredList;
    List<Datum> suggestions;
    ViewHolder holder;
    private int position;
    private SearchByImageAdapterItemClickListener searchByImageAdapterItemClickListener;
    private LayoutInflater inflater;
    private Context context;

    String name;

    public SearchByImageAdapter(Context context, List<Datum> listdata, SearchByImageAdapterItemClickListener searchByImageAdapterItemClickListener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listdata = listdata;
        mImageFilteredList=new ArrayList<>(listdata);
        suggestions=new ArrayList<Datum>();
        this.searchByImageAdapterItemClickListener = searchByImageAdapterItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.fragment_search_by_image, parent, false);
        return new ViewHolder(listItem, searchByImageAdapterItemClickListener);
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }


    // This is important method due to which even if you scroll list, editText values will not get changed/empty. This method will maintain its position in recyclerview
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //reference to edit text

        final Datum myListData = listdata.get(position);
        holder.imageName.setTag(position);
        holder.imageName.setText(myListData.getTitle());
      //  Glide.with(context).load(myListData.getLink()).into(holder.profile_image);
        Picasso picasso = new Picasso.Builder(context).listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        }).build();

        Picasso.with(context).setLoggingEnabled(true);
        Picasso.with(context).load((myListData.getLink())).memoryPolicy(MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_STORE).placeholder(R.drawable.ic_placeholder_image_24).into(holder.profile_image);


        holder.setIsRecyclable(true);
        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageViewScreenActivity.class);
                intent.putExtra("name", myListData.getTitle());
                intent.putExtra("img_url", myListData.getLink());

                context.startActivity(intent);
            }
        });

        holder.imageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageViewScreenActivity.class);
                intent.putExtra("name", myListData.getTitle());
                intent.putExtra("img_url", myListData.getLink());
                context.startActivity(intent);
            }
        });
    }

    //    //filter result on image name

    @Override
    public Filter getFilter() {

        return nameFilter;
    }


    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            //suggestions.clear();
            String str = String.valueOf(((Datum) resultValue).getTitle());
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Datum image : mImageFilteredList) {
                    Log.e("image",""+mImageFilteredList.toString());
                    if (image.getTitle().toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(image);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Datum> filterList = (ArrayList<Datum>) results.values;

            if (results != null && results.count > 0) {
                listdata.clear();

                for (Datum imagesFilter : filterList) {
                    listdata.add(imagesFilter);
                    Log.e("Imagesss",""+imagesFilter.getTitle());
                }
                notifyDataSetChanged();
            }
        }
    };

    //view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View mView;
        TextView imageName;
        ImageView profile_image;
        SearchByImageAdapterItemClickListener searchByImageAdapterItemClickListener;
        public ViewHolder(View itemView, SearchByImageAdapterItemClickListener searchByImageAdapterItemClickListener) {
            super(itemView);
            this.imageName = (TextView) itemView.findViewById(R.id.imageName);
            this.profile_image = (ImageView) itemView.findViewById(R.id.profile_image );
            this.searchByImageAdapterItemClickListener = searchByImageAdapterItemClickListener;

            mView = itemView;
            itemView.setOnClickListener(this);
            imageName.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            //Do whatever you what with the position
            Toast.makeText(context, "my tournament list clicked Position : " + position, Toast.LENGTH_SHORT).show();
            searchByImageAdapterItemClickListener.onItemClick(v,getAdapterPosition());


        }
    }

    public interface SearchByImageAdapterItemClickListener {
        void onItemClick(View view, int position);

    }

    public interface SearchByImageAdapterListener {
        void onImageSelected(Datum data);
    }

    public void filterList(ArrayList<Datum> filterdNames) {
        this.listdata = filterdNames;
        notifyDataSetChanged();
    }
}