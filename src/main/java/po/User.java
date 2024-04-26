package po;

public class User {
    private String username;
    private String Sdcard;//身份证号
    private String nickname;
    private String password;
    private String phoneNumber;
    private String address;
    private String email;
    private String avatat;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatat() {
        return avatat;
    }

    public void setAvatat(String avatat) {
        this.avatat = avatat;
    }

    public User(){

    }
    public User(String username, String sdcard, String nickname, String password, String phoneNumber, String address, String email) {
        this.username = username;
        Sdcard = sdcard;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSdcard() {
        return Sdcard;
    }

    public void setSdcard(String sdcard) {
        Sdcard = sdcard;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", Sdcard='" + Sdcard + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
