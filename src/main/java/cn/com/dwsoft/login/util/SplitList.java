package cn.com.dwsoft.login.util;

import cn.com.dwsoft.authority.exception.ServiceException;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author haider
 * @date 2021年09月01日 18:54
 */
@Data
public class SplitList<T> {
    public static final int size = 999;
    public static <T> List<List<T>> splitList(Collection<T> target){
        return splitList(target,size);
    }
    public static <T> List<List<T>> splitList(Collection<T> target, int size) {
        if (target == null)return new ArrayList<>();
        if (size < 1) throw new ServiceException("SplitList.splitList.size 不能小于1");
        List<List<T>> listArr = new ArrayList<>();
        //获取被拆分的数组个数
        int arrSize = target.size()%size==0?target.size()/size:target.size()/size+1;
        AtomicInteger integer = new AtomicInteger(0);
        List<T> sub = new ArrayList<>();
        int i = 0;
        int j = size * (i + 1) - 1;
        for (T t : target) {
            if(j < integer.getAndIncrement()){
                i++;
                j = size * (i + 1) - 1;
                listArr.add(sub);
                sub = new ArrayList<>();
            }
            sub.add(t);
        }
        if (!sub.isEmpty()){
            listArr.add(sub);
        }
        return listArr;
    }
}
