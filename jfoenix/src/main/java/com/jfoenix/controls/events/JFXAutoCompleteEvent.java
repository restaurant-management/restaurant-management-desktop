/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.jfoenix.controls.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * @author Shadi Shaheen
 * @version 1.0.0
 * @since 2018-02-01
 */
public class JFXAutoCompleteEvent<T> extends Event {

	private T object;

	public JFXAutoCompleteEvent(EventType<? extends Event> eventType, T object) {
		super(eventType);
		this.object = object;
	}

	public T getObject(){
		return object;
	}

	//TODO: more events to be added
	public static final EventType<JFXAutoCompleteEvent> SELECTION =
			new EventType<JFXAutoCompleteEvent>(Event.ANY, "JFX_AUTOCOMPLETE_SELECTION");
}
