package cn.com.dwsoft.login.process.zxtapp.packet.strategy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sqw
 * @version 1.0
 * @description TODO
 * @ClassName AskParam
 * @Date 2020/12/10
 * @since jdk1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AskParam {

    private String key;

    private String type;

    private int value;
}
