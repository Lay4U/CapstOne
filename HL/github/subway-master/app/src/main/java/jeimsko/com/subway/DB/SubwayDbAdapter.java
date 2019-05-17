package jeimsko.com.subway.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import jeimsko.com.subway.utils.ComUtils;

/**
 * Created by jeimsko on 2016. 9. 12..
 */

public class SubwayDbAdapter {

    private  static final String TAG = SubwayDbAdapter.class.getCanonicalName();

    private  DatabaseHelper   mDHelper;
    public  static  SQLiteDatabase   mDb;
    private Context    mContext = null;

    private static final String DATABASE_CREATE  = "create table subway (_id integer primary key autoincrement," +
                 "code text not null, stationname text not null, trainnum text, outcode text, cyber text, x double, y double, xwgs double, ywgs double );";
    private static final String DATABASE_NAME    = "data";
    private static final int    DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE   = "subway";

    public static  final String KEY_ROWID       = "_id";
    public static  final String KEY_CODE        = "code";
    public static  final String KEY_STATIONNAME = "stationname";
    public static  final String KEY_TRAINNUM     = "trainnum";
    public static  final String KEY_OUTCODE     = "outcode";
    public static  final String KEY_CYBER       = "cyber";
    public static  final String KEY_X           = "x";
    public static  final String KEY_Y           = "y";
    public static  final String KEY_XWGS        = "xwgs";
    public static  final String KEY_YWGS        = "ywgs";

    private static class DatabaseHelper extends SQLiteOpenHelper{

        DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name, factory, version);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            ComUtils.printLog(TAG, "onCreate ~~~~~~~~~~~~~~~");
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            ComUtils.printLog(TAG, "Upgrade data base from version " + oldVersion + " to " +newVersion + " , which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE );
            onCreate(db);

        }
    }


    public  SubwayDbAdapter(Context context){
        this.mContext = context;
    }

    public SubwayDbAdapter open() throws SQLException{

        mDHelper  =  new DatabaseHelper(this.mContext , DATABASE_NAME, null, DATABASE_VERSION);
        mDb       =  mDHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDHelper.close();
    }


    public long createSubway(String code, String stationname, String trainnum, String outcode, String cyber, double x, double y, double xwgs, double ywgs){

        ComUtils.printLog(TAG, "open ~~~~~~~~~~~~~~~ 2");
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CODE, code);
        initialValues.put(KEY_STATIONNAME, stationname);
        initialValues.put(KEY_TRAINNUM, trainnum);
        initialValues.put(KEY_OUTCODE, outcode);
        initialValues.put(KEY_CYBER, cyber);
        initialValues.put(KEY_X, x);
        initialValues.put(KEY_Y, y);
        initialValues.put(KEY_XWGS, xwgs);
        initialValues.put(KEY_YWGS, ywgs);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }


    public boolean deleteNote(long rowId){
        ComUtils.printLog(TAG, "Delete called = " + "value__" + rowId);
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + " = " + rowId, null) > 0;
    }


    public Cursor fetchAllSubway(){

        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_CODE, KEY_STATIONNAME, KEY_TRAINNUM, KEY_OUTCODE, KEY_CYBER, KEY_X, KEY_Y, KEY_XWGS, KEY_YWGS }, null, null, null, null, null);

    }


    public Cursor fetchSubway(long rowId) throws SQLException {

        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_CODE, KEY_STATIONNAME, KEY_TRAINNUM, KEY_OUTCODE, KEY_CYBER, KEY_X, KEY_Y, KEY_XWGS, KEY_YWGS }, KEY_ROWID
                + "=" + rowId, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public boolean updateSubway(long rowId, String code, String stationname, String trainname, String outcode, String cyber, double x, double y, double xwgs, double ywgs) {
        ContentValues args = new ContentValues();
        args.put(KEY_ROWID,       rowId);
        args.put(KEY_CODE,         code);
        args.put(KEY_STATIONNAME, stationname);
        args.put(KEY_TRAINNUM,    trainname);
        args.put(KEY_OUTCODE,     outcode);
        args.put(KEY_CYBER,       cyber);
        args.put(KEY_X,           x);
        args.put(KEY_Y,           y);
        args.put(KEY_XWGS,        xwgs);
        args.put(KEY_YWGS,        ywgs);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }


}
