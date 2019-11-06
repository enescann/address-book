package com.todolist.adres.Fragmentler;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.todolist.adres.Activity.MainActivity;
import com.todolist.adres.BuildConfig;
import com.todolist.adres.DataBase.DataBase;
import com.todolist.adres.Models.KisiModel;
import com.todolist.adres.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class KisiEkleDuzenleFragment extends Fragment {
    private EditText ad,telefon,mail,adres;
    private Button kaydet;
    private TextInputLayout adSoyadInputLayout;
    private ImageView imageView;
    private KisiEkleDuzenleFragmentDinleyicisi dinleyici;
    private Bundle arguments;
    private Long id;
    private Bitmap bitmap;
    final int CAMERA_CAPTURE = 1;
    final int PIC_CROP=2;
    private Uri picUri;
    public interface KisiEkleDuzenleFragmentDinleyicisi{
        //
        public void kisiEkleDuzenleIslemiYapildi(Long id);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        dinleyici = null;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dinleyici = (KisiEkleDuzenleFragmentDinleyicisi) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.ekle_duzenle_fragment,container,false);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ad = (EditText) getActivity().findViewById(R.id.adSoyadEt);
        mail = (EditText) getActivity().findViewById(R.id.mailEt);
        adres = (EditText) getActivity().findViewById(R.id.adresEt);
        telefon = (EditText) getActivity().findViewById(R.id.telefonEt);
        kaydet = (Button) getActivity().findViewById(R.id.kaydet);
        adSoyadInputLayout= (TextInputLayout) getActivity().findViewById(R.id.adSoyadTextInputLayout);
        imageView = (ImageView) getActivity().findViewById(R.id.profile_image);
        arguments = getArguments();

        if (arguments!= null){
            //kişi düzenlenecekse
            id=arguments.getLong(MainActivity.ID);
            ad.setText(arguments.getString("ad"));
            mail.setText(arguments.getString("mail"));
            telefon.setText(arguments.getString("telefon"));
            adres.setText(arguments.getString("adres"));

            if (arguments.getParcelable("resim")!=null){
                bitmap = arguments.getParcelable("resim");
                imageView.setImageBitmap(bitmap);
            }

        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               // String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
               // File imageFile = new File(imageFilePath);
                //picUri = Uri.fromFile(imageFile); // convert path to Uri
                //Uri picUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider",imageFile);
                //Uri picUri = FileProvider.getUriForFile(getContext().getApplicationContext(), getContext().getApplicationContext().getPackageName() + ".provider", imageFile);
                //intent.putExtra( MediaStore.EXTRA_OUTPUT, picUri );
                //startActivityForResult(intent,CAMERA_CAPTURE);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_CAPTURE);
            }
        });

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ad.getText().toString().trim().length()!=0){
                    kaydetVeyaGuncelle();
                    dinleyici.kisiEkleDuzenleIslemiYapildi(id);

                }else{
                    adSoyadInputLayout.setError("Lütfen bir ad giriniz...");
                    ad.requestFocus();
                }
            }
        });
    }

    private void kaydetVeyaGuncelle() {

        KisiModel model = new KisiModel(ad.getText().toString(),mail.getText().toString(),telefon.getText().toString(),adres.getText().toString());
        DataBase veritabani = new DataBase(getActivity());

        if (bitmap!= null){
            byte [] byteArray = getByteArray(bitmap);
            model.setProfil(byteArray);
        }

        if (arguments == null){
            //Yeni kayıt girilecek
            id = veritabani.kaydet(model);

        }else{
            //Güncelleme yapılacak
            veritabani.guncelle(id,model);
        }
    }

    private byte[] getByteArray(Bitmap bitmap) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,bos);
        return bos.toByteArray();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
/*        if (requestCode == CAMERA_CAPTURE && resultCode == getActivity().RESULT_OK) {
        Bundle extras = data.getExtras();
        bitmap = (Bitmap) extras.get("data");
        imageView.setImageBitmap(bitmap);
    }*/
        //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
       // imageView.setImageBitmap(bitmap);
       if (resultCode == getActivity().RESULT_OK){

            if (requestCode == CAMERA_CAPTURE){
                Bundle bundle=data.getExtras();
                bitmap=bundle.getParcelable("data");
                imageView.setImageBitmap(bitmap);


            }else if (requestCode == PIC_CROP){
                picUri = data.getData();
                //Uri uri = picUri;
                //Log.d("AmirHome", uri.toString());
                crop();
            }else {
                Log.w("HATA", "excecption");
            }
        }
    }
    private void crop() {
        try {

            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Telefonunuz fotoğraf kırpmayı desteklemiyor.";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
