package com.ruoyi.biz.common.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ruoyi.biz.common.ApiCommonService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.payment.HuoJianPaymentUtil;
import com.ruoyi.common.utils.payment.NineBrotherPaymentUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.payment.RendePaymentUtil;
import com.ruoyi.common.utils.payment.SiFangPaymentUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 策略Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Service
public class ApiCommonServiceImpl implements ApiCommonService
{

    private static final Logger log = LoggerFactory.getLogger(ApiCommonServiceImpl.class);

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Override
    public String upload(MultipartFile file, String ip) throws Exception {
        // 1:阿里云 默认2:亚马逊 3:ftp
        String ossType = iFaRiskConfigService.getConfigValue("oss.type", "2");
        if ("1".equals(ossType)) {
            return uploadAliyun(file);
        } else if ("2".equals(ossType)) {
            return uploadAmazon(file);
        } else if ("3".equals(ossType)) {
            return uploadFtp(file, ip);
        } else {
            throw new ServiceException("OSS type error", HttpStatus.ERROR);
        }
    }

    /**
     * 上传到ftp
     * @param file
     * @return
     * @throws Exception
     */
    private String uploadFtp(MultipartFile file, String ip) throws Exception {
        String host = iFaRiskConfigService.getConfigValue("ftp.host", null);
        String port = iFaRiskConfigService.getConfigValue("ftp.port", null);
        String username = iFaRiskConfigService.getConfigValue("ftp.username", null);
        String password = iFaRiskConfigService.getConfigValue("ftp.password", null);
        String remotePath = iFaRiskConfigService.getConfigValue("ftp.remotePath", null);

        String fileFullPath = remotePath + FileUploadUtils.extractFilename(file);

        if (StringUtils.isEmpty(host) || StringUtils.isEmpty(port) || StringUtils.isEmpty(username) ||
                StringUtils.isEmpty(password) || StringUtils.isEmpty(remotePath)) {
            throw new ServiceException("ftp config error", HttpStatus.ERROR);
        }

        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
        String fileName = null;
        try {
            ftpClient.connect(host, Integer.parseInt(port));
            ftpClient.login(username, password);
            // 被动模式
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            int reply = ftpClient.getReplyCode();
            log.error("ftp连接成功：" + reply);
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return null;
            }

            fileName = fileFullPath.substring(fileFullPath.lastIndexOf("/") + 1);
            String filePath = fileFullPath.substring(0, fileFullPath.lastIndexOf("/"));

            // 创建目录
            String basePath = "/";
            for (String p : filePath.split("/")) {
                basePath += (p + "/");
                boolean hasPath = ftpClient.changeWorkingDirectory(basePath);
                if (!hasPath) {
                    // 创建目录，一次只能创建一个目录
                    ftpClient.makeDirectory(basePath);
                }
            }

            ftpClient.changeWorkingDirectory(filePath);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            boolean flag = ftpClient.storeFile(fileName, file.getInputStream());
            log.error("ftp上传结果：" + flag);
        } catch (SocketException e) {
            log.error("ftp 连接失败", e);
        } catch (IOException e) {
            log.error("ftp 上传失败", e);
        } finally {
            try {
                if (file.getInputStream() != null) {
                    file.getInputStream().close();
                }
                ftpClient.logout();
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "http://" + ip + "/ftp" + fileFullPath;
    }

    /**
     * 上传到阿里云OSS
     * @param file
     * @return
     * @throws Exception
     */
    private String uploadAliyun(MultipartFile file) throws Exception {
        String endpoint = iFaRiskConfigService.getConfigValue("aliyunoss.endpoint", null);
        String accessKeyId = iFaRiskConfigService.getConfigValue("aliyunoss.accessKeyId", null);
        String accessKeySecret = iFaRiskConfigService.getConfigValue("aliyunoss.accessKeySecret", null);
        String bucketName = iFaRiskConfigService.getConfigValue("aliyunoss.bucketName", null);
        String filehost = iFaRiskConfigService.getConfigValue("aliyunoss.filehost", null);
        String url = iFaRiskConfigService.getConfigValue("aliyunoss.url", null);

        if (StringUtils.isEmpty(endpoint) || StringUtils.isEmpty(accessKeyId) || StringUtils.isEmpty(accessKeySecret) ||
                StringUtils.isEmpty(bucketName) || StringUtils.isEmpty(filehost) || StringUtils.isEmpty(url)) {
            throw new ServiceException("Aliyun OSS config error", HttpStatus.ERROR);
        }

        // 生成 OSSClient
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 原始文件名称
        // String originalFilename = file.getOriginalFilename();

        // 编码文件名
        String filePathName = FileUploadUtils.extractFilename(file);
        // 文件路径名称
        filePathName = filehost + "/" + filePathName;
        try {
            ossClient.putObject(bucketName, filePathName, file.getInputStream());
        } catch (IOException e) {
            log.error("uploadOSS", e);
            throw new ServiceException("OSS error", HttpStatus.ERROR);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        String result =  url + "/" + filePathName;
        return result;
    }

    /**
     * 上传到亚马逊s3
     * @param file
     * @return
     * @throws Exception
     */
    private String uploadAmazon(MultipartFile file) throws Exception {
        String domain = iFaRiskConfigService.getConfigValue("amazon.domain", null);
        String accessKey = iFaRiskConfigService.getConfigValue("amazon.accessKey", null);
        String secretKey = iFaRiskConfigService.getConfigValue("amazon.secretKey", null);
        String bucketName = iFaRiskConfigService.getConfigValue("amazon.bucketName", null);
        String regionName = iFaRiskConfigService.getConfigValue("amazon.regionName", null);

        if (StringUtils.isEmpty(domain) || StringUtils.isEmpty(accessKey) || StringUtils.isEmpty(secretKey) ||
                StringUtils.isEmpty(bucketName) || StringUtils.isEmpty(regionName)) {
            throw new ServiceException("Amazon OSS config error", HttpStatus.ERROR);
        }

        String filePath = FileUploadUtils.extractFilename(file);

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(regionName))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
        try {
            File uploadFile = convert(file);
            s3client.putObject(new PutObjectRequest(bucketName, filePath, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));

            if (uploadFile.exists()) {
                uploadFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return domain.concat("/" + filePath);
    }

    /**
     * 文件格式转换
     * @param file
     * @return
     * @throws IOException
     */
    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        try (InputStream in = file.getInputStream();
             OutputStream out = new FileOutputStream(convFile)) {
            byte[] bytes = new byte[1024];
            int read;
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            return convFile;
        }
    }

    @Override
    public String getPayment(Integer memberId, String orderId, Date createTime, BigDecimal money, String ip) throws Exception {
        // 1:九哥大区 2:仁德 3:火箭 4:四方
        String paymentType = iFaRiskConfigService.getConfigValue("payment.type", "1");
        if ("1".equals(paymentType)) {
            return getPaymentFromNineBrother(orderId, createTime, money, ip);
        } else if ("2".equals(paymentType)) {
            return getPaymentFromRenDe(orderId, createTime, money, ip);
        } else if ("3".equals(paymentType)) {
            return getPaymentFromHuoJian(orderId, createTime, money, ip);
        } else if ("4".equals(paymentType)) {
            return getPaymentFromSiFang(memberId, orderId, createTime, money, ip);
        } else {
            throw new ServiceException("payment type error", HttpStatus.ERROR);
        }
    }

    /**
     * 四方支付
     * @param memberId
     * @param orderId
     * @param createTime
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromSiFang(Integer memberId, String orderId, Date createTime, BigDecimal money, String ip) throws Exception {
        String pay_memberid = iFaRiskConfigService.getConfigValue("sifang.pay_memberid", null);
        String Md5key = iFaRiskConfigService.getConfigValue("sifang.Md5key", null);
        String pay_bankcode = iFaRiskConfigService.getConfigValue("sifang.pay_bankcode", null);

        if (StringUtils.isEmpty(pay_memberid) || StringUtils.isEmpty(Md5key) || StringUtils.isEmpty(pay_bankcode)) {
            throw new ServiceException("sifang payment config error", HttpStatus.ERROR);
        }

        String result = SiFangPaymentUtil.getPaymentUrl(memberId, pay_memberid, Md5key, pay_bankcode, orderId, createTime, money, ip);
        return result;
    }

    /**
     * 九哥大区
     * @param orderId
     * @param createTime
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromNineBrother(String orderId, Date createTime, BigDecimal money, String ip) throws Exception {
        String gateway = iFaRiskConfigService.getConfigValue("9brother.gateway", null);
        String merchNo = iFaRiskConfigService.getConfigValue("9brother.merchNo", null);
        String appId = iFaRiskConfigService.getConfigValue("9brother.appId", null);
        String apiKey = iFaRiskConfigService.getConfigValue("9brother.apiKey", null);

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(merchNo) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(apiKey)) {
            throw new ServiceException("9brother payment config error", HttpStatus.ERROR);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String result = NineBrotherPaymentUtil.getPaymentUrl(gateway, merchNo, appId, apiKey, orderId, sdf.format(createTime), money.setScale(0, RoundingMode.HALF_UP).toString(), ip);
        return result;
    }

    /**
     * 仁德支付
     * @param orderId
     * @param createTime
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromRenDe(String orderId, Date createTime, BigDecimal money, String ip) throws Exception {
        String gateway = iFaRiskConfigService.getConfigValue("rende.gateway", null);
        String apiKey = iFaRiskConfigService.getConfigValue("rende.apiKey", null);
        String apiSecret = iFaRiskConfigService.getConfigValue("rende.apiSecret", null);
        String gateId = iFaRiskConfigService.getConfigValue("rende.gateId", null);
        String action = iFaRiskConfigService.getConfigValue("rende.action", null);

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(apiKey) || StringUtils.isEmpty(apiSecret) ||
                StringUtils.isEmpty(gateId) || StringUtils.isEmpty(action)) {
            throw new ServiceException("rende payment config error", HttpStatus.ERROR);
        }

        String result = RendePaymentUtil.getPaymentUrl(gateway, apiKey, apiSecret, gateId, action, orderId, createTime, money, ip);
        return result;
    }

    /**
     * 火箭支付
     * @param orderId
     * @param createTime
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromHuoJian(String orderId, Date createTime, BigDecimal money, String ip) throws Exception {
        String gateway = iFaRiskConfigService.getConfigValue("huojian.gateway", null);
        String appId = iFaRiskConfigService.getConfigValue("huojian.appId", null);
        String apiKey = iFaRiskConfigService.getConfigValue("huojian.apiKey", null);

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(apiKey)) {
            throw new ServiceException("huojian payment config error", HttpStatus.ERROR);
        }

        String result = HuoJianPaymentUtil.getPaymentUrl(gateway, appId, apiKey, orderId, createTime, money, ip);
        return result;
    }

}