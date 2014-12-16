package m;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.x500.X500Principal;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import net.mobidom.bp.beans.Обращение;
import net.mobidom.bp.beans.ПодписьОбращения;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;

public class M {

  static Logger log = Logger.getLogger(M.class.getName());

  static CryptoProvider cryptoProvider;

  private static ПодписьОбращения verifySignature(Обращение request, ПодписьОбращения signature) throws Exception {
    try {
      byte[] data = createSigningData(request);
      X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(
          new ByteArrayInputStream(signature.getCertificate()));
      boolean verify = cryptoProvider.verifySignature(certificate, new ByteArrayInputStream(data), reverse(signature.getSignature()));
      signature.setSignatureValid(verify);
    } catch (Exception e) {
      log.log(Level.WARNING, "unable to verify signature", e);
      signature.setSignatureValid(false);
    }

    return signature;
  }

  private static byte[] createSigningData(Обращение request) throws UnsupportedEncodingException {
    String dataStr = request.getНомер();
    if (request.getФизическоеЛицо() != null) {
      dataStr += request.getФизическоеЛицо().getФио().toFullString();
    } else if (request.getЮридическоеЛицо() != null) {
      dataStr += request.getЮридическоеЛицо().getНазвание().replace('"', '\'');
    }

    dataStr += request.getУслуга();

    log.info("данные для проверки подписи: " + dataStr);

    return dataStr.getBytes("UTF-8");
  }

  private static byte[] reverse(byte[] data) {
    byte[] revData = new byte[data.length];
    for (int i = 0; i < data.length; i++) {
      revData[i] = data[data.length - 1 - i];
    }
    return revData;
  }

  private static void fillInfoString(ПодписьОбращения sign) {

    StringBuilder ownerInfo = new StringBuilder();

    try {

      X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(
          new ByteArrayInputStream(sign.getCertificate()));

      Properties props = new Properties();

      props.load(new ByteArrayInputStream(certificate.getSubjectX500Principal().getName(X500Principal.RFC1779).replace(',', '\n')
          .getBytes("UTF-8")));

      if (props.containsKey("CN")) {
        ownerInfo.append("Владелец: ").append(props.get("CN")).append("\n");
      }

      if (props.containsKey("O")) {
        ownerInfo.append("Организация: ").append(props.get("O")).append("\n");
      }

      if (props.containsKey("OU")) {
        ownerInfo.append("Отдел: ").append(props.get("OU")).append("\n");
      }

      if (props.containsKey("OID.1.2.840.113549.1.9.1")) {
        ownerInfo.append("Эл.почта: ").append(props.get("OID.1.2.840.113549.1.9.1")).append("\n");
      }

      ownerInfo.append("Действует до: ").append(new SimpleDateFormat("dd.MM.yyyy").format(certificate.getNotAfter()));

    } catch (Exception e) {
      log.log(Level.WARNING, "unable to read X500Principal info");
    }

    sign.setOwnerInfo(ownerInfo.toString());
  }

  public static void main(String[] args) throws Exception {

    cryptoProvider = new CryptoProvider();

    JAXBContext jaxbContext = JAXBContext.newInstance(Обращение.class);
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    JAXBElement<Обращение> element = (JAXBElement<Обращение>) unmarshaller.unmarshal(new File("C:/work/siu/_test_documents/50/50.xml"));
    Обращение request = element.getValue();

    jaxbContext = JAXBContext.newInstance(ПодписьОбращения.class);
    unmarshaller = jaxbContext.createUnmarshaller();
    ПодписьОбращения signature = (ПодписьОбращения) unmarshaller.unmarshal(new File("C:/work/siu/_test_documents/50/50.signature"));

    verifySignature(request, signature);

    fillInfoString(signature);

    System.out.println("====================");
    System.out.println(signature.getOwnerInfo());
    System.out.println("====================");

  }
}
