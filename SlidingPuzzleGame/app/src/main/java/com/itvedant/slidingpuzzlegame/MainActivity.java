package com.itvedant.slidingpuzzlegame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    protected static final int MENU_SCRAMBLE = 0;
    protected static final int MENU_SELECT_IMAGE = 1;
    protected static final int MENU_TAKE_PHOTO = 2;

    protected static final int RESULT_SELECT_IMAGE = 0;
    protected static final int RESULT_TAKE_PHOTO = 1;

    protected static final String KEY_SHOW_NUMBERS = "showNumbers";
    protected static final String KEY_IMAGE_URI = "imageUri";
    protected static final String KEY_PUZZLE = "slidePuzzle";
    protected static final String KEY_PUZZLE_SIZE = "puzzleSize";

    protected static final String FILENAME_DIR = "com.itvedant.slidingpuzzlegame";
    protected static final String FILENAME_PHOTO_DIR = FILENAME_DIR + "/photo";
    protected static final String FILENAME_PHOTO = "photo.jpg";

    protected static final int DEFAULT_SIZE = 3;

    private SlidePuzzleView view;
    private SlidePuzzle slidePuzzle;
    private BitmapFactory.Options bitmapOptions;
    private int puzzleWidth = 1;
    private int puzzleHeight = 1;
    private Uri imageUri;
    private boolean portrait;
    private boolean expert;

    MediaPlayer mPlayer;
    private final java.util.List<String> mActList = new java.util.ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;

        slidePuzzle = new SlidePuzzle();

        view = new SlidePuzzleView(this, slidePuzzle);
        setContentView(view);

        shuffle();

        if (!loadPreferences()) {
            setPuzzleSize(DEFAULT_SIZE, true);
        }

        Uri path = Uri.parse("android.resource://com.itvedant.slidingpuzzlegame/" + R.drawable.dolby);

        loadBitmap(path);
    }

    private void shuffle() {
        slidePuzzle.init(puzzleWidth, puzzleHeight);
        slidePuzzle.shuffle();
        view.invalidate();
        expert = view.getShowNumbers() == SlidePuzzleView.ShowNumbers.NONE;
    }

    protected void loadBitmap(Uri uri) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            InputStream imageStream = getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(imageStream, null, o);

            int targetWidth = view.getTargetWidth();
            int targetHeight = view.getTargetHeight();

            if (o.outWidth > o.outHeight && targetWidth < targetHeight) {
                int i = targetWidth;
                targetWidth = targetHeight;
                targetHeight = i;
            }

            if (targetWidth < o.outWidth || targetHeight < o.outHeight) {
                double widthRatio = (double) targetWidth / (double) o.outWidth;
                double heightRatio = (double) targetHeight / (double) o.outHeight;
                double ratio = Math.max(widthRatio, heightRatio);

                o.inSampleSize = (int) Math.pow(2, (int) Math.round(Math.log(ratio) / Math.log(0.5)));
            } else {
                o.inSampleSize = 1;
            }

            o.inScaled = false;
            o.inJustDecodeBounds = false;

            imageStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream, null, o);

            if (bitmap == null) {
                Toast.makeText(this, getString(R.string.error_could_not_load_image), Toast.LENGTH_LONG).show();
                return;
            }

            int rotate = 0;

            Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        rotate = cursor.getInt(0);

                        if (rotate == -1) {
                            rotate = 0;
                        }
                    }
                } finally {
                    cursor.close();
                }
            }

            if (rotate != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            setBitmap(bitmap);
            imageUri = uri;
        } catch (FileNotFoundException ex) {
            Toast.makeText(this, MessageFormat.format(getString(R.string.error_could_not_load_image_error), ex.getMessage()), Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void setBitmap(Bitmap bitmap) {
        portrait = bitmap.getWidth() < bitmap.getHeight();

        view.setBitmap(bitmap);
        setPuzzleSize(Math.min(puzzleWidth, puzzleHeight), true);

        setRequestedOrientation(portrait ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void selectImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_SELECT_IMAGE);
    }

    private void takePicture() {
        File dir = getSaveDirectory();

        if (dir == null) {
            Toast.makeText(this, getString(R.string.error_could_not_create_directory_to_store_photo), Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(dir, FILENAME_PHOTO);
        Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(photoPickerIntent, RESULT_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case RESULT_SELECT_IMAGE: {
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    loadBitmap(selectedImage);
                }

                break;
            }

            case RESULT_TAKE_PHOTO: {
                if (resultCode == RESULT_OK) {
                    File file = new File(getSaveDirectory(), FILENAME_PHOTO);

                    if (file.exists()) {
                        Uri uri = Uri.fromFile(file);

                        if (uri != null) {
                            loadBitmap(uri);
                        }
                    }
                }

                break;
            }
        }
    }

    private File getSaveDirectory() {
        File root = new File(Environment.getExternalStorageDirectory().getPath());
        File dir = new File(root, FILENAME_PHOTO_DIR);

        if (!dir.exists()) {
            if (!root.exists() || !dir.mkdirs()) {
                return null;
            }
        }

        return dir;
    }

    private float getImageAspectRatio() {
        Bitmap bitmap = view.getBitmap();

        if (bitmap == null) {
            return 1;
        }

        float width = bitmap.getWidth();
        float height = bitmap.getHeight();

        return width / height;
    }

    protected void setPuzzleSize(int size, boolean scramble) {
        float ratio = getImageAspectRatio();

        if (ratio < 1) {
            ratio = 1f / ratio;
        }

        int newWidth;
        int newHeight;

        if (portrait) {
            newWidth = size;
            newHeight = (int) (size * ratio);
        } else {
            newWidth = (int) (size * ratio);
            newHeight = size;
        }

        if (scramble || newWidth != puzzleWidth || newHeight != puzzleHeight) {
            puzzleWidth = newWidth;
            puzzleHeight = newHeight;
            shuffle();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean hasCamera = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);

        menu.add(0, MENU_SELECT_IMAGE, 0, R.string.menu_select_image);

        if (hasCamera) {
            menu.add(0, MENU_TAKE_PHOTO, 0, R.string.menu_take_photo);
        }

        menu.add(0, MENU_SCRAMBLE, 0, R.string.menu_scramble);

        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return onOptionsItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SCRAMBLE:
                shuffle();
                return true;

            case MENU_SELECT_IMAGE:
                selectImage();
                return true;

            case MENU_TAKE_PHOTO:
                takePicture();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected SharedPreferences getPreferences() {
        return getSharedPreferences(MainActivity.class.getName(), Activity.MODE_PRIVATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mPlayer != null) {
            mPlayer.pause();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mPlayer != null) {
            mPlayer.start();
        }
    }

    protected boolean loadPreferences() {
        SharedPreferences prefs = getPreferences();

        try {

            String s = prefs.getString(KEY_IMAGE_URI, null);

            if (s == null) {
                imageUri = null;
            } else {
                loadBitmap(Uri.parse(s));
            }

            int size = prefs.getInt(KEY_PUZZLE_SIZE, 0);
            s = prefs.getString(KEY_PUZZLE, null);

            if (size > 0 && s != null) {
                String[] tileStrings = s.split("\\;");

                if (tileStrings.length / size > 1) {
                    setPuzzleSize(size, false);
                    slidePuzzle.init(puzzleWidth, puzzleHeight);

                    int[] tiles = new int[tileStrings.length];

                    for (int i = 0; i < tiles.length; i++) {
                        try {
                            tiles[i] = Integer.parseInt(tileStrings[i]);
                        } catch (NumberFormatException ex) {
                        }
                    }

                    slidePuzzle.setTiles(tiles);
                }
            }

            return prefs.contains(KEY_SHOW_NUMBERS);
        } catch (ClassCastException ex) {
            // ignore broken settings
            return false;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("Dolby processing", "onDestroy()");

        // Release Media Player instance
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
