/*
 * Copyright 2000-2010 JetBrains s.r.o.
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
package org.jetbrains.plugins.github.ui;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.HyperlinkAdapter;
import org.jetbrains.plugins.github.GithubUtil;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author oleg
 * @date 10/20/10
 */
public class GithubSettingsPanel {
  private static final String SIGNUP_FOR_FREE_ACOUNT = "https://github.com/signup/free";

  private JTextField myLoginTextField;
  private JPasswordField myPasswordField;
  private JTextPane mySignupTextField;
  private JPanel myPane;
  private JButton myTestButton;

  public GithubSettingsPanel() {
    mySignupTextField.addHyperlinkListener(new HyperlinkAdapter() {
      @Override
      protected void hyperlinkActivated(final HyperlinkEvent e) {
        BrowserUtil.launchBrowser(e.getURL().toExternalForm());
      }
    });
    mySignupTextField.setText(
      "<html>Do not have an account? <a href=\"" + SIGNUP_FOR_FREE_ACOUNT + "\">" + "Signup for free" + "</a></html>");
    mySignupTextField.setBackground(myPane.getBackground());
    mySignupTextField.setCursor(new Cursor(Cursor.HAND_CURSOR));
    myTestButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        final boolean result = GithubUtil.testConnection(getLogin(), getPassword());
        Messages.showInfoMessage(result ? "Connection successful" : "Cannot login using given credentials", result ? "Success" : "Fail");
      }
    });
  }

  public JComponent getPanel() {
    return myPane;
  }

  public void setLogin(final String login) {
    myLoginTextField.setText(login);
  }

  public void setPassword(final String password) {
    myPasswordField.setText(password);
  }

  public String getLogin() {
    return myLoginTextField.getText();
  }

  public String getPassword() {
    return String.valueOf(myPasswordField.getPassword());
  }

  public JComponent getPreferrableFocusComponent() {
    return myLoginTextField;
  }
}

