package ru.codeinside.gws.p.router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Killer {

  //Proto      Recv-Q Send-Q Local Address   Foreign Address  State       PID/Program name
  //tcp        0      0      0.0.0.0:37789   0.0.0.0:*        LISTEN      24715/java
  //tcp        0      0      127.0.0.1:6942  0.0.0.0:*        LISTEN      24715/java

  public static boolean kill(int byPort) {
    if (kill(byPort, false)) {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        return false;
      }
      kill(byPort, true);
      return true;
    }
    return false;
  }

  public static boolean kill(int byPort, boolean force) {
    String os = System.getProperty("os.name");
    if (!os.toLowerCase().contains("linux")) {
      return false;
    }
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
    if (pid != null) {
      process = null;
      try {
        String command = "kill " + (force ? "-KILL " : "-TERM ") + pid;
        System.out.println(command);
        process = Runtime.getRuntime().exec(command);
        int result = process.waitFor();
        System.out.println("result:" + result);
        return true;
      } catch (Exception err) {
        err.printStackTrace();
      } finally {
        if (process != null) {
          process.destroy();
        }
      }
    }
    return false;
  }
}
