/*
 * Copyright 2009 Rally Software Development
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
  
package com.rallydev.rallydroid;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PokerSelection extends Activity {
	/** Called when the activity is first created. */

	private static Map<Integer, Points> mapper = new HashMap<Integer, Points>();
	static {
		mapper.put(R.id.button_zero, Points.ZERO);
		mapper.put(R.id.button_one, Points.ONE);
		mapper.put(R.id.button_two, Points.TWO);
		mapper.put(R.id.button_three, Points.THREE);
		mapper.put(R.id.button_five, Points.FIVE);
		mapper.put(R.id.button_eight, Points.EIGHT);
		mapper.put(R.id.button_thirteen, Points.THIRTEEN);
		mapper.put(R.id.button_twenty, Points.TWENTY);
		mapper.put(R.id.button_forty, Points.FORTY);
		mapper.put(R.id.button_hundred, Points.HUNDRED);
		mapper.put(R.id.button_question, Points.QUESTION);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_list);

		ClickListener clickListener = new ClickListener(this);

		for (int id : mapper.keySet()) {
			Button button = (Button) findViewById(id);
			button.setOnClickListener(clickListener);
		}
	}

	private class ClickListener implements View.OnClickListener {
		private Activity parent;

		public ClickListener(Activity parent) {
			this.parent = parent;
		}

		public void onClick(View v) {
			Intent intent = new Intent(parent, PokerDisplay.class);
			intent.putExtra("Points", mapper.get(v.getId()));
			startActivity(intent);

		}
	}
}