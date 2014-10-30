package ru.codeinside.gws.p.router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Killer {


    public static boolean killPortOwner(int port) {
        if (!isLinux()) {
            return false;
        }

        Integer pid = pidOfPortOwner(port);
        if (null == pid) {
            return true; // !found
        }

        if (!killPid(pid, false)) {
            return false; // !signaled
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            return false;
        }

        return null == pidOfPortOwner(port) || killPid(pid, true);
    }

    public static boolean killPid(int pid, boolean force) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("kill" + (force ? " -9 " : " ") + pid);
            return process.waitFor() == 0;
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    /**
     * Linux 'netstat -ntpl' result format:
     * <pre>
     * Proto      Recv-Q Send-Q Local Address   Foreign Address  State       PID/Program name
     * tcp        0      0      0.0.0.0:37789   0.0.0.0:*        LISTEN      24715/java
     * tcp        0      0      127.0.0.1:6942  0.0.0.0:*        LISTEN      24715/java
     * </pre>
     */
    public static Integer pidOfPortOwner(int byPort) {
        Process process = null;
        InputStream inputStream = null;
        Integer pid = null;
        try {
            process = Runtime.getRuntime().exec("netstat -ntpl");
            String line;
            inputStream = process.getInputStream();
            BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = input.readLine()) != null) {
                if (pid != null) {
                    continue;
                }
                List<String> parts = new ArrayList<String>();
                for (String item : line.split(" ", 200)) {
                    if (item != null) {
                        item = item.trim();
                        if (!item.isEmpty()) {
                            parts.add(item);
                        }
                    }
                }
                if (parts.size() != 7 || !"LISTEN".equals(parts.get(5)) || "-".equals(parts.get(6))) {
                    continue;
                }
                String addr = parts.get(3);
                int delim = addr.lastIndexOf(':');
                if (delim >= 0) {
                    int port = Integer.parseInt(addr.substring(delim + 1));
                    if (byPort == port) {
                        String pidAndProg = parts.get(6);
                        delim = pidAndProg.indexOf('/');
                        if (delim > 0) {
                            pid = Integer.parseInt(pidAndProg.substring(0, delim));
                        }
                    }
                }
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
        return pid;
    }
}
