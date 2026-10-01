package compiler.valhalla.inlinetypes;

import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;

public class SanityCheck8346105 {

    @LooselyConsistentValue
    static value class Point4 {
        int a, b, c, d;
        Point4(int a, int b, int c, int d) { this.a=a; this.b=b; this.c=c; this.d=d; }
    }

    // Holder is itself a value class. `this.p = p` in its constructor
    // emits putfield -> line 2117 (the code path we want to exercise).
    @LooselyConsistentValue
    static value class Holder {
        @NullRestricted Point4 p;
        Holder(Point4 p) { this.p = p; }
    }

    // write path: constructor-driven putfield of a flat field
    static Holder build(Point4 v) { return new Holder(v); }

    // read path: getfield -> line 2059 (non-atomic flat read, materializes Point4)
    static Point4 read(Holder h) { return h.p; }

    public static void main(String[] args) {
        Point4 v = new Point4(1, 2, 3, 4);
        Holder h = new Holder(v);
        for (int i = 0; i < 20_000; i++) {
            Holder h2 = build(v);
            Point4 r = read(h2);
            if (r.a + r.b + r.c + r.d != 10) {
                throw new AssertionError(r.a + "," + r.b + "," + r.c + "," + r.d);
            }
        }
        System.out.println("OK");
    }
}