package po;

public class UserEnterprise {
    String username;
    String enterprise_group_name;
    String role;
    String personal_fund;

    public UserEnterprise() {
    }

    public UserEnterprise(String username, String enterprise_group_name, String role, String personal_fund) {
        this.username = username;
        this.enterprise_group_name = enterprise_group_name;
        this.role = role;
        this.personal_fund = personal_fund;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEnterprise_group_name() {
        return enterprise_group_name;
    }

    public void setEnterprise_group_name(String enterprise_group_name) {
        this.enterprise_group_name = enterprise_group_name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPersonal_fund() {
        return personal_fund;
    }

    public void setPersonal_fund(String personal_fund) {
        this.personal_fund = personal_fund;
    }
}
