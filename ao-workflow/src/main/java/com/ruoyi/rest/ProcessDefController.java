package com.ruoyi.rest;

import cn.hutool.core.util.IdUtil;
import com.ruoyi.config.properties.SysProperties;
import com.ruoyi.entity.vo.ProcessDefinitionVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import com.ruoyi.mapstruct.ProcessDefinitionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;


/**
 *  流程定义controller
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/activiti7/processDef")
public class ProcessDefController {


    private final RepositoryService repositoryService;

    private final ProcessDefinitionMapper processDefinitionMapper;

    private final SysProperties sysProperties;

    /**
     *  上传BPMN流程定义文件
     * @return  ResponseEntity
     */
    @PostMapping("/uploadBPMN")
    public ResponseEntity<Object> uploadBPMN(@RequestParam("processFile")MultipartFile multipartFile,
                                             @RequestParam("processName") String processName) throws IOException {

        String fileName = multipartFile.getOriginalFilename();

        String extension = FilenameUtils.getExtension(fileName);

        InputStream in = multipartFile.getInputStream();

        Deployment deployment = null;

        if("zip".equals(extension)) {
            ZipInputStream zip = new ZipInputStream(in);
            deployment = repositoryService.createDeployment()
                    .addZipInputStream(zip)
                    .name(processName)
                    .deploy();
        } else {
            deployment = repositoryService.createDeployment()
                    .addInputStream(fileName, in)
                    .name(processName)
                    .deploy();
        }

        return ResponseEntity.ok(deployment);
    }

    /**
     *  通过xml部署
     * @param BPMNResourceXML 流程定义文件xml
     * @return  ResponseEntity<Object>
     */
    @PostMapping("/deploymentResource")
    public ResponseEntity<Object> deploymentResource(@RequestParam("BPMNResourceXML") String BPMNResourceXML) {

        Deployment deployment = repositoryService.createDeployment()
                .addString("createWithBPMNJS.bpmn", BPMNResourceXML)
                // .name(deploymentName)
                .deploy();

        return ResponseEntity.ok(deployment);
    }

    /**
     *  获取流程定义列表
     * @return ResponseEntity<Object>
     */
    @GetMapping("/getDefinitions")
    public ResponseEntity<Object> definitions(Integer current, Integer pageSize) {

        long total = repositoryService.createProcessDefinitionQuery()
                .count();

        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .listPage(current, pageSize);

        List<ProcessDefinitionVo> processDefinitionVos = processDefinitionMapper.toVo(processDefinitions);
        Map<String, Object> result = new HashMap<>(){
            {
                put("data", processDefinitionVos);
                put("total", total);
            }
        };

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     *  获取流程定义xml
     * @param deploymentId  部署ID
     * @param resourceName 部署resourceName
     * @return  ResponseEntity<Object>
     */
    @GetMapping("/BPMNXMLResource")
    public void BPMNXMLResource(HttpServletResponse response, @RequestParam("deploymentId") String deploymentId,
                                @RequestParam("resourceName") String resourceName) throws IOException {
        InputStream in = repositoryService.getResourceAsStream(deploymentId, resourceName);
        if(in != null) {
            int available = in.available();

            byte[] bytes = new byte[available];

            response.setContentType("text/xml");
            ServletOutputStream out = response.getOutputStream();
            while (in.read(bytes) != -1) {
                out.write(bytes);
            }
            in.close();
        }
    }

    /**
     *  upload bpmn file
     * @param processFile bpm file
     * @return ResponseEntity
     */
    @PostMapping("/uploadProcessBpmnFile")
    public ResponseEntity<Object> uploadProcessBpmnFile(MultipartFile processFile) throws FileNotFoundException {
        if(processFile.isEmpty()) {
            log.error("上传文件为空");
        }
        String fileName = processFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(fileName);

        fileName = IdUtil.fastSimpleUUID() + "." + extension;
        String bpmnDir = sysProperties.getBpmnLocation();

        if(StringUtils.isEmpty(bpmnDir)) {
            log.error("save bpmn file parent path is empty, Please sure config [bpmnLocation] option.");
            throw new FileNotFoundException();
        }

        boolean addseparator = bpmnDir.endsWith("/") || bpmnDir.endsWith("//")
                || bpmnDir.endsWith("\\") || bpmnDir.endsWith(File.separator);
        if (!addseparator) {
            bpmnDir = bpmnDir + File.separator;
        }

        File file = new File(bpmnDir + fileName);
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            processFile.transferTo(file);
        } catch (IOException e) {
            log.error("save bpmn file failure {}", e);
        }
        return ResponseEntity.ok(fileName);
    }


    //删除流程定义
    @GetMapping(value = "/delDefinition")
    public ResponseEntity delDefinition(@RequestParam("depID") String depID) {
        try {
            repositoryService.deleteDeployment(depID, true);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
