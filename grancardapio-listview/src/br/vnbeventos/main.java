package br.vnbeventos;
 
import vnb.Agenda.AgendaListView;
import vnb.Coberturas.CoberturasListView;
import vnb.Galeria.Galeria;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
 
    public class main extends TabActivity{
     
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
     
            TabHost tabHost = getTabHost();
          
            TabSpec inicio = tabHost.newTabSpec("Inicio");
            inicio.setIndicator("", getResources().getDrawable(R.drawable.icon_inicio_tab));
            Intent inicioIntent = new Intent(this, Principal.class);
            inicio.setContent(inicioIntent);
     
            TabSpec coberturas = tabHost.newTabSpec("Inicio");
            coberturas.setIndicator(" ", getResources().getDrawable(R.drawable.icon_coberturas_tab));
            Intent coberturasIntent = new Intent(this, CoberturasListView.class);
            coberturas.setContent(coberturasIntent);
            
            TabSpec agenda = tabHost.newTabSpec("Agenda");
            agenda.setIndicator(" ", getResources().getDrawable(R.drawable.icon_agenda_tab));
            Intent agendaIntent = new Intent(this, AgendaListView.class);
            agenda.setContent(agendaIntent);          
        
            //Adicionando
            tabHost.addTab(inicio);
            tabHost.addTab(coberturas);
            tabHost.addTab(agenda);
           
        }

}