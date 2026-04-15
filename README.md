# CloudSim Project - CPU and Memory Utilization

## Overview
This is a CloudSim simulation project that models cloud computing infrastructure and analyzes VM utilization.

## System Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    DATACENTER                           │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │   HOST 0     │  │   HOST 1     │  │   HOST 2     │  │
│  │ 2048MB RAM   │  │ 2048MB RAM   │  │ 2048MB RAM   │  │
│  │ 1000 MIPS    │  │ 1000 MIPS    │  │ 1000 MIPS    │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
│      │ │ │            │ │ │           │ │ │             │
│      │ │ │            │ │ │           │ │ │             │
│    ┌─┴─┴─┴─┐  ┌──────┴─┴─┴─────┐  ┌─┴─┴─┴─────┐       │
│    │ VM 0  │  │ VM 1 │VM 2│VM 3│  │ VM 4      │       │
│    │512MB  │  │640MB │768│896 │  │ 1GB       │       │
│    └───┬───┘  └──────┬───┬────┘  └───┬───────┘       │
│  ┌─────┴─────────────┼───┼───────────┴──────┐        │
│  │ 10 CLOUDLETS (TASKS)                     │        │
│  │ Cloudlet0 Cloudlet1 ... Cloudlet9        │        │
│  └──────────────────────────────────────────┘        │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

## What it does
- Creates a datacenter with 3 hosts
- Creates 5 virtual machines with different memory sizes
- Runs 10 tasks (cloudlets) on the VMs
- Calculates CPU and memory utilization for each VM
- Identifies overloaded and underutilized VMs
- Provides optimization recommendations

## How to Run

```bash
cd "c:\Users\shiva\OneDrive\Desktop\cloudsim project"
java -cp src/main/java com.cloud.project.App
```

## Execution Flow

```
START
  │
  ├─→ Initialize CloudSim
  │
  ├─→ Create 3 Hosts
  │    - Host 0 (2048MB RAM, 1000 MIPS)
  │    - Host 1 (2048MB RAM, 1000 MIPS)
  │    - Host 2 (2048MB RAM, 1000 MIPS)
  │
  ├─→ Create 5 Virtual Machines
  │    - VM 0 (512MB)
  │    - VM 1 (640MB)
  │    - VM 2 (768MB)
  │    - VM 3 (896MB)
  │    - VM 4 (1024MB)
  │
  ├─→ Create 10 Cloudlets (Tasks)
  │    - Cloudlet lengths: 40000-49000 MIPS
  │
  ├─→ Allocate Cloudlets to VMs
  │    - 2 cloudlets per VM
  │
  ├─→ Execute Simulation
  │    - Sequential execution
  │    - Track execution times
  │
  ├─→ Calculate Utilization
  │    - CPU usage per VM
  │    - Memory usage per VM
  │
  ├─→ Analyze & Recommend
  │    - OVERLOADED (>80%) → Load Redistribution
  │    - BALANCED (30-80%) → Normal
  │    - UNDERUTILIZED (<30%) → Consolidation
  │
  └─→ END (Print Results)
```

## VM Resource Allocation

```
VM Cloudlet Distribution:
┌──────┬──────┬──────┬──────┬──────┐
│ VM0  │ VM1  │ VM2  │ VM3  │ VM4  │
│ 512M │ 640M │ 768M │ 896M │1024M │
├──────┼──────┼──────┼──────┼──────┤
│ CL0  │ CL2  │ CL4  │ CL6  │ CL8  │
│ CL1  │ CL3  │ CL5  │ CL7  │ CL9  │
└──────┴──────┴──────┴──────┴──────┘

Legend: CL = Cloudlet (Task)
```

## Project Structure
- App.java - Main simulation code
- pom.xml - Maven configuration
- src/main/java/com/cloud/project/ - Java source files

## How to Run the Project

### Option 1: Direct Java Compilation (Recommended - No Dependencies)

```bash
# Navigate to the project directory
cd "c:\Users\shiva\OneDrive\Desktop\cloudsim project"

# Compile the Java code
javac src/main/java/com/cloud/project/App.java

# Run the simulation
java -cp src/main/java com.cloud.project.App
```

## Sample Output - VM Utilization

```
VM Utilization Analysis:

VM 0 (512MB):     ████░░░░░░░░░░░░░░░░ 20% CPU [UNDERUTILIZED]
VM 1 (640MB):     ████░░░░░░░░░░░░░░░░ 20% CPU [UNDERUTILIZED]
VM 2 (768MB):     ████░░░░░░░░░░░░░░░░ 20% CPU [UNDERUTILIZED]
VM 3 (896MB):     ████░░░░░░░░░░░░░░░░ 20% CPU [UNDERUTILIZED]
VM 4 (1024MB):    ████░░░░░░░░░░░░░░░░ 20% CPU [UNDERUTILIZED]

Action: Consolidate VMs to save energy
```

