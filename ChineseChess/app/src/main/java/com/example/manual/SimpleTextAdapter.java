package com.example.manual;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tang.chinesechess.R;

import java.util.List;

/**
 * Author:  andy.xwt
 * Date:    2018/6/20 15:49
 * Description:
 */

public class SimpleTextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private final View.OnClickListener clickListener;

    private List<String> list;

    public SimpleTextAdapter(Context context, View.OnClickListener clickListener) {
        this.mContext = context;
        this.clickListener = clickListener;
        resetSelected();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("SimpleTextAdapter", "onCreateViewHolder");
        SimpleTextHolder holder = new SimpleTextHolder(LayoutInflater.from(mContext).inflate(R.layout.item_simple_text, parent, false));
        holder.mTextView.setOnClickListener(clickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SimpleTextHolder textHolder = (SimpleTextHolder) holder;
        textHolder.bind(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void reset(List<String> stringList) {
        list = stringList;
        resetSelected();
        notifyDataSetChanged();
    }

    private class SimpleTextHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        SimpleTextHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_text);
            mTextView.setTextColor(getColorStateList());
        }

        public void bind(String s, int position) {
            mTextView.setText(s);
            mTextView.setTag(s);
            mTextView.setSelected(position == current);
        }
    }


    private int[][] states;
    private int[] colors;
    public ColorStateList getColorStateList() {
        if (states == null) {
            states = new int[][]{
                    new int[]{android.R.attr.state_pressed},
                    new int[]{android.R.attr.state_focused},
                    new int[]{android.R.attr.state_selected},
                    new int[]{}
            };
        }

        if (colors == null) {
            colors = new int[]{
                    getColorPressed(),
                    getColorPressed(),
                    getColorPressed(),
                    getColor()
            };
        }
        return new ColorStateList(states, colors);
    }

    private int getColorPressed() {
        return Color.BLUE;
    }

    private int getColor() {
        return Color.WHITE;
    }

    private int current = -1;
    private void resetSelected() {
        current = null == list || list.isEmpty() ? -1 : 0;
    }

    public void toRight(RecyclerView recyclerView) {
        setSelected(recyclerView, false);
        if (current < getItemCount()) {
            current++;
        }
        setSelected(recyclerView, true);
    }

    private void setSelected(RecyclerView recyclerView, boolean selected) {
        View view = recyclerView.findViewWithTag(current);
        if (null != view) {
            view.setSelected(selected);
        }
    }

    public void toLeft(RecyclerView recyclerView) {
        setSelected(recyclerView, false);
        if (current > 0) {
            current--;
        }
        setSelected(recyclerView, true);
    }
}
