package com.fd.fileuploaddemo.filepicker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fd.fileuploaddemo.R;
import com.fd.fileuploaddemo.filepicker.PickerManager;
import com.fd.fileuploaddemo.filepicker.models.Document;
import com.fd.fileuploaddemo.filepicker.views.SmoothCheckBox;

import java.util.List;

/**
 * Created by Pranit on 12-09-2016.
 */

public class FileListAdapter extends SelectableAdapter<FileListAdapter.FileViewHolder, Document>{


    private final Context context;

    private SmoothCheckBox previousChecked;

    public FileListAdapter(Context context, List<Document> items, List<String> selectedPaths) {
        super(items, selectedPaths);
        this.context = context;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_doc_layout, parent, false);

        return new FileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FileViewHolder holder, int position) {
        final Document document = getItems().get(position);

        holder.imageView.setImageResource(document.getTypeDrawable());
        holder.fileNameTextView.setText(document.getTitle());
        holder.fileSizeTextView.setText(Formatter.formatShortFileSize(context, Long.parseLong(document.getSize())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PickerManager.getInstance().getMaxCount() == 1) {
                    // if single file is to be selected then deselect previous file and select new file
                    if (previousChecked != null) {
                        previousChecked.setChecked(false, true);
                    }
                    holder.checkBox.setChecked(true, true);
                    previousChecked = holder.checkBox;
                } else {
                    if (holder.checkBox.isChecked() || PickerManager.getInstance().shouldAdd()) {
                        // for multiple files
                        holder.checkBox.setChecked(!holder.checkBox.isChecked(), true);
                    }
                }
            }
        });

        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.checkBox.isChecked() && !PickerManager.getInstance().shouldAdd()) {
                    return;
                }
            }
        });

        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(isSelected(document));

        holder.itemView.setBackgroundResource(isSelected(document)?R.color.bg_gray:android.R.color.white);

        holder.checkBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                toggleSelection(document);
                holder.itemView.setBackgroundResource(isChecked?R.color.bg_gray:android.R.color.white);

                if(isChecked)
                    PickerManager.getInstance().add(document);
                else
                    PickerManager.getInstance().remove(document);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        SmoothCheckBox checkBox;

        ImageView imageView;

        TextView fileNameTextView;

        TextView fileSizeTextView;

        public FileViewHolder(View itemView) {
            super(itemView);
            checkBox = (SmoothCheckBox) itemView.findViewById(R.id.checkbox);
            imageView = (ImageView) itemView.findViewById(R.id.file_iv);
            fileNameTextView = (TextView) itemView.findViewById(R.id.file_name_tv);
            fileSizeTextView = (TextView) itemView.findViewById(R.id.file_size_tv);
        }
    }
}
