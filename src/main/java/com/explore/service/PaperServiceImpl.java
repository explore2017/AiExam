package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.dao.PaperMapper;
import com.explore.pojo.Paper;
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
}
