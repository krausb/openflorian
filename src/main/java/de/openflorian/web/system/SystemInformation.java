package de.openflorian.web.system;

/*
 * This file is part of Openflorian.
 * 
 * Copyright (C) 2015  Bastian Kraus
 * 
 * Openflorian is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version)
 *     
 * Openflorian is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with Openflorian.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.management.OperatingSystemMXBean;

/**
 * {@link SystemInformation provides information about the underlying operating system
 * and memory usage of the current JVM.
 *
 * @author Bastian Kraus <bk@pogo-systems.de>
 */
@SuppressWarnings("restriction")
public class SystemInformation {

	private static final Logger Log = LoggerFactory.getLogger(SystemInformation.class);
	private final Runtime runtime = Runtime.getRuntime();
	private static String infoSpacer = " / ";
	private static String newLine = "\n";
	private final OperatingSystemMXBean osBean = ManagementFactory
			.getPlatformMXBean(OperatingSystemMXBean.class);
	
	private MemoryPoolMXBean metaspaceBean = null;
	
    public SystemInformation() {
        List<MemoryPoolMXBean> beans = 
                ManagementFactory.getMemoryPoolMXBeans();
        for(MemoryPoolMXBean bean : beans) {
            if(bean.getName().toLowerCase().indexOf("metaspace") >= 0) {
                metaspaceBean = bean;
                break;
            }
        }
        if(metaspaceBean == null) {
        	// java <= 7 fallback
	        for(MemoryPoolMXBean bean : beans) {
	            if(bean.getName().toLowerCase().indexOf("perm gen") >= 0) {
	                metaspaceBean = bean;
	                break;
	            }
	        }
    	}
    }
 

