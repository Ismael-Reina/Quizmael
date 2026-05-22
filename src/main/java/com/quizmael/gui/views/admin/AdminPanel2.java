package com.quizmael.gui.views.admin;

import com.quizmael.controller.QuizTestController;
import com.quizmael.controller.UserController;
import com.quizmael.model.QuizTest;
import com.quizmael.model.User;
import com.quizmael.model.enums.Role;
import com.quizmael.model.enums.State;
import com.quizmael.util.I18nUtil;
import com.quizmael.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 * Administrative panel for managing users, content, and system configurations.
 * Only accessible to users with administrator privileges.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class AdminPanel2 extends com.quizmael.gui.common.BasePanel {
    
    // ------------------------------------------------------------
    //                     Attributes
    // ------------------------------------------------------------
    private final QuizTestController quizTestController;
    private final UserController userController;

    private DefaultTableModel tableModel;
    private List<User> cachedUsers = new ArrayList<>();
    private List<QuizTest> cachedTests = new ArrayList<>();
    private boolean isViewingUsers = true;


    // ------------------------------------------------------------
    //                     Public Methods
    // ------------------------------------------------------------

    /**
     * Creates new form LoginPanel
     */
    public AdminPanel2(QuizTestController quizTestController, UserController UserController) {
        this.quizTestController = quizTestController;
        this.userController = UserController;

        initComponents();

        // Table configuration
        initCustomTableConfiguration();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // the administrator can only select one row

        // Attach selection listener to toggle action buttons dynamically
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleTableSelection(table.getSelectedRow());
            }
        });

        // Load default administrative view on startup
        tbtUsers.setSelected(true);
        switchToUsersView();
    }

    // ------------------------------------------------------------
    //                     Private Methods
    // ------------------------------------------------------------

    /**
     * Extracts the TableModel from the GUI Builder component and configures headers.
     * Overrides default NetBeans properties to allow dynamic column counts safely.
     */
    private void initCustomTableConfiguration() {
        // Create a fresh dynamic model to bypass NetBeans hardcoded design-time arrays
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Administrative grid records should remain read-only by default
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Safeguard lookup to dynamically handle variable column index scales safely
                if (getRowCount() > 0 && getValueAt(0, columnIndex) != null) {
                    return getValueAt(0, columnIndex).getClass();
                }
                return Object.class;
            }
        };

        // Bind the clean independent model back to our UI table component
        table.setModel(tableModel);
    }

    /**
     * Switches the primary table view to display system users.
     */
    private void switchToUsersView() {
        isViewingUsers = true;
        tbtTests.setSelected(false);
        LoggerUtil.info(getClass(), "Admin requested system user management view.");

        // Update table headers dynamically for users
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new Object[]{ "ID", "Username", "Email", "Role"});

        // Re-adjust column widths for user data profiling
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);   // ID
        columnModel.getColumn(1).setPreferredWidth(200);  // Username
        columnModel.getColumn(2).setPreferredWidth(300);  // Email
        columnModel.getColumn(3).setPreferredWidth(120);  // Role

        // Update sidebar button text context for users
        btnChangeRole.setVisible(true);
        btnChangeState.setVisible(false);
        btnChangePassword.setVisible(true);

        refreshUsersTable();
    }

    /**
     * Switches the primary table view to display public quizzes.
     */
    private void switchToTestsView() {
        isViewingUsers = false;
        tbtUsers.setSelected(false);
        LoggerUtil.info(getClass(), "Admin requested community quiz moderation view.");

        // Update table headers dynamically for quizzes
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new Object[]{ "ID", "Title", "Topic", "Author", "Language", "Status" });

        // Re-adjust column widths for quiz metadata profiling
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);   // ID
        columnModel.getColumn(1).setPreferredWidth(250);  // Title
        columnModel.getColumn(2).setPreferredWidth(150);  // Topic
        columnModel.getColumn(3).setPreferredWidth(120);  // Author
        columnModel.getColumn(4).setPreferredWidth(80);   // Language
        columnModel.getColumn(5).setPreferredWidth(100);  // Status

        // Hide user-specific action buttons to avoid context cross-contamination
        btnChangeRole.setVisible(false);
        btnChangeState.setVisible(true);
        btnChangePassword.setVisible(false);

        refreshTestsTable();
    }

    /**
     * Fetches fresh user data from backend and updates the UI grid.
     */
    private void refreshUsersTable() {
        tableModel.setRowCount(0);
        try {
            // Fetching all registered accounts from the controller
            cachedUsers = userController.findAllUsers();
            for (User u : cachedUsers) {
                tableModel.addRow(new Object[]{
                        u.getId(),
                        u.getName(),
                        u.getEmail() != null ? u.getEmail() : "N/A",
                        u.getRole()
                });
            }
        } catch (Exception e) {
            LoggerUtil.error(getClass(), "Failed to refresh admin users grid view", e);
            showError("admin.error.load_users");
        }
    }

    /**
     * Fetches fresh quiz data from backend and updates the UI grid.
     */
    private void refreshTestsTable() {
        tableModel.setRowCount(0);
        try {
            // Fetching all community tests from the controller
            cachedTests = quizTestController.findAllTests();
            for (QuizTest q : cachedTests) {
                tableModel.addRow(new Object[]{
                        q.getId(),
                        q.getTitle(),
                        q.getTopicsAsString(),
                        q.getCreator() != null ? q.getCreator().getName() : "Anonymous",
                        q.getLanguage(),
                        q.getState()
                });
            }
        } catch (Exception e) {
            LoggerUtil.error(getClass(), "Failed to refresh admin quizzes grid view", e);
            showError("admin.error.load_quizzes");
        }
    }

    /**
     * Toggles action button enablement dynamically based on active row selections
     * and the current contextual view tab (Users vs Quizzes).
     *
     * @param selectedRow the visually selected row index, or -1 if selection is empty.
     */
    private void handleTableSelection(int selectedRow) {
        boolean hasSelection = (selectedRow >= 0);

        if (isViewingUsers) {
            // Enable user-specific operations if a row is selected
            btnChangePassword.setEnabled(hasSelection);
            btnChangeRole.setEnabled(hasSelection);
            btnDelete.setEnabled(hasSelection);
            btnChangeState.setEnabled(false); // Always disabled in user context
        } else {
            // Enable quiz-specific operations if a row is selected
            btnChangePassword.setEnabled(false); // Always disabled in quiz context
            btnChangeRole.setEnabled(false);     // Always disabled in quiz context
            btnDelete.setEnabled(hasSelection);
            btnChangeState.setEnabled(hasSelection);
        }
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        tbtUsers = new javax.swing.JToggleButton();
        tbtTests = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        scrollPanel = new javax.swing.JScrollPane();
        list = new javax.swing.JList<>();
        btnChangeState = new javax.swing.JButton();
        btnChangePassword = new javax.swing.JButton();
        btnChangeRole = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnMainMenu = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setLayout(new java.awt.GridBagLayout());

        buttonGroup1.add(tbtUsers);
        tbtUsers.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        tbtUsers.setText("Usuarios");
        tbtUsers.setMaximumSize(new java.awt.Dimension(300, 90));
        tbtUsers.setMinimumSize(new java.awt.Dimension(300, 90));
        tbtUsers.setPreferredSize(new java.awt.Dimension(300, 90));
        tbtUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbtUsersActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 30, 30);
        add(tbtUsers, gridBagConstraints);

        buttonGroup1.add(tbtTests);
        tbtTests.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        tbtTests.setText("Tests");
        tbtTests.setMaximumSize(new java.awt.Dimension(300, 90));
        tbtTests.setMinimumSize(new java.awt.Dimension(300, 90));
        tbtTests.setPreferredSize(new java.awt.Dimension(300, 90));
        tbtTests.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbtTestsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 30, 0);
        add(tbtTests, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(300, 500));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 500));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", "", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(jScrollPane1, gridBagConstraints);

        scrollPanel.setMinimumSize(new java.awt.Dimension(300, 500));
        scrollPanel.setPreferredSize(new java.awt.Dimension(300, 500));

        scrollPanel.setViewportView(list);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(scrollPanel, gridBagConstraints);

        btnChangeState.setFont(new java.awt.Font("Dialog", 0, 26)); // NOI18N
        btnChangeState.setText("Cambiar estado");
        btnChangeState.setEnabled(false);
        btnChangeState.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnChangeState.setMaximumSize(new java.awt.Dimension(400, 200));
        btnChangeState.setMinimumSize(new java.awt.Dimension(300, 80));
        btnChangeState.setPreferredSize(new java.awt.Dimension(300, 80));
        btnChangeState.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeStateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        add(btnChangeState, gridBagConstraints);

        btnChangePassword.setFont(new java.awt.Font("Dialog", 0, 26)); // NOI18N
        btnChangePassword.setText("Cambiar contraseña");
        btnChangePassword.setEnabled(false);
        btnChangePassword.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnChangePassword.setMaximumSize(new java.awt.Dimension(400, 200));
        btnChangePassword.setMinimumSize(new java.awt.Dimension(300, 80));
        btnChangePassword.setPreferredSize(new java.awt.Dimension(300, 80));
        btnChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePasswordActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        add(btnChangePassword, gridBagConstraints);

        btnChangeRole.setFont(new java.awt.Font("Dialog", 0, 26)); // NOI18N
        btnChangeRole.setText("Cambiar rol");
        btnChangeRole.setEnabled(false);
        btnChangeRole.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnChangeRole.setMaximumSize(new java.awt.Dimension(400, 200));
        btnChangeRole.setMinimumSize(new java.awt.Dimension(300, 80));
        btnChangeRole.setPreferredSize(new java.awt.Dimension(300, 80));
        btnChangeRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeRoleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        add(btnChangeRole, gridBagConstraints);

        btnDelete.setFont(new java.awt.Font("Dialog", 0, 26)); // NOI18N
        btnDelete.setText("Eliminar");
        btnDelete.setEnabled(false);
        btnDelete.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnDelete.setMaximumSize(new java.awt.Dimension(400, 200));
        btnDelete.setMinimumSize(new java.awt.Dimension(300, 80));
        btnDelete.setPreferredSize(new java.awt.Dimension(300, 80));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        add(btnDelete, gridBagConstraints);

        btnMainMenu.setFont(new java.awt.Font("Dialog", 0, 26)); // NOI18N
        btnMainMenu.setText("Menú principal");
        btnMainMenu.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnMainMenu.setMaximumSize(new java.awt.Dimension(400, 200));
        btnMainMenu.setMinimumSize(new java.awt.Dimension(300, 80));
        btnMainMenu.setPreferredSize(new java.awt.Dimension(300, 80));
        btnMainMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMainMenuActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        add(btnMainMenu, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    // ------------------------------------------------------------
    //                     Action Event Handlers
    // ------------------------------------------------------------
    private void btnMainMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMainMenuActionPerformed
        LoggerUtil.info(getClass(), "Admin exiting dashboard. Returning to principal menu container.");
        userController.navigateToMainMenu();
    }//GEN-LAST:event_btnMainMenuActionPerformed

    private void tbtUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbtUsersActionPerformed
        if (tbtUsers.isSelected()) {
            switchToUsersView();
        } else {
            tbtUsers.setSelected(true); // Prevent full deselection gap
        }
    }//GEN-LAST:event_tbtUsersActionPerformed

    private void tbtTestsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbtTestsActionPerformed
        if (tbtTests.isSelected()) {
            switchToTestsView();
        } else {
            tbtTests.setSelected(true); // Prevent full deselection gap
        }
    }//GEN-LAST:event_tbtTestsActionPerformed

    private void btnChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePasswordActionPerformed
        // 1. Validate selection and current view context
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1 || !isViewingUsers) {
            showError("admin.error.no_selection");
            return;
        }

        // 2. Retrieve target user entity
        User targetUser = cachedUsers.get(table.convertRowIndexToModel(selectedRow));

        // 3. Prompt admin for the new forced password using the centralized BasePanel dialog
        String forcedPassword = showInputDialog("admin.prompt.new_password", "dialog.title.warning");

        // 4. If admin didn't cancel and provided a valid string, proceed with the override
        if (forcedPassword != null && !forcedPassword.trim().isEmpty()) {
            try {
                userController.forcePasswordReset(targetUser.getId(), forcedPassword.trim());
                LoggerUtil.info(getClass(), "Administrative password reset override applied to user: " + targetUser.getName());
                showTimedMessage("admin.success.password_reset", "dialog.title.info", 2500);
            } catch (IllegalArgumentException ex) {
                // Catches weak password policy exceptions thrown by the service layer
                showError(ex.getMessage());
            } catch (Exception e) {
                LoggerUtil.error(getClass(), "Failed override password injection routine", e);
                showError("admin.error.action_failed");
            }
        }
    }//GEN-LAST:event_btnChangePasswordActionPerformed

    private void btnChangeRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeRoleActionPerformed
        // 1. Validate selection and current view context
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1 || !isViewingUsers) {
            showError("admin.error.no_selection");
            return;
        }

        // 2. Retrieve the underlying User entity from the table's model index
        User targetUser = cachedUsers.get(table.convertRowIndexToModel(selectedRow));

        // 3. Determine the opposite role to toggle
        Role newRole = (targetUser.getRole() == Role.ADMINISTRATOR) ? Role.REGISTERED : Role.ADMINISTRATOR;

        try {
            // 4. Delegate mutation to the controller and refresh the UI grid
            userController.updateUserRole(targetUser.getId(), newRole);
            LoggerUtil.info(getClass(), "Role mutated for user: " + targetUser.getName() + " to " + newRole);
            refreshUsersTable();
        } catch (Exception e) {
            LoggerUtil.error(getClass(), "Failed mutation process on user permissions", e);
            showError("admin.error.action_failed");
        }
    }//GEN-LAST:event_btnChangeRoleActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // 1. Validate that at least one row is selected in either Quizzes or Users view
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("admin.error.no_selection");
            return;
        }

        // 2. Require explicit confirmation before permanent deletion using BasePanel wrapper
        boolean isConfirmed = showConfirmDialog("admin.prompt.delete_confirm", "dialog.title.warning");
        if (!isConfirmed) return;

        // 3. Route deletion logic based on the currently active tab (Users vs Quizzes)
        if (isViewingUsers) {
            User targetUser = cachedUsers.get(table.convertRowIndexToModel(selectedRow));
            try {
                // Check the boolean return value to confirm database mutation success
                boolean success = userController.deleteUser(targetUser.getId());

                if (success) {
                    LoggerUtil.info(getClass(), "Hard deletion executed successfully for user: " + targetUser.getName());
                    refreshUsersTable();
                } else {
                    LoggerUtil.warn(getClass(), "Service refused user registry erasure for ID: " + targetUser.getId());
                    showError("admin.error.delete_constraint");
                }
            } catch (Exception e) {
                LoggerUtil.error(getClass(), "Failed critical constraint execution during account deletion process", e);
                showError("admin.error.action_failed");
            }
        } else {
            QuizTest targetTest = cachedTests.get(table.convertRowIndexToModel(selectedRow));
            try {
                boolean success = quizTestController.deleteTest(targetTest.getId());

                if (success) {
                    LoggerUtil.info(getClass(), "Test deleted successfully: " + targetTest.getTitle());
                    refreshTestsTable();
                } else {
                    LoggerUtil.warn(getClass(), "Cannot delete test due to existing game records: " + targetTest.getTitle());
                    showError("admin.error.delete_restricted_test");
                }
            } catch (Exception e) {
                LoggerUtil.error(getClass(), "Failed critical data execution during quiz deletion process", e);
                showError("admin.error.action_failed");
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnChangeStateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeStateActionPerformed
        // 1. Validate selection and check if we are in the correct view context
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1 || isViewingUsers) {
            showError("admin.error.no_selection");
            return;
        }

        // 2. Retrieve target quiz entity
        QuizTest targetTest = cachedTests.get(table.convertRowIndexToModel(selectedRow));

        // 3. Gather available state transitions from the Enum definition
        State[] states = State.values();

        // 4. Prompt the administrator using the centralized BasePanel wrapper method
        int selection = showOptionDialog(
                "admin.prompt.select_state",
                "dialog.title.warning",
                states,
                targetTest.getState()
        );

        // 5. Abort execution safely if the user closed the window without an explicit click selection (-1)
        if (selection == -1) {
            return;
        }

        State selectedState = states[selection];

        try {
            // 6. Execute modification across the controller transaction layer
            boolean updated = quizTestController.updateTestState(targetTest.getId(), selectedState);

            if (updated) {
                LoggerUtil.info(getClass(), "State successfully mutated to " + selectedState + " for test: " + targetTest.getTitle());
                refreshTestsTable();
            } else {
                LoggerUtil.warn(getClass(), "Persistence layer rejected status mutation for test ID: " + targetTest.getId());
                showError("admin.error.action_failed");
            }
        } catch (Exception e) {
            LoggerUtil.error(getClass(), "Critical failure during administrative quiz state override flow", e);
            showError("admin.error.action_failed");
        }
    }//GEN-LAST:event_btnChangeStateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChangePassword;
    private javax.swing.JButton btnChangeRole;
    private javax.swing.JButton btnChangeState;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnMainMenu;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> list;
    private javax.swing.JScrollPane scrollPanel;
    private javax.swing.JTable table;
    private javax.swing.JToggleButton tbtTests;
    private javax.swing.JToggleButton tbtUsers;
    // End of variables declaration//GEN-END:variables
}
