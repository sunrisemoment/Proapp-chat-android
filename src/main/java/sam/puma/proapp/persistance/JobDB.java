package sam.puma.proapp.persistance;


/**
 * Created by Anton on 3/31/2016.
 */

    import java.util.ArrayList;
    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.database.sqlite.SQLiteDatabase;

    import sam.puma.proapp.entities.Job;

public class JobDB extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "job";

    public static String CREATEACTIONTABLE = "create table "+ Job.ACTIONTABLENAME+"("
            + Job.ACTIONID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + Job.ACTIONNAME + " Text) ";
    public static String CREATEWORKTABLE = "create table " + Job.WORKTABLENAME+"("
            + Job.WORKID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + Job.WORKTITLE + " Text ) ";
    public static String CREATELOCATION = "create table "+Job.LOCATIONTABLENAME+"("
            + Job.LOCATIONID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + Job.LOCATIONNAME + " Text ) ";
    public static String CREATEQUANTITYTABLE = "create table "+ Job.QUANTITYTABLENAME+"("
            + Job.QUANTITYID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + Job.QUANTITY + " TEXT)";
        public JobDB(Context context)
        {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(CREATEACTIONTABLE);
            db.execSQL(CREATELOCATION);
            db.execSQL(CREATEWORKTABLE);
            db.execSQL(CREATEQUANTITYTABLE);
            ContentValues contentValues = new ContentValues();
            contentValues.put(Job.ACTIONNAME, "Start");
            db.insert(Job.ACTIONTABLENAME, null, contentValues);
            contentValues.put(Job.ACTIONNAME, "Finish");
            db.insert(Job.ACTIONTABLENAME, null, contentValues);
            contentValues.put(Job.ACTIONNAME, "Offer");
            db.insert(Job.ACTIONTABLENAME, null, contentValues);
            contentValues.put(Job.ACTIONNAME, "Accept");
            db.insert(Job.ACTIONTABLENAME, null, contentValues);
            contentValues.put(Job.ACTIONNAME, "AssignWork");
            db.insert(Job.ACTIONTABLENAME, null, contentValues);
            contentValues.put(Job.ACTIONNAME, "Abort");
            db.insert(Job.ACTIONTABLENAME, null, contentValues);
            contentValues.put(Job.ACTIONNAME, "Appraise");
            db.insert(Job.ACTIONTABLENAME, null, contentValues);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS "+Job.ACTIONTABLENAME);
            db.execSQL("DROP TABLE IF EXISTS " + Job.WORKTABLENAME);
            db.execSQL("DROP TABLE IF EXISTS " + Job.LOCATIONTABLENAME);
            db.execSQL("DROP TABLE IF EXISTS " + Job.QUANTITYTABLENAME);
            onCreate(db);
        }

        public boolean addAction(String actionname)
        {
            SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(Job.ACTIONNAME, actionname);
                db.insert(Job.ACTIONTABLENAME, null, contentValues);
            return true;
        }

        public boolean updateAction (Integer id, String actionname)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Job.ACTIONNAME, actionname);
            db.update(Job.ACTIONTABLENAME, contentValues, Job.ACTIONNAME+ "= ? ", new String[]{actionname});
            return true;
        }

        public Integer deleteAction (String actionName)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(Job.ACTIONTABLENAME,
                    Job.ACTIONNAME+"= ? ",
                    new String[] {actionName});
        }

        public ArrayList<String> getAllActions()
        {
            ArrayList<String> array_list = new ArrayList<String>();

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from "+Job.ACTIONTABLENAME, null );
            res.moveToFirst();

            while(res.isAfterLast() == false){
                array_list.add(res.getString(res.getColumnIndex(Job.ACTIONNAME)));
                res.moveToNext();
            }
            return array_list;
        }


    ///////////////////////////////////////////////////////////////////////////////


    public boolean addWork(String workname) {
        String name = workname.trim();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from " + Job.WORKTABLENAME + " where " + Job.WORKTITLE + " = ?", new String[]{name});
        if(c.getCount()== 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Job.WORKTITLE, name);
//            db.insert(Job.WORKTABLENAME, null, contentValues);
            db.insertWithOnConflict(Job.WORKTABLENAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            return true;
        } else {
            return false;
        }
    }
    public String  getwork(int id){
        return null;
    }

    public ArrayList<String > getAllWork(){
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+Job.WORKTABLENAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(Job.WORKTITLE)));
            res.moveToNext();
        }
        return array_list;
    }
    public Integer  deleteWork(String workname){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Job.WORKTABLENAME,
                Job.WORKTITLE+" = ?",new String[]{workname});

    }
    public void updateWork(int id ,String worktitle){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Job.WORKTITLE, worktitle);
        db.update(Job.WORKTABLENAME, contentValues, Job.WORKID + " = ? ", new String[]{Integer.toString(id)});
    }

    /////////////////////////////////////////////////////////////////////////////////////
    public boolean addLocation(String locationname) {
        String name = locationname.trim();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from " + Job.LOCATIONTABLENAME + " where " + Job.LOCATIONNAME + " = ?", new String[]{name});
        if (c.getCount() == 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Job.LOCATIONNAME, name);
            db.insert(Job.LOCATIONTABLENAME, null, contentValues);
            return true;
        } else {
            return false;
        }
    }

    public Cursor getLocation(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =  db.rawQuery( "select * from "+Job.LOCATIONTABLENAME+" where id="+id+"", null );
        return c;
    }

    public boolean updateLocation (Integer id, String locationname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Job.LOCATIONNAME, locationname);
        db.update(Job.LOCATIONTABLENAME, contentValues, Job.LOCATIONID+" = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteLocation (String command)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Job.LOCATIONTABLENAME,
               Job.LOCATIONNAME + " = ? ", new String[]{command});
    }

    public ArrayList<String> getAllLocation()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+Job.LOCATIONTABLENAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(Job.LOCATIONNAME)));
            res.moveToNext();
        }
        return array_list;
    }

   ///Quantity//////////////////////////////////////////////////////////////////////////////////////

    public boolean addQuantity(String name){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Job.QUANTITY, name);
        db.insert(Job.QUANTITYTABLENAME, null, contentValues);
        return true;
    }
    public ArrayList<String> getAllQuantity(){
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+Job.QUANTITYTABLENAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(Job.QUANTITY)));
            res.moveToNext();
        }
        return array_list;
    }
    public Integer deleteQuantity(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Job.QUANTITYTABLENAME,
                Job.QUANTITY + " = ? ", new String[]{name});

    }
    public void updateQuantity(String name){

    }
}
