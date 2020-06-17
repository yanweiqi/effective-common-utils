package com.effective.common.base.enums;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 10:53 AM
 */
public enum PayrollBySwitch {

    Monday,
    Tuesday,
    Wendsday,
    Thursday,
    Friday,
    Saturday,
    Sunday;

    private static final int FIXED_WORK_HOUR_PER_DAY = 8;

    private static final double OVER_TIME_PAY_RATE = 1/2;

    public double pay(double workHours, double payPerHour){

        double basePay = workHours  * payPerHour;

        double overTimePay = 0;
        switch(this){
            case Saturday: case Sunday:
                overTimePay = workHours * payPerHour * OVER_TIME_PAY_RATE;
                break;
            default:
                overTimePay = workHours <= FIXED_WORK_HOUR_PER_DAY ?
                        0 : workHours - FIXED_WORK_HOUR_PER_DAY;
                break;

        }
        return basePay+overTimePay;
    }

}
