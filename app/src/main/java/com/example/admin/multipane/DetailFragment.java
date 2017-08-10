package com.example.admin.multipane;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static final String TAG = "Detail";
    private static int RESULT_LOAD_IMAGE = 1;
    private String mParam1;
    private String mParam2;
    private EditText etName,etNationality;
    private Spinner spinnerGender;
    private Button btnSave,btnDel;
    private ImageButton ibImage;
    private OnFragmentInteractionListener mListener;
    Bitmap bitmap;
    String id ;
    DataBase dataBase;

    // TODO: Rename and change types and number of parameters

    public DetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etName = (EditText) view.findViewById(R.id.etName);
        etNationality = (EditText) view.findViewById(R.id.etNationality);
        spinnerGender = (Spinner) view.findViewById(R.id.spinnerGender);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnDel = (Button) view.findViewById(R.id.btnDel);
        ibImage = (ImageButton) view.findViewById(R.id.ibImage);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.genres, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerGender.setAdapter(adapter);
        dataBase = new DataBase(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        id = mParam1;
        Log.d(TAG, "onActivityCreated: "+ id);
        if(id != "-1"){
            //try {

                ArrayList<Artist> listArtists = dataBase.getArtists(id);
                if (listArtists.size() > 0) {
                    etName.setText(listArtists.get(0).getName());
                    etNationality.setText(listArtists.get(0).getNationality());
                    byte[] b = listArtists.get(0).getBitmap();
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    ibImage.setImageBitmap(bitmap);
                    Log.d(TAG, "onCreate: " + id+ listArtists.get(0).getGender());
                }

//            }catch(Exception ex){
//                Toast.makeText(getActivity(),"" + ex, Toast.LENGTH_SHORT).show();
//            }
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etName.getText().toString().equals("") || etNationality.getText().toString().equals("") || spinnerGender.getSelectedItem().toString().equals("")){
                    Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else {
                    byte[] b = null;
                    if (bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                        b = stream.toByteArray();
                    }
                    Artist artist = new Artist(-1, etName.getText().toString(), etNationality.getText().toString(),
                            spinnerGender.getSelectedItem().toString(), b);
                    Log.d(TAG, "onClick: " + artist.getName() + " " + artist.getNationality()+ " " + artist.getGender() +
                            " " + artist.getBitmap() );
                    //try {
                        if (Integer.parseInt(id) >= 0) {
                            dataBase.uploadNewArtist(artist, id);
                            Toast.makeText(getActivity(), "Element updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "onClick: "+ artist);
                            dataBase.saveNewArtist(artist);
                            Toast.makeText(getActivity(), "Element created successfully", Toast.LENGTH_SHORT).show();
                        }
                        mListener.onFragmentInteraction(-1);
//                    } catch (Exception ex) {
//                        dataBase.saveNewArtist(artist);
//                        Toast.makeText(getActivity(), "Element created successfully", Toast.LENGTH_SHORT).show();
//                    }
                }

            }
        });
        ibImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getPic = new Intent(Intent.ACTION_GET_CONTENT);
                getPic.setType("image/*");
                startActivityForResult(getPic.createChooser(getPic, "Select Picture"), RESULT_LOAD_IMAGE);
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Integer.parseInt(id) >= 0) {
                        dataBase.DeleteContact(id);
                        bitmap = null;
                        etName.setText("");
                        etNationality.setText("");
                        spinnerGender.setSelection(1);
                        ibImage.setImageResource(R.drawable.user);
                        Toast.makeText(getActivity(), "Element deleted successfully", Toast.LENGTH_SHORT).show();
                        mListener.onFragmentInteraction(-1);
                    }
                }catch(Exception ex){}

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                ImageView imageView =  getActivity().findViewById(R.id.ibImage);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int inter);
    }
}
