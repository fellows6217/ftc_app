
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


@Autonomous(name="Preciousss: AutoPivot", group="Preciousss")

/*
 * Created by Josie and Ben on 11/4/17.
 *
 */
public class AutoPivot extends LinearOpMode {

    //FR = Front Right, FL = Front Left, BR = Back Right, BL = Back Left.
    DcMotor motorFR;
    DcMotor motorFL;
    DcMotor motorBR;
    DcMotor motorBL;
    Servo servoTapper;
    CRServo servoConL;
    CRServo servoConR;
    DcMotor motorConL;
    DcMotor motorConR;
    BNO055IMU imu;

    Orientation angles;
    Acceleration gravity;
    NormalizedColorSensor colorSensor;
    boolean iAmBlue = false;
    boolean iAmRed = true;
    boolean isBoxSide = true;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // H a r d w a r e   M a p p i n g
        motorFR = hardwareMap.dcMotor.get("motorFR");
        motorFR.setDirection(DcMotor.Direction.REVERSE);
        motorFL = hardwareMap.dcMotor.get("motorFL");
        motorFL.setDirection(DcMotor.Direction.FORWARD);
        motorBL = hardwareMap.dcMotor.get("motorBL");
        motorBL.setDirection(DcMotor.Direction.FORWARD);
        motorBR = hardwareMap.dcMotor.get("motorBR");
        motorBR.setDirection(DcMotor.Direction.REVERSE);
        motorConL = hardwareMap.dcMotor.get("motorConL");
        motorConL.setDirection(DcMotor.Direction.FORWARD);
        motorConR = hardwareMap.dcMotor.get("motorConR");
        motorConR.setDirection(DcMotor.Direction.FORWARD);
        servoConL = hardwareMap.crservo.get("servoConL");
        servoConL.setDirection(CRServo.Direction.FORWARD);
        servoConR = hardwareMap.crservo.get("servoConR");
        servoConR.setDirection(CRServo.Direction.REVERSE);
        servoTapper = hardwareMap.servo.get("tapper");
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight) colorSensor).enableLight(true);
        }

        // S t a r t

        waitForStart();

        boolean autoClear = false;
        telemetry.setAutoClear(autoClear);
        telemetry.addLine("starting");
        telemetry.update();

       pivotTo(90);
    }
    void pivotTo (int target){
        //Pivot to counterclockwise is positive.
        //Pivot to clockwise is negative.
        double baseWheelPower = .35;
        double minWheelPower = .15;
        double wheelPower = baseWheelPower;
        float fudgeFactor = 5f;
        double dif = target;
        while(dif > fudgeFactor - minWheelPower) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles != null) {
                double currentHeading = angles.firstAngle;
                dif = (target - currentHeading);
                wheelPower = ( ( ( dif / target ) - minWheelPower ) * baseWheelPower ) + minWheelPower ;
                if (target - currentHeading > 0) {
                    motorFL.setPower(-wheelPower);
                    motorBL.setPower(-wheelPower);
                    motorFR.setPower(wheelPower);
                    motorBR.setPower(wheelPower);
                } else {
                    motorFL.setPower(wheelPower);
                    motorBL.setPower(wheelPower);
                    motorFR.setPower(-wheelPower);
                    motorBR.setPower(-wheelPower);
                }
            }
        }

        sR();

    }
    void pivotBy(int angle) {

        // Positive angle turns clockwise with power given

        // Any faster than this and the gyro is far less accurate
       /* float power = .25f;

        // The gyro tends to overestimate the angle
        float fudgeFactor = 0.97f;
        int initialHeading = 0;
        angle = Math.round(angle * fudgeFactor);
        if (angle < 0) {
            // Counterclockwise for negative angle
            power = -power;
            // Always need a positive angle for later comparison involving absolute value
            angle = -angle;
        } else {
            initialHeading = 360;
        }

        gyro.resetZAxisIntegrator();
        motorFL.setPower(-power);
        motorBL.setPower(-power);
        motorFR.setPower(power);
        motorBR.setPower(power);

        int curHeading = 0;
        int iCount = 0;
        while (curHeading < angle) {
            iCount = iCount + 1;
            //curHeading = Math.abs(angles.firstangle() - initialHeading);
            telemetry.addData("1", "%03d", curHeading);
            telemetry.addData("2", "%03d", gyro.getIntegratedZValue());
            telemetry.addData("3", "%03d", iCount);
            telemetry.update();
        }
        sR();*/
    }

    void move(float posx, float posy, float waitTime) {
        float FRBLPower = posy + posx;
        float FLBRPower = posy - posx;
        motorFR.setPower(FRBLPower);
        motorFL.setPower(FLBRPower);
        motorBR.setPower(FLBRPower);
        motorBL.setPower(FRBLPower);
        Wait(waitTime);
        sR();
    }

    void Conveyor (float waitTime)   {
        servoConL.setPower(1);
        servoConR.setPower(1);
        motorConL.setPower(1);
        motorConR.setPower(1);
        Wait(waitTime);


    }

    void pivotRight(float waitTime) {

        motorFL.setPower(-.5f);
        motorBL.setPower(-.5f);
        motorFR.setPower(.5f);
        motorBR.setPower(.5f);
        Wait(waitTime);
        sR();
    }

    void pivotLeft(float waitTime) {

        motorFL.setPower(.25f);
        motorBL.setPower(.25f);
        motorFR.setPower(-.25f);
        motorBR.setPower(-.25f);
        Wait(waitTime);
    }



    void letsGo(float posx, float posy) {
        float FRBLPower = posy - posx;
        float FLBRPower = posy + posx;
        motorFR.setPower(FRBLPower);
        motorFL.setPower(FLBRPower);
        motorBR.setPower(FLBRPower);
        motorBL.setPower(FRBLPower);
        Wait(0.1f);
    }

    void sR() {
        float power = 0.f;
        motorFL.setPower(power);
        motorBL.setPower(power);
        motorFR.setPower(power);
        motorBR.setPower(power);
    }

    void Wait(double WaitTime) {
        runtime.reset();
        while (runtime.seconds() < WaitTime) {
            //Comment this out to avoid it overwriting other telemetry
            //telemetry.addData("5", " %2.5f S Elapsed", runtime.seconds());
            //telemetry.update();
        }
    }

    double lightLevel(double odsVal) {
        /*
         * This method adjusts the light sensor input, read as an ODS,
         * to obtain values that are large enough to use to drive
         * the robot.  It does this by taking the third decimal values
         * and beyond as the working values.
         */
        // LB = left bumper, RB = right bumper.
        int prefix = (int) (odsVal * 10);
        odsVal = Math.pow(10, odsVal) * prefix;

        return (odsVal);
    }
}














