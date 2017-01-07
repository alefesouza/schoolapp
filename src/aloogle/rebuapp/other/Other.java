/*
 * Copyright (C) 2015 Alefe Souza <contato@alefesouza.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package aloogle.rebuapp.other;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import java.util.Calendar;
import java.util.Date;
import aloogle.rebuapp.R;
import android.support.v4.app.*;
import aloogle.rebuapp.fragment.*;

@SuppressLint("NewApi")
public class Other {

	public static String getSala(Activity activity) {
		String sala = null;
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		String prefClassRoom = preferences.getString("classRoom", "none");
		switch (prefClassRoom) {
		case "none":
			sala = "Escolha sua sala";
			break;
		case "1a":
			sala = "1°A";
			break;
		case "1b":
			sala = "1°B";
			break;
		case "1c":
			sala = "1°C";
			break;
		case "2a":
			sala = "2°A";
			break;
		case "2b":
			sala = "2°B";
			break;
		case "2c":
			sala = "2°C";
			break;
		case "2d":
			sala = "2°D";
			break;
		case "3a":
			sala = "3°A";
			break;
		case "3b":
			sala = "3°B";
			break;
		case "3c":
			sala = "3°C";
			break;
		}
		return sala;
	}

	public static String getClube(Activity activity) {
		String clube = null;
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		String prefClubeRoom = preferences.getString("clubeRoom", "none");
		switch (prefClubeRoom) {
		case "none":
			clube = "Escolha seu clube";
			break;
		case "clubeacademia":
			clube = "Acadêmia";
			break;
		case "clubeartesanatohippie":
			clube = "Artesanato Hippie";
			break;
		case "clubeboxe":
			clube = "Boxe";
			break;
		case "clubedanca":
			clube = "Dança";
			break;
		case "clubeestetica":
			clube = "Estética";
			break;
		case "clubefotografia":
			clube = "Fotografia";
			break;
		case "clubefutsal":
			clube = "Futsal";
			break;
		case "clubejornal":
			clube = "Jornal";
			break;
		case "clubemusica":
			clube = "Música";
			break;
		case "clubeorganizacaofestas":
			clube = "Organização de Festas";
			break;
		case "clubeorigame":
			clube = "Origame";
			break;
		case "clubesaber":
			clube = "Saber";
			break;
		case "clubeteatro":
			clube = "Teatro";
			break;
		case "clubevolei":
			clube = "Vôlei";
			break;
		}
		return clube;
	}

	public static String getEletiva(Activity activity) {
		String eletiva = null;
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		String prefEletivaRoom = preferences.getString("eletivaRoom", "none");
		switch (prefEletivaRoom) {
		case "none":
			eletiva = "Escolha sua eletiva";
			break;
		case "eletivacomunicacaosocial":
			eletiva = "Comunicação Social";
			break;
		case "eletivaengenharia":
			eletiva = "Engenharia";
			break;
		case "eletivafisiologia":
			eletiva = "Fisiologia";
			break;
		case "eletivafutsalfeminino":
			eletiva = "Futsal Feminino";
			break;
		case "eletivalegislacao":
			eletiva = "Legislação";
			break;
		case "eletivamedicina":
			eletiva = "Medicina";
			break;
		case "eletivaquimica":
			eletiva = "Química";
			break;
		case "eletivateatro":
			eletiva = "Teatro";
			break;
		}
		return eletiva;
	}

	public static String getWeekDay(boolean isvalue) {
		String weekday = null;
		String value = null;

		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		switch (day) {
		case Calendar.SUNDAY:
			weekday = "Domingo";
			value = "domingo";
			break;
		case Calendar.MONDAY:
			weekday = "Segunda";
			value = "segunda";
			break;
		case Calendar.TUESDAY:
			weekday = "Terça";
			value = "terca";
			break;
		case Calendar.WEDNESDAY:
			weekday = "Quarta";
			value = "quarta";
			break;
		case Calendar.THURSDAY:
			weekday = "Quinta";
			value = "quinta";
			break;
		case Calendar.FRIDAY:
			weekday = "Sexta";
			value = "sexta";
			break;
		case Calendar.SATURDAY:
			weekday = "Sábado";
			value = "sabado";
			break;
		}
		if (isvalue) {
			return value;
		} else {
			return weekday;
		}
	}

	public static String getColor(Activity activity) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		String userColor = preferences.getString("prefColor", "005400");
		return userColor;
	}

	public static String getColor2(Activity activity, int qual) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		String userColor = preferences.getString("prefColor", "005400");
		if (userColor.matches("005400|008002|00cc00")) {
			if (qual == 0) {
				return "008002";
			} else {
				return "005400";
			}
		} else {
			if (qual == 0) {
				return "0a4e91";
			} else {
				return "003061";
			}
		}
	}

	public static String compareDate(long toCompare) {
		String what = "";
		Date currentDate = new Date();

		long diff = currentDate.getTime() - toCompare;
		long seconds = diff / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;
		long months = days / 31;
		long years = months / 12;

		if (seconds < 60) {
			String plural = null;
			if (seconds <= 1) {
				plural = "segundo";
			} else {
				plural = "segundos";
			}
			what = String.valueOf(seconds) + " " + plural;
		} else if (minutes < 60) {
			String plural = null;
			if (seconds <= 1) {
				plural = "minuto";
			} else {
				plural = "minutos";
			}
			what = String.valueOf(minutes) + " " + plural;
		} else if (hours < 24) {
			String plural = null;
			if (hours <= 1) {
				plural = "hora";
			} else {
				plural = "horas";
			}
			what = String.valueOf(hours) + " " + plural;
		} else if (days < 31) {
			String plural = null;
			if (days <= 1) {
				plural = "dia";
			} else {
				plural = "dias";
			}
			what = String.valueOf(days) + " " + plural;
		} else if (months < 12) {
			String plural = null;
			if (months <= 1) {
				plural = "mês";
			} else {
				plural = "meses";
			}
			what = String.valueOf(months) + " " + plural;
		} else {
			String plural = null;
			if (years <= 1) {
				plural = "ano";
			} else {
				plural = "anos";
			}
			what = String.valueOf(years) + " " + plural;
		}
		return what;
	}

	public static boolean isConnected(Activity activity) {
		ConnectivityManager cm = (ConnectivityManager)activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
		boolean isConnected = cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
		return isConnected;
	}

	public static void openPanel(Activity activity) {
		if (String.valueOf(Build.VERSION.SDK_INT).matches("10|16|17|18")) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("http://apps.aloogle.net/schoolapp/rebua/painel"));
			activity.startActivity(intent);
		} else {
			Intent painel = new Intent(activity, aloogle.rebuapp.activity.FragmentActivity.class);
			painel.putExtra("fragment", 2);
			painel.putExtra("titulo", "Painel");
			painel.putExtra("url", "http://apps.aloogle.net/schoolapp/rebua/painel");
			activity.startActivity(painel);
		}
	}

	public static int getShortcutDrawable(Context activity, int pos, int cor) {
		int b;
		switch (pos) {
		case 2:
			switch (cor) {
			case 1:
				b = R.drawable.widget_clube_preto;
				break;
			case 2:
				b = R.drawable.widget_clube_branco;
				break;
			case 3:
				b = R.drawable.widget_clube_cinza;
				break;
			default:
				b = R.drawable.ic_launcher;
			}
			break;
		case 3:
			switch (cor) {
			case 1:
				b = R.drawable.widget_eletiva_preto;
				break;
			case 2:
				b = R.drawable.widget_eletiva_branco;
				break;
			case 3:
				b = R.drawable.widget_eletiva_cinza;
				break;
			default:
				b = R.drawable.ic_launcher;
			}
			break;
		case 4:
			switch (cor) {
			case 1:
				b = R.drawable.widget_comunicados_preto;
				break;
			case 2:
				b = R.drawable.widget_comunicados_branco;
				break;
			case 3:
				b = R.drawable.widget_comunicados_cinza;
				break;
			default:
				b = R.drawable.ic_launcher;
			}
			break;
		case 5:
			switch (cor) {
			case 1:
				b = R.drawable.widget_notificacoes_preto;
				break;
			case 2:
				b = R.drawable.widget_notificacoes_branco;
				break;
			case 3:
				b = R.drawable.widget_notificacoes_cinza;
				break;
			default:
				b = R.drawable.ic_launcher;
			}
			break;
		case 6:
			switch (cor) {
			case 1:
				b = R.drawable.widget_biblioteca_preto;
				break;
			case 2:
				b = R.drawable.widget_biblioteca_branco;
				break;
			case 3:
				b = R.drawable.widget_biblioteca_cinza;
				break;
			default:
				b = R.drawable.ic_launcher;
			}
			break;
		case 7:
			switch (cor) {
			case 1:
				b = R.drawable.widget_cantina_preto;
				break;
			case 2:
				b = R.drawable.widget_cantina_branco;
				break;
			case 3:
				b = R.drawable.widget_cantina_cinza;
				break;
			default:
				b = R.drawable.ic_launcher;
			}
			break;
		case 8:
			switch (cor) {
			case 1:
				b = R.drawable.widget_anotacoes_preto;
				break;
			case 2:
				b = R.drawable.widget_anotacoes_branco;
				break;
			case 3:
				b = R.drawable.widget_anotacoes_cinza;
				break;
			default:
				b = R.drawable.ic_launcher;
			}
			break;
		case 9:
			switch (cor) {
			case 1:
				b = R.drawable.widget_dicionario_preto;
				break;
			case 2:
				b = R.drawable.widget_dicionario_branco;
				break;
			case 3:
				b = R.drawable.widget_dicionario_cinza;
				break;
			default:
				b = R.drawable.ic_launcher;
			}
			break;
		case 10:
			switch (cor) {
			case 1:
				b = R.drawable.widget_cartazes_preto;
				break;
			case 2:
				b = R.drawable.widget_cartazes_branco;
				break;
			case 3:
				b = R.drawable.widget_cartazes_cinza;
				break;
			default:
				b = R.drawable.ic_launcher;
			}
			break;
		case 11:
			switch (cor) {
			case 1:
				b = R.drawable.widget_blog_preto;
				break;
			case 2:
				b = R.drawable.widget_blog_branco;
				break;
			case 3:
				b = R.drawable.widget_blog_cinza;
				break;
			default:
				b = R.drawable.ic_launcher;
			}
			break;
		case 12:
			switch (cor) {
			case 1:
				b = R.drawable.widget_blog_preto;
				break;
			case 2:
				b = R.drawable.widget_blog_branco;
				break;
			case 3:
				b = R.drawable.widget_blog_cinza;
				break;
			default:
				b = R.drawable.ic_launcher;
			}
			break;
		case 15:
			switch (cor) {
			case 1:
				b = R.drawable.widget_painel_preto;
				break;
			case 2:
				b = R.drawable.widget_painel_branco;
				break;
			case 3:
				b = R.drawable.widget_painel_cinza;
				break;
			default:
				b = R.drawable.ic_launcher;
			}
			break;
		default:
			b = R.drawable.ic_launcher;
		}
		return b;
	}

	public static Fragment getFragment(int pos) {
		Fragment f;
		switch (pos) {
		case 1:
			f = new SalaFragment();
			break;
		case 2:
			f = new ClubeFragment();
			break;
		case 3:
			f = new EletivaFragment();
			break;
		case 4:
			f = new ComunicadosFragment();
			break;
		case 5:
			f = new NotificationsFragment();
			break;
		case 6:
			f = new ReadingFragment();
			break;
		case 7:
			f = new CantinaFragment();
			break;
		case 8:
			f = new AnnotationsFragment();
			break;
		case 9:
			f = new DictionaryFragment();
			break;
		case 10:
			f = new CartazFragment();
			break;
		default:
			f = new SalaFragment();
		}
		return f;
	}
}
