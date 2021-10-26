package com.effective.common.shell;


import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.annotation.processing.FilerException;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@Data
@Builder
public class ShellUtils {

    public static void runShell(String scriptPath, String... param) {
        try {
            if ("".equals(scriptPath) && scriptPath.length() <= 0) {
                System.out.println("参数为空");
                return;
            }
            //String[] cmd = new String[]{scriptPath};
            List<String> cmdList = new ArrayList<String>();
            cmdList.add(scriptPath);
            cmdList.addAll(Arrays.asList(param));
            //解决脚本没有执行权限
            ProcessBuilder builder = new ProcessBuilder("/bin/chmod", "755", scriptPath);
            Process process = builder.start();
            process.waitFor();
            String[] cmd = cmdList.toArray(new String[cmdList.size()]);
            Process ps = Runtime.getRuntime().exec(cmd);
            ps.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            //执行结果
            String result = sb.toString();
            System.out.println(result);
        } catch (Exception e) {
            System.out.print("error");
            e.printStackTrace();
        }
    }

    /**
     * @param sourcePath 源文件
     * @param fileName   文件名称
     * @param targetPath 目标路径
     * @param fileSuffix 文件后缀
     * @param splitSize  单位 kb，mb，gb
     *                   return json
     */
    public Result<String> splitFile(String sourcePath, String fileName, String targetPath, String fileSuffix, int splitSize) {
        Result result = Result.builder().resultCode(ResultCode.SUCCESS).build();
        splitSize = Optional.ofNullable(splitSize).orElse(100);
        if (StringUtils.isBlank(sourcePath)) {
            result.setResultCode(ResultCode.SOURCE_FAIL_PATH_NOT_EMPTY);
            return result;
        }
        if (StringUtils.isBlank(fileName)) {
            result.setResultCode(ResultCode.SOURCE_FAIL_NAME_NOT_EMPTY);
            return result;
        }
        if (StringUtils.isBlank(targetPath)) {
            result.setResultCode(ResultCode.TARGET_FAIL_PATH_NOT_EMPTY);
            return result;
        }
        if (StringUtils.isBlank(fileSuffix)) {
            result.setResultCode(ResultCode.FAIL_SUFFIX_NOT_EMPTY);
            return result;
        }

        try {
            File file = new File(sourcePath + File.separator + fileName);
            if (!file.exists()) {
                result.setResultCode(ResultCode.SOURCE_FAIL_PATH_NOT_EXIST);
                return result;
            }
            int fileSize = getFileSize(file.getAbsolutePath());
            int splitNum = fileSize > splitSize ? (fileSize % splitSize == 0 ? fileSize / splitSize : fileSize / splitSize + 1) : 1;
            log.info("需要才分{}文件",splitNum);
            if (splitNum > 1) {
                ProcessBuilder builder = new ProcessBuilder("/bin/chmod", "755", sourcePath);
                Process process = builder.start();
                process.waitFor();
                String split = "split -b " + splitSize + "m " + sourcePath + File.separator + fileName + " " + targetPath +  File.separator + fileSuffix;
                callShell(split);
            }
        } catch (Exception e) {
            System.out.print("error");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 通过脚本获取文件大小
     *
     * @param filePath
     * @return 返回m
     * @throws FilerException
     */
    public int getFileSize(String filePath) throws FilerException {
        String[] result = callShell("du -m " + filePath)
                .replace(" ", "")
                .split("\\t");
        if (result.length < 2) {
            throw new FilerException("请检查文件是否存在 " + filePath + "，执行命令错误");
        } else {
            return Integer.parseInt(result[0]);
        }
    }

    public static String callShell(String shellString) {
        String result = new String();
        try {
            String[] cmd = new String[]{"sh", "-c", shellString};
            Process process = Runtime.getRuntime().exec(cmd);
            int exitValue = process.waitFor();
            if (0 != exitValue) {
                log.error("call shell failed. error code is :" + exitValue);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                    log.info(line);
                }
                result = sb.toString();
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return result;
    }


    public static void main(String[] args) {
        ShellUtils shellUtils = new ShellUtils();
        shellUtils.splitFile("/tmp/shell", "ff.txt", "/tmp/shell", "ff_", 100);
    }

    public class FileBean {
        private String sourcePath;
        private String fileName;
        private String fileSuffix;
        private int splitSize;
        private String targetPath;
        private Map<String, String> splitFileList;
    }


}


@Data
@Builder
class Result<T> {
    private ResultCode resultCode;
    private T t;
}


enum ResultCode {
    SUCCESS("0", "执行成功"),
    FAIL("1", "执行失败"),
    SOURCE_FAIL_PATH_NOT_EMPTY("10", "源文件路径不能为空"),
    SOURCE_FAIL_NAME_NOT_EMPTY("11", "文件名称不能为空"),
    TARGET_FAIL_PATH_NOT_EMPTY("12", "目标路径不能为空"),
    FAIL_SUFFIX_NOT_EMPTY("13", "文件后缀不能为空"),
    SOURCE_FAIL_PATH_NOT_EXIST("14", "文件不存在"),
    ;
    private String code;
    private String msg;

    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
