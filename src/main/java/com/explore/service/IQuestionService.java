package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Question;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.util.List;

public interface IQuestionService {
    ServerResponse saveQuestion(Question question);
    ServerResponse editQuestionByQuestionId(Integer questionId,Question newQuestion);
    ServerResponse deleteQuestionByQuestionId(Integer questionId);
    ServerResponse<Question> getQuestionsByQuestionId(Integer questionId);
    ServerResponse<List<Question>> getAllQuestions();
    ServerResponse<List<Question>> getQuestionsByCondition(Integer subjectId,Integer difficulty,Integer questionTypeId,String keyPoint);
    ServerResponse batchImport(MultipartFile file);
    ServerResponse uploadImg(MultipartFile file, HttpServletRequest request) throws FileNotFoundException;
}
