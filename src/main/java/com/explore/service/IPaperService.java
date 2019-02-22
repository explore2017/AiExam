package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Paper;

public interface IPaperService {
    ServerResponse savePaper(Paper paper);
    ServerResponse editPaperByPaperId(Integer paperId,Paper paper);
    ServerResponse deceltPaperByPaperId(Integer paperId);
    ServerResponse<Paper> getPaperByPaperId(Integer paperId);

}
