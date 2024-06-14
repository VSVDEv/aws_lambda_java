package helloworld;

public class Member {

    private long id;
    private String fullName;
    private double balance;

    public Member(){}

    public Member(long id, String fullName, double balance) {
        this.id = id;
        this.fullName = fullName;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;

        Member member = (Member) object;

        if (id != member.id) return false;
        if (java.lang.Double.compare(member.balance, balance) != 0) return false;
        if (fullName != null ? !fullName.equals(member.fullName) : member.fullName != null) return false;

        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        temp = Double.doubleToLongBits(balance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Member{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", balance=" + balance +
                '}';
    }
}