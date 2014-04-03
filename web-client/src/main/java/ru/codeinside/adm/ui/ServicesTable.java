package ru.codeinside.adm.ui;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import org.glassfish.embeddable.GlassFishException;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.webui.Configurator;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.gws.TRef;
import ru.codeinside.gses.webui.osgi.LogCustomizer;
import ru.codeinside.gses.webui.osgi.TRefRegistryImpl;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.Server;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

final public class ServicesTable extends FilterTable {

  final static String FS_PREFIX = "file:" + System.getProperty("com.sun.aas.instanceRoot") + "/";
  final static String GF_PREFIX = "reference:" + FS_PREFIX + "applications/";


  ServicesTable() {
    super("Активные модули");

    addContainerProperty("name", String.class, null);
    addContainerProperty("symbolicName", String.class, null);
    addContainerProperty("version", String.class, null);
    addContainerProperty("revision", Revision.class, null);
    addContainerProperty("location", String.class, null);
    addContainerProperty("log", CheckBox.class, null);
    addContainerProperty("undeploy", Button.class, null);

    setVisibleColumns(new String[]{
      "name", "symbolicName", "version", "revision", "location", "log", "undeploy"
    });

    setColumnHeaders(new String[]{
      "Компонент", "Название", "Вер.", "Рев.", "Модуль", "Журнал", ""
    });

    setPageLength(0);
    setSelectable(false); // нет действий с выделением
    setSizeFull();
    setSortContainerPropertyId("name");
    setFilterBarVisible(true);
    setFilterDecorator(new FilterDecorator_());
    addGeneratedColumn("log", new ColumnGenerator() {
      @Override
      public Object generateCell(CustomTable source, Object itemId, Object columnId) {
        return null;
      }
    });
  }

  void reload() {
    removeAllItems();
    boolean serverLogEnabled = LogCustomizer.isServerLogEnabled().equals(Boolean.TRUE);
    List<TRef<Server>> serverRefs = TRefRegistryImpl.getServerRefs();
    int i = 0;
    for (final TRef<Server> ref : serverRefs) {
      if (ref.getRef() != null) {

        final String name = ref.getName();
        String componentName;
        String symbolicName = ref.getSymbolicName();

        if (symbolicName.equals(name)) {
          componentName = null;
        } else {
          componentName = name;
        }

        String version = ref.getVersion();

        String originalLocation = ref.getLocation();
        String location;
        if (originalLocation.startsWith(GF_PREFIX)) {
          location = originalLocation.substring(GF_PREFIX.length(), originalLocation.length() - 1);
        } else if (originalLocation.startsWith(FS_PREFIX)) {
          int slash = originalLocation.lastIndexOf(File.separatorChar);
          location = originalLocation.substring(slash + 1);
        } else {
          location = originalLocation;
        }

        Revision revision = ref.getRef().getRevision();

        CheckBox checkBox = new CheckBox();
        checkBox.setImmediate(true);
        checkBox.setValue(LogCustomizer.isServerLogEnabled(name));
        checkBox.addListener(new LogAction(name));
        checkBox.setReadOnly(!serverLogEnabled);

        Button unDeploy = new Button("Удалить", new UndeployAction(originalLocation));

        addItem(new Object[]{componentName, symbolicName, version, revision, location, checkBox, unDeploy}, i++);
      }
    }
    sort();
  }

  final static class LogAction implements ValueChangeListener {
    final String name;

    public LogAction(String name) {
      this.name = name;
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
      LogCustomizer.setServerLogEnabled(name, Boolean.TRUE == event.getProperty().getValue());
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
      if (location.startsWith(GF_PREFIX)) {
        try {
          String[] split = location.split("/");
          String name = split[split.length - 1];
          AdminServiceProvider.get().createLog(Flash.getActor(), "bundle", name, "undeploy", null, true);
          Configurator.getDeployer().undeploy(name);
        } catch (GlassFishException e) {
          // почти все ошибки валются лишь в консоль!
          getWindow().showNotification(e.getMessage(), Window.Notification.TYPE_HUMANIZED_MESSAGE);
        }
      } else if (location.startsWith(FS_PREFIX)) {
        File bundle = new File(location.substring(5));
        AdminServiceProvider.get().createLog(Flash.getActor(), "bundle", bundle.getName(), "undeploy", null, true);
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