	/**
	 * Formats given byte with 1024 exchange ratio. up to exabinary byte (EiB)
	 *
	 * @param size
	 *            given value, that should be formatted.
	 * @return formatted byte. String like "638.2MiB". will consider
	 *         regionspecific formatting ("."->"," for german). Negativ values
	 *         will return "0"
	 */
	public static String formatBinaryByte(long size) {
		if (size <= 0) {
			return "0";
		}
		final String[] units = new String[] { "B", "KiB", "MiB", "GiB", "TiB",
				"PiB", "EiB" }; // http://physics.nist.gov/cuu/Units/binary.html
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size
				/ Math.pow(1024, digitGroups))
				+ units[digitGroups];
	}

	/**
	 * Formats given byte with 1000 exchange ratio. up to exa byte (EiB)
	 *
	 * @param size
	 *            given value, that should be formatted.
	 * @return formatted byte. String like "638.2MiB". will consider
	 *         regionspecific formatting ("."->"," for german). Negativ values
	 *         will return "0"
	 */
	public static String formatDecimalByte(long size) {
		if (size <= 0) {
			return "0";
		}
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB",
				"PB", "EB" }; // http://physics.nist.gov/cuu/Units/binary.html
		int digitGroups = (int) (Math.log10(size) / Math.log10(1000));
		return new DecimalFormat("#,##0.#").format(size
				/ Math.pow(1000, digitGroups))
				+ units[digitGroups];
	}

	/**
	 * formats given nanoSeconds up to Seconds.
	 *
	 * @param nanos
	 * @return String like: "100millis"
	 */
	public static String formatNanos(long nanos) {
		if (nanos <= 0) {
			return "0";
		}
		final String[] units = new String[] { "nanos", "micros", "millis", "s" };
		int digitGroups = (int) (Math.log10(nanos) / Math.log10(1000));
		if (digitGroups > 3) {
			long millis = nanos / 1000000;
			return String.format(
					"%dd %02dh %02dm %02ds",
					TimeUnit.MILLISECONDS.toDays(millis),
					TimeUnit.MILLISECONDS.toHours(millis)
							- TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS
									.toDays(millis)),
					TimeUnit.MILLISECONDS.toMinutes(millis)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
									.toHours(millis)),
					TimeUnit.MILLISECONDS.toSeconds(millis)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes(millis)));
		}
		return new DecimalFormat("#,##0.#").format(nanos
				/ Math.pow(1000, digitGroups))
				+ units[digitGroups];
	}

	/**
	 * Formats given <code>floated</code> to a percentage number string.
	 *
	 * @param floated
	 * @return
	 */
	public static String formatPercentage(double floated) {
		return Math.round(floated * 100.0) + "%";
	}

	/**
	 * Gets the count of available cpus
	 * 
	 * @return String 
	 * 			cpu count 
	 */
	public String getCPUCount() {
		return getCPUCountRaw() + "";
	}

	/**
	 * Gets the count of available cpus
	 * 
	 * @return long 
	 * 			cpu count 
	 */
	public long getCPUCountRaw() {
		return runtime.availableProcessors();
	}

	public String getProcessCPUTime() {
		long ret = getProcessCPUTimeRaw();
		// long ret =-1;
		if (ret < 0) {
			return "not supported";
		}
		return formatNanos(ret);
	}

	public long getProcessCPUTimeRaw() {
		return osBean.getProcessCpuTime();
	}

	public String getSystemCPULoad() {
		double ret = getSystemCPULoadRaw();
		// double ret = -1;
		if (ret < 0) {
			return "not supported";
		}
		return formatPercentage(ret);
	}

	public double getSystemCPULoadRaw() {
		return osBean.getSystemLoadAverage();
	}

	public String getProcessCPULoad() { 
		double ret = getProcessCPULoadRaw();
		// double ret = -1;
		if (ret < 0) {
			return "not supported";
		}
		return formatPercentage(ret);
	}

	public double getProcessCPULoadRaw() {
		return osBean.getProcessCpuLoad();
	}

	public String getSystemCPUFree() { 
		double ret = getSystemCPUFreeRaw();
		// double ret = -1;
		if (ret > 1) {
			return "not supported";
		}
		return formatPercentage(getSystemCPUFreeRaw());
	}

	public double getSystemCPUFreeRaw() {
		return 1.0 - osBean.getSystemLoadAverage();
	}

	public String getMemoryFree() {
		return formatBinaryByte(getMemoryFreeRaw());
	}

	public long getMemoryFreeRaw() {
		return runtime.freeMemory();
	}

	public String getMemoryUsed() {
		return formatBinaryByte(getMemoryUsedRaw());
	}

	public long getMemoryUsedRaw() {
		return runtime.totalMemory() - runtime.freeMemory();
	}

	public String getMemoryMax() {
		long maxMemory = getMemoryMaxRaw();
		return maxMemory == Long.MAX_VALUE ? "no limit"
				: formatBinaryByte(maxMemory);
	}

	public long getMemoryMaxRaw() {
		return runtime.maxMemory();
	}

	public String getHDDInfo(File... hdds) {
		StringBuilder sb = new StringBuilder("Count: ").append(hdds.length)
				.append(newLine).append("\t");
		for (File file : hdds) {
			sb.append(getHDDInfo(file).replace(newLine, newLine + "\t"))
					.append(newLine);
		}
		return sb.toString();
	}

	public String getHDDInfo(File hdd) {
		StringBuilder sb = new StringBuilder("absolutePath: ").append(
				hdd.getAbsolutePath()).append(newLine);
		sb.append("Free: ").append(getHDDFree(hdd)).append(" (total/usable)")
				.append(newLine);
		sb.append("Used: ").append(getHDDUsed(hdd)).append(newLine);
		sb.append("ize: ").append(getHDDTotal(hdd)).append(newLine);
		return sb.toString();
	}

	public String getHDDFree(File hdd) {
		return formatBinaryByte(hdd.getFreeSpace()) + infoSpacer
				+ formatBinaryByte(hdd.getUsableSpace());
	}

	public String getHDDUsed(File hdd) {
		return formatBinaryByte(hdd.getTotalSpace() - hdd.getFreeSpace());
	}

	public String getHDDTotal(File hdd) {
		long maxMemory = hdd.getTotalSpace();
		return maxMemory == Long.MAX_VALUE ? "no limit"
				: formatBinaryByte(maxMemory);
	}
	
	/**
	 * Calculate the permgen space usage
	 * 
	 * @return
	 * 		int
	 */
	public double getPermgenUsageRaw() {
		return ((getCurrentPermgenUsageRaw() * 100) 
	            / getMaxPermgenSpaceRaw());
	}
	
	/**
	 * Get formatted calculated permgen space usage
	 * 
	 * @return
	 */
	public String getPermgenUsage() {
		return formatPercentage(getPermgenUsageRaw());
	}
	
	/**
	 * Returns the current permgen memory usage in byte as {@link long}
	 * @return
	 */
	public long getCurrentPermgenUsageRaw() {
		MemoryUsage currentUsage = metaspaceBean.getUsage();
		return currentUsage.getUsed();
	}
	
	/**
	 * Returns the current formatted permgen memory usage
	 * 
	 * @return
	 */
	public String getCurrentPermgenUsage() {
		return formatBinaryByte(getCurrentPermgenUsageRaw());
	}
	
	/**
	 * Returns the max available permgen memory size in byte as {@link long}
	 * 
	 * @return
	 */	
	public long getMaxPermgenSpaceRaw() {
		MemoryUsage currentUsage = metaspaceBean.getUsage();
		return currentUsage.getMax();
	}
	
	/**
	 * Returns the formatted max available permgen memory size for this VM.
	 * 
	 * @return
	 */
	public String getMaxPermgenSpace() {
		return formatBinaryByte(getMaxPermgenSpaceRaw());
	}

	public String getSystemInformation() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName())
				.append(newLine);
		sb.append("CPU Core count: ").append(getCPUCount()).append(newLine);
		sb.append("CPU Load(Processload/Systemload): ")
				.append(getProcessCPULoad()).append(infoSpacer)
				.append(getSystemCPULoad()).append(newLine);	
		sb.append("CPU Time: ").append(getProcessCPUTime()).append(newLine);
		sb.append("MEM Usage(used/free/max): ").append(getMemoryUsed())
				.append(infoSpacer).append(getMemoryFree()).append(infoSpacer)
				.append(getMemoryMax()).append(newLine);
		sb.append("HDDs: ").append(getHDDInfo(File.listRoots()));
		if (sb.toString().endsWith("\t" + newLine)) {
			return sb.toString().replace("\t" + newLine, "");
		} else if (sb.toString().endsWith(newLine)) {
			return sb.toString().replace(newLine, "");
		} else {
			return sb.toString();
		}
	}
	
	public HashMap<String, String> getSystemInformationList() {
		HashMap<String, String> info = new HashMap<String, String>();
		
		info.put("CPU Cores", getCPUCount());
		info.put("CPU Load(Processload/Systemload)", getProcessCPULoad() + "/" + getSystemCPUFree());
		info.put("CPU Time", getProcessCPUTime());
		info.put("MEM used", getMemoryUsed());
		info.put("MEM free", getMemoryFree());
		info.put("MEM max", getMemoryMax());
		info.put("PermGen usage", getPermgenUsage());
		info.put("PermGen used", getCurrentPermgenUsage());
		info.put("PermGen max", getMaxPermgenSpace());
		
		return info;
	}

	/**
	 * Logs all information gathered from {@link SystemInformation#getSystemInformationList()} to logging system
	 */
	public void logAllInformation() {
		for (Entry<String, String> e : getSystemInformationList().entrySet()) {
			Log.debug(e.getKey() + ": " + e.getValue());
		}
	}

	/**
	 * System.out.println()s all information gathered from {@link SystemInformation#getSystemInformationList()} to logging system
	 */
	public void sysoutAllInformation() {
		for (Entry<String, String> e : getSystemInformationList().entrySet()) {
			System.out.println(e.getKey() + ": " + e.getValue());
		}
	}
	
	public static void setInfoSpacer(String infoSpacer) {
		SystemInformation.infoSpacer = infoSpacer;
	}

	public static void setNewLine(String newLine) {
		SystemInformation.newLine = newLine;
	}

}
