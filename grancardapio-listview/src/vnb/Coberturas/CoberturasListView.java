package vnb.Coberturas;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import vnb.Galeria.Galeria;
import vnb.Galeria.ShowImage;

import br.vnbeventos.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

public class CoberturasListView extends Activity {
	// All static variables
	
	static final String URL = "http://www.vnbeventos.com.br/coberturas.xml";
	// XML node keys
	static final String KEY_EVENTO = "evento"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_ESTABELECIMENTO = "estabelecimento";
	static final String KEY_TITULO = "titulo";
	static final String KEY_DATA = "data";
	static final String KEY_THUMB_URL = "thumb_url";
	
	ListView list;
    LazyAdapter adapter;
    
    

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coberturas_layout);
		 
		if (TemConexao()){
            trace("tem conexão.");
        }else{
            trace("sem conexão.");
        }
		
        
		ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

		XMLParser parser = new XMLParser();
		String xml = parser.getXmlFromUrl(URL); // getting XML from URL
		Document doc = parser.getDomElement(xml); // getting DOM element
		
		NodeList nl = doc.getElementsByTagName(KEY_EVENTO);
		// looping through all song nodes <song>
		for (int i = 0; i < nl.getLength(); i++) {
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			Element e = (Element) nl.item(i);
			// adding each child node to HashMap key => value
			map.put(KEY_ID, parser.getValue(e, KEY_ID));
			map.put(KEY_ESTABELECIMENTO, parser.getValue(e, KEY_ESTABELECIMENTO));
			map.put(KEY_TITULO, parser.getValue(e, KEY_TITULO));
			map.put(KEY_DATA, parser.getValue(e, KEY_DATA));
			map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));

			// adding HashList to ArrayList
			songsList.add(map);
		}
		

		list=(ListView)findViewById(R.id.list);
		
		// Getting adapter by passing xml data ArrayList
        adapter=new LazyAdapter(this, songsList);        
        list.setAdapter(adapter);
        

        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			
				//Pelo Index
				//Toast.makeText(getApplicationContext(), "You selected item #: " + position,Toast.LENGTH_SHORT).show();

				//Pelo Texto
				TextView idEvento =(TextView)view.findViewById(R.id.idEvento);
				String Evento = idEvento.getText().toString();
				Log.i("mk", "string : "+Evento); 
				Toast.makeText(getApplicationContext(), "ID do Evento Selecionado #: " +Evento,Toast.LENGTH_SHORT).show();
				
				//Inicia Galeria
				//startActivity(new Intent(CustomizedListView.this, Galeria.class));
				
				String Cobertura = idEvento.getText().toString();
									
				Intent i = new Intent(CoberturasListView.this, vnb.WebService.WebService.class);
				
				i.putExtra("idEvento", Cobertura);
				
				// Inicia a Activity
		    	startActivity(i);
									
			}
		});		
        
	} 
	
	 private boolean TemConexao() {
	        boolean lblnRet = false;
	        try
	        {
	            ConnectivityManager cm = (ConnectivityManager)
	            getSystemService(Context.CONNECTIVITY_SERVICE);
	            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
	                lblnRet = true;
	            } else {
	                lblnRet = false;
	            }
	        }catch (Exception e) {
	            trace(e.getMessage());
	        }
	        return lblnRet;
	    }
	 
	    public void toast (String msg){
	        Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
	    }
	  
	    private void trace (String msg){
	        Log.d ("Internet", msg);
	        toast(msg);
	    } 
	
}