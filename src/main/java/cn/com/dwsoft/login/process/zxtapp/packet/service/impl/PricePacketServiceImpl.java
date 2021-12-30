package cn.com.dwsoft.login.process.zxtapp.packet.service.impl;

import cn.com.dwsoft.login.process.zxtapp.packet.dto.CalculatorInpDTO;
import cn.com.dwsoft.login.process.zxtapp.packet.dto.PackageOutDTO;
import cn.com.dwsoft.login.process.zxtapp.packet.dto.SmsEstimateDTO;
import cn.com.dwsoft.login.process.zxtapp.packet.mapper.AskQuestionMapper;
import cn.com.dwsoft.login.process.zxtapp.packet.mapper.PackageMapper;
import cn.com.dwsoft.login.process.zxtapp.packet.pojo.AskQuestionDO;
import cn.com.dwsoft.login.process.zxtapp.packet.pojo.PackageDO;
import cn.com.dwsoft.login.process.zxtapp.packet.pojo.SmsEstimateDO;
import cn.com.dwsoft.login.process.zxtapp.packet.service.IPricePacketService;
import cn.com.dwsoft.login.process.zxtapp.packet.strategy.AskParam;
import cn.com.dwsoft.login.process.zxtapp.packet.strategy.AskStrategy;
import cn.com.dwsoft.login.process.zxtapp.packet.strategy.IParamStrategy;
import cn.com.dwsoft.login.process.zxtapp.packet.strategy.SmsEstimation;
import cn.com.dwsoft.login.process.zxtapp.task.entity.ConsumptionInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.PackageInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.service.ConsumptionInfoService;
import cn.com.dwsoft.login.process.zxtapp.task.service.impl.PackageInfoServiceImpl;
import cn.com.dwsoft.login.process.zxtapp.util.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sqw
 * @version 1.0
 * @description 套餐服务实现层
 * @ClassName PricePacketServiceImpl
 * @Date 2020/12/8
 * @since jdk1.8
 */
@Service
@Slf4j
public class PricePacketServiceImpl implements IPricePacketService {

    @Resource
    private PackageMapper packageMapper;
    @Autowired
    private PackageInfoServiceImpl packageInfoService;

    @Resource
    private AskQuestionMapper askQuestionMapper;

    @Resource
    private ConsumptionInfoService consumptionInfoService;

    private List<String> operatorsArr=Arrays.asList("联通","移动","电信");

    private static Map<String,List<AskParam>> map=new HashMap<>();

    static {
        List<AskParam> list=new ArrayList();
        list.add(new AskParam("money","S1",0));
        AskParam askParam=new AskParam("money","S1",0);
        map.put("money",new ArrayList());
        map.put("minutes",new ArrayList());
        map.put("flow",new ArrayList());
        map.put("daily_rental",new ArrayList());
        map.put("family_share",new ArrayList());
        map.put("fixed_network",new ArrayList<>());
        map.put("other_flow",new ArrayList<>());
    }

    @Override
    public List<PackageOutDTO> getPacketOfCalculator(CalculatorInpDTO calculatorInpDTO) {
        if(calculatorInpDTO.getPage()!=0){
            PageHelper.startPage(calculatorInpDTO.getPage(),10,false,false,false);
        }
        QueryWrapper<PackageInfoEntity> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(calculatorInpDTO.getProvince())){
            wrapper.like("province", calculatorInpDTO.getProvince());
        }
        if(StringUtils.isNotBlank(calculatorInpDTO.getOperators()) && operatorsArr.contains(calculatorInpDTO.getOperators())){
            wrapper.like("operators",calculatorInpDTO.getOperators());
        }
        if (StringUtils.isNotBlank(calculatorInpDTO.getSecondaryCard())){
            if("contain".equalsIgnoreCase(calculatorInpDTO.getSecondaryCard())){
                wrapper.eq("secondary_card","1");
            }else if("notIncluded".equalsIgnoreCase(calculatorInpDTO.getSecondaryCard())){
                wrapper.eq("secondary_card","0");
            }
            wrapper.ne("package_lable","宽带");
        }

