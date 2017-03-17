package com.fable.outer.rmi.event.help;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

import com.sun.management.OperatingSystemMXBean;

public class WatchSysInfo {

	private static final int CPUTIME = 500;
	private static final int PERCENT = 100;
	private static final int FAULTLENGTH = 10;

	public static Double formatData(Double data) {
		DecimalFormat df = new DecimalFormat("##.00");
		return Double.parseDouble(df.format(data));
	}
	
	public static Double getMemerypre() {
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		// 总的物理内存+虚拟内存
		long totalvirtualMemory = osmxb.getTotalSwapSpaceSize();
		// 剩余的物理内存
		long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
		Double compare = (Double) (1 - freePhysicalMemorySize * 1.0 / totalvirtualMemory) * 100;
		return formatData(compare);
	}

	// 获取cpu使用率
	public static Double getCpuRatioForWindows() {
		try {
			String procCmd = System.getenv("windir")
					+ "//system32//wbem//wmic.exe process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
			// 取进程信息
			long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
			Thread.sleep(CPUTIME);
			long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
			if (c0 != null && c1 != null) {
				long idletime = c1[0] - c0[0];
				long busytime = c1[1] - c0[1];
				return formatData(Double.valueOf(PERCENT * (busytime) * 1.0 / (busytime + idletime)));
			} else {
				return (double) 0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return (double) 0;
		}
	}

	private static long[] readCpu(final Process proc) {
		long[] retn = new long[2];
		try {
			proc.getOutputStream().close();
			InputStreamReader ir = new InputStreamReader(proc.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line = input.readLine();
			if (line == null || line.length() < FAULTLENGTH) {
				return null;
			}
			int capidx = line.indexOf("Caption");
			int cmdidx = line.indexOf("CommandLine");
			int rocidx = line.indexOf("ReadOperationCount");
			int umtidx = line.indexOf("UserModeTime");
			int kmtidx = line.indexOf("KernelModeTime");
			int wocidx = line.indexOf("WriteOperationCount");
			long idletime = 0;
			long kneltime = 0;
			long usertime = 0;
			while ((line = input.readLine()) != null) {
				if (line.length() < wocidx) {
					continue;
				}
				// 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
				// ThreadCount,UserModeTime,WriteOperation
				String caption = substring(line, capidx, cmdidx - 1).trim();
				String cmd = substring(line, cmdidx, kmtidx - 1).trim();
				if (cmd.indexOf("wmic.exe") >= 0) {
					continue;
				}
				String s1 = substring(line, kmtidx, rocidx - 1).trim();
				String s2 = substring(line, umtidx, wocidx - 1).trim();
				if (caption.equals("System Idle Process") || caption.equals("System")) {
					if (s1.length() > 0)
						idletime += Long.valueOf(s1).longValue();
					if (s2.length() > 0)
						idletime += Long.valueOf(s2).longValue();
					continue;
				}
				if (s1.length() > 0)
					kneltime += Long.valueOf(s1).longValue();
				if (s2.length() > 0)
					usertime += Long.valueOf(s2).longValue();
			}
			retn[0] = idletime;
			retn[1] = kneltime + usertime;
			return retn;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				proc.getInputStream().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 由于String.subString对汉字处理存在问题（把一个汉字视为一个字节)，因此在 包含汉字的字符串时存在隐患，现调整如下：
	 * 
	 * @param src
	 *            要截取的字符串
	 * @param start_idx
	 *            开始坐标（包括该坐标)
	 * @param end_idx
	 *            截止坐标（包括该坐标）
	 * @return
	 */
	private static String substring(String src, int start_idx, int end_idx) {
		byte[] b = src.getBytes();
		String tgt = "";
		for (int i = start_idx; i <= end_idx; i++) {
			tgt += (char) b[i];
		}
		return tgt;
	}

	// 获取硬盘使用率
	public static Double getDisk() {
		// 操作系统
		long total = 0;
		long free = 0;
		for (char c = 'A'; c <= 'Z'; c++) {
			String dirName = c + ":/";
			File win = new File(dirName);
			if (win.exists()) {
				total += (long) win.getTotalSpace();
				free += (long) win.getFreeSpace();
			}
		}
		Double compare = (Double) (1 - free * 1.0 / total) * 100;
		return formatData(compare);
	}

	public static double getLinuxCpuUsage() throws Exception {
		double cpuUsed = 0;
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec("top -b -n 1");// 调用系统的“top"命令
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String str = null;
			String[] strArray = null;
			while ((str = in.readLine()) != null) {
				int m = 0;
				if (str.indexOf(" R ") != -1) {// 只分析正在运行的进程，top进程本身除外 &&
					strArray = str.split(" ");
					for (String tmp : strArray) {
						if (tmp.trim().length() == 0)
							continue;
						++m;
						if (m == 9) {// 第9列为CPU的使用百分比(RedHat
							cpuUsed += Double.parseDouble(tmp);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return formatData(cpuUsed);
	}

	public static double getLinuxMemUsage() throws Exception {
		double memUsed = 0;
		double memTotal = 0;
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec("free");// 调用系统的free命令
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String str = null;
			String[] strArray = null;
			while ((str = in.readLine()) != null) {
				int m = 0;
				if (str.indexOf("Mem") != -1) {
					strArray = str.split(" ");
					for (String tmp : strArray) {
						if (tmp.trim().length() == 0) {
							continue;
						} else {
							++m;
							if (m == 2) {
								memTotal = Double.parseDouble(tmp);
							}
							if (m == 3) {
								memUsed = Double.parseDouble(tmp);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return formatData(memUsed / memTotal * 100);
	}

	public static double getLinuxDiskUsage() throws Exception {
		double freeHD = 0;
		double usedHD = 0;
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec("df");// df 查看硬盘空间
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String str = null;
			String[] strArray = null;
			while ((str = in.readLine()) != null) {
				int m = 0;
				if (str.indexOf("/") != -1 || str.indexOf("tmp") != -1) {
					strArray = str.split(" ");
					for (String tmp : strArray) {
						if (tmp.trim().length() == 0) {
							continue;
						} else {
							++m;
							if (m == 2) {
								usedHD += Double.parseDouble(tmp);
							}
							if (m == 3) {
								freeHD += Double.parseDouble(tmp);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return formatData(usedHD / (usedHD + freeHD) * 100);
	}
}
