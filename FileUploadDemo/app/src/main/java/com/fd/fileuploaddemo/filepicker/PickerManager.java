package com.fd.fileuploaddemo.filepicker;

import com.fd.fileuploaddemo.R;
import com.fd.fileuploaddemo.filepicker.models.BaseFile;

import java.util.ArrayList;

/**
 * Created by Pranit on 12-09-2016.
 */

public class PickerManager {
    private static PickerManager ourInstance = new PickerManager();
    private int maxCount = FilePickerConst.DEFAULT_MAX_COUNT;
    private int currentCount;
    private PickerManagerListener pickerManagerListener;
    private ArrayList<String> alreadySelectedFiles;

    public static PickerManager getInstance() {
        return ourInstance;
    }

    private ArrayList<BaseFile> files;

    private int theme = R.style.AppTheme;

    private PickerManager() {
        files = new ArrayList<>();
    }

    public void setMaxCount(int count)
    {
        clearSelections();
        this.maxCount = count;
    }


    public int getCurrentCount() {
        return currentCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setPickerManagerListener(PickerManagerListener pickerManagerListener)
    {
        this.pickerManagerListener = pickerManagerListener;
    }

    public void add(BaseFile file)
    {
        if(file!=null && shouldAdd() && !files.contains(file))
        {
            files.add(file);
            currentCount++;

            if(pickerManagerListener!=null)
                pickerManagerListener.onItemSelected(currentCount);
        }
    }

    public void remove(BaseFile file)
    {
        if(files.contains(file))
        {
            files.remove(file);
            currentCount--;

            if(pickerManagerListener!=null)
                pickerManagerListener.onItemSelected(currentCount);
        }
    }

    public boolean shouldAdd()
    {
        return currentCount < maxCount;
    }

    public ArrayList<BaseFile> getSelectedFiles()
    {
        return files;
    }

    public ArrayList<String> getSelectedFilePaths()
    {
        ArrayList<String> paths = new ArrayList<>();
        for (int index = 0; index < files.size(); index++) {
            paths.add(files.get(index).getPath());
        }
        return paths;
    }

    public void clearSelections()
    {
        files.clear();
        currentCount = 0;
        maxCount=0;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }
}
