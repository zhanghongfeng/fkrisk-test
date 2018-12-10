package com.dashu.fk.test.tools.file;

import com.dashu.fk.test.tools.Sleep;
import com.dashu.fk.test.tools.system.SystemInfo;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

/**
 * Created by zhf2015 on 17/5/9.
 */
public class FileHandle {
    private static final Logger logger = LoggerFactory.getLogger(FileHandle.class);

    public static boolean writeStringToFile(String context, String filepath, String file) {
        try {
            File f = new File(filepath);
            if (!f.exists()) {
                f.mkdirs();
            }
            if (filepath.endsWith("/")) {
                f = new File(filepath + file);
            } else {
                f = new File(filepath + "/" + file);
            }
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileWriter writer = new FileWriter(f, false);

            writer.write(context);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean writeStringToNewFile(String context, String filepath, String file) {
        try {
            String osName = SystemInfo.getOSInfo();
            if (osName.contains("OS")) {
                filepath = filepath;
            } else if (osName.contains("win")) {

            } else if (osName.contains("Linux")) {
                filepath = "/home" + filepath;
            }

            File f = new File(filepath);
            if (!f.exists()) {
                boolean isok = f.mkdirs();
                logger.info("创建文件夹:[{}] - {}", filepath, isok ? "成功" : "失败");
            }

            if (filepath.endsWith("/")) {
                f = new File(filepath + file);
            } else {
                f = new File(filepath + "/" + file);
            }

            logger.info("文件[{}] 的绝对路径是:[{}]", f.getName(), f.getAbsolutePath());
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    f.delete();
                    Sleep.sleep(50);
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileWriter writer = new FileWriter(f, false);

            writer.write(context);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static Map<String, String> readTxt(String fileStr) {
        Map<String, String> map = Maps.newHashMap();

        /* 读取数据 */
        try {
            String rootpath = FileHandle.class.getClassLoader().getResource("").getPath();
            String finalPath = rootpath + fileStr;
            File file = new File(finalPath);
            logger.info("文件:{},是否存在:{}", finalPath, file.exists());

            InputStream inputStream = FileHandle.class.getClass().getResourceAsStream("/" + fileStr);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                String[] names = lineTxt.split(",");
//                for (int i = 0 ; i < names.length ; i++) {
//                    map.put(names[0], names[]);
//                }
                map.put(names[0], names[1]);
            }

            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
        logger.info("测试用户数：[{}]", map.size());
        return map;
    }

//    public boolean writeTxt(String fileStr) {
//        Map<String, String> map = Maps.newHashMap();
//
//       /* 输出数据 */
//        try {
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:/结果.txt")),
//                    "UTF-8"));
//
//            for (String name : map.keySet()) {
//                bw.write(name + " " + map.get(name));
//                bw.newLine();
//            }
//            bw.close();
//        } catch (Exception e) {
//            System.err.println("write errors :" + e);
//        }
//    }
}
