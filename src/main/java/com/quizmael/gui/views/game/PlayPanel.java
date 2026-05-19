package com.quizmael.gui.views.game;

import com.quizmael.controller.GameController;
import com.quizmael.model.Answer;
import com.quizmael.model.Question;
import com.quizmael.util.I18nUtil;

import java.awt.Color;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

/**
 * Panel used to display and play a quiz session.
 * Shows questions, options, manages the visual timer, and handles user interaction.
 * 
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class PlayPanel extends com.quizmael.gui.common.BasePanel {

    // ------------------------------------------------------------
    //                     Attributes
    // ------------------------------------------------------------

    private final GameController gameController;
    private final ButtonGroup answerGroup;
    private final List<JToggleButton> toggleButtons;

    // State management
    private boolean isAnswerSubmitted;
    private boolean hasNextQuestion;
    private Timer uiTimer;

    // Visual Constants
    private static final Color DEFAULT_BUTTON_COLOR = new Color(204, 204, 255); // Pastel Lavender Blue
    private static final Color CORRECT_ANSWER_COLOR = new Color(0, 153, 51);  // Green
    private static final Color INCORRECT_ANSWER_COLOR = new Color(204, 0, 0); // Red


    // ------------------------------------------------------------
    //                     Constructor
    // ------------------------------------------------------------

    /**
     * Creates new form PlayPanel with Dependency Injection.
     *
     * @param gameController the controller managing game logic and timers
     */
    public PlayPanel(GameController gameController) {
        this.gameController = gameController;
        initComponents();

        // Initialize ButtonGroup and visual configurations
        answerGroup = new ButtonGroup();
        answerGroup.add(tbtAnswer1);
        answerGroup.add(tbtAnswer2);
        answerGroup.add(tbtAnswer3);
        answerGroup.add(tbtAnswer4);

        toggleButtons = List.of(tbtAnswer1, tbtAnswer2, tbtAnswer3, tbtAnswer4);
        for (JToggleButton btn : toggleButtons) {
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
        }
    }

    // ------------------------------------------------------------
    //                     Lifecycle Methods
    // ------------------------------------------------------------

    /**
     * Overrides setVisible to automatically initialize the game UI state,
     * load the first question, and start the visual timer polling.
     */
    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            // Reset state for a new game
            isAnswerSubmitted = false;
            hasNextQuestion = true;
            btnAnswerNext.setText(I18nUtil.getMessage("play.btn.answer"));
            lblTime.setVisible(true);

            // Load the first question
            loadQuestion(gameController.getCurrentQuestion());

            // Start visual timer polling
            startUITimer();
        } else {
            // Clean up when leaving the panel
            stopUITimer();
            resetButtonState();
        }
    }

    // ------------------------------------------------------------
    //                     Private Methods
    // ------------------------------------------------------------

    /**
     * Starts a local timer that queries the controller every second
     * to update the visual countdown label.
     */
    private void startUITimer() {
        if (uiTimer != null && uiTimer.isRunning()) {
            uiTimer.stop();
        }

        uiTimer = new Timer(500, e -> {
            int left = gameController.getTimeLeft();
            if (left >= 0) {
                // Format seconds into MM:SS
                lblTime.setText(String.format("%02d:%02d", left / 60, left % 60));
            }
        });
        uiTimer.start();
    }

    private void stopUITimer() {
        if (uiTimer != null) {
            uiTimer.stop();
        }
    }

    /**
     * Loads the given question and its answers into the interface.
     *
     * @param question the question to be displayed
     */
    private void loadQuestion(Question question) {
        if (question == null) return;

        lblQuestion.setText(question.getText());

        // Get and shuffle the answers
        List<Answer> answers = question.getAnswers();
        Collections.shuffle(answers);

        // Assign text and underlying answer objects to buttons
        for (int i = 0; i < toggleButtons.size(); i++) {
            if (i < answers.size()) {
                JToggleButton btn = toggleButtons.get(i);
                btn.setText(answers.get(i).getText());
                btn.putClientProperty("answer", answers.get(i));
                btn.setVisible(true);
            } else {
                toggleButtons.get(i).setVisible(false);
            }
        }

        resetButtonState();
    }

    /**
     * Main action handler for the primary button.
     * Routes logic based on the current submission state.
     */
    private void handleMainAction() {
        if (!isAnswerSubmitted) {
            submitAnswer();
        } else {
            advanceGameFlow();
        }
    }

    /**
     * Handles the logic for evaluating the user's selected answer.
     */
    private void submitAnswer() {
        AbstractButton selectedToggle = getSelectedToggle();
        if (selectedToggle == null) {
            showError("play.error.no_selection");
            return;
        }

        Answer selectedAnswer = (Answer) selectedToggle.getClientProperty("answer");

        // Let the controller process the answer in the DB
        hasNextQuestion = gameController.processAnswer(selectedAnswer);

        // Reveal colors to the user
        colorAnswers(selectedAnswer);
        isAnswerSubmitted = true;

        // Update button text to guide the user's next action
        if (hasNextQuestion) {
            btnAnswerNext.setText(I18nUtil.getMessage("play.btn.next"));
        } else {
            btnAnswerNext.setText(I18nUtil.getMessage("play.btn.finish"));
            stopUITimer(); // Stop timer visually since they answered the last question
        }
    }

    /**
     * Handles the transition when the user clicks "Next" or "Finish".
     */
    private void advanceGameFlow() {
        if (hasNextQuestion) {
            loadQuestion(gameController.getCurrentQuestion());
            isAnswerSubmitted = false;
            btnAnswerNext.setText(I18nUtil.getMessage("play.btn.answer"));
        } else {
            // User saw the final colors, now we officially finish and show results
            gameController.finishGame();
        }
    }

    private void resetButtonState() {
        answerGroup.clearSelection();
        for (JToggleButton btn : toggleButtons) {
            btn.setEnabled(true);
            btn.setBackground(DEFAULT_BUTTON_COLOR);
            btn.setBorder(null);
            btn.setSelected(false);
        }
    }

    private AbstractButton getSelectedToggle() {
        Enumeration<AbstractButton> buttons = answerGroup.getElements();
        while (buttons.hasMoreElements()) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button;
            }
        }
        return null;
    }

    /**
     * Colors the answer buttons to provide visual feedback.
     * * @param selectedAnswer The answer chosen by the user.
     */
    private void colorAnswers(Answer selectedAnswer) {
        for (JToggleButton btn : toggleButtons) {
            if (!btn.isVisible()) continue;

            Answer answer = (Answer) btn.getClientProperty("answer");
            btn.setSelected(false); // Remove active selection state

            if (answer.isCorrect()) {
                btn.setBackground(CORRECT_ANSWER_COLOR);
                btn.setBorder(new LineBorder(CORRECT_ANSWER_COLOR, 3));
            } else if (answer.equals(selectedAnswer)) {
                btn.setBackground(INCORRECT_ANSWER_COLOR);
                btn.setBorder(new LineBorder(INCORRECT_ANSWER_COLOR, 3));
            }

            // Disable button to prevent changing answers
            btn.setEnabled(false);
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

        buttonGroup = new javax.swing.ButtonGroup();
        northPanel = new javax.swing.JPanel();
        lblQuestion = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        centerPanel = new javax.swing.JPanel();
        tbtAnswer1 = new javax.swing.JToggleButton();
        tbtAnswer2 = new javax.swing.JToggleButton();
        tbtAnswer3 = new javax.swing.JToggleButton();
        tbtAnswer4 = new javax.swing.JToggleButton();
        southPanel = new javax.swing.JPanel();
        feedBackPanel = new javax.swing.JPanel();
        lblQuestion1 = new javax.swing.JLabel();
        answerNextPanel = new javax.swing.JPanel();
        verticalGlue = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        btnAnswerNext = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(50, 100, 50, 100));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setLayout(new java.awt.BorderLayout());

        northPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 20, 0));
        northPanel.setMinimumSize(new java.awt.Dimension(600, 200));
        northPanel.setPreferredSize(new java.awt.Dimension(600, 200));
        northPanel.setLayout(new java.awt.GridBagLayout());

        lblQuestion.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        lblQuestion.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        lblQuestion.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lblQuestion.setMinimumSize(new java.awt.Dimension(300, 130));
        lblQuestion.setPreferredSize(new java.awt.Dimension(300, 130));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        northPanel.add(lblQuestion, gridBagConstraints);

        lblTime.setMaximumSize(new java.awt.Dimension(300, 50));
        lblTime.setMinimumSize(new java.awt.Dimension(50, 30));
        lblTime.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        northPanel.add(lblTime, gridBagConstraints);

        add(northPanel, java.awt.BorderLayout.NORTH);

        centerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 0, 15, 0));
        centerPanel.setLayout(new java.awt.GridBagLayout());

        buttonGroup.add(tbtAnswer1);
        tbtAnswer1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 30, 30);
        centerPanel.add(tbtAnswer1, gridBagConstraints);

        buttonGroup.add(tbtAnswer2);
        tbtAnswer2.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 30, 0);
        centerPanel.add(tbtAnswer2, gridBagConstraints);

        buttonGroup.add(tbtAnswer3);
        tbtAnswer3.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(30, 0, 0, 30);
        centerPanel.add(tbtAnswer3, gridBagConstraints);

        buttonGroup.add(tbtAnswer4);
        tbtAnswer4.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(30, 30, 0, 0);
        centerPanel.add(tbtAnswer4, gridBagConstraints);

        add(centerPanel, java.awt.BorderLayout.CENTER);

        southPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 1, 1, 1));
        southPanel.setMinimumSize(new java.awt.Dimension(600, 180));
        southPanel.setPreferredSize(new java.awt.Dimension(600, 180));
        southPanel.setLayout(new java.awt.BorderLayout());

        feedBackPanel.setMinimumSize(new java.awt.Dimension(800, 50));
        feedBackPanel.setPreferredSize(new java.awt.Dimension(800, 100));
        feedBackPanel.setLayout(new java.awt.GridBagLayout());

        lblQuestion1.setMaximumSize(new java.awt.Dimension(300, 50));
        lblQuestion1.setMinimumSize(new java.awt.Dimension(50, 30));
        lblQuestion1.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        feedBackPanel.add(lblQuestion1, gridBagConstraints);

        southPanel.add(feedBackPanel, java.awt.BorderLayout.WEST);

        answerNextPanel.setMinimumSize(new java.awt.Dimension(190, 60));
        answerNextPanel.setPreferredSize(new java.awt.Dimension(190, 60));
        answerNextPanel.setLayout(new javax.swing.BoxLayout(answerNextPanel, javax.swing.BoxLayout.Y_AXIS));
        answerNextPanel.add(verticalGlue);

        btnAnswerNext.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        btnAnswerNext.setText("Responder");
        btnAnswerNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAnswerNext.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnAnswerNext.setMaximumSize(new java.awt.Dimension(250, 150));
        btnAnswerNext.setMinimumSize(new java.awt.Dimension(190, 60));
        btnAnswerNext.setPreferredSize(new java.awt.Dimension(190, 60));
        btnAnswerNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnswerNextActionPerformed(evt);
            }
        });
        answerNextPanel.add(btnAnswerNext);

        southPanel.add(answerNextPanel, java.awt.BorderLayout.EAST);

        add(southPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnswerNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnswerNextActionPerformed
        handleMainAction();
    }//GEN-LAST:event_btnAnswerNextActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel answerNextPanel;
    private javax.swing.JButton btnAnswerNext;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JPanel feedBackPanel;
    private javax.swing.JLabel lblQuestion;
    private javax.swing.JLabel lblQuestion1;
    private javax.swing.JLabel lblTime;
    private javax.swing.JPanel northPanel;
    private javax.swing.JPanel southPanel;
    private javax.swing.JToggleButton tbtAnswer1;
    private javax.swing.JToggleButton tbtAnswer2;
    private javax.swing.JToggleButton tbtAnswer3;
    private javax.swing.JToggleButton tbtAnswer4;
    private javax.swing.Box.Filler verticalGlue;
    // End of variables declaration//GEN-END:variables

}
