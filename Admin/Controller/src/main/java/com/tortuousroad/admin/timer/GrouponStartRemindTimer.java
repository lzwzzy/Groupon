package com.tortuousroad.admin.timer;

import com.tortuousroad.framework.base.context.SpringApplicationContext;
import com.tortuousroad.support.message.entity.Message;
import com.tortuousroad.support.message.service.MessageService;
import com.tortuousroad.support.remind.entity.StartRemind;
import com.tortuousroad.support.remind.service.StartRemindService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 开团提醒定时
 */
//@Component
public class GrouponStartRemindTimer {

    private StartRemindService startRemindService = SpringApplicationContext.getBean(StartRemindService.class);

    private MessageService messageService = SpringApplicationContext.getBean(MessageService.class);

    @PostConstruct
    public void start() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<StartRemind> startReminds = startRemindService.getByTimeInterval();
                startReminds.forEach(startRemind -> {
                    Message message = new Message();
                    message.setUserId(startRemind.getUserId());
                    message.setTitle("开团提醒");
                    message.setContent(startRemind.getDealTitle() + "将在24小时后开团.");
                    //FIXME 循环中操作数据库性能低,需要改进
                    messageService.save(message);
                    startRemindService.deleteById(startRemind.getId());
                });
            }
        }, 10 * 1000, 60 * 1000);
    }

}
