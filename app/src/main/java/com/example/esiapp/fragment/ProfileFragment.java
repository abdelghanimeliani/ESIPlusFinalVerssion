package com.example.esiapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.esiapp.Info;
import com.example.esiapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;


public class ProfileFragment extends Fragment {


    private static final int RESULT_OK = 0;
    public static final String SHARED_PREFS = "info";
    private static final String TAG = "ProfileFragment";
    private Uri pickedImgUri = null;
    private int  numgender , numgrad , numgroupe ;
    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    DatabaseReference reference;
    StorageReference storageRef;
    //view
    private ImageView profile_picture, edit_profile_picture, modifierlecompte, modifierlesinfos;
    TextView email, full_name;
    private Spinner Genderspinner, Gradespinner, Groupspinner;
    private EditText password_edit;
    private ImageView saveinfodata, savecomptedata, saveimage;

    String semail, spass_word, sfullname, picture, sgrad, sgroupe, sgender;


    private static final int TAKE_IMAGE_CODE = 1001;

    public ProfileFragment()
    {
    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // innitialisation de vues
        Genderspinner = view.findViewById(R.id.gender_spinner);
        Gradespinner = view.findViewById(R.id.grade_spinner);
        Groupspinner = view.findViewById(R.id.groupe_spinner);

        profile_picture = view.findViewById(R.id.profile_picture);
        edit_profile_picture = view.findViewById(R.id.edit_profile_picture);
        email = view.findViewById(R.id.profile_email_editor);
        full_name = view.findViewById(R.id.profile_fullname);
        password_edit = view.findViewById(R.id.profile_password);

        modifierlecompte = view.findViewById(R.id.edit_account);
        modifierlesinfos = view.findViewById(R.id.edit_information);

        saveinfodata = view.findViewById(R.id.saveinfodata);
        savecomptedata = view.findViewById(R.id.savecomptedata);
        saveimage = view.findViewById(R.id.saveimage);

        loadData();

        /* hna 3andek tjib l'immage hna matebdach tetfelsef 9olna djib l'immage berk*/



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//--------------------------------------------------------------------------------------------------------------


        password_edit.setEnabled(false);
        Gradespinner.setEnabled(false);
        Groupspinner.setEnabled(false);
        Gradespinner.setEnabled(false);
        savecomptedata.setVisibility(View.INVISIBLE);
        saveinfodata.setVisibility(View.INVISIBLE);
        saveimage.setVisibility(View.INVISIBLE);
        saveinfodata.setEnabled(false);
        savecomptedata.setEnabled(false);
        saveimage.setEnabled(false);

//----------------------------------------------------------------------------------------------------------------


        ArrayAdapter<CharSequence> genderadapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender, android.R.layout.simple_spinner_item);
        genderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Groupspinner.setAdapter(genderadapter);

        ArrayAdapter<CharSequence> gradadapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.grade, android.R.layout.simple_spinner_item);
        gradadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Gradespinner.setAdapter(genderadapter);

        ArrayAdapter<CharSequence> groupeadapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.groupe, android.R.layout.simple_spinner_item);
        groupeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Groupspinner.setAdapter(groupeadapter);

//-------------------------------------------------------------------------------------------------------------------


        Genderspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                sgender = parent.getItemAtPosition(position).toString();
                numgender= position ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        Gradespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                sgrad = parent.getItemAtPosition(position).toString();
                numgrad = position ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        Groupspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sgroupe = parent.getItemAtPosition(position).toString();
                numgroupe = position ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//-----------------------------------------------------------------------------------------------------------------


        modifierlesinfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //clicksound();
                Gradespinner.setEnabled(true);
                Groupspinner.setEnabled(true);
                Gradespinner.setEnabled(true);
                saveinfodata.setVisibility(View.VISIBLE);
                modifierlesinfos.setEnabled(false);
                saveinfodata.setEnabled(true);
            }
        });
        //-------------------------------------------------------------------------------------------------------------------

        modifierlecompte.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
               // clicksound();
                password_edit.setEnabled(true);
                savecomptedata.setVisibility(View.VISIBLE);
                savecomptedata.setEnabled(true);
                modifierlecompte.setEnabled(false);
                semail=email.getText().toString();
                spass_word=password_edit.getText().toString();
            }
        });

        //----------------------------------------------------------------------------------------------------------------
        edit_profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //clicksound();
                saveimage.setVisibility(View.VISIBLE);
                saveimage.setEnabled(true);
                openGallery();
                edit_profile_picture.setEnabled(false);
                edit_profile_picture.setVisibility(View.INVISIBLE);
            }
        });
//----------------------------------------------------------------------------------------------------------------------
        savecomptedata.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               // clicksound();
            }
        });

//----------------------------------------------------------------------------------------------------------------


        saveinfodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // clicksound();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                reference = database.getReference("info").push();
                Info info = new Info(sgender, sgrad, sgroupe);
                reference.setValue(info);
                saveData();
                reference.setValue(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Information Saved");
                        savecomptedata.setVisibility(View.INVISIBLE);
                        modifierlesinfos.setVisibility(View.VISIBLE);
                        modifierlesinfos.setEnabled(true);

                    }
                });
            }
        });
//_______________**************___________------__________________**************____________________*************_____________________


        saveimage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
               // clicksound();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profil_images");
                final StorageReference imageFilePath = storageReference.child(Objects.requireNonNull(pickedImgUri.getLastPathSegment()));
                imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        edit_profile_picture.setVisibility(View.VISIBLE);
                        edit_profile_picture.setEnabled(true);
                        saveimage.setVisibility(View.INVISIBLE);
                        saveimage.setEnabled(false);

                        imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri uri) {
                                //hna mafhamtekch wech 9otli dir amiiiine dirha
                                // nta w rani dayerlek el fou9
                                // win tcharger l'immage
                            }
                        });
                    }
                });
            }
        });
    }


    public void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, TAKE_IMAGE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == TAKE_IMAGE_CODE && data != null) {
            pickedImgUri = data.getData();
            profile_picture.setImageURI(pickedImgUri);

        }
    }

   /* public void clicksound ()
    {
        MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.sonduclick);
        mediaPlayer.start();
    }*/
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
    public void saveData()
    {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor = sharedPreferences.edit();
        editor.putInt("grad", numgrad);
        editor.putInt("gender", numgender);
        editor.putInt("groupe", numgroupe);
        editor.putString("email",semail);

        editor.apply();
    }

    public void loadData()
    {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        int legenre , legrad,legroupe ;
        String leemail ;
        legenre = sharedPreferences.getInt("gender",0);
        legrad = sharedPreferences.getInt("grad",0);
        legroupe = sharedPreferences.getInt("groupe",0);
        leemail = sharedPreferences.getString("email","");

        Gradespinner.setSelection(legrad);
        Genderspinner.setSelection(legenre);
        Groupspinner.setSelection(legroupe);
        email.setText(leemail);

    }

}
