package com.example.metroapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.example.metroapp.BookingFragment.bitmap;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyTicketFragment extends Fragment {


    private TextView name;
    private TextView mailTicket;
    private TextView phone;
    private TextView logout;
    private View view;
    public static ImageView qr;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private User user;
    private StorageReference storageReference;
    private ProgressBar progressBar;



    public MyTicketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_ticket, container, false);
        initView();
        try {
            getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private void initView() {
        name = view.findViewById(R.id.name);
        mailTicket = view.findViewById(R.id.mail_ticket);
        phone = view.findViewById(R.id.phone);
        logout = view.findViewById(R.id.logout);
        progressBar=view.findViewById(R.id.progress);
        firebaseAuth=FirebaseAuth.getInstance();
        storageReference=FirebaseStorage.getInstance().getReference("images/")
               .child(firebaseAuth.getCurrentUser().getUid());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().onBackPressed();
            }
        });
        qr = view.findViewById(R.id.qr);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("users")
                .child(firebaseAuth.getCurrentUser().getUid());

    }
    private void getData() throws IOException {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user=dataSnapshot.getValue(User.class);
                name.setText(user.getName());
                mailTicket.setText(user.getMail());
                phone.setText(user.getPhone());
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        final File file=File.createTempFile("image","jpg");
        storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmapp= BitmapFactory.decodeFile(file.getAbsolutePath());
                qr.setImageBitmap(bitmapp);
            }
        });
    }


}
