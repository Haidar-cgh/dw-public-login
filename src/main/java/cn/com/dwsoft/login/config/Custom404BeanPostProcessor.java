package cn.com.dwsoft.login.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.valves.ErrorReportValve;
import org.apache.coyote.UpgradeProtocol;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.embedded.TomcatWebServerFactoryCustomizer;
import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Servlet;

@ConditionalOnClass({Servlet.class, Tomcat.class, UpgradeProtocol.class, TomcatWebServerFactoryCustomizer.class})
@Component
@Slf4j
public class Custom404BeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ConfigurableTomcatWebServerFactory) {
            ConfigurableTomcatWebServerFactory configurableTomcatWebServerFactory = (ConfigurableTomcatWebServerFactory) bean;

            addTomcat404CodePage(configurableTomcatWebServerFactory);
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    private void addTomcat404CodePage(ConfigurableTomcatWebServerFactory factory) {
        factory.addContextCustomizers((context) -> {
            String classPath = getClass().getResource("/").getPath();
            String path =  classPath + "404.html";
            ErrorReportValve valve = new ErrorReportValve();
            valve.setProperty("errorCode.404", path);
            valve.setShowServerInfo(false);
            valve.setShowReport(false);
            valve.setAsyncSupported(true);
            context.getParent().getPipeline().addValve(valve);
        });
    }
}
