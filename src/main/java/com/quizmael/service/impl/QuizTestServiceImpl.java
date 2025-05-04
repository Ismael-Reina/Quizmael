package com.quizmael.service.impl;

import com.quizmael.dao.QuizTestDao;
import com.quizmael.model.QuizTest;
import com.quizmael.service.QuizTestService;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link QuizTestService} interface.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class QuizTestServiceImpl implements QuizTestService {

    private final QuizTestDao quizTestDao;

    public QuizTestServiceImpl(QuizTestDao quizTestDao) {
        this.quizTestDao = quizTestDao;
    }

    // ------------------------------------------------------------
    //           Management of tests created by a user
    // ------------------------------------------------------------

    @Override
    public List<QuizTest> findTestsByCreator(int userId) {
        return quizTestDao.findByCreatorId(userId);
    }

    @Override
    public Optional<QuizTest> findTestById(int testId) {
        return quizTestDao.findById(testId);
    }

    @Override
    public void createTest(QuizTest test) {
        quizTestDao.save(test);
    }

    @Override
    public void updateTest(QuizTest test) {
        quizTestDao.update(test);
    }

    @Override
    public void deleteTest(int testId) {
        quizTestDao.delete(testId);
    }

    // ------------------------------------------------------------
    //           Public tests (validated or not drafts)
    // ------------------------------------------------------------

    @Override
    public List<QuizTest> findAllPublicTests() {
        return quizTestDao.findAllPublic();
    }

    @Override
    public List<QuizTest> findTestsByTopic(String topicName) {
        return quizTestDao.findByTopic(topicName); // Search by topic name
    }

    @Override
    public List<QuizTest> findTestsByTitle(String title) {
        return quizTestDao.findByTitleContaining(title); // Partial search by title
    }

}
