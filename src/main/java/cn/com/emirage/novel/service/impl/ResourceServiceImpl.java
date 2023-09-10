package cn.com.emirage.novel.service.impl;

import cn.com.emirage.novel.core.common.resp.RestResp;
import cn.com.emirage.novel.dto.resp.ImgVerifyCodeRespDto;
import cn.com.emirage.novel.manager.redis.VerifyCodeManager;
import cn.com.emirage.novel.service.ResourceService;
import com.aliyun.oss.OSSClient;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 资源（图片/视频/文档）相关服务实现类
 *
 * @author xiongxiaoyang
 * @date 2022/5/17
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    private final VerifyCodeManager verifyCodeManager;

    @Value("${novel.file.upload.path}")
    private String fileUploadPath;

    @Override
    public RestResp<ImgVerifyCodeRespDto> getImgVerifyCode() throws IOException {
        String sessionId = IdWorker.get32UUID();
        return RestResp.ok(ImgVerifyCodeRespDto.builder()
            .sessionId(sessionId)
            .img(verifyCodeManager.genImgVerifyCode(sessionId))
            .build());
    }
    @Autowired
    private OSSClient ossClient;
    @SneakyThrows
    @Override
    public RestResp<String> uploadImage(MultipartFile file) {
        LocalDateTime now = LocalDateTime.now();
        String savePath ="image/"                + now.format(DateTimeFormatter.ofPattern("yyyy"))+"/"
                + now.format(DateTimeFormatter.ofPattern("MM")) +"/"
                + now.format(DateTimeFormatter.ofPattern("dd"))+"/";
        String oriName = file.getOriginalFilename();
        assert oriName != null;

        String saveFileName = IdWorker.get32UUID() + oriName.substring(oriName.lastIndexOf("."));
        String objectName = savePath+saveFileName;
//        File saveFile = new File(fileUploadPath + savePath, saveFileName);
//        if (!saveFile.getParentFile().exists()) {
//            boolean isSuccess = saveFile.getParentFile().mkdirs();
//            if (!isSuccess) {
//                throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
//            }
//        }
//        file.transferTo(saveFile);
//        if (Objects.isNull(ImageIO.read(saveFile))) {
//            // 上传的文件不是图片
//            Files.delete(saveFile.toPath());
//            throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_TYPE_NOT_MATCH);
//        }
        ossClient.putObject(bucketName, objectName, file.getInputStream());
        return RestResp.ok(formatPath(objectName) );
    }
    private String formatPath(String objectName){
        return "https://"  +bucketName+"."+ ossClient.getEndpoint().getHost() + "/" + objectName;
    }
}
