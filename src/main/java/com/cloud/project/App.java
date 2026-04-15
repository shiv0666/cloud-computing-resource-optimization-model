package com.cloud.project;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * CloudSim Project - CPU and Memory Utilization Model
 * 
 * Simulates cloud computing:
 * - Creates hosts, VMs, and tasks
 * - Measures CPU and memory usage
 * - Identifies overloaded/underutilized VMs
 * - Suggests optimization actions
 */

// Data classes for simulation
class Host {
    int hostId;
    int mips;
    int ram;
    long bandwidth;
    long storage;
    
    Host(int hostId, int mips, int ram, long bandwidth, long storage) {
        this.hostId = hostId;
        this.mips = mips;
        this.ram = ram;
        this.bandwidth = bandwidth;
        this.storage = storage;
    }
}

class VirtualMachine {
    int vmId;
    int mips;
    long memory;
    long bandwidth;
    long storage;
    double cpuUtilization;
    double memoryUtilization;
    
    VirtualMachine(int vmId, int mips, long memory, long bandwidth, long storage) {
        this.vmId = vmId;
        this.mips = mips;
        this.memory = memory;
        this.bandwidth = bandwidth;
        this.storage = storage;
        this.cpuUtilization = 0.0;
        this.memoryUtilization = 0.0;
    }
}

class Cloudlet {
    int cloudletId;
    int vmId;
    long length;
    long fileSize;
    long outputSize;
    double startTime;
    double finishTime;
    double executionTime;
    
    Cloudlet(int cloudletId, long length, long fileSize, long outputSize) {
        this.cloudletId = cloudletId;
        this.vmId = -1;
        this.length = length;
        this.fileSize = fileSize;
        this.outputSize = outputSize;
        this.startTime = 0;
        this.finishTime = 0;
        this.executionTime = 0;
    }
}

public class App {
    
    // List to store created VMs
    private static List<VirtualMachine> vmList;
    
    // List to store created Cloudlets
    private static List<Cloudlet> cloudletList;
    
    // List to store Hosts
    private static List<Host> hostList;
    
    // Number of hosts
    private static final int NUM_HOSTS = 3;
    
    // Number of VMs
    private static final int NUM_VMS = 5;
    
    // Number of Cloudlets
    private static final int NUM_CLOUDLETS = 10;
    
    // Random number generator
    private static Random random = new Random(42);

