package com.todolist.adres.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.todolist.adres.Fragmentler.KisiDetayFragment;
import com.todolist.adres.Fragmentler.KisiEkleDuzenleFragment;
import com.todolist.adres.Fragmentler.KisiListesiFragment;
import com.todolist.adres.R;

public class MainActivity extends AppCompatActivity implements KisiListesiFragment.KisiListesiFragmentDinleyicisi,
        KisiDetayFragment.KisiDetayFragmentDinleyicisi, KisiEkleDuzenleFragment.KisiEkleDuzenleFragmentDinleyicisi {

    private KisiListesiFragment kisiListesiFragment;
    public static final String ID = "_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.AppTheme);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String s=sharedPreferences.getString("tema","0");
        int tema=Integer.parseInt(s);
        switch (tema){
            case 0:setTheme(R.style.AppTheme); break;
            case 1:setTheme(R.style.AppThemeMavi); break;
            case 2:setTheme(R.style.AppThemeYesil); break;
            case 3:setTheme(R.style.AppThemePink); break;
            case 4:setTheme(R.style.AppThemeKahverengi); break;
            case 5:setTheme(R.style.AppThemeGri); break;

        }

        setContentView(R.layout.activity_main);
        ///Yapılandırma değişikliğinde tekrar fragment oluşturmamak için
        if (savedInstanceState!= null)
            return;
        if (findViewById(R.id.container)!= null){
            //TELEFON
            kisiListesiFragment = new KisiListesiFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container,kisiListesiFragment);
            transaction.commit();
        }
    }

    @Override
    public void elemanSecildi(Long id) {
        if (findViewById(R.id.container)!= null) {
            //TELEFON
            detayFragmentiniAc(id,R.id.container);
        }else {
            //Tablet ise
            getSupportFragmentManager().popBackStack();
            detayFragmentiniAc(id,R.id.sagPanelcontainer);
        }


    }

    private void detayFragmentiniAc(Long id, int containerId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ID,id);
        KisiDetayFragment kisiDetayFragment = new KisiDetayFragment();
        kisiDetayFragment.setArguments(arguments);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId,kisiDetayFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void kisiEklermisinKardes() {
        if (findViewById(R.id.container) != null) {
            //Telefon ise
            kisiEkleDuzenleFragmentiniAc(R.id.container,null);
        } else {
            //Tablet ise
            kisiEkleDuzenleFragmentiniAc(R.id.sagPanelcontainer,null);
        }


    }
    private void kisiEkleDuzenleFragmentiniAc(int view_id, Bundle arguments) {

        KisiEkleDuzenleFragment kisiEkleDuzenle=new KisiEkleDuzenleFragment();

        if (arguments!=null)
            kisiEkleDuzenle.setArguments(arguments);

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(view_id,kisiEkleDuzenle);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    @Override
    public void kisiDuzenle(Bundle arguments) {
        if (findViewById(R.id.container) != null) {
            //Telefon
            kisiEkleDuzenleFragmentiniAc(R.id.container,arguments);
        } else {
            //Tablet
            kisiEkleDuzenleFragmentiniAc(R.id.sagPanelcontainer,arguments);
        }
    }
    @Override
    public void kisiSilindi() {
        getSupportFragmentManager().popBackStack();
        if (findViewById(R.id.container) == null){
            //tablet
            kisiListesiFragment.guncelle();
        }

    }

    @Override
    public void kisiEkleDuzenleIslemiYapildi(Long id) {
        getSupportFragmentManager().popBackStack(); //ekle veya duzenle fragmentini sil
        if (findViewById(R.id.container) == null){
            //tablet ise
            getSupportFragmentManager().popBackStack(); //varsa detay fragmenti onuda sil
            detayFragmentiniAc(id,R.id.sagPanelcontainer);
            kisiListesiFragment.guncelle();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (kisiListesiFragment == null){
            //Tablet ise
            kisiListesiFragment = (KisiListesiFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.kisiListesiFragment);
        }
    }
}
