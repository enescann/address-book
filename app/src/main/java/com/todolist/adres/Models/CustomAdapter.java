package com.todolist.adres.Models;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.todolist.adres.R;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private List<KisiModel> modelList;

    public CustomAdapter(Context context, List<KisiModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public int getCount() {
        if (modelList!= null)
        return modelList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (modelList!= null)
        return modelList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (modelList!= null)
        return modelList.get(position).getId();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (modelList == null)
        return null;
        LinearLayout container = (LinearLayout) ((Activity)context).getLayoutInflater().inflate(R.layout.custom_listview,null);
        ImageView profil = container.findViewById(R.id.profil_custom_list);
        TextView AdSoyad = container.findViewById(R.id.adCustomList);
        TextView Telefon = container.findViewById(R.id.telefonCustomList);
        TextView Mail = container.findViewById(R.id.mailCustomList);

        KisiModel model = modelList.get(position);
        AdSoyad.setText(model.getAd());
        Telefon.setText(model.getTelefon());
        Mail.setText(model.getMail());
        if (model.getProfil() != null) {
            byte [] bytes = model.getProfil();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            profil.setImageBitmap(bitmap);
        }
        return container;
    }
}
