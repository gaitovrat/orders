package vsb.gai0010.servlet;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LoginAttribute {
    LOGIN("login"), PASSWORD("password"), ERROR("error"), USER("user");

    private final String attribute;

    @Override
    public String toString() {
        return attribute;
    }
}