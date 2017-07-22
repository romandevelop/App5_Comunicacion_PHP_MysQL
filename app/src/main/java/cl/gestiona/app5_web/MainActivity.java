package cl.gestiona.app5_web;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;

public class MainActivity extends AppCompatActivity {


    private ListView list_producto;
    private EditText txtnombre, txtprecio;
    private AsyncHttpClient client = new AsyncHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_producto = (ListView) findViewById(R.id.list_producto);
        txtnombre = (EditText) findViewById(R.id.txtnombre);
        txtprecio = (EditText) findViewById(R.id.txtprecio);
        clienteObtieneDatos();
    }


    public void guardarProducto(View view){
        if (txtnombre.getText().toString().isEmpty() || txtprecio.getText().toString().isEmpty()){
            printToast("Complete todos los campos");
        }else{
            clienteEnviaDatos();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clienteObtieneDatos();
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtnombre.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(txtprecio.getWindowToken(), 0);
    }

    public void clienteObtieneDatos(){
        String url = "http://todojava.net/data.php";
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    cargarListView(new String (responseBody));
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void cargarListView(String respuesta){
        List<Producto> lista = new ArrayList<>();
        try{
            JSONArray json = new JSONArray(respuesta);
            System.out.println("------SE LLAMA A LA LISTA___");
            for (int i=0; i<json.length();i++){
                Producto nuevo = new Producto();
                nuevo.setCodigo(json.getJSONObject(i).getString("codigo"));
                nuevo.setNombre(json.getJSONObject(i).getString("nombre"));
                nuevo.setPrecio(Integer.parseInt(json.getJSONObject(i).getString("precio")));
                lista.add(nuevo);
            }
            //creamos el adaptador del lisview
            ArrayAdapter<Producto> adapter = new ArrayAdapter<Producto>(this,
                                                                        android.R.layout.simple_list_item_1,
                                                                        lista);
            list_producto.setAdapter(adapter);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void clienteEnviaDatos(){
        String url = "http://todojava.net/newdata.php";
        RequestParams params = new RequestParams();
        params.add("nombre", txtnombre.getText().toString());
        params.add("precio", txtprecio.getText().toString());

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    printToast(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }


    public void printToast(String msg){
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }








}
