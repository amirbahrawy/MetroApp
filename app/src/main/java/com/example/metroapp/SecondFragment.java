package com.example.metroapp;


import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    private List<String> station;
    private String[] s;
    Resources res;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DividerItemDecoration dividerItemDecoration;
    View view;
    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_second, container, false);
        initViews();
        return view;
    }
    private void initViews() {
        recyclerView=view.findViewById(R.id.rec);
        layoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        dividerItemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        station=new ArrayList<>();
        res=getResources();
        s=res.getStringArray(R.array.second_line);
        for(String x:s){
            station.add(x);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration );
        recyclerView.setAdapter(new StaionsAdapter(station));
    }

    class StaionsAdapter extends RecyclerView.Adapter<StaionsAdapter.StaionsVH>{
        List<String> list;
        public StaionsAdapter(List<String> list) {
            this.list=list;
        }

        @NonNull
        @Override
        public StaionsAdapter.StaionsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(getContext()).inflate(R.layout.item,parent,false);
            return new StaionsAdapter.StaionsVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StaionsAdapter.StaionsVH holder, int position) {
            String staion=list.get(position);
            holder.textView.setText(staion);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class StaionsVH extends RecyclerView.ViewHolder {
            TextView textView;
            public StaionsVH(@NonNull View itemView) {
                super(itemView);
                textView=itemView.findViewById(R.id.text);
            }
        }
    }

}