    public static void main(String[] args) {
        try {
            printHeader();

            // Initialize simulation
            println(">>> Initializing CloudSim Simulation...");
            println(">>> Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            println(">>> Number of simulation users: 1\n");

            // Create hosts
            createHosts();
            println(">>> " + NUM_HOSTS + " Hosts created successfully\n");

            // Create VMs
            createVirtualMachines();
            println(">>> " + NUM_VMS + " Virtual Machines created successfully\n");

            // Create Cloudlets
            createCloudlets();
            println(">>> " + NUM_CLOUDLETS + " Cloudlets created successfully\n");

            // Allocate cloudlets to VMs
            allocateCloudletsToVMs();
            println(">>> Cloudlets allocated to VMs\n");

            // Execute simulation
            println("--- Starting CloudSim Simulation ---\n");
            List<Cloudlet> completedCloudlets = executeSimulation();
            println(">>> CloudSim simulation completed\n");

            // Print results
            printSimulationResults(completedCloudlets);

        } catch (Exception e) {
            println("The simulation has been terminated due to an error!");
            e.printStackTrace();
        }
    }

    /**
     * Print simulation header
     */
    private static void printHeader() {
        println("========================================");
        println("  CloudSim: Efficient CPU and Memory");
        println("     Utilization Model Project");
        println("========================================\n");
    }

    /**
     * Create hosts for the datacenter
     */
    private static void createHosts() {
        hostList = new ArrayList<>();
        
        println("--- Creating Hosts ---");
        for (int i = 0; i < NUM_HOSTS; i++) {
            int mips = 1000;
            int ram = 2048;
            long bandwidth = 10000L;
            long storage = 1000000L;
            
            Host host = new Host(i, mips, ram, bandwidth, storage);
            hostList.add(host);
            
            println("  Host " + i + " created: MIPS=" + mips + ", RAM=" + ram + "MB, " +
                   "Bandwidth=" + bandwidth + "Mbps, Storage=" + storage + "MB");
        }
    }

    /**
     * Create Virtual Machines (VMs)
     */
    private static void createVirtualMachines() {
        vmList = new ArrayList<>();
        
        println("--- Creating Virtual Machines ---");
        for (int i = 0; i < NUM_VMS; i++) {
            int mips = 1000;
            long memory = 512 + (i * 128);
            long bandwidth = 1000L;
            long storage = 10000L;
            
            VirtualMachine vm = new VirtualMachine(i, mips, memory, bandwidth, storage);
            vmList.add(vm);
            
            println("  VM " + i + " created: MIPS=" + mips + ", RAM=" + memory +
                   "MB, Bandwidth=" + bandwidth + "Mbps");
        }
    }

    /**
     * Create Cloudlets (tasks)
     */
    private static void createCloudlets() {
        cloudletList = new ArrayList<>();
        
        println("--- Creating Cloudlets ---");
        for (int i = 0; i < NUM_CLOUDLETS; i++) {
            long length = 40000 + (i * 1000);
            long fileSize = 300;
            long outputSize = 300;
            
            Cloudlet cloudlet = new Cloudlet(i, length, fileSize, outputSize);
            cloudletList.add(cloudlet);
            
            println("  Cloudlet " + i + " created: Length=" + length + " MIPS");
        }
    }

    /**
     * Allocate cloudlets to VMs
     */
    private static void allocateCloudletsToVMs() {
        println("--- Allocating Cloudlets to VMs ---");
        int cloudletIndex = 0;
        
        for (int i = 0; i < NUM_VMS; i++) {
            for (int j = 0; j < 2; j++) {
                if (cloudletIndex < NUM_CLOUDLETS) {
                    cloudletList.get(cloudletIndex).vmId = i;
                    println("  Cloudlet " + cloudletIndex + " → VM " + i);
                    cloudletIndex++;
                }
            }
        }
    }

    /**
     * Execute simulation
     */
    private static List<Cloudlet> executeSimulation() {
        println("--- Executing Cloudlets ---\n");
        
        double simulationTime = 0.0;
        int[] vmCloudletCount = new int[NUM_VMS];
        double[] vmTotalExecTime = new double[NUM_VMS];
        
        for (Cloudlet cloudlet : cloudletList) {
            int vmId = cloudlet.vmId;
            VirtualMachine vm = vmList.get(vmId);
            
            // Calculate execution time: length / MIPS
            cloudlet.executionTime = (double) cloudlet.length / vm.mips;
            cloudlet.startTime = simulationTime;
            cloudlet.finishTime = simulationTime + cloudlet.executionTime;
            
            // Update simulation time
            simulationTime += cloudlet.executionTime;
            
            vmCloudletCount[vmId]++;
            vmTotalExecTime[vmId] += cloudlet.executionTime;
            
            // Simulate CPU and Memory utilization
            vm.cpuUtilization = (vmCloudletCount[vmId] / (double) NUM_CLOUDLETS) * 100;
            vm.memoryUtilization = vm.cpuUtilization * 0.8;
        }
        
        return cloudletList;
    }

    /**
     * Print simulation results
     */
    private static void printSimulationResults(List<Cloudlet> completedCloudlets) {
        println("\n" + "=".repeat(90));
        println("    SIMULATION RESULTS AND ANALYSIS");
        println("=".repeat(90) + "\n");

        // Print detailed cloudlet information
        println(String.format("%-10s %-10s %-15s %-15s %-15s %-15s %-15s",
                "Cloudlet", "VM ID", "Start Time", "Finish Time", "Exec Time", "Status", "Info"));
        println(String.format("%-10s %-10s %-15s %-15s %-15s %-15s %-15s",
                "--------", "------", "----------", "-----------", "---------", "------", "----"));

        double totalExecutionTime = 0;
        DecimalFormat dft = new DecimalFormat("###.##");

        for (Cloudlet cloudlet : completedCloudlets) {
            println(String.format("%-10d %-10d %-15s %-15s %-15s %-15s %-15s",
                    cloudlet.cloudletId,
                    cloudlet.vmId,
                    dft.format(cloudlet.startTime),
                    dft.format(cloudlet.finishTime),
                    dft.format(cloudlet.executionTime),
                    "SUCCESS",
                    dft.format(cloudlet.length) + " MIPS"));

            totalExecutionTime += cloudlet.executionTime;
        }

        println("\n");

        // VM Utilization Analysis
        println("=".repeat(90));
        println("    VM UTILIZATION ANALYSIS & OPTIMIZATION");
        println("=".repeat(90) + "\n");

        analyzeVmUtilization(completedCloudlets);

        println("\n");
        println("=".repeat(90));
        println("    SUMMARY STATISTICS");
        println("=".repeat(90));
        println("Total Cloudlets: " + completedCloudlets.size());
        println("Total Execution Time: " + dft.format(totalExecutionTime) + " seconds");
        println("Average Execution Time: " + dft.format(totalExecutionTime / completedCloudlets.size()) + " seconds");
        println("Total Hosts: " + hostList.size());
        println("Total VMs: " + vmList.size());
        println("\n" + "=".repeat(90));
        println("  SIMULATION COMPLETED SUCCESSFULLY!");
        println("=".repeat(90) + "\n");

        // Print run instructions
        printRunInstructions();
    }

    /**
     * Analyze VM utilization
     */
    private static void analyzeVmUtilization(List<Cloudlet> cloudletList) {
        DecimalFormat dft = new DecimalFormat("###.##");
        
        // Track utilization per VM
        int[] vmCloudletCount = new int[NUM_VMS];
        double[] vmExecutionTime = new double[NUM_VMS];

        // Calculate VM utilization
        for (Cloudlet cloudlet : cloudletList) {
            int vmId = cloudlet.vmId;
            if (vmId >= 0 && vmId < NUM_VMS) {
                vmCloudletCount[vmId]++;
                vmExecutionTime[vmId] += cloudlet.executionTime;
            }
        }

        // Print VM utilization report
        println(String.format("%-8s %-12s %-15s %-15s %-25s",
                "VM ID", "CPU Util %", "Memory MB", "Exec Time", "Status & Recommendation"));
        println(String.format("%-8s %-12s %-15s %-15s %-25s",
                "-----", "---------", "---------", "---------", "-----"));

        for (int i = 0; i < NUM_VMS; i++) {
            long memoryMB = 512 + (i * 128);
            
            // Calculate CPU utilization based on cloudlet load
            double cpuUtilization = (vmCloudletCount[i] / (double) NUM_CLOUDLETS) * 100;
            
            // Calculate memory utilization
            double memoryUtilization = cpuUtilization * 0.8;

            // Determine VM status
            String status = "";
            String recommendation = "";

            if (cpuUtilization > 80 || memoryUtilization > 80) {
                status = "OVERLOADED";
                recommendation = "Load Redistribution Triggered";
            } else if (cpuUtilization < 30 && memoryUtilization < 30) {
                status = "UNDERUTILIZED";
                recommendation = "Consolidation Suggested";
            } else {
                status = "BALANCED";
                recommendation = "Optimal";
            }

            println(String.format("%-8d %-12s %-15d %-15s %-25s",
                    i,
                    dft.format(cpuUtilization) + "%",
                    memoryMB,
                    dft.format(vmExecutionTime[i]) + "s",
                    status + " → " + recommendation));

            // Print detailed message for each VM
            println("  │ VM " + i + " Analysis: ");
            println("  │   - CPU Utilization: " + dft.format(cpuUtilization) + "%");
            println("  │   - Memory Utilization: " + dft.format(memoryUtilization) + "%");
            println("  │   - Cloudlets Executed: " + vmCloudletCount[i]);
            println("  │   - Total Execution Time: " + dft.format(vmExecutionTime[i]) + "s");
            println("  │   - Host Allocation: Host " + (i % NUM_HOSTS));
            println("  │   - STATUS: " + status);
            
            if (cpuUtilization > 80 || memoryUtilization > 80) {
                println("  └─━ ACTION: VM Overloaded → Load Redistribution Triggered ⚠️");
            } else if (cpuUtilization < 30 && memoryUtilization < 30) {
                println("  └─━ ACTION: VM Underutilized → Consolidation Suggested 💡");
            } else {
                println("  └─━ ACTION: VM Balanced → No Action Needed ✓");
            }
            println("");
        }
    }

    /**
     * Print run instructions
     */
    private static void printRunInstructions() {
        println("=".repeat(50));
        println("         HOW TO RUN THIS PROJECT");
        println("=".repeat(50));
        println("\n1. Navigate to the project directory:");
        println("   cd \"c:\\Users\\shiva\\OneDrive\\Desktop\\cloudsim project\"\n");

        println("2. Compile the Java code:");
        println("   javac src/main/java/com/cloud/project/App.java\n");

        println("3. Run the simulation:");
        println("   java -cp src/main/java com.cloud.project.App\n");

        println("4. Alternative - With Maven (if installed):");
        println("   mvn clean install && mvn exec:java\n");

        println("=".repeat(50));
        println("    Project completed successfully!");
        println("=".repeat(50));
    }

    /**
     * Utility method to print with newline
     */
    private static void println(String message) {
        System.out.println(message);
    }
}
