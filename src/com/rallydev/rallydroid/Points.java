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

public enum Points {
	ZERO {
		public int getImageId() { return R.drawable.card0; }
	},
	ONE {
		public int getImageId() { return R.drawable.card1; }
	},
	TWO {
		public int getImageId() { return R.drawable.card2; }
	},
	THREE {
		public int getImageId() { return R.drawable.card3; }
	},
	FIVE {
		public int getImageId() { return R.drawable.card5; }
	},
	EIGHT {
		public int getImageId() { return R.drawable.card8; }
	},
	THIRTEEN {
		public int getImageId() { return R.drawable.card13; }
	},
	TWENTY {
		public int getImageId() { return R.drawable.card20; }
	},
	FORTY {
		public int getImageId() { return R.drawable.card40; }
	},
	HUNDRED {
		public int getImageId() { return R.drawable.card100; }
	},
	QUESTION {
		public int getImageId() { return R.drawable.cardq; }
	};
	
	public abstract int getImageId();
}
