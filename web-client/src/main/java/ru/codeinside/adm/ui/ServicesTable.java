package ru.codeinside.adm.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import org.glassfish.embeddable.GlassFishException;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.webui.Configurator;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.gws.TRef;
import ru.codeinside.gses.webui.osgi.TRefRegistryImpl;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.Server;

import java.io.File;
import java.net.URL;
import java.util.List;

final public class ServicesTable extends Table {

  final static String FS_PREFIX = "file:" + System.getProperty("com.sun.aas.instanceRoot") + "/";
  final static String GF_PREFIX = "reference:" + FS_PREFIX + "applications/";


  ServicesTable() {
    super("Активные модули");

    addContainerProperty("name", String.class, "");
    addContainerProperty("symbolicName", String.class, "");
    addContainerProperty("version", String.class, "");
    addContainerProperty("location", String.class, "");
    addContainerProperty("revision", Revision.class, "");
    addContainerProperty("wsdlUrl", String.class, "");
    addContainerProperty("undeploy", Component.class, "");
    setVisibleColumns(new String[]{"name", "symbolicName", "version", "location", "revision", "wsdlUrl", "undeploy"});
    setColumnHeaders(new String[]{"Компонент", "Название", "Вер.", "Модуль", "Рев.", "WSDL", ""});

    reload();

    setPageLength(0);
    setSelectable(true);
    setSizeFull();
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    if (visible) {
      reload();
    }
  }

  void reload() {
    removeAllItems();
    List<TRef<Server>> serverRefs = TRefRegistryImpl.getServerRefs();
    int i = 0;
    for (final TRef<Server> ref : serverRefs) {
      if (ref.getRef() != null) {

        String componentName = ref.getName();
        String symbolicName = ref.getSymbolicName();

        if (symbolicName.equals(componentName)) {
          componentName = null;
        }

        String version = ref.getVersion();

        String originalLocation = ref.getLocation();
        String location;
        if (originalLocation.startsWith(GF_PREFIX)) {
          location = originalLocation.substring(GF_PREFIX.length());
        } else if (originalLocation.startsWith(FS_PREFIX)) {
          location = originalLocation.substring(FS_PREFIX.length());
        } else {
          location = originalLocation;
        }

        Revision revision = ref.getRef().getRevision();

        URL wsdlUrl = ref.getRef().getWsdlUrl();
        String wsdl = wsdlUrl != null ? wsdlUrl.toString() : "";

        Component unDeploy = new Button("Удалить", new UndeployAction(originalLocation));

        addItem(new Object[]{componentName, symbolicName, version, location, revision, wsdl, unDeploy}, i++);
      }
    }
  }


  // Работает лишь для активированных через deploy!
  final class UndeployAction implements Button.ClickListener {

    private static final long serialVersionUID = 1L;

    final String location;

    public UndeployAction(String location) {
      this.location = location;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
      AdminServiceProvider.get().createLog(Flash.getActor(), "bundle", location, "undeploy", null, true);
      if (location.startsWith(GF_PREFIX)) {
        try {
          String[] split = location.split("/");
          String name = split[split.length - 1];
          Configurator.getDeployer().undeploy(name);
        } catch (GlassFishException e) {
          // почти все ошибки валются лишь в консоль!
          getWindow().showNotification(e.getMessage(), Window.Notification.TYPE_HUMANIZED_MESSAGE);
        }
      } else if (location.startsWith(FS_PREFIX)) {
        File bundle = new File(location.substring(5));
        if (bundle.exists()) {
          bundle.delete();
          try {
            Thread.sleep(5100); // время реакции fileInstall
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
          }
        }
      }
      reload();
    }
  }
}
