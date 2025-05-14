package com.quizmael.gui.views.auth;

import com.quizmael.controller.AppController;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    private final AppController controller;
    
    // ------------------------------------------------------------
    //                     Public Methods
    // ------------------------------------------------------------
    
    /**
     * Creates new form LoginPanel
     * 
     * @param controller the application controller
     */
    public RegisterPanel(AppController controller) {
        this.controller = controller;
        initComponents();
        
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
        String password = txtPassword.getText().trim();
        String passwordRepeat = txtPasswordRepeat.getText().trim();
        String email = txtEmail.getText().trim();

        // Check for empty fields
        if (username.isBlank() || password.isBlank() || passwordRepeat.isBlank()) {
            showError("All fields marked with * are required.", "Validation Error");
            return;
        }

        // Username format (alphanumeric, underscores, 3-20 characters)
        if (!username.matches("^[a-zA-Z0-9_]{3,20}$")) {
            showError( "Username must be 3-20 characters and only letters, numbers or underscores.", "Validation Error");
            return;
        }

        // Email format (optional, but must be valid if provided)
        
        if (!email.isBlank() && !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            showError("Invalid email format.", "Validation Error");
            return;
        }

        // Password must be 6-20 characters long, and include at least one uppercase letter, one lowercase letter, one number, and one special character.
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}$")) {
            showError("Password must be 6-20 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.", "Validation Error");
            return;
        }

        try {
            controller.getAuthController().registerUser(
                username.trim(),
                email.trim(),
                password.trim(),
                passwordRepeat.trim(),
                txtPasswordHint.getText().trim(),
                txtSecretQuestion.getText().trim(),
                txtSecretAnswer.getText().trim(),
                txtBirthDate.getText().trim()
            );
        } catch (IllegalArgumentException e) {
            switch (e.getMessage()) {
                case "Username already exists" -> showError("That username is already taken. Please choose another.", "Username Exists");
                case "Passwords do not match." -> showError("Passwords do not match.", "Validation Error");
                case "Invalid birth date format." -> showError("Please enter a valid birth date in the format yyyy-MM-dd.", "Validation Error");
                default -> showError("Registration or validation error: " + e.getMessage(), "Error");
            }
        } catch (Exception e) {
            showError("Unexpected error during registration: " + e.getMessage(), "Error");
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

                // Ignorar teclas de control (como borrar)
                if (Character.isISOControl(keyChar)) {
                    return;
                }

                // Comprobar longitud y formato permitido
                if (currentText.length() >= maxLength || !String.valueOf(keyChar).matches(allowedRegex)) {
                    e.consume(); // Evita que se escriba
                    Toolkit.getDefaultToolkit().beep(); // Feedback sonoro
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
        lblProfilePicture = new javax.swing.JLabel();
        btnProfilePicture = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 60, 25, 60));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setLayout(new java.awt.GridBagLayout());

        lblUserName.setText("Nombre de usuario *");
        lblUserName.setMaximumSize(new java.awt.Dimension(300, 50));
        lblUserName.setMinimumSize(new java.awt.Dimension(50, 30));
        lblUserName.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 40);
        add(lblUserName, gridBagConstraints);

        txtUserName.setMaximumSize(new java.awt.Dimension(300, 40));
        txtUserName.setMinimumSize(new java.awt.Dimension(150, 25));
        txtUserName.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 40);
        add(txtUserName, gridBagConstraints);

        lblPassword.setText("Contraseña *");
        lblPassword.setMaximumSize(new java.awt.Dimension(300, 50));
        lblPassword.setMinimumSize(new java.awt.Dimension(50, 30));
        lblPassword.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 40);
        add(lblPassword, gridBagConstraints);

        txtPassword.setMinimumSize(new java.awt.Dimension(40, 22));
        txtPassword.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 40);
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
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        add(lblPasswordRepeat, gridBagConstraints);

        txtPasswordRepeat.setMinimumSize(new java.awt.Dimension(40, 22));
        txtPasswordRepeat.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        add(txtPasswordRepeat, gridBagConstraints);

        lblPasswordHint.setText("Pista de contraseña");
        lblPasswordHint.setMaximumSize(new java.awt.Dimension(300, 50));
        lblPasswordHint.setMinimumSize(new java.awt.Dimension(50, 30));
        lblPasswordHint.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 40);
        add(lblPasswordHint, gridBagConstraints);

        txtPasswordHint.setMaximumSize(new java.awt.Dimension(300, 40));
        txtPasswordHint.setMinimumSize(new java.awt.Dimension(150, 25));
        txtPasswordHint.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 40);
        add(txtPasswordHint, gridBagConstraints);

        lblSecretQuestion.setText("Pregunta secreta");
        lblSecretQuestion.setMaximumSize(new java.awt.Dimension(300, 50));
        lblSecretQuestion.setMinimumSize(new java.awt.Dimension(50, 30));
        lblSecretQuestion.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 40);
        add(lblSecretQuestion, gridBagConstraints);

        txtSecretQuestion.setMaximumSize(new java.awt.Dimension(300, 40));
        txtSecretQuestion.setMinimumSize(new java.awt.Dimension(150, 25));
        txtSecretQuestion.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 40);
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
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        add(lblSecretAnswer, gridBagConstraints);

        txtSecretAnswer.setMinimumSize(new java.awt.Dimension(40, 22));
        txtSecretAnswer.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        add(txtSecretAnswer, gridBagConstraints);

        lblEmail.setText("Correo electrónico");
        lblEmail.setMaximumSize(new java.awt.Dimension(300, 50));
        lblEmail.setMinimumSize(new java.awt.Dimension(50, 30));
        lblEmail.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 40);
        add(lblEmail, gridBagConstraints);

        txtEmail.setMaximumSize(new java.awt.Dimension(300, 40));
        txtEmail.setMinimumSize(new java.awt.Dimension(150, 25));
        txtEmail.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 40);
        add(txtEmail, gridBagConstraints);

        lblBirthDate.setText("Fecha de nacimiento");
        lblBirthDate.setMaximumSize(new java.awt.Dimension(300, 50));
        lblBirthDate.setMinimumSize(new java.awt.Dimension(50, 30));
        lblBirthDate.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        add(lblBirthDate, gridBagConstraints);

        txtBirthDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("yyyy-MM-dd"))));
        txtBirthDate.setToolTipText("Formato: yyyy-MM-dd");
        txtBirthDate.setMaximumSize(new java.awt.Dimension(300, 40));
        txtBirthDate.setMinimumSize(new java.awt.Dimension(150, 25));
        txtBirthDate.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        add(txtBirthDate, gridBagConstraints);

        lblProfilePicture.setText("Foto de perfil");
        lblProfilePicture.setMaximumSize(new java.awt.Dimension(300, 50));
        lblProfilePicture.setMinimumSize(new java.awt.Dimension(50, 30));
        lblProfilePicture.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        add(lblProfilePicture, gridBagConstraints);

        btnProfilePicture.setText("Añadir foto de perfil");
        btnProfilePicture.setMaximumSize(new java.awt.Dimension(300, 40));
        btnProfilePicture.setMinimumSize(new java.awt.Dimension(150, 25));
        btnProfilePicture.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        add(btnProfilePicture, gridBagConstraints);

        btnBack.setText("Atrás");
        btnBack.setMaximumSize(new java.awt.Dimension(200, 50));
        btnBack.setMinimumSize(new java.awt.Dimension(150, 25));
        btnBack.setPreferredSize(new java.awt.Dimension(150, 25));
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        add(btnBack, gridBagConstraints);

        btnRegister.setText("Registrarse");
        btnRegister.setMaximumSize(new java.awt.Dimension(200, 50));
        btnRegister.setMinimumSize(new java.awt.Dimension(150, 25));
        btnRegister.setPreferredSize(new java.awt.Dimension(150, 25));
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        add(btnRegister, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        performRegistration();
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        controller.getAuthController().goBackToLogin();
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnProfilePicture;
    private javax.swing.JButton btnRegister;
    private javax.swing.JLabel lblBirthDate;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPasswordHint;
    private javax.swing.JLabel lblPasswordRepeat;
    private javax.swing.JLabel lblProfilePicture;
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