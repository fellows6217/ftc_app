package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Josie Miner on 2/9/2018.
 */
@Autonomous(name="Preciousss: headingTest 17:53, 9/30/18", group="Preciousss")

public class headingTest extends superAuto {

    public void runOpMode() {
        setUp();
        fancyGyroPivot(90);
        Wait(4);
        fancyGyroPivot(-40);
        Wait(4);
        fancyGyroPivot(140);
        Wait(4);
        fancyGyroPivot(0);
        Wait(4);

    }
}