package com.effective.common.base.enums;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 10:58 AM
 */
public enum PayrollByEnumStrategy {
    Monday(PayType.WeekDay),
    Tuesday(PayType.WeekDay),
    Wendsday(PayType.WeekDay),
    Thursday(PayType.WeekDay),
    Friday(PayType.WeekDay),
    Saturday(PayType.WeekEnd),
    Sunday(PayType.WeekEnd);

    PayType payType;

    private PayrollByEnumStrategy(PayType payType) {
        this.payType = payType;
    }

    public double pay(double workHours, double payPerHour) {
        return payType.pay(workHours, payPerHour);
    }


    private enum PayType {

        WeekDay {
            @Override
            double overTimePay(double workHours, double payPerHour) {
                double overTimePay = workHours <= FIXED_WORK_HOUR_PER_DAY ? 0 : (workHours - FIXED_WORK_HOUR_PER_DAY)

                        * payPerHour * OVER_TIME_PAY_RATE;
                return overTimePay;
            }

        },
        WeekEnd {
            @Override
            double overTimePay(double workHours, double payPerHour) {
                double overTimePay = workHours * payPerHour * OVER_TIME_PAY_RATE;
                return overTimePay;
            }

        };
        private static final int FIXED_WORK_HOUR_PER_DAY = 8;
        private static final double OVER_TIME_PAY_RATE = 1 / 2;


        abstract double overTimePay(double workHours, double payPerHour);

        public double pay(double workHours, double payPerHour) {
            double basePay = workHours * payPerHour;
            double overTimePay = overTimePay(workHours, payPerHour);
            return basePay + overTimePay;
        }
    }

    public static void main(String[] args) {

        // TODO Auto-generated method stub
        //计算一周的工资总额——每天工作10小时，每小时400块。
        double sum = 0;
        for(PayrollBySwitch day : PayrollBySwitch.values()){
            sum += day.pay(10, 50);
        }

        System.out.println("工资总额 by Switch= ￥"+sum);


        sum = 0;
        for(PayrollBySwitch day : PayrollBySwitch.values()){
            sum += day.pay(10, 50);
        }

        System.out.println("工资总额 by Enum Strategy= ￥"+sum);

    }
}
