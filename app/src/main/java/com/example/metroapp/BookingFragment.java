package com.example.metroapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFragment extends Fragment {
    protected Spinner From;
    protected TextView pay;
    protected Spinner To;
    protected  Map<String, Integer> stations1 ;
    protected  Map<String, Integer> stations2 ;
    protected  Map<String, Integer> stations3 ;
    public static Bitmap bitmap ;
    private String ClickedCountryNameFrom;
    private String ClickedCountryNameTo;
    private ArrayList<String> mCountryList;
    private StationAdapter mAdapter;
    private TextView textView;
    private TextView cost;
    public  String code=null;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference1;
    private ProgressBar progressBar;
    Resources res;
    String data[];
    View view;

    public BookingFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_booking, container, false);
        initStations();
        return view;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        From.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ClickedCountryNameFrom = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        To.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ClickedCountryNameTo=(String) parent.getItemAtPosition(position);
                textView.setText(""+CalculateStations());
                cost.setText(""+cost()+" LE");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private int cost() {
       if (CalculateStations()>16)
         return 7;
       else if (CalculateStations()>8)
           return 5;
       else if (CalculateStations()<=8&CalculateStations()>0)
           return 3;
       else return 0;
    }
    private void initStations() {
        From = view.findViewById(R.id.From);
        To = view.findViewById(R.id.To);
        mCountryList=new ArrayList<>();
        res=getResources();
        progressBar=view.findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Codes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String s = dataSnapshot1.getKey();
                    if (s.equals(firebaseAuth.getCurrentUser().getUid())){
                        code=s;
                        break;
                    }
                    else
                    code=null;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        firebaseAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference("images");
        databaseReference1=firebaseDatabase.getReference("Images")
                .child(firebaseAuth.getCurrentUser().getUid());
        data=res.getStringArray(R.array.first_line);
        mCountryList.add("\t"+"\t"+"\t"+"----  First Line  ----");
        for (String x:data){
            mCountryList.add(x);
        }
        data=res.getStringArray(R.array.second_line);
        mCountryList.add("\t"+"\t"+"\t"+"----  Second Line  ----");

        for (String x:data){
            mCountryList.add(x);
        }
        data=res.getStringArray(R.array.Third_line);
        mCountryList.add("\t"+"\t"+"\t"+"----  Third Line  ----");

        for (String x:data){
            mCountryList.add(x);
        }
        mAdapter = new StationAdapter(getContext(), mCountryList);
        From.setAdapter(mAdapter);
        To.setAdapter(mAdapter);
        textView=view.findViewById(R.id.no_stations);
        cost=view.findViewById(R.id.cost);
        pay=view.findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    progressBar.setVisibility(View.VISIBLE);
                if (CalculateStations()==0){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "Please choose correct stations", Toast.LENGTH_SHORT).show(); }
                else if (code==null && CalculateStations()!=0){
                    code=firebaseAuth.getCurrentUser().getUid();
                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(code);
                    Generate_QR_Code();
                }
                else if(code != null)
                {
                    Toast.makeText(getContext()," you paied before ." , Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else return;

            }
        });
        stations1=new HashMap<>();
        stations2=new HashMap<>();
        stations3=new HashMap<>();
        String[] x=getResources().getStringArray(R.array.first_line);
        for(int i=0;i<x.length;i++){
            stations1.put(x[i],i+1);
        }
        String[] y=getResources().getStringArray(R.array.second_line);
        for(int i=0;i<y.length;i++){
            stations2.put(y[i],i+36);
        }
        String[] z=getResources().getStringArray(R.array.Third_line);
        for(int i=0;i<z.length;i++){
            stations3.put(z[i],i+56);
        }
    }
    private int CalculateStations() {
        int start = 0;
        int end = 0;
        int result=0;
        if(stations1.containsKey(ClickedCountryNameFrom))
            start=stations1.get(ClickedCountryNameFrom);
        else if (stations2.containsKey(ClickedCountryNameFrom))
            start=stations2.get(ClickedCountryNameFrom);
        else if (stations3.containsKey(ClickedCountryNameFrom))
            start=stations3.get(ClickedCountryNameFrom);
        if(stations1.containsKey(ClickedCountryNameTo))
            end=stations1.get(ClickedCountryNameTo);
        else if (stations2.containsKey(ClickedCountryNameTo))
            end=stations2.get(ClickedCountryNameTo);
        else if (stations3.containsKey(ClickedCountryNameTo))
            end=stations3.get(ClickedCountryNameTo);
        if(start==0||end==0)
            result=0;
        //from line 1 to line 3 from el marg
       else if (start<=14&end>55){
           result=Math.abs(stations1.get(ClickedCountryNameFrom)-stations1.get("El Shohadaa"));
           result=result+Math.abs(stations2.get("Atbaa")-stations2.get("El Shohadaa"));
           result=result+Math.abs(stations3.get(ClickedCountryNameTo)-stations3.get("Ataba"));
       }
       //from line 1 to line 3 from helwan
       else if(start<=35&end>55){
            result=Math.abs(stations1.get(ClickedCountryNameFrom)-stations1.get("Saddat"));
            result=result+Math.abs(stations2.get("Atbaa")-stations2.get("Saddat"));
            result=result+Math.abs(stations3.get(ClickedCountryNameTo)-stations3.get("Ataba"));
        }
        // from line 2 to line 3
        else if(start>35&start<=55&end>55){
            result=Math.abs(stations2.get(ClickedCountryNameFrom)-stations1.get("Atbaa"));
            result=result+Math.abs(stations3.get("Ataba")-stations3.get(ClickedCountryNameTo));
        }
       //from line 1 to line 2 from el marg
       else if (start<=14&end>35&end<=55){
           result=Math.abs(stations1.get(ClickedCountryNameFrom)-stations1.get("El Shohadaa"));
           result=result+Math.abs(stations2.get("El Shohadaa")-stations2.get(ClickedCountryNameTo)); }
        //from line 1 to line 2 from helwan
        else if (start<=35&end>35&end<=55){
            result=Math.abs(stations1.get(ClickedCountryNameFrom)-stations1.get("Saddat"));
            result=result+Math.abs(stations2.get("Saddat")-stations2.get(ClickedCountryNameTo)); }
        //from line 3 to line 2
        else if(start>55&end>35&end<=55){
            result=Math.abs(stations3.get(ClickedCountryNameFrom)-stations3.get("Ataba"));
            result=result+Math.abs(stations2.get(ClickedCountryNameTo)-stations2.get("Atbaa")); }
        //from line 3 to line 1
        else if(start>55&end<=35){
            result=Math.abs(stations3.get(ClickedCountryNameFrom)-stations3.get("Ataba"));
            result=result+Math.abs(stations2.get("Atbaa")-stations2.get("Saddat"));
            result=result+Math.abs(stations1.get(ClickedCountryNameTo)-stations1.get("Saddat")); }
       //from line 2 to 1 to el marg
       else if (start>35&start<=55&end<=14){
           result=Math.abs(stations2.get(ClickedCountryNameFrom)-stations2.get("El Shohadaa"));
           result=result+Math.abs(stations1.get("El Shohadaa")-stations1.get(ClickedCountryNameTo));}
        //from line 2 to 1 to helwan
        else if (start>35&start<=55&end<=35&end>14){
            result=Math.abs(stations2.get(ClickedCountryNameFrom)-stations2.get("Saddat"));
            result=result+Math.abs(stations1.get("Saddat")-stations1.get(ClickedCountryNameTo));}
        else
            result=Math.abs(start-end);
        int a=result;
        return result;
    }
    private void Generate_QR_Code(){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(code, BarcodeFormat.QR_CODE,
                    500, 500);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            uploadImage(bitmap);
        }
        catch (WriterException e){
            e.printStackTrace();
        }
    }
    private void uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        final StorageReference imagesRef = storageReference.child(firebaseAuth.getCurrentUser().getUid());
        imagesRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Done!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    /* public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
            }
        });

        return builder;
    }*/
    class StationAdapter extends ArrayAdapter<String> {

        public StationAdapter(Context context, ArrayList<String> countryList) {
            super(context, 0, countryList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return initView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return initView(position, convertView, parent);
        }


        private View initView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.stations_spinner_row, parent, false);
            }
            TextView textViewname = convertView.findViewById(R.id.text_view_name);
            String currentItem = getItem(position);
            if(currentItem!= null) {
                textViewname.setText(currentItem);
            }
            return convertView;
        }
}




}
