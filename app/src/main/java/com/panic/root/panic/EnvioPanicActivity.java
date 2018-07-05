package com.panic.root.panic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.telephony.gsm.SmsManager;


@SuppressWarnings("deprecation")
@SuppressLint("HandlerLeak")
public class EnvioPanicActivity extends Activity {


	 //textBox y demas elementos del activity
	private Cursor dbCursor;
	private String tableName;
	private String fieldName;
	private String[] from;
	private DBAdapter db;




	 private ProgressDialog pDialog;
	 private Handler puente = new Handler() {
		 @Override
		 public void handleMessage(Message msg) {
			 //Mostramos el mensage recibido del servidor en pantalla
			 Toast.makeText(getApplicationContext(), (String) msg.obj,
					 Toast.LENGTH_LONG).show();
		 }
	 };

	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_main);

		 //Acceder a datos de los Contactos para el envío de mensajes--------------------
		 db = new DBAdapter(getApplicationContext());
		 db.open();

		 tableName = DBOpenHelper.TABLE_CUSTOMER;
		 fieldName = DBOpenHelper.CUSTOMER_COL_PHONE;
		 from = new String[] { "_id", "phone" };
		 dbCursor = db.getCursor(tableName,from,null,null);
		 // Obtenemos los índices de las columnas
		 int nameColumn = dbCursor.getColumnIndex(fieldName);
		 //int phoneColumn = dbCursor.getColumnIndex(People.NUMBER);
		 //Recorremos el cursor
		 for(dbCursor.moveToFirst(); !dbCursor.isAfterLast(); dbCursor.moveToNext()){
			 String phone = dbCursor.getString(nameColumn);
			 enviarSMS(phone);
		 }
		 //---------------------------------------------------------------------------
		 new SendData().execute();
	 }

@SuppressWarnings("rawtypes")
private class SendData extends AsyncTask {
	@Override
	protected Object doInBackground(Object... arg0) {
		Datasend();
		return null;
	}
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(EnvioPanicActivity.this);
		pDialog.setMessage("Enviando Auxilio..por favor espere");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
	}
}

	 private void Datasend(){
	    //Utilizamos la clase Httpclient para conectar
	    HttpClient httpclient = new DefaultHttpClient();
	    //Utilizamos la HttpPost para enviar lso datos
	    //A la url donde se encuentre nuestro archivo receptor
	    HttpPost httppost = new HttpPost("http://192.168.0.25/aguara/datareceptor.php");
	    try {
	    List<NameValuePair> postValues = new ArrayList<NameValuePair>(2);
	    postValues.add(new BasicNameValuePair("latitud", getIntent().getStringExtra("latitud")));
	    postValues.add(new BasicNameValuePair("longitud", getIntent().getStringExtra("longitud")));
		postValues.add(new BasicNameValuePair("precision", getIntent().getStringExtra("precision")));
		postValues.add(new BasicNameValuePair("imei", getIntent().getStringExtra("imei")));
	    //Encapsulamos
	    httppost.setEntity(new UrlEncodedFormEntity(postValues));
	    //Lanzamos la petici�n
	    HttpResponse respuesta = httpclient.execute(httppost);
	    //Conectamos para recibir datos de respuesta
	    HttpEntity entity = respuesta.getEntity();
	    //Creamos el InputStream como su propio nombre indica
	    InputStream is = entity.getContent();
	    //Limpiamos el codigo obtenido atraves de la funcion
	    //StreamToString explicada m�s abajo
	    String resultado= StreamToString(is);
	    //Enviamos el resultado LIMPIO al Handler para mostrarlo
	    Message sms = new Message();
	    sms.obj = resultado;
		//Vuelve al Activity anterior
		finish();
	}catch (IOException e) {
	    //TODO Auto-generated catch block

	 }
	}
    //Funcion para 'limpiar' el codigo recibido
    public String StreamToString(InputStream is) {
        //Creamos el Buffer
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            //Bucle para leer todas las l�neas
            //En este ejemplo al ser solo 1 la respuesta
            //Pues no har�a falta
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //retornamos el codigo l�mpio
        return sb.toString();
    }

	private void enviarSMS(String phone){
		try {
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phone,null,"Mensaje de Alerta recibido. Lat: "+getIntent().getStringExtra("latitud")
					+" Long: "+getIntent().getStringExtra("longitud"),null,null);
			Toast.makeText(getApplicationContext(), "Mensaje de Alerta enviado.", Toast.LENGTH_LONG).show();
		}

		catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
			e.printStackTrace();

	}
	}
}