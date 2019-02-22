package com.explore.service.Impl;

import com.explore.common.ServerResponse;
import com.explore.dao.PaperMapper;
import com.explore.pojo.Paper;
import com.explore.service.IPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PaperServiceImpl implements IPaperService {

    @Autowired
    PaperMapper paperMapper;

    @Override
    public ServerResponse savePaper(Paper paper) {
        Date date = new Date();
        paper.setCreateTime(date);
        paper.setUpdateTime(date);
        paperMapper.insert(paper);
        return ServerResponse.createBySuccessMessage("添加成功 ");
    }

    @Override
    public ServerResponse editPaperByPaperId(Integer paperId, Paper paper) {
        return null;
    }

    @Override
    public ServerResponse deceltPaperByPaperId(Integer paperId) {
        return null;
    }

    @Override
    public ServerResponse<Paper> getPaperByPaperId(Integer paperId) {
        return null;
    }
}
