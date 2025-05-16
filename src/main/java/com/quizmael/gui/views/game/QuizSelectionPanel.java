package com.quizmael.gui.views.game;

import com.quizmael.controller.AppController;
import com.quizmael.gui.views.profile.*;
import com.quizmael.gui.views.auth.*;
import com.quizmael.model.QuizTest;
import com.quizmael.model.enums.Language;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Panel for browsing and selecting available quizzes.
 * Allows users to filter or choose a quiz to play.
 * 
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class QuizSelectionPanel extends com.quizmael.gui.common.BasePanel {
    
    // ------------------------------------------------------------
    //                     Attributes
    // ------------------------------------------------------------
    private final AppController appController;
    private SpinnerNumberModel spinnerModel;
    private DefaultTableModel tableModel;
    private List<QuizTest> filteredQuizzesList = new ArrayList<>();
    
    // ------------------------------------------------------------
    //                     Public Methods
    // ------------------------------------------------------------
    
    /**
     * Creates new form LoginPanel
     */
    public QuizSelectionPanel(AppController appController) {
        this.appController = appController;
        initComponents();
        // TODO: hace visibles estos componentes cuando implemente su funcionalidad ----------------------------
        tabbedPanel.setVisible(false);
        lblAnswerOptions.setVisible(false);
        cboAnswerOptions.setVisible(false);
        lblMaxTime.setVisible(false);
        cboMaxTime.setVisible(false);
        // -----------------------------------------------------------------------------------------------------
        
        // Initializations
        spinnerModel = (SpinnerNumberModel) spnNumQuestions.getModel();
        tableModel = (DefaultTableModel) tableQuizzes.getModel();
        
        // Table configuration
        tableQuizzes.setFillsViewportHeight(true);
        tableQuizzes.setAutoCreateRowSorter(true);
        TableColumnModel columnModel = tableQuizzes.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(250); // Title
        columnModel.getColumn(1).setPreferredWidth(400); // Description
        columnModel.getColumn(2).setPreferredWidth(250); // Topics
        columnModel.getColumn(3).setPreferredWidth(100); // Language

        // Table listener
        tableQuizzes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableQuizzes.getSelectedRow();
                handleQuizTableSelection(selectedRow);
            }
        });
    }
    
    /**
     * Resets the filter components to their default values and reloads
     * the available topics and creators for the filter dropdowns.
     */
    public void resetFilters() {
        loadTopics();
        loadCreators();
        chkDifficulty.setSelected(false);
        sldDifficulty.setEnabled(false);
        sldDifficulty.setValue(3);
        chkSpanish.setSelected(true);
        chkEnglish.setSelected(true);
        tableModel.setRowCount(0);
        spnNumQuestions.setValue(10);
    }
    
    // ------------------------------------------------------------
    //                     Private Methods
    // ------------------------------------------------------------
    
    private void handleQuizTableSelection(int selectedRow) {
        if (selectedRow >= 0) {
            // Get the selected QuizTest object 
            QuizTest selectedTest = filteredQuizzesList.get(tableQuizzes.convertRowIndexToModel(selectedRow));
            
            // Enable the Start Quiz Button
            btnStartQuiz.setEnabled(true);
            
            // Enable and set the spinner with the valid range of questions
            spnNumQuestions.setEnabled(true);
            int totalQuestions = selectedTest.getQuestions().size();
            spinnerModel.setMaximum(totalQuestions);

            // Update labels with details
            lblSelectedQuizCreator.setText(selectedTest.getCreator().getName());
            lblSelectedQuizAnswerOptions.setText(String.valueOf(selectedTest.getOptionsCount()));
            lblSelectedQuizDifficulty.setText(String.valueOf(selectedTest.getDifficulty()));
            lblSelectedQuizNumQuestions.setText(String.valueOf(totalQuestions));
            lblSelectedQuizRating.setText(String.valueOf(selectedTest.getAverageRating()));
            lblSelectedQuizTimesPlayed.setText(String.valueOf(selectedTest.getTimesPlayed()));
        } else {
            // If no selection, disables components and clean labels from the selected quiz
            btnStartQuiz.setEnabled(false);
            spnNumQuestions.setEnabled(false);
            cleanSelectedQuizLabels();
        }
    }

    
    private void cleanSelectedQuizLabels() {
        lblSelectedQuizCreator.setText("-");
        lblSelectedQuizAnswerOptions.setText("-");
        lblSelectedQuizDifficulty.setText("-");
        lblSelectedQuizNumQuestions.setText("-");
        lblSelectedQuizRating.setText("-");
        lblSelectedQuizTimesPlayed.setText("-");
    }

    
    public void onFilterButtonClick() {
        // 1. Language verification
        if (!chkSpanish.isSelected() && !chkEnglish.isSelected()) {
            showError("Debes seleccionar al menos un idioma.", "Error");
            return;
        }

        // 2. Get selected filters
        String topicName = null;
        if (cboTopics.getSelectedIndex() > 0) {
            topicName = (String) cboTopics.getSelectedItem();
        }
        
        String creatorName = null;
        if (cboCreators.getSelectedIndex() > 0) {
            creatorName = (String) cboCreators.getSelectedItem();
        }
        
        Integer difficulty = chkDifficulty.isSelected() ? sldDifficulty.getValue() : null;

        Set<Language> selectedLanguages = new HashSet<>();
        if (chkSpanish.isSelected()) selectedLanguages.add(Language.ES);
        if (chkEnglish.isSelected()) selectedLanguages.add(Language.EN);

        // 3. Call the controller
        List<QuizTest> results = appController.getGameController().findFilteredPublicTests(
                topicName, creatorName, difficulty, selectedLanguages);

        // 4. Show results
        filteredQuizzesList.clear();
        tableModel.setRowCount(0);
        if (results.isEmpty()) {
            showMessage("No se encontraron tests con los filtros aplicados.", "Sin resultados");
        } else {
            for (QuizTest qt : results) {
                // Add test to the quizzes list attribute
                filteredQuizzesList.add(qt);
                
                // Fill table rows
                tableModel.addRow(new Object[]{
                        qt.getTitle(),
                        qt.getDescription(),
                        qt.getTopicsAsString(),
                        qt.getLanguage().toString()
                });
            }
        }
    }
    
    /**
     * Loads available topics from the database and populates the topic combo box.
     * The first item is a placeholder for selecting all topics.
     */
    private void loadTopics() {
        cboTopics.removeAllItems();
        cboTopics.addItem("-- Selecciona un tema --");
        List<String> topics = appController.getGameController().loadAvailableTopics();
        for (String topicName : topics) {
            cboTopics.addItem(topicName);
        }
    }
    
    /**
     * Loads all test creators from the database and populates the creator combo box.
     * The first item is a placeholder for selecting all creators.
     */
    private void loadCreators() {
        cboCreators.removeAllItems();
        cboCreators.addItem("-- Selecciona un creador --");

        List<String> creators = appController.getGameController().loadAvailableCreators();
        for (String creatorName : creators) {
            cboCreators.addItem(creatorName);
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

        northPanel = new javax.swing.JPanel();
        tabbedPanel = new javax.swing.JTabbedPane();
        centerPanel = new javax.swing.JPanel();
        cboTopics = new javax.swing.JComboBox<>();
        cboCreators = new javax.swing.JComboBox<>();
        chkDifficulty = new javax.swing.JCheckBox();
        sldDifficulty = new javax.swing.JSlider();
        languagesPanel = new javax.swing.JPanel();
        chkSpanish = new javax.swing.JCheckBox();
        chkEnglish = new javax.swing.JCheckBox();
        btnFilter = new javax.swing.JButton();
        scpTableQujzzes = new javax.swing.JScrollPane();
        tableQuizzes = new javax.swing.JTable();
        quizConfigurationPanel = new javax.swing.JPanel();
        lblAnswerOptions = new javax.swing.JLabel();
        cboAnswerOptions = new javax.swing.JComboBox<>();
        lblMaxTime = new javax.swing.JLabel();
        cboMaxTime = new javax.swing.JComboBox<>();
        lblNumQuestions = new javax.swing.JLabel();
        spnNumQuestions = new javax.swing.JSpinner();
        southPanel = new javax.swing.JPanel();
        quizDetailsPanel = new javax.swing.JPanel();
        imgSelectedQuizPanel = new javax.swing.JPanel();
        lblSelectedQuizImage = new javax.swing.JLabel();
        infoSelectedQuizPanel = new javax.swing.JPanel();
        lblSelectedQuizCreatorIcon = new javax.swing.JLabel();
        lblSelectedQuizCreator = new javax.swing.JLabel();
        lblSelectedQuizAnswerOptionsIcon = new javax.swing.JLabel();
        lblSelectedQuizAnswerOptions = new javax.swing.JLabel();
        lblSelectedQuizDifficultyIcon = new javax.swing.JLabel();
        lblSelectedQuizDifficulty = new javax.swing.JLabel();
        lblSelectedQuizNumQuestionsIcon = new javax.swing.JLabel();
        lblSelectedQuizNumQuestions = new javax.swing.JLabel();
        lblSelectedQuizRatingIcon = new javax.swing.JLabel();
        lblSelectedQuizRating = new javax.swing.JLabel();
        lblSelectedQuizTimesPlayedIcon = new javax.swing.JLabel();
        lblSelectedQuizTimesPlayed = new javax.swing.JLabel();
        goBackPanel = new javax.swing.JPanel();
        verticalGlue1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        btnGoBack = new javax.swing.JButton();
        startQuizPanel = new javax.swing.JPanel();
        verticalGlue = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        btnStartQuiz = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(50, 100, 50, 100));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setLayout(new java.awt.BorderLayout());

        northPanel.setLayout(new java.awt.GridBagLayout());

        tabbedPanel.setMinimumSize(new java.awt.Dimension(900, 180));
        tabbedPanel.setPreferredSize(new java.awt.Dimension(900, 180));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        northPanel.add(tabbedPanel, gridBagConstraints);

        add(northPanel, java.awt.BorderLayout.NORTH);

        centerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 0, 15, 0));
        centerPanel.setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        centerPanel.add(cboTopics, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        centerPanel.add(cboCreators, gridBagConstraints);

        chkDifficulty.setText("Nivel de dificultad");
        chkDifficulty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        chkDifficulty.setMaximumSize(new java.awt.Dimension(60, 50));
        chkDifficulty.setMinimumSize(new java.awt.Dimension(30, 30));
        chkDifficulty.setPreferredSize(new java.awt.Dimension(30, 30));
        chkDifficulty.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkDifficultyItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        centerPanel.add(chkDifficulty, gridBagConstraints);

        sldDifficulty.setMajorTickSpacing(1);
        sldDifficulty.setMaximum(5);
        sldDifficulty.setMinimum(1);
        sldDifficulty.setMinorTickSpacing(1);
        sldDifficulty.setPaintLabels(true);
        sldDifficulty.setPaintTicks(true);
        sldDifficulty.setToolTipText("");
        sldDifficulty.setValue(3);
        sldDifficulty.setEnabled(false);
        sldDifficulty.setMinimumSize(new java.awt.Dimension(140, 43));
        sldDifficulty.setPreferredSize(new java.awt.Dimension(140, 43));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.15;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        centerPanel.add(sldDifficulty, gridBagConstraints);

        languagesPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true), "Idiomas"), javax.swing.BorderFactory.createEmptyBorder(8, 20, 8, 20)));
        languagesPanel.setLayout(new java.awt.GridBagLayout());

        chkSpanish.setSelected(true);
        chkSpanish.setText("Español");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 15);
        languagesPanel.add(chkSpanish, gridBagConstraints);

        chkEnglish.setSelected(true);
        chkEnglish.setText("Inglés");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 15);
        languagesPanel.add(chkEnglish, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        centerPanel.add(languagesPanel, gridBagConstraints);

        btnFilter.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        btnFilter.setText("Filtrar");
        btnFilter.setMaximumSize(new java.awt.Dimension(65, 40));
        btnFilter.setMinimumSize(new java.awt.Dimension(55, 30));
        btnFilter.setPreferredSize(new java.awt.Dimension(55, 30));
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        centerPanel.add(btnFilter, gridBagConstraints);

        scpTableQujzzes.setMinimumSize(new java.awt.Dimension(1000, 150));
        scpTableQujzzes.setPreferredSize(new java.awt.Dimension(1000, 150));

        tableQuizzes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Título", "Description", "Temas", "Idioma"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        tableQuizzes.setToolTipText("");
        scpTableQujzzes.setViewportView(tableQuizzes);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 5.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 10, 0);
        centerPanel.add(scpTableQujzzes, gridBagConstraints);

        quizConfigurationPanel.setLayout(new java.awt.GridBagLayout());

        lblAnswerOptions.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAnswerOptions.setText("Número de opciones de respuesta");
        lblAnswerOptions.setEnabled(false);
        lblAnswerOptions.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblAnswerOptions.setMaximumSize(new java.awt.Dimension(300, 50));
        lblAnswerOptions.setMinimumSize(new java.awt.Dimension(50, 30));
        lblAnswerOptions.setOpaque(true);
        lblAnswerOptions.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        quizConfigurationPanel.add(lblAnswerOptions, gridBagConstraints);

        cboAnswerOptions.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        quizConfigurationPanel.add(cboAnswerOptions, gridBagConstraints);

        lblMaxTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMaxTime.setText("Tiempo máximo  por respuesta");
        lblMaxTime.setEnabled(false);
        lblMaxTime.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblMaxTime.setMaximumSize(new java.awt.Dimension(300, 50));
        lblMaxTime.setMinimumSize(new java.awt.Dimension(50, 30));
        lblMaxTime.setOpaque(true);
        lblMaxTime.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        quizConfigurationPanel.add(lblMaxTime, gridBagConstraints);

        cboMaxTime.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        quizConfigurationPanel.add(cboMaxTime, gridBagConstraints);

        lblNumQuestions.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNumQuestions.setText("Número de preguntas a responder");
        lblNumQuestions.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblNumQuestions.setMaximumSize(new java.awt.Dimension(300, 50));
        lblNumQuestions.setMinimumSize(new java.awt.Dimension(50, 30));
        lblNumQuestions.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        quizConfigurationPanel.add(lblNumQuestions, gridBagConstraints);

        spnNumQuestions.setModel(new javax.swing.SpinnerNumberModel(10, 10, null, 1));
        spnNumQuestions.setEnabled(false);
        spnNumQuestions.setMaximumSize(new java.awt.Dimension(50, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        quizConfigurationPanel.add(spnNumQuestions, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 5, 0);
        centerPanel.add(quizConfigurationPanel, gridBagConstraints);

        add(centerPanel, java.awt.BorderLayout.CENTER);

        southPanel.setLayout(new java.awt.BorderLayout());

        quizDetailsPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        quizDetailsPanel.setMinimumSize(new java.awt.Dimension(800, 50));
        quizDetailsPanel.setPreferredSize(new java.awt.Dimension(800, 100));
        quizDetailsPanel.setLayout(new java.awt.BorderLayout());

        imgSelectedQuizPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), javax.swing.BorderFactory.createEmptyBorder(5, 1, 1, 1)));

        lblSelectedQuizImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectedQuizImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logo_64.png"))); // NOI18N
        lblSelectedQuizImage.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblSelectedQuizImage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblSelectedQuizImage.setMaximumSize(new java.awt.Dimension(150, 150));
        lblSelectedQuizImage.setMinimumSize(new java.awt.Dimension(100, 100));
        lblSelectedQuizImage.setPreferredSize(new java.awt.Dimension(100, 100));
        imgSelectedQuizPanel.add(lblSelectedQuizImage);

        quizDetailsPanel.add(imgSelectedQuizPanel, java.awt.BorderLayout.WEST);

        infoSelectedQuizPanel.setLayout(new java.awt.GridBagLayout());

        lblSelectedQuizCreatorIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectedQuizCreatorIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user.png"))); // NOI18N
        lblSelectedQuizCreatorIcon.setToolTipText("Creador");
        lblSelectedQuizCreatorIcon.setMaximumSize(new java.awt.Dimension(64, 64));
        lblSelectedQuizCreatorIcon.setMinimumSize(new java.awt.Dimension(36, 36));
        lblSelectedQuizCreatorIcon.setPreferredSize(new java.awt.Dimension(36, 36));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        infoSelectedQuizPanel.add(lblSelectedQuizCreatorIcon, gridBagConstraints);

        lblSelectedQuizCreator.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSelectedQuizCreator.setText("-");
        lblSelectedQuizCreator.setMaximumSize(new java.awt.Dimension(300, 50));
        lblSelectedQuizCreator.setMinimumSize(new java.awt.Dimension(50, 30));
        lblSelectedQuizCreator.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        infoSelectedQuizPanel.add(lblSelectedQuizCreator, gridBagConstraints);

        lblSelectedQuizAnswerOptionsIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectedQuizAnswerOptionsIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/numberOptions.png"))); // NOI18N
        lblSelectedQuizAnswerOptionsIcon.setToolTipText("Número de opciones de respuesta");
        lblSelectedQuizAnswerOptionsIcon.setMaximumSize(new java.awt.Dimension(64, 64));
        lblSelectedQuizAnswerOptionsIcon.setMinimumSize(new java.awt.Dimension(36, 36));
        lblSelectedQuizAnswerOptionsIcon.setPreferredSize(new java.awt.Dimension(36, 36));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        infoSelectedQuizPanel.add(lblSelectedQuizAnswerOptionsIcon, gridBagConstraints);

        lblSelectedQuizAnswerOptions.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSelectedQuizAnswerOptions.setText("-");
        lblSelectedQuizAnswerOptions.setMaximumSize(new java.awt.Dimension(300, 50));
        lblSelectedQuizAnswerOptions.setMinimumSize(new java.awt.Dimension(50, 30));
        lblSelectedQuizAnswerOptions.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        infoSelectedQuizPanel.add(lblSelectedQuizAnswerOptions, gridBagConstraints);

        lblSelectedQuizDifficultyIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectedQuizDifficultyIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/difficulty.png"))); // NOI18N
        lblSelectedQuizDifficultyIcon.setToolTipText("Dificultad");
        lblSelectedQuizDifficultyIcon.setMaximumSize(new java.awt.Dimension(64, 64));
        lblSelectedQuizDifficultyIcon.setMinimumSize(new java.awt.Dimension(36, 36));
        lblSelectedQuizDifficultyIcon.setPreferredSize(new java.awt.Dimension(36, 36));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        infoSelectedQuizPanel.add(lblSelectedQuizDifficultyIcon, gridBagConstraints);

        lblSelectedQuizDifficulty.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSelectedQuizDifficulty.setText("-");
        lblSelectedQuizDifficulty.setMaximumSize(new java.awt.Dimension(300, 50));
        lblSelectedQuizDifficulty.setMinimumSize(new java.awt.Dimension(50, 30));
        lblSelectedQuizDifficulty.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        infoSelectedQuizPanel.add(lblSelectedQuizDifficulty, gridBagConstraints);

        lblSelectedQuizNumQuestionsIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectedQuizNumQuestionsIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/question-mark.png"))); // NOI18N
        lblSelectedQuizNumQuestionsIcon.setToolTipText("Número de preguntas");
        lblSelectedQuizNumQuestionsIcon.setMaximumSize(new java.awt.Dimension(64, 64));
        lblSelectedQuizNumQuestionsIcon.setMinimumSize(new java.awt.Dimension(36, 36));
        lblSelectedQuizNumQuestionsIcon.setPreferredSize(new java.awt.Dimension(36, 36));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        infoSelectedQuizPanel.add(lblSelectedQuizNumQuestionsIcon, gridBagConstraints);

        lblSelectedQuizNumQuestions.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSelectedQuizNumQuestions.setText("-");
        lblSelectedQuizNumQuestions.setMaximumSize(new java.awt.Dimension(300, 50));
        lblSelectedQuizNumQuestions.setMinimumSize(new java.awt.Dimension(50, 30));
        lblSelectedQuizNumQuestions.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        infoSelectedQuizPanel.add(lblSelectedQuizNumQuestions, gridBagConstraints);

        lblSelectedQuizRatingIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectedQuizRatingIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/star.png"))); // NOI18N
        lblSelectedQuizRatingIcon.setToolTipText("Valoración media");
        lblSelectedQuizRatingIcon.setMaximumSize(new java.awt.Dimension(64, 64));
        lblSelectedQuizRatingIcon.setMinimumSize(new java.awt.Dimension(36, 36));
        lblSelectedQuizRatingIcon.setPreferredSize(new java.awt.Dimension(36, 36));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        infoSelectedQuizPanel.add(lblSelectedQuizRatingIcon, gridBagConstraints);

        lblSelectedQuizRating.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSelectedQuizRating.setText("-");
        lblSelectedQuizRating.setMaximumSize(new java.awt.Dimension(300, 50));
        lblSelectedQuizRating.setMinimumSize(new java.awt.Dimension(50, 30));
        lblSelectedQuizRating.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        infoSelectedQuizPanel.add(lblSelectedQuizRating, gridBagConstraints);

        lblSelectedQuizTimesPlayedIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectedQuizTimesPlayedIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/counter.png"))); // NOI18N
        lblSelectedQuizTimesPlayedIcon.setToolTipText("Veces jugado");
        lblSelectedQuizTimesPlayedIcon.setMaximumSize(new java.awt.Dimension(64, 64));
        lblSelectedQuizTimesPlayedIcon.setMinimumSize(new java.awt.Dimension(36, 36));
        lblSelectedQuizTimesPlayedIcon.setPreferredSize(new java.awt.Dimension(36, 36));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        infoSelectedQuizPanel.add(lblSelectedQuizTimesPlayedIcon, gridBagConstraints);

        lblSelectedQuizTimesPlayed.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSelectedQuizTimesPlayed.setText("-");
        lblSelectedQuizTimesPlayed.setMaximumSize(new java.awt.Dimension(300, 50));
        lblSelectedQuizTimesPlayed.setMinimumSize(new java.awt.Dimension(50, 30));
        lblSelectedQuizTimesPlayed.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        infoSelectedQuizPanel.add(lblSelectedQuizTimesPlayed, gridBagConstraints);

        quizDetailsPanel.add(infoSelectedQuizPanel, java.awt.BorderLayout.CENTER);

        southPanel.add(quizDetailsPanel, java.awt.BorderLayout.CENTER);

        goBackPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));
        goBackPanel.setLayout(new javax.swing.BoxLayout(goBackPanel, javax.swing.BoxLayout.Y_AXIS));
        goBackPanel.add(verticalGlue1);

        btnGoBack.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        btnGoBack.setText("Atrás");
        btnGoBack.setEnabled(false);
        btnGoBack.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGoBack.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnGoBack.setMaximumSize(new java.awt.Dimension(140, 40));
        btnGoBack.setMinimumSize(new java.awt.Dimension(100, 40));
        btnGoBack.setPreferredSize(new java.awt.Dimension(100, 40));
        btnGoBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoBackActionPerformed(evt);
            }
        });
        goBackPanel.add(btnGoBack);

        southPanel.add(goBackPanel, java.awt.BorderLayout.WEST);

        startQuizPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        startQuizPanel.setMinimumSize(new java.awt.Dimension(190, 60));
        startQuizPanel.setPreferredSize(new java.awt.Dimension(190, 60));
        startQuizPanel.setLayout(new javax.swing.BoxLayout(startQuizPanel, javax.swing.BoxLayout.Y_AXIS));
        startQuizPanel.add(verticalGlue);

        btnStartQuiz.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        btnStartQuiz.setText("Iniciar Test");
        btnStartQuiz.setEnabled(false);
        btnStartQuiz.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnStartQuiz.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnStartQuiz.setMaximumSize(new java.awt.Dimension(250, 150));
        btnStartQuiz.setMinimumSize(new java.awt.Dimension(190, 60));
        btnStartQuiz.setPreferredSize(new java.awt.Dimension(190, 60));
        btnStartQuiz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartQuizActionPerformed(evt);
            }
        });
        startQuizPanel.add(btnStartQuiz);

        southPanel.add(startQuizPanel, java.awt.BorderLayout.EAST);

        add(southPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void chkDifficultyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkDifficultyItemStateChanged
        sldDifficulty.setEnabled(chkDifficulty.isSelected());
    }//GEN-LAST:event_chkDifficultyItemStateChanged

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        onFilterButtonClick();
    }//GEN-LAST:event_btnFilterActionPerformed

    private void btnGoBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoBackActionPerformed
        appController.showMainMenuPanel();
    }//GEN-LAST:event_btnGoBackActionPerformed

    private void btnStartQuizActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartQuizActionPerformed
        int selectedRow = tableQuizzes.getSelectedRow();
        if (selectedRow == -1) {
            showError("Por favor, selecciona un test.", "Error");
            return;
        }

        // Convert the visual index to the model
        int modelIndex = tableQuizzes.convertRowIndexToModel(selectedRow);
        QuizTest selectedTest = filteredQuizzesList.get(modelIndex);

        // Load the test and display the game panel
        appController.getPlayPanel().loadQuizTest(selectedTest);
        appController.showPlayPanel();
    }//GEN-LAST:event_btnStartQuizActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnGoBack;
    private javax.swing.JButton btnStartQuiz;
    private javax.swing.JComboBox<String> cboAnswerOptions;
    private javax.swing.JComboBox<String> cboCreators;
    private javax.swing.JComboBox<String> cboMaxTime;
    private javax.swing.JComboBox<String> cboTopics;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JCheckBox chkDifficulty;
    private javax.swing.JCheckBox chkEnglish;
    private javax.swing.JCheckBox chkSpanish;
    private javax.swing.JPanel goBackPanel;
    private javax.swing.JPanel imgSelectedQuizPanel;
    private javax.swing.JPanel infoSelectedQuizPanel;
    private javax.swing.JPanel languagesPanel;
    private javax.swing.JLabel lblAnswerOptions;
    private javax.swing.JLabel lblMaxTime;
    private javax.swing.JLabel lblNumQuestions;
    private javax.swing.JLabel lblSelectedQuizAnswerOptions;
    private javax.swing.JLabel lblSelectedQuizAnswerOptionsIcon;
    private javax.swing.JLabel lblSelectedQuizCreator;
    private javax.swing.JLabel lblSelectedQuizCreatorIcon;
    private javax.swing.JLabel lblSelectedQuizDifficulty;
    private javax.swing.JLabel lblSelectedQuizDifficultyIcon;
    private javax.swing.JLabel lblSelectedQuizImage;
    private javax.swing.JLabel lblSelectedQuizNumQuestions;
    private javax.swing.JLabel lblSelectedQuizNumQuestionsIcon;
    private javax.swing.JLabel lblSelectedQuizRating;
    private javax.swing.JLabel lblSelectedQuizRatingIcon;
    private javax.swing.JLabel lblSelectedQuizTimesPlayed;
    private javax.swing.JLabel lblSelectedQuizTimesPlayedIcon;
    private javax.swing.JPanel northPanel;
    private javax.swing.JPanel quizConfigurationPanel;
    private javax.swing.JPanel quizDetailsPanel;
    private javax.swing.JScrollPane scpTableQujzzes;
    private javax.swing.JSlider sldDifficulty;
    private javax.swing.JPanel southPanel;
    private javax.swing.JSpinner spnNumQuestions;
    private javax.swing.JPanel startQuizPanel;
    private javax.swing.JTabbedPane tabbedPanel;
    private javax.swing.JTable tableQuizzes;
    private javax.swing.Box.Filler verticalGlue;
    private javax.swing.Box.Filler verticalGlue1;
    // End of variables declaration//GEN-END:variables
}
