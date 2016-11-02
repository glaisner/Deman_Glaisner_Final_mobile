package com.example.deman_glaisner.deman_glaisner_mobile.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by nico on 02/11/16.
 */

public class FavoriContentProvider extends ContentProvider {

    public static final Uri CONTENT_URI =
            Uri.parse("content://com.example.deman_glaisner.deman_glaisner_mobile/Favory");
    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;
    // Constantes pour identifier les requetes
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.deman_glaisner.deman_glaisner_mobile", "Favory", ALLROWS);
        uriMatcher.addURI("com.example.deman_glaisner.deman_glaisner_mobile", "Favory/#", SINGLE_ROW);
    }

    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "key_title";


    private FavoryDBHelper myFavoryHelper;

        @Override
        public boolean onCreate() {
           myFavoryHelper = new FavoryDBHelper(getContext(), FavoryDBHelper.DATABASE_NAME,null,FavoryDBHelper.DATABASE_VERSION);

            return true;
        }


        @Nullable
        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
            //ouverture de la base de donnée
            SQLiteDatabase db = myFavoryHelper.getWritableDatabase();

            //parametres de la requete SQL
            String groupBy = null;
            String having = null;

            //construction de la requete
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            //ajout de la table
            queryBuilder.setTables(FavoryDBHelper.DATABASE_TABLE);

            // si requete de ligne on limite les retours à la premiere ligne
            switch (uriMatcher.match(uri)) {
                case SINGLE_ROW:
                    String rowId = uri.getPathSegments().get(1);
                    //ajout de la clause where, si on demande un element précis
                    queryBuilder.appendWhere(KEY_ID + "=" + rowId);
                default: break;
            }
            Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
            return cursor;
        }

        @Nullable
        @Override
        public String getType (Uri uri){
            switch (uriMatcher.match(uri)){
                case ALLROWS : return "vnd.android.cursor.dir/vnd.paad.elemental";
                case SINGLE_ROW : return "vnd.android.cursor.item/vnd.paad.elemental";
                default: throw new IllegalArgumentException("URI non reconnue");
            }
        }


        @Nullable
        @Override
        public Uri insert (Uri uri, ContentValues values){
            //ouverture de la base de donnée
            SQLiteDatabase db = myFavoryHelper.getWritableDatabase();

            //hack column vide
            String nullColumnHack = null;
            switch (uriMatcher.match(uri)){
                case ALLROWS:
                    long id = db.insert(FavoryDBHelper.DATABASE_TABLE, nullColumnHack, values);
                    if (id > -1){
                        // contruit l'uri de la ligne crée
                        Uri instertedId = ContentUris.withAppendedId(uri, id);

                        //notifie le changement des données
                        getContext().getContentResolver().notifyChange(instertedId, null);

                        return instertedId;
                    }
                    default:break;
            }

            return null;

        }

        @Override
        public int delete (Uri uri, String selection, String[]selectionArgs){
            //ouverture de la base de donnée
            SQLiteDatabase db = myFavoryHelper.getWritableDatabase();
            // si requete de ligne on limite les retours à la premiere ligne
            switch (uriMatcher.match(uri)){
                case SINGLE_ROW : String rowId = uri.getPathSegments().get(1);
                    selection = KEY_ID + "=" + rowId
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                default: break;
            }
            if (selection == null) {
                selection = "1";
            }

            // on effectue la suppression
            int deleteCount = db.delete(FavoryDBHelper.DATABASE_TABLE, selection, selectionArgs);

            //notifie le changement des données
            getContext().getContentResolver().notifyChange(uri, null);

            return deleteCount;
        }

        @Override
        public int update (Uri uri, ContentValues values, String selection, String[]selectionArgs){
            //ouverture de la base de donnée
            SQLiteDatabase db = myFavoryHelper.getWritableDatabase();
            // si requete de ligne on limite les retours à la premiere ligne
            switch (uriMatcher.match(uri)){
                case SINGLE_ROW : String rowId = uri.getPathSegments().get(1);
                    selection = KEY_ID + "=" + rowId
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                default: break;
            }
            if (selection == null) {
                selection = "1";
            }

            // on effectue l'update
            int updateCount = db.update(FavoryDBHelper.DATABASE_TABLE, values, selection, selectionArgs);

            //notifie le changement des données
            getContext().getContentResolver().notifyChange(uri, null);

            return updateCount;
        }




    private static class FavoryDBHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "myDatabase";
        private static final String DATABASE_TABLE = "Favory";
        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE_CREATE = "create table " +
                DATABASE_TABLE + " (" + KEY_ID +
                " integer primary key autoincrement, " +
                KEY_TITLE + " text not null);";


        public FavoryDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("DATABASE", "Mise à jour de la version " +
                    oldVersion + " vers la verison " + newVersion +
                    ": toutes les données seront perdues.");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);

        }
    }
}