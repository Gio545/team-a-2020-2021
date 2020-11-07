package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.util.*;

@Autonomous(name = "Auto Op Mode")
public class AutoOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // make sure to angle robot right - curve will be fixed at a later date
        // TODO for hardware team

        double MS_PER_INCHES = 1000/77.0;
        double POWER = 1;

        MecanumDrive drive = MecanumDrive.standard(hardwareMap);
        for (DcMotor motor: drive.getMotors()) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        DcMotor shooter = hardwareMap.dcMotor.get("shooter");
        CRServo shooterServo = hardwareMap.crservo.get("pinball");

        DistanceSensor bottomSensor = hardwareMap.get(DistanceSensor.class, "bottom_sensor");
        DistanceSensor topSensor = hardwareMap.get(DistanceSensor.class, "top_sensor");
        int wobbleGoalTarget = 0; // 0 = A, 1 = B, 2 = C

        // POSSIBLE WOBBLE DROP
        WobbleGoal wobbleGoal = WobbleGoal.standard(hardwareMap);

        //Start wobble servos upon init
        /* arm.setPower(-1);
        grabber.setPower(1); */

        wobbleGoal.liftArm();

        waitForStart();

        shooter.setPower(-1);
        drive.forwardWithPower(POWER);
        sleep((long)(37 * MS_PER_INCHES));
        drive.stop();

        double bottom = bottomSensor.getDistance(DistanceUnit.INCH);
        double top = topSensor.getDistance(DistanceUnit.INCH);

                telemetry.addData("b", bottom);
                telemetry.addData("t", top);
                telemetry.update();

        if (bottom < 11.3) {
            wobbleGoalTarget = 1;
        }
        if (top < 14) {
            wobbleGoalTarget = 2;
        }

        // Movey move
        shooter.setPower(-1);
        drive.forwardWithPower(POWER);
        sleep((long)(33 * MS_PER_INCHES));
        drive.stop();

        // Shooty shoot
        for (int i = 0; i < 3; ++i) {
            // Thwack thwack thwack
            sleep(400);
            shooterServo.setPower(1);
            sleep(200);
            shooterServo.setPower(-1);
            sleep(200);
            shooterServo.setPower(0);
            //sleep(300);
        }

        shooter.setPower(0);

        // Parky park
        drive.forwardWithPower(POWER);
        if (wobbleGoalTarget == 0) {
            sleep((long)(26 * MS_PER_INCHES));
        } else if (wobbleGoalTarget == 1) {
            sleep((long)(57 * MS_PER_INCHES));
        } else if (wobbleGoalTarget == 2) {
            sleep((long)(90 * MS_PER_INCHES));
        }
        drive.stop();
        if (wobbleGoalTarget == 1) {
            drive.strafeLeftWithPower(POWER);
            sleep((long)(30 * MS_PER_INCHES));
            drive.stop();
        } else {
            drive.strafeRightWithPower(POWER);
            sleep((long)(40 * MS_PER_INCHES));
            drive.stop();
        }

        wobbleGoal.lowerArm();
        sleep(500);
        wobbleGoal.stopArm();
        wobbleGoal.release();
        sleep(200);
        wobbleGoal.stopGrabber();
        wobbleGoal.liftArm();
        sleep(500);
        wobbleGoal.stopArm();

        if (wobbleGoalTarget == 0) {
            drive.forwardWithPower(-POWER);
            sleep((long)(14 * MS_PER_INCHES));
            drive.strafeRightWithPower(-POWER);
            sleep((long)(12 * MS_PER_INCHES));
            drive.stop();
        } else if (wobbleGoalTarget == 1) {
            drive.forwardWithPower(-POWER);
            sleep((long)(35 * MS_PER_INCHES));
            drive.strafeRightWithPower(POWER);
            sleep((long)(30 * MS_PER_INCHES));
            drive.stop();
        } else if (wobbleGoalTarget == 2) {
            drive.forwardWithPower(-POWER);
            sleep((long)(40 * MS_PER_INCHES));
            drive.strafeRightWithPower(-POWER);
            sleep((long)(40 * MS_PER_INCHES));
            drive.stop();
        }
    }
}