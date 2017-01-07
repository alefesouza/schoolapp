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
 
package aloogle.rebuapp.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.TaskStackBuilder;
import android.preference.PreferenceManager;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;
import aloogle.rebuapp.R;

public class NotificationReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals("aloogle.rebuapp.UPDATE_STATUS")) {
			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
			final Editor editor = preferences.edit();
			String sala = preferences.getString("classRoom", "");
			String clube = preferences.getString("clubeRoom", "");
			String eletiva = preferences.getString("eletivaRoom", "");
			boolean notification = preferences.getBoolean("prefNotification", true);
			if (notification) {
				String[]lastparts = preferences.getString("lastReceivedIds", "").split("\\$\\%\\#");
				try {
					JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

					if (json.getString("to").matches(sala + "|" + clube + "|" + eletiva + "|todos") || (json.getString("to").equals("responsaveis") && preferences.getBoolean("isRespon", false)) || (json.getString("to").equals("cantina") && preferences.getBoolean("notifCantina", false))) {
						editor.putString("receivedId", json.getString("id"));
						editor.commit();
						editor.putString("receivedTo", json.getString("to"));
						editor.commit();
						editor.putString("receivedTicker", json.getString("barra"));
						editor.commit();
						editor.putString("receivedTitle", json.getString("titulo"));
						editor.commit();
						editor.putString("receivedText", json.getString("texto"));
						editor.commit();
						editor.putString("receivedBigTitle", json.getString("titulogrande"));
						editor.commit();
						editor.putString("receivedBigText", json.getString("textogrande"));
						editor.commit();
						editor.putString("receivedSummary", json.getString("sumario"));
						editor.commit();

						if (Arrays.asList(lastparts).contains(json.getString("id"))) {}
						else {
							editor.putInt("count", preferences.getInt("count", 0) + 1);
							editor.commit();
							editor.putString("receivedTitles", json.getString("texto") + "$%#" + preferences.getString("receivedTitles", ""));
							editor.commit();
						}
					}
				} catch (JSONException e) {}

				Intent cancelintent = new Intent(context, CancelReceiver.class);
				cancelintent.setAction("notification_cancelled");
				PendingIntent cancel = PendingIntent.getBroadcast(context, 0, cancelintent, PendingIntent.FLAG_CANCEL_CURRENT);

				if ((preferences.getString("receivedTo", "").matches(sala + "|" + clube + "|" + eletiva + "|todos")) || (preferences.getString("receivedTo", "").equals("responsaveis") && preferences.getBoolean("isRespon", false)) || (preferences.getString("receivedTo", "").equals("cantina") && preferences.getBoolean("notifCantina", false))) {
					if (Arrays.asList(lastparts).contains(preferences.getString("receivedId", ""))) {}
					else {
						if (preferences.getInt("count", 0) == 1) {
							editor.putString("lastReceivedIds", preferences.getString("receivedId", "") + "$%#" + preferences.getString("lastReceivedIds", ""));
							editor.commit();
							String[]lastparts2 = preferences.getString("lastReceivedIds", "").split("\\$\\%\\#");
							if (lastparts2.length >= 5) {
								editor.putString("lastReceivedIds", lastparts2[0] + "$%#" + lastparts2[1] + "$%#" + lastparts2[2] + "$%#" + lastparts2[3] + "$%#" + lastparts2[4]);
								editor.commit();
							}

							NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
								.setSmallIcon(R.drawable.ic_launcher)
								.setTicker(preferences.getString("receivedTicker", ""))
								.setContentTitle(preferences.getString("receivedTitle", ""))
								.setContentText(preferences.getString("receivedText", ""))
								.setAutoCancel(true)
								.setLights(0xFF005400, 1500, 2500)
								.setDefaults(NotificationCompat.DEFAULT_VIBRATE)
								.setPriority(NotificationCompat.PRIORITY_MAX)
								.setStyle(new BigTextStyle()
									.setBigContentTitle(preferences.getString("receivedBigTitle", ""))
									.bigText(preferences.getString("receivedBigText", ""))
									.setSummaryText(preferences.getString("receivedSummary", "")))
								.setDeleteIntent(cancel);
							
							Intent resultIntent = new Intent(context, aloogle.rebuapp.activity.SplashScreen.class);
							resultIntent.putExtra("fromnotification", true);

							TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

							stackBuilder.addParentStack(aloogle.rebuapp.activity.SplashScreen.class);

							stackBuilder.addNextIntent(resultIntent);
							PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
							mBuilder.setContentIntent(resultPendingIntent);
							NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

							mNotificationManager.notify(0, mBuilder.build());
						} else {
							editor.putString("lastReceivedIds", preferences.getString("receivedId", "") + "$%#" + preferences.getString("lastReceivedIds", ""));
							editor.commit();
							String[]lastparts2 = preferences.getString("lastReceivedTitles", "").split("\\$\\%\\#");
							if (lastparts2.length >= 5) {
								editor.putString("lastReceivedIds", lastparts2[0] + "$%#" + lastparts2[1] + "$%#" + lastparts2[2] + "$%#" + lastparts2[3] + "$%#" + lastparts2[4]);
								editor.commit();
							}

							String[]parts = preferences.getString("receivedTitles", "").split("\\$\\%\\#");
							NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
							inboxStyle.setBigContentTitle(context.getString(R.string.app_name));
							inboxStyle.setSummaryText(preferences.getInt("count", 0) + " novas notificações");
							for (int i = 0; i <= parts.length - 1; i++) {
								inboxStyle.addLine(parts[i]);
							}

							NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
								.setSmallIcon(R.drawable.ic_launcher)
								.setTicker("Novas notificações! - RebuApp")
								.setContentTitle("RebuApp")
								.setLights(0xFF005400, 1500, 2500)
								.setContentText(preferences.getInt("count", 0) + " novas notificações")
								.setAutoCancel(true)
								.setStyle(inboxStyle)
								.setDeleteIntent(cancel);

							Intent resultIntent = new Intent(context, aloogle.rebuapp.activity.SplashScreen.class);
							resultIntent.putExtra("fromnotification", "true");

							TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

							stackBuilder.addParentStack(aloogle.rebuapp.activity.SplashScreen.class);

							stackBuilder.addNextIntent(resultIntent);
							PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
							mBuilder.setContentIntent(resultPendingIntent);

							NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
							mNotificationManager.notify(0, mBuilder.build());
						}
					}
				}
			}
		}
	}
}
