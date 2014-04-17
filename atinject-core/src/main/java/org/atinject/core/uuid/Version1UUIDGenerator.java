package org.atinject.core.uuid;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

// this class has been taken from http://johannburkard.de/software/uuid/
// version 3.4
@ApplicationScoped
public class Version1UUIDGenerator extends AbstractUUIDGenerator {

    private AtomicLong lastTime;

    private long clockSeqAndNode;

    @PostConstruct
    public void initialize() {
        lastTime = new AtomicLong(Long.MIN_VALUE);
        String macAddress = new HardwareAddressLookup().toString();
        clockSeqAndNode = 0x8000000000000000L;
        clockSeqAndNode |= Hex.parseLong(macAddress);
        clockSeqAndNode |= (long) (Math.random() * 0x3FFF) << 48;
    }

    @Override
    public UUID get() {
        return new UUID(newTime(), clockSeqAndNode);
    }
    
    private long newTime() {
        return createTime(System.currentTimeMillis());
    }

    private long createTime(long currentTimeMillis) {
        // UTC time
        long timeMillis = (currentTimeMillis * 10000) + 0x01B21DD213814000L;

        while (true) {
            long current = lastTime.get();
            if (timeMillis > current) {
                if (lastTime.compareAndSet(current, timeMillis)) {
                    break;
                }
            } else {
                if (lastTime.compareAndSet(current, current + 1)) {
                    timeMillis = current + 1;
                    break;
                }
            }
        }

        long time;
        
        // time low
        time = timeMillis << 32;

        // time mid
        time |= (timeMillis & 0xFFFF00000000L) >> 16;

        // time hi and version
        time |= 0x1000 | ((timeMillis >> 48) & 0x0FFF); // version 1

        return time;
    }

    /**
     * Scans MAC addresses for good ones.
     */
    private static class HardwareAddressLookup {

        @Override
        public String toString() {
            String out = null;
            try {
                Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
                if (ifs != null) {
                    while (ifs.hasMoreElements()) {
                        NetworkInterface iface = ifs.nextElement();
                        byte[] hardware = iface.getHardwareAddress();
                        if (hardware != null && hardware.length == 6
                                && hardware[1] != (byte) 0xff) {
                            out = Hex.append(new StringBuilder(36), hardware).toString();
                            break;
                        }
                    }
                }
            }
            catch (SocketException ex) {
                // Ignore it.
            }
            return out;
        }

    }

    public static final class Hex {

        private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
                'f' };

        /**
         * Turns a <code>byte</code> array into hex octets.
         *
         * @param a the {@link Appendable}, may not be <code>null</code>
         * @param bytes the <code>byte</code> array
         * @return {@link Appendable}
         */
        public static Appendable append(Appendable a, byte[] bytes) {
            try {
                for (byte b : bytes) {
                    a.append(DIGITS[(byte) ((b & 0xF0) >> 4)]);
                    a.append(DIGITS[(byte) (b & 0x0F)]);
                }
            }
            catch (IOException ex) {
                // Bla
            }
            return a;
        }

        /**
         * Parses a <code>long</code> from a hex encoded number. This method will skip all characters that are not 0-9,
         * A-F and a-f.
         * <p>
         * Returns 0 if the {@link CharSequence} does not contain any interesting characters.
         *
         * @param s the {@link CharSequence} to extract a <code>long</code> from, may not be <code>null</code>
         * @return a <code>long</code>
         * @throws NullPointerException if the {@link CharSequence} is <code>null</code>
         */
        public static long parseLong(CharSequence s) {
            long out = 0;
            byte shifts = 0;
            char c;
            for (int i = 0; i < s.length() && shifts < 16; i++) {
                c = s.charAt(i);
                if ((c > 47) && (c < 58)) {
                    ++shifts;
                    out <<= 4;
                    out |= c - 48;
                }
                else if ((c > 64) && (c < 71)) {
                    ++shifts;
                    out <<= 4;
                    out |= c - 55;
                }
                else if ((c > 96) && (c < 103)) {
                    ++shifts;
                    out <<= 4;
                    out |= c - 87;
                }
            }
            return out;
        }

    }

}
