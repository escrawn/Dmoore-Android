package com.dodhev.dmooretaskmanger;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Escrawn on 06/09/2017.
 */

public class TaskAdapter extends ArrayAdapter<Task> {

    public TaskAdapter(Context context,List<Task> objects) {
        super(context, 0, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.task_list_item, parent, false);
        }
        Task currentTask = getItem(position);

        TextView idView = (TextView) listItemView.findViewById(R.id.idTask);
        idView.setText(String.valueOf(currentTask.getId()));

        TextView nameView = (TextView) listItemView.findViewById(R.id.nameTask);
        nameView.setText(currentTask.getName());

        TextView descView = (TextView) listItemView.findViewById(R.id.descTask);
        descView.setText(currentTask.getDesc());


        return listItemView;
    }
}
