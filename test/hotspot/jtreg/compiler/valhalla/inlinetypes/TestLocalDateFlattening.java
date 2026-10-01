package compiler.valhalla.inlinetypes;

import jdk.internal.vm.annotation.NullRestricted;

public class TestLocalDateFlattening {

    // A value class with more than one field, fitting in 64 bits
    // year: 2 bytes, month: 1 byte, day: 1 byte -> total 4 bytes
    static value class MyLocalDate {
        short year;
        byte month;
        byte day;

        public MyLocalDate(short year, byte month, byte day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }
    }

    static class Container {
        @NullRestricted
        MyLocalDate date;
        MyLocalDate nullableDate;

        public Container() {
            this.date = new MyLocalDate((short) 2026, (byte) 4, (byte) 24);
            super();
        }
    }

    public static void main(String[] args) {
        Container c = new Container();
        System.out.println("Container created");
    }
}
