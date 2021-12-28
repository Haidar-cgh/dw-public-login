package cn.com.dwsoft.login.process.zxtapp.task.service.impl;



import cn.com.dwsoft.login.process.zxtapp.task.common.MyGame;
import cn.com.dwsoft.login.process.zxtapp.task.common.TimeUtils;
import cn.com.dwsoft.login.process.zxtapp.task.entity.UserAddInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.ZxgameInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.PackageInfoDao;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.ZxgameInfoDao;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserAddInfoService;
import cn.com.dwsoft.login.process.zxtapp.task.service.ZxgameInfoService;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import cn.com.dwsoft.login.process.zxtapp.util.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Random;

@Service("zxgameInfoService")
public class ZxgameInfoServiceImpl extends ServiceImpl<ZxgameInfoDao, ZxgameInfoEntity> implements ZxgameInfoService {
    @Resource
    UserAddInfoService userAddInfoService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ZxgameInfoEntity> page = this.page(
                new Query<ZxgameInfoEntity>().getPage(params),
                new QueryWrapper<ZxgameInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public ZxgameInfoEntity goldCoin(String mdn) {
        UserAddInfoEntity addInfoEntity = userAddInfoService.getById(mdn);
        if(addInfoEntity.getGoldCoin()-1<0) return null;
        addInfoEntity.setGoldCoin(addInfoEntity.getGoldCoin()-1);
        Random random = new Random();
        ZxgameInfoEntity zxgameInfoEntity=new ZxgameInfoEntity();
        zxgameInfoEntity.setMdn(mdn);
        zxgameInfoEntity.setGameStype("2");
        zxgameInfoEntity.setCoin("2");
        zxgameInfoEntity.setOptionDate(TimeUtils.getStringYMD());
        int i = random.nextInt(10000) + 1;
        int selectCount =0;
        boolean flag=false;
        if (i == MyGame.goldCoin_1) {
             selectCount = this.baseMapper.selectCount(new QueryWrapper<>(zxgameInfoEntity)
                     .eq("option_date", TimeUtils.getStringYMD())
                     .eq("game_stype", "2")
                     .eq("level", "1"));
             if(selectCount>=1){
                 flag=true;
             }else {
                 addInfoEntity.setGoldCoin(addInfoEntity.getGoldCoin() + 500);
                 zxgameInfoEntity.setLevel("1");
                 zxgameInfoEntity.setCounts(500);
                 System.out.println("===========一等奖");
             }
        } else if (i >= MyGame.goldCoin_2[0] && i <= MyGame.goldCoin_2[1]) {
            selectCount = this.baseMapper.selectCount(new QueryWrapper<>(zxgameInfoEntity)
                    .eq("option_date", TimeUtils.getStringYMD())
                    .eq("game_stype", "2")
                    .eq("level", "2"));
            if(selectCount>=5){
                flag=true;
            }else {
                addInfoEntity.setGoldCoin(addInfoEntity.getGoldCoin() + 100);
                zxgameInfoEntity.setLevel("2");
                zxgameInfoEntity.setCounts(100);
                System.out.println("========2等奖");
            }
        } else if (i >= MyGame.goldCoin_3[0] && i <= MyGame.goldCoin_3[1]) {
            selectCount = this.baseMapper.selectCount(new QueryWrapper<>(zxgameInfoEntity)
                    .eq("option_date", TimeUtils.getStringYMD())
                    .eq("game_stype", "2")
                    .eq("level", "3"));
            if(selectCount>=10){
                flag=true;
            }else {
                addInfoEntity.setGoldCoin(addInfoEntity.getGoldCoin() + 50);
                zxgameInfoEntity.setLevel("3");
                zxgameInfoEntity.setCounts(50);
                System.out.println("============3等奖");
            }
        } else if (i >= MyGame.goldCoin_4[0] && i <= MyGame.goldCoin_4[1]) {
            selectCount = this.baseMapper.selectCount(new QueryWrapper<>(zxgameInfoEntity)
                    .eq("option_date", TimeUtils.getStringYMD())
                    .eq("game_stype", "2")
                    .eq("level", "4"));
            if(selectCount>=25){
                flag=true;
            }else {
                addInfoEntity.setGoldCoin(addInfoEntity.getGoldCoin() + 20);
                zxgameInfoEntity.setLevel("4");
                zxgameInfoEntity.setCounts(20);
                System.out.println("===============4等奖");
            }
        } else if (i >= MyGame.goldCoin_5[0] && i <= MyGame.goldCoin_5[1]) {
            selectCount = this.baseMapper.selectCount(new QueryWrapper<>(zxgameInfoEntity)
                    .eq("option_date", TimeUtils.getStringYMD())
                    .eq("game_stype", "2")
                    .eq("level", "5"));
            if(selectCount>=50){
                flag=true;
            }else {
                addInfoEntity.setGoldCoin(addInfoEntity.getGoldCoin() + 10);
                zxgameInfoEntity.setLevel("5");
                zxgameInfoEntity.setCounts(10);
                System.out.println("=================5等奖");
            }
        } else if (i >= MyGame.goldCoin_6[0] && i <= MyGame.goldCoin_6[1]) {
            selectCount = this.baseMapper.selectCount(new QueryWrapper<>(zxgameInfoEntity)
                    .eq("option_date", TimeUtils.getStringYMD())
                    .eq("game_stype", "2")
                    .eq("level", "6"));
            if(selectCount>=100){
                flag=true;
            }else {
                addInfoEntity.setGoldCoin(addInfoEntity.getGoldCoin() + 5);
                zxgameInfoEntity.setLevel("6");
                zxgameInfoEntity.setCounts(5);
                System.out.println("=================6等奖");
            }
        } else if (i >= MyGame.goldCoin_7[0] && i <= MyGame.goldCoin_7[1]) {
            selectCount = this.baseMapper.selectCount(new QueryWrapper<>(zxgameInfoEntity)
                    .eq("option_date", TimeUtils.getStringYMD())
                    .eq("game_stype", "2")
                    .eq("level", "7"));
            if(selectCount>=250){
                flag=true;
            }else {
                addInfoEntity.setGoldCoin(addInfoEntity.getGoldCoin() + 2);
                zxgameInfoEntity.setLevel("7");
                zxgameInfoEntity.setCounts(2);
                System.out.println("7等奖");
            }
        } else if (i >= MyGame.goldCoin_8[0] && i <= MyGame.goldCoin_8[1]) {
            flag=true;
        } else if (i >= MyGame.goldCoin_9[0] && i <= MyGame.goldCoin_9[1]) {
            addInfoEntity.setSilverCoin(addInfoEntity.getSilverCoin()+500);
            zxgameInfoEntity.setLevel("9");
            zxgameInfoEntity.setCoin("1");
            zxgameInfoEntity.setCounts(500);
            System.out.println("9等奖");
        }
        if(flag){
            addInfoEntity.setSilverCoin(addInfoEntity.getSilverCoin()+2000);
            zxgameInfoEntity.setLevel("8");
            zxgameInfoEntity.setCoin("1");
            zxgameInfoEntity.setCounts(2000);
            System.out.println("8等奖");
        }
        userAddInfoService.saveOrUpdate(addInfoEntity);
        this.baseMapper.insert(zxgameInfoEntity);
        return zxgameInfoEntity;
    }

    @Override
    public ZxgameInfoEntity silverCoin(String mdn) {
        UserAddInfoEntity addInfoEntity = userAddInfoService.getById(mdn);
        if(addInfoEntity.getSilverCoin()-50<0) return null;
        addInfoEntity.setSilverCoin(addInfoEntity.getSilverCoin()-50);
        Random random = new Random();
        ZxgameInfoEntity zxgameInfoEntity=new ZxgameInfoEntity();
        zxgameInfoEntity.setMdn(mdn);
        zxgameInfoEntity.setGameStype("1");
        zxgameInfoEntity.setCoin("2");
        zxgameInfoEntity.setOptionDate(TimeUtils.getStringYMD());
        int i = random.nextInt(10000) + 1;
        int selectCount =0;
        boolean flag=false;
        if (i == MyGame.silverCoin_1) {
            selectCount = this.baseMapper.selectCount(new QueryWrapper<>(zxgameInfoEntity)
                    .eq("option_date", TimeUtils.getStringYMD())
                    .eq("game_stype", "1")
                    .eq("level", "1"));
            if(selectCount>=1){
                flag=true;
            }else {
                addInfoEntity.setGoldCoin(addInfoEntity.getGoldCoin() + 100);
                zxgameInfoEntity.setLevel("1");
                zxgameInfoEntity.setCounts(100);
                System.out.println("===========一等奖");
            }
        } else if (i >= MyGame.silverCoin_2[0] && i <= MyGame.silverCoin_2[1]) {
            selectCount = this.baseMapper.selectCount(new QueryWrapper<>(zxgameInfoEntity)
                    .eq("option_date", TimeUtils.getStringYMD())
                    .eq("game_stype", "1")
                    .eq("level", "2"));
            if(selectCount>=3){
                flag=true;
            }else {
                addInfoEntity.setGoldCoin(addInfoEntity.getGoldCoin() + 50);
                zxgameInfoEntity.setLevel("2");
                zxgameInfoEntity.setCounts(50);
                System.out.println("========2等奖");
            }
        } else if (i >= MyGame.silverCoin_3[0] && i <= MyGame.silverCoin_3[1]) {
            selectCount = this.baseMapper.selectCount(new QueryWrapper<>(zxgameInfoEntity)
                    .eq("option_date", TimeUtils.getStringYMD())
                    .eq("game_stype", "1")
                    .eq("level", "3"));
            if(selectCount>=5){
                flag=true;
            }else {
                addInfoEntity.setGoldCoin(addInfoEntity.getGoldCoin() + 20);
                zxgameInfoEntity.setLevel("3");
                zxgameInfoEntity.setCounts(20);
                System.out.println("============3等奖");
            }
        } else if (i >= MyGame.silverCoin_4[0] && i <= MyGame.silverCoin_4[1]) {
            selectCount = this.baseMapper.selectCount(new QueryWrapper<>(zxgameInfoEntity)
                    .eq("option_date", TimeUtils.getStringYMD())
                    .eq("game_stype", "1")
                    .eq("level", "4"));
            if(selectCount>=15){
                flag=true;
            }else {
                addInfoEntity.setGoldCoin(addInfoEntity.getGoldCoin() + 10);
                zxgameInfoEntity.setLevel("4");
                zxgameInfoEntity.setCounts(10);
                System.out.println("===============4等奖");
            }
        } else if (i >= MyGame.silverCoin_5[0] && i <= MyGame.silverCoin_5[1]) {
            selectCount = this.baseMapper.selectCount(new QueryWrapper<>(zxgameInfoEntity)
                    .eq("option_date", TimeUtils.getStringYMD())
                    .eq("game_stype", "1")
                    .eq("level", "5"));
            if(selectCount>=30){
                flag=true;
            }else {
                addInfoEntity.setGoldCoin(addInfoEntity.getGoldCoin() + 5);
                zxgameInfoEntity.setLevel("5");
                zxgameInfoEntity.setCounts(5);
                System.out.println("=================5等奖");
            }
        } else if (i >= MyGame.goldCoin_6[0] && i <= MyGame.goldCoin_6[1]) {
            selectCount = this.baseMapper.selectCount(new QueryWrapper<>(zxgameInfoEntity)
                    .eq("option_date", TimeUtils.getStringYMD())
                    .eq("game_stype", "1")
                    .eq("level", "6"));
            if(selectCount>=150){
                flag=true;
            }else {
                addInfoEntity.setGoldCoin(addInfoEntity.getGoldCoin() + 1);
                zxgameInfoEntity.setLevel("6");
                zxgameInfoEntity.setCounts(1);
                System.out.println("=================6等奖");
            }
        } else if (i >= MyGame.silverCoin_7[0] && i <= MyGame.silverCoin_7[1]) {
            flag=true;
        } else if (i >= MyGame.silverCoin_8[0] && i <= MyGame.silverCoin_8[1]) {
            addInfoEntity.setSilverCoin(addInfoEntity.getSilverCoin()+50);
            zxgameInfoEntity.setLevel("8");
            zxgameInfoEntity.setCoin("1");
            zxgameInfoEntity.setCounts(50);
            System.out.println("8等奖");
        } else if (i >= MyGame.silverCoin_9[0] && i <= MyGame.silverCoin_9[1]) {
            zxgameInfoEntity.setLevel("9");
            zxgameInfoEntity.setCoin("1");
            zxgameInfoEntity.setCounts(0);
            System.out.println("9等奖");
        }
        if(flag){
            addInfoEntity.setSilverCoin(addInfoEntity.getSilverCoin() + 300);
            zxgameInfoEntity.setLevel("7");
            zxgameInfoEntity.setCoin("1");
            zxgameInfoEntity.setCounts(300);
            System.out.println("7等奖");
        }
        userAddInfoService.saveOrUpdate(addInfoEntity);
        this.baseMapper.insert(zxgameInfoEntity);
        return zxgameInfoEntity;
    }

}