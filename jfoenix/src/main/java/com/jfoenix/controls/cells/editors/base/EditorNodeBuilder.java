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

package com.jfoenix.controls.cells.editors.base;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;

/**
 * <h1>Editor Builder</h1>
 * this a builder interface to create editors for treetableview/tableview cells
 * <p>
 *
 * @author Shadi Shaheen
 * @version 1.0
 * @since 2016-03-09
 */
public interface EditorNodeBuilder<T> {
    /**
     * This method is called when the editor start editing the cell
     *
     * @return Nothing
     */
    void startEdit();

    /**
     * This method is called when the editor cancel editing the cell
     *
     * @return Nothing
     */
    void cancelEdit();

    /**
     * This method is called when the editor updates the visuals of the cell
     *
     * @param item the new item for the cell
     * @param empty whether or not this cell holds a value
     * @return Nothing
     */
    void updateItem(T item, boolean empty);

    /**
     * This method is will create the editor node to be displayed when
     * editing the cell
     *
     * @param value               current value of the cell
     * @param keyEventsHandler    keyboard events handler for the cell
     * @param focusChangeListener focus change listener for the cell
     * @return the editor node
     */
    Region createNode(T value, EventHandler<KeyEvent> keyEventsHandler, ChangeListener<Boolean> focusChangeListener);

    /**
     * This method is used to update the editor node to corresponde with
     * the new value of the cell
     *
     * @param value the new value of the cell
     * @return Nothing
     */
    void setValue(T value);

    /**
     * This method is used to get the current value from the editor node
     *
     * @return T the value of the editor node
     */
    T getValue();

    /**
     * This method will be called before committing the new value of the cell
     *
     * @return Nothing
     * @throws Exception
     */
    void validateValue() throws Exception;

    /**
     * set ui nodes to null for memory efficiency
     */
    void nullEditorNode();
}
