package by.skachkovdmitry.mailing_service.service.api;


public interface IMailService {
    void send();

    void send(String data, String mail);
}
