package com.quizmael.gui.views.auth;

import com.quizmael.controller.AuthController;
import com.quizmael.model.User;
import com.quizmael.util.I18nUtil;
import com.quizmael.util.ValidationConstants;

import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.JTextField;

/**
 * Panel for new user registration.
 * Provides fields to input user details and create an account.
 * 
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class RegisterPanel extends com.quizmael.gui.common.BasePanel {
    
    // ------------------------------------------------------------
    //                     Attributes
    // ------------------------------------------------------------
    private final AuthController authController;
    
    // ------------------------------------------------------------
    //                     Public Methods
    // ------------------------------------------------------------
    
    /**
     * Creates new form LoginPanel
     * 
     * @param authController the controller handling registration logic
     */
    public RegisterPanel(AuthController authController) {
        this.authController = authController;
        initComponents();

        // TODO: hace visibles estos componentes cuando implemente su funcionalidad ----------------------------
        btnProfilePicture.setVisible(false);
    // -----------------------------------------------------------------------------------------------------
                
        // Apply input restrictions to text fields
        limitTextFieldInput(txtUserName, 20, "[a-zA-Z0-9_]");
        limitTextFieldInput(txtEmail, 40, ".");
        limitTextFieldInput(txtPassword, 20, ".");
        limitTextFieldInput(txtPasswordRepeat, 20, ".");
        limitTextFieldInput(txtPasswordHint, 100, ".");
        limitTextFieldInput(txtSecretQuestion, 100, ".");
        limitTextFieldInput(txtSecretAnswer, 100, ".");
    }
    
    
    // ------------------------------------------------------------
    //                     Private Methods
    // ------------------------------------------------------------

    private void performRegistration() {

        String username = txtUserName.getText().trim();
        // Secure password retrieval from JPasswordField
        String password = new String(txtPassword.getPassword()).trim();
        String passwordRepeat = new String(txtPasswordRepeat.getPassword()).trim();
        String secretAnswer = new String(txtSecretAnswer.getPassword()).trim();
        String email = txtEmail.getText().trim();
        String birthDateStr = txtBirthDate.getText().trim();

        // 1. Basic Validation: Check for empty mandatory fields
        if (username.isBlank() || password.isBlank() || passwordRepeat.isBlank()) {
            showError("register.error.required_fields");
            return;
        }

        // 1b. Validate Interdependent Secret Fields (Pair matching)
        boolean hasQuestion = !txtSecretQuestion.getText().trim().isEmpty();
        boolean hasAnswer = !secretAnswer.isEmpty();

        if (hasQuestion != hasAnswer) {
            showError("register.error.secret_pair");
            return;
        }

        // 2. Validate Password Match
        if (!password.equals(passwordRepeat)) {
            showError("register.error.password_mismatch");
            return;
        }

        // 3. Username format (alphanumeric, underscores, 3-20 characters)
        if (!username.matches(ValidationConstants.USERNAME_REGEX)) {
            showError("register.error.username_format");
            return;
        }

        // 4. Email format (optional, but must be valid if provided)
        if (!email.isBlank() && !email.matches(ValidationConstants.EMAIL_REGEX)) {
            showError("register.error.email_format");
            return;
        }

        // 5. Password strength
        if (!password.matches(ValidationConstants.PASSWORD_REGEX)) {
            showError("error.validation.password_strength");
            return;
        }

        // 6. Parse Birth Date
        LocalDate birthDate = null;
        if (!birthDateStr.isBlank()) {
            try {
                birthDate = LocalDate.parse(birthDateStr);
            } catch (DateTimeParseException e) {
                showError("register.error.birthdate_format");
                return;
            }
        }

        // 7. Create User Entity
        try {
            User newUser = new User();
            newUser.setName(username);
            newUser.setEmail(email);
            newUser.setPassword(password); // Service layer will hash this
            newUser.setPasswordHint(txtPasswordHint.getText().trim());
            newUser.setSecretQuestion(txtSecretQuestion.getText().trim());
            newUser.setSecretAnswer(secretAnswer);
            newUser.setBirthDate(birthDate);

            // 8. Delegate to Controller
            authController.handleRegister(newUser);

            // Registration successful notification (Optional, since AuthController navigates to Login)
            showTimedMessage("Registration successful!", "Success", 3000);

        } catch (IllegalArgumentException e) {
            showError("error.validation_prefix");
        } catch (Exception e) {
            showError("error.unexpected_prefix");
        }
    }

    /**
     * Limits input in a JTextField by maximum length and allowed characters.
     *
     * @param field the text field to restrict
     * @param maxLength the maximum number of characters allowed
     * @param allowedRegex a regex defining allowed characters (applied per keystroke)
     */
    private void limitTextFieldInput(JTextField field, int maxLength, String allowedRegex) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String currentText = field.getText();
                char keyChar = e.getKeyChar();

                // Ignore control keys like Backspace
                if (Character.isISOControl(keyChar)) {
                    return;
                }

                // Check length and allowed format
                if (currentText.length() >= maxLength || !String.valueOf(keyChar).matches(allowedRegex)) {
                    e.consume(); // Prevent input
                    Toolkit.getDefaultToolkit().beep(); // Audio feedback
                }
            }
        });
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblUserName = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        lblPasswordRepeat = new javax.swing.JLabel();
        txtPasswordRepeat = new javax.swing.JPasswordField();
        lblPasswordHint = new javax.swing.JLabel();
        txtPasswordHint = new javax.swing.JTextField();
        lblSecretQuestion = new javax.swing.JLabel();
        txtSecretQuestion = new javax.swing.JTextField();
        lblSecretAnswer = new javax.swing.JLabel();
        txtSecretAnswer = new javax.swing.JPasswordField();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblBirthDate = new javax.swing.JLabel();
        txtBirthDate = new javax.swing.JFormattedTextField();
        btnProfilePicture = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(25, 25), new java.awt.Dimension(25, 25), new java.awt.Dimension(25, 25));

        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(30, 80, 30, 80), javax.swing.BorderFactory.createEtchedBorder()));
        setMinimumSize(new java.awt.Dimension(800, 841));
        setLayout(new java.awt.GridBagLayout());

        lblUserName.setText("Nombre de usuario *");
        lblUserName.setMaximumSize(new java.awt.Dimension(300, 50));
        lblUserName.setMinimumSize(new java.awt.Dimension(50, 30));
        lblUserName.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 25, 10, 40);
        add(lblUserName, gridBagConstraints);

        txtUserName.setToolTipText("Username");
        txtUserName.setMaximumSize(new java.awt.Dimension(300, 40));
        txtUserName.setMinimumSize(new java.awt.Dimension(150, 25));
        txtUserName.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 25, 10, 40);
        add(txtUserName, gridBagConstraints);

        lblPassword.setText("Contraseña *");
        lblPassword.setMaximumSize(new java.awt.Dimension(300, 50));
        lblPassword.setMinimumSize(new java.awt.Dimension(50, 30));
        lblPassword.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 25, 10, 40);
        add(lblPassword, gridBagConstraints);

        txtPassword.setToolTipText("Password must be 6-20 characters long and include at least one uppercase letter, one lowercase letter, one number, \"                     + \"and one of the next special character: @, $, !, %, *, ?, &");
        txtPassword.setMinimumSize(new java.awt.Dimension(40, 22));
        txtPassword.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 25, 10, 40);
        add(txtPassword, gridBagConstraints);

        lblPasswordRepeat.setText("Repita la contraseña *");
        lblPasswordRepeat.setMaximumSize(new java.awt.Dimension(300, 50));
        lblPasswordRepeat.setMinimumSize(new java.awt.Dimension(50, 30));
        lblPasswordRepeat.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 25);
        add(lblPasswordRepeat, gridBagConstraints);

        txtPasswordRepeat.setToolTipText("Password must be 6-20 characters long and include at least one uppercase letter, one lowercase letter, one number, \"                     + \"and one of the next special character: @, $, !, %, *, ?, &");
        txtPasswordRepeat.setMinimumSize(new java.awt.Dimension(40, 22));
        txtPasswordRepeat.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 25);
        add(txtPasswordRepeat, gridBagConstraints);

        lblPasswordHint.setText("Pista de contraseña");
        lblPasswordHint.setMaximumSize(new java.awt.Dimension(300, 50));
        lblPasswordHint.setMinimumSize(new java.awt.Dimension(50, 30));
        lblPasswordHint.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 25, 10, 40);
        add(lblPasswordHint, gridBagConstraints);

        txtPasswordHint.setToolTipText("Password hint");
        txtPasswordHint.setMaximumSize(new java.awt.Dimension(300, 40));
        txtPasswordHint.setMinimumSize(new java.awt.Dimension(150, 25));
        txtPasswordHint.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 25, 10, 40);
        add(txtPasswordHint, gridBagConstraints);

        lblSecretQuestion.setText("Pregunta secreta");
        lblSecretQuestion.setMaximumSize(new java.awt.Dimension(300, 50));
        lblSecretQuestion.setMinimumSize(new java.awt.Dimension(50, 30));
        lblSecretQuestion.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 25, 10, 40);
        add(lblSecretQuestion, gridBagConstraints);

        txtSecretQuestion.setToolTipText("Secret Question");
        txtSecretQuestion.setMaximumSize(new java.awt.Dimension(300, 40));
        txtSecretQuestion.setMinimumSize(new java.awt.Dimension(150, 25));
        txtSecretQuestion.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 25, 10, 40);
        add(txtSecretQuestion, gridBagConstraints);

        lblSecretAnswer.setText("Respuesta secreta");
        lblSecretAnswer.setMaximumSize(new java.awt.Dimension(300, 50));
        lblSecretAnswer.setMinimumSize(new java.awt.Dimension(50, 30));
        lblSecretAnswer.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 25);
        add(lblSecretAnswer, gridBagConstraints);

        txtSecretAnswer.setToolTipText("Secret Answer");
        txtSecretAnswer.setMinimumSize(new java.awt.Dimension(40, 22));
        txtSecretAnswer.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 25);
        add(txtSecretAnswer, gridBagConstraints);

        lblEmail.setText("Correo electrónico");
        lblEmail.setMaximumSize(new java.awt.Dimension(300, 50));
        lblEmail.setMinimumSize(new java.awt.Dimension(50, 30));
        lblEmail.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 25, 10, 40);
        add(lblEmail, gridBagConstraints);

        txtEmail.setToolTipText("email");
        txtEmail.setMaximumSize(new java.awt.Dimension(300, 40));
        txtEmail.setMinimumSize(new java.awt.Dimension(150, 25));
        txtEmail.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 25, 10, 40);
        add(txtEmail, gridBagConstraints);

        lblBirthDate.setText("Fecha de nacimiento");
        lblBirthDate.setMaximumSize(new java.awt.Dimension(300, 50));
        lblBirthDate.setMinimumSize(new java.awt.Dimension(50, 30));
        lblBirthDate.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 25);
        add(lblBirthDate, gridBagConstraints);

        txtBirthDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("yyyy-MM-dd"))));
        txtBirthDate.setToolTipText("Birthdate in format: yyyy-MM-dd");
        txtBirthDate.setMaximumSize(new java.awt.Dimension(300, 40));
        txtBirthDate.setMinimumSize(new java.awt.Dimension(150, 25));
        txtBirthDate.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 25);
        add(txtBirthDate, gridBagConstraints);

        btnProfilePicture.setText("Añadir foto de perfil");
        btnProfilePicture.setMaximumSize(new java.awt.Dimension(200, 50));
        btnProfilePicture.setMinimumSize(new java.awt.Dimension(160, 45));
        btnProfilePicture.setPreferredSize(new java.awt.Dimension(160, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 25, 10, 40);
        add(btnProfilePicture, gridBagConstraints);

        btnBack.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnBack.setText("Atrás");
        btnBack.setMaximumSize(new java.awt.Dimension(200, 50));
        btnBack.setMinimumSize(new java.awt.Dimension(160, 45));
        btnBack.setPreferredSize(new java.awt.Dimension(160, 45));
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 25, 25, 40);
        add(btnBack, gridBagConstraints);

        btnRegister.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnRegister.setText("Registrarse");
        btnRegister.setMaximumSize(new java.awt.Dimension(200, 50));
        btnRegister.setMinimumSize(new java.awt.Dimension(160, 45));
        btnRegister.setPreferredSize(new java.awt.Dimension(160, 45));
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 25, 25);
        add(btnRegister, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        performRegistration();
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        authController.navigateToLogin();
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnProfilePicture;
    private javax.swing.JButton btnRegister;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel lblBirthDate;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPasswordHint;
    private javax.swing.JLabel lblPasswordRepeat;
    private javax.swing.JLabel lblSecretAnswer;
    private javax.swing.JLabel lblSecretQuestion;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JFormattedTextField txtBirthDate;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPasswordHint;
    private javax.swing.JPasswordField txtPasswordRepeat;
    private javax.swing.JPasswordField txtSecretAnswer;
    private javax.swing.JTextField txtSecretQuestion;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
}