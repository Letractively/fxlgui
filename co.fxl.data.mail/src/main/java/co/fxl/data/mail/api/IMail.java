package co.fxl.data.mail.api;

public interface IMail {

	IMail addTo(String email);

	IMail addCC(String email);

	IMail subject(String subject);

	IMail text(String text);

	IMail send();

}
