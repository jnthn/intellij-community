/*
 * Copyright 2000-2011 JetBrains s.r.o.
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
package com.intellij.cvsSupport2.ui.experts.importToCvs;

import com.intellij.cvsSupport2.config.ImportConfiguration;
import com.intellij.cvsSupport2.cvsBrowser.CvsElement;
import com.intellij.cvsSupport2.cvsoperations.cvsImport.ImportDetails;
import com.intellij.cvsSupport2.ui.experts.CvsWizard;
import com.intellij.cvsSupport2.ui.experts.SelectCVSConfigurationStep;
import com.intellij.cvsSupport2.ui.experts.SelectCvsElementStep;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.CvsBundle;

import javax.swing.tree.TreeSelectionModel;
import java.io.File;

/**
 * author: lesya
 */
public class ImportWizard extends CvsWizard {
  private final SelectCVSConfigurationStep mySelectCVSConfigurationStep;
  private final SelectCvsElementStep mySelectCvsElementStep;
  private final SelectImportLocationStep mySelectImportLocationStep;
  private final CustomizeKeywordSubstitutionStep myKeywordSubstitutionStep;
  private final ImportSettingsStep mySettingsStep;

  public ImportWizard(Project project, VirtualFile selectedFile) {
    super(CvsBundle.message("dialog.title.import.into.cvs"), project);
    ImportConfiguration importConfig = ImportConfiguration.getInstance();
    mySelectCVSConfigurationStep = new SelectCVSConfigurationStep(project, this);
    mySelectCvsElementStep = new SelectCvsElementStep(CvsBundle.message("dialog.title.select.directory.to.import.into"),this,
                                                      project,
                                                      mySelectCVSConfigurationStep, true, TreeSelectionModel.SINGLE_TREE_SELECTION, false,
                                                      false);

    mySelectImportLocationStep = new SelectImportLocationStep(
                                            CvsBundle.message("dialog.title.select.import.directory"),
                                             this,
                                            project,
                                            selectedFile);

    myKeywordSubstitutionStep = new CustomizeKeywordSubstitutionStep(CvsBundle.message("dialog.title.customize.keyword.substitutions"),
                this, importConfig);

    mySettingsStep = new ImportSettingsStep(this, mySelectImportLocationStep, importConfig);

    addStep(mySelectCVSConfigurationStep);
    addStep(mySelectCvsElementStep);
    addStep(mySelectImportLocationStep);
    addStep(myKeywordSubstitutionStep);
    addStep(mySettingsStep);

    init();
  }

  public ImportDetails createImportDetails() {
    CvsElement module = mySelectCvsElementStep.getSelectedCvsElement();
    String moduleName = mySettingsStep.getModuleName();
    String importModuleName = module.getElementPath().equals(".") ? moduleName : module.getElementPath() + "/" + moduleName;

    final File selectedFile = mySelectImportLocationStep.getSelectedFile();
    if (selectedFile == null) return null;
    return new ImportDetails(selectedFile,
                             mySettingsStep.getVendor(),
                             mySettingsStep.getReleaseTag(),
                             mySettingsStep.getLogMessage(),
                             importModuleName,
                             mySelectCVSConfigurationStep.getSelectedConfiguration(),
                             myKeywordSubstitutionStep.getFileExtensions(),
                             mySelectImportLocationStep.getIgnoreFileFilter());
  }

  protected String getHelpID() {
    return "cvs.import";
  }
}
