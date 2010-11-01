/*
 * Copyright 2000-2009 JetBrains s.r.o.
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
package com.intellij.openapi.roots.ui.configuration.libraryEditor;

import com.intellij.ide.util.treeView.AbstractTreeStructure;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.ui.configuration.OrderRootTypeUIFactory;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class LibraryTreeStructure extends AbstractTreeStructure {
  private final Object myRootElement;
  private final NodeDescriptor myRootElementDescriptor;
  private final LibraryRootsComponent myParentEditor;

  public LibraryTreeStructure(LibraryRootsComponent parentElement) {
    myParentEditor = parentElement;
    myRootElement = new Object();
    myRootElementDescriptor = new NodeDescriptor(null, null) {
      public boolean update() {
        myName = ProjectBundle.message("library.root.node");
        return false;
      }
      public Object getElement() {
        return myRootElement;
      }
    };
  }

  public Object getRootElement() {
    return myRootElement;
  }

  public Object[] getChildElements(Object element) {
    if (element == myRootElement) {
      ArrayList<LibraryTableTreeContentElement> elements = new ArrayList<LibraryTableTreeContentElement>(3);
      final LibraryEditor parentEditor = myParentEditor.getLibraryEditor();
      for (OrderRootType type : OrderRootType.getAllTypes()) {
        final String[] urls = parentEditor.getUrls(type);
        if (urls.length > 0) {
          elements.add(OrderRootTypeUIFactory.FACTORY.getByKey(type).createElement());
        }
      }
      return elements.toArray();
    }

    if (element instanceof OrderRootTypeElement) {
      OrderRootTypeElement rootTypeElement = (OrderRootTypeElement)element;
      OrderRootType orderRootType = rootTypeElement.getOrderRootType();
      ArrayList<ItemElement> items = new ArrayList<ItemElement>();
      final LibraryEditor libraryEditor = myParentEditor.getLibraryEditor();
      final String[] urls = libraryEditor.getUrls(orderRootType).clone();
      Arrays.sort(urls, LibraryRootsComponent.ourUrlComparator);
      for (String url : urls) {
        items.add(new ItemElement(rootTypeElement, url, orderRootType, libraryEditor.isJarDirectory(url), libraryEditor.isValid(url, orderRootType)));
      }
      return items.toArray();
    }
    return ArrayUtil.EMPTY_OBJECT_ARRAY;
  }

  public void commit() {
  }

  public boolean hasSomethingToCommit() {
    return false;
  }

  public Object getParentElement(Object element) {
    Object rootElement = getRootElement();
    if (element == rootElement) {
      return null;
    }
    if (element instanceof ItemElement) {
      return ((ItemElement)element).getParent();
    }
    return rootElement;
  }

  @NotNull
  public NodeDescriptor createDescriptor(Object element, NodeDescriptor parentDescriptor) {
    if (element == myRootElement) {
      return myRootElementDescriptor;
    }
    if (element instanceof LibraryTableTreeContentElement) {
      return ((LibraryTableTreeContentElement)element).createDescriptor(parentDescriptor, myParentEditor);
    }
    return null;
  }
}
