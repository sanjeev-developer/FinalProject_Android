package com.example.firenote.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.firenote.R;
import com.example.firenote.model.UserData;

import java.util.List;


public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.MyViewHolder>
{
    private Context context;
    List<UserData> user;

    public ViewAdapter(Context contexts, List<UserData> user)
    {
        this.context = contexts;
        this.user = user;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewlayout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        holder.txt_iddata.setText(""+user.get(position).id);
        holder.txt_titledata.setText(user.get(position).getTitle());
        holder.txt_notedata.setText(user.get(position).getSubtitle());
        holder.txt_auddata.setText(user.get(position).getAudiodata());
        holder.txt_imgdata.setText(user.get(position).getImagedata());
        holder.txt_latdata.setText(""+user.get(position).getLat());
        holder.txt_longdata.setText(""+user.get(position).getLng());
        holder.txt_catdata.setText(user.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_iddata,txt_titledata,txt_notedata,txt_catdata,txt_auddata,txt_imgdata,txt_latdata,txt_longdata;


        public MyViewHolder(View view) {
            super(view);

            txt_iddata = view.findViewById(R.id.txt_iddata);
            txt_titledata = view.findViewById(R.id.txt_titledata);
            txt_notedata = view.findViewById(R.id.txt_notedata);
            txt_catdata = view.findViewById(R.id.txt_catdata);
            txt_auddata = view.findViewById(R.id.txt_auddata);
            txt_imgdata = view.findViewById(R.id.txt_imgdata);
            txt_latdata = view.findViewById(R.id.txt_latdata);
            txt_longdata = view.findViewById(R.id.txt_longdata);

        }
    }
}

