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

package com.jfoenix.skins;

import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sun.javafx.scene.control.skin.TreeTableCellSkin;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;

/**
 * @author Shadi Shaheen
 * @version 1.0
 * @since 2016-03-09
 */
public class JFXTreeTableCellSkin<S, T>  extends TreeTableCellSkin<S, T> {

    public JFXTreeTableCellSkin(TreeTableCell<S, T> treeTableCell) {
        super(treeTableCell);
    }

    @Override
    protected void updateChildren() {
        super.updateChildren();
        updateDisclosureNode();
    }

    private void updateDisclosureNode() {
        Node disclosureNode = ((JFXTreeTableCell<S, T>) getSkinnable()).getDisclosureNode();
        if (disclosureNode != null) {
            TreeItem<S> item = getSkinnable().getTreeTableRow().getTreeItem();
            final S value = item == null ? null : item.getValue();
            boolean disclosureVisible = value != null
                                        && !item.isLeaf()
                                        && value instanceof RecursiveTreeObject
                                        && ((RecursiveTreeObject) value).getGroupedColumn() == getSkinnable().getTableColumn();
            disclosureNode.setVisible(disclosureVisible);
            if (!disclosureVisible) {
                getChildren().remove(disclosureNode);
            } else if (disclosureNode.getParent() == null) {
                getChildren().add(disclosureNode);
                disclosureNode.toFront();
            } else {
                disclosureNode.toBack();
            }
            if (disclosureNode.getScene() != null) {
                disclosureNode.applyCss();
            }
        }
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        updateDisclosureNode();
        double disclosureWidth = 0;
        Node disclosureNode = ((JFXTreeTableCell<S, T>) getSkinnable()).getDisclosureNode();
        if (disclosureNode.isVisible()) {
            Pos alignment = getSkinnable().getAlignment();
            alignment = alignment == null ? Pos.CENTER_LEFT : alignment;
            layoutInArea(disclosureNode, x + 8, y, w, h, 0, Insets.EMPTY, false, false, HPos.LEFT, VPos.CENTER);
            disclosureWidth = disclosureNode.getLayoutBounds().getWidth() + 18;
        }
        super.layoutChildren(x + disclosureWidth, y, w - disclosureWidth, h);
    }
}