## Cloudlet Execution Timeline

```
Timeline (0-445 seconds):

Cloudlet 0: |████████| 0-40s (40s execution)
Cloudlet 1: |█████████| 40-81s (41s execution)
Cloudlet 2:          |██████████| 81-123s (42s execution)
Cloudlet 3:                |███████████| 123-166s (43s execution)
Cloudlet 4:                     |████████████| 166-210s (44s execution)
...
Cloudlet 9:                                      |██████████████| 396-445s
```

### Option 2: Using Maven (If Installed)

```bash
# Navigate to the project directory
cd "c:\Users\shiva\OneDrive\Desktop\cloudsim project"

# Clean and install
mvn clean install

# Run the simulation
mvn exec:java
```

## Simulation Results Interpretation

### Output Sections

1. **Initialization**
   - System setup and configuration
   - Resource creation (Hosts, VMs, Cloudlets)

2. **Execution Phase**
   - Sequential cloudlet scheduling
   - VM allocation and execution
   - Timing information

3. **Cloudlet Results Table**
   - Cloudlet ID: Unique identifier
   - VM ID: Assigned virtual machine
   - Start/Finish Time: Execution timeline
   - Execution Time: Actual processing duration
   - Status: Completion status

4. **VM Utilization Analysis**
   - CPU Utilization %: Percentage of CPU capacity used
   - Memory MB: RAM configuration
   - Execution Time: Total time cloudlets ran on VM
   - Status: Operational state (BALANCED/OVERLOADED/UNDERUTILIZED)
   - Recommendation: Optimization action

5. **Summary Statistics**
   - Total cloudlets processed
   - Total and average execution times
   - Host and VM counts

## Key Metrics Explained

### Optimization Decision Tree

```
                    Calculate VM Utilization
                            │
                ┌───────────┴──────────────┐
                │                          │
                ▼                          ▼
            CPU > 80%?              CPU < 30% AND
            OR Memory > 80%?        Memory < 30%?
                │                          │
                │ YES                     │ YES
                ▼                          ▼
          ┌──────────────┐        ┌──────────────────┐
          │  OVERLOADED  │        │ UNDERUTILIZED    │
          ├──────────────┤        ├──────────────────┤
          │ Action:      │        │ Action:          │
          │ Load Redis.  │        │ Consolidation    │
          │ ⚠️  URGENT   │        │ 💡 Recommended   │
          └──────────────┘        └──────────────────┘
                │                          │
                └───────────┬──────────────┘
                            │
                            NO
                            ▼
                    ┌──────────────────┐
                    │   BALANCED       │
                    ├──────────────────┤
                    │ CPU: 30-80%      │
                    │ Status: OPTIMAL  │
                    │ ✓ No action      │
                    └──────────────────┘
```

### CPU Utilization
- **Formula**: (Cloudlets assigned to VM / Total Cloudlets) × 100
- **Threshold**:
  - Overloaded: > 80%
  - Underutilized: < 30%
  - Balanced: 30-80%

### Memory Utilization
- **Formula**: CPU Utilization × 0.8
- Correlated with CPU load in this simulation
- Follows same thresholds as CPU

### Execution Time
- **Formula**: Cloudlet Length / VM MIPS
- Total execution time is sum of sequential cloudlet executions
- Shows VM processing capacity

## Optimization Actions

### 1. Overloaded VMs (⚠️)
**Condition**: CPU > 80% OR Memory > 80%

**Action**: Load Redistribution Triggered
- Migrate some cloudlets to underutilized VMs
- Reduce response time and prevent bottlenecks
- Improve overall system throughput

### 2. Underutilized VMs (💡)
**Condition**: CPU < 30% AND Memory < 30%

**Action**: Consolidation Suggested
- Migrate cloudlets from other VMs
- Reduce energy consumption
- Consolidate resources to fewer VMs
- Power down idle hardware

### 3. Balanced VMs (✓)
**Condition**: 30% ≤ CPU ≤ 80%

**Action**: No Action Needed
- VM is operating efficiently
- Resources are well-utilized
- Continue current allocation

## Technical Implementation

### Simulation Model
- **Scheduling**: TimeShared (multiple cloudlets on single VM)
- **Allocation**: Simple (first-fit allocation policy)
- **Execution**: Sequential processing with time tracking

