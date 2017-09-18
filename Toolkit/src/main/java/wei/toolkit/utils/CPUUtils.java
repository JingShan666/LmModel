package wei.toolkit.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class CPUUtils {


    private static class CpuFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                return true;
            }
            return false;
        }
    }

    public static int getNumberCores() {
        try {
            File file = new File("/sys/devices/system/cpu/");
            File[] files = file.listFiles(new CpuFilter());
            Loger.log(CPUUtils.class.getSimpleName(), "cpu count = " + files.length);
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            Loger.log(CPUUtils.class.getSimpleName(), "获取Cpu个数失败");
            return 1;
        }

    }
}
