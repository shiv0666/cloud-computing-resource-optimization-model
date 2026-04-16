package com.cloud.project;

import java.io.*;
import java.util.*;
import java.text.*;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.*;
import org.cloudbus.cloudsim.lists.*;
import org.cloudbus.cloudsim.provisioners.*;

public class App {
    private static List<Cloudlet> cloudletList;
    private static List<Vm> vmList;

    public static void main(String[] args) throws Exception {
        // Initialize CloudSim
        int numUser = 1;
        Calendar calendar = Calendar.getInstance();
        boolean traceEvents = false;
        CloudSim.init(numUser, calendar, traceEvents);

        // Create Datacenter
        Datacenter datacenter0 = createDatacenter("Datacenter_0");

        // Create Broker
        DatacenterBroker broker = new DatacenterBroker("Broker");
        int brokerId = broker.getId();

        // Create VMs
        vmList = new ArrayList<>();
        int[] vmRam = {512, 640, 768, 896, 1024};
        for (int i = 0; i < 5; i++) {
            int vmid = i;
            int mips = 1000;
            long size = 10000;
            int ram = vmRam[i];
            long bw = 1000;
            int pesNumber = 1;
            String vmm = "Xen";
            Vm vm = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm,
                    new CloudletSchedulerTimeShared());
            vmList.add(vm);
        }
        broker.submitVmList(vmList);

        // Create Cloudlets
        cloudletList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int cloudletId = i;
            long length = 40000L + i * 1000;
            long fileSize = 300;
            long outputSize = 300;
            UtilizationModel utilizationModel = new UtilizationModelFull();
            Cloudlet cloudlet = new Cloudlet(cloudletId, length, 1, fileSize, outputSize,
                    utilizationModel, utilizationModel, utilizationModel);
            cloudlet.setUserId(brokerId);
            cloudlet.setVmId(i % vmList.size());
            cloudletList.add(cloudlet);
        }
        broker.submitCloudletList(cloudletList);

        // Run simulation
        CloudSim.startSimulation();
        List<Cloudlet> newList = broker.getCloudletReceivedList();
        CloudSim.stopSimulation();

        // Generate JSON
        generateJSON(newList);
        System.out.println("CloudSim Simulation Complete");
        System.out.println("JSON saved");
    }

    private static Datacenter createDatacenter(String name) throws Exception {
        List<Host> hostList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<Pe> peList = new ArrayList<>();
            peList.add(new Pe(0, new PeProvisionerSimple(3000)));
            int hostId = i;
            int ram = 2048;
            long storage = 1000000;
            int bw = 10000;
            Host host = new Host(hostId, new RamProvisionerSimple(ram),
                    new BwProvisionerSimple(bw), storage, peList,
                    new VmSchedulerTimeShared(peList));
            hostList.add(host);
        }
        String arch = "x86";
        String os = "Linux";
        String vmm = "Xen";
        double timeZone = 10.0;
        double cost = 3.0;
        double costPerMem = 0.05;
        double costPerStorage = 0.1;
        double costPerBw = 0.1;
        LinkedList<Storage> storageList = new LinkedList<>();
        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, timeZone, cost, costPerMem, costPerStorage, costPerBw);
        Datacenter datacenter = new Datacenter(name, characteristics,
                new VmAllocationPolicySimple(hostList), storageList, 0);
        return datacenter;
    }

    private static void generateJSON(List<Cloudlet> cloudlets) throws Exception {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"totalVMs\":").append(vmList.size()).append(",");
        json.append("\"totalHosts\":").append(3).append(",");
        json.append("\"totalCloudlets\":").append(cloudlets.size()).append(",");
        json.append("\"timestamp\":\"").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\",");
        json.append("\"cloudlets\":[");
        DecimalFormat df = new DecimalFormat("###.##");
        for (int i = 0; i < cloudlets.size(); i++) {
            Cloudlet c = cloudlets.get(i);
            json.append("{\"cloudletId\":").append(c.getCloudletId()).append(",");
            json.append("\"vmId\":").append(c.getVmId()).append(",");
            json.append("\"startTime\":").append(df.format(c.getExecStartTime())).append(",");
            json.append("\"finishTime\":").append(df.format(c.getFinishTime())).append(",");
            json.append("\"executionTime\":").append(df.format(c.getActualCPUTime())).append(",");
            json.append("\"length\":").append(c.getCloudletLength()).append("}");
            if (i < cloudlets.size() - 1) json.append(",");
        }
        json.append("]}");
        String path = System.getProperty("user.home") + "\\OneDrive\\Desktop\\cloudsim project\\simulation-data.json";
        java.nio.file.Files.write(java.nio.file.Paths.get(path), json.toString().getBytes());
    }
}