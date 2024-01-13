package by.skachkovdmitry.mailing_service.core.dto;


import java.util.Objects;

public class MailCodeDTO {

    private String code;

    private String mail;


    public MailCodeDTO() {
    }

    public MailCodeDTO(String code, String mail) {
        this.code = code;
        this.mail = mail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MailCodeDTO that)) return false;
        return Objects.equals(code, that.code) && Objects.equals(mail, that.mail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, mail);
    }
}
