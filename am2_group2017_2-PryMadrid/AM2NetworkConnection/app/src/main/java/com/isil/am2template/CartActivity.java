package com.isil.am2template;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.isil.am2template.model.entity.Cart;
import com.isil.am2template.model.entity.Place;
import com.isil.am2template.storage.PreferencesHelper;
import com.isil.am2template.storage.db.CRUDOperations;
import com.isil.am2template.storage.db.MyDatabase;
import com.isil.am2template.storage.request.ApiClient;
import com.isil.am2template.storage.request.StorageConstant;
import com.isil.am2template.storage.request.entity.CartRaw;
import com.isil.am2template.storage.request.entity.CartResponse;
import com.isil.am2template.utils.StringUtils;
import com.isil.am2template.view.adapters.CartAdapter;
import com.isil.am2template.view.adapters.PlaceAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pablo Claus on 11/12/2017.
 */

public class CartActivity extends AppCompatActivity {

    private static final String TAG ="CartActivity" ;
    private static final int CART_DETAIL=1;


    private TextView tviLogout,tviUser;
    private ListView lstCart;
    private Button btnCheckout;
    private List<Cart> lsCartEntitites;
    private CRUDOperations crudOperations;
    private CartAdapter cartAdapter;
    private View flayLoading;
    private Button btnClean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //populate();
        init();
        loadData();
    }


    private void loadData() {
        crudOperations= new CRUDOperations(new MyDatabase(this));
        lsCartEntitites= crudOperations.getAllCart();
        cartAdapter= new CartAdapter(this,lsCartEntitites);
        lstCart.setAdapter(cartAdapter);

    }







    private void init() {
        tviLogout= (TextView)findViewById(R.id.tviLogout);
        tviUser= (TextView)findViewById(R.id.tviUser);
        lstCart= (ListView)(findViewById(R.id.lstCart));
        btnCheckout= (Button)(findViewById(R.id.btnCheckout));
        flayLoading=findViewById(R.id.flayLoading);
        btnClean= (Button)(findViewById(R.id.btnClean));

        //user Info
        String username = PreferencesHelper.getUserSession(this);
        if(username!=null)
        {
            tviUser.setText("Bienvenido "+ StringUtils.firstCapitalize(username));
        }



        //events
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gotoNote(ACTION_ADD, null);
                //sendtoCart(ACTION_CART, null);
                sendtoServer();
                showMessage("Se procesó tu pedido");
                crudOperations.clearCart();
                finish();
            }
        });

        btnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gotoNote(ACTION_ADD, null);
                //sendtoCart(ACTION_CART, null);
                crudOperations.clearCart();
                showMessage("No hay items");
                finish();
            }
        });

        /*lstPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Place place = (Place) adapterView.getAdapter().getItem(i);
                gotoNote(ACTION_DETAIL, place);
            }
        });*/

        tviLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    /*private void gotoNote(int action, Place place) {
        Intent intent= new Intent(this,DetailPlaceActivity.class);


        intent.putExtra("FRAGMENT",DetailPlaceActivity.DETAIL_PLACE);
        intent.putExtra("PLACE", place);
        startActivity(intent);


    }*/

    private void sendtoServer(){

        lsCartEntitites= crudOperations.getAllCart();
        String ownerId = PreferencesHelper.getUserID(this);
        String token = PreferencesHelper.getTokenSession(this);
        for (Cart _cart : lsCartEntitites){

            //CRUDOperations crudOperations= new CRUDOperations(new MyDatabase(this));
            //crudOperations.clearDb();
            //crudOperations.addCart(new Cart(_cart.getId(),_cart.getTitle(), _cart.getQty()));
            addRestNote(token,_cart.getId(),_cart.getTitle(), _cart.getQty(),ownerId);



        }
    }

    private void operationSuccess(){}

    private void showLoading(){
        flayLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        flayLoading.setVisibility(View.GONE);
    }

    private void showMessage(String message){
        Toast.makeText(this,
                message,Toast.LENGTH_LONG).show();
    }


    private void addRestNote(String token, String Id, String title, String qty, String ownerId) {


        CartRaw cartRaw= new CartRaw();
        cartRaw.setId(Id);
        cartRaw.setTitle(title);
        cartRaw.setQty(qty);
        cartRaw.setOwnerId(ownerId  );
        Map<String, String> map = new HashMap<>();
        map.put("user-token",token);

        Call<Object> call= ApiClient.getMyApiClient().addCart(
                StorageConstant.APPLICATIONID, StorageConstant.RESTAPIKEY,map,cartRaw);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                //hideLoading();
                if(response!=null){

                    if(response.isSuccessful()){
                        operationSuccess();
                    }else{
                        showMessage("Ocurrió un error");
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                hideLoading();
                showMessage(t.getMessage());
            }
        });


      /*  Call<CartResponse> call= ApiClient.getMyApiClient().addCart(cartRaw);
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if(response!=null){
                    CartResponse cartResponse=null;

                    if(response.isSuccessful()){
                        cartResponse= response.body();
                        if (cartResponse != null) {
                            savedNote(cartResponse.getData());
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
            }
        });*/
    }



    private void logout() {
        crudOperations.clearCart();
        PreferencesHelper.signOut(this);
        startActivity(new Intent(this, LoginActivity.class));

        //finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "MainActivity onResumen - 2");
        loadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "MainActivity onPause - 1");
    }
}
