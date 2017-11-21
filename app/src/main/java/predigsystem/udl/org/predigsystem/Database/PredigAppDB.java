package predigsystem.udl.org.predigsystem.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Didac on 13/11/2017.
 */

public class PredigAppDB extends SQLiteOpenHelper {

    public String SQLCreate_User = "CREATE TABLE User "+
            "( NIF VARCHAR(12) PRIMARY KEY, " +
            "Password VARCHAR(50), " +
            "Name VARCHAR(60), " +
            "Email VARCHAR(100), " +
            "Phone VARCHAR(9), " +
            "Address VARCHAR(100), " +
            "createdAt VARCHAR(10) )";

    public String SQLCreate_Role = "CREATE TABLE Role " +
            "( Role VARCHAR(15), " + "nif VARCHAR(12), FOREIGN KEY (nif) REFERENCES User(NIF) )";

    public String SQLCREATE_BloodPressure = "CREATE TABLE BloodPressure " +
            "( Systolic FLOAT, " +
            "Diastolic FLOAT, " +
            "Pulse FLOAT, " +
            "Date LONG, " +
            "Latitude FLOAT, " +
            "Longitude FLOAT, " +
            "nif VARCHAR(12), FOREIGN KEY (nif) REFERENCES User(NIF) )";

    public String SQLCreate_VisitsDoctor = "CREATE TABLE VisitsDoctor " +
            "( Doctor VARCHAR(100), " +
            "Date VARCHAR(10)," +
            "Reason VARCHAR(200), " +
            "nif VARCHAR(12), FOREIGN KEY (nif) REFERENCES User(NIF) )";

    public String SQLInsert_Root = "INSERT INTO User (NIF, Password, Name, Email, Phone, Address, createdAt) VALUES ('00000000X', '1234', 'Root', 'root@udl.cat' ,'666666666', 'C/ Root 1234', '13/11/2017')";


    public PredigAppDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLCreate_User); //Execute the sentence to create de table's Database
        db.execSQL(SQLCreate_Role); //Execute the sentence to create de role's Database
        db.execSQL(SQLCREATE_BloodPressure); //Execute the sentence to create de BloodPressure's Database
        db.execSQL(SQLCreate_VisitsDoctor); //Execute the sentence to create de VisitDoctor's Database
        db.execSQL(SQLInsert_Root); //Insert the user to test
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");//Delete the last version of database
        db.execSQL("DROP TABLE IF EXISTS Role");//Delete the last version of database
        db.execSQL("DROP TABLE IF EXISTS BloodPressure");//Delete the last version of database
        db.execSQL("DROP TABLE IF EXISTS VisitsDoctor");//Delete the last version of database
        db.execSQL(SQLCreate_User); //Create the new version
        db.execSQL(SQLCreate_Role); //Create the new version
        db.execSQL(SQLCREATE_BloodPressure); //Create the new version
        db.execSQL(SQLCreate_VisitsDoctor); //Create the new version
    }
}
