package jpql;

public class MemberDTO {

    private String usename;
    private int age;

    public String getUsename() {
        return usename;
    }

    public MemberDTO(String usename, int age) {
        this.usename = usename;
        this.age = age;
    }

    public void setUsename(String usename) {
        this.usename = usename;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
