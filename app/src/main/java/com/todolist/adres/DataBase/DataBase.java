package com.todolist.adres.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.todolist.adres.Models.KisiModel;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {
    private static final String Data_Base = "data_base";
    private static final String TABLO_ISMI = "tablo";
    private static final int VERSIYON = 10;

    //kolon isimleri
    private static final String ID = "_id";
    private static final String AD = "ad";
    private static final String TELEFON = "telefon";
    private static final String MAIL = "mail";
    private static final String ADRES = "adres";
    private static final String PROFIL = "profil";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLO_ISMI +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AD + " TEXT NOT NULL, " +
                TELEFON + " TEXT, " +
                MAIL + " TEXT, " +
                ADRES + " TEXT, " +
                PROFIL + " BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLO_ISMI);
        onCreate(db);

    }
    public DataBase(@Nullable Context context) {

        super(context,Data_Base, null,VERSIYON);
    }

    public long kaydet(KisiModel model) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(AD, model.getAd());
        cv.put(TELEFON, model.getTelefon());
        cv.put(MAIL, model.getMail());
        cv.put(ADRES, model.getAdres());

        if (model.getProfil() != null) {
            cv.put(PROFIL, model.getProfil());
        }

        long id = db.insert(TABLO_ISMI, null, cv);
        db.close();

        return id;
    }
    public void guncelle(long id, KisiModel model){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(AD, model.getAd());
        cv.put(TELEFON, model.getTelefon());
        cv.put(MAIL, model.getMail());
        cv.put(ADRES, model.getAdres());

        if (model.getProfil() != null) {
            cv.put(PROFIL, model.getProfil());
        }

        db.update(TABLO_ISMI, cv, ID + "=" + id, null);

        db.close();

    }

    public List<KisiModel> getTumKisiler() {
        List<KisiModel> modelList = new ArrayList<KisiModel>();
        SQLiteDatabase db = getReadableDatabase();
        String [] sutunlar = new String[]{AD,ADRES,TELEFON,MAIL,ID,PROFIL};
        Cursor c = db.query(TABLO_ISMI,sutunlar,null,null,null,null,null);
        int AdSiraNo = c.getColumnIndex(AD);
        int AdresSiraNo = c.getColumnIndex(ADRES);
        int TelefonSiraNo = c.getColumnIndex(TELEFON);
        int IdSiraNo = c.getColumnIndex(ID);
        int MaiSiraNo = c.getColumnIndex(MAIL);
        int ProfilSiraNo = c.getColumnIndex(PROFIL);
        if (c.moveToFirst()){
            do {
                KisiModel model =new KisiModel();

                model.setAd(c.getString(AdSiraNo));
                model.setAdres(c.getString(AdresSiraNo));
                model.setTelefon(c.getString(TelefonSiraNo));
                model.setMail(c.getString(MaiSiraNo));
                model.setId(c.getLong(IdSiraNo));

                if (c.getBlob(ProfilSiraNo)!=null)
                    model.setProfil(c.getBlob(ProfilSiraNo));

                modelList.add(model);

            }while (c.moveToNext());

        }else {
            modelList = null;

        }
        db.close();
        return modelList;
    }

    public KisiModel getKisi(long id) {
        KisiModel model =  new KisiModel();
        SQLiteDatabase db = getReadableDatabase();
        String [] sutunlar = new String[]{AD,ADRES,TELEFON,MAIL,ID,PROFIL};
        Cursor c = db.query(TABLO_ISMI,sutunlar,ID+" = ? ",new String[]{String.valueOf(id)},null,null,null);
        //c.moveToNext();
        c.moveToFirst();  //buraya dikkat...
        int AdSiraNo = c.getColumnIndex(AD);
        int AdresSiraNo = c.getColumnIndex(ADRES);
        int TelefonSiraNo = c.getColumnIndex(TELEFON);
        int IdSiraNo = c.getColumnIndex(ID);
        int MaiSiraNo = c.getColumnIndex(MAIL);
        int ProfilSiraNo = c.getColumnIndex(PROFIL);

        model.setAd(c.getString(AdSiraNo));
        model.setAdres(c.getString(AdresSiraNo));
        model.setTelefon(c.getString(TelefonSiraNo));
        model.setMail(c.getString(MaiSiraNo));
        model.setId(c.getLong(IdSiraNo));
        if (c.getBlob(ProfilSiraNo)!=null)
            model.setProfil(c.getBlob(ProfilSiraNo));
        db.close();

        return  model;
    }

    public void sil(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLO_ISMI,ID+" = ? ",new String[]{String.valueOf(id)});
        db.close();
    }
}
