package tests.application.server.rabbitmq;

import com.houkunlin.system.applog.starter.AppLogInfo;
import com.houkunlin.system.applog.starter.AppLogProperties;
import com.houkunlin.system.applog.starter.store.AppLogStore;
import com.houkunlin.system.applog.starter.store.RabbitMqAppLogStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HouKunLin
 */
@ConditionalOnClass(AmqpTemplate.class)
@Configuration
public class StoreConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(StoreConfiguration.class);

    /**
     * 作为日志服务，需要处理来自 rabbitmq 的应用日志信息，然后把日志保存到数据库系统中
     *
     * @param info 应用日志
     */
    @RabbitListener(queues = "#{appLogProperties.mqQueue}")
    public void readLog(AppLogInfo info) {
        logger.info("通过 MQ 获取到日志信息：{}", info);
    }

    @Bean
    public AppLogStore appLogStore(final AmqpTemplate amqpTemplate, final AppLogProperties appLogProperties) {
        return new RabbitMqAppLogStore(amqpTemplate, appLogProperties);
    }
}
