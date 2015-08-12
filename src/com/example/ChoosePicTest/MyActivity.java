package com.example.ChoosePicTest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    private Button takePhoto;
    private ImageView picture;
    private Uri imageUri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        takePhoto = (Button) findViewById(R.id.take_photo);
        picture = (ImageView) findViewById(R.id.picture);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建File对象，用于存储拍照后的图片
                //getExernalStorageDirectory方法获取手机SD卡的根目录
                //tempIage.jpg图片名字
                File outputImage = new File(Environment.getExternalStorageDirectory(),"tempIage.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (Exception e){
                    e.printStackTrace();
                }
                //将File对象转换成Uri对象
                //Uri对象标识着图片的唯一地址
                imageUri = Uri.fromFile(outputImage);

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                //启动相机程序
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case TAKE_PHOTO:
            if (requestCode == RESULT_OK) {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(imageUri,"image/*");
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                //启动剪裁程序
                startActivityForResult(intent,CROP_PHOTO);
            }
            break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK){
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                       //将裁剪后的照片显示出来
                        picture.setImageBitmap(bitmap);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }
}
