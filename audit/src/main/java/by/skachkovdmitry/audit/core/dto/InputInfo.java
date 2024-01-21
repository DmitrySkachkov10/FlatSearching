package by.skachkovdmitry.audit.core.dto;


@lombok.Getter
@lombok.Setter
@lombok.EqualsAndHashCode
public class InputInfo {


    private User user;

    private String essenceType;

    private String text;

    private String id;

    public InputInfo() {
    }

    public InputInfo(User user, String essenceType, String text, String id) {
        this.user = user;
        this.essenceType = essenceType;
        this.text = text;
        this.id = id;
    }

}
