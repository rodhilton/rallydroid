/*
 * Copyright 2010 Rally Software Development
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

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DomainObject implements Serializable {
	private Integer oid;
	private String creationDate;
	protected JSONObject object;
	
	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public DomainObject() {
		super();
	}

	public DomainObject(JSONObject object) {
		this.object = object;
		try {
			this.creationDate = object.getString("CreationDate");
			this.oid = object.getInt("ObjectID");
		} catch (JSONException e) {
			throw new IllegalArgumentException(
					"Object not correctly populated", e);
		}
	}

	public String getString(String name) {
		try {
			return object.getString(name);
		} catch (JSONException e) {
			Log.e("json", name + " is not a valid property");
			return "";
		}
	}

	public String toString() {
		return String.format("%s: %s", oid, creationDate);
	}


	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * 
	 * Maintainers must change this value if and only if the new version of this
	 * class is not compatible with old versions. See Sun docs for <a
	 * href=http://java.sun.com/products/jdk/1.1/docs/guide
	 * /serialization/spec/version.doc.html> details. </a>
	 * 
	 * Not necessary to include in first version of the class, but included here
	 * as a reminder of its importance.
	 */
	private static final long serialVersionUID = 7526471155622776145L;
}
