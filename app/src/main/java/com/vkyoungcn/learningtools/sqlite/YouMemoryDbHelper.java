package com.vkyoungcn.learningtools.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by VkYoung16 on 2018/3/26 0026.
 */

public class YouMemoryDbHelper extends SQLiteOpenHelper {
    //如果修改了数据库结构方案，则应当改动（增加）版本号
    private static final String TAG = "YouMemory-DbHelper";
    private static final int DATEBASE_VERSION = 1;
    private static final String DATEBASE_NAME = "YouMemory.db";
    private volatile static YouMemoryDbHelper sYouMemoryDbHelper = null;
    private SQLiteDatabase mSQLiteDatabase = null;

    /*建表语句的构造*/

    /* Mission、Group、MissionXGroup在程序初次运行时即创建；
     * 各任务特有的Items、GroupXItems表在添加具体任务时创建；
     * 因为各任务的Item表名后缀不同*/
    public static final String SQL_CREATE_MISSION =
            "CREATE TABLE " + YouMemoryContract.Mission.TABLE_NAME + " (" +
                    YouMemoryContract.Mission._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    YouMemoryContract.Mission.COLUMN_NAME + " TEXT, "+
                    YouMemoryContract.Mission.COLUMN_DESCRIPTION + " TEXT)";

    public static final String SQL_CREATE_GROUP =
            "CREATE TABLE " + YouMemoryContract.Group.TABLE_NAME + " (" +
                    YouMemoryContract.Group._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    YouMemoryContract.Group.COLUMN_DESCRIPTION + " TEXT, "+
                    YouMemoryContract.Group.COLUMN_INIT_PICK_TIME + " DATE, "+
                    YouMemoryContract.Group.COLUMN_LAST_PICK_TIME + " DATE, "+
                    YouMemoryContract.Group.COLUMN_TIME_PICKED + " INTEGER, "+
                    YouMemoryContract.Group.COLUMN_SPECIAL_MARK + " INTEGER)";

    public static final String SQL_CREATE_MISSION_X_GROUP =
            "CREATE TABLE " + YouMemoryContract.MissionCrossGroup.TABLE_NAME + " (" +
                    YouMemoryContract.MissionCrossGroup._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    YouMemoryContract.MissionCrossGroup.COLUMN_MISSION_ID + " INTEGER, "+
                    YouMemoryContract.MissionCrossGroup.COLUMN_GROUP_ID + " INTEGER)";


    /*以下两种表需要根据具体的任务id创建，需动态生成建表语句*/
    /* 根据Mission_id创建具体的任务项目表，所需语句*/
    public String getSqlCreateItemWithMissionId(int mission_id){
         return "CREATE TABLE " +
                YouMemoryContract.ItemBasic.TABLE_NAME + mission_id+" (" +
                YouMemoryContract.ItemBasic._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                YouMemoryContract.ItemBasic.COLUMN_NAME + " TEXT, " +
                YouMemoryContract.ItemBasic.COLUMN_EXTENDING_LIST + " TEXT, " +
                YouMemoryContract.ItemBasic.COLUMN_PICKING_TIME_LIST + " TEXT)";
    }

    /* 根据Mission_id创建具体的项目-分组交叉表，所需语句*/
    public String getSqlCreateGroupXItemWithMissionId(int mission_id){
        return "CREATE TABLE " +
                YouMemoryContract.GroupCrossItem.TABLE_NAME + mission_id+" (" +
                YouMemoryContract.GroupCrossItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                YouMemoryContract.GroupCrossItem.COLUMN_GROUP_ID + " INTEGER, " +
                YouMemoryContract.GroupCrossItem.COLUMN_ITEM_ID + " INTEGER)";
    }

    private static final String SQL_DROP_MISSION =
            "DROP TABLE IF EXISTS " +  YouMemoryContract.Mission.TABLE_NAME;
    private static final String SQL_DROP_GROUP =
            "DROP TABLE IF EXISTS " + YouMemoryContract.Group.TABLE_NAME;
    private static final String SQL_DROP_MISSION_X_GROUP =
            "DROP TABLE IF EXISTS " + YouMemoryContract.MissionCrossGroup.TABLE_NAME;

    /*以下两种表的删除语句动态生成*/
    public String getSqlDropItemWithMissionId(int mission_id){
        return "DROP TABLE IF EXISTS " +  YouMemoryContract.ItemBasic.TABLE_NAME + mission_id;
    }

    public String getSqlDropGroupXItemWithMissionId(int mission_id){
        return "DROP TABLE IF EXISTS " +  YouMemoryContract.GroupCrossItem.TABLE_NAME + mission_id;
    }

    public YouMemoryDbHelper(Context context) {
        super(context, DATEBASE_NAME, null, DATEBASE_VERSION);
        //Log.i(TAG,"inside YouMemoryDbHelper Constructor, after the super ");
        this.mSQLiteDatabase = getWritableDatabase();
        //Log.i(TAG,"inside YouMemoryDbHelper Constructor, got the Wdb: "+mSQLiteDatabase.toString());
    }

    //DCL模式单例，因为静态内部类模式不支持传参
    public static YouMemoryDbHelper getInstance(Context context){
        //Log.i(TAG,"inside YouMemoryDbHelper getInstance, before any calls");
        if(sYouMemoryDbHelper == null){
            synchronized (YouMemoryDbHelper.class){
                if(sYouMemoryDbHelper == null){
                    sYouMemoryDbHelper = new YouMemoryDbHelper(context);
                }
            }
        }
        return sYouMemoryDbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.i(TAG,"inside YouMemoryDbHelper onCreate");
        db.execSQL(SQL_CREATE_MISSION);
        //Log.i(TAG,"inside YouMemoryDbHelper.onCreate(),behind 1st CREATE");
        db.execSQL(SQL_DROP_GROUP);
        //Log.i(TAG,"inside YouMemoryDbHelper.onCreate(),behind 2nd CREATE");
        db.execSQL(SQL_CREATE_MISSION_X_GROUP);
        //Log.i(TAG,"inside YouMemoryDbHelper.onCreate(),behind 3rd CREATE");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //删掉旧表
        db.execSQL(SQL_DROP_MISSION);
        db.execSQL(SQL_CREATE_MISSION_X_GROUP);
        db.execSQL(SQL_DROP_GROUP);

        //重建新表
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db,oldVersion,newVersion);
    }

    /*CRUD部分需要时再写*/

}
