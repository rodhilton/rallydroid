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

public class Artifact implements Serializable {
	public Artifact(String type, String ref, String formattedID, Integer oid) {
		super();
		this.type = type;
		this.ref = ref;
		this.formattedID = formattedID;
		this.oid = oid;
	}
	public String getFormattedID() {
		return formattedID;
	}
	public String getRef() {
		return ref;
	}
	public Integer getOid() {
		return oid;
	}
	public String getType() {
		return type;
	}
	private String formattedID;
	private String ref;
	private Integer oid;
	private String type;
}
