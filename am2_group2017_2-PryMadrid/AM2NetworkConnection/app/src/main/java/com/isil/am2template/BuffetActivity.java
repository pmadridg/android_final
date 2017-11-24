package com.isil.am2template;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.isil.am2template.model.entity.Buffet;
import com.isil.am2template.model.entity.Cart;
import com.isil.am2template.model.entity.Place;
import com.isil.am2template.storage.PreferencesHelper;
import com.isil.am2template.storage.db.CRUDOperations;
import com.isil.am2template.storage.db.MyDatabase;
import com.isil.am2template.storage.request.ApiClient;
import com.isil.am2template.storage.request.StorageConstant;
import com.isil.am2template.storage.request.entity.BuffetResponse;
import com.isil.am2template.view.adapters.BuffetAdapter;
import com.isil.am2template.view.adapters.PlaceAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pablo Claus on 11/2/2017.
 */

public class BuffetActivity extends AppCompatActivity {


    private static final String TAG ="BuffetActivity" ;
    private static final int ACTION_CART=3;

    private ListView lstBuffet;
    private BuffetAdapter buffetAdapter;
    private List<Buffet> lsBuffetEntities;
    private TextView tviLogout,tviUser;
    private Button btnAddBuffet;
    private CRUDOperations crudOperations;
    private EditText eteQty;
    private TextView tviQty;
    private String qty = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffet);
        //populate();

        init();
        String token = PreferencesHelper.getTokenSession(this);
        retrieveBuffet(token);

        //loadData();
    }

    /*private void retrieveBuffet2(){
        Call<List<Buffet>> call= ApiClient.getMyApiClient().buffet();
        call.enqueue(new Callback<List<Buffet>>() {
            @Override
            public void onResponse(Call<List<Buffet>> call, Response<List<Buffet>> response) {
                if(response!=null){
                    List<Buffet> buffetsResponse=null;
                    if(response.isSuccessful()){
                        buffetsResponse= response.body();
                        lsBuffetEntities = buffetResponse;
                        if(buffetsResponse!=null){
                            renderBuffet(buffetsResponse);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Buffet>> call, Throwable t) {

            }
        });
    }*/
    private void showMessage(String message){
        Toast.makeText(this,
                "LogIn "+message,Toast.LENGTH_LONG).show();
    }

    public void retrieveBuffet(String token){


        Map<String, String> map = new HashMap<>();
        map.put("user-token",token);
        Call<BuffetResponse> call= ApiClient.getMyApiClient().showBuffet(
                StorageConstant.APPLICATIONID, StorageConstant.RESTAPIKEY,map);

        call.enqueue(new Callback<BuffetResponse>() {
            @Override
            public void onResponse(Call<BuffetResponse> call, Response<BuffetResponse> response) {

                if(response!=null){
                    BuffetResponse buffetResponse=null;
                    if(response.isSuccessful()){
                        buffetResponse= response.body();
                        renderBuffet(buffetResponse);
                        //error
                    }else{
                    }
                }
            }

            @Override
            public void onFailure(Call<BuffetResponse> call, Throwable t) {

                showMessage(t.getMessage());
            }
        });
    }

    private void init() {

        tviLogout= (TextView)findViewById(R.id.tviLogout);
        tviUser= (TextView)findViewById(R.id.tviUser);
        btnAddBuffet= (Button)(findViewById(R.id.btnAddBuffet));
        lstBuffet= (ListView)(findViewById(R.id.lstFood));
        //eteQty = (EditText) (findViewById(R.id.eteQty));

        lstBuffet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Buffet buffet = (Buffet) adapterView.getAdapter().getItem(i);

            }
        });
        crudOperations= new CRUDOperations(new MyDatabase(this));



        btnAddBuffet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gotoNote(ACTION_ADD, null);
                sendtoCart(ACTION_CART, null);
            }
        });


        tviLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });





    }

    private void sendtoCart(int action,View view){
        for (Buffet _buffet : lsBuffetEntities){
            if(_buffet.isChecked()==true){
                //CRUDOperations crudOperations= new CRUDOperations(new MyDatabase(this));
                //crudOperations.clearDb();


                crudOperations.addCart(new Cart(_buffet.getBuffetId(),_buffet.getTitle(),_buffet.getQty()));
            }
        }
        List<String> fila = new ArrayList<String>();
        Log.v(TAG, "Cart" + crudOperations.getAllCart() );
        List<Cart> prueba = crudOperations.getAllCart();
        for (Cart carrito : prueba){

            fila.add(carrito.getId());


        }
        Log.v(TAG, "ListaCart" + fila );

    }

    private void logout() {
        crudOperations.clearCart();
        PreferencesHelper.signOut(this);
        startActivity(new Intent(this, LoginActivity.class));
        //finish();
    }



    private void renderBuffet(List<Buffet> buffetEntityList) {
        Log.v("CONSOLE", "renderNotes");
        lsBuffetEntities= buffetEntityList;



        buffetAdapter= new BuffetAdapter(this,lsBuffetEntities);
        lstBuffet.setAdapter(buffetAdapter);



    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "MainActivity onResumen - 2");
        //loadData();
        //retrieveBuffet(token);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "MainActivity onPause - 1");
    }

    /*public void guardarPedidos(View view) {
        List<String> grabaditos = null;
        for( Buffet _buffet : lsBuffetEntities ){
            if( _buffet.getMarcado() ){
                grabaditos.add(_buffet.getObjectId());
            }
        }
        PreferencesHelper.setGrabados( this,  TextUtils.join(",", grabaditos) );


    }*/
}
