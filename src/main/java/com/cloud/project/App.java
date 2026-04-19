package com.cloud.project;

import java.io.*;
import java.util.*;
import java.text.*;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.*;
import org.cloudbus.cloudsim.lists.*;
import org.cloudbus.cloudsim.provisioners.*;

public class App {
    private static final int[] VM_RAM = {512, 640, 768, 896, 1024};
    private static final int HOST_COUNT = 3;
    private static final int[] SCALABILITY_CLOUDLET_COUNTS = {10, 20, 30};

    private static class SimulationResult {
        int cloudletCount;
        List<Cloudlet> cloudlets;
        double makespan;
        double avgExecutionTime;
        double throughput;
    }

    public static void main(String[] args) throws Exception {
        List<SimulationResult> scalabilityResults = new ArrayList<>();
        SimulationResult baseResult = null;

        for (int cloudletCount : SCALABILITY_CLOUDLET_COUNTS) {
            SimulationResult result = runExperiment(cloudletCount);
            scalabilityResults.add(result);
            if (cloudletCount == 10) {
                baseResult = result;
            }
        }

        generateJSON(baseResult, scalabilityResults);
        System.out.println("CloudSim Simulation Complete");
        System.out.println("JSON saved");
    }

    private static SimulationResult runExperiment(int cloudletCount) throws Exception {
        int numUser = 1;
        Calendar calendar = Calendar.getInstance();
        boolean traceEvents = false;
        CloudSim.init(numUser, calendar, traceEvents);

        Datacenter datacenter0 = createDatacenter("Datacenter_" + cloudletCount);

        DatacenterBroker broker = new DatacenterBroker("Broker_" + cloudletCount);
        int brokerId = broker.getId();

        List<Vm> vmList = createVmList(brokerId);
        broker.submitVmList(vmList);

        List<Cloudlet> cloudletList = createCloudletList(brokerId, vmList.size(), cloudletCount);
        broker.submitCloudletList(cloudletList);

        CloudSim.startSimulation();
        List<Cloudlet> receivedCloudlets = broker.getCloudletReceivedList();
        CloudSim.stopSimulation();

        return buildSimulationResult(cloudletCount, receivedCloudlets);
    }

    private static List<Vm> createVmList(int brokerId) {
        List<Vm> vmList = new ArrayList<>();
        for (int i = 0; i < VM_RAM.length; i++) {
            int vmid = i;
            int mips = 1000;
            long size = 10000;
            int ram = VM_RAM[i];
            long bw = 1000;
            int pesNumber = 1;
            String vmm = "Xen";
            Vm vm = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm,
                    new CloudletSchedulerTimeShared());
            vmList.add(vm);
        }
        return vmList;
    }

    private static List<Cloudlet> createCloudletList(int brokerId, int vmCount, int cloudletCount) {
        List<Cloudlet> cloudletList = new ArrayList<>();
        for (int i = 0; i < cloudletCount; i++) {
            int cloudletId = i;
            long length = 40000L + i * 1000L;
            long fileSize = 300;
            long outputSize = 300;
            UtilizationModel utilizationModel = new UtilizationModelFull();
            Cloudlet cloudlet = new Cloudlet(cloudletId, length, 1, fileSize, outputSize,
                    utilizationModel, utilizationModel, utilizationModel);
            cloudlet.setUserId(brokerId);
            cloudlet.setVmId(i % vmCount);
            cloudletList.add(cloudlet);
        }
        return cloudletList;
    }

    private static SimulationResult buildSimulationResult(int cloudletCount, List<Cloudlet> cloudlets) {
        SimulationResult result = new SimulationResult();
        result.cloudletCount = cloudletCount;
        result.cloudlets = cloudlets;

        double totalExecutionTime = 0.0;
        double makespan = 0.0;
        for (Cloudlet cloudlet : cloudlets) {
            totalExecutionTime += cloudlet.getActualCPUTime();
            makespan = Math.max(makespan, cloudlet.getFinishTime());
        }

        result.makespan = makespan;
        result.avgExecutionTime = cloudlets.isEmpty() ? 0.0 : totalExecutionTime / cloudlets.size();
        result.throughput = makespan == 0.0 ? 0.0 : cloudlets.size() / makespan;
        return result;
    }

    private static Datacenter createDatacenter(String name) throws Exception {
        List<Host> hostList = new ArrayList<>();
        for (int i = 0; i < HOST_COUNT; i++) {
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

    private static void generateJSON(SimulationResult baseResult, List<SimulationResult> scalabilityResults) throws Exception {
        DecimalFormat df = new DecimalFormat("###.####", DecimalFormatSymbols.getInstance(Locale.US));
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"totalVMs\":").append(VM_RAM.length).append(",");
        json.append("\"totalHosts\":").append(HOST_COUNT).append(",");
        json.append("\"totalCloudlets\":").append(baseResult.cloudlets.size()).append(",");
        json.append("\"timestamp\":\"").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\",");
        json.append("\"cloudlets\":[");
        for (int i = 0; i < baseResult.cloudlets.size(); i++) {
            Cloudlet c = baseResult.cloudlets.get(i);
            json.append("{\"cloudletId\":").append(c.getCloudletId()).append(",");
            json.append("\"vmId\":").append(c.getVmId()).append(",");
            json.append("\"startTime\":").append(df.format(c.getExecStartTime())).append(",");
            json.append("\"finishTime\":").append(df.format(c.getFinishTime())).append(",");
            json.append("\"executionTime\":").append(df.format(c.getActualCPUTime())).append(",");
            json.append("\"length\":").append(c.getCloudletLength()).append("}");
            if (i < baseResult.cloudlets.size() - 1) json.append(",");
        }

        json.append("],");
        json.append("\"scalability\":[");
        for (int i = 0; i < scalabilityResults.size(); i++) {
            SimulationResult result = scalabilityResults.get(i);
            json.append("{\"cloudlets\":").append(result.cloudletCount).append(",");
            json.append("\"makespan\":").append(df.format(result.makespan)).append(",");
            json.append("\"avgTime\":").append(df.format(result.avgExecutionTime)).append(",");
            json.append("\"throughput\":").append(df.format(result.throughput)).append("}");
            if (i < scalabilityResults.size() - 1) {
                json.append(",");
            }
        }
        json.append("]}");

        String path = System.getProperty("user.home") + "\\OneDrive\\Desktop\\cloudsim project\\simulation-data.json";
        java.nio.file.Files.write(java.nio.file.Paths.get(path), json.toString().getBytes());
    }
}