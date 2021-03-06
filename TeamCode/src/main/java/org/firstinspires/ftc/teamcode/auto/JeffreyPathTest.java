package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryConfig;
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryConfigManager;
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryGroupConfig;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.io.IOException;

@Autonomous(name = "Jeffrey Path Test Op Mode")
public class JeffreyPathTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        String filename = "Jeffrey.yaml";
        Trajectory traj;

        try {
            TrajectoryConfig config = TrajectoryConfigManager.loadConfig(hardwareMap.appContext.getAssets().open(filename));
            TrajectoryGroupConfig groupConfig = TrajectoryConfigManager.loadGroupConfig(hardwareMap.appContext.getAssets().open("_group.yaml"));
            traj = config.toTrajectory(groupConfig);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load trajectory: " + e.getMessage());
        }

        drive.followTrajectory(traj);
    }
}
