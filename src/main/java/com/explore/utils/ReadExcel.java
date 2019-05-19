package com.explore.utils;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.sym.error;

public class ReadExcel {
    public static List<Map<Integer, String>> readExcelContentByList(MultipartFile file) {

        List<Map<Integer, String>> list = new ArrayList<Map<Integer,String>>();
        Workbook wb = null;
        Row row=null;
        //判断是否为excel类型文件
        if(!file.getOriginalFilename().endsWith(".xls")&&!file.getOriginalFilename().endsWith(".xlsx"))
        {
            System.out.println("文件不是excel类型");
        }

        InputStream  fis =null;
        try
        {
            //获取一个绝对地址的流
            fis =file.getInputStream();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if(file.getOriginalFilename().endsWith(".xls")){
            try
            {
                //2003版本的excel，用.xls结尾
                wb = new HSSFWorkbook(fis);//得到工作簿

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }else{
            try
            {
                //2007版本的excel，用.xlsx结尾
                wb = new XSSFWorkbook(fis);//得到工作簿
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                System.out.println(e);
                e.printStackTrace();
            }

        }



        Sheet sheet = wb.getSheetAt(0);

        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();

        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            if(row==null){return  list;}
            int j = 0;
            Map<Integer,String> map = new HashMap<Integer, String>();
            while (j < colNum) {
                if( row.getCell(j)!=null){
                    row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                    map.put(j, row.getCell(j).getStringCellValue());
                }
                j++;
            }
            list.add(map);
        }
        return list;
    }
}
