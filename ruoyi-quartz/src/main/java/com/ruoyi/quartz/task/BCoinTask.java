package com.ruoyi.quartz.task;

import com.ruoyi.coin.task.service.BCoinTaskService;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("bCoinTask")
public class BCoinTask
{

    private static final Logger log = LoggerFactory.getLogger(BCoinTask.class);

    @Autowired
    private BCoinTaskService bCoinTaskService;

    // 刷新B种
    public void updateBCoin() throws JobExecutionException
    {
        log.info("刷新B种--开始");
        try {
            if (checkProd()) {
                bCoinTaskService.updateBCoin();
            }
        } catch (Exception e) {
            log.error("刷新B种--失败", e);
            throw new JobExecutionException(e);
        }
        log.info("刷新B种--结束");
    }

    // 刷新现货交易对
    public void updateBCoinSpot() throws JobExecutionException
    {
        log.info("刷新现货交易对--开始");
        try {
            if (checkProd()) {
                bCoinTaskService.updateBCoinSpot();
            }
        } catch (Exception e) {
            log.error("刷新现货交易对--失败", e);
            throw new JobExecutionException(e);
        }
        log.info("刷新现货交易对--结束");
    }

    // 刷新合约交易对
    public void updateBCoinContract() throws JobExecutionException
    {
        log.info("刷新合约交易对--开始");
        try {
            if (checkProd()) {
                bCoinTaskService.updateBCoinContract();
            }
        } catch (Exception e) {
            log.error("刷新合约交易对--失败", e);
            throw new JobExecutionException(e);
        }
        log.info("刷新合约交易对--结束");
    }

    /**
     * 检查是否prod环境
     * @return
     */
    private boolean checkProd() {
        String filePath = "/home/admin/prod.txt";
        File file = new File(filePath);
        if (file.exists()) {
            log.info("文件存在，prod环境");
        } else {
            log.info("文件不存在，模拟环境");
        }
        return file.exists();
    }

}
