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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import aloogle.rebuapp.R;
import aloogle.rebuapp.adapter.TagAdapter;
import aloogle.rebuapp.other.CustomTextView;

@SuppressLint("InflateParams")
public class LicensesFragment extends Fragment {
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 	Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.license, container, false);

		aloogle.rebuapp.activity.FragmentActivity.ActionBarColor(((ActionBarActivity)getActivity()), getString(R.string.sourcecodelicenses));

		String[]textos = {
			"<h4>Notice for file(s)</h4>" +
			"<a href=\"http://apps.aloogle.net/blogapp/redirect.php?to=androidsupport\">Android Support Libraries</a>" +
			"<br><ul>" +
			"<li>android-support-v4</li>" +
			"<li>android-support-v7-appcompat</li>" +
			"<li>android-support-v7-cardview</li>" +
			"<li>android-support-v7-recyclerview</li>" +
			"<li>google-play-services</li>" +
			"</ul>", 		"<h4>Notice for file(s)</h4>" +
			"<a href=\"http://apps.aloogle.net/schoolapp/redirect.php?to=slidingtab\">Sliding Tab Layout</a>" +
			"<br><ul>" +
			"<li>SlidingTabStrip.java</li>" +
			"<li>SlidingTabLayout.java</li>" +
			"</ul>", 		"<h4>Notice for file(s)</h4>" +
			"<a href=\"http://apps.aloogle.net/schoolapp/redirect.php?to=parse\">Parse</a>" +
			"<br><ul>" +
			"<li>Parse-1.4.3.jar</li>" +
			"</ul>", 		"<h4>Notice for file(s)</h4>" +
			"<a href=\"http://apps.aloogle.net/schoolapp/redirect.php?to=listviewanimations\">ListViewAnimations</a>" +
			"<br><ul>" +
			"<li>albcore.jar</li>" +
			"</ul>", 		"<h4>Notice for file(s)</h4>" +
			"<a href=\"http://apps.aloogle.net/schoolapp/redirect.php?to=nineoldandroids\">NineOldAndroids</a>" +
			"<br><ul>" +
			"<li>nineoldandroids-2.4.0.jar</li>" +
			"</ul>", 		"<h4>Notice for file(s)</h4>" +
			"<a href=\"http://apps.aloogle.net/schoolapp/redirect.php?to=fab\">FloatingActionButton</a>" +
			"<br><ul>" +
			"<li>FloatingActionButton</li>" +
			"</ul>", 		"<h4>Notice for file(s)</h4>" +
			"<a href=\"http://apps.aloogle.net/schoolapp/redirect.php?to=materialdesignlibrary\">MaterialDesignLibrary</a>" +
			"<br><ul>" +
			"<li>MaterialDesignLibrary</li>" +
			"</ul>", 		"<h4>Notice for file(s)</h4>" +
			"<a href=\"http://apps.aloogle.net/schoolapp/redirect.php?to=supportv4pf\">Android Support v4 Preference Fragment</a>" +
			"<br><ul>" +
			"<li>android-support-v4-preferencefragment</li>" +
			"</ul>", 		"<h4>Notice for file(s)</h4>" +
			"<a href=\"http://apps.aloogle.net/schoolapp/redirect.php?to=observablescrollview\">ObservableScrollView</a>" +
			"<br><ul>" +
			"<li>ObservableScrollView</li>" +
			"</ul>"
		};

		String[]licenses = {
			"<pre><code>" +
			" Copyright (C) 2014 The Android Open Source Project" +
			"<br><br>" +
			"   Licensed under the Apache License, Version 2.0 (the \"License\");" +
			"   you may not use this file except in compliance with the License." +
			"   You may obtain a copy of the License at" +
			"<br><br>" +
			"       http://www.apache.org/licenses/LICENSE-2.0" +
			"<br><br>" +
			"   Unless required by applicable law or agreed to in writing, software" +
			"   distributed under the License is distributed on an \"AS IS\" BASIS," +
			"   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied." +
			"   See the License for the specific language governing permissions and" +
			"   limitations under the License." +
			"  </code></pre>", 		"<pre><code>" +
			"   Copyright 2014 Google Inc. All rights reserved." +
			"<br><br>" +
			"   Licensed under the Apache License, Version 2.0 (the \"License\");" +
			"   you may not use this file except in compliance with the License." +
			"   You may obtain a copy of the License at" +
			"<br><br>" +
			"       http://www.apache.org/licenses/LICENSE-2.0" +
			"<br><br>" +
			"   Unless required by applicable law or agreed to in writing, software" +
			"   distributed under the License is distributed on an \"AS IS\" BASIS," +
			"   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied." +
			"   See the License for the specific language governing permissions and" +
			"   limitations under the License." +
			"  </code></pre>", 		"<pre><code>" +
			"   Parse grants you a revocable, personal, worldwide, royalty-free," +
			"   non-assignable and non-exclusive license to use the software provided to you by Parse as part of the Parse Services as provided to you by Parse." +
			"   This license is for the sole purpose of enabling you to use and enjoy the benefit of the Parse Services as provided by Parse, in the manner permitted by the Terms." +
			"  </code></pre>", 		"<pre><code>" +
			"Copyright 2014 Niek Haarman" +
			"<br><br>" +
			"   Licensed under the Apache License, Version 2.0 (the \"License\");" +
			"   you may not use this file except in compliance with the License." +
			"   You may obtain a copy of the License at" +
			"<br><br>" +
			"       http://www.apache.org/licenses/LICENSE-2.0" +
			"<br><br>" +
			"   Unless required by applicable law or agreed to in writing, software" +
			"   distributed under the License is distributed on an \"AS IS\" BASIS," +
			"   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied." +
			"   See the License for the specific language governing permissions and" +
			"   limitations under the License." +
			"</code></pre>", 		"<pre><code>" +
			"   Copyright 2012 Jake Wharton" +
			"<br><br>" +
			"   Licensed under the Apache License, Version 2.0 (the \"License\");" +
			"   you may not use this file except in compliance with the License." +
			"   You may obtain a copy of the License at" +
			"<br><br>" +
			"       http://www.apache.org/licenses/LICENSE-2.0" +
			"<br><br>" +
			"   Unless required by applicable law or agreed to in writing, software" +
			"   distributed under the License is distributed on an \"AS IS\" BASIS," +
			"   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied." +
			"   See the License for the specific language governing permissions and" +
			"   limitations under the License." +
			"</code></pre>", 		"<pre><code>" +
			"   The MIT License (MIT)" +
			"<br><br>" +
			"   Copyright (c) 2014 Oleksandr Melnykov" +
			"<br><br>" +
			"   Permission is hereby granted, free of charge, to any person obtaining a copy" +
			"   of this software and associated documentation files (the \"Software\"), to deal" +
			"   in the Software without restriction, including without limitation the rights" +
			"   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell" +
			"   copies of the Software, and to permit persons to whom the Software is" +
			"   furnished to do so, subject to the following conditions:" +
			"<br><br>" +
			"   The above copyright notice and this permission notice shall be included in all" +
			"   copies or substantial portions of the Software." +
			"<br><br>" +
			"   THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR" +
			"   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY," +
			"   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE" +
			"   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER" +
			"   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM," +
			"   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE" +
			"   SOFTWARE." +
			"  </code></pre>", 		"<pre><code>" +
			"Copyright 2014 Ivan Navas" +
			"<br><br>" +
			"   Licensed under the Apache License, Version 2.0 (the \"License\");" +
			"   you may not use this file except in compliance with the License." +
			"   You may obtain a copy of the License at" +
			"<br><br>" +
			"       http://www.apache.org/licenses/LICENSE-2.0" +
			"<br><br>" +
			"   Unless required by applicable law or agreed to in writing, software" +
			"   distributed under the License is distributed on an \"AS IS\" BASIS," +
			"   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied." +
			"   See the License for the specific language governing permissions and" +
			"   limitations under the License." +
			"  </code></pre>", 		"<pre><code>" +
			"   Copyright 2014 kolavar" +
			"<br><br>" +
			"   Licensed under the Apache License, Version 2.0 (the \"License\");" +
			"   you may not use this file except in compliance with the License." +
			"   You may obtain a copy of the License at" +
			"<br><br>" +
			"       http://www.apache.org/licenses/LICENSE-2.0" +
			"<br><br>" +
			"   Unless required by applicable law or agreed to in writing, software" +
			"   distributed under the License is distributed on an \"AS IS\" BASIS," +
			"   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied." +
			"   See the License for the specific language governing permissions and" +
			"   limitations under the License." +
			"  </code></pre>", 		"<pre><code>" +
			"   Copyright 2014 Soichiro Kashima" +
			"<br><br>" +
			"   Licensed under the Apache License, Version 2.0 (the \"License\");" +
			"   you may not use this file except in compliance with the License." +
			"   You may obtain a copy of the License at" +
			"<br><br>" +
			"       http://www.apache.org/licenses/LICENSE-2.0" +
			"<br><br>" +
			"   Unless required by applicable law or agreed to in writing, software" +
			"   distributed under the License is distributed on an \"AS IS\" BASIS," +
			"   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied." +
			"   See the License for the specific language governing permissions and" +
			"   limitations under the License." +
			"  </code></pre>"
		};

		for (int i = 0; i < textos.length; i++) {
			LayoutInflater textoinflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View textoview = textoinflater.inflate(R.layout.license_text, null);
			TextView texto = (TextView)textoview.findViewById(R.id.text);
			CustomTextView code = (CustomTextView)textoview.findViewById(R.id.licensetext);
			texto.setMovementMethod(LinkMovementMethod.getInstance());
			texto.setText(Html.fromHtml(textos[i], null, new TagAdapter()));
			code.setText(Html.fromHtml(licenses[i], null, new TagAdapter()));
			LinearLayout viewlicenses = (LinearLayout)view.findViewById(R.id.licenses);
			viewlicenses.addView(textoview);
		}
		return view;
	}
}
