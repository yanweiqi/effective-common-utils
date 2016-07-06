package com.effective.common.even;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import com.effective.common.domain.Cpu;
import com.effective.common.utils.ResourceUtils;

public class LoadCpu implements Runnable {
	
	private static DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	private static ScheduledExecutorService excutor = Executors.newSingleThreadScheduledExecutor();
	
	
	public static void main(String[] args) throws SigarException{
		String sigarPath = "D:\\workspace\\eclipse\\ywq\\effective-common-utils\\app-report\\src\\main\\resources\\hyperic-sigar-1.6.4\\sigar-bin\\lib";
		ResourceUtils.addSystemEnv(ResourceUtils.CLASS_PATH, sigarPath, ResourceUtils.getOSType());
		
		fixedRate();
	}
	
	public static void fixedRate(){
		excutor.scheduleAtFixedRate(new LoadCpu(), //执行线程
									0,  //初始化延迟
									5000, //两次开始的执行的最小时间间隔
									TimeUnit.MILLISECONDS //计时单位
									);
	}
	
    private static List<Cpu> load() throws SigarException {
    	List<Cpu> cpus = new ArrayList<Cpu>();
        Sigar sigar = new Sigar();
        CpuInfo[] infos = sigar.getCpuInfoList();
        CpuPerc[] cpuList = sigar.getCpuPercList();

        for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
            CpuInfo info = infos[i];
            System.out.println("第" + (i + 1) + "块CPU信息");
            Cpu cpu = new Cpu();
            cpu.setIndex(i++);
            System.out.println("CPU的总量MHz:    " + info.getMhz());// CPU的总量MHz
            cpu.setTotalMhz(info.getMhz());
            System.out.println("CPU生产商:    " + info.getVendor());// 获得CPU的卖主，如：Intel
            cpu.setProductFactory(info.getVendor());
            System.out.println("CPU类别:    " + info.getModel());// 获得CPU的类别，如：Celeron
            cpu.setModel(info.getModel());
            System.out.println("CPU缓存数量:    " + info.getCacheSize());// 缓冲存储器数量
            cpu.setCache(info.getCacheSize());
            printCpuPerc(cpuList[i]);
            cpus.add(cpu);
        }
        System.out.println("*******************"+ df.format(new Date(System.currentTimeMillis())) +"**************************");
        return cpus;
    }
	
    private static void printCpuPerc(CpuPerc cpu) {
        System.out.println("CPU用户使用率:    " + CpuPerc.format(cpu.getUser()));// 用户使用率
        System.out.println("CPU系统使用率:    " + CpuPerc.format(cpu.getSys()));// 系统使用率
        System.out.println("CPU当前等待率:    " + CpuPerc.format(cpu.getWait()));// 当前等待率
        System.out.println("CPU当前错误率:    " + CpuPerc.format(cpu.getNice()));//
        System.out.println("CPU当前空闲率:    " + CpuPerc.format(cpu.getIdle()));// 当前空闲率
        System.out.println("CPU总的使用率:    " + CpuPerc.format(cpu.getCombined()));// 总的使用率
    }

	@Override
	public void run() {
		try {
			load();
		} catch (SigarException e) {
			e.printStackTrace();
		}
	}
}
