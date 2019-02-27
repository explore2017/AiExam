package com.explore.dao;

import com.explore.pojo.PaperCompose;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface PaperComposeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PaperCompose record);

    int insertSelective(PaperCompose record);

    PaperCompose selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaperCompose record);

    int updateByPrimaryKey(PaperCompose record);

    List<PaperCompose> selectQuestionByPaperIdOrderBySequence(Integer paperId);

    PaperCompose  selectPaperComposeBySequenceAndPaperId(@Param("paperId") Integer paperId, @Param("sequence")Integer sequence);

    int deleteByPaperId(Integer paperId);

    int updateTosequenceByPaperId(@Param("paperId") Integer paperId, @Param("sequence")Integer sequence);
}