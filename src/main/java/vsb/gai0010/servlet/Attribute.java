package vsb.gai0010.servlet;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Attribute {
    LOGIN("login"), PASSWORD("password"), ERROR("error"), USER("user"), CLOTH("cloth");

    private final String attribute;

    @Override
    public String toString() {
        return attribute;
    }
}
