/*
 * Copyright 2000-2010 JetBrains s.r.o.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.openapi.roots.libraries.ui;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author nik
 */
public abstract class AttachRootButtonDescriptor {
  private final OrderRootType myOrderRootType;
  protected final String myButtonText;

  protected AttachRootButtonDescriptor(@NotNull OrderRootType orderRootType, @NotNull String buttonText) {
    myOrderRootType = orderRootType;
    myButtonText = buttonText;
  }

  public abstract VirtualFile[] selectFiles(@NotNull JComponent parent, @Nullable VirtualFile initialSelection,
                                            @Nullable Module contextModule, @Nullable String libraryName);

  public String getButtonText() {
    return myButtonText;
  }

  public OrderRootType getRootType() {
    return myOrderRootType;
  }

  public boolean addAsJarDirectories() {
    return false;
  }

  @NotNull
  public VirtualFile[] scanForActualRoots(@NotNull VirtualFile[] rootCandidates, JComponent parent) {
    return rootCandidates;
  }
}
