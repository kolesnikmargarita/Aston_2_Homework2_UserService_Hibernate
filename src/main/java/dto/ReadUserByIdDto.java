package dto;

public class ReadUserByIdDto {

    private Long id;

    public ReadUserByIdDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
