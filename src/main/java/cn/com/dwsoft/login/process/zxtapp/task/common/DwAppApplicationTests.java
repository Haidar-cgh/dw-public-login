package cn.com.dwsoft.login.process.zxtapp.task.common;

import cn.com.dwsoft.login.process.zxtapp.task.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DwAppApplicationTests {
    @Autowired
    TaskInfoService taskInfoService;
    @Autowired
    RebateFlowService rebateFlowService;
    @Autowired
    UserSignInfoService userSignInfoService;
    @Autowired
    UserAddInfoService userAddInfoService;
    @Autowired
    ConsumptionInfoService consumptionInfoService;
    @Test
    public void test() {
//    taskInfoService.saveFinashTask("13720036073",1);
//    taskInfoService.getNotFinished("13720036073");
//    String taskProgress = taskInfoService.getTaskProgress("13720036073");
//    System.out.println(taskProgress);
//    RebateFlowEntity rebateFlowInfo = rebateFlowService.getRebateFlowInfo("13720036073");
//    System.out.println(rebateFlowInfo);
//    rebateFlowService.saveRebateFlowInfo("13720036073");
//    int i = userSignInfoService.signIn("13720036073");
//    System.out.println(i);
//    List<UserSignInfoEntity> weeks = userSignInfoService.getWeeks("13720036073");
//    System.out.println(weeks);
//    weeks.forEach(str-> System.out.println());

//        UserAddInfoEntity byId = userAddInfoService.getById("13720036073");
//        System.out.println(byId);
//        SMSEntity smsEntity=new SMSEntity();
//        smsEntity.setMdn("13720036073");
//        smsEntity.setSourceText("【话费账单】尊敬的137****6073客户，您08月01日-08月31日共消费38.19元。\n" +
//                "主要消费项目包括：\n" +
//                "-套餐及固定费98.00元；\n" +
//                "-套餐外语音通信费0.19元。\n" +
//                "查询费用详情，请点击https://10086.cn/d/E7v2qy，账单页面访问免流量，更多信息可详询10086。【中国移动】");
//        smsEntity.setSourceType("yd");
//        consumptionInfoService.saveAllSMS(smsEntity);
//        ConsumptionInfoEntity consumptionInfoEntity = consumptionInfoService.queryOne("13720036073");

        System.out.println(taskInfoService.getTasksAndStatus("13720036073"));
    }

}
