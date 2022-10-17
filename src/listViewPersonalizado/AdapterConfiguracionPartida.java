package listViewPersonalizado;

import java.util.ArrayList;
import objetos.EtiquetasXML;
import objetos.Rutas;
import xml.XMLParser;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import es.uvigo.gti.mygamewordsearch.R;

public class AdapterConfiguracionPartida extends BaseAdapter {
	protected Activity activity;
	
	ArrayList<ConfiguracionPartida> items;


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Generamos una convertView por motivos de eficiencia
		View v = convertView;
		// Asociamos el layout de la lista que hemos creado
		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inf.inflate(R.layout.lista_partidas, null);
		}
		Context contexto = parent.getContext();
		// Creamos un objeto directivo
		ConfiguracionPartida cp = items.get(position);
		// Rellenamos la fotografía
		ImageView foto = (ImageView) v.findViewById(R.id.foto);
		foto.setImageDrawable(cp.getFoto());
		// Rellenamos la categoria
		TextView nombre = (TextView) v.findViewById(R.id.categoria);
		
		XMLParser xml = new XMLParser(contexto);
		ArrayList<String> config = xml.leeOpcionesJuego(Rutas.getRutaArchivos() + cp.getCategoria() + Rutas.getOpcionesJuego());
		String str = (config.get(0).toString().equals(EtiquetasXML.getSi())? " (" + config.get(1).toString() + " " + contexto.getResources().getString(R.string.niveles) + ")":"");		
		nombre.setText(cp.getCategoria() + str);
		// Rellenamos el tamano
		TextView tama�o = (TextView) v.findViewById(R.id.tamano);
		tama�o.setText(contexto.getString(R.string.tamano) + " " + cp.getTama�o() + "x" + cp.getTama�o());
		// Rellenamos con la informaci�n de palabras invertidas
		return v;
	}
	
	
	public AdapterConfiguracionPartida(Activity activity, ArrayList<ConfiguracionPartida> items) {
		this.activity = activity;
		this.items = items;
	}

	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int posicion) {
		return items.get(posicion);
	}

	@Override
	public long getItemId(int posicion) {
		return items.get(posicion).getId();
	}


}
