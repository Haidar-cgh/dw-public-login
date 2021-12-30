package cn.com.dwsoft.login.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.github.pagehelper.PageInterceptor;
import com.github.pagehelper.autoconfigure.PageHelperProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@MapperScan(basePackages = {"cn.com.dwsoft.login.process.**.mapper"})
@EnableTransactionManagement
public class MybatisConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }

    @Bean
    public PageInterceptor pageInterceptor(PageHelperProperties pageHelperProperties){
        PageInterceptor interceptor = new PageInterceptor();
        pageHelperProperties.setReasonable("true");
        pageHelperProperties.setSupportMethodsArguments("true");
        pageHelperProperties.setRowBoundsWithCount("true");
        interceptor.setProperties(pageHelperProperties.getProperties());
        return interceptor;
    }

}