        //（all 不限,fiveG： 5G，homeFuse： 家庭融合，dayCard：日租卡）
        switch (calculatorInpDTO.getUniversalType()){
            case "all" :
                break;
            case "fiveG" :
                wrapper.like("package_name","5G");
                break;
            case "homeFuse" :
                wrapper.like("package_lable","合约");
                break;
            case "dayCard":
                wrapper.like("package_lable","日租卡");
                break;
            default:
        }
        //（all 不限,ltNine： 10G以内，nineToThirty： 10G-30G，gtThirty：30G以上）
        switch (calculatorInpDTO.getUniversalFlow()){
            case "all" :
                break;
            case "ltNine" :
                wrapper.lt("universal_flow",10);
                break;
            case "nineToThirty" :
                wrapper.between("universal_flow",10,30);
                break;
            case "gtThirty":
                wrapper.gt("universal_flow",30);
                break;
            default:
        }
        List<PackageOutDTO> calculatorPackages;
        List<PackageInfoEntity> packageInfoEntities = packageInfoService.list(wrapper);
        if (packageInfoEntities != null && !packageInfoEntities.isEmpty()){
            calculatorPackages = packageInfoEntities.stream().map(obj -> BeanUtils.eToT(obj,PackageOutDTO.class)).collect(Collectors.toList());
        }else {
            calculatorPackages = new ArrayList<>();
        }
        return calculatorPackages;
    }

    //    @Override
