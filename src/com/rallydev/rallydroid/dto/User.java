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
  
package com.rallydev.rallydroid.dto;

import java.io.Serializable;

public class User implements Serializable {
	public String getDisplayName() {
		return displayName;
	}
	public String getSubscriptionName() {
		return subscriptionName;
	}
	public User(String displayName, String subscriptionName) {
		super();
		this.displayName = displayName;
		this.subscriptionName = subscriptionName;
	}

	private String displayName;
	private String subscriptionName;
	
	/**
	   * Determines if a de-serialized file is compatible with this class.
	   *
	   * Maintainers must change this value if and only if the new version
	   * of this class is not compatible with old versions. See Sun docs
	   * for <a href=http://java.sun.com/products/jdk/1.1/docs/guide
	   * /serialization/spec/version.doc.html> details. </a>
	   *
	   * Not necessary to include in first version of the class, but
	   * included here as a reminder of its importance.
	   */
	private static final long serialVersionUID = 7526471155622776149L;
}