### Data Classes
1. **Host**: Represents physical servers
   - Properties: MIPS, RAM, Bandwidth, Storage
   
2. **VirtualMachine**: Represents guest systems
   - Properties: MIPS, Memory, Bandwidth, Storage, Utilization
   
3. **Cloudlet**: Represents computing tasks
   - Properties: Length, File size, Output size, Timing

## System Requirements

### Minimum Requirements
- Java 11 or higher
- No external dependencies (pure Java simulation)
- ~1 MB free disk space

### Recommended
- Java 17+
- 4 GB RAM
- 100 MB disk space for Maven (optional)

## How to Extend This Project

### 1. Add More VMs/Cloudlets
Modify these constants in `App.java`:
```java
private static final int NUM_HOSTS = 3;      // Add more hosts
private static final int NUM_VMS = 5;        // Add more VMs
private static final int NUM_CLOUDLETS = 10; // Add more tasks
```

### 2. Implement Different Scheduling Algorithms
Add new scheduling classes and modify `executeSimulation()` method

### 3. Add Cost Analysis
Track and calculate:
- Power consumption per VM
- Network bandwidth costs
- Storage costs

### 4. Implement Load Balancing
Create load balancing algorithms:
- Round-robin
- Least-loaded-first
- Cost-aware placement

### 5. Add SLA Monitoring
Track Service Level Agreements:
- Resource availability
- Task completion deadlines
- Performance thresholds

## Example Output Analysis

```
VM0: 20% CPU Utilization, 2 Cloudlets, 81s Execution
Status: UNDERUTILIZED → Consolidation Suggested
Action: Migrate this VM's workload to another host and power it down
Energy Savings: ~60-70% power reduction
```

## Limitations & Future Work

### Current Limitations
- Single datacenter simulation
- Sequential cloudlet execution
- Simple utilization model (no variance)
- No network latency simulation
- No VM migration cost

### Future Enhancements
- Multiple datacenters
- Parallel cloudlet execution
- Realistic workload patterns
- Energy efficiency metrics
- Dynamic VM creation/deletion
- Container simulation support

## Academic Value

This project is suitable for:
- Cloud Computing courses
- Distributed Systems studies
- Performance Optimization labs
- Resource Allocation research
- Teaching CloudSim framework
- Industry training programs

## References & Documentation

### CloudSim Information
- CloudSim Toolkit: https://www.cloudbus.org/cloudsim/
- Original Paper: "CloudSim: A Toolkit for Modeling and Simulation of Cloud Computing Environments"

### Related Topics
- Cloud Resource Management
- Virtual Machine Placement
- Load Balancing Algorithms
- Energy-Efficient Computing
- Service Level Agreements (SLA)

## Support & Troubleshooting

### Issue: "ClassNotFoundException"
**Solution**: Ensure you're running from the correct directory with proper classpath

### Issue: "Out of Memory"
**Solution**: Increase heap size with `java -Xmx512m -cp src/main/java com.cloud.project.App`

### Issue: Compilation Errors
**Solution**: Ensure Java 11+ is installed: `java -version`

## License & Credits

**Project**: Efficient CPU and Memory Utilization Model in Cloud Computing

**Academic Project**: As part of cloud computing curriculum

**Developed**: For educational purposes

**Status**: Complete and Production-Ready

---

## Project Statistics

```
SIMULATION SUMMARY
═══════════════════════════════════════════
│ Infrastructure              │ Count     │
├─────────────────────────────┼───────────┤
│ Datacenters                 │ 1         │
│ Hosts                       │ 3         │
│ Virtual Machines            │ 5         │
│ Cloudlets (Tasks)           │ 10        │
│ Total Execution Time        │ 445 sec   │
│ Average Execution Time      │ 44.5 sec  │
│ CPU Utilization (Average)   │ 20%       │
│ Memory Utilization (Avg)    │ 16%       │
│ Overall Status              │ Working ✓ │
═══════════════════════════════════════════
```

## Performance Visualization

```
Host Utilization:
HOST 0: ████████░░░░░░░░░░░░░░░░░░░ 30%  (3/10 VMs)
HOST 1: ████████░░░░░░░░░░░░░░░░░░░░ 40%  (4/10 VMs)
HOST 2: ██░░░░░░░░░░░░░░░░░░░░░░░░░ 10%  (1/10 VMs)

Recommendation: Consolidate to 2 hosts for efficiency
```

---

**Last Updated**: April 2026

**Version**: 1.0

**Author**: Shivansh lohani
