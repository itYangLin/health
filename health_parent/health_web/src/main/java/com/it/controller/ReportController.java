package com.it.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.it.constant.MessageConstant;
import com.it.service.MemberService;
import com.it.service.OrderService;
import com.it.service.SetMealService;
import com.it.utils.DateUtils;
import entity.Result;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetMealService setMealService;

    @Reference
    private OrderService orderService;

    //会员统计报表
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        try {
            //创建Map
            Map map = new HashMap();
            // 封装日期
            List<String> mintList = new ArrayList<>();
            Calendar calendar = Calendar.getInstance(); //获取当前日期
            calendar.add(Calendar.MONTH,-12);  //当前月份减12
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH,1); //当前月份加一
                String s = DateUtils.parseDate2String(calendar.getTime(), "yyyy-MM");
                mintList.add(s);
            }
            map.put("months", mintList);
            List<Integer> memberList = memberService.findMemberByMonths(mintList);
            map.put("memberCount",memberList);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }


    //套餐统计报表
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        try {
            Map map = new HashMap();

            //获取预约套餐数量
            List<Map> setMealCount =  setMealService.getSetmealReport();
            map.put("setmealCount",setMealCount);

            //获取套餐名字
            List<String> list = new ArrayList<>();
            for (Map map1 : setMealCount) {
                String name = (String) map1.get("name");
                list.add(name);
            }
            map.put("setmealNames",list);
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }


    //运营统计
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map data = orderService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,data);
       } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取运营数据
            Map map = orderService.getBusinessReportData();
            //获取文件io流
            InputStream is = request.getSession().getServletContext().getResourceAsStream("template/report_template.xlsx");
            //通过io流创建工作薄
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            //获取第一页数据
            XSSFSheet sheet = workbook.getSheetAt(0);
            //获取第三行
            XSSFRow row = sheet.getRow(2);
            //获取第6列 把数据填充进去
            row.getCell(5).setCellValue((String)map.get("reportDate"));
            //填充数据
            sheet.getRow(4).getCell(5).setCellValue(map.get("todayNewMember").toString());
            sheet.getRow(4).getCell(7).setCellValue(map.get("totalMember").toString());

            sheet.getRow(5).getCell(5).setCellValue(map.get("thisWeekNewMember").toString());
            sheet.getRow(5).getCell(7).setCellValue(map.get("thisMonthNewMember").toString());

            sheet.getRow(7).getCell(5).setCellValue(map.get("todayOrderNumber").toString());
            sheet.getRow(7).getCell(7).setCellValue(map.get("todayVisitsNumber").toString());

            sheet.getRow(8).getCell(5).setCellValue(map.get("thisWeekOrderNumber").toString());
            sheet.getRow(8).getCell(7).setCellValue(map.get("thisWeekVisitsNumber").toString());

            sheet.getRow(9).getCell(5).setCellValue(map.get("thisMonthOrderNumber").toString());
            sheet.getRow(9).getCell(7).setCellValue(map.get("thisMonthVisitsNumber").toString());

            List<Map> list = (List<Map>) map.get("hotSetmeal");
            int o = 12;
            for (Map map1 : list) {
                String name = (String) map1.get("name");
                String setmeal_count = map1.get("setmeal_count").toString();
                String proportion = map1.get("proportion").toString();
                int j =4;
                sheet.getRow(o).getCell(j).setCellValue(name);
                j++;
                sheet.getRow(o).getCell(j).setCellValue(setmeal_count);
                j++;
                sheet.getRow(o).getCell(j).setCellValue(proportion);
                o++;
            };

            //设置文件输出流 下载文件
            ServletOutputStream os = response.getOutputStream();
            //设置文件类型
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            //告诉浏览器下载
            response.setHeader("Content-Disposition","attachment;filename=report.xlsx");

            workbook.write(os);
            os.flush();
            os.close();
            is.close();
            workbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

}
