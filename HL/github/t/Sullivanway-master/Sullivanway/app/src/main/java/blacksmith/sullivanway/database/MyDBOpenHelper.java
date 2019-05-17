package blacksmith.sullivanway.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "subway.db";

    public MyDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        onCreate(db);
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL(Congestion.SQL_CREATE);
        db.execSQL(Elevator.SQL_CREATE);
        db.execSQL(FavoriteRoute.SQL_CREATE);
        db.execSQL(DownLine.SQL_CREATE);
        db.execSQL(Station.SQL_CREATE);
        db.execSQL(Transfer.SQL_CREATE);
        db.execSQL(TransferMap.SQL_CREATE);
    }

    private void dropTables(SQLiteDatabase db) {
        db.execSQL(Congestion.SQL_DROP);
        db.execSQL(Elevator.SQL_DROP);
        db.execSQL(FavoriteRoute.SQL_DROP);
        db.execSQL(DownLine.SQL_DROP);
        db.execSQL(Station.SQL_DROP);
        db.execSQL(Transfer.SQL_DROP);
        db.execSQL(TransferMap.SQL_DROP);
    }

    public void initDatabase(SQLiteDatabase db, boolean retry) {
        try {
            // 데이터 삭제&재삽입
            db.execSQL(Congestion.SQL_DELETE_ALL);
            db.execSQL(Elevator.SQL_DELETE_ALL);
            db.execSQL(FavoriteRoute.SQL_DELETE_ALL);
            db.execSQL(DownLine.SQL_DELETE_ALL);
            db.execSQL(Station.SQL_DELETE_ALL);
            db.execSQL(Transfer.SQL_DELETE_ALL);
            db.execSQL(TransferMap.SQL_DELETE_ALL);

            Congestion.initDatabase();
            Elevator.initDatabase();
            DownLine.initDatabase();
            Station.initDatabase();
            Transfer.initDatabase();
            TransferMap.initDatabase();
        } catch (SQLiteException e) {
            if (retry) {
                // 테이블 삭제&재생성
                // TODO 유저 데이터는 따로 백업이 필요하다...
                dropTables(db);
                createTables(db);
                initDatabase(db, false);
            } else {
                e.printStackTrace();
                System.exit(1);
            }

        }

    }

    public void setDatabase(SQLiteDatabase db) {
        Congestion.setDatabase(db);
        Elevator.setDatabase(db);
        FavoriteRoute.setDatabase(db);
        DownLine.setDatabase(db);
        Station.setDatabase(db);
        Transfer.setDatabase(db);
        TransferMap.setDatabase(db);
    }
}
