package compiler.valhalla.inlinetypes;

import jdk.internal.vm.annotation.NullRestricted;

public class TestAtomicityOps {

    // 1 field (4 bytes) -> Naturally Atomic
    static value class NatAtomic {
        int a;
        public NatAtomic(int a) { this.a = a; }
    }

    // 2 fields (4 bytes total) -> NOT Naturally Atomic
    static value class NotNatAtomic {
        short a;
        short b;
        public NotNatAtomic(short a, short b) { this.a = a; this.b = b; }
    }

    static class Container {
        @NullRestricted NatAtomic nat;
        @NullRestricted NotNatAtomic notNat;
        
        public Container() {
            this.nat = new NatAtomic(0);
            this.notNat = new NotNatAtomic((short)0, (short)0);
            super();
        }
    }

    static void writeNat(Container c, NatAtomic v) {
        c.nat = v;
    }

    static void writeNotNat(Container c, NotNatAtomic v) {
        c.notNat = v;
    }

    public static void main(String[] args) {
        Container c = new Container();
        NatAtomic nat = new NatAtomic(42);
        NotNatAtomic notNat = new NotNatAtomic((short)1, (short)2);
        
        // Warmup to trigger C2 compilation
        for (int i = 0; i < 20_000; i++) {
            writeNat(c, nat);
            writeNotNat(c, notNat);
        }
    }
}
