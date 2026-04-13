import java.util.*;

abstract class Member {
    protected String id;
    protected String nama;
    protected int saldo;

    public Member(String id, String nama) {
        this.id = id;
        this.nama = nama;
        this.saldo = 0;
    }

    public void topUp(int jumlah) {
        this.saldo += jumlah;
    }

    public int getSaldo() {
        return saldo;
    }

    public String getInfo() {
        return id + " | " + nama + " | " + getType() + " | saldo: " + saldo;
    }

    protected abstract String getType();

    public abstract int hitungPembayaran(int hargaDasar, int sesi);
}

class Reguler extends Member {
    public Reguler(String id, String nama) {
        super(id, nama);
    }

    @Override
    protected String getType() {
        return "REGULER";
    }

    @Override
    public int hitungPembayaran(int hargaDasar, int sesi) {
        int total = hargaDasar * sesi;
        if (sesi > 5) {
            total = total - (int) (total * 0.10);
        }
        int pajak = (int) (total * 0.05);
        total = total + pajak;
        return Math.max(total, 0);
    }
}

class VIP extends Member {
    public VIP (String id, String nama) {
        super (id, nama);
    }

    @Override
    protected String getType() {
        return "VIP";
    }

    @Override
    public int hitungPembayaran(int hargaDasar, int sesi) {
        int total = hargaDasar * sesi;
        if (sesi > 5) {
            total = total - (int) (total * 0.10);
        }
        total = total - (int) (total * 0.15);
        int pajak = (int) (total * 0.05);
        total = total + pajak;
        return Math.max(total, 0);
    }
}

class GymSystem {
    private List<Member> members = new ArrayList<>();

    public void addMember(String tipe, String id, String nama) {
        for (Member m : members) {
            if (m.id.equals(id)) {
                System.out.println("Member sudah terdaftar");
                return;
            }
        }
        if (tipe.equals("REGULER")) {
            members.add(new Reguler(id, nama));
        } else {
            members.add(new VIP(id, nama));
        }
        System.out.println(tipe + " " + id + " berhasil ditambahkan");
    }

    public void topUp(String id, int jumlah) {
        Member m = findMember(id);
        if (m == null) {
            System.out.println("Member tidak ditemukan");
            return;
        }
        
        m.topUp(jumlah);
        System.out.println("Saldo " + id + ": " + m.getSaldo());
    }

    public void buy(String id, String layanan, int sesi) {
        Member m = findMember(id);
        if (m == null) {
            System.out.println("Member tidak ditemukan");
            return;
        }

        int hargaDasar;
        if (layanan.equals("cardio")) {
            hargaDasar = 20000;
        } else if (layanan.equals("yoga")) {
            hargaDasar = 25000;
        } else if (layanan.equals("personal_training")) {
            hargaDasar = 40000;
        } else {
            System.out.println("Layanan tidak valid");
            return;
        }

        int total = m.hitungPembayaran(hargaDasar, sesi);

        if (m.getSaldo() < total) {
            System.out.println("Saldo " + id + " tidak cukup");
            return;
        }

        m.saldo -= total;
        System.out.println("Total bayar " + id + ": " + total);
        System.out.println("Saldo " + id + ": " + m.getSaldo());
    }

    public void check(String id) {
        Member m = findMember(id);
        if (m == null) {
            System.out.println("Member tidak ditemukan");
            return;
        }
        System.out.println(m.getInfo());
    }

    public int count() {
        return members.size();
    }

    public Member findMember(String id) {
        for (Member m : members) {
            if (m.id.equals(id)) return m;
        }
        return null;
    }
}

public class UTPGYM {
    public static void main(String[] args) {
        Scanner jaed = new Scanner(System.in);
        GymSystem gym = new GymSystem();
        int n = Integer.parseInt(jaed.nextLine().trim());

        for (int i = 0; i < n; i++) {
            String line = jaed.nextLine().trim();
            String[] part = line.split(" ");

            String utp = part[0];

            if (utp.equals("ADD")) {
                gym.addMember(part[1], part[2], part[3]);
            } else if (utp.equals("TOPUP")) {
                gym.topUp(part[1], Integer.parseInt(part[2]));
            } else if (utp.equals("BUY")) {
                gym.buy(part[1], part[2], Integer.parseInt(part[3]));
            } else if (utp.equals("CHECK")) {
                gym.check(part[1]);
            } else if (utp.equals("COUNT")) {
                System.out.println("Total member: " + gym.count());
            }
        }

        jaed.close();
    }
}