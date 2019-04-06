package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Paper;
import com.explore.pojo.PaperCompose;
import com.explore.pojo.Question;

import java.util.List;

public interface IPaperService {
    ServerResponse savePaper(Paper paper);
    ServerResponse editPaperByPaperId(Integer paperId,Paper paper);
    ServerResponse deletePaperByPaperId(Integer paperId);
    ServerResponse<Paper> getPaperById(Integer id);
    ServerResponse<List<Paper>> getAllPaper();
    ServerResponse<List<Question>> getDetailsByPaperId(Integer paperId);
    ServerResponse addPaperComposeByPaperId(PaperCompose paperCompose );
    ServerResponse editPaperComposeByPaperId(Integer paperId,PaperCompose paperCompose );
    ServerResponse deletePaperComposeBySequenceAndPaperId(Integer paperId,Integer sequence);
    ServerResponse autoQuestion(Integer paperId,Integer questionTypeId,Integer quantity,Double singeScore,Integer subjectId,String keyPoint);
    ServerResponse checkPaper(Integer paperId,Double totalScore);
    ServerResponse changeSequence(PaperCompose paperCompose);
    ServerResponse updatePaperScore(Integer paperId);
    ServerResponse getPaperByClass(Integer classId);
}
