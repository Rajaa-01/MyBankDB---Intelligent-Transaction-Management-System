package models.clients;

public record ClientRecord(
        Long id,
        String name,
        String email
) {

    public ClientRecord(String name, String email) {
        this(null, name, email);
    }

    @Override
    public String toString() {
        return "Client{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }
}