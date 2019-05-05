//package com.explore.controller;
//
//import com.explore.common.ServerResponse;
//import com.explore.pojo.Batch;
//import com.explore.service.IBatchService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//@Controller
//public class BatchController {
//
//    @Autowired
//    IBatchService batchService;
//
//    @RequestMapping(value = "/batch",method = RequestMethod.POST)
//    @ResponseBody
//    public ServerResponse save(@RequestBody Batch batch){
//        return batchService.save(batch);
//    }
//
//}