//    public List<PackageOutDTO> getPacketOfCalculator(CalculatorInpDTO calculatorInpDTO) {
//        PackageDO pricePacketDO=BeanUtils.eToT(calculatorInpDTO,PackageDO.class);
//        StringBuffer sb= new StringBuffer();
//        if(StringUtils.isNotBlank(calculatorInpDTO.getProvince())){
//            sb.append(" AND t.province like concat('%',#{obj.province},'%')");
//        }
//
//        if(StringUtils.isNotBlank(calculatorInpDTO.getOperators())){
//            for(int n=0;n<operatorsArr.length;n++){
//                if(calculatorInpDTO.getOperators().contains(operatorsArr[n])){
//                    sb.append(" AND t.operators like '%"+operatorsArr[n]+"%'");
//                    break;
//                }
//            }
//           /* sb.append(" AND t.operators=#{obj.operators}");*/
//        }
//        switch (calculatorInpDTO.getType()){
//            case "LM" :
//                if(calculatorInpDTO.getGearValue()>0)
//                    sb.append(" AND t.gear_value<=#{obj.gearValue,jdbcType=INTEGER} AND t.gear_value>0");
//                if(calculatorInpDTO.getGearValue()>50){
//                    if(calculatorInpDTO.getUniversalFlow()!=0)
//                        sb.append(" AND t.universal_flow>=#{obj.universalFlow,jdbcType=INTEGER}");
//                    if(calculatorInpDTO.getUniversalVoice()!=0)
//                        sb.append(" AND t.universal_voice>=#{obj.universalVoice,jdbcType=INTEGER}");
//                }
//                break;
//            case "MF" :
//                if(calculatorInpDTO.getUniversalFlow()>0)
//                    sb.append(" AND t.universal_flow>=#{obj.universalFlow,jdbcType=INTEGER}");
//                if(calculatorInpDTO.getUniversalFlow()<10){
//                    if(calculatorInpDTO.getGearValue()!=0)
//                        sb.append(" AND t.gear_value<=#{obj.gearValue,jdbcType=INTEGER}");
//                    if(calculatorInpDTO.getUniversalVoice()!=0)
//                        sb.append(" AND t.universal_voice>=#{obj.universalVoice,jdbcType=INTEGER}");
//                }
//                break;
//            case "MV" :
//                if(calculatorInpDTO.getUniversalVoice()>0)
//                    sb.append(" AND t.universal_voice>=#{obj.universalVoice,jdbcType=INTEGER}");
//                if(calculatorInpDTO.getUniversalVoice()<300){
//                    if(calculatorInpDTO.getGearValue()!=0)
//                        sb.append(" AND t.gear_value<=#{obj.gearValue,jdbcType=INTEGER}");
//                    if(calculatorInpDTO.getUniversalFlow()!=0)
//                        sb.append(" AND t.universal_flow>=#{obj.universalFlow,jdbcType=INTEGER}");
//                }
//                break;
//            default:
//
//        }
///*        if(StringUtils.isNotBlank(calculatorInpDTO.getGearValue())
//                && ((StringUtils.equals(calculatorInpDTO.getType(),"MF")
//                && Integer.parseInt(calculatorInpDTO.getUniversalFlow())<=10)
//                || (StringUtils.equals(calculatorInpDTO.getType(),"MV")
//                && Integer.parseInt(calculatorInpDTO.getUniversalVoice())<=300))){
//            sb.append(" AND t.gear_value<=#{obj.gearValue} AND t.gear_value>0");
//        }
//        if(StringUtils.isNotBlank(calculatorInpDTO.getUniversalFlow())
//                && ((StringUtils.equals(calculatorInpDTO.getType(),"LM")
//                && Integer.parseInt(calculatorInpDTO.getGearValue())>=50)
//                || (StringUtils.equals(calculatorInpDTO.getType(),"MV")
//                && Integer.parseInt(calculatorInpDTO.getUniversalVoice())<=300))){
//            sb.append(" AND t.universal_flow>=#{obj.universalFlow}");
//        }
//        if(StringUtils.isNotBlank(calculatorInpDTO.getUniversalVoice())
//                && ((StringUtils.equals(calculatorInpDTO.getType(),"LM")
//                && Integer.parseInt(calculatorInpDTO.getGearValue())>=50)
//                || (StringUtils.equals(calculatorInpDTO.getType(),"MF")
//                && Integer.parseInt(calculatorInpDTO.getUniversalFlow())<=10))){
//            sb.append(" AND t.universal_voice>=#{obj.universalVoice}");
//        }*/
//        if(calculatorInpDTO.getSecondaryCard()!=null && calculatorInpDTO.getSecondaryCard()!=0){
//            sb.append(" AND t.secondary_card=1");
//        }
//        if(calculatorInpDTO.getBroadbandBandwidth()!=null && calculatorInpDTO.getBroadbandBandwidth()!=0){
//            sb.append(" AND (t.broadband_bandwidth is not null or t.broadband_bandwidth!='') ");
//        }
//        if(calculatorInpDTO.getPage()!=0){
//            PageHelper.startPage(calculatorInpDTO.getPage(),10,false,false,false);
//        }
//        List<PackageDO> calculatorOutDTOS = packageMapper.findByCondition(sb.toString(),pricePacketDO);
//        List<PackageOutDTO> calculatorPackages = calculatorOutDTOS.stream().map(obj -> BeanUtils.eToT(obj,PackageOutDTO.class)).collect(Collectors.toList());
//        return calculatorPackages;
//    }

    @Override
    public int smsMoneyEstimate(String mdn, String totalMoney) {
        SmsEstimateDO smsEstimateDO=packageMapper.getSmsEstimate(mdn);
        if(smsEstimateDO==null){
            smsEstimateDO=new SmsEstimateDO();
            smsEstimateDO.setMdn(mdn);
            smsEstimateDO.setModifyFlag("0");
        }
        if(StringUtils.equals("0",smsEstimateDO.getModifyFlag())){
            int money = SmsEstimation.moneyArithmetic(Float.parseFloat(totalMoney));
            smsEstimateDO.setMoneyVal(money);
            return packageMapper.insertSmsEstimate(smsEstimateDO);
        }
        return 0;
    }
    //流量以G为单位
    @Override
    public int smsFlowEstimate(String mdn, String usedFlow) {
        SmsEstimateDO smsEstimateDO=packageMapper.getSmsEstimate(mdn);
        if(smsEstimateDO==null){
            smsEstimateDO=new SmsEstimateDO();
            smsEstimateDO.setMdn(mdn);
            smsEstimateDO.setModifyFlag("0");
        }
        if(StringUtils.equals("0",smsEstimateDO.getModifyFlag())){
            int flow = SmsEstimation.flowArithmetic(Float.parseFloat(usedFlow));
            smsEstimateDO.setFlowVal(flow);
            return packageMapper.insertSmsEstimate(smsEstimateDO);
        }
        return 0;
    }

    @Override
    public int smsVoiceEstimate(String mdn,String total,String unUsed) {
        SmsEstimateDO smsEstimateDO=packageMapper.getSmsEstimate(mdn);
        if(smsEstimateDO==null){
            smsEstimateDO=new SmsEstimateDO();
            smsEstimateDO.setMdn(mdn);
            smsEstimateDO.setModifyFlag("0");
        }
        if(StringUtils.equals("0",smsEstimateDO.getModifyFlag())){
            int voice = SmsEstimation.voiceArithmetic(Float.parseFloat(total),Float.parseFloat(unUsed));
            smsEstimateDO.setVoiceVal(voice);
            return packageMapper.insertSmsEstimate(smsEstimateDO);
        }
        return 0;
    }

    @Override
    public SmsEstimateDTO smsEstimateVal(String mdn) {
        ConsumptionInfoEntity consumptionInfoEntity =consumptionInfoService.queryOne(mdn);
        if(consumptionInfoEntity==null){
            consumptionInfoEntity=new ConsumptionInfoEntity();
        }
        String voiceTotal=consumptionInfoEntity.getTotalVoice()==null?"0":consumptionInfoEntity.getTotalVoice();
        String voiceUnuserd=consumptionInfoEntity.getRemainingVoice()==null?"0":consumptionInfoEntity.getRemainingVoice();
        String totalMoney=consumptionInfoEntity.getAccountAmount()==null?"0":consumptionInfoEntity.getAccountAmount();
        String usedFlow=consumptionInfoEntity.getUsedFlow()==null?"0":consumptionInfoEntity.getUsedFlow();
        SmsEstimateDO smsEstimateDO=packageMapper.getSmsEstimate(mdn);
        if(smsEstimateDO==null){
            smsEstimateDO=new SmsEstimateDO();
            smsEstimateDO.setMdn(mdn);
            smsEstimateDO.setModifyFlag("0");
        }
        if(StringUtils.equals("0",smsEstimateDO.getModifyFlag())){
            int voice = SmsEstimation.voiceArithmetic(Float.parseFloat(voiceTotal),Float.parseFloat(voiceUnuserd));
            smsEstimateDO.setVoiceVal(voice);
            int money = SmsEstimation.moneyArithmetic(Float.parseFloat(totalMoney));
            smsEstimateDO.setMoneyVal(money);
            int flow = SmsEstimation.flowArithmetic(Float.parseFloat(usedFlow));
            smsEstimateDO.setFlowVal(flow);
            int num = packageMapper.insertSmsEstimate(smsEstimateDO);
        }
        //smsEstimateDO=packageMapper.getSmsEstimate(mdn);
        return BeanUtils.eToT(smsEstimateDO,SmsEstimateDTO.class);
    }

    @Override
    public int modifyEstimate(String mdn, String type, String val) {
        SmsEstimateDO smsEstimateDO=packageMapper.getSmsEstimate(mdn);
        if(smsEstimateDO==null){
            smsEstimateDO=new SmsEstimateDO();
            smsEstimateDO.setMdn(mdn);
        }
        switch (type){
            case "money" :
                smsEstimateDO.setMoneyVal(Integer.parseInt(val));
                break;
            case "flow" :
                smsEstimateDO.setFlowVal(Integer.parseInt(val));
                break;
            case "voice" :
                smsEstimateDO.setVoiceVal(Integer.parseInt(val));
                break;
            default:
                return 0;
        }
        smsEstimateDO.setModifyFlag("1");
        return packageMapper.insertSmsEstimate(smsEstimateDO);
    }

    @Override
    public PackageOutDTO packageDetail(String key) {
        PackageDO packageDO = packageMapper.findById(key);
        return BeanUtils.eToT(packageDO,PackageOutDTO.class);
    }

    /*@Override
    public List<PackageOutDTO> getPacketOfRecommend(String mdn,String province,String operators) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        List<AskQuestionDO> questions = askQuestionMapper.findAskAnswer(mdn);
        SmsEstimateDO smsEstimate = askQuestionMapper.findSmsEstimate(mdn);
        parseQuestion(questions);
        StringBuffer sb=new StringBuffer();
        PackageDO packageDO=new PackageDO();
        sb.append(" AND t.province like concat('%',#{obj.province},'%')");
        packageDO.setProvince(province);
        for(int n=0;n<operatorsArr.length;n++){
            if(operators.contains(operatorsArr[n])){
                sb.append(" AND t.operators like '%"+operatorsArr[n]+"%'");
                break;
            }
        }

        for(Map.Entry<String,List<AskParam>> entry : map.entrySet()){
            switch (entry.getKey()){
                case "money" :
                    IParamStrategy moneyStrategy=new AskStrategy(smsEstimate.getMoneyVal(),"money",entry.getValue());
                    long money=moneyStrategy.arithmetic();
                    if(money!=0){
                        packageDO.setGearValue(money);
                        sb.append(" AND t.gear_value<=#{obj.gearValue}");
                    }
                    break;
                case "minutes" :
                    IParamStrategy voiceStrategy=new AskStrategy(smsEstimate.getVoiceVal(),"minutes",entry.getValue());
                    long voice=voiceStrategy.arithmetic();
                    if(voice!=0){
                        packageDO.setUniversalVoice(voice);
                        sb.append(" AND t.universal_voice>=#{obj.universalVoice}");
                    }
                    break;
                case "flow" :
                    IParamStrategy flowStrategy=new AskStrategy(smsEstimate.getFlowVal(),"flow",entry.getValue());
                    long flow=flowStrategy.arithmetic();
                    if(flow!=0){
                        packageDO.setUniversalFlow(flow);
                        sb.append(" AND t.universal_flow>=#{obj.universalFlow}");
                    }
                    break;
                case "daily_rental" :
                    IParamStrategy dailyRentalStrategy=new AskStrategy(0,"daily_rental",entry.getValue());
                    long dailyRental=dailyRentalStrategy.arithmetic();
                    if(dailyRental>0){
                        sb.append(" AND t.package_lable like '%日租%'");
                    }
                    break;
                case "family_share" ://可办副卡数量
                    IParamStrategy familyShareStrategy=new AskStrategy(0,"family_share",entry.getValue());
                    long familyShare=familyShareStrategy.arithmetic();
                    if(familyShare!=0){
                        packageDO.setSecondaryCardCount(familyShare);
                        sb.append(" AND t.secondary_card_count>=#{obj.secondaryCardCount}");
                    }
                    break;
                case "fixed_network" ://宽带带宽
                    IParamStrategy fixedNetworkStrategy=new AskStrategy(0,"fixed_network",entry.getValue());
                    long fixedNetwork=fixedNetworkStrategy.arithmetic();
                    if(fixedNetwork!=0){
                        packageDO.setBroadbandBandwidth(fixedNetwork);
                        sb.append(" AND t.broadband_bandwidth>=#{obj.broadbandBandwidth}");
                    }
                    break;
                case "other_flow" ://定向流量
                    IParamStrategy otherFlowStrategy=new AskStrategy(0,"other_flow",entry.getValue());
                    long otherFlow=otherFlowStrategy.arithmetic();
                    if(otherFlow!=0){
                        packageDO.setDirectionalFlow(otherFlow);
                        sb.append(" AND t.directional_flow>=#{obj.directionalFlow}");
                    }
                    break;
                case "unlimited" :
                    IParamStrategy unlimitedStrategy=new AskStrategy(0,"unlimited",entry.getValue());
                    long unlimitedRental=unlimitedStrategy.arithmetic();
                    if(unlimitedRental>0){
                        sb.append(" AND t.package_lable like '%冰激凌%'");
                    }
                    break;
                default:
                    break;
            }
        }
        PageHelper.startPage(1,3);
        List<PackageDO> calculatorOutDTOS = packageMapper.findByCondition(sb.toString(),packageDO);
        List<PackageOutDTO> calculatorPackages = calculatorOutDTOS.stream().map(obj -> BeanUtils.eToT(obj,PackageOutDTO.class)).collect(Collectors.toList());
        return calculatorPackages;
    }*/

    @Override
    public List<PackageOutDTO> getPacketOfRecommend(String mdn,String province,String operators) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        List<AskQuestionDO> questions = askQuestionMapper.findAskAnswer(mdn);
        SmsEstimateDO smsEstimate = askQuestionMapper.findSmsEstimate(mdn);
        parseQuestion(questions);
        for(int n=0;n<operatorsArr.size();n++){
            if(operators.contains(operatorsArr.get(n))){
                operators=operatorsArr.get(n);
                break;
            }
        }
        PackageDO packageDO=new PackageDO();
        for(Map.Entry<String,List<AskParam>> entry : map.entrySet()){
            switch (entry.getKey()){
                case "money" :
                    IParamStrategy moneyStrategy=new AskStrategy(smsEstimate.getMoneyVal(),"money",entry.getValue());
                    long money=moneyStrategy.arithmetic();
                    packageDO.setGearValue(money);
                    break;
                case "minutes" :
                    IParamStrategy voiceStrategy=new AskStrategy(smsEstimate.getVoiceVal(),"minutes",entry.getValue());
                    long voice=voiceStrategy.arithmetic();
                    packageDO.setUniversalVoice(voice);
                    break;
                case "flow" :
                    IParamStrategy flowStrategy=new AskStrategy(smsEstimate.getFlowVal(),"flow",entry.getValue());
                    long flow=flowStrategy.arithmetic();
                    packageDO.setUniversalFlow(flow);
                    break;
                default:
                    break;
            }
        }
        Set<Integer> isExists=new HashSet<>();
        List<PackageOutDTO> calculatorPackages=new ArrayList<>();
        List<PackageDO> packages = packageMapper.findByProOpr(province,operators);
        //判断输入流量是否超出最大限，如果超出，选择一个最大限流量套餐、
        Optional<PackageDO>  maxPkgOpt = packages.stream()
                .filter(Objects::nonNull)
                .max(Comparator.comparingLong(PackageDO::getUniversalFlow));
        //判断输入分钟数是否超出最大限，如果超出，选择一个最大限分钟数套餐
        Optional<PackageDO>  maxMinutesOpt = packages.stream()
                .filter(Objects::nonNull)
                .max(Comparator.comparingLong(PackageDO::getUniversalVoice));
        PackageDO maxFlowPkg = maxPkgOpt.get();
        PackageDO maxVoicePkg = maxMinutesOpt.get();
        if(maxFlowPkg.getUniversalFlow()<=packageDO.getUniversalFlow()){
            calculatorPackages.add(BeanUtils.eToT(maxFlowPkg,PackageOutDTO.class));
            Optional<PackageDO>  minutesOpt = packages.stream()
                    .filter(Objects::nonNull)
                    .filter(obj -> obj.getUniversalVoice()<=packageDO.getUniversalVoice())
                    .sorted(Comparator.comparingLong(PackageDO::getUniversalVoice).reversed()
                            .thenComparing(Comparator.comparingLong(PackageDO::getUniversalFlow).reversed()
                                    .thenComparing(Comparator.comparingLong(PackageDO::getGearValue).reversed()))).findFirst();
            if(!isExists.contains(minutesOpt.get().getId())){
                calculatorPackages.add(BeanUtils.eToT(minutesOpt.get(),PackageOutDTO.class));
                isExists.add(minutesOpt.get().getId());
            }
            Optional<PackageDO>  moneyOpt = packages.stream()
                    .filter(Objects::nonNull)
                    .filter(obj -> obj.getGearValue()<=packageDO.getGearValue())
                    .sorted(Comparator.comparingLong(PackageDO::getUniversalFlow).reversed()
                            .thenComparing(Comparator.comparingLong(PackageDO::getUniversalVoice).reversed()
                                    .thenComparing(Comparator.comparingLong(PackageDO::getGearValue).reversed()))).findFirst();
            if(!isExists.contains(moneyOpt.get().getId())){
                calculatorPackages.add(BeanUtils.eToT(moneyOpt.get(),PackageOutDTO.class));
                isExists.add(moneyOpt.get().getId());
            }
        }else if(maxVoicePkg.getUniversalVoice()<=packageDO.getUniversalVoice()){
            calculatorPackages.add(BeanUtils.eToT(maxVoicePkg,PackageOutDTO.class));
            Optional<PackageDO>  flowOpt = packages.stream()
                    .filter(Objects::nonNull)
                    .filter(obj -> obj.getUniversalFlow()<=packageDO.getUniversalFlow())
                    .sorted(Comparator.comparingLong(PackageDO::getUniversalFlow).reversed()
                            .thenComparing(Comparator.comparingLong(PackageDO::getUniversalVoice).reversed()
                                    .thenComparing(Comparator.comparingLong(PackageDO::getGearValue).reversed()))).findFirst();
            if(!isExists.contains(flowOpt.get().getId())){
                calculatorPackages.add(BeanUtils.eToT(flowOpt.get(),PackageOutDTO.class));
                isExists.add(flowOpt.get().getId());
            }

            Optional<PackageDO>  moneyOpt = packages.stream()
                    .filter(Objects::nonNull)
                    .filter(obj -> obj.getGearValue()<=packageDO.getGearValue())
                    .sorted(Comparator.comparingLong(PackageDO::getUniversalVoice).reversed()
                            .thenComparing(Comparator.comparingLong(PackageDO::getUniversalFlow).reversed()
                                    .thenComparing(Comparator.comparingLong(PackageDO::getGearValue).reversed()))).findFirst();
            if(!isExists.contains(moneyOpt.get().getId())){
                calculatorPackages.add(BeanUtils.eToT(moneyOpt.get(),PackageOutDTO.class));
                isExists.add(moneyOpt.get().getId());
            }

        }else{
            //资费与流量组合
            Optional<PackageDO> moneyAndFlowOpt = packages.stream()
                    .filter(Objects::nonNull)
                    .filter(obj -> obj.getGearValue()<=packageDO.getGearValue())
                    .filter(obj -> obj.getUniversalFlow()>=packageDO.getUniversalFlow())
                    .sorted(Comparator.comparingLong(PackageDO::getGearValue).reversed()).findFirst();
            moneyAndFlowOpt.ifPresent(o ->  {
                if(!isExists.contains(moneyAndFlowOpt.get().getId())){
                    calculatorPackages.add(BeanUtils.eToT(moneyAndFlowOpt.get(),PackageOutDTO.class));
                    isExists.add(moneyAndFlowOpt.get().getId());
                }
            });
            //资费与语音组合
            Optional<PackageDO> moneyAndVoiceOpt = packages.stream()
                    .filter(Objects::nonNull)
                    .filter(obj -> obj.getGearValue()<=packageDO.getGearValue())
                    .filter(obj -> obj.getUniversalVoice()>=packageDO.getUniversalVoice())
                    .sorted(Comparator.comparingLong(PackageDO::getGearValue).reversed()).findFirst();
            moneyAndVoiceOpt.ifPresent(o -> {
                if(!isExists.contains(moneyAndVoiceOpt.get().getId())){
                    calculatorPackages.add(BeanUtils.eToT(moneyAndVoiceOpt.get(),PackageOutDTO.class));
                    isExists.add(moneyAndVoiceOpt.get().getId());
                }
            });
            //流量与语音组合
            Optional<PackageDO> flowAndVoiceOpt = packages.stream()
                    .filter(Objects::nonNull)
                    .filter(obj -> obj.getUniversalFlow()>=packageDO.getUniversalFlow())
                    .filter(obj -> obj.getUniversalVoice()>=packageDO.getUniversalVoice())
                    .sorted(Comparator.comparingLong(PackageDO::getUniversalFlow)
                            .thenComparing(Comparator.comparingLong(PackageDO::getUniversalVoice))).findFirst();
            flowAndVoiceOpt.ifPresent(o -> {
                if(!isExists.contains(flowAndVoiceOpt.get().getId())){
                    calculatorPackages.add(BeanUtils.eToT(flowAndVoiceOpt.get(),PackageOutDTO.class));
                    isExists.add(flowAndVoiceOpt.get().getId());
                }
            });
        }
        return calculatorPackages;
    }

    private void parseQuestion(List<AskQuestionDO> questions) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        for (AskQuestionDO obj : questions) {
            String options=obj.getOptions();
            String[] optionArr = options.split(",");
            for(int i=0;i<optionArr.length;i++){
                String option=optionArr[i];
                toAskParam(obj,option);
            }
        }
    }

    private void toAskParam(AskQuestionDO question,String flag) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        try {
            Class<?> cls = question.getClass();
            String[] arr={"p","t","v"};
            AskParam askParam=new AskParam();
            for(int i=0;i<arr.length;i++){
                PropertyDescriptor pd = new PropertyDescriptor("option_"+flag+"_"+arr[i], cls);
                Method rM = pd.getReadMethod();//获得读方法
                String num = (String) rM.invoke(question);
                switch (arr[i]){
                    case "p":
                        askParam.setKey(num);
                        break;
                    case "t":
                        askParam.setType(num);
                        break;
                    case "v":
                        askParam.setValue(Integer.parseInt(num));
                        break;
                    default:
                        continue;
                }
            }
            if(map.containsKey(askParam.getKey())){
                map.get(askParam.getKey()).add(askParam);
            }else{
                List<AskParam> list = new ArrayList<>();
                list.add(askParam);
                map.put(askParam.getKey(),list);
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
