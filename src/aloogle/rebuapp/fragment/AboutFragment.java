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
 
package aloogle.rebuapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm./*
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
 
packageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import aloogle.rebuapp.R;

public class AboutFragment extends Fragment {
	Activity activity;
	View view;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.about, container, false);

		aloogle.rebuapp.activity.FragmentActivity.ActionBarColor(((ActionBarActivity)getActivity()), getString(R.string.about));

		try {
			String sobretext = "<h3>RebuApp vers&atilde;o " + getActivity().get/*
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
 
packageManager().get/*
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
 
packageInfo(getActivity().get/*
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
 
packageName(), 0).versionName + "</h3>\n" +
				"Aplicativo desenvolvido por <a href=\"http://google.com/+AlefeSouza\">Alefe Souza</a>.<br><br>" +
				"RebuApp &eacute; um aplicativo para Android e web (acesse a vers&atilde;o web em http://rebuapp.tk de qualquer iPhone, Windows Phone ou computador) que cont&eacute;m o hor&aacute;rio e agenda de todas as salas, recados de clubes e eletivas, comunicados da direção, acesso à pesquisa de todos os livros dispon&iacute;veis na biblioteca, e atalhos para as redes sociais da Escola Estadual Prof&ordm; Willian Rodrigues Rebu&aacute; em Carapicu&iacute;ba, S&atilde;o Paulo, Brasil." +
				"<h3>Licen&ccedil;a</h3>\n" +
				"At&eacute; a vers&atilde;o 1.5 esse aplicativo foi lan&ccedil;ado sob <a href=\"http://choosealicense.com/licenses/gpl-v3\">licen&ccedil;a GPLv3</a>, caso queira dar uma olhada o c&oacute;digo fonte da vers&atilde;o 1.5 est&aacute; dispon&iacute;vel no meu <a href=\"https://github.com/alefesouza/schoolapp\">GitHub</a>.";

			TextView sobre = (TextView)view.findViewById(R.id.sobre);
			sobre.setMovementMethod(LinkMovementMethod.getInstance());
			sobre.setText(Html.fromHtml(sobretext));

		} catch (NameNotFoundException e) {}

		view.findViewById(R.id.sourcecodelicenses).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), aloogle.rebuapp.activity.FragmentActivity.class);
				intent.putExtra("fragment", 3);
				startActivity(intent);
			}
		});
		return view;
	}
}
