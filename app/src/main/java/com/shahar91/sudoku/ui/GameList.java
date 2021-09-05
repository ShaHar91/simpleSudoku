package com.shahar91.sudoku.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cursoradapter.widget.ResourceCursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.shahar91.sudoku.Music;
import com.shahar91.sudoku.R;
import com.shahar91.sudoku.data.DatabaseConnector;

public class GameList extends ListActivity {

    private static final String TAG = "Sudoku";

    public static final String ROW_ID = "row_id";

    public static final String KEY_DIFFICULTY = "com.ShaHar91.sudoku.difficulty";
    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFICULTY_MEDIUM = 1;
    public static final int DIFFICULTY_HARD = 2;

    int diff;

    SimpleCursorAdapter dexAdapter;
    MyAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_list);

        DatabaseConnector databaseConnector = new DatabaseConnector(
                GameList.this);
        mListAdapter = new MyAdapter(this, null);

        diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
        getPuzzle(diff);

        // String[] from = new String[] { "item", "score" };
        // int[] to = new int[] { R.id.game_id, R.id.ratingBar1 };
        // dexAdapter = new SimpleCursorAdapter(GameList.this,
        // R.layout.list_item,
        // null, from, to);
        // setListAdapter(dexAdapter);

        // Cursor myCur = null;
        //
        // myCur = databaseConnector.getAllEasy();

        mListAdapter = new MyAdapter(this, null);
        setListAdapter(mListAdapter);
    }

    private class MyAdapter extends ResourceCursorAdapter {

        public MyAdapter(Context context, Cursor cur) {
            super(context, R.layout.list_item, cur);
        }

        @Override
        public void bindView(View view, Context context, Cursor cur) {

            TextView gameId = (TextView) view.findViewById(R.id.game_id);
            TextView gameTime = (TextView) view.findViewById(R.id.game_time);
            RatingBar score = (RatingBar) view.findViewById(R.id.ratingBar1);

            gameId.setText(cur.getString(cur.getColumnIndex("item")) + ".");
            gameTime.setText(cur.getString(cur.getColumnIndex("time")));
            score.setRating(cur.getInt(cur.getColumnIndex("score")));

        }
    }

    private class GetEasyTask extends AsyncTask<Object, Object, Cursor> {

        // perform the database access
        @Override
        protected Cursor doInBackground(Object... params) {
            DatabaseConnector databaseConnector = new DatabaseConnector(
                    GameList.this);
            // get a cursor containing all books
            return databaseConnector.getAllEasy();
        }

        // use the Cursor returned from the doInBackground method
        @Override
        protected void onPostExecute(Cursor result) {
            mListAdapter.changeCursor(result); // set the adapter's Cursor
        }
    }

    private class GetMediumTask extends AsyncTask<Object, Object, Cursor> {

        // perform the database access
        @Override
        protected Cursor doInBackground(Object... params) {
            DatabaseConnector databaseConnector = new DatabaseConnector(
                    GameList.this);
            // get a cursor containing all books
            return databaseConnector.getAllMedium();
        }

        // use the Cursor returned from the doInBackground method
        @Override
        protected void onPostExecute(Cursor result) {
            mListAdapter.changeCursor(result); // set the adapter's Cursor
        }
    }

    private class GetHardTask extends AsyncTask<Object, Object, Cursor> {

        // perform the database access
        @Override
        protected Cursor doInBackground(Object... params) {
            DatabaseConnector databaseConnector = new DatabaseConnector(
                    GameList.this);
            // get a cursor containing all books
            return databaseConnector.getAllHard();
        }

        // use the Cursor returned from the doInBackground method
        @Override
        protected void onPostExecute(Cursor result) {

            mListAdapter.changeCursor(result); // set the adapter's Cursor
        }
    }

    private int[] getPuzzle(int diff) {
        switch (diff) {
            case DIFFICULTY_HARD:
                new GetHardTask().execute();
                break;
            case DIFFICULTY_MEDIUM:
                new GetMediumTask().execute();

                break;
            case DIFFICULTY_EASY:
            default:
                new GetEasyTask().execute();

                break;
        }
        return null;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent game = new Intent(GameList.this, Game.class);

        game.putExtra(ROW_ID, position);
        game.putExtra(Game.KEY_DIFFICULTY, diff);

        startActivity(game);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Music.INSTANCE.play(this, R.raw.main);
    }
}