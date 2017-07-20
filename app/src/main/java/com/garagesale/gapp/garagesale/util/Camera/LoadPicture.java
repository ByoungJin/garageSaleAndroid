package com.garagesale.gapp.garagesale.util.Camera;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import android.content.Context;

import com.garagesale.gapp.garagesale.util.setPermission;
import com.gun0912.tedpermission.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.garagesale.gapp.garagesale.R.anim.rotate;
import static com.garagesale.gapp.garagesale.R.anim.scale;
import static com.garagesale.gapp.garagesale.util.Camera.RealPathUtil.getRealPath;

/**
 * Created by juyeol on 2017-07-05.
 * 이함수는 실질적인 갤러리, 카메라에 대한 접근로직이 포함됨.
 * 권한획득 로직도 포함해서 좀 더러움
 */

public class LoadPicture {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_GALLERY = 2;

    private Intent intent;
    private Fragment fragment;
    private Context context;
    private Uri outputFileUri;

    public LoadPicture(Fragment fragment, Context context){
        this.fragment = fragment;
        this.context = context;
    }

    /**
     * 갤러리 실행 (+권한)
     */
    public void onGallery(){
        new setPermission(context,  new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                fragment.startActivityForResult(intent, REQUEST_GALLERY);
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }
        },Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 카메라 실행 (+권한)
     *
     * @return 찍은 사진에 대한 Uri 을 던져줌
     */
    public Uri onCamera() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == fragment.getActivity().getPackageManager().PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == fragment.getActivity().getPackageManager().PERMISSION_GRANTED) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
                outputFileUri = ProviderUtil.getOutputMediaFileUri(fragment.getActivity().getBaseContext());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                fragment.startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        }
        else{
            new setPermission(context, new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                }
                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                }
            }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        return outputFileUri;
    }

    // CROP 이미지 설정값들 (비율, 크기, 파일형식)
    private static final String TYPE_IMAGE = "image/*";
    private static final int PROFILE_IMAGE_ASPECT_X = 1;
    private static final int PROFILE_IMAGE_ASPECT_Y = 1;
    private static final int PROFILE_IMAGE_OUTPUT_X = 400;
    private static final int PROFILE_IMAGE_OUTPUT_Y = 400;
    private static final int REQUEST_CODE_PROFILE_IMAGE_CROP = 3;

    public void doCrop(Uri outputFileUri) {
        intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("scale", scale);
        intent.putExtra("outputX", PROFILE_IMAGE_OUTPUT_X);
        intent.putExtra("outputY", PROFILE_IMAGE_OUTPUT_Y);
        intent.putExtra("aspectX", PROFILE_IMAGE_ASPECT_X);
        intent.putExtra("aspectY", PROFILE_IMAGE_ASPECT_Y);
        intent.setDataAndType(outputFileUri, TYPE_IMAGE);
        intent.putExtra("crop", "true");
        intent.putExtra("circleCrop", true);
        fragment.startActivityForResult(intent, REQUEST_CODE_PROFILE_IMAGE_CROP);
    }




    /**
     * uri 를 비트맵 이미지로 변환
     *
     * @param uri
     * 이미지 경로 uri
     *
     * @return Bitmap
     * bitmap 이미지 반환
     */
    public Bitmap drawFile(Uri uri) {
        Bitmap bitmapImage;
        try {
            bitmapImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "IOException:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
        return bitmapImage;
    }

    /**
     *
     * uri를 기반으로 서버와 통신하기위해
     * 이미지를 포함한 MultipartBody로 가공
     *
     * @param uri
     * 이미지 경로 uri
     *
     * @return MultipartBody
     * 멀티파트 바디 반환
     */
    public MultipartBody.Part createBody(Uri uri) {
        //Log.d("dsa",String.valueOf(uri.getPath();

        File file = new File(getRealPathFromURI(uri));
        RequestBody requestFile = RequestBody.create(
                MediaType.parse(fragment.getActivity().getContentResolver().getType(uri)), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("profile", file.getName(), requestFile);

        return body;
    }

    public String getRealPathFromURI(Uri contentUri)
    {
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e)
        {
            return contentUri.getPath();
        }
    }


}
