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
 
package aloogle.rebuapp.widget;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import aloogle.rebuapp.R;
import aloogle.rebuapp.other.Other;

public class WidgetShortcutConfigure extends ActionBarActivity {

	private WidgetShortcutConfigure context;
	String value;
	int what;
	int color;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_shortcut_configure);
		context = this;

		color = 1;

		Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setTitle(getString(R.string.addshortcutwidget));
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#" + Other.getColor(context))));
		findViewById(R.id.frame).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#" + Other.getColor(context))));

		final EditText edit = (EditText)findViewById(R.id.editText1);
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
		builderSingle.setIcon(R.drawable.ic_launcher);
		builderSingle.setTitle("Selecione uma categoria:");
		final ArrayAdapter < String > arrayAdapter = new ArrayAdapter < String > (context, android.R.layout.select_dialog_singlechoice);
		String[]categoryTitles = getResources().getStringArray(R.array.navigation_main_sections);

		for (int i = 1; i < categoryTitles.length; i++) {
			arrayAdapter.add(categoryTitles[i]);
		}
		arrayAdapter.add("Painel");

		builderSingle.setNegativeButton("Cancelar",
			new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				WidgetShortcutConfigure.this.finish();
			}
		});

		builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String strName = arrayAdapter.getItem(which);
				value = strName;
				what = which + 2;
				edit.setText(strName);
			}
		});
		builderSingle.show();

		Button b = (Button)findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RadioGroup rg = (RadioGroup)findViewById(R.id.radioIcon);
				switch (rg.getCheckedRadioButtonId()) {
				case R.id.radioPreto:
					color = 1;
					break;
				case R.id.radioBranco:
					color = 2;
					break;
				case R.id.radioCinza:
					color = 3;
					break;
				case R.id.radioIcone:
					color = 4;
					break;
				}

				String textvalue = edit.getText().toString();
				if (what == 13) {
					what += 2;
				}
				Intent.ShortcutIconResource icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), Other.getShortcutDrawable(getApplicationContext(), what, color));

				Intent launchIntent = new Intent();
				launchIntent.setClassName(getApplicationContext(), getApplicationContext().get/*
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
 
packageManager().getLaunchIntentFor/*
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
 
package(getApplicationContext().get/*
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
 
packageName()).getComponent().getClassName());
				launchIntent.putExtra("widgetpos", what);
				launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				Intent intent = new Intent();
				intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
				intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, textvalue);
				intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

				intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

				setResult(RESULT_OK, intent);

				finish();
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			WidgetShortcutConfigure.this.finish();
			return true;
		default:
			return
			super.onOptionsItemSelected(item);
		}
	}
}
