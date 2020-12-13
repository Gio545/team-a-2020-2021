package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryConfig;
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryConfigManager;
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryGroupConfig;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.io.IOException;

@Autonomous(name = "Path Test Op Moded")
public class PathTestOpMode extends LinearOpMode {
    private Trajectory loadTrajectory(String name, TrajectoryGroupConfig groupConfig) {
        String filename = "trajectory/" + name + ".yaml";
        try {
            TrajectoryConfig config = TrajectoryConfigManager.loadConfig(hardwareMap.appContext.getAssets().open(filename));
            return config.toTrajectory(groupConfig);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load trajectory: " + e.getMessage());
        }
    }

    private TrajectoryGroupConfig loadGroupConfig() {
        try {
            return TrajectoryConfigManager.loadGroupConfig(hardwareMap.appContext.getAssets().open("trajectory/_group.yaml"));
        } catch (IOException e) {
            throw new RuntimeException("Unable to load trajectory: " + e.getMessage());
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        DistanceSensor bottomSensor = hardwareMap.get(DistanceSensor.class, "bottom_sensor");
        DistanceSensor topSensor = hardwareMap.get(DistanceSensor.class, "top_sensor");
        int wobbleGoalTarget = 0; // 0 = A, 1 = B, 2 = C

        DcMotor shooter = hardwareMap.dcMotor.get("shooter");
        CRServo shooterServo = hardwareMap.crservo.get("pinball");

        TrajectoryGroupConfig groupConfig = loadGroupConfig();
        Trajectory preScan = loadTrajectory("redprescan", groupConfig);
        Trajectory shoot = loadTrajectory("redshoot", groupConfig);

        waitForStart();

        if (isStopRequested()) return;

        drive.setPoseEstimate(preScan.start());

        shooter.setPower(-1);

        drive.followTrajectory(preScan);

        double bottom = bottomSensor.getDistance(DistanceUnit.INCH);
        double top = topSensor.getDistance(DistanceUnit.INCH);
        if ((5 >= top && top >= 1) && (5 >= bottom && bottom >= 1)) {
            wobbleGoalTarget = 2;
        }
        else if ((8 >= bottom && bottom >= 4) && (top > 5)) {
            wobbleGoalTarget = 1;
        }

        drive.followTrajectory(shoot);

        // Shooty shoot
        for (int i = 0; i < 3; ++i) {
            // Thwack thwack thwack
            // sleep(200);
            shooterServo.setPower(1);
            sleep(220);
            shooterServo.setPower(-1);
            sleep(200);
            shooterServo.setPower(0);
            //sleep(300);
        }

        shooter.setPower(0);
    }
}
