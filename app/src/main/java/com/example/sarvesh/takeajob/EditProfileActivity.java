package com.example.sarvesh.takeajob;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;

public class EditProfileActivity extends AppCompatActivity {

    private final String LINK="http://192.168.43.187/takeajob/updateProfile.php";
    Bitmap myBitmap;
    Uri picUri,docUri;
    public String saveLocation;
    EditText et1,et2,et3,et4,et5,et6,et7;
    TextView tv;
    Button browse,upload,save;
    String name,age,contact,designation,company_name,experience,current_salary;
    ProgressDialog progress;

    AwesomeValidation awesomeValidation;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    CircleImageView circleImageView;

    private final static int ALL_PERMISSIONS_RESULT = 107;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setTitle("Edit Profile");
        progress=new ProgressDialog(this);
        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);

        circleImageView= (CircleImageView) findViewById(R.id.circleImage);
        et1= (EditText) findViewById(R.id.name);
        et2= (EditText) findViewById(R.id.age);
        et3= (EditText) findViewById(R.id.contact);
        et4= (EditText) findViewById(R.id.designation);
        et5= (EditText) findViewById(R.id.companyName);
        et6= (EditText) findViewById(R.id.experience);
        et7= (EditText) findViewById(R.id.currentSal);
        tv= (TextView) findViewById(R.id.path);

        loadImageFromStorage(getSharedPreferences("takeajobdata",MODE_PRIVATE).getString("pathtoimage",null));

        Intent i=getIntent();
        et1.setText(i.getStringExtra("name"));
        et2.setText(i.getStringExtra("age"));
        et3.setText(i.getStringExtra("designation"));
        et4.setText(i.getStringExtra("contact"));
        et5.setText(i.getStringExtra("c_name"));
        et6.setText(i.getStringExtra("experience"));

        awesomeValidation.addValidation(this,R.id.name,"(-?([A-Z].\\s)?([A-Z][a-z]+)\\s?)+([A-Z]'([A-Z][a-z]+))?",R.string.nameerror);
        awesomeValidation.addValidation(this,R.id.age,"^[0-9]{1,2}$",R.string.ageerror);
        awesomeValidation.addValidation(this,R.id.contact,"^[0-9]{10}$",R.string.contacterror);
        awesomeValidation.addValidation(this,R.id.designation,"(-?([A-Z][a-z].\\s)?([A-Z][a-z]+)\\s?)+([A-Z]'([A-Z][a-z]+))?",R.string.designationerror);
        awesomeValidation.addValidation(this,R.id.companyName,"(-?([A-Z][a-z].\\s)?([A-Z][a-z]+)\\s?)+([A-Z]'([A-Z][a-z]+))?",R.string.companyerror);
        awesomeValidation.addValidation(this,R.id.experience,"^[0-9]{1,2}$",R.string.experienceerror);
        awesomeValidation.addValidation(this,R.id.currentSal,"^[0-9]{1,10}$",R.string.salaryerror);

        browse= (Button) findViewById(R.id.browse);
        upload= (Button) findViewById(R.id.upload_resume);
        save= (Button) findViewById(R.id.save_changes);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(getPickImageChooserIntent(), 100);
                circleImageView.setImageBitmap(myBitmap);
            }
        });

        permissions.add(CAMERA);
        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(getPickFileChooserIntent(), 200);

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //yet to provide code to upload resume file to server.
                Toast.makeText(EditProfileActivity.this, "Feature not available yet", Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()) {

                    progress.setMessage("Please Wait!");
                    progress.show();

                    String username=getSharedPreferences("takeajobdata", MODE_PRIVATE).getString("username",null);
                    HashMap<String,String> map=new HashMap<>();

                    map.put("username", username);
                    name=et1.getText().toString().trim();
                    map.put("name",name);
                    age=et2.getText().toString().trim();
                    map.put("age",age);
                    contact=et3.getText().toString().trim();
                    map.put("contact",contact);
                    designation=et4.getText().toString().trim();
                    map.put("designation",designation);
                    company_name=et5.getText().toString().trim();
                    map.put("company_name",company_name);
                    experience=et6.getText().toString().trim();
                    map.put("experience",experience);
                    current_salary=et7.getText().toString().trim();
                    map.put("current_salary",current_salary);

                    RequestPackage rp=new RequestPackage();
                    rp.setMethod("POST");
                    rp.setUri(LINK);
                    rp.setParams(map);
                    if (isOnline()) {
                        EditProfileActivity.MyTask task = new EditProfileActivity.MyTask();
                        task.execute(rp);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please check your Internet Connectivity", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private boolean isOnline(){
        ConnectivityManager cm= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info.isConnectedOrConnecting();
    }
    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            circleImageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    /**
     * Create a chooser intent to select the source to get image from.

     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).

     * All possible sources are added to the intent chooser.
     */
    public Intent getPickFileChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList();
        PackageManager packageManager = getPackageManager();

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("application/*");

        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam =packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        Log.d("DIRECTORY",getImage.toString());
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //FileOutputStream outputStream=null;
        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == 100){
            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                    myBitmap = rotateImageIfRequired(myBitmap, picUri);
                    myBitmap = getResizedBitmap(myBitmap, 500);
                    /*
                    String extr = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                    File myPath = new File(extr, "TakeAJob");
                    if (!myPath.exists()) {
                        if (!myPath.mkdirs()) {
                            Log.d("onActivityResult", "Oops! Failed create "
                                    + "TakeAJob" + " directory");
                        }
                    }
                    else {
                        FileOutputStream fos;
                        try {
                            fos = new FileOutputStream(myPath);
                            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            fos.flush();
                            fos.close();
                            MediaStore.Images.Media.insertImage(getContentResolver(),
                                    myBitmap, myPath.getPath(), "profileImg.png");
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            Log.d("FileSave", "Oops! Failed save "
                                    + "TakeAJob" + " directory");
                        }
                    }*/

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {

                bitmap = (Bitmap) data.getExtras().get("data");
                myBitmap = bitmap;

            }
        }
        else if(resultCode == 200){
                docUri = getPickImageResultUri(data);
                tv.setText(docUri.toString());

            }

        }

    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    /**
     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.

     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }

        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        picUri = savedInstanceState.getParcelable("pic_uri");
    }

    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (hasPermission((String) perms)) {

                    } else {

                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                //Log.d("API123", "permisionrejected " + permissionsRejected.size());

                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }
    private class MyTask extends AsyncTask<RequestPackage,Void,String> {

        @Override
        protected String doInBackground(RequestPackage[] params) {
            return ServerCon.uploadData(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
        /*Intent to HomePage*/
        progress.dismiss();
            if (s!=null) {
                if (s.contains("successfull")) {
                    Toast.makeText(EditProfileActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();

                    saveLocation=saveToInternalStorage(myBitmap);
                    getSharedPreferences("takeajobdata", MODE_PRIVATE).edit().putString("pathtoimage", saveLocation).apply();

                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(EditProfileActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(EditProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
