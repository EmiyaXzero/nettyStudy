package com.my.netty.study.nettycompant;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shanghang
 * @title: BindDemo
 * @projectName nettyStudy
 * @description: excel处理类
 * @date 2020.12.14-15:42
 */
public class ExcelDealDemo {
    public static void main(String[] args) throws IOException {
        List temp =  new ArrayList();
        String xlsPath = "C:\\Users\\shanghang\\Desktop\\3号之后.xlsx";
        FileInputStream fileIn =  new  FileInputStream(xlsPath);
        //根据指定的文件输入流导入Excel从而产生Workbook对象
        Workbook wb0 =  new XSSFWorkbook(fileIn);
        //获取Excel文档中的第一个表单
        Sheet sht0 = wb0.getSheetAt( 0 );
        List<String> errorNum = new ArrayList<>();
        //对Sheet中的每一行进行迭代
        int count = -1;
        for  (Row r : sht0) {
            count++;
            System.out.println(count);
//            if(r.getRowNum()< 1 ){
//                //首页跳过
//                continue ;
//            }
            String request = "";
            for (int i = 0 ;i <r.getLastCellNum();i++){
                String s = r.getCell(i).getStringCellValue();
                if(s.equals("")){
                    break;
                }else {
                    request+=s;
                }
            }
            JSONObject jsonObject =JSONObject.parseObject(request);
            JSONArray memberList = (JSONArray) jsonObject.get("memberList");
            String mainBillId = "";
            boolean isTure = false;
            for (int i = 0 ;i< memberList.size();i++){
                JSONObject tempJson = (JSONObject) memberList.get(i);
                if(null == tempJson.get("roleId")){
                    continue;
                }
                if(tempJson.get("roleId").equals("22")){
                    mainBillId = tempJson.getString("memberBillId");
                }else if(tempJson.get("roleId").equals("500000010111")){
                    JSONArray orderList = (JSONArray) tempJson.get("orderList");
                    JSONArray offerList = (JSONArray) ((JSONObject)orderList.get(0)).get("offerList");
                    for (int j =0 ;j<offerList.size();j++){
                        if(((JSONObject)offerList.get(j)).getString("offerType").equals("OFFER_PLAN_BROADBAND")){
                            isTure = true;
                        }
                    }
                }
            }
            if(!isTure){
                errorNum.add(mainBillId);
            }

        }
        for (String ss : errorNum){
            System.out.println(ss);
        }
    }
}
